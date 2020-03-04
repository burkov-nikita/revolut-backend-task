package com.revolut.backend.task.service.crud;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.UUID;

public interface CrudService<T> {
    T findBy(UUID id);
    T findBy(UUID id, LockModeType lockModeType);
    List<T> findBy(List<UUID> id, LockModeType lockModeType);
    T create(T entity);
    void update(T entity);
    void update(String query, Object... parameters);
    void delete(UUID id);
}
