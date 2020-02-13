package com.revolut.backend.task.dao;

import java.util.List;
import java.util.UUID;

public interface EntityDao<T> {
    T findBy(UUID id);
    List<T> findBy(List ids);
    T save(T entity);
    void delete(UUID id);
    void update(T entity);
}
