package com.cuervo.application.service.client;

import com.cuervo.domain.enums.IdentificationType;
import com.cuervo.domain.exception.InvalidClientException;
import com.cuervo.domain.model.Client;
import com.cuervo.domain.port.out.ClientRepositoryPort;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CreateClientServiceTest {

    @Test
    void shouldCreateClientSuccessfully() {

        ClientRepositoryPort clientRepositoryPort =
                mock(ClientRepositoryPort.class);

        CreateClientService service =
                new CreateClientService(
                        clientRepositoryPort
                );

        Client client =
                mock(Client.class);

        Client savedClient =
                mock(Client.class);

        when(client.getIdentificationType())
                .thenReturn(
                        IdentificationType.CC
                );

        when(client.getIdentificationNumber())
                .thenReturn(
                        "1075000000"
                );

        when(
                clientRepositoryPort
                        .findByIdentificationTypeAndIdentificationNumber(
                                IdentificationType.CC,
                                "1075000000"
                        )
        ).thenReturn(
                Optional.empty()
        );

        when(clientRepositoryPort.save(client))
                .thenReturn(savedClient);

        Client result =
                service.execute(client);

        assertEquals(
                savedClient,
                result
        );

        verify(
                clientRepositoryPort
        ).findByIdentificationTypeAndIdentificationNumber(
                IdentificationType.CC,
                "1075000000"
        );

        verify(
                clientRepositoryPort
        ).save(client);
    }

    @Test
    void shouldRejectDuplicatedClientIdentification() {

        ClientRepositoryPort clientRepositoryPort =
                mock(ClientRepositoryPort.class);

        CreateClientService service =
                new CreateClientService(
                        clientRepositoryPort
                );

        Client client =
                mock(Client.class);

        Client existingClient =
                mock(Client.class);

        when(client.getIdentificationType())
                .thenReturn(
                        IdentificationType.CC
                );

        when(client.getIdentificationNumber())
                .thenReturn(
                        "1075000000"
                );

        when(
                clientRepositoryPort
                        .findByIdentificationTypeAndIdentificationNumber(
                                IdentificationType.CC,
                                "1075000000"
                        )
        ).thenReturn(
                Optional.of(existingClient)
        );

        assertThrows(
                InvalidClientException.class,
                () -> service.execute(client)
        );

        verify(
                clientRepositoryPort,
                never()
        ).save(any(Client.class));
    }
}