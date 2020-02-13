package com.revolut.backend.task.service.impl.crud;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.revolut.backend.task.dao.impl.AccountDao;
import com.revolut.backend.task.entity.Account;
import com.revolut.backend.task.service.impl.AccountGeneratorService;

import java.util.List;
import java.util.UUID;

public class AccountCrudService implements CrudService<Account> {

    @Inject
    private AccountDao accountDao;

    @Inject
    private AccountGeneratorService accountGeneratorService;

    @Override
    @Transactional
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
