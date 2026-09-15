package com.cuervo.infrastructure.web.dtotransaction;

import com.cuervo.domain.enums.MovementType;
import com.cuervo.domain.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(
        Long id,
        TransactionType transactionType,
        MovementType movementType,
        BigDecimal amount,
        LocalDateTime transactionDate,
        Long accountId,
        String transferId
) {
}