package com.cuervo.infrastructure.web.mapper;

import com.cuervo.domain.model.Account;
import com.cuervo.infrastructure.web.dtoaccount.AccountResponse;
import com.cuervo.infrastructure.web.dtoaccount.CreateAccountRequest;
import org.springframework.stereotype.Component;

@Component
public class AccountWebMapper {

    public Account toDomain(CreateAccountRequest request) {

        return new Account(
                request.accountType(),
                null,
                request.clientId()
        );
    }

    public AccountResponse toResponse(Account account) {

        return new AccountResponse(
                account.getId(),
                account.getAccountType(),
                account.getAccountNumber(),
                account.getStatus(),
                account.getBalance(),
                account.getAvailableBalance(),
                account.getGmfExempt(),
                account.getCreatedAt(),
                account.getUpdatedAt(),
                account.getClientId()
        );
    }   
}