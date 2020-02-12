package com.revolut.backend.task.api.controller;

import com.revolut.backend.task.entity.Account;
import com.revolut.backend.task.service.impl.AccountService;

import javax.inject.Inject;
import javax.ws.rs.*;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/account")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public class AccountController {

    @Inject
    private AccountService accountService;

    @POST
    @Path("create")
    public Account createAccount(Account account) {
        return accountService.create(account);
    }

    @GET
    @Path("{id}")
    public Account getAccount(@PathParam("id") String id) {
        return accountService.findBy(id);
    }

    @DELETE
    @Path("{id}")
    public void removeAccount(@PathParam("id") String id) {
        accountService.delete(id);
    }

    @POST
    @Path("update")
    public void updateAccount(Account account) {
        accountService.update(account);
    }


}
