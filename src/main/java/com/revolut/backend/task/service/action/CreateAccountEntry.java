package com.revolut.backend.task.service.action;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revolut.backend.task.entity.AccountEntry;
import com.revolut.backend.task.service.crud.AccountEntryCrudService;

import java.util.function.Function;

@Singleton
public class CreateAccountEntry implements Function<Action.Context, AccountEntry> {

    @Inject
    private AccountEntryCrudService accountEntryCrudService;

    @Override
    public AccountEntry apply(Action.Context context) {
        return accountEntryCrudService.create(new AccountEntry(
                context.getCreditAccount(),
                context.getDebitAccount(),
                context.getAccountTransferDTO().getAmount()));
    }
}
