package com.revolut.backend.task.service.action;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;
import com.revolut.backend.task.dto.AccountTransferDTO;
import com.revolut.backend.task.entity.Account;
import com.revolut.backend.task.service.crud.AccountCrudService;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

import static java.lang.String.valueOf;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toMap;
import static javax.persistence.LockModeType.PESSIMISTIC_WRITE;

@Singleton
public class ChangeSaldo implements Consumer<Action.Context> {

    private AccountCrudService accountCrudService;

    @Inject
    public ChangeSaldo(AccountCrudService accountCrudService) {
        this.accountCrudService = accountCrudService;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void accept(Action.Context context) {
        AccountTransferDTO dto = context.getAccountTransferDTO();

        Map<UUID, Account> accounts = accountCrudService.findBy(asList(dto.getCreditAccountId(), dto.getDebitAccountId()), PESSIMISTIC_WRITE)
                .stream()
                .collect(toMap(Account::getId, account -> account));

        BigDecimal creditSaldo = new BigDecimal(valueOf(accounts.get(dto.getCreditAccountId()).getSaldo()));

        if (asList(0,1).contains(creditSaldo.subtract(dto.getAmount()).compareTo(new BigDecimal("0")))) {
            accountCrudService.update("UPDATE Account SET saldo = saldo - ?0 WHERE id = ?1",
                    context.getAccountTransferDTO().getAmount(),
                    accounts.get(dto.getCreditAccountId()).getId());

            accountCrudService.update("UPDATE Account SET saldo = saldo + ?0 WHERE id = ?1",
                    context.getAccountTransferDTO().getAmount(),
                    accounts.get(dto.getDebitAccountId()).getId());
        }
    }
}
