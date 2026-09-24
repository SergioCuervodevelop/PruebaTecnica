package com.cuervo.application.service.account;

import com.cuervo.application.port.in.account.CreateAccountUseCase;
import com.cuervo.domain.enums.AccountType;
import com.cuervo.domain.enums.IdentificationType;
import com.cuervo.domain.exception.EntityNotFoundException;
import com.cuervo.domain.exception.InvalidAccountStateException;
import com.cuervo.domain.model.Account;
import com.cuervo.domain.model.Client;
import com.cuervo.domain.port.out.AccountNumberGeneratorPort;
import com.cuervo.domain.port.out.AccountRepositoryPort;
import com.cuervo.domain.port.out.ClientRepositoryPort;

public class CreateAccountService
        implements CreateAccountUseCase {

    private final AccountRepositoryPort accountRepositoryPort;
    private final ClientRepositoryPort clientRepositoryPort;
    private final AccountNumberGeneratorPort accountNumberGeneratorPort;

    public CreateAccountService(
            AccountRepositoryPort accountRepositoryPort,
            ClientRepositoryPort clientRepositoryPort,
            AccountNumberGeneratorPort accountNumberGeneratorPort) {

        this.accountRepositoryPort =
                accountRepositoryPort;

        this.clientRepositoryPort =
                clientRepositoryPort;

        this.accountNumberGeneratorPort =
                accountNumberGeneratorPort;
    }

    @Override
    public Account create(
            AccountType accountType,
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
                .existsByClientIdAndAccountType(
                        client.getId(),
                        accountType
                )) {

            throw new InvalidAccountStateException(
                    "El cliente ya tiene una cuenta de este tipo"
            );
        }

        String accountNumber;

        do {
            accountNumber =
                    accountNumberGeneratorPort
                            .generate(accountType);

        } while (
                accountRepositoryPort
                        .findByAccountNumber(accountNumber)
                        .isPresent()
        );

        Account account =
                new Account(
                        accountType,
                        accountNumber,
                        client.getId()
                );

        return accountRepositoryPort.save(account);
    }
}