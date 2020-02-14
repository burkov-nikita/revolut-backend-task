package com.revolut.backend.task.service.crud;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;
import com.revolut.backend.task.service.AccountGeneratorService;
import com.revolut.backend.task.dao.impl.AccountDao;
import com.revolut.backend.task.entity.Account;

import java.util.List;
import java.util.UUID;

@Singleton
public class AccountCrudService implements CrudService<Account> {

    private AccountDao accountDao;

    private AccountGeneratorService accountGeneratorService;

    @Inject
    public AccountCrudService(AccountDao accountDao, AccountGeneratorService accountGeneratorService) {
        this.accountDao = accountDao;
        this.accountGeneratorService = accountGeneratorService;
    }

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
        if (entity != null && (entity.getNum() == null || entity.getNum().equals(""))) {
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
