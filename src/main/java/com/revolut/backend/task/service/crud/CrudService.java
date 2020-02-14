package com.revolut.backend.task.service.crud;

import java.util.List;
import java.util.UUID;

public interface CrudService<T> {
    T findBy(UUID id);
    List<T> findBy(List ids);
    T create(T entity);
    void update(T entity);
    void delete(UUID id);
}
