package com.cuervo.infrastructure.persistence.mapper;

import com.cuervo.domain.model.Client;
import com.cuervo.infrastructure.persistence.entity.ClientEntity;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper {

    public ClientEntity toEntity(Client client) {

        if (client == null) return null;

        ClientEntity entity = new ClientEntity();
        entity.setId(client.getId());
        entity.setIdentificationType(client.getIdentificationType());
        entity.setIdentificationNumber(client.getIdentificationNumber());
        entity.setFirstName(client.getFirstName());
        entity.setLastName(client.getLastName());
        entity.setEmail(client.getEmail());
        entity.setBirthDate(client.getBirthDate());
        entity.setCreatedAt(client.getCreatedAt());
        entity.setUpdatedAt(client.getUpdatedAt());

        return entity;
    }


    public Client toDomain(ClientEntity entity) {


        Client client = new Client(
                entity.getIdentificationType(),
                entity.getIdentificationNumber(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getEmail(),
                entity.getBirthDate()
        );

        client.setId(entity.getId());
        client.setCreatedAt(entity.getCreatedAt());
        client.setUpdatedAt(entity.getUpdatedAt());

        return client;
    }
}
