package com.revolut.backend.task.api.controller;

import com.revolut.backend.task.api.annotation.AccountNumberToUUID;
import com.revolut.backend.task.dto.AccountTransferDTO;
import com.revolut.backend.task.service.MoneyTransferService;

import javax.inject.Inject;
import javax.validation.constraints.Min;
import javax.ws.rs.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/transfer")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public class MoneyTransferController {

    @Inject
    private MoneyTransferService moneyTransferService;

    @POST
    @Path("from/{from}/to/{to}")
    public void transferMoneyBetweenAccounts(@PathParam("from") @AccountNumberToUUID UUID from,
                                                           @PathParam("to") @AccountNumberToUUID UUID to,
                                                           @Min(0) @QueryParam("amount") BigDecimal amount) {
        moneyTransferService.transferMoney(from, to, amount);
    }

    @POST
    @Path("batch")
    public void transferMoneyInBatchMode(List<AccountTransferDTO> entries) {
        moneyTransferService.transferMoney(entries);
    }

    @POST
    @Path("to/{to}")
    public void transferMoneyToAccount(@PathParam("to") @AccountNumberToUUID UUID to,
                                       @QueryParam("amount") BigDecimal amount) {
        moneyTransferService.transferMoney(to, amount);
    }
}
