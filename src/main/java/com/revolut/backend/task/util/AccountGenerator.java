package com.revolut.backend.task.util;


import com.revolut.backend.task.entity.Account;
import com.revolut.backend.task.exception.MissedCurrencyIdException;

import java.util.Random;
import java.util.UUID;

import static java.lang.String.valueOf;
import static java.util.UUID.fromString;
import static java.util.UUID.nameUUIDFromBytes;
import static org.apache.commons.lang3.StringUtils.*;

public class AccountGenerator {

    private static final int CHECK_BIT = 0;

    public static String generateAccountNumber(Account account) {
        if (account.getCurrencyId() == null || account.getCurrencyId() == 0) {
            throw new MissedCurrencyIdException();
        }

        return rightPad(valueOf(new Random().nextInt(Integer.MAX_VALUE) + 1), 11, '0') +
                CHECK_BIT +
                leftPad(account.getCurrencyId().toString(), 3, "0");
    }

    public static UUID getUUID(String value) {
        return isNumeric(value) ? nameUUIDFromBytes(value.getBytes()) : fromString(value);
    }
}
