package com.revolut.backend.task.entity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "ACCOUNT_ENTRY")
public class AccountEntry extends AbstractIdentifiableObject {

    public AccountEntry() {
    }

    public AccountEntry(Account debitAccount, Account creditAccount, BigDecimal amount) {
        this.debitAccount = debitAccount;
        this.creditAccount = creditAccount;
        this.amount = amount;
    }

    @OneToOne(fetch = FetchType.LAZY)
    private Account debitAccount;

    @OneToOne(fetch = FetchType.LAZY)
    private Account creditAccount;

    @Column(name = "AMOUNT", nullable = false)
    private BigDecimal amount;

    public Account getDebitAccount() {
        return debitAccount;
    }

    public void setDebitAccount(Account debitAccount) {
        this.debitAccount = debitAccount;
    }

    public Account getCreditAccount() {
        return creditAccount;
    }

    public void setCreditAccount(Account creditAccount) {
        this.creditAccount = creditAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }


    @Override
    public String toString() {
        return "AccountEntry{" +
                ", debitAccount=" + debitAccount +
                ", creditAccount=" + creditAccount +
                ", amount=" + amount +
                '}';
    }
}
