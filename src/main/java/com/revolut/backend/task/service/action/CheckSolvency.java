package com.revolut.backend.task.service.action;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;
import com.revolut.backend.task.dto.AccountTransferDTO;
import com.revolut.backend.task.entity.Account;
import com.revolut.backend.task.service.crud.AccountCrudService;

import javax.persistence.LockModeType;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.function.Predicate;

@Singleton
public class CheckSolvency implements Predicate<Action.Context> {

    private AccountCrudService accountCrudService;

    @Inject
    public CheckSolvency(AccountCrudService accountCrudService) {
        this.accountCrudService = accountCrudService;
    }

    @Override
    @Transactional
    public boolean test(Action.Context context) {
        AccountTransferDTO dto = context.getAccountTransferDTO();

        Account account = accountCrudService.findBy(dto.getCreditAccountId(), LockModeType.PESSIMISTIC_READ);

        return Arrays.asList(0,1).contains(
                account.getSaldo()
                        .subtract(dto.getAmount())
                        .compareTo(new BigDecimal(0)));
    }
}
