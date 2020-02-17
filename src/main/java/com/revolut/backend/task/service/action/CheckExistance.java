package com.revolut.backend.task.service.action;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.function.Predicate;
import java.util.logging.Logger;

@Singleton
public class CheckExistance implements Predicate<Action.Context> {

    @Inject
    Logger logger;

    @Override
    public boolean test(Action.Context context) {
        boolean isExist = context.getCreditAccount() != null && context.getDebitAccount() != null;
        if (!isExist) {
            logger.warning("Account " + context.getAccountTransferDTO().getCreditAccountNumber() + " or " + context.getAccountTransferDTO().getDebitAccountNumber() + " is not exists");
        }
        return isExist;
    }
}
