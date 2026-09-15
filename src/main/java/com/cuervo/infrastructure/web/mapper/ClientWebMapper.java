package com.cuervo.infrastructure.web.mapper;

import com.cuervo.domain.model.Client;
import com.cuervo.infrastructure.web.dtoclient.ClientResponse;
import com.cuervo.infrastructure.web.dtoclient.CreateClientRequest;
import com.cuervo.infrastructure.web.dtoclient.UpdateClientRequest;
import org.springframework.stereotype.Component;

@Component
public class ClientWebMapper {

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

    public ClientResponse toResponse(Client client) {

        if (client == null) {
            return null;
        }

        return new ClientResponse(
                client.getId(),
                client.getIdentificationType(),
                client.getIdentificationNumber(),
                client.getFirstName(),
                client.getLastName(),
                client.getEmail(),
                client.getBirthDate(),
                client.getCreatedAt(),
                client.getUpdatedAt()
        );
    }
}