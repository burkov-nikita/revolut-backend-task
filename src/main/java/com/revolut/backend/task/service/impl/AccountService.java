package com.revolut.backend.task.service.impl;

import com.google.inject.Inject;
import com.revolut.backend.task.dao.impl.AccountDao;
import com.revolut.backend.task.entity.Account;
import com.revolut.backend.task.entity.AccountBalance;
import com.revolut.backend.task.service.CrudService;

import java.util.List;
import java.util.UUID;

public class AccountService implements CrudService<Account> {

    @Inject
    private AccountDao accountDao;

    @Inject
    private AccountGeneratorService accountGeneratorService;

    @Override
    public Account findBy(UUID id) {
        return accountDao.findBy(id);
    }

    public List<Account> findBy(List ids) {
        return accountDao.findBy(ids);
    }

    @Override
    public Account create(Account entity) {
        if (entity.getNum() == null || entity.getNum().equals("")) {
            entity.setNum(accountGeneratorService.generateAccountNumber(entity));
        }
        entity.setAccountBalance(new AccountBalance());
        return accountDao.save(entity);
    }

    @Override
    public void update(Account entity) {
        accountDao.update(entity);
    }

    @Override
    public void delete(UUID id) {
        accountDao.delete(id);
    }
}
