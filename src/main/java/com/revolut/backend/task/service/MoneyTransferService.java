package com.revolut.backend.task.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revolut.backend.task.dto.AccountTransferDTO;
import com.revolut.backend.task.service.action.*;
import com.revolut.backend.task.service.action.Action.Context;
import com.revolut.backend.task.util.HappyRubAccount;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import static java.util.Collections.singletonList;

@Singleton
public class MoneyTransferService {

    @Inject
    Logger logger;

    private AccountFetcher fetchAccounts;
    private CheckCurrencies byCurrencies;
    private CheckExistance byExistence;
    private ChangeSaldo changeSaldo;
    private CreateAccountEntry createAccountEntry;
    private HappyRubAccount happyRubAccount;

    @Inject
    public MoneyTransferService(AccountFetcher fetchAccounts,
                                CheckCurrencies byCurrencies,
                                CheckExistance byExistence,
                                ChangeSaldo changeSaldo,
                                CreateAccountEntry createAccountEntry,
                                HappyRubAccount happyRubAccount) {
        this.fetchAccounts = fetchAccounts;
        this.byCurrencies = byCurrencies;
        this.byExistence = byExistence;
        this.changeSaldo = changeSaldo;
        this.createAccountEntry = createAccountEntry;
        this.happyRubAccount = happyRubAccount;
    }

    public void transferMoney(UUID debitAccount, BigDecimal amount) {
        logger.info("Transferring to: " + debitAccount + " amount: " + amount);
        transferMoney(singletonList(new AccountTransferDTO(happyRubAccount.getAccount().getId(), debitAccount, amount)));
    }

    public void transferMoney(List<AccountTransferDTO> entities) {
        logger.info("Transferring in batch mode. Size: " + entities.size());
        process(entities);
    }

    public void transferMoney(UUID creditAccount, UUID debitAccount, BigDecimal amount) {
        logger.info("Transferring from: " + creditAccount + " to: " + debitAccount + " amount: " + amount);
        transferMoney(singletonList(new AccountTransferDTO(creditAccount, debitAccount, amount)));
    }

    private void process(List<AccountTransferDTO> entities) {
        entities.stream()
                .map(Context::new)
                .map(fetchAccounts)
                .filter(byExistence)
                .filter(byCurrencies)
                .peek(changeSaldo)
                .forEach(createAccountEntry);
    }
}
