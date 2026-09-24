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

import static org.junit.jupiter.api.Assertions.*;
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

        account.setId(1L);

        when(accountRepositoryPort
                .findByAccountNumber("5312345678"))
                .thenReturn(Optional.of(account));

        when(transactionRepositoryPort
                .save(any(Transaction.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0)
                );

        Transaction result = service.execute(
                "5312345678",
                TransactionType.DEPOSIT,
                new BigDecimal("20000")
        );

        assertEquals(
                new BigDecimal("20000"),
                account.getBalance()
        );

        assertEquals(
                new BigDecimal("20000"),
                account.getAvailableBalance()
        );

        assertEquals(
                TransactionType.DEPOSIT,
                result.getTransactionType()
        );

        assertEquals(
                MovementType.CREDIT,
                result.getMovementType()
        );

        assertEquals(
                new BigDecimal("20000"),
                result.getAmount()
        );

        assertEquals(
                1L,
                result.getAccountId()
        );

        assertNull(result.getTransferId());

        verify(accountRepositoryPort)
                .save(account);

        verify(transactionRepositoryPort)
                .save(any(Transaction.class));
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

        account.setId(1L);

        account.deposit(
                new BigDecimal("50000")
        );

        when(accountRepositoryPort
                .findByAccountNumber("5312345678"))
                .thenReturn(Optional.of(account));

        when(transactionRepositoryPort
                .save(any(Transaction.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0)
                );

        Transaction result = service.execute(
                "5312345678",
                TransactionType.WITHDRAWAL,
                new BigDecimal("20000")
        );

        assertEquals(
                new BigDecimal("30000"),
                account.getBalance()
        );

        assertEquals(
                new BigDecimal("30000"),
                account.getAvailableBalance()
        );

        assertEquals(
                TransactionType.WITHDRAWAL,
                result.getTransactionType()
        );

        assertEquals(
                MovementType.DEBIT,
                result.getMovementType()
        );

        assertEquals(
                new BigDecimal("20000"),
                result.getAmount()
        );

        assertEquals(
                1L,
                result.getAccountId()
        );

        verify(accountRepositoryPort)
                .save(account);

        verify(transactionRepositoryPort)
                .save(any(Transaction.class));
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

        account.setId(1L);

        account.deposit(
                new BigDecimal("10000")
        );

        when(accountRepositoryPort
                .findByAccountNumber("5312345678"))
                .thenReturn(Optional.of(account));

        assertThrows(
                InsufficientBalanceException.class,
                () -> service.execute(
                        "5312345678",
                        TransactionType.WITHDRAWAL,
                        new BigDecimal("50000")
                )
        );

        assertEquals(
                new BigDecimal("10000"),
                account.getBalance()
        );

        assertEquals(
                new BigDecimal("10000"),
                account.getAvailableBalance()
        );

        verify(accountRepositoryPort, never())
                .save(any(Account.class));

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

        account.setId(1L);

        when(accountRepositoryPort
                .findByAccountNumber("5312345678"))
                .thenReturn(Optional.of(account));

        assertThrows(
                InvalidTransferException.class,
                () -> service.execute(
                        "5312345678",
                        TransactionType.TRANSFER,
                        new BigDecimal("20000")
                )
        );

        verify(accountRepositoryPort, never())
                .save(any(Account.class));

        verify(transactionRepositoryPort, never())
                .save(any(Transaction.class));
    }
}