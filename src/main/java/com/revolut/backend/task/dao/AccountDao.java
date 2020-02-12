package com.revolut.backend.task.dao;

import com.revolut.backend.task.entity.Account;

public interface AccountDao {
    Account findBy(String id);
    Account save(Account account);
    void delete(String id);
    void update(Account account);
}
