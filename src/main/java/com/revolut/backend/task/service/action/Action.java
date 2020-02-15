package com.revolut.backend.task.service.action;

import com.revolut.backend.task.dto.AccountTransferDTO;
import com.revolut.backend.task.entity.Account;

import java.util.function.Function;

@FunctionalInterface
public interface Action extends Function<Action.Context, Action.Context> {

    class Context {
        final private AccountTransferDTO accountTransferDTO;
        private Account creditAccount;
        private Account debitAccount;

        AccountTransferDTO getAccountTransferDTO() {
            return accountTransferDTO;
        }

        Account getCreditAccount() {
            return creditAccount;
        }

        void setCreditAccount(Account creditAccount) {
            this.creditAccount = creditAccount;
        }

        Account getDebitAccount() {
            return debitAccount;
        }

        void setDebitAccount(Account debitAccount) {
            this.debitAccount = debitAccount;
        }

        public Context(AccountTransferDTO accountTransferDTO) {
            this.accountTransferDTO = accountTransferDTO;
        }
    }
}
