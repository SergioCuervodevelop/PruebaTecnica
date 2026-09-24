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
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateAccountServiceTest {

    @Mock
    private AccountRepositoryPort accountRepositoryPort;

    @Mock
    private ClientRepositoryPort clientRepositoryPort;

    @Mock
    private AccountNumberGeneratorPort accountNumberGeneratorPort;

    @InjectMocks
    private CreateAccountService createAccountService;

    @Test
    void shouldCreateAccountSuccessfully() {

        String identificationNumber = "1075234567";

        Client client = mock(Client.class);

        when(client.getId())
                .thenReturn(1L);

        when(clientRepositoryPort
                .findByIdentificationNumber(identificationNumber))
                .thenReturn(Optional.of(client));

        when(accountRepositoryPort
                .existsByClientIdAndAccountType(
                        1L,
                        AccountType.SAVINGS))
                .thenReturn(false);

        when(accountNumberGeneratorPort
                .generate(AccountType.SAVINGS))
                .thenReturn("5312345678");

        when(accountRepositoryPort
                .findByAccountNumber("5312345678"))
                .thenReturn(Optional.empty());

        when(accountRepositoryPort.save(any(Account.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        Account result = createAccountService.create(
                AccountType.SAVINGS,
                identificationNumber
        );

        assertNotNull(result);

        assertEquals(
                AccountType.SAVINGS,
                result.getAccountType()
        );

        assertEquals(
                "5312345678",
                result.getAccountNumber()
        );

        assertEquals(
                AccountStatus.ACTIVE,
                result.getStatus()
        );

        assertEquals(
                1L,
                result.getClientId()
        );

        verify(clientRepositoryPort)
                .findByIdentificationNumber(
                        identificationNumber
                );

        verify(accountRepositoryPort)
                .save(any(Account.class));
    }

    @Test
    void shouldThrowExceptionWhenClientDoesNotExist() {

        String identificationNumber = "9999999999";

        when(clientRepositoryPort
                .findByIdentificationNumber(identificationNumber))
                .thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> createAccountService.create(
                        AccountType.SAVINGS,
                        identificationNumber
                )
        );

        verify(clientRepositoryPort)
                .findByIdentificationNumber(
                        identificationNumber
                );

        verifyNoInteractions(
                accountRepositoryPort,
                accountNumberGeneratorPort
        );
    }

    @Test
    void shouldThrowExceptionWhenClientAlreadyHasAccountType() {

        String identificationNumber = "1075234567";

        Client client = mock(Client.class);

        when(client.getId())
                .thenReturn(1L);

        when(clientRepositoryPort
                .findByIdentificationNumber(identificationNumber))
                .thenReturn(Optional.of(client));

        when(accountRepositoryPort
                .existsByClientIdAndAccountType(
                        1L,
                        AccountType.SAVINGS))
                .thenReturn(true);

        assertThrows(
                InvalidAccountStateException.class,
                () -> createAccountService.create(
                        AccountType.SAVINGS,
                        identificationNumber
                )
        );

        verify(accountRepositoryPort, never())
                .save(any(Account.class));
    }
}