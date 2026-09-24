package com.cuervo.domain.port.out;

import com.cuervo.domain.enums.IdentificationType;
import com.cuervo.domain.model.Client;

import java.util.List;
import java.util.Optional;

public interface ClientRepositoryPort {

    Client save(Client client);

    Optional<Client>
    findByIdentificationTypeAndIdentificationNumber(
            IdentificationType identificationType,
            String identificationNumber
    );

    List<Client> findAll();

    void deleteByIdentificationTypeAndIdentificationNumber(
            IdentificationType identificationType,
            String identificationNumber
    );
}