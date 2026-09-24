package com.cuervo.application.port.in.client;

import com.cuervo.domain.enums.IdentificationType;

public interface DeleteClientUseCase {

    void execute(
            IdentificationType identificationType,
            String identificationNumber
    );
}