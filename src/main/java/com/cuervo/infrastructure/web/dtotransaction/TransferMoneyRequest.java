package com.cuervo.infrastructure.web.dtotransaction;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransferMoneyRequest(

        @NotBlank
        String sourceAccountNumber,

        @NotBlank
        String destinationAccountNumber,

        @NotNull
        @Positive
        BigDecimal amount

) {
}