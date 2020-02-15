package com.revolut.backend.task.service.action;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revolut.backend.task.dto.AccountTransferDTO;
import com.revolut.backend.task.entity.Account;
import com.revolut.backend.task.service.crud.AccountCrudService;

import java.util.Map;
import java.util.UUID;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toMap;

@Singleton
public class AccountFetcher implements Action {

    private AccountCrudService accountCrudService;

    @Inject
    public AccountFetcher(AccountCrudService accountCrudService) {
        this.accountCrudService = accountCrudService;
    }

    @Override
    public Context apply(Context context) {
        AccountTransferDTO dto = context.getAccountTransferDTO();

        Map<UUID, Account> accounts = accountCrudService.findBy(asList(dto.getCreditAccountId(), dto.getDebitAccountId()))
                .stream()
                .collect(toMap(Account::getId, account -> account));

        context.setCreditAccount(accounts.get(dto.getCreditAccountId()));
        context.setDebitAccount(accounts.get(dto.getDebitAccountId()));

        return context;
    }
}
