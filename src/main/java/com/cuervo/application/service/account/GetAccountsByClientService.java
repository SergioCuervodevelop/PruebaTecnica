package com.cuervo.application.service.account;

import com.cuervo.application.port.in.account.GetAccountsByClientUseCase;
import com.cuervo.domain.enums.IdentificationType;
import com.cuervo.domain.exception.EntityNotFoundException;
import com.cuervo.domain.model.Account;
import com.cuervo.domain.model.Client;
import com.cuervo.domain.port.out.AccountRepositoryPort;
import com.cuervo.domain.port.out.ClientRepositoryPort;

import java.util.List;

public class GetAccountsByClientService
        implements GetAccountsByClientUseCase {

    private final ClientRepositoryPort clientRepositoryPort;
    private final AccountRepositoryPort accountRepositoryPort;

    public GetAccountsByClientService(
            ClientRepositoryPort clientRepositoryPort,
            AccountRepositoryPort accountRepositoryPort) {

        this.clientRepositoryPort =
                clientRepositoryPort;

        this.accountRepositoryPort =
                accountRepositoryPort;
    }

    @Override
    public List<Account> getByIdentification(
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

        return accountRepositoryPort
                .findByClientId(
                        client.getId()
                );
    }
}