package com.cuervo.infrastructure.web.controller;

import com.cuervo.application.port.in.client.*;
import com.cuervo.domain.enums.IdentificationType;
import com.cuervo.domain.exception.EntityNotFoundException;
import com.cuervo.domain.model.Client;
import com.cuervo.infrastructure.persistence.mapper.ClientMapper;
import com.cuervo.infrastructure.web.dtoclient.ClientResponse;
import com.cuervo.infrastructure.web.dtoclient.ClientSummaryResponse;
import com.cuervo.infrastructure.web.dtoclient.CreateClientRequest;
import com.cuervo.infrastructure.web.dtoclient.UpdateClientRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final CreateClientUseCase createClientUseCase;
    private final GetClientUseCase getClientUseCase;
    private final UpdateClientUseCase updateClientUseCase;
    private final DeleteClientUseCase deleteClientUseCase;
    private final RestoreClientUseCase restoreClientUseCase;
    private final GetClientSummaryUseCase getClientSummaryUseCase;
    private final ClientMapper clientMapper;

    public ClientController(
            CreateClientUseCase createClientUseCase,
            GetClientUseCase getClientUseCase,
            UpdateClientUseCase updateClientUseCase,
            DeleteClientUseCase deleteClientUseCase,
            RestoreClientUseCase restoreClientUseCase,
            GetClientSummaryUseCase getClientSummaryUseCase,
            ClientMapper clientMapper) {

        this.createClientUseCase = createClientUseCase;
        this.getClientUseCase = getClientUseCase;
        this.updateClientUseCase = updateClientUseCase;
        this.deleteClientUseCase = deleteClientUseCase;
        this.restoreClientUseCase = restoreClientUseCase;
        this.getClientSummaryUseCase = getClientSummaryUseCase;
        this.clientMapper = clientMapper;
    }

    @PostMapping
    public ResponseEntity<ClientResponse> createClient(
            @Valid @RequestBody CreateClientRequest request) {

        Client client =
                clientMapper.toDomain(request);

        Client createdClient =
                createClientUseCase.execute(client);

        ClientResponse response =
                clientMapper.toResponse(createdClient);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{identificationType}/{identificationNumber}")
    public ResponseEntity<ClientResponse> getClient(
            @PathVariable IdentificationType identificationType,
            @PathVariable String identificationNumber) {

        Client client =
                getClientUseCase
                        .execute(
                                identificationType,
                                identificationNumber
                        )
                        .orElseThrow(
                                () -> new EntityNotFoundException(
                                        "Client not found"
                                )
                        );

        ClientResponse response =
                clientMapper.toResponse(client);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{identificationType}/{identificationNumber}")
    public ResponseEntity<ClientResponse> updateClient(
            @PathVariable IdentificationType identificationType,
            @PathVariable String identificationNumber,
            @Valid @RequestBody UpdateClientRequest request) {

        Client client =
                clientMapper.toDomain(request);

        Client updatedClient =
                updateClientUseCase.execute(
                        identificationType,
                        identificationNumber,
                        client
                );

        ClientResponse response =
                clientMapper.toResponse(updatedClient);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{identificationType}/{identificationNumber}")
    public ResponseEntity<Void> deleteClient(
            @PathVariable IdentificationType identificationType,
            @PathVariable String identificationNumber) {

        deleteClientUseCase.execute(
                identificationType,
                identificationNumber
        );

        return ResponseEntity
                .noContent()
                .build();
    }

    @PatchMapping("/{identificationType}/{identificationNumber}/restore")
    public ResponseEntity<ClientResponse> restoreClient(
            @PathVariable IdentificationType identificationType,
            @PathVariable String identificationNumber) {

        Client restoredClient =
                restoreClientUseCase.restore(
                        identificationType,
                        identificationNumber
                );

        ClientResponse response =
                clientMapper.toResponse(restoredClient);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/summary")
    public ResponseEntity<ClientSummaryResponse> getSummary() {

        ClientSummaryResponse response =
                getClientSummaryUseCase.execute();

        return ResponseEntity.ok(response);
    }
}