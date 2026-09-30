package com.cuervo.application.service.account;

import com.cuervo.application.port.in.account.RestoreAccountUseCase;
import com.cuervo.domain.exception.EntityNotFoundException;
import com.cuervo.domain.model.Account;
import com.cuervo.domain.port.out.AccountRepositoryPort;

public class RestoreAccountService implements RestoreAccountUseCase {

    private final AccountRepositoryPort accountRepositoryPort;

    public RestoreAccountService(
            AccountRepositoryPort accountRepositoryPort
    ) {
        this.accountRepositoryPort = accountRepositoryPort;
    }

    @Override
    public Account restore(String accountNumber) {

        Account account = accountRepositoryPort
                .findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Account not found"
                        )
                );

        account.restore();

        return accountRepositoryPort.save(account);
    }
}