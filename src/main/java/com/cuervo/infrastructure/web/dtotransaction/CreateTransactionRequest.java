package com.cuervo.infrastructure.web.dtotransaction;

import com.cuervo.domain.enums.TransactionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreateTransactionRequest(

        @NotNull
        Long accountId,

        @NotNull
        TransactionType transactionType,

        @NotNull
        @Positive
        BigDecimal amount

) {
}