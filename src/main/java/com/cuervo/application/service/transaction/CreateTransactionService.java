package com.cuervo.application.service.transaction;

import com.cuervo.application.port.in.transaction.CreateTransactionUseCase;
import com.cuervo.domain.enums.TransactionType;
import com.cuervo.domain.exception.EntityNotFoundException;
import com.cuervo.domain.model.Account;
import com.cuervo.domain.model.Transaction;
import com.cuervo.domain.port.out.AccountRepositoryPort;
import com.cuervo.domain.port.out.TransactionRepositoryPort;
import org.springframework.transaction.annotation.Transactional;

public class CreateTransactionService implements CreateTransactionUseCase {

    private final TransactionRepositoryPort transactionRepositoryPort;
    private final AccountRepositoryPort accountRepositoryPort;

    public CreateTransactionService(
            TransactionRepositoryPort transactionRepositoryPort,
            AccountRepositoryPort accountRepositoryPort) {

        this.transactionRepositoryPort = transactionRepositoryPort;
        this.accountRepositoryPort = accountRepositoryPort;
    }

    @Override
    @Transactional
    public Transaction execute(Transaction transaction) {

        Account account = accountRepositoryPort
                .findById(transaction.getAccountId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Account not found"));

        if (transaction.getTransactionType() == TransactionType.DEPOSIT) {

            account.deposit(transaction.getAmount());

        } else if (transaction.getTransactionType() == TransactionType.WITHDRAWAL) {

            account.withdraw(transaction.getAmount());

        }

        accountRepositoryPort.save(account);

        return transactionRepositoryPort.save(transaction);
    }
}