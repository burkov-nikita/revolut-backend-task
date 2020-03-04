package com.revolut.backend.task.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "ACCOUNT")
public class Account {

    public Account(Long currencyId, String metadata) {
        this.currencyId = currencyId;
        this.metadata = metadata;
    }

    public Account(Long currencyId, BigDecimal saldo, String metadata) {
        this.currencyId = currencyId;
        this.metadata = metadata;
        this.saldo = saldo;
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
}
