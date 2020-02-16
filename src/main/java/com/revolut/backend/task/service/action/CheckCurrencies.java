package com.revolut.backend.task.service.action;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.function.Predicate;
import java.util.logging.Logger;

@Singleton
public class CheckCurrencies implements Predicate<Action.Context> {

    @Inject
    Logger logger;

    @Override
    public boolean test(Action.Context context) {
        boolean isTheSameCurrency = context.getCreditAccount().getCurrencyId().equals(context.getDebitAccount().getCurrencyId());
        if (!isTheSameCurrency) {
            logger.warning("Accounts " + context.getCreditAccount().getNum() + ", " + context.getDebitAccount().getNum() + " have different cuurencies");
        }
        return isTheSameCurrency;
    }
}
