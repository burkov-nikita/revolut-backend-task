package com.revolut.backend.task.api.controller;

import com.revolut.backend.task.api.annotation.AccountNumberToUUID;
import com.revolut.backend.task.entity.Account;
import com.revolut.backend.task.entity.AccountEntry;
import com.revolut.backend.task.service.crud.AccountCrudService;
import com.revolut.backend.task.service.crud.AccountEntryCrudService;

import javax.inject.Inject;
import javax.ws.rs.*;
import java.util.List;
import java.util.UUID;

import static java.util.Collections.singletonList;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/account")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public class AccountController {

    @Inject
    private AccountCrudService accountCrudService;

    @Inject
    private AccountEntryCrudService accountEntryCrudService;

    @POST
    @Path("create")
    public Account createAccount(Account account) {
        return accountCrudService.create(account);
    }

    @GET
    @Path("{id}")
    public Account getAccount(@PathParam("id") @AccountNumberToUUID UUID id) {
        return accountCrudService.findBy(id);
    }

    @DELETE
    @Path("{id}")
    public void removeAccount(@PathParam("id") @AccountNumberToUUID UUID id) {
        accountCrudService.delete(id);
    }

    @POST
    @Path("update")
    public void updateAccount(Account account) {
        accountCrudService.update(account);
    }

    @GET
    @Path("{id}/statement")
    public List<AccountEntry> showStatement(@PathParam("id") @AccountNumberToUUID UUID id) {
        return accountEntryCrudService.findBy(singletonList(id));
    }
}
