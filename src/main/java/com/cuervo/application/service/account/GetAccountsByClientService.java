package com.cuervo.application.service.account;

import com.cuervo.application.port.in.account.GetAccountsByClientUseCase;
import com.cuervo.domain.exception.EntityNotFoundException;
import com.cuervo.domain.model.Account;
import com.cuervo.domain.model.Client;
import com.cuervo.domain.port.out.AccountRepositoryPort;
import com.cuervo.domain.port.out.ClientRepositoryPort;

import java.util.List;

public class GetAccountsByClientService
        implements GetAccountsByClientUseCase {

    private final AccountRepositoryPort accountRepositoryPort;
    private final ClientRepositoryPort clientRepositoryPort;

    public GetAccountsByClientService(
            AccountRepositoryPort accountRepositoryPort,
            ClientRepositoryPort clientRepositoryPort) {

        this.accountRepositoryPort = accountRepositoryPort;
        this.clientRepositoryPort = clientRepositoryPort;
    }

    @Override
    public List<Account> getByIdentificationNumber(
            String identificationNumber) {

        Client client = clientRepositoryPort
                .findByIdentificationNumber(identificationNumber)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Client not found"
                        ));

        return accountRepositoryPort
                .findByClientId(client.getId());
    }
}