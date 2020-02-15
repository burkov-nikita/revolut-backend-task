package com.revolut.backend.task.service.action;

import com.revolut.backend.task.dto.AccountTransferDTO;
import com.revolut.backend.task.entity.Account;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Function;

@FunctionalInterface
public interface Action extends Function<Action.Context, Action.Context> {

    @Getter
    @Setter
    class Context {
        final private AccountTransferDTO accountTransferDTO;
        private Account creditAccount;
        private Account debitAccount;

        public Context(AccountTransferDTO accountTransferDTO) {
            this.accountTransferDTO = accountTransferDTO;
        }
    }
}
