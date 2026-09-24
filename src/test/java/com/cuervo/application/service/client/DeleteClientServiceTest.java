package com.cuervo.application.service.client;

import com.cuervo.domain.enums.IdentificationType;
import com.cuervo.domain.exception.InvalidClientException;
import com.cuervo.domain.model.Client;
import com.cuervo.domain.port.out.AccountRepositoryPort;
import com.cuervo.domain.port.out.ClientRepositoryPort;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DeleteClientServiceTest {

    @Test
    void shouldRejectDeleteWhenClientHasAccounts() {

        ClientRepositoryPort clientRepositoryPort =
                mock(ClientRepositoryPort.class);

        AccountRepositoryPort accountRepositoryPort =
                mock(AccountRepositoryPort.class);

        DeleteClientService service =
                new DeleteClientService(
                        clientRepositoryPort,
                        accountRepositoryPort
                );

        Client client =
                mock(Client.class);

        IdentificationType identificationType =
                IdentificationType.CC;

        String identificationNumber =
                "1075234567";

        when(
                clientRepositoryPort
                        .findByIdentificationTypeAndIdentificationNumber(
                                identificationType,
                                identificationNumber
                        )
        ).thenReturn(
                Optional.of(client)
        );

        when(client.getId())
                .thenReturn(1L);

        when(
                accountRepositoryPort
                        .existsByClientId(1L)
        ).thenReturn(true);

        assertThrows(
                InvalidClientException.class,
                () -> service.execute(
                        identificationType,
                        identificationNumber
                )
        );

        verify(
                clientRepositoryPort,
                never()
        ).deleteByIdentificationTypeAndIdentificationNumber(
                any(IdentificationType.class),
                anyString()
        );
    }

    @Test
    void shouldDeleteClientWhenClientHasNoAccounts() {

        ClientRepositoryPort clientRepositoryPort =
                mock(ClientRepositoryPort.class);

        AccountRepositoryPort accountRepositoryPort =
                mock(AccountRepositoryPort.class);

        DeleteClientService service =
                new DeleteClientService(
                        clientRepositoryPort,
                        accountRepositoryPort
                );

        Client client =
                mock(Client.class);

        IdentificationType identificationType =
                IdentificationType.CC;

        String identificationNumber =
                "1075234567";

        when(
                clientRepositoryPort
                        .findByIdentificationTypeAndIdentificationNumber(
                                identificationType,
                                identificationNumber
                        )
        ).thenReturn(
                Optional.of(client)
        );

        when(client.getId())
                .thenReturn(1L);

        when(
                accountRepositoryPort
                        .existsByClientId(1L)
        ).thenReturn(false);

        service.execute(
                identificationType,
                identificationNumber
        );

        verify(
                clientRepositoryPort
        ).deleteByIdentificationTypeAndIdentificationNumber(
                identificationType,
                identificationNumber
        );
    }
}