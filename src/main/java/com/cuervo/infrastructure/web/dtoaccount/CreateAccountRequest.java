package com.cuervo.infrastructure.web.dtoaccount;

import com.cuervo.domain.enums.AccountType;
import com.cuervo.domain.enums.IdentificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateAccountRequest(

        @NotNull(message = "Account type is required")
        AccountType accountType,

        @NotNull(message = "Identification type is required")
        IdentificationType identificationType,

        @NotBlank(message = "Identification number is required")
        String identificationNumber

) {
}