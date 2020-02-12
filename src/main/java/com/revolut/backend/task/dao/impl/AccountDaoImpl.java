package com.revolut.backend.task.dao.impl;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import com.revolut.backend.task.dao.AccountDao;
import com.revolut.backend.task.entity.Account;

import javax.persistence.EntityManager;
import java.util.Optional;

public class AccountDaoImpl implements AccountDao {

    @Inject
    private Provider<EntityManager> entityManager;

    @Override
    public Account findBy(String id) {
        return entityManager.get().find(Account.class, id);
    }

    @Override
    @Transactional
    public Account save(Account account) {
        return entityManager.get().merge(account);
    }

    @Override
    @Transactional
    public void delete(String id) {
        Optional.ofNullable(entityManager.get().find(Account.class, id))
                .ifPresent(entity -> entityManager.get().remove(entity));
    }

    @Override
    @Transactional
    public void update(Account account) {
        Optional.ofNullable(entityManager.get().find(Account.class, account.getId()))
                .ifPresent(entity -> {
                            account.setId(entity.getId());
                            entityManager.get().merge(account);
                        }
                );
    }
}
