package com.cuervo.application.port.in.account;

import com.cuervo.domain.model.Account;

public interface RestoreAccountUseCase {

    Account restore(String accountNumber);
}