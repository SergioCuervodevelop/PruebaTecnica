package com.cuervo.application.service.client;

import com.cuervo.application.port.in.client.GetClientSummaryUseCase;
import com.cuervo.domain.model.Client;
import com.cuervo.domain.port.out.AccountRepositoryPort;
import com.cuervo.domain.port.out.ClientRepositoryPort;
import com.cuervo.infrastructure.web.dtoclient.ClientAccountSummary;
import com.cuervo.infrastructure.web.dtoclient.ClientSummaryResponse;

import java.util.List;

public class GetClientSummaryService implements GetClientSummaryUseCase {

    private final ClientRepositoryPort clientRepositoryPort;
    private final AccountRepositoryPort accountRepositoryPort;

    public GetClientSummaryService(
            ClientRepositoryPort clientRepositoryPort,
            AccountRepositoryPort accountRepositoryPort) {

        this.clientRepositoryPort = clientRepositoryPort;
        this.accountRepositoryPort = accountRepositoryPort;
    }

    @Override
    public ClientSummaryResponse execute() {

        List<Client> clients =
                clientRepositoryPort.findAll();

        List<ClientAccountSummary> clientSummaries =
                clients.stream()
                        .map(client -> {

                            int accountCount =
                                    accountRepositoryPort
                                            .findByClientId(
                                                    client.getId()
                                            )
                                            .size();

                            return new ClientAccountSummary(
                                    client.getIdentificationNumber(),
                                    client.getFirstName(),
                                    client.getLastName(),
                                    accountCount
                            );
                        })
                        .toList();

        int totalAccounts =
                clientSummaries.stream()
                        .mapToInt(
                                ClientAccountSummary::accountCount
                        )
                        .sum();

        return new ClientSummaryResponse(
                clients.size(),
                totalAccounts,
                clientSummaries
        );
    }
}