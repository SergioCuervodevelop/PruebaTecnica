package com.cuervo.infrastructure.web.controller;

import com.cuervo.application.port.in.client.*;
import com.cuervo.domain.model.Client;
import com.cuervo.infrastructure.web.dtoclient.ClientResponse;
import com.cuervo.infrastructure.web.dtoclient.CreateClientRequest;
import com.cuervo.infrastructure.web.mapper.ClientWebMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ClientControllerTest {

    @Test
    void shouldCreateClientSuccessfully() {

        CreateClientUseCase createClientUseCase =
                mock(CreateClientUseCase.class);

        GetClientUseCase getClientUseCase =
                mock(GetClientUseCase.class);

        UpdateClientUseCase updateClientUseCase =
                mock(UpdateClientUseCase.class);

        DeleteClientUseCase deleteClientUseCase =
                mock(DeleteClientUseCase.class);

        GetClientSummaryUseCase getClientSummaryUseCase =
                mock(GetClientSummaryUseCase.class);

        ClientWebMapper clientWebMapper =
                mock(ClientWebMapper.class);

        ClientController controller =
                new ClientController(
                        createClientUseCase,
                        getClientUseCase,
                        updateClientUseCase,
                        deleteClientUseCase,
                        getClientSummaryUseCase,
                        clientWebMapper
                );

        CreateClientRequest request =
                mock(CreateClientRequest.class);

        Client client =
                mock(Client.class);

        Client createdClient =
                mock(Client.class);

        ClientResponse response =
                mock(ClientResponse.class);

        when(clientWebMapper.toDomain(request))
                .thenReturn(client);

        when(createClientUseCase.execute(client))
                .thenReturn(createdClient);

        when(clientWebMapper.toResponse(createdClient))
                .thenReturn(response);

        ResponseEntity<ClientResponse> result =
                controller.createClient(request);

        assertEquals(
                201,
                result.getStatusCode().value()
        );

        assertEquals(
                response,
                result.getBody()
        );

        verify(clientWebMapper)
                .toDomain(request);

        verify(createClientUseCase)
                .execute(client);

        verify(clientWebMapper)
                .toResponse(createdClient);
    }

    @Test
    void shouldGetClientSuccessfully() {

        CreateClientUseCase createClientUseCase =
                mock(CreateClientUseCase.class);

        GetClientUseCase getClientUseCase =
                mock(GetClientUseCase.class);

        UpdateClientUseCase updateClientUseCase =
                mock(UpdateClientUseCase.class);

        DeleteClientUseCase deleteClientUseCase =
                mock(DeleteClientUseCase.class);

        GetClientSummaryUseCase getClientSummaryUseCase =
                mock(GetClientSummaryUseCase.class);

        ClientWebMapper clientWebMapper =
                mock(ClientWebMapper.class);

        ClientController controller =
                new ClientController(
                        createClientUseCase,
                        getClientUseCase,
                        updateClientUseCase,
                        deleteClientUseCase,
                        getClientSummaryUseCase,
                        clientWebMapper
                );

        Client client =
                mock(Client.class);

        ClientResponse response =
                mock(ClientResponse.class);

        String identificationNumber =
                "1075000000";

        when(getClientUseCase.execute(
                identificationNumber
        )).thenReturn(
                Optional.of(client)
        );

        when(clientWebMapper.toResponse(client))
                .thenReturn(response);

        ResponseEntity<ClientResponse> result =
                controller.getClient(
                        identificationNumber
                );

        assertEquals(
                200,
                result.getStatusCode().value()
        );

        assertEquals(
                response,
                result.getBody()
        );

        verify(getClientUseCase)
                .execute(
                        identificationNumber
                );

        verify(clientWebMapper)
                .toResponse(client);
    }
}