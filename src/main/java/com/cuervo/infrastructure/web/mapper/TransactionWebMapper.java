package com.cuervo.infrastructure.web.mapper;

import com.cuervo.domain.enums.MovementType;
import com.cuervo.domain.model.Transaction;
import com.cuervo.infrastructure.persistence.entity.AccountEntity;
import com.cuervo.infrastructure.persistence.entity.TransactionEntity;
import com.cuervo.infrastructure.web.dtotransaction.CreateTransactionRequest;
import com.cuervo.infrastructure.web.dtotransaction.TransactionResponse;
import org.springframework.stereotype.Component;

@Component
public class TransactionWebMapper {

    public Transaction toDomain(CreateTransactionRequest request) {

        if (request == null) {
            return null;
        }

        MovementType movementType = switch (request.transactionType()) {
            case DEPOSIT -> MovementType.CREDIT;
            case WITHDRAWAL -> MovementType.DEBIT;
            case TRANSFER -> MovementType.DEBIT;
        };

        return new Transaction(
                request.transactionType(),
                movementType,
                request.amount(),
                request.accountId(),
                null
        );
    }

    public TransactionResponse toResponse(Transaction transaction) {

        return new TransactionResponse(
                transaction.getId(),
                transaction.getTransactionType(),
                transaction.getMovementType(),
                transaction.getAmount(),
                transaction.getTransactionDate(),
                transaction.getAccountId(),
                transaction.getTransferId()
        );
    }

    private MovementType getMovementType(
            CreateTransactionRequest request) {

        if (request.transactionType() == null) {
            return null;
        }

        return switch (request.transactionType()) {
            case DEPOSIT -> MovementType.CREDIT;
            case WITHDRAWAL -> MovementType.DEBIT;
            case TRANSFER -> MovementType.DEBIT;
        };
    }

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
}