package com.revolut.backend.task.service;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;
import com.revolut.backend.task.dto.AccountTransferDTO;
import com.revolut.backend.task.entity.Account;
import com.revolut.backend.task.entity.AccountEntry;
import com.revolut.backend.task.service.action.*;
import com.revolut.backend.task.service.crud.AccountCrudService;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;
import static java.util.stream.Stream.of;

@Singleton
public class MoneyTransferService {

    @Inject
    private AccountCrudService accountCrudService;

    @Inject
    private AccountFetcher fetchAccounts;

    @Inject
    private CheckCurrencies byCurrencies;

    @Inject
    private CheckExistance byExistence;

    @Inject
    private CheckSolvency bySolvency;

    @Inject
    private ChangeSaldo changeSaldo;

    @Inject
    private CreateAccountEntry createAccountEntry;

    @Transactional
    public void transferMoney(UUID debitAccount, BigDecimal amount, BiFunction<Account, BigDecimal, Account> direction) {
        accountCrudService.findBy(singletonList(debitAccount))
                .forEach(account -> account.changeSaldo(amount, direction));
    }

    @Transactional
    public List<AccountEntry> transferMoney(UUID creditAccount, UUID debitAccount, BigDecimal amount) {
        List<AccountEntry> accountEntries = of(new Action.Context(new AccountTransferDTO(creditAccount, debitAccount, amount)))
                .map(fetchAccounts)
                .filter(byExistence)
                .filter(byCurrencies)
                .filter(bySolvency)
                .peek(changeSaldo)
                .map(createAccountEntry)
                .collect(Collectors.toList());
        return accountEntries.isEmpty() ? ImmutableList.of(new AccountEntry()) : accountEntries;
    }
}
