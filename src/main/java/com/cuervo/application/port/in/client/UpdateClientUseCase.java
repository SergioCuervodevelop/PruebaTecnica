package com.cuervo.application.port.in.client;

import com.cuervo.domain.model.Client;

public interface UpdateClientUseCase {
    Client execute(Long id, Client client);
}
