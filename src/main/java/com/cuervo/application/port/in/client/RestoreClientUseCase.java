package com.cuervo.application.port.in.client;

import com.cuervo.domain.enums.IdentificationType;
import com.cuervo.domain.model.Client;

public interface RestoreClientUseCase {

    Client restore(
            IdentificationType identificationType,
            String identificationNumber
    );
}