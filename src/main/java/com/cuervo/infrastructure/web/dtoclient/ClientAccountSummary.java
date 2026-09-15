package com.cuervo.infrastructure.web.dtoclient;


public record ClientAccountSummary(
        Long clientId,
        String firstName,
        String lastName,
        int accountCount
) {
}