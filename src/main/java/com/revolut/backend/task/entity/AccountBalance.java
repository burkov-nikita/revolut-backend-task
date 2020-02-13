package com.revolut.backend.task.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "ACCOUT_BALANCE")
public class AccountBalance extends AbstractIdentifiableObject {

    @OneToOne(mappedBy = "accountBalance")
    @JsonIgnore
    private Account account;

    @Column(name = "SALDO")
    private BigDecimal saldo = new BigDecimal(0);

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    @Override
    public String toString() {
        return "AccountBalance{" +
                "account=" + account +
                ", saldo=" + saldo +
                '}';
    }
}
