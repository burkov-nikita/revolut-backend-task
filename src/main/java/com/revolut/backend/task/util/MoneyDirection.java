package com.revolut.backend.task.util;

import com.revolut.backend.task.entity.Account;

import java.math.BigDecimal;
import java.util.function.BiFunction;

public class MoneyDirection {
    public static BiFunction<Account, BigDecimal, Account> INCREASE_SALDO = (account, amount) -> {
        account.setSaldo(account.getSaldo().add(amount));
        return account;
    };

    public static BiFunction<Account, BigDecimal, Account> DESCREASE_SALDO = (account, amount) -> {
        account.setSaldo(account.getSaldo().subtract(amount));
        return account;
    };
}
