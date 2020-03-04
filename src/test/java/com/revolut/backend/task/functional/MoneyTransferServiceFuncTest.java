package com.revolut.backend.task.functional;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revolut.backend.task.ApplicationConfig;
import com.revolut.backend.task.dto.AccountTransferDTO;
import com.revolut.backend.task.entity.Account;
import org.eclipse.jetty.http.HttpStatus;
import org.glassfish.jersey.test.DeploymentContext;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.String.valueOf;
import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.client.Entity.json;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.eclipse.jetty.http.HttpStatus.NO_CONTENT_204;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

        assertEquals(950000L, saldo.longValue());
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

        assertEquals(NO_CONTENT_204, response.getStatus());
    }

    @Test
    public void transferMoneyToFuncTestOk() {
        Account account = createAccountAndGet("Test");

        IntStream.range(0, 1000).parallel().forEach(i -> {
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

        assertEquals(100000L, saldo.longValue());
    }

    @Test
    public void transferMoneyFromToFuncTest() {
        Account from = createAccountAndGet("from");
        Account to = createAccountAndGet("to");

        // Fill 'from' account
        IntStream.range(0, 1000).parallel().forEach(i -> {
            target("/transfer/to/" + from.getId())
                    .queryParam("amount", AMOUNT.longValue())
                    .request()
                    .post(json(null));
        });

        IntStream.range(0, 500).parallel().forEach(i -> {

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

        assertEquals(150000L, saldoFrom.longValue());
        assertEquals(0L, saldoTo.longValue());
    }

    @Test
    public void showStatementTest() {
        Account from = createAccountAndGet("from");
        Account to = createAccountAndGet("to");

        target("/transfer/to/" + from.getId())
                .queryParam("amount", AMOUNT.longValue())
                .request()
                .post(json(null));

        target("/transfer/from/{from}/to/{to}")
                .resolveTemplate("from", from.getId())
                .resolveTemplate("to", to.getId())
                .queryParam("amount", AMOUNT.longValue())
                .request()
                .post(json(null));

        Response response = target("/account/{id}/statement")
                .resolveTemplate("id", from.getId())
                .request()
                .get();

        assertEquals(HttpStatus.OK_200, response.getStatus());

        List<Map> entries = (List<Map>) response.readEntity(List.class);

        assertEquals(2, entries.size());
        AtomicBoolean filterFlag = new AtomicBoolean(false);
        entries.stream().filter(entry -> ((Map) entry.get("creditAccount")).get("id").equals(from.getId().toString()))
                .forEach(entry -> {
                    assertEquals(AMOUNT.doubleValue(), entry.get("amount"));
                    assertEquals(to.getId().toString(), ((Map) entry.get("debitAccount")).get("id"));
                    filterFlag.set(true);
                });
        assertTrue(filterFlag.get());
    }
}
