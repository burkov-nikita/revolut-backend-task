package com.revolut.backend.task.entity;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "ACCOUNT")
public class Account {

    @Id
    @GenericGenerator(
            name = "custom-uuid",
            strategy = "com.revolut.backend.task.dao.util.AccountCustomUuidGenerator",
            parameters = {
                    @Parameter(name = "accountNumber", value = "num")
            }
    )
    @GeneratedValue(generator = "custom-uuid", strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    private UUID id;

    @Column(name = "CURRENCY_ID")
    private Long currencyId;

    @Column(name = "NUM", unique = true)
    private String num;

    @Column(name = "METADATA")
    private String metadata;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ACCOUNT_BALANCE_ID")
    private AccountBalance accountBalance;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String info) {
        this.metadata = info;
    }

    public Long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public AccountBalance getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(AccountBalance accountBalance) {
        this.accountBalance = accountBalance;
        accountBalance.setAccount(this);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", currencyId=" + currencyId +
                ", num='" + num + '\'' +
                ", metadata='" + metadata + '\'' +
                ", accountBalance=" + accountBalance +
                '}';
    }
}
