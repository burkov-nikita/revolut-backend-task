package com.revolut.backend.task.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "ACCOUNT_ENTRY")
public class AccountEntry {

    public AccountEntry(Account creditAccount, Account debitAccount, BigDecimal amount) {
        this.debitAccount = debitAccount;
        this.creditAccount = creditAccount;
        this.amount = amount;
    }

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "ID")
    private UUID id;

    @OneToOne
    @JoinColumn(name = "debit_account_num", referencedColumnName = "id")
    @JsonIgnoreProperties({"saldo"})
    private Account debitAccount;

    @OneToOne
    @JoinColumn(name = "credit_account_num", referencedColumnName = "id")
    @JsonIgnoreProperties({"saldo"})
    private Account creditAccount;

    @Column(name = "AMOUNT", nullable = false)
    private BigDecimal amount;
}
