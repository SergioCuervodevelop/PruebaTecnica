package com.cuervo.infrastructure.web.controller;

import com.cuervo.application.port.in.transaction.CreateTransactionUseCase;
import com.cuervo.application.port.in.transaction.GetTransactionsByAccountUseCase;
import com.cuervo.application.port.in.transaction.TransferMoneyUseCase;
import com.cuervo.domain.model.Transaction;
import com.cuervo.infrastructure.web.dtotransaction.CreateTransactionRequest;
import com.cuervo.infrastructure.web.dtotransaction.TransactionResponse;
import com.cuervo.infrastructure.web.dtotransaction.TransferMoneyRequest;
import com.cuervo.infrastructure.web.mapper.TransactionWebMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TransactionControllerTest {

    @Test
    void shouldCreateTransactionSuccessfully() {

        CreateTransactionUseCase createTransactionUseCase =
                mock(CreateTransactionUseCase.class);

        GetTransactionsByAccountUseCase getTransactionsByAccountUseCase =
                mock(GetTransactionsByAccountUseCase.class);

        TransferMoneyUseCase transferMoneyUseCase =
                mock(TransferMoneyUseCase.class);

        TransactionWebMapper transactionWebMapper =
                mock(TransactionWebMapper.class);

        TransactionController controller =
                new TransactionController(
                        createTransactionUseCase,
                        getTransactionsByAccountUseCase,
                        transferMoneyUseCase,
                        transactionWebMapper
                );

        CreateTransactionRequest request =
                new CreateTransactionRequest(
                        1L,
                        com.cuervo.domain.enums.TransactionType.DEPOSIT,
                        new BigDecimal("10000")
                );

        Transaction transaction = mock(Transaction.class);
        Transaction createdTransaction = mock(Transaction.class);
        TransactionResponse response = mock(TransactionResponse.class);

        when(transactionWebMapper.toDomain(request))
                .thenReturn(transaction);

        when(createTransactionUseCase.execute(transaction))
                .thenReturn(createdTransaction);

        when(transactionWebMapper.toResponse(createdTransaction))
                .thenReturn(response);

        ResponseEntity<TransactionResponse> result =
                controller.create(request);

        assertEquals(201, result.getStatusCode().value());
        assertEquals(response, result.getBody());
    }

    @Test
    void shouldTransferMoneySuccessfully() {

        CreateTransactionUseCase createTransactionUseCase =
                mock(CreateTransactionUseCase.class);

        GetTransactionsByAccountUseCase getTransactionsByAccountUseCase =
                mock(GetTransactionsByAccountUseCase.class);

        TransferMoneyUseCase transferMoneyUseCase =
                mock(TransferMoneyUseCase.class);

        TransactionWebMapper transactionWebMapper =
                mock(TransactionWebMapper.class);

        TransactionController controller =
                new TransactionController(
                        createTransactionUseCase,
                        getTransactionsByAccountUseCase,
                        transferMoneyUseCase,
                        transactionWebMapper
                );

        TransferMoneyRequest request =
                new TransferMoneyRequest(
                        1L,
                        2L,
                        new BigDecimal("30000")
                );

        Transaction debit = mock(Transaction.class);
        Transaction credit = mock(Transaction.class);

        TransactionResponse debitResponse =
                mock(TransactionResponse.class);

        TransactionResponse creditResponse =
                mock(TransactionResponse.class);

        when(transferMoneyUseCase.transfer(
                1L,
                2L,
                new BigDecimal("30000")
        )).thenReturn(List.of(debit, credit));

        when(transactionWebMapper.toResponse(debit))
                .thenReturn(debitResponse);

        when(transactionWebMapper.toResponse(credit))
                .thenReturn(creditResponse);

        ResponseEntity<List<TransactionResponse>> result =
                controller.transfer(request);

        assertEquals(201, result.getStatusCode().value());
        assertEquals(2, result.getBody().size());
    }
}