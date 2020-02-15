package com.revolut.backend.task.service.action;

import com.google.inject.Singleton;
import com.revolut.backend.task.util.SaldoDirection;

import java.util.function.Consumer;

@Singleton
public class ChangeSaldo implements Consumer<Action.Context> {

    @Override
    public void accept(Action.Context context) {
        context.getCreditAccount().changeSaldo(context.getAccountTransferDTO().getAmount(), SaldoDirection.DESCREASE_SALDO);
        context.getDebitAccount().changeSaldo(context.getAccountTransferDTO().getAmount(), SaldoDirection.INCREASE_SALDO);
    }
}
