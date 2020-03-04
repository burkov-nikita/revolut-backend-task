package com.revolut.backend.task.service.crud;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revolut.backend.task.dao.impl.AccountEntryDao;
import com.revolut.backend.task.entity.AccountEntry;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.UUID;

@Singleton
public class AccountEntryCrudService implements CrudService<AccountEntry> {

    private AccountEntryDao accountEntryDao;

    @Inject
    public AccountEntryCrudService(AccountEntryDao accountEntryDao) {
        this.accountEntryDao = accountEntryDao;
    }

    @Override
    public AccountEntry findBy(UUID id) {
        return accountEntryDao.findBy(id);
    }

    @Override
    public AccountEntry findBy(UUID id, LockModeType lockModeType) {
        return accountEntryDao.findBy(id, lockModeType);
    }

    @Override
    public List<AccountEntry> findBy(List<UUID> id, LockModeType lockModeType) {
        return accountEntryDao.findBy(id, lockModeType);
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
    public void update(String query, Object... parameters) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(UUID id) {
        accountEntryDao.delete(id);
    }
}
