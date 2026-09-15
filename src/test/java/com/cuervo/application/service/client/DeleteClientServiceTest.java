package com.cuervo.application.service.client;

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

        Client client = mock(Client.class);

        when(clientRepositoryPort.findById(1L))
                .thenReturn(Optional.of(client));

        when(accountRepositoryPort.existsByClientId(1L))
                .thenReturn(true);

        assertThrows(
                InvalidClientException.class,
                () -> service.execute(1L)
        );

        verify(clientRepositoryPort, never())
                .deleteById(1L);
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

        Client client = mock(Client.class);

        when(clientRepositoryPort.findById(1L))
                .thenReturn(Optional.of(client));

        when(accountRepositoryPort.existsByClientId(1L))
                .thenReturn(false);

        service.execute(1L);

        verify(clientRepositoryPort).deleteById(1L);
    }
}