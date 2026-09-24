package com.cuervo.application.port.in.account;

import com.cuervo.domain.enums.AccountType;
import com.cuervo.domain.enums.IdentificationType;
import com.cuervo.domain.model.Account;

public interface CreateAccountUseCase {

    Account create(
            AccountType accountType,
            IdentificationType identificationType,
            String identificationNumber
    );
}