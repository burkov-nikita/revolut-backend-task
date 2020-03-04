package com.revolut.backend.task.dao.impl;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.revolut.backend.task.dao.EntityDao;
import com.revolut.backend.task.entity.AccountEntry;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import java.util.List;
import java.util.UUID;

public class AccountEntryDao implements EntityDao<AccountEntry> {

    private Provider<EntityManager> entityManager;

    @Inject
    public AccountEntryDao(Provider<EntityManager> entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public AccountEntry findBy(UUID id) {
        return entityManager.get().find(AccountEntry.class, id);
    }

    @Override
    public AccountEntry findBy(UUID id, LockModeType lockModeType) {
        return entityManager.get().find(AccountEntry.class, id, lockModeType);
    }

    @Override
    public List<AccountEntry> findBy(List<UUID> id, LockModeType lockModeType) {
        return (List<AccountEntry>) entityManager.get()
                .createQuery("FROM AccountEntry ae WHERE ae.debitAccount.id IN :ids OR ae.creditAccount.id IN :ids")
                .setParameter("ids", id)
                .setLockMode(lockModeType)
                .getResultList();
    }

    @Override
    public AccountEntry save(AccountEntry entity) {
        return entityManager.get().merge(entity);
    }

    @Override
    public void delete(UUID id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update(AccountEntry entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update(String query, Object... parameters) {
        throw new UnsupportedOperationException();
    }
}
