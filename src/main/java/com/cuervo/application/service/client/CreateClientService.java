package com.cuervo.application.service.client;

import com.cuervo.application.port.in.client.CreateClientUseCase;
import com.cuervo.domain.exception.InvalidClientException;
import com.cuervo.domain.model.Client;
import com.cuervo.domain.port.out.ClientRepositoryPort;

public class CreateClientService implements CreateClientUseCase {

    private final ClientRepositoryPort clientRepositoryPort;

    public CreateClientService(
            ClientRepositoryPort clientRepositoryPort) {

        this.clientRepositoryPort =
                clientRepositoryPort;
    }

    @Override
    public Client execute(Client client) {

        boolean clientExists =
                clientRepositoryPort
                        .findByIdentificationTypeAndIdentificationNumber(
                                client.getIdentificationType(),
                                client.getIdentificationNumber()
                        )
                        .isPresent();

        if (clientExists) {
            throw new InvalidClientException(
                    "A client with this identification already exists"
            );
        }

        return clientRepositoryPort.save(client);
    }
}