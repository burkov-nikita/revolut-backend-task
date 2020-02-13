package com.revolut.backend.task.api.controller;

import com.revolut.backend.task.api.annotation.AccountNumberToUUID;
import com.revolut.backend.task.entity.AccountEntry;
import com.revolut.backend.task.service.impl.MoneyTransferService;

import javax.inject.Inject;
import javax.ws.rs.*;
import java.math.BigDecimal;
import java.util.UUID;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/transfer")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public class MoneyTransferController {

    @Inject
    private MoneyTransferService moneyTransferService;

    @POST
    public AccountEntry transferMoneyByAccountNumber(@QueryParam("from") @AccountNumberToUUID UUID from,
                                                     @QueryParam("to") @AccountNumberToUUID UUID to,
                                                     @QueryParam("amount") BigDecimal amount) {
       return moneyTransferService.transferMoney(from, to, amount);
    }

}
