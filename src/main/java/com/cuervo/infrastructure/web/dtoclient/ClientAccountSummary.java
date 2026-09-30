package com.cuervo.infrastructure.web.dtoclient;

import com.cuervo.domain.enums.ClientStatus;
import com.cuervo.domain.enums.IdentificationType;

public record ClientAccountSummary(

        IdentificationType identificationType,
        String identificationNumber,
        String firstName,
        String lastName,
        ClientStatus status,
        int accountCount

) {
}