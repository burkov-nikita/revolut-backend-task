package com.revolut.backend.task.service;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;
import com.revolut.backend.task.dto.AccountTransferDTO;
import com.revolut.backend.task.entity.Account;
import com.revolut.backend.task.entity.AccountEntry;
import com.revolut.backend.task.service.action.*;
import com.revolut.backend.task.service.action.Action.Context;
import com.revolut.backend.task.service.crud.AccountCrudService;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

@Singleton
public class MoneyTransferService {

    private AccountCrudService accountCrudService;
    private AccountFetcher fetchAccounts;
    private CheckCurrencies byCurrencies;
    private CheckExistance byExistence;
    private CheckSolvency bySolvency;
    private ChangeSaldo changeSaldo;
    private CreateAccountEntry createAccountEntry;

    @Inject
    public MoneyTransferService(AccountCrudService accountCrudService,
                                AccountFetcher fetchAccounts,
                                CheckCurrencies byCurrencies,
                                CheckExistance byExistence,
                                CheckSolvency bySolvency,
                                ChangeSaldo changeSaldo,
                                CreateAccountEntry createAccountEntry) {
        this.accountCrudService = accountCrudService;
        this.fetchAccounts = fetchAccounts;
        this.byCurrencies = byCurrencies;
        this.bySolvency = bySolvency;
        this.byExistence = byExistence;
        this.changeSaldo = changeSaldo;
        this.createAccountEntry = createAccountEntry;
    }

    @Transactional
    public void transferMoney(UUID debitAccount, BigDecimal amount, BiFunction<Account, BigDecimal, Account> direction) {
        accountCrudService.findBy(singletonList(debitAccount))
                .forEach(account -> account.changeSaldo(amount, direction));
    }

    @Transactional
    public List<AccountEntry> transferMoney(List<AccountTransferDTO> entities) {
        List<AccountEntry> accountEntries = entities.stream()
                .map(Context::new)
                .map(fetchAccounts)
                .filter(byExistence)
                .filter(byCurrencies)
                .filter(bySolvency)
                .peek(changeSaldo)
                .map(createAccountEntry)
                .collect(toList());

        return accountEntries.isEmpty() ? ImmutableList.of(new AccountEntry()) : accountEntries;
    }

    @Transactional
    public List<AccountEntry> transferMoney(UUID creditAccount, UUID debitAccount, BigDecimal amount) {
        return transferMoney(singletonList(new AccountTransferDTO(creditAccount, debitAccount, amount)));
    }
}
