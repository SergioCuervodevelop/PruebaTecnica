package com.cuervo.infrastructure.web.controller;

import com.cuervo.application.port.in.transaction.CreateTransactionUseCase;
import com.cuervo.application.port.in.transaction.GetTransactionsByAccountUseCase;
import com.cuervo.application.port.in.transaction.TransferMoneyUseCase;
import com.cuervo.domain.enums.TransactionType;
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
import static org.mockito.Mockito.*;

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
                        "5312345678",
                        TransactionType.DEPOSIT,
                        new BigDecimal("10000")
                );

        Transaction createdTransaction =
                mock(Transaction.class);

        TransactionResponse response =
                mock(TransactionResponse.class);

        when(createTransactionUseCase.execute(
                "5312345678",
                TransactionType.DEPOSIT,
                new BigDecimal("10000")
        )).thenReturn(createdTransaction);

        when(transactionWebMapper.toResponse(
                createdTransaction,
                "5312345678"
        )).thenReturn(response);

        ResponseEntity<TransactionResponse> result =
                controller.create(request);

        assertEquals(
                201,
                result.getStatusCode().value()
        );

        assertEquals(
                response,
                result.getBody()
        );

        verify(createTransactionUseCase).execute(
                "5312345678",
                TransactionType.DEPOSIT,
                new BigDecimal("10000")
        );

        verify(transactionWebMapper).toResponse(
                createdTransaction,
                "5312345678"
        );
    }

    @Test
    void shouldGetTransactionsByAccountSuccessfully() {

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

        Transaction transaction1 =
                mock(Transaction.class);

        Transaction transaction2 =
                mock(Transaction.class);

        TransactionResponse response1 =
                mock(TransactionResponse.class);

        TransactionResponse response2 =
                mock(TransactionResponse.class);

        when(getTransactionsByAccountUseCase.execute(
                "5312345678"
        )).thenReturn(
                List.of(
                        transaction1,
                        transaction2
                )
        );

        when(transactionWebMapper.toResponse(
                transaction1,
                "5312345678"
        )).thenReturn(response1);

        when(transactionWebMapper.toResponse(
                transaction2,
                "5312345678"
        )).thenReturn(response2);

        ResponseEntity<List<TransactionResponse>> result =
                controller.getByAccount(
                        "5312345678"
                );

        assertEquals(
                200,
                result.getStatusCode().value()
        );

        assertEquals(
                2,
                result.getBody().size()
        );

        assertEquals(
                response1,
                result.getBody().get(0)
        );

        assertEquals(
                response2,
                result.getBody().get(1)
        );

        verify(getTransactionsByAccountUseCase)
                .execute("5312345678");
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
                        "5312345678",
                        "3312345678",
                        new BigDecimal("30000")
                );

        Transaction debit =
                mock(Transaction.class);

        Transaction credit =
                mock(Transaction.class);

        TransactionResponse debitResponse =
                mock(TransactionResponse.class);

        TransactionResponse creditResponse =
                mock(TransactionResponse.class);

        when(transferMoneyUseCase.transfer(
                "5312345678",
                "3312345678",
                new BigDecimal("30000")
        )).thenReturn(
                List.of(
                        debit,
                        credit
                )
        );

        when(transactionWebMapper.toResponse(
                debit,
                "5312345678"
        )).thenReturn(debitResponse);

        when(transactionWebMapper.toResponse(
                credit,
                "3312345678"
        )).thenReturn(creditResponse);

        ResponseEntity<List<TransactionResponse>> result =
                controller.transfer(request);

        assertEquals(
                201,
                result.getStatusCode().value()
        );

        assertEquals(
                2,
                result.getBody().size()
        );

        assertEquals(
                debitResponse,
                result.getBody().get(0)
        );

        assertEquals(
                creditResponse,
                result.getBody().get(1)
        );

        verify(transferMoneyUseCase).transfer(
                "5312345678",
                "3312345678",
                new BigDecimal("30000")
        );

        verify(transactionWebMapper).toResponse(
                debit,
                "5312345678"
        );

        verify(transactionWebMapper).toResponse(
                credit,
                "3312345678"
        );
    }
}