package com.cuervo.infrastructure.persistence.mapper;

import com.cuervo.domain.model.Client;
import com.cuervo.infrastructure.persistence.entity.ClientEntity;
import com.cuervo.infrastructure.web.dtoclient.ClientResponse;
import com.cuervo.infrastructure.web.dtoclient.CreateClientRequest;
import com.cuervo.infrastructure.web.dtoclient.UpdateClientRequest;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper {

    // DOMAIN -> ENTITY
    public ClientEntity toEntity(Client client) {

        if (client == null) {
            return null;
        }

        ClientEntity entity = new ClientEntity();

        entity.setId(client.getId());
        entity.setIdentificationType(client.getIdentificationType());
        entity.setIdentificationNumber(client.getIdentificationNumber());
        entity.setFirstName(client.getFirstName());
        entity.setLastName(client.getLastName());
        entity.setEmail(client.getEmail());
        entity.setBirthDate(client.getBirthDate());
        entity.setStatus(client.getStatus());
        entity.setCreatedAt(client.getCreatedAt());
        entity.setUpdatedAt(client.getUpdatedAt());

        return entity;
    }

    // ENTITY -> DOMAIN
    public Client toDomain(ClientEntity entity) {

        if (entity == null) {
            return null;
        }

        Client client = new Client(
                entity.getIdentificationType(),
                entity.getIdentificationNumber(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getEmail(),
                entity.getBirthDate()
        );

        client.setId(entity.getId());
        client.setStatus(entity.getStatus());
        client.setCreatedAt(entity.getCreatedAt());
        client.setUpdatedAt(entity.getUpdatedAt());

        return client;
    }

    // CREATE REQUEST -> DOMAIN
    public Client toDomain(CreateClientRequest request) {

        if (request == null) {
            return null;
        }

        return new Client(
                request.identificationType(),
                request.identificationNumber(),
                request.firstName(),
                request.lastName(),
                request.email(),
                request.birthDate()
        );
    }

    // UPDATE REQUEST -> DOMAIN
    public Client toDomain(UpdateClientRequest request) {

        if (request == null) {
            return null;
        }

        return Client.forUpdate(
                request.firstName(),
                request.lastName(),
                request.email()
        );
    }

    // DOMAIN -> RESPONSE
    public ClientResponse toResponse(Client client) {

        if (client == null) {
            return null;
        }

        return new ClientResponse(
                client.getIdentificationType(),
                client.getIdentificationNumber(),
                client.getFirstName(),
                client.getLastName(),
                client.getEmail(),
                client.getBirthDate(),
                client.getStatus(),
                client.getCreatedAt(),
                client.getUpdatedAt()
        );
    }
}