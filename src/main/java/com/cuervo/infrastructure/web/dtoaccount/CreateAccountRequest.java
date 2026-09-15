package com.cuervo.infrastructure.web.dtoaccount;

import com.cuervo.domain.enums.AccountType;
import jakarta.validation.constraints.NotNull;

public record CreateAccountRequest(

        @NotNull(message = "Account type is required")
        AccountType accountType,

        @NotNull(message = "Client ID is required")
        Long clientId

) {
}
                                                                        