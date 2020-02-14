package com.revolut.backend.task.unit;

import com.revolut.backend.task.entity.Account;
import com.revolut.backend.task.util.MoneyDirection;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class MoneyDirectionTest {

    @Test
    public void increaseSaldoTest() {
        Account account = new Account();
        BigDecimal amount = new BigDecimal(1000);
        IntStream.range(0, 1000).forEach(i ->
                account.changeSaldo(amount, MoneyDirection.INCREASE_SALDO)
        );
        assertEquals(new BigDecimal(1000000), account.getSaldo());
    }

    @Test
    public void decreaseSaldoTest() {
        Account account = new Account();
        BigDecimal amount = new BigDecimal(1000);
        IntStream.range(0, 1000).forEach(i ->
                account.changeSaldo(amount, MoneyDirection.DESCREASE_SALDO)
        );
        assertEquals(new BigDecimal(-1000000), account.getSaldo());
    }

    @Test
    public void mixSaldoTest() {
        Account account = new Account();
        BigDecimal amount = new BigDecimal(1000);
        IntStream.range(0, 1000).forEach(i -> {
            account.changeSaldo(amount, MoneyDirection.INCREASE_SALDO);
            account.changeSaldo(amount, MoneyDirection.DESCREASE_SALDO);
        });
        assertEquals(new BigDecimal(0), account.getSaldo());
    }
}
