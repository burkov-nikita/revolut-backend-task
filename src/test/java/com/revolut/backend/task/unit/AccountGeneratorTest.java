package com.revolut.backend.task.unit;

import com.revolut.backend.task.entity.Account;
import com.revolut.backend.task.exception.MissedCurrencyIdException;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.UUID;

import static com.revolut.backend.task.util.AccountGenerator.generateAccountNumber;
import static com.revolut.backend.task.util.AccountGenerator.getUUID;
import static java.util.UUID.nameUUIDFromBytes;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class AccountGeneratorTest {

    @Test
    public void generateAccountNumberTest() {
        Account account = new Account();
        account.setCurrencyId(643L);

        String id = generateAccountNumber(account);
        assertTrue(StringUtils.isNumeric(id));
        assertEquals(15, id.length());
    }

    @Test
    public void generateAccountNumberWoCurrency() {
        boolean flag = false;
        Account account = new Account();

        try {
            generateAccountNumber(account);
        } catch (Exception e) {
            assertTrue(e instanceof MissedCurrencyIdException);
            flag = true;
        }
        assertTrue(flag);
    }

    @Test
    public void getUUIDFromUUIDTest() {
        UUID uuid = UUID.randomUUID();
        UUID result = getUUID(uuid.toString());
        assertEquals(uuid.toString(), result.toString());
    }

    @Test
    public void getUUIDFromNumberTest() {
        String number = "12345678901234";
        UUID result = getUUID(number);
        assertEquals(nameUUIDFromBytes(number.getBytes()).toString(), result.toString());
    }

    @Test
    public void getUUIDFromNullTest() {
        String nul = null;
        boolean flag = false;
        try {
            getUUID(nul);
        } catch (Exception e) {
            assertTrue(e instanceof NullPointerException);
            flag = true;
        }
        assertTrue(flag);
    }
}
