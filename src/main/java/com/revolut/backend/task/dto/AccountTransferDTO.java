package com.revolut.backend.task.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountTransferDTO {
    private UUID creditAccountId;
    private UUID debitAccountId;
    private BigDecimal amount;
}
