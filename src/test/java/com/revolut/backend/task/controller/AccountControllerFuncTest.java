package com.revolut.backend.task.controller;

import com.revolut.backend.task.ApplicationConfig;
import com.revolut.backend.task.entity.Account;
import org.glassfish.jersey.test.DeploymentContext;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import java.math.BigDecimal;

import static java.lang.Long.valueOf;
import static java.util.UUID.nameUUIDFromBytes;
import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.apache.commons.lang3.StringUtils.isNumeric;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AccountControllerFuncTest extends JerseyTest {

    private static final String METADATA = "Test";
    private Account account;

    @Override
    protected DeploymentContext configureDeployment() {
        return DeploymentContext.builder(ApplicationConfig.class).build();
    }

    @Test
    public void createAccountTestOk() {
        Account rawAccount = new Account(643L, METADATA);

        account = target("/account/create")
                .request()
                .post(entity(rawAccount, APPLICATION_JSON))
                .readEntity(Account.class);

        assertEquals(valueOf(643L), account.getCurrencyId());
        assertEquals(METADATA, account.getMetadata());
        assertEquals(new BigDecimal("0"), account.getSaldo());
        assertTrue(isNumeric(account.getNum()));
        assertEquals(account.getId().toString(), nameUUIDFromBytes(account.getNum().getBytes()).toString());
    }

    @Test
    public void getAccountTestOk() {
        this.createAccountTestOk();

        Account foundAccount = target("/account/" + account.getId())
                .request()
                .get()
                .readEntity(Account.class);

        assertEquals(valueOf(643L), foundAccount.getCurrencyId());
        assertEquals(METADATA, foundAccount.getMetadata());
        assertEquals(new BigDecimal("0.00"), foundAccount.getSaldo());
        assertTrue(isNumeric(foundAccount.getNum()));
        assertEquals(foundAccount.getId().toString(), nameUUIDFromBytes(foundAccount.getNum().getBytes()).toString());
    }
}
