package com.cuervo.application.port.in.client;

import com.cuervo.domain.model.Client;
import com.cuervo.infrastructure.web.dtoclient.ClientSummaryResponse;

import java.util.List;

public interface GetClientSummaryUseCase {

    ClientSummaryResponse execute();
}