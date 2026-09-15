package com.cuervo.application.service.account;

import com.cuervo.application.port.in.account.GetAccountUseCase;
import com.cuervo.domain.exception.EntityNotFoundException;
import com.cuervo.domain.model.Account;
import com.cuervo.domain.port.out.AccountRepositoryPort;

public class GetAccountService implements GetAccountUseCase {

    private final AccountRepositoryPort accountRepositoryPort;

    public GetAccountService(AccountRepositoryPort accountRepositoryPort) {
        this.accountRepositoryPort = accountRepositoryPort;
    }

    @Override
    public Account getById(Long id) {

        return accountRepositoryPort.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Account not found"
                ));
    }
}