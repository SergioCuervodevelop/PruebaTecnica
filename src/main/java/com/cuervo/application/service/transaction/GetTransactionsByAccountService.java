package com.cuervo.application.service.transaction;

import com.cuervo.application.port.in.transaction.GetTransactionsByAccountUseCase;
import com.cuervo.domain.exception.EntityNotFoundException;
import com.cuervo.domain.model.Account;
import com.cuervo.domain.model.Transaction;
import com.cuervo.domain.port.out.AccountRepositoryPort;
import com.cuervo.domain.port.out.TransactionRepositoryPort;

import java.util.List;

public class GetTransactionsByAccountService
        implements GetTransactionsByAccountUseCase {

    private final TransactionRepositoryPort transactionRepositoryPort;
    private final AccountRepositoryPort accountRepositoryPort;

    public GetTransactionsByAccountService(
            TransactionRepositoryPort transactionRepositoryPort,
            AccountRepositoryPort accountRepositoryPort) {

        this.transactionRepositoryPort = transactionRepositoryPort;
        this.accountRepositoryPort = accountRepositoryPort;
    }

    @Override
    public List<Transaction> execute(String accountNumber) {

        Account account = accountRepositoryPort
                .findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Account not found"
                        ));

        return transactionRepositoryPort
                .findByAccountId(account.getId());
    }
}