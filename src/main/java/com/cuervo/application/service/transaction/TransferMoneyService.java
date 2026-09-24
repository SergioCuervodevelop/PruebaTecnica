package com.cuervo.application.service.transaction;

import com.cuervo.application.port.in.transaction.TransferMoneyUseCase;
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
import java.util.List;
import java.util.UUID;

public class TransferMoneyService implements TransferMoneyUseCase {

    private final AccountRepositoryPort accountRepositoryPort;
    private final TransactionRepositoryPort transactionRepositoryPort;

    public TransferMoneyService(
            AccountRepositoryPort accountRepositoryPort,
            TransactionRepositoryPort transactionRepositoryPort) {

        this.accountRepositoryPort = accountRepositoryPort;
        this.transactionRepositoryPort = transactionRepositoryPort;
    }

    @Override
    @Transactional
    public List<Transaction> transfer(
            String sourceAccountNumber,
            String destinationAccountNumber,
            BigDecimal amount) {

        if (sourceAccountNumber.equals(destinationAccountNumber)) {
            throw new InvalidTransferException(
                    "Source and destination accounts must be different"
            );
        }

        Account sourceAccount = accountRepositoryPort
                .findByAccountNumber(sourceAccountNumber)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Source account not found"
                        ));

        Account destinationAccount = accountRepositoryPort
                .findByAccountNumber(destinationAccountNumber)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Destination account not found"
                        ));

        String transferId = UUID.randomUUID().toString();

        sourceAccount.withdraw(amount);
        destinationAccount.deposit(amount);

        accountRepositoryPort.save(sourceAccount);
        accountRepositoryPort.save(destinationAccount);

        Transaction debit = new Transaction(
                TransactionType.TRANSFER,
                MovementType.DEBIT,
                amount,
                sourceAccount.getId(),
                transferId
        );

        Transaction credit = new Transaction(
                TransactionType.TRANSFER,
                MovementType.CREDIT,
                amount,
                destinationAccount.getId(),
                transferId
        );

        Transaction savedDebit =
                transactionRepositoryPort.save(debit);

        Transaction savedCredit =
                transactionRepositoryPort.save(credit);

        return List.of(savedDebit, savedCredit);
    }
}