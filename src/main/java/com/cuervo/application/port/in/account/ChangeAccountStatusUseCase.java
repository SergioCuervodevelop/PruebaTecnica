package com.cuervo.application.port.in.account;

import com.cuervo.domain.enums.AccountStatus;
import com.cuervo.domain.model.Account;

public interface ChangeAccountStatusUseCase {

    Account change(
            String accountNumber,
            AccountStatus status
    );
}
