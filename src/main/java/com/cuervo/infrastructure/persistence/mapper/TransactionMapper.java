package com.cuervo.infrastructure.persistence.mapper;

import com.cuervo.domain.model.Transaction;
import com.cuervo.infrastructure.persistence.entity.AccountEntity;
import com.cuervo.infrastructure.persistence.entity.TransactionEntity;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public TransactionEntity toEntity(Transaction transaction) {

        if (transaction == null) {
            return null;
        }

        TransactionEntity entity = new TransactionEntity();

        entity.setId(transaction.getId());
        entity.setTransactionType(transaction.getTransactionType());
        entity.setMovementType(transaction.getMovementType());
        entity.setAmount(transaction.getAmount());
        entity.setTransactionDate(transaction.getTransactionDate());
        entity.setTransferId(transaction.getTransferId());

        if (transaction.getAccountId() != null) {
            AccountEntity account = new AccountEntity();
            account.setId(transaction.getAccountId());
            entity.setAccount(account);
        }

        return entity;
    }

    public Transaction toDomain(TransactionEntity entity) {

        if (entity == null) {
            return null;
        }

        Long accountId = entity.getAccount() != null
                ? entity.getAccount().getId()
                : null;

        Transaction transaction = new Transaction(
                entity.getTransactionType(),
                entity.getMovementType(),
                entity.getAmount(),
                accountId,
                entity.getTransferId()
        );

        transaction.setId(entity.getId());
        transaction.setTransactionDate(entity.getTransactionDate());

        return transaction;
    }
}