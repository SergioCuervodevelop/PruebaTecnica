package com.cuervo.application.service.transaction;

import com.cuervo.domain.enums.AccountType;
import com.cuervo.domain.enums.MovementType;
import com.cuervo.domain.enums.TransactionType;
import com.cuervo.domain.exception.EntityNotFoundException;
import com.cuervo.domain.exception.InvalidTransferException;
import com.cuervo.domain.model.Account;
import com.cuervo.domain.model.Transaction;
import com.cuervo.domain.port.out.AccountRepositoryPort;
import com.cuervo.domain.port.out.TransactionRepositoryPort;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TransferMoneyServiceTest {

    @Test
    void shouldRejectTransferToSameAccount() {

        AccountRepositoryPort accountRepositoryPort =
                mock(AccountRepositoryPort.class);

        TransactionRepositoryPort transactionRepositoryPort =
                mock(TransactionRepositoryPort.class);

        TransferMoneyService service =
                new TransferMoneyService(
                        accountRepositoryPort,
                        transactionRepositoryPort
                );

        assertThrows(
                InvalidTransferException.class,
                () -> service.transfer(
                        "5312345678",
                        "5312345678",
                        new BigDecimal("10000")
                )
        );

        verifyNoInteractions(accountRepositoryPort);
        verifyNoInteractions(transactionRepositoryPort);
    }

    @Test
    void shouldTransferMoneySuccessfully() {

        AccountRepositoryPort accountRepositoryPort =
                mock(AccountRepositoryPort.class);

        TransactionRepositoryPort transactionRepositoryPort =
                mock(TransactionRepositoryPort.class);

        TransferMoneyService service =
                new TransferMoneyService(
                        accountRepositoryPort,
                        transactionRepositoryPort
                );

        Account sourceAccount = new Account(
                AccountType.SAVINGS,
                "5312345678",
                1L
        );

        sourceAccount.setId(1L);

        Account destinationAccount = new Account(
                AccountType.CHECKING,
                "3312345678",
                2L
        );

        destinationAccount.setId(2L);

        sourceAccount.deposit(
                new BigDecimal("100000")
        );

        when(accountRepositoryPort
                .findByAccountNumber("5312345678"))
                .thenReturn(Optional.of(sourceAccount));

        when(accountRepositoryPort
                .findByAccountNumber("3312345678"))
                .thenReturn(Optional.of(destinationAccount));

        when(transactionRepositoryPort
                .save(any(Transaction.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0)
                );

        List<Transaction> result =
                service.transfer(
                        "5312345678",
                        "3312345678",
                        new BigDecimal("30000")
                );

        assertEquals(
                new BigDecimal("70000"),
                sourceAccount.getBalance()
        );

        assertEquals(
                new BigDecimal("30000"),
                destinationAccount.getBalance()
        );

        assertEquals(2, result.size());

        Transaction debit = result.get(0);
        Transaction credit = result.get(1);

        assertEquals(
                TransactionType.TRANSFER,
                debit.getTransactionType()
        );

        assertEquals(
                TransactionType.TRANSFER,
                credit.getTransactionType()
        );

        assertEquals(
                MovementType.DEBIT,
                debit.getMovementType()
        );

        assertEquals(
                MovementType.CREDIT,
                credit.getMovementType()
        );

        assertEquals(
                new BigDecimal("30000"),
                debit.getAmount()
        );

        assertEquals(
                new BigDecimal("30000"),
                credit.getAmount()
        );

        assertEquals(
                1L,
                debit.getAccountId()
        );

        assertEquals(
                2L,
                credit.getAccountId()
        );

        assertNotNull(debit.getTransferId());

        assertEquals(
                debit.getTransferId(),
                credit.getTransferId()
        );

        verify(accountRepositoryPort)
                .save(sourceAccount);

        verify(accountRepositoryPort)
                .save(destinationAccount);

        verify(transactionRepositoryPort, times(2))
                .save(any(Transaction.class));
    }

    @Test
    void shouldThrowExceptionWhenSourceAccountDoesNotExist() {

        AccountRepositoryPort accountRepositoryPort =
                mock(AccountRepositoryPort.class);

        TransactionRepositoryPort transactionRepositoryPort =
                mock(TransactionRepositoryPort.class);

        TransferMoneyService service =
                new TransferMoneyService(
                        accountRepositoryPort,
                        transactionRepositoryPort
                );

        when(accountRepositoryPort
                .findByAccountNumber("5399999999"))
                .thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> service.transfer(
                        "5399999999",
                        "3312345678",
                        new BigDecimal("10000")
                )
        );

        verify(accountRepositoryPort, never())
                .save(any(Account.class));

        verify(transactionRepositoryPort, never())
                .save(any(Transaction.class));
    }

    @Test
    void shouldThrowExceptionWhenDestinationAccountDoesNotExist() {

        AccountRepositoryPort accountRepositoryPort =
                mock(AccountRepositoryPort.class);

        TransactionRepositoryPort transactionRepositoryPort =
                mock(TransactionRepositoryPort.class);

        TransferMoneyService service =
                new TransferMoneyService(
                        accountRepositoryPort,
                        transactionRepositoryPort
                );

        Account sourceAccount = new Account(
                AccountType.SAVINGS,
                "5312345678",
                1L
        );

        sourceAccount.setId(1L);

        sourceAccount.deposit(
                new BigDecimal("100000")
        );

        when(accountRepositoryPort
                .findByAccountNumber("5312345678"))
                .thenReturn(Optional.of(sourceAccount));

        when(accountRepositoryPort
                .findByAccountNumber("3399999999"))
                .thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> service.transfer(
                        "5312345678",
                        "3399999999",
                        new BigDecimal("10000")
                )
        );

        assertEquals(
                new BigDecimal("100000"),
                sourceAccount.getBalance()
        );

        verify(accountRepositoryPort, never())
                .save(any(Account.class));

        verify(transactionRepositoryPort, never())
                .save(any(Transaction.class));
    }
}