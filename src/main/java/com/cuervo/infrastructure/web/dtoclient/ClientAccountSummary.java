package com.cuervo.infrastructure.web.dtoclient;

public record ClientAccountSummary(

        String identificationNumber,
        String firstName,
        String lastName,
        int accountCount

) {
}