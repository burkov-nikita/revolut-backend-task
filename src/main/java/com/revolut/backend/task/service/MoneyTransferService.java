package com.revolut.backend.task.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;
import com.revolut.backend.task.entity.Account;
import com.revolut.backend.task.entity.AccountEntry;
import com.revolut.backend.task.service.crud.AccountCrudService;
import com.revolut.backend.task.service.crud.AccountEntryCrudService;
import com.revolut.backend.task.util.MoneyDirection;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toMap;

@Singleton
public class MoneyTransferService {

    @Inject
    private AccountCrudService accountCrudService;

    @Inject
    private AccountEntryCrudService accountEntryCrudService;

    @Transactional
    public void transferMoney(UUID debitAccount, BigDecimal amount, BiFunction<Account, BigDecimal, Account> direction) {
        accountCrudService.findBy(singletonList(debitAccount))
                .forEach(account -> account.changeSaldo(amount, direction));
    }

    @Transactional
    public AccountEntry transferMoney(UUID creditAccount, UUID debitAccount, BigDecimal amount) {
        List<Account> accounts = accountCrudService.findBy(asList(debitAccount, creditAccount));

        if (accounts.size() == 2) {
            Map<UUID, Account> accountsMap = accounts.stream().collect(toMap(Account::getId, account -> account));
            boolean solvent = accountsMap.get(creditAccount).isSolvent(amount);
            if (solvent) {
                accountsMap.get(creditAccount).changeSaldo(amount, MoneyDirection.DESCREASE_SALDO);
                accountsMap.get(debitAccount).changeSaldo(amount, MoneyDirection.INCREASE_SALDO);
                return accountEntryCrudService.create(new AccountEntry(accountsMap.get(creditAccount), accountsMap.get(debitAccount), amount));
            }

        }
        return new AccountEntry();
    }
}
