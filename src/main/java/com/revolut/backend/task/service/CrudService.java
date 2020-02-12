package com.revolut.backend.task.service;

public interface CrudService<T> {
    T findBy(String id);
    T create(T entity);
    void update(T entity);
    void delete(String id);
}
