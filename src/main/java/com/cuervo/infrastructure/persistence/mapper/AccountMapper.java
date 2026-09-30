package com.cuervo.infrastructure.persistence.mapper;

import com.cuervo.domain.model.Account;
import com.cuervo.infrastructure.persistence.entity.AccountEntity;
import com.cuervo.infrastructure.persistence.entity.ClientEntity;
import com.cuervo.infrastructure.web.dtoaccount.AccountResponse;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public AccountEntity toEntity(Account account) {

        if (account == null) {
            return null;
        }

        AccountEntity entity = new AccountEntity();

        entity.setId(account.getId());
        entity.setAccountType(account.getAccountType());
        entity.setAccountNumber(account.getAccountNumber());
        entity.setStatus(account.getStatus());
        entity.setBalance(account.getBalance());
        entity.setAvailableBalance(account.getAvailableBalance());
        entity.setGmfExempt(account.getGmfExempt());
        entity.setCreatedAt(account.getCreatedAt());
        entity.setUpdatedAt(account.getUpdatedAt());

        if (account.getClientId() != null) {
            ClientEntity client = new ClientEntity();
            client.setId(account.getClientId());
            entity.setClient(client);
        }

        return entity;
    }

    public Account toDomain(AccountEntity entity) {

        if (entity == null) {
            return null;
        }

        Long clientId = null;

        if (entity.getClient() != null) {
            clientId = entity.getClient().getId();
        }

        Account account = new Account(
                entity.getAccountType(),
                entity.getAccountNumber(),
                clientId
        );

        account.setId(entity.getId());
        account.setStatus(entity.getStatus());
        account.setBalance(entity.getBalance());
        account.setAvailableBalance(entity.getAvailableBalance());
        account.setGmfExempt(entity.getGmfExempt());
        account.setCreatedAt(entity.getCreatedAt());
        account.setUpdatedAt(entity.getUpdatedAt());

        return account;
    }

    public AccountResponse toResponse(Account account) {

        if (account == null) {
            return null;
        }

        return new AccountResponse(
                account.getAccountType(),
                account.getAccountNumber(),
                account.getStatus(),
                account.getBalance(),
                account.getAvailableBalance(),
                account.getGmfExempt(),
                account.getCreatedAt(),
                account.getUpdatedAt()
        );
    }
}