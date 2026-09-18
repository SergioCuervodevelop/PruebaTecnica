package com.cuervo.application.port.in.account;

import com.cuervo.domain.model.Account;

import java.util.List;

public interface GetAccountsByClientUseCase {

    List<Account> getByClientId(Long clientId);
}