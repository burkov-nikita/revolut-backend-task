package com.revolut.backend.task.dao;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.UUID;

public interface EntityDao<T> {
    T findBy(UUID id);
    T findBy(UUID id, LockModeType lockModeType);
    List<T> findBy(List<UUID> id, LockModeType lockModeType);
    T save(T entity);
    void delete(UUID id);
    void update(T entity);
    void update(String query, Object... parameters);
}
