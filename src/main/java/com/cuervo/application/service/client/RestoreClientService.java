package com.cuervo.application.service.client;

import com.cuervo.application.port.in.client.RestoreClientUseCase;
import com.cuervo.domain.enums.IdentificationType;
import com.cuervo.domain.exception.EntityNotFoundException;
import com.cuervo.domain.model.Client;
import com.cuervo.domain.port.out.ClientRepositoryPort;

public class RestoreClientService
        implements RestoreClientUseCase {

    private final ClientRepositoryPort clientRepositoryPort;

    public RestoreClientService(
            ClientRepositoryPort clientRepositoryPort) {

        this.clientRepositoryPort =
                clientRepositoryPort;
    }

    @Override
    public Client restore(
            IdentificationType identificationType,
            String identificationNumber) {

        Client client =
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

        client.restore();

        return clientRepositoryPort.save(client);
    }
}