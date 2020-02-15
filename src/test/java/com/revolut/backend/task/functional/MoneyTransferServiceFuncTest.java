package com.revolut.backend.task.functional;

import com.revolut.backend.task.ApplicationConfig;
import com.revolut.backend.task.entity.Account;
import org.glassfish.jersey.test.DeploymentContext;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.stream.IntStream;

import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.client.Entity.json;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.junit.Assert.assertEquals;

//@RunWith(ConcurrentTestRunner.class)
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
    public void transferMoneyToFuncTest() {
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
