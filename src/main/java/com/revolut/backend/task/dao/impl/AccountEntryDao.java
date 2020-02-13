package com.revolut.backend.task.dao.impl;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.revolut.backend.task.dao.EntityDao;
import com.revolut.backend.task.entity.AccountEntry;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.UUID;

public class AccountEntryDao implements EntityDao<AccountEntry> {

    @Inject
    private Provider<EntityManager> entityManager;

    @Override
    public AccountEntry findBy(UUID id) {
        return entityManager.get().find(AccountEntry.class, id);
    }

    @Override
    public List<AccountEntry> findBy(List ids) {
        return (List<AccountEntry>) entityManager.get()
                .createQuery("FROM AccountEntry ae WHERE ae.debitAccount IN :ids OR ae.creditAccount IN :ids")
                .setParameter("ids", ids)
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
}
