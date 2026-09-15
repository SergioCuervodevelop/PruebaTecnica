package com.cuervo.application.port.in.transaction;

import com.cuervo.domain.model.Transaction;

public interface CreateTransactionUseCase {

    Transaction execute(Transaction transaction);
}