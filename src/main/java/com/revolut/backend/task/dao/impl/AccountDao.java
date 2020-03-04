package com.revolut.backend.task.dao.impl;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.revolut.backend.task.dao.EntityDao;
import com.revolut.backend.task.entity.Account;
import com.revolut.backend.task.exception.NotAllowedIdException;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static javax.persistence.LockModeType.PESSIMISTIC_WRITE;

@Singleton
public class AccountDao implements EntityDao<Account> {

    private Provider<EntityManager> entityManager;

    @Inject
    public AccountDao(Provider<EntityManager> entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Account findBy(UUID id) {
        return entityManager.get().find(Account.class, id);
    }

    @Override
    public Account findBy(UUID id, LockModeType lockModeType) {
        return entityManager.get().find(Account.class, id, lockModeType);
    }

    @Override
    public List<Account> findBy(List<UUID> id, LockModeType lockModeType) {
        return (List<Account>) entityManager.get()
                .createQuery("FROM Account a WHERE a.id IN :ids")
                .setParameter("ids", id)
                .setLockMode(lockModeType)
                .getResultList();
    }

    @Override
    public Account save(Account account) {
        if (account != null && account.getId() != null) {
            throw new NotAllowedIdException();
        } else {
            return entityManager.get().merge(account);
        }
    }

    @Override
    public void delete(UUID id) {
        Optional.ofNullable(entityManager.get().find(Account.class, id))
                .ifPresent(entity -> entityManager.get().remove(entity));
    }

    @Override
    public void update(Account account) {
        Optional.ofNullable(entityManager.get().find(Account.class, account.getId(), PESSIMISTIC_WRITE))
                .ifPresent(entity -> {
                    entity.setMetadata(account.getMetadata());
                    entity.setSaldo(account.getSaldo());
                });
    }

    @Override
    public void update(String query, Object... parameters) {
        Query updateQuery = entityManager.get().createQuery(query);
        for (int i = 0; i < parameters.length; i++) {
            updateQuery = updateQuery.setParameter(i, parameters[i]);
        }
        updateQuery.executeUpdate();
    }
}
