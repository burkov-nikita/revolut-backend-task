package com.revolut.backend.task.functional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revolut.backend.task.ApplicationConfig;
import com.revolut.backend.task.entity.Account;
import com.revolut.backend.task.exception.NotAllowedIdException;
import com.revolut.backend.task.exception.MissedCurrencyIdException;
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
import static org.eclipse.jetty.http.HttpStatus.*;
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

        Response response = target("/account/create")
                .request()
                .post(entity(rawAccount, APPLICATION_JSON));

        assertEquals(OK_200, response.getStatus());

        account = response.readEntity(Account.class);

        assertEquals(valueOf(643L), account.getCurrencyId());
        assertEquals(METADATA, account.getMetadata());
        assertEquals(0L, account.getSaldo().longValue());
        assertTrue(isNumeric(account.getNum()));
        assertEquals(account.getId().toString(), nameUUIDFromBytes(account.getNum().getBytes()).toString());
    }

    @Test
    public void createAccountWithStringCurrencyBad() throws JsonProcessingException {
        Account rawAccount = new Account(643L, METADATA);
        ObjectMapper mapper = new ObjectMapper();
        String postData = mapper.writeValueAsString(rawAccount);
        postData = postData.replace("643", "\"RUB\"");
        Response response = target("/account/create")
                .request()
                .post(entity(postData, APPLICATION_JSON));

        assertEquals(BAD_REQUEST_400, response.getStatus());
    }

    @Test
    public void createAccountTestWithoutCurrencyBad() {
        Account rawAccount = new Account();
        rawAccount.setMetadata(METADATA);

        Response response = target("/account/create")
                .request()
                .post(entity(rawAccount, APPLICATION_JSON));

        assertEquals(BAD_REQUEST_400, response.getStatus());
        assertEquals(MissedCurrencyIdException.MESSAGE, response.readEntity(String.class));
    }

    @Test
    public void createAccountWithDefinedPropertiesBad() {
        this.createAccountTestOk();
        Account account = this.account;

        Response response = target("/account/create")
                .request()
                .post(entity(account, APPLICATION_JSON));

        assertEquals(BAD_REQUEST_400, response.getStatus());
        assertEquals(NotAllowedIdException.MESSAGE, response.readEntity(String.class));
    }

    @Test
    public void getAccountTestOk() {
        this.createAccountTestOk();

        Response response = target("/account/" + account.getId())
                .request()
                .get();

        assertEquals(OK_200, response.getStatus());

        Account foundAccount = response.readEntity(Account.class);

        assertEquals(valueOf(643L), foundAccount.getCurrencyId());
        assertEquals(METADATA, foundAccount.getMetadata());
        assertEquals(0L, foundAccount.getSaldo().longValue());
        assertTrue(isNumeric(foundAccount.getNum()));
        assertEquals(foundAccount.getId().toString(), nameUUIDFromBytes(foundAccount.getNum().getBytes()).toString());
    }

    @Test
    public void removeAccountTestOk() {
        this.getAccountTestOk();

        Response deleteResponse = target("/account/" + account.getId())
                .request()
                .delete();

        assertEquals(NO_CONTENT_204, deleteResponse.getStatus());

        Response getResponse = target("/account/" + account.getId())
                .request()
                .get();

        assertEquals(NO_CONTENT_204, getResponse.getStatus());
    }

    @Test
    public void updateAccountTestOk() {
        this.createAccountTestOk();
        Account account = this.account;
        account.setMetadata("Test_Test");

        Response updateResponse = target("account/update/")
                .request()
                .post(entity(account, APPLICATION_JSON));

        assertEquals(NO_CONTENT_204, updateResponse.getStatus());

        Response getResponse = target("/account/" + account.getId())
                .request()
                .get();

        assertEquals(OK_200, getResponse.getStatus());

        assertEquals(valueOf(643L), account.getCurrencyId());
        assertEquals("Test_Test", account.getMetadata());
        assertEquals(0L, account.getSaldo().longValue());
        assertTrue(isNumeric(account.getNum()));
        assertEquals(account.getId().toString(), nameUUIDFromBytes(account.getNum().getBytes()).toString());
    }
}
