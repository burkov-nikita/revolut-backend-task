package com.revolut.backend.task.dao.impl;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;
import com.revolut.backend.task.dao.EntityDao;
import com.revolut.backend.task.entity.Account;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class AccountDao implements EntityDao<Account> {

    @Inject
    private Provider<EntityManager> entityManager;

    @Override
    public Account findBy(UUID id) {
        return entityManager.get().find(Account.class, id);
    }

    @Override
    public List<Account> findBy(List ids) {
        return (List<Account>)entityManager.get()
                .createQuery("FROM Account a WHERE a.id IN :ids")
                .setParameter("ids", ids)
                .getResultList();
    }

    @Override
    @Transactional
    public Account save(Account account) {
        return entityManager.get().merge(account);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
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
