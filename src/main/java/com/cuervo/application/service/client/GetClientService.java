package com.cuervo.application.service.client;

import com.cuervo.application.port.in.client.GetClientUseCase;
import com.cuervo.domain.model.Client;
import com.cuervo.domain.port.out.ClientRepositoryPort;

import java.util.Optional;

public class GetClientService implements GetClientUseCase {

    private final ClientRepositoryPort clientRepositoryPort;

    public GetClientService(ClientRepositoryPort clientRepositoryPort) {
        this.clientRepositoryPort = clientRepositoryPort;
    }

    @Override
    public Optional<Client> execute(Long id) {
        return clientRepositoryPort.findById(id);
    }
}
