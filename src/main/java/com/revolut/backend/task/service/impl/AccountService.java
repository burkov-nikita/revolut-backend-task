package com.revolut.backend.task.service.impl;

import com.google.inject.Inject;
import com.revolut.backend.task.dao.AccountDao;
import com.revolut.backend.task.entity.Account;
import com.revolut.backend.task.service.CrudService;

public class AccountService implements CrudService<Account> {

    @Inject
    private AccountDao accountDao;

    @Override
    public Account findBy(String id) {
        return accountDao.findBy(id);
    }

    @Override
    public Account create(Account entity) {
        return accountDao.save(entity);
    }

    @Override
    public void update(Account entity) {
        accountDao.update(entity);
    }

    @Override
    public void delete(String id) {
        accountDao.delete(id);
    }
}
