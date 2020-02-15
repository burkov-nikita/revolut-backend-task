package com.revolut.backend.task.controller;

import com.revolut.backend.task.ApplicationConfig;
import com.revolut.backend.task.entity.Account;
import org.glassfish.jersey.test.DeploymentContext;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import javax.ws.rs.core.Response;
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
    private static final String REQUEST_FAILED = "Request failed.";
    private Account account;

    @Override
    protected DeploymentContext configureDeployment() {
        return DeploymentContext.builder(ApplicationConfig.class).build();
    }

    @Test
    public void createAccountTestOk() {
        Account rawAccount = new Account(643L, METADATA);

        Response response = target("/account/create")
                .request()
                .post(entity(rawAccount, APPLICATION_JSON));

        assertEquals(200, response.getStatus());

        account = response.readEntity(Account.class);

        assertEquals(valueOf(643L), account.getCurrencyId());
        assertEquals(METADATA, account.getMetadata());
        assertEquals(new BigDecimal("0"), account.getSaldo());
        assertTrue(isNumeric(account.getNum()));
        assertEquals(account.getId().toString(), nameUUIDFromBytes(account.getNum().getBytes()).toString());
    }

    @Test
    public void createAccountTestWithoutCurrencyBad() {
        Account rawAccount = new Account();
        rawAccount.setMetadata(METADATA);

        Response response = target("/account/create")
                .request()
                .post(entity(rawAccount, APPLICATION_JSON));

        assertEquals(500, response.getStatus());
        assertEquals(REQUEST_FAILED, response.getStatusInfo().getReasonPhrase());
    }

    @Test
    public void createAccountWithDefinedPropertiesBad() {
        this.createAccountTestOk();
        Account account = this.account;

        account.setMetadata("TESTSTE");

        Response response = target("/account/create")
                .request()
                .post(entity(account, APPLICATION_JSON));

        assertEquals(500, response.getStatus());
        assertEquals(REQUEST_FAILED, response.getStatusInfo().getReasonPhrase());

    }

    @Test
    public void getAccountTestOk() {
        this.createAccountTestOk();

        Response response = target("/account/" + account.getId())
                .request()
                .get();

        assertEquals(200, response.getStatus());

        Account foundAccount = response.readEntity(Account.class);

        assertEquals(valueOf(643L), foundAccount.getCurrencyId());
        assertEquals(METADATA, foundAccount.getMetadata());
        assertEquals(new BigDecimal("0.00"), foundAccount.getSaldo());
        assertTrue(isNumeric(foundAccount.getNum()));
        assertEquals(foundAccount.getId().toString(), nameUUIDFromBytes(foundAccount.getNum().getBytes()).toString());
    }

    @Test
    public void removeAccountTestOk() {
        this.getAccountTestOk();

        Response deleteResponse = target("/account/" + account.getId())
                .request()
                .delete();

        assertEquals(204, deleteResponse.getStatus());

        Response getResponse = target("/account/" + account.getId())
                .request()
                .get();

        assertEquals(204, getResponse.getStatus());
    }

    @Test
    public void updateAccountTestOk() {
        this.createAccountTestOk();
        Account account = this.account;
        account.setMetadata("Test_Test");

        Response updateResponse = target("account/update/")
                .request()
                .post(entity(account, APPLICATION_JSON));

        assertEquals(204, updateResponse.getStatus());

        Response getResponse = target("/account/" + account.getId())
                .request()
                .get();

        assertEquals(200, getResponse.getStatus());

        assertEquals(valueOf(643L), account.getCurrencyId());
        assertEquals("Test_Test", account.getMetadata());
        assertEquals(new BigDecimal("0"), account.getSaldo());
        assertTrue(isNumeric(account.getNum()));
        assertEquals(account.getId().toString(), nameUUIDFromBytes(account.getNum().getBytes()).toString());
    }
}
