package com.cuervo.infrastructure.web.dtotransaction;

import com.cuervo.domain.enums.MovementType;
import com.cuervo.domain.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(

        TransactionType transactionType,
        MovementType movementType,
        BigDecimal amount,
        LocalDateTime transactionDate,
        String accountNumber,
        String transferId

) {
}