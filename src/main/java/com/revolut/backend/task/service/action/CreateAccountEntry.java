package com.revolut.backend.task.service.action;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;
import com.revolut.backend.task.entity.AccountEntry;
import com.revolut.backend.task.service.crud.AccountEntryCrudService;

import java.util.function.Consumer;
import java.util.logging.Logger;

@Singleton
public class CreateAccountEntry implements Consumer<Action.Context> {

    @Inject
    Logger logger;

    private AccountEntryCrudService accountEntryCrudService;

    @Inject
    public CreateAccountEntry(AccountEntryCrudService accountEntryCrudService) {
        this.accountEntryCrudService = accountEntryCrudService;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void accept(Action.Context context) {
        logger.info("Entry: " + context.getCreditAccount().getNum() + " - " + context.getDebitAccount().getNum());
        accountEntryCrudService.create(new AccountEntry(
                context.getCreditAccount(),
                context.getDebitAccount(),
                context.getAccountTransferDTO().getAmount()));
    }
}
