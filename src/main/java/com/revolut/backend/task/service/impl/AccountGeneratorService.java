package com.revolut.backend.task.service.impl;


import com.google.inject.Singleton;
import com.revolut.backend.task.entity.Account;
import com.revolut.backend.task.exception.MissedCurrencyIdException;

import static java.lang.String.valueOf;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.apache.commons.lang3.StringUtils.leftPad;
import static org.apache.commons.lang3.StringUtils.rightPad;

@Singleton
public class AccountGeneratorService {

    private static final int CHECK_BIT = 0;

    public String generateAccountNumber(Account account) {
        if (account.getCurrencyId() == null || account.getCurrencyId() == 0) {
            throw new MissedCurrencyIdException();
        }

        return rightPad(valueOf(current().nextInt(1, 999999)), 6, '0') +
                CHECK_BIT +
                leftPad(account.getCurrencyId().toString(), 3, "0");
    }

}
