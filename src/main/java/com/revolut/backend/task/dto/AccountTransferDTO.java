package com.revolut.backend.task.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

import static com.revolut.backend.task.util.AccountGenerator.getUUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountTransferDTO implements Serializable {

    private static final long serialVersionUID = -8468082668739042312L;

    private UUID creditAccountId;
    private UUID debitAccountId;
    private String creditAccountNumber;
    private String debitAccountNumber;
    private BigDecimal amount = new BigDecimal("0");

    public AccountTransferDTO(UUID creditAccountId, UUID debitAccountId, BigDecimal amount) {
        this.creditAccountId = creditAccountId;
        this.debitAccountId = debitAccountId;
        this.amount = amount;
    }

    public void setCreditAccountNumber(String creditAccountNumber) {
        this.creditAccountId = getUUID(creditAccountNumber);
        this.creditAccountNumber = creditAccountNumber;
    }

    public void setDebitAccountNumber(String debitAccountNumber) {
        this.debitAccountId = getUUID(debitAccountNumber);
        this.debitAccountNumber = debitAccountNumber;
    }
}
