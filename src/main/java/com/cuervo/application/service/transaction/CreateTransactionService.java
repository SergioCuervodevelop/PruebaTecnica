package com.cuervo.application.service.transaction;

import com.cuervo.application.port.in.transaction.CreateTransactionUseCase;
import com.cuervo.domain.enums.MovementType;
import com.cuervo.domain.enums.TransactionType;
import com.cuervo.domain.exception.EntityNotFoundException;
import com.cuervo.domain.exception.InvalidTransferException;
import com.cuervo.domain.model.Account;
import com.cuervo.domain.model.Transaction;
import com.cuervo.domain.port.out.AccountRepositoryPort;
import com.cuervo.domain.port.out.TransactionRepositoryPort;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

public class CreateTransactionService
        implements CreateTransactionUseCase {

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
    public Transaction execute(
            String accountNumber,
            TransactionType transactionType,
            BigDecimal amount) {

        Account account = accountRepositoryPort
                .findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Account not found"
                        ));

        MovementType movementType = switch (transactionType) {

            case DEPOSIT -> {
                account.deposit(amount);
                yield MovementType.CREDIT;
            }

            case WITHDRAWAL -> {
                account.withdraw(amount);
                yield MovementType.DEBIT;
            }

            case TRANSFER -> throw new InvalidTransferException(
                    "Transfers must use the transfer endpoint"
            );
        };

        Transaction transaction = new Transaction(
                transactionType,
                movementType,
                amount,
                account.getId(),
                null
        );

        accountRepositoryPort.save(account);

        return transactionRepositoryPort.save(transaction);
    }
}