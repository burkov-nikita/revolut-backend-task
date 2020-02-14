package com.revolut.backend.task.entity;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.function.BiFunction;

import static java.util.Arrays.asList;

@Entity
@Table(name = "ACCOUNT")
public class Account {

    public Account() {
    }

    public Account(Long currencyId, String metadata) {
        this.currencyId = currencyId;
        this.metadata = metadata;
    }

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

    @Column(name = "SALDO")
    private BigDecimal saldo = new BigDecimal(0);

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

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public Account changeSaldo(BigDecimal amount, BiFunction<Account, BigDecimal, Account> function) {
        function.apply(this, amount);
        return this;
    }

    public boolean isSolvent(BigDecimal amount) {
        return asList(0, 1).contains(this.getSaldo()
                .subtract(amount)
                .compareTo(new BigDecimal(0)));
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", currencyId=" + currencyId +
                ", num='" + num + '\'' +
                ", metadata='" + metadata + '\'' +
                ", saldo=" + saldo +
                '}';
    }
}
