package com.cuervo.infrastructure.web.dtoaccount;

import com.cuervo.domain.enums.AccountStatus;
import com.cuervo.domain.enums.AccountType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AccountResponse(

        AccountType accountType,
        String accountNumber,
        AccountStatus status,
        BigDecimal balance,
        BigDecimal availableBalance,
        Boolean gmfExempt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt

) {
}