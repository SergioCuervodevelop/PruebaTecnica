package com.cuervo.application.port.in.transaction;

import com.cuervo.domain.enums.TransactionType;
import com.cuervo.domain.model.Transaction;

import java.math.BigDecimal;

public interface CreateTransactionUseCase {

    Transaction execute(
            String accountNumber,
            TransactionType transactionType,
            BigDecimal amount
    );
}