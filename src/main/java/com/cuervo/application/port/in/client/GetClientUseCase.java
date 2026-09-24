package com.cuervo.application.port.in.client;

import com.cuervo.domain.enums.IdentificationType;
import com.cuervo.domain.model.Client;

import java.util.Optional;

public interface GetClientUseCase {

    Optional<Client> execute(
            IdentificationType identificationType,
            String identificationNumber
    );
}