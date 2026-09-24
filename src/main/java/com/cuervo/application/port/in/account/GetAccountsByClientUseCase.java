package com.cuervo.application.port.in.account;

import com.cuervo.domain.enums.IdentificationType;
import com.cuervo.domain.model.Account;

import java.util.List;

public interface GetAccountsByClientUseCase {

    List<Account>
    getByIdentification(
            IdentificationType identificationType,
            String identificationNumber
    );
}