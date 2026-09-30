package com.cuervo.infrastructure.web.controller;

import com.cuervo.application.port.in.client.*;
import com.cuervo.domain.enums.IdentificationType;
import com.cuervo.domain.model.Client;
import com.cuervo.infrastructure.persistence.mapper.ClientMapper;
import com.cuervo.infrastructure.web.dtoclient.ClientResponse;
import com.cuervo.infrastructure.web.dtoclient.CreateClientRequest;
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

        RestoreClientUseCase restoreClientUseCase =
                mock(RestoreClientUseCase.class);

        GetClientSummaryUseCase getClientSummaryUseCase =
                mock(GetClientSummaryUseCase.class);

        ClientMapper clientMapper =
                mock(ClientMapper.class);

        ClientController controller =
                new ClientController(
                        createClientUseCase,
                        getClientUseCase,
                        updateClientUseCase,
                        deleteClientUseCase,
                        restoreClientUseCase,
                        getClientSummaryUseCase,
                        clientMapper
                );

        CreateClientRequest request =
                mock(CreateClientRequest.class);

        Client client =
                mock(Client.class);

        Client createdClient =
                mock(Client.class);

        ClientResponse response =
                mock(ClientResponse.class);

        when(clientMapper.toDomain(request))
                .thenReturn(client);

        when(createClientUseCase.execute(client))
                .thenReturn(createdClient);

        when(clientMapper.toResponse(createdClient))
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

        verify(clientMapper)
                .toDomain(request);

        verify(createClientUseCase)
                .execute(client);

        verify(clientMapper)
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

        RestoreClientUseCase restoreClientUseCase =
                mock(RestoreClientUseCase.class);

        GetClientSummaryUseCase getClientSummaryUseCase =
                mock(GetClientSummaryUseCase.class);

        ClientMapper clientMapper =
                mock(ClientMapper.class);

        ClientController controller =
                new ClientController(
                        createClientUseCase,
                        getClientUseCase,
                        updateClientUseCase,
                        deleteClientUseCase,
                        restoreClientUseCase,
                        getClientSummaryUseCase,
                        clientMapper
                );

        Client client =
                mock(Client.class);

        ClientResponse response =
                mock(ClientResponse.class);

        IdentificationType identificationType =
                IdentificationType.CC;

        String identificationNumber =
                "1075000000";

        when(
                getClientUseCase.execute(
                        identificationType,
                        identificationNumber
                )
        ).thenReturn(
                Optional.of(client)
        );

        when(clientMapper.toResponse(client))
                .thenReturn(response);

        ResponseEntity<ClientResponse> result =
                controller.getClient(
                        identificationType,
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
                        identificationType,
                        identificationNumber
                );

        verify(clientMapper)
                .toResponse(client);
    }

    @Test
    void shouldRestoreClientSuccessfully() {

        CreateClientUseCase createClientUseCase =
                mock(CreateClientUseCase.class);

        GetClientUseCase getClientUseCase =
                mock(GetClientUseCase.class);

        UpdateClientUseCase updateClientUseCase =
                mock(UpdateClientUseCase.class);

        DeleteClientUseCase deleteClientUseCase =
                mock(DeleteClientUseCase.class);

        RestoreClientUseCase restoreClientUseCase =
                mock(RestoreClientUseCase.class);

        GetClientSummaryUseCase getClientSummaryUseCase =
                mock(GetClientSummaryUseCase.class);

        ClientMapper clientMapper =
                mock(ClientMapper.class);

        ClientController controller =
                new ClientController(
                        createClientUseCase,
                        getClientUseCase,
                        updateClientUseCase,
                        deleteClientUseCase,
                        restoreClientUseCase,
                        getClientSummaryUseCase,
                        clientMapper
                );

        IdentificationType identificationType =
                IdentificationType.CC;

        String identificationNumber =
                "1075000000";

        Client restoredClient =
                mock(Client.class);

        ClientResponse response =
                mock(ClientResponse.class);

        when(
                restoreClientUseCase.restore(
                        identificationType,
                        identificationNumber
                )
        ).thenReturn(restoredClient);

        when(
                clientMapper.toResponse(restoredClient)
        ).thenReturn(response);

        ResponseEntity<ClientResponse> result =
                controller.restoreClient(
                        identificationType,
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

        verify(restoreClientUseCase)
                .restore(
                        identificationType,
                        identificationNumber
                );

        verify(clientMapper)
                .toResponse(restoredClient);
    }
}