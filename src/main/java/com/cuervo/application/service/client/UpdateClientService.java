package com.cuervo.application.service.client;

import com.cuervo.application.port.in.client.UpdateClientUseCase;
import com.cuervo.domain.exception.EntityNotFoundException;
import com.cuervo.domain.model.Client;
import com.cuervo.domain.port.out.ClientRepositoryPort;

public class UpdateClientService implements UpdateClientUseCase {

    private final ClientRepositoryPort clientRepositoryPort;

    public UpdateClientService(
            ClientRepositoryPort clientRepositoryPort) {

        this.clientRepositoryPort = clientRepositoryPort;
    }

    @Override
    public Client execute(
            String identificationNumber,
            Client client) {

        Client existingClient = clientRepositoryPort
                .findByIdentificationNumber(identificationNumber)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Client not found"
                        ));

        existingClient.updateInformation(
                client.getFirstName(),
                client.getLastName(),
                client.getEmail()
        );

        return clientRepositoryPort.save(existingClient);
    }
}