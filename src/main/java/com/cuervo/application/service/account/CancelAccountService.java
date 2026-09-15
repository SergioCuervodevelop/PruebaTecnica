package com.cuervo.application.service.account;

import com.cuervo.application.port.in.account.CancelAccountUseCase;
import com.cuervo.domain.exception.EntityNotFoundException;
import com.cuervo.domain.model.Account;
import com.cuervo.domain.port.out.AccountRepositoryPort;

public class CancelAccountService
        implements CancelAccountUseCase {

    private final AccountRepositoryPort accountRepositoryPort;

    public CancelAccountService(
            AccountRepositoryPort accountRepositoryPort) {

        this.accountRepositoryPort = accountRepositoryPort;
    }

    @Override
    public Account cancel(Long id) {

        Account account = accountRepositoryPort.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Account not found"));

        account.cancel();

        return accountRepositoryPort.save(account);
    }
}