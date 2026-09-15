package com.cuervo.infrastructure.web.dtoclient;

import java.util.List;

public record ClientSummaryResponse(
        int totalClients,
        int totalAccounts,
        List<ClientAccountSummary> clients
) {
}