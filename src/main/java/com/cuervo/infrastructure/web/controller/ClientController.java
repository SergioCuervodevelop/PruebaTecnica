package com.cuervo.infrastructure.web.controller;


import com.cuervo.application.port.in.client.*;
import com.cuervo.domain.exception.EntityNotFoundException;
import com.cuervo.domain.model.Client;
import com.cuervo.infrastructure.web.dtoclient.ClientResponse;
import com.cuervo.infrastructure.web.dtoclient.ClientSummaryResponse;
import com.cuervo.infrastructure.web.dtoclient.CreateClientRequest;
import com.cuervo.infrastructure.web.dtoclient.UpdateClientRequest;
import com.cuervo.infrastructure.web.mapper.ClientWebMapper;
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
    private final GetClientSummaryUseCase getClientSummaryUseCase;
    private final ClientWebMapper clientWebMapper;

    public ClientController(
            CreateClientUseCase createClientUseCase,
            GetClientUseCase getClientUseCase,
            UpdateClientUseCase updateClientUseCase,
            DeleteClientUseCase deleteClientUseCase,
            GetClientSummaryUseCase getClientSummaryUseCase,
            ClientWebMapper clientWebMapper) {

        this.createClientUseCase = createClientUseCase;
        this.getClientUseCase = getClientUseCase;
        this.updateClientUseCase = updateClientUseCase;
        this.deleteClientUseCase = deleteClientUseCase;
        this.getClientSummaryUseCase = getClientSummaryUseCase;
        this.clientWebMapper = clientWebMapper;
    }

    @PostMapping
    public ResponseEntity<ClientResponse> createClient(
            @Valid @RequestBody CreateClientRequest request) {

        Client client = clientWebMapper.toDomain(request);

        Client createdClient =
                createClientUseCase.execute(client);

        ClientResponse response =
                clientWebMapper.toResponse(createdClient);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> getClient(
            @PathVariable Long id) {

        Client client = getClientUseCase.execute(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Client not found"));

        ClientResponse response =
                clientWebMapper.toResponse(client);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientResponse> updateClient(
            @PathVariable Long id,
            @Valid @RequestBody UpdateClientRequest request) {

        Client client = clientWebMapper.toDomain(request);

        Client updatedClient =
                updateClientUseCase.execute(id, client);

        ClientResponse response =
                clientWebMapper.toResponse(updatedClient);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(
            @PathVariable Long id) {

        deleteClientUseCase.execute(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/summary")
    public ResponseEntity<ClientSummaryResponse> getSummary() {

        ClientSummaryResponse response =
                getClientSummaryUseCase.execute();

        return ResponseEntity.ok(response);
    }
}