package com.cuervo.application.service.client;

import com.cuervo.domain.enums.IdentificationType;
import com.cuervo.domain.exception.EntityNotFoundException;
import com.cuervo.domain.exception.InvalidClientException;
import com.cuervo.domain.model.Client;
import com.cuervo.domain.port.out.AccountRepositoryPort;
import com.cuervo.domain.port.out.ClientRepositoryPort;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DeleteClientServiceTest {

    private ClientRepositoryPort clientRepositoryPort;
    private AccountRepositoryPort accountRepositoryPort;

    private DeleteClientService deleteClientService;

    @BeforeEach
    void setUp() {
        clientRepositoryPort =
                mock(ClientRepositoryPort.class);

        accountRepositoryPort =
                mock(AccountRepositoryPort.class);

        deleteClientService =
                new DeleteClientService(
                        clientRepositoryPort,
                        accountRepositoryPort
                );
    }

    @Test
    void shouldDeleteClientWhenClientHasNoAccounts() {

        IdentificationType identificationType =
                IdentificationType.CC;

        String identificationNumber =
                "1075234567";

        Client client = mock(Client.class);

        when(
                clientRepositoryPort
                        .findByIdentificationTypeAndIdentificationNumber(
                                identificationType,
                                identificationNumber
                        )
        ).thenReturn(Optional.of(client));

        when(client.getId())
                .thenReturn(1L);

        when(
                accountRepositoryPort
                        .existsByClientId(1L)
        ).thenReturn(false);

        deleteClientService.execute(
                identificationType,
                identificationNumber
        );

        verify(client).delete();

        verify(
                clientRepositoryPort
        ).save(client);

        verify(
                clientRepositoryPort,
                never()
        ).deleteByIdentificationTypeAndIdentificationNumber(
                identificationType,
                identificationNumber
        );
    }

    @Test
    void shouldThrowExceptionWhenClientHasAccounts() {

        IdentificationType identificationType =
                IdentificationType.CC;

        String identificationNumber =
                "1075234567";

        Client client = mock(Client.class);

        when(
                clientRepositoryPort
                        .findByIdentificationTypeAndIdentificationNumber(
                                identificationType,
                                identificationNumber
                        )
        ).thenReturn(Optional.of(client));

        when(client.getId())
                .thenReturn(1L);

        when(
                accountRepositoryPort
                        .existsByClientId(1L)
        ).thenReturn(true);

        assertThrows(
                InvalidClientException.class,
                () -> deleteClientService.execute(
                        identificationType,
                        identificationNumber
                )
        );

        verify(
                client,
                never()
        ).delete();

        verify(
                clientRepositoryPort,
                never()
        ).save(any(Client.class));

        verify(
                clientRepositoryPort,
                never()
        ).deleteByIdentificationTypeAndIdentificationNumber(
                any(),
                anyString()
        );
    }

    @Test
    void shouldThrowExceptionWhenClientDoesNotExist() {

        IdentificationType identificationType =
                IdentificationType.CC;

        String identificationNumber =
                "9999999999";

        when(
                clientRepositoryPort
                        .findByIdentificationTypeAndIdentificationNumber(
                                identificationType,
                                identificationNumber
                        )
        ).thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> deleteClientService.execute(
                        identificationType,
                        identificationNumber
                )
        );

        verifyNoInteractions(
                accountRepositoryPort
        );

        verify(
                clientRepositoryPort,
                never()
        ).save(any(Client.class));
    }
}