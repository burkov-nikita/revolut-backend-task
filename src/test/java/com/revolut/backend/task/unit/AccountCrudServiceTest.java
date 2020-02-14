package com.revolut.backend.task.unit;

import com.revolut.backend.task.dao.impl.AccountDao;
import com.revolut.backend.task.entity.Account;
import com.revolut.backend.task.exception.MissedCurrencyIdException;
import com.revolut.backend.task.service.AccountGeneratorService;
import com.revolut.backend.task.service.crud.AccountCrudService;
import org.apache.commons.lang3.StringUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import static junit.framework.TestCase.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AccountCrudServiceTest {

    private static AccountCrudService service;
    private static AccountDao accountDao;

    @BeforeClass
    public static void init() {
        accountDao = mock(AccountDao.class);
        service = new AccountCrudService(accountDao, new AccountGeneratorService());
    }

    @Test
    public void createAccountWithoutIdButWithCurrency() {
        Account account = new Account(643L, "Test");
        when(accountDao.save(account)).thenReturn(account);
        service.create(account);
        assertFalse(account.getNum().isEmpty());
        assertTrue(StringUtils.isNumeric(account.getNum()));
    }

    @Test
    public void createAccountWithoutIdAndCurrency() {
        boolean flag = false;
        Account account = new Account();
        when(accountDao.save(account)).thenReturn(account);
        try {
            service.create(account);
        } catch (Exception e) {
            assertTrue(e instanceof MissedCurrencyIdException);
            flag = true;
        } finally {
            assertTrue(flag);
        }
    }

    @Test
    public void createAccountNull() {
        Account account = null;
        when(accountDao.save(account)).thenReturn(account);
        Account result = service.create(account);
        assertNull(result);
    }
}
