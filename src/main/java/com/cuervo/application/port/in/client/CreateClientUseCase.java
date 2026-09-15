package com.cuervo.application.port.in.client;

import com.cuervo.domain.model.Client;

public interface CreateClientUseCase {

    Client execute(Client client);
}
