package com.cuervo.application.service.client;

import com.cuervo.application.port.in.client.CreateClientUseCase;
import com.cuervo.domain.model.Client;
import com.cuervo.domain.port.out.ClientRepositoryPort;

public class CreateClientService implements CreateClientUseCase {

    private final ClientRepositoryPort clientRepositoryPort;

    public CreateClientService(ClientRepositoryPort clientRepositoryPort) {
        this.clientRepositoryPort = clientRepositoryPort;
    }

    @Override
    public Client execute(Client client) {
        return clientRepositoryPort.save(client);
    }
}