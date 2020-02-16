package com.revolut.backend.task.service.action;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revolut.backend.task.entity.AccountEntry;
import com.revolut.backend.task.service.crud.AccountEntryCrudService;

import java.util.function.Function;
import java.util.logging.Logger;

@Singleton
public class CreateAccountEntry implements Function<Action.Context, AccountEntry> {

    @Inject
    Logger logger;

    private AccountEntryCrudService accountEntryCrudService;

    @Inject
    public CreateAccountEntry(AccountEntryCrudService accountEntryCrudService) {
        this.accountEntryCrudService = accountEntryCrudService;
    }

    @Override
    public AccountEntry apply(Action.Context context) {
        logger.info("Entry: " + context.getCreditAccount().getNum() + " - " + context.getDebitAccount().getNum());
        return accountEntryCrudService.create(new AccountEntry(
                context.getCreditAccount(),
                context.getDebitAccount(),
                context.getAccountTransferDTO().getAmount()));
    }
}
