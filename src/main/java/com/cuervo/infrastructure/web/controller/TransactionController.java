package com.cuervo.infrastructure.web.controller;

import com.cuervo.application.port.in.transaction.CreateTransactionUseCase;
import com.cuervo.application.port.in.transaction.GetTransactionsByAccountUseCase;
import com.cuervo.application.port.in.transaction.TransferMoneyUseCase;
import com.cuervo.domain.model.Transaction;
import com.cuervo.infrastructure.web.dtotransaction.CreateTransactionRequest;
import com.cuervo.infrastructure.web.dtotransaction.TransactionResponse;
import com.cuervo.infrastructure.web.dtotransaction.TransferMoneyRequest;
import com.cuervo.infrastructure.web.mapper.TransactionWebMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final CreateTransactionUseCase createTransactionUseCase;
    private final GetTransactionsByAccountUseCase getTransactionsByAccountUseCase;
    private final TransferMoneyUseCase transferMoneyUseCase;
    private final TransactionWebMapper transactionWebMapper;

    public TransactionController(
            CreateTransactionUseCase createTransactionUseCase,
            GetTransactionsByAccountUseCase getTransactionsByAccountUseCase,
            TransferMoneyUseCase transferMoneyUseCase,
            TransactionWebMapper transactionWebMapper
    ) {
        this.createTransactionUseCase = createTransactionUseCase;
        this.getTransactionsByAccountUseCase =
                getTransactionsByAccountUseCase;
        this.transferMoneyUseCase = transferMoneyUseCase;
        this.transactionWebMapper = transactionWebMapper;
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> create(
            @Valid @RequestBody CreateTransactionRequest request) {

        Transaction created =
                createTransactionUseCase.execute(
                        request.accountNumber(),
                        request.transactionType(),
                        request.amount()
                );

        TransactionResponse response =
                transactionWebMapper.toResponse(
                        created,
                        request.accountNumber()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/account/{accountNumber}")
    public ResponseEntity<List<TransactionResponse>> getByAccount(
            @PathVariable String accountNumber
    ) {

        List<TransactionResponse> response =
                getTransactionsByAccountUseCase
                        .execute(accountNumber)
                        .stream()
                        .map(transaction ->
                                transactionWebMapper.toResponse(
                                        transaction,
                                        accountNumber
                                ))
                        .toList();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/transfer")
    public ResponseEntity<List<TransactionResponse>> transfer(
            @Valid @RequestBody TransferMoneyRequest request) {

        List<Transaction> transactions =
                transferMoneyUseCase.transfer(
                        request.sourceAccountNumber(),
                        request.destinationAccountNumber(),
                        request.amount()
                );

        TransactionResponse debitResponse =
                transactionWebMapper.toResponse(
                        transactions.get(0),
                        request.sourceAccountNumber()
                );

        TransactionResponse creditResponse =
                transactionWebMapper.toResponse(
                        transactions.get(1),
                        request.destinationAccountNumber()
                );

        List<TransactionResponse> response =
                List.of(
                        debitResponse,
                        creditResponse
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}