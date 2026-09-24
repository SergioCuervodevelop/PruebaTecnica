package com.cuervo.application.service.client;

import com.cuervo.application.port.in.client.DeleteClientUseCase;
import com.cuervo.domain.enums.IdentificationType;
import com.cuervo.domain.exception.EntityNotFoundException;
import com.cuervo.domain.exception.InvalidClientException;
import com.cuervo.domain.model.Client;
import com.cuervo.domain.port.out.AccountRepositoryPort;
import com.cuervo.domain.port.out.ClientRepositoryPort;

public class DeleteClientService
        implements DeleteClientUseCase {

    private final ClientRepositoryPort clientRepositoryPort;
    private final AccountRepositoryPort accountRepositoryPort;

    public DeleteClientService(
            ClientRepositoryPort clientRepositoryPort,
            AccountRepositoryPort accountRepositoryPort) {

        this.clientRepositoryPort =
                clientRepositoryPort;

        this.accountRepositoryPort =
                accountRepositoryPort;
    }

    @Override
    public void execute(
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

        if (accountRepositoryPort
                .existsByClientId(client.getId())) {

            throw new InvalidClientException(
                    "No se puede eliminar el cliente porque tiene cuentas asociadas"
            );
        }

        clientRepositoryPort
                .deleteByIdentificationTypeAndIdentificationNumber(
                        identificationType,
                        identificationNumber
                );
    }
}