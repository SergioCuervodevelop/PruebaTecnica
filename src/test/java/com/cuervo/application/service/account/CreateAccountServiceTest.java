package com.cuervo.application.service.account;

import com.cuervo.domain.enums.AccountStatus;
import com.cuervo.domain.enums.AccountType;
import com.cuervo.domain.exception.EntityNotFoundException;
import com.cuervo.domain.exception.InvalidAccountStateException;
import com.cuervo.domain.model.Account;
import com.cuervo.domain.model.Client;
import com.cuervo.domain.port.out.AccountNumberGeneratorPort;
import com.cuervo.domain.port.out.AccountRepositoryPort;
import com.cuervo.domain.port.out.ClientRepositoryPort;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CreateAccountServiceTest {

    @Test
    void shouldCreateAccountSuccessfully() {

        AccountRepositoryPort accountRepositoryPort =
                mock(AccountRepositoryPort.class);

        ClientRepositoryPort clientRepositoryPort =
                mock(ClientRepositoryPort.class);

        AccountNumberGeneratorPort accountNumberGeneratorPort =
                mock(AccountNumberGeneratorPort.class);

        CreateAccountService service =
                new CreateAccountService(
                        accountRepositoryPort,
                        clientRepositoryPort,
                        accountNumberGeneratorPort
                );

        Account account = new Account(
                AccountType.SAVINGS,
                null,
                1L
        );

        Client client = mock(Client.class);

        when(clientRepositoryPort.findById(1L))
                .thenReturn(Optional.of(client));

        when(accountRepositoryPort.existsByClientIdAndAccountType(
                1L,
                AccountType.SAVINGS
        )).thenReturn(false);

        when(accountNumberGeneratorPort.generate(AccountType.SAVINGS))
                .thenReturn("5312345678");

        when(accountRepositoryPort.findByAccountNumber("5312345678"))
                .thenReturn(Optional.empty());

        when(accountRepositoryPort.save(account))
                .thenReturn(account);

        Account result = service.create(account);

        assertEquals("5312345678", result.getAccountNumber());
        assertEquals(AccountStatus.ACTIVE, result.getStatus());

        verify(accountRepositoryPort).save(account);
    }

    @Test
    void shouldThrowExceptionWhenClientDoesNotExist() {

        AccountRepositoryPort accountRepositoryPort =
                mock(AccountRepositoryPort.class);

        ClientRepositoryPort clientRepositoryPort =
                mock(ClientRepositoryPort.class);

        AccountNumberGeneratorPort accountNumberGeneratorPort =
                mock(AccountNumberGeneratorPort.class);

        CreateAccountService service =
                new CreateAccountService(
                        accountRepositoryPort,
                        clientRepositoryPort,
                        accountNumberGeneratorPort
                );

        Account account = new Account(
                AccountType.SAVINGS,
                null,
                99L
        );

        when(clientRepositoryPort.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> service.create(account)
        );
    }

    @Test
    void shouldRejectDuplicateAccountTypeForClient() {

        AccountRepositoryPort accountRepositoryPort =
                mock(AccountRepositoryPort.class);

        ClientRepositoryPort clientRepositoryPort =
                mock(ClientRepositoryPort.class);

        AccountNumberGeneratorPort accountNumberGeneratorPort =
                mock(AccountNumberGeneratorPort.class);

        CreateAccountService service =
                new CreateAccountService(
                        accountRepositoryPort,
                        clientRepositoryPort,
                        accountNumberGeneratorPort
                );

        Account account = new Account(
                AccountType.SAVINGS,
                null,
                1L
        );

        Client client = mock(Client.class);

        when(clientRepositoryPort.findById(1L))
                .thenReturn(Optional.of(client));

        when(accountRepositoryPort.existsByClientIdAndAccountType(
                1L,
                AccountType.SAVINGS
        )).thenReturn(true);

        assertThrows(
                InvalidAccountStateException.class,
                () -> service.create(account)
        );
    }
}