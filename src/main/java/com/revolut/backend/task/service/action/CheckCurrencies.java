package com.revolut.backend.task.service.action;

import com.google.inject.Singleton;

import java.util.function.Predicate;

@Singleton
public class CheckCurrencies implements Predicate<Action.Context> {

    @Override
    public boolean test(Action.Context context) {
        return context.getCreditAccount().getCurrencyId().equals(context.getDebitAccount().getCurrencyId());
    }
}
