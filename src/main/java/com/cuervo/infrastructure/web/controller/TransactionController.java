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

        Transaction transaction =
                transactionWebMapper.toDomain(request);

        Transaction created =
                createTransactionUseCase.execute(transaction);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(transactionWebMapper.toResponse(created));
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<TransactionResponse>> getByAccount(
            @PathVariable Long accountId
    ) {

        List<TransactionResponse> response =
                getTransactionsByAccountUseCase
                        .execute(accountId)
                        .stream()
                        .map(transactionWebMapper::toResponse)
                        .toList();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/transfer")
    public ResponseEntity<List<TransactionResponse>> transfer(
            @Valid @RequestBody TransferMoneyRequest request) {

        List<Transaction> transactions =
                transferMoneyUseCase.transfer(
                        request.sourceAccountId(),
                        request.destinationAccountId(),
                        request.amount()
                );

        List<TransactionResponse> response =
                transactions.stream()
                        .map(transactionWebMapper::toResponse)
                        .toList();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}