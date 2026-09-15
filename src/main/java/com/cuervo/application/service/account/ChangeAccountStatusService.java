package com.cuervo.application.service.account;

import com.cuervo.application.port.in.account.ChangeAccountStatusUseCase;
import com.cuervo.domain.enums.AccountStatus;
import com.cuervo.domain.exception.EntityNotFoundException;
import com.cuervo.domain.exception.InvalidAccountStateException;
import com.cuervo.domain.model.Account;
import com.cuervo.domain.port.out.AccountRepositoryPort;

public class ChangeAccountStatusService
        implements ChangeAccountStatusUseCase {

    private final AccountRepositoryPort accountRepositoryPort;

    public ChangeAccountStatusService(
            AccountRepositoryPort accountRepositoryPort) {

        this.accountRepositoryPort = accountRepositoryPort;
    }

    @Override
    public Account change(Long id, AccountStatus status) {

        Account account = accountRepositoryPort.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Account not found"));

        if (status == null) {
            throw new InvalidAccountStateException(
                    "Account status is required"
            );
        }

        switch (status) {

            case ACTIVE -> account.activate();

            case INACTIVE -> account.deactivate();

            case CANCELLED -> throw new InvalidAccountStateException(
                    "Use the cancellation operation to cancel an account"
            );
        }

        return accountRepositoryPort.save(account);
    }
}