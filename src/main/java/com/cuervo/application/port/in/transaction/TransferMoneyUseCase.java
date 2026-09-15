package com.cuervo.application.port.in.transaction;

import com.cuervo.domain.model.Transaction;

import java.math.BigDecimal;
import java.util.List;

public interface TransferMoneyUseCase {

    List<Transaction> transfer(
            Long sourceAccountId,
            Long destinationAccountId,
            BigDecimal amount
    );
}