package com.revolut.backend.task.functional;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revolut.backend.task.ApplicationConfig;
import com.revolut.backend.task.dto.AccountTransferDTO;
import com.revolut.backend.task.entity.Account;
import com.revolut.backend.task.entity.AccountEntry;
import org.eclipse.jetty.http.HttpStatus;
import org.glassfish.jersey.test.DeploymentContext;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.String.valueOf;
import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.client.Entity.json;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.eclipse.jetty.http.HttpStatus.NO_CONTENT_204;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MoneyTransferServiceFuncTest extends JerseyTest {

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
    public void transferMoneyBatchTestOk() throws JsonProcessingException {
        Account happyAccount = createAccountAndGet("Happy account");

        target("/transfer/to/" + happyAccount.getId())
                .queryParam("amount", 1000000)
                .request()
                .post(json(null));

        List<AccountTransferDTO> toHappyAccount = IntStream.range(0, 1000)
                .mapToObj(i -> createAccountAndGet(valueOf(i)))
                .map(account -> new AccountTransferDTO(account.getId(), happyAccount.getId(), new BigDecimal("100")))
                .collect(Collectors.toList());

        List<AccountTransferDTO> fromHappyAccount = IntStream.range(0, 500)
                .mapToObj(i -> createAccountAndGet(valueOf(i)))
                .map(account -> new AccountTransferDTO(happyAccount.getId(), account.getId(), new BigDecimal("100")))
                .collect(Collectors.toList());

        List<AccountTransferDTO> batch = new ArrayList<>(2000);
        batch.addAll(toHappyAccount);
        batch.addAll(fromHappyAccount);
        Collections.shuffle(batch);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String postData = mapper.writeValueAsString(batch);

        target("/transfer/batch/")
                .request()
                .post(entity(postData, APPLICATION_JSON));

        BigDecimal saldo = target("/account/" + happyAccount.getId())
                .request()
                .get()
                .readEntity(Account.class)
                .getSaldo();

        assertEquals(saldo, new BigDecimal("950000.00"));
    }

    @Test
    public void transferMoneyToNoAccountFuncTestBad() {
        Response response = target("/transfer/to/" + UUID.randomUUID())
                .queryParam("amount", 1000000)
                .request()
                .post(json(null));

        assertEquals(NO_CONTENT_204, response.getStatus());
    }

    @Test
    public void transferMoneyFromToNoAccountFuncTest() {
        Response response = target("/transfer/from/{from}/to/{to}")
                .resolveTemplate("from", UUID.randomUUID())
                .resolveTemplate("to", UUID.randomUUID())
                .queryParam("amount", AMOUNT.longValue())
                .request()
                .post(json(null));

        assertEquals(HttpStatus.OK_200, response.getStatus());

        List<Map> result = (List<Map>)response.readEntity(List.class);
        assertEquals(1, result.size());
        result.forEach(map -> assertNull(map.get("id")));
    }

    @Test
    public void transferMoneyToFuncTestOk() {
        Account account = createAccountAndGet("Test");

        IntStream.range(0, 1000).forEach(i -> {
            target("/transfer/to/" + account.getId())
                    .queryParam("amount", AMOUNT.longValue())
                    .request()
                    .post(json(null));
        });

        BigDecimal saldo = target("/account/" + account.getId())
                .request()
                .get()
                .readEntity(Account.class)
                .getSaldo();

        assertEquals(saldo, new BigDecimal("100000.00"));
    }

    @Test
    public void transferMoneyFromToFuncTest() {
        Account from = createAccountAndGet("from");
        Account to = createAccountAndGet("to");

        // Fill 'from' account
        IntStream.range(0, 1000).forEach(i -> {
            target("/transfer/to/" + from.getId())
                    .queryParam("amount", AMOUNT.longValue())
                    .request()
                    .post(json(null));
        });

        IntStream.range(0, 500).forEach(i -> {

            // from-to
            target("/transfer/from/{from}/to/{to}")
                    .resolveTemplate("from", from.getId())
                    .resolveTemplate("to", to.getId())
                    .queryParam("amount", AMOUNT.longValue())
                    .request()
                    .post(json(null));

            // fill from
            target("/transfer/to/" + from.getId())
                    .queryParam("amount", AMOUNT.longValue())
                    .request()
                    .post(json(null));

            // to-from
            target("/transfer/from/{from}/to/{to}")
                    .resolveTemplate("from", to.getId())
                    .resolveTemplate("to", from.getId())
                    .queryParam("amount", AMOUNT.longValue())
                    .request()
                    .post(json(null));
        });

        BigDecimal saldoFrom = target("/account/" + from.getId())
                .request()
                .get()
                .readEntity(Account.class)
                .getSaldo();

        BigDecimal saldoTo = target("/account/" + to.getId())
                .request()
                .get()
                .readEntity(Account.class)
                .getSaldo();

        assertEquals(saldoFrom, new BigDecimal("150000.00"));
        assertEquals(saldoTo, new BigDecimal("0.00"));
    }
}
