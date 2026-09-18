package com.cuervo.application.service.account;

import com.cuervo.application.port.in.account.GetAccountsByClientUseCase;
import com.cuervo.domain.model.Account;
import com.cuervo.domain.port.out.AccountRepositoryPort;

import java.util.List;

public class GetAccountsByClientService implements GetAccountsByClientUseCase {

    private final AccountRepositoryPort accountRepositoryPort;

    public GetAccountsByClientService(
            AccountRepositoryPort accountRepositoryPort) {
        this.accountRepositoryPort = accountRepositoryPort;
    }

    @Override
    public List<Account> getByClientId(Long clientId) {
        return accountRepositoryPort.findByClientId(clientId);
    }
}