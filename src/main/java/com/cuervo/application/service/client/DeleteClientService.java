package com.cuervo.application.service.client;

import com.cuervo.application.port.in.client.DeleteClientUseCase;
import com.cuervo.domain.exception.EntityNotFoundException;
import com.cuervo.domain.exception.InvalidClientException;
import com.cuervo.domain.port.out.AccountRepositoryPort;
import com.cuervo.domain.port.out.ClientRepositoryPort;

public class DeleteClientService implements DeleteClientUseCase {

    private final ClientRepositoryPort clientRepositoryPort;
    private final AccountRepositoryPort accountRepositoryPort;

    public DeleteClientService(
            ClientRepositoryPort clientRepositoryPort,
            AccountRepositoryPort accountRepositoryPort) {

        this.clientRepositoryPort = clientRepositoryPort;
        this.accountRepositoryPort = accountRepositoryPort;
    }

    @Override
    public void execute(Long id) {

        clientRepositoryPort.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Client not found"));

        boolean hasAccounts =
                accountRepositoryPort.existsByClientId(id);

        if (hasAccounts) {
            throw new InvalidClientException(
                    "Client cannot be deleted because it has accounts"
            );
        }

        clientRepositoryPort.deleteById(id);
    }
}