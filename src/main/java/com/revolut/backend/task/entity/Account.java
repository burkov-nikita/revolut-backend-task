package com.revolut.backend.task.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.function.BiFunction;

import static java.util.Arrays.asList;
import static org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Cache(usage = READ_WRITE, region = "account")
@Table(name = "ACCOUNT")
public class Account {

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

    @Column(name = "CURRENCY_ID", updatable = false, nullable = false)
    private Long currencyId;

    @Column(name = "NUM", unique = true, nullable = false, updatable = false)
    private String num;

    @Column(name = "METADATA")
    private String metadata;

    @Column(name = "SALDO")
    private BigDecimal saldo = new BigDecimal(0);

    public Account changeSaldo(BigDecimal amount, BiFunction<Account, BigDecimal, Account> function) {
        function.apply(this, amount);
        return this;
    }

    public boolean isSolvent(BigDecimal amount) {
        return asList(0, 1).contains(saldo
                .subtract(amount)
                .compareTo(new BigDecimal(0)));
    }
}
