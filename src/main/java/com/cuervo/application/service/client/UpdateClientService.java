package com.cuervo.application.service.client;

import com.cuervo.application.port.in.client.UpdateClientUseCase;
import com.cuervo.domain.enums.IdentificationType;
import com.cuervo.domain.exception.EntityNotFoundException;
import com.cuervo.domain.model.Client;
import com.cuervo.domain.port.out.ClientRepositoryPort;

public class UpdateClientService
        implements UpdateClientUseCase {

    private final ClientRepositoryPort clientRepositoryPort;

    public UpdateClientService(
            ClientRepositoryPort clientRepositoryPort) {

        this.clientRepositoryPort =
                clientRepositoryPort;
    }

    @Override
    public Client execute(
            IdentificationType identificationType,
            String identificationNumber,
            Client client) {

        Client existingClient =
                clientRepositoryPort
                        .findByIdentificationTypeAndIdentificationNumber(
                                identificationType,
                                identificationNumber
                        )
                        .orElseThrow(
                                () -> new EntityNotFoundException(
                                        "Cliente no encontrado"
                                )
                        );

        existingClient.setFirstName(
                client.getFirstName()
        );

        existingClient.setLastName(
                client.getLastName()
        );

        existingClient.setEmail(
                client.getEmail()
        );

        return clientRepositoryPort.save(
                existingClient
        );
    }
}