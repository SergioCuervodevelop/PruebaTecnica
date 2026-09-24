package com.cuervo.infrastructure.web.mapper;

import com.cuervo.domain.model.Transaction;
import com.cuervo.infrastructure.web.dtotransaction.TransactionResponse;
import org.springframework.stereotype.Component;

@Component
public class TransactionWebMapper {

    public TransactionResponse toResponse(
            Transaction transaction,
            String accountNumber) {

        return new TransactionResponse(
                transaction.getTransactionType(),
                transaction.getMovementType(),
                transaction.getAmount(),
                transaction.getTransactionDate(),
                accountNumber,
                transaction.getTransferId()
        );
    }
}