package com.revolut.backend.task.util;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revolut.backend.task.entity.Account;
import com.revolut.backend.task.service.crud.AccountCrudService;
import lombok.Getter;

import java.math.BigDecimal;

@Singleton
public class HappyRubAccount {
    @Getter
    private final Account account;

    @Inject
    public HappyRubAccount(AccountCrudService accountCrudService) {
        this.account = accountCrudService.create(new Account(643L, new BigDecimal(999999999L), "RUB Happy Account"));
    }
}
