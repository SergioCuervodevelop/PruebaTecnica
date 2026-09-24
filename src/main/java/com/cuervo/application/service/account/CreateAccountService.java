package com.cuervo.application.service.account;

import com.cuervo.application.port.in.account.CreateAccountUseCase;
import com.cuervo.domain.enums.AccountStatus;
import com.cuervo.domain.enums.AccountType;
import com.cuervo.domain.exception.EntityNotFoundException;
import com.cuervo.domain.exception.InvalidAccountStateException;
import com.cuervo.domain.model.Account;
import com.cuervo.domain.model.Client;
import com.cuervo.domain.port.out.AccountNumberGeneratorPort;
import com.cuervo.domain.port.out.AccountRepositoryPort;
import com.cuervo.domain.port.out.ClientRepositoryPort;

public class CreateAccountService implements CreateAccountUseCase {

    private final AccountRepositoryPort accountRepositoryPort;
    private final ClientRepositoryPort clientRepositoryPort;
    private final AccountNumberGeneratorPort accountNumberGeneratorPort;

    public CreateAccountService(
            AccountRepositoryPort accountRepositoryPort,
            ClientRepositoryPort clientRepositoryPort,
            AccountNumberGeneratorPort accountNumberGeneratorPort) {

        this.accountRepositoryPort = accountRepositoryPort;
        this.clientRepositoryPort = clientRepositoryPort;
        this.accountNumberGeneratorPort = accountNumberGeneratorPort;
    }

    @Override
    public Account create(AccountType accountType, String identificationNumber) {

        Client client = clientRepositoryPort.findByIdentificationNumber(identificationNumber)
                .orElseThrow(() ->
                        new EntityNotFoundException("Client not found"));

        Account account = new Account(
                accountType,
                null,
                client.getId()
        );

        boolean accountAlreadyExists =
                accountRepositoryPort
                        .existsByClientIdAndAccountType(
                                account.getClientId(),
                                account.getAccountType()
                        );

        if (accountAlreadyExists) {
            throw new InvalidAccountStateException(
                    "Client already has a " +
                            account.getAccountType() +
                            " account"
            );
        }

        String accountNumber;

        do {
            accountNumber =
                    accountNumberGeneratorPort.generate(
                            account.getAccountType()
                    );

        } while (
                accountRepositoryPort
                        .findByAccountNumber(accountNumber)
                        .isPresent()
        );

        account.setAccountNumber(accountNumber);
        account.setStatus(AccountStatus.ACTIVE);

        return accountRepositoryPort.save(account);
    }
}