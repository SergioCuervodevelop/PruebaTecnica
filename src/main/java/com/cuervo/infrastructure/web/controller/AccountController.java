package com.cuervo.infrastructure.web.controller;

import com.cuervo.application.port.in.account.CancelAccountUseCase;
import com.cuervo.application.port.in.account.ChangeAccountStatusUseCase;
import com.cuervo.application.port.in.account.CreateAccountUseCase;
import com.cuervo.application.port.in.account.GetAccountUseCase;
import com.cuervo.application.port.in.account.GetAccountsByClientUseCase;
import com.cuervo.domain.enums.AccountStatus;
import com.cuervo.domain.enums.IdentificationType;
import com.cuervo.domain.model.Account;
import com.cuervo.infrastructure.web.dtoaccount.AccountResponse;
import com.cuervo.infrastructure.web.dtoaccount.CreateAccountRequest;
import com.cuervo.infrastructure.web.mapper.AccountWebMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final CreateAccountUseCase createAccountUseCase;
    private final GetAccountUseCase getAccountUseCase;
    private final GetAccountsByClientUseCase getAccountsByClientUseCase;
    private final ChangeAccountStatusUseCase changeAccountStatusUseCase;
    private final CancelAccountUseCase cancelAccountUseCase;
    private final AccountWebMapper accountWebMapper;

    public AccountController(
            CreateAccountUseCase createAccountUseCase,
            GetAccountUseCase getAccountUseCase,
            GetAccountsByClientUseCase getAccountsByClientUseCase,
            ChangeAccountStatusUseCase changeAccountStatusUseCase,
            CancelAccountUseCase cancelAccountUseCase,
            AccountWebMapper accountWebMapper) {

        this.createAccountUseCase = createAccountUseCase;
        this.getAccountUseCase = getAccountUseCase;
        this.getAccountsByClientUseCase =
                getAccountsByClientUseCase;
        this.changeAccountStatusUseCase =
                changeAccountStatusUseCase;
        this.cancelAccountUseCase =
                cancelAccountUseCase;
        this.accountWebMapper =
                accountWebMapper;
    }

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(
            @Valid @RequestBody CreateAccountRequest request) {

        Account createdAccount =
                createAccountUseCase.create(
                        request.accountType(),
                        request.identificationType(),
                        request.identificationNumber()
                );

        AccountResponse response =
                accountWebMapper.toResponse(
                        createdAccount
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountResponse> getAccount(
            @PathVariable String accountNumber) {

        Account account =
                getAccountUseCase
                        .getByAccountNumber(
                                accountNumber
                        );

        AccountResponse response =
                accountWebMapper.toResponse(account);

        return ResponseEntity.ok(response);
    }

    @GetMapping(
            "/client/{identificationType}/{identificationNumber}"
    )
    public ResponseEntity<List<AccountResponse>>
    getAccountsByClient(
            @PathVariable
            IdentificationType identificationType,

            @PathVariable
            String identificationNumber) {

        List<AccountResponse> response =
                getAccountsByClientUseCase
                        .getByIdentification(
                                identificationType,
                                identificationNumber
                        )
                        .stream()
                        .map(accountWebMapper::toResponse)
                        .toList();

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{accountNumber}/status")
    public ResponseEntity<AccountResponse> changeStatus(
            @PathVariable String accountNumber,
            @RequestParam AccountStatus status) {

        Account account =
                changeAccountStatusUseCase
                        .change(
                                accountNumber,
                                status
                        );

        return ResponseEntity.ok(
                accountWebMapper.toResponse(
                        account
                )
        );
    }

    @DeleteMapping("/{accountNumber}")
    public ResponseEntity<AccountResponse> cancelAccount(
            @PathVariable String accountNumber) {

        Account account =
                cancelAccountUseCase
                        .cancel(accountNumber);

        return ResponseEntity.ok(
                accountWebMapper.toResponse(
                        account
                )
        );
    }
}