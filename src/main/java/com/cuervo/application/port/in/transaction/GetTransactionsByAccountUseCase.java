package com.cuervo.application.port.in.transaction;

import com.cuervo.domain.model.Transaction;

import java.util.List;

public interface GetTransactionsByAccountUseCase {

    List<Transaction> execute(Long accountId);
}