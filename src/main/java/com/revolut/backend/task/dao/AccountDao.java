package com.revolut.backend.task.dao;

import com.revolut.backend.task.entity.Account;

import java.util.List;

public interface AccountDao {
    Account findBy(String id);
    List findBy(List ids);
    Account save(Account account);
    void delete(String id);
    void update(Account account);
}
