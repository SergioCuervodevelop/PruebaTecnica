package com.cuervo.application.service.transaction;

import com.cuervo.domain.enums.AccountType;
import com.cuervo.domain.enums.MovementType;
import com.cuervo.domain.exception.InvalidTransferException;
import com.cuervo.domain.model.Account;
import com.cuervo.domain.model.Transaction;
import com.cuervo.domain.port.out.AccountRepositoryPort;
import com.cuervo.domain.port.out.TransactionRepositoryPort;
import org.junit.jupiter.api.Test;
import com.cuervo.domain.exception.EntityNotFoundException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
                        1L,
                        1L,
                        new BigDecimal("10000")
                )
        );
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

        Account destinationAccount = new Account(
                AccountType.CHECKING,
                "3312345678",
                2L
        );

        sourceAccount.deposit(new BigDecimal("100000"));

        when(accountRepositoryPort.findById(1L))
                .thenReturn(Optional.of(sourceAccount));

        when(accountRepositoryPort.findById(2L))
                .thenReturn(Optional.of(destinationAccount));

        when(transactionRepositoryPort.save(any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        List<Transaction> result = service.transfer(
                1L,
                2L,
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

        assertEquals(
                MovementType.DEBIT,
                result.get(0).getMovementType()
        );

        assertEquals(
                MovementType.CREDIT,
                result.get(1).getMovementType()
        );

        assertEquals(
                result.get(0).getTransferId(),
                result.get(1).getTransferId()
        );
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

        when(accountRepositoryPort.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> service.transfer(
                        99L,
                        2L,
                        new BigDecimal("10000")
                )
        );
    }
}