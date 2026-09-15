package com.cuervo.application.service.client;

import com.cuervo.domain.model.Client;
import com.cuervo.domain.port.out.ClientRepositoryPort;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CreateClientServiceTest {

    @Test
    void shouldCreateClientSuccessfully() {

        ClientRepositoryPort clientRepositoryPort =
                mock(ClientRepositoryPort.class);

        CreateClientService service =
                new CreateClientService(clientRepositoryPort);

        Client client = mock(Client.class);
        Client savedClient = mock(Client.class);

        when(clientRepositoryPort.save(client))
                .thenReturn(savedClient);

        Client result = service.execute(client);

        assertEquals(savedClient, result);

        verify(clientRepositoryPort).save(client);
    }
}