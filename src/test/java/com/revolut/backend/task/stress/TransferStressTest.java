package com.revolut.backend.task.stress;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revolut.backend.task.ApplicationConfig;
import com.revolut.backend.task.dto.AccountTransferDTO;
import com.revolut.backend.task.entity.Account;
import org.glassfish.jersey.test.DeploymentContext;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.String.valueOf;
import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.client.Entity.json;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.junit.Assert.assertEquals;

public class TransferStressTest extends JerseyTest {

    private static final BigDecimal AMOUNT = new BigDecimal(100);

    @Override
    protected DeploymentContext configureDeployment() {
        return DeploymentContext.builder(ApplicationConfig.class).build();
    }

    private Account createAccountAndGet(String metadata) {
        Account rawAccount = new Account(643L, metadata);

        return target("/account/create")
                .request()
                .post(entity(rawAccount, APPLICATION_JSON))
                .readEntity(Account.class);
    }

    @Test
    public void transferAllAndConcurrent() throws JsonProcessingException {
        Account happyAccount = createAccountAndGet("Happy account");

        target("/transfer/to/" + happyAccount.getId())
                .queryParam("amount", 1000)
                .request()
                .post(json(null));

        List<AccountTransferDTO> toHappyAccount = IntStream.range(0, 100)
                .mapToObj(i -> createAccountAndGet(valueOf(i)))
                .peek(account -> target("/transfer/to/" + account.getId())
                        .queryParam("amount", 100)
                        .request()
                        .post(json(null)))
                .map(account -> new AccountTransferDTO(account.getId(), happyAccount.getId(), new BigDecimal("1")))
                .collect(Collectors.toList());

        List<AccountTransferDTO> fromHappyAccount = IntStream.range(0, 100)
                .mapToObj(i -> createAccountAndGet(valueOf(i)))
                .map(account -> new AccountTransferDTO(happyAccount.getId(), account.getId(), new BigDecimal("1")))
                .collect(Collectors.toList());

        List<AccountTransferDTO> batch = new ArrayList<>(200);
        batch.addAll(toHappyAccount);
        batch.addAll(fromHappyAccount);
        Collections.shuffle(batch);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String postData = mapper.writeValueAsString(batch);

        IntStream.range(0, 100).parallel().forEach(i -> {
            target("/transfer/batch/")
                    .request()
                    .post(entity(postData, APPLICATION_JSON));

            target("/transfer/to/" + happyAccount.getId())
                    .queryParam("amount", AMOUNT.longValue())
                    .request()
                    .post(json(null));
        });

        BigDecimal saldo = target("/account/" + happyAccount.getId())
                .request()
                .get()
                .readEntity(Account.class)
                .getSaldo();

        assertEquals(11000L, saldo.longValue());

    }
}
