package com.cuervo.application.service.transaction;

import com.cuervo.domain.enums.AccountType;
import com.cuervo.domain.enums.MovementType;
import com.cuervo.domain.enums.TransactionType;
import com.cuervo.domain.exception.InsufficientBalanceException;
import com.cuervo.domain.exception.InvalidTransferException;
import com.cuervo.domain.model.Account;
import com.cuervo.domain.model.Transaction;
import com.cuervo.domain.port.out.AccountRepositoryPort;
import com.cuervo.domain.port.out.TransactionRepositoryPort;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CreateTransactionServiceTest {

    @Test
    void shouldDepositSuccessfully() {

        TransactionRepositoryPort transactionRepositoryPort =
                mock(TransactionRepositoryPort.class);

        AccountRepositoryPort accountRepositoryPort =
                mock(AccountRepositoryPort.class);

        CreateTransactionService service =
                new CreateTransactionService(
                        transactionRepositoryPort,
                        accountRepositoryPort
                );

        Account account = new Account(
                AccountType.SAVINGS,
                "5312345678",
                1L
        );

        Transaction transaction = new Transaction(
                TransactionType.DEPOSIT,
                MovementType.CREDIT,
                new BigDecimal("20000"),
                1L,
                null
        );

        when(accountRepositoryPort.findById(1L))
                .thenReturn(Optional.of(account));

        when(transactionRepositoryPort.save(transaction))
                .thenReturn(transaction);

        Transaction result = service.execute(transaction);

        assertEquals(
                new BigDecimal("20000"),
                account.getBalance()
        );

        assertEquals(
                new BigDecimal("20000"),
                account.getAvailableBalance()
        );

        assertEquals(transaction, result);

        verify(accountRepositoryPort).save(account);
        verify(transactionRepositoryPort).save(transaction);
    }

    @Test
    void shouldWithdrawSuccessfully() {

        TransactionRepositoryPort transactionRepositoryPort =
                mock(TransactionRepositoryPort.class);

        AccountRepositoryPort accountRepositoryPort =
                mock(AccountRepositoryPort.class);

        CreateTransactionService service =
                new CreateTransactionService(
                        transactionRepositoryPort,
                        accountRepositoryPort
                );

        Account account = new Account(
                AccountType.SAVINGS,
                "5312345678",
                1L
        );

        account.deposit(new BigDecimal("50000"));

        Transaction transaction = new Transaction(
                TransactionType.WITHDRAWAL,
                MovementType.DEBIT,
                new BigDecimal("20000"),
                1L,
                null
        );

        when(accountRepositoryPort.findById(1L))
                .thenReturn(Optional.of(account));

        when(transactionRepositoryPort.save(transaction))
                .thenReturn(transaction);

        service.execute(transaction);

        assertEquals(
                new BigDecimal("30000"),
                account.getBalance()
        );

        assertEquals(
                new BigDecimal("30000"),
                account.getAvailableBalance()
        );
    }

    @Test
    void shouldRejectWithdrawalWhenBalanceIsInsufficient() {

        TransactionRepositoryPort transactionRepositoryPort =
                mock(TransactionRepositoryPort.class);

        AccountRepositoryPort accountRepositoryPort =
                mock(AccountRepositoryPort.class);

        CreateTransactionService service =
                new CreateTransactionService(
                        transactionRepositoryPort,
                        accountRepositoryPort
                );

        Account account = new Account(
                AccountType.SAVINGS,
                "5312345678",
                1L
        );

        account.deposit(new BigDecimal("10000"));

        Transaction transaction = new Transaction(
                TransactionType.WITHDRAWAL,
                MovementType.DEBIT,
                new BigDecimal("50000"),
                1L,
                null
        );

        when(accountRepositoryPort.findById(1L))
                .thenReturn(Optional.of(account));

        assertThrows(
                InsufficientBalanceException.class,
                () -> service.execute(transaction)
        );

        assertEquals(
                new BigDecimal("10000"),
                account.getBalance()
        );

        verify(transactionRepositoryPort, never())
                .save(any(Transaction.class));
    }

    @Test
    void shouldRejectTransferTransaction() {

        TransactionRepositoryPort transactionRepositoryPort =
                mock(TransactionRepositoryPort.class);

        AccountRepositoryPort accountRepositoryPort =
                mock(AccountRepositoryPort.class);

        CreateTransactionService service =
                new CreateTransactionService(
                        transactionRepositoryPort,
                        accountRepositoryPort
                );

        Account account = new Account(
                AccountType.SAVINGS,
                "5312345678",
                1L
        );

        Transaction transaction = new Transaction(
                TransactionType.TRANSFER,
                MovementType.DEBIT,
                new BigDecimal("20000"),
                1L,
                null
        );

        when(accountRepositoryPort.findById(1L))
                .thenReturn(Optional.of(account));

        assertThrows(
                InvalidTransferException.class,
                () -> service.execute(transaction)
        );

        verify(accountRepositoryPort, never())
                .save(any(Account.class));

        verify(transactionRepositoryPort, never())
                .save(any(Transaction.class));
    }
}