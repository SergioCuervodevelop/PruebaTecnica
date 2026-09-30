package com.cuervo.infrastructure.web.dtoclient;

import com.cuervo.domain.enums.ClientStatus;
import com.cuervo.domain.enums.IdentificationType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ClientResponse(

        IdentificationType identificationType,
        String identificationNumber,
        String firstName,
        String lastName,
        String email,
        LocalDate birthDate,
        ClientStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt

) {
}