package com.revolut.backend.task.service.crud;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revolut.backend.task.dao.impl.AccountEntryDao;
import com.revolut.backend.task.entity.AccountEntry;

import java.util.List;
import java.util.UUID;

@Singleton
public class AccountEntryCrudService implements CrudService<AccountEntry> {

    @Inject
    private AccountEntryDao accountEntryDao;

    @Override
    public AccountEntry findBy(UUID id) {
        return accountEntryDao.findBy(id);
    }

    @Override
    public List<AccountEntry> findBy(List ids) {
        return accountEntryDao.findBy(ids);
    }

    @Override
    public AccountEntry create(AccountEntry entity) {
        return accountEntryDao.save(entity);
    }

    @Override
    public void update(AccountEntry entity) {
        accountEntryDao.update(entity);
    }

    @Override
    public void delete(UUID id) {
        accountEntryDao.delete(id);
    }
}
