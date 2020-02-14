package com.revolut.backend.task.integration;

import com.revolut.backend.task.ApplicationConfig;
import com.revolut.backend.task.entity.Account;
import org.glassfish.jersey.test.DeploymentContext;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import java.math.BigDecimal;
import java.util.stream.IntStream;

import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.junit.Assert.assertEquals;

//@RunWith(ConcurrentTestRunner.class)
public class MoneyTransferServiceFuncTest extends JerseyTest {

    private static final BigDecimal AMOUNT = new BigDecimal(100);

    @Override
    protected DeploymentContext configureDeployment() {
        return DeploymentContext.builder(ApplicationConfig.class).build();
    }

    @Test
    public void transferMoneyToFuncTest() {
        Account rawAccount = new Account(643L, "Test");

        Account account = target("/account/create")
                .request()
                .post(entity(rawAccount, APPLICATION_JSON))
                .readEntity(Account.class);

        IntStream.range(0, 1000).forEach(i -> {
            target("/transfer/to/" + account.getId())
                    .queryParam("amount", AMOUNT.longValue())
                    .request()
                    .post(Entity.json(null));
        });

        BigDecimal saldo = target("/account/" + account.getId())
                .request()
                .get()
                .readEntity(Account.class)
                .getSaldo();

        assertEquals(saldo, new BigDecimal("100000.00"));
    }
}
