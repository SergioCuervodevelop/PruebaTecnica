package com.cuervo.application.service.transaction;

import com.cuervo.application.port.in.transaction.GetTransactionsByAccountUseCase;
import com.cuervo.domain.model.Transaction;
import com.cuervo.domain.port.out.TransactionRepositoryPort;

import java.util.List;

public class GetTransactionsByAccountService
        implements GetTransactionsByAccountUseCase {

    private final TransactionRepositoryPort transactionRepositoryPort;

    public GetTransactionsByAccountService(
            TransactionRepositoryPort transactionRepositoryPort) {

        this.transactionRepositoryPort = transactionRepositoryPort;
    }

    @Override
    public List<Transaction> execute(Long accountId) {

        return transactionRepositoryPort.findByAccountId(accountId);
    }
}