package com.cuervo.application.port.in.account;

import com.cuervo.domain.model.Account;

public interface GetAccountUseCase {

    Account getByAccountNumber(String accountNumber);
}