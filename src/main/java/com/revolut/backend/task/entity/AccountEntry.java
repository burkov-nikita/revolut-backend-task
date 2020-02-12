package com.revolut.backend.task.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "ACCOUNT_ENTRY")
public class AccountEntry {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "ID")
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    private Account debitAccount;

    @OneToOne(fetch = FetchType.LAZY)
    private Account creditAccount;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @Column(name = "DESCRIPTION")
    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getDescriptiont() {
        return description;
    }

    public void setDescriptiont(String descriptiont) {
        this.description = descriptiont;
    }

    @Override
    public String toString() {
        return "AccountEntry{" +
                "id='" + id + '\'' +
                ", debitAccount=" + debitAccount +
                ", creditAccount=" + creditAccount +
                ", amount=" + amount +
                ", descriptiont='" + description + '\'' +
                '}';
    }
}
