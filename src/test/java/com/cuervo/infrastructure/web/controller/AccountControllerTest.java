package com.cuervo.infrastructure.web.controller;

import com.cuervo.application.port.in.account.CancelAccountUseCase;
import com.cuervo.application.port.in.account.ChangeAccountStatusUseCase;
import com.cuervo.application.port.in.account.CreateAccountUseCase;
import com.cuervo.application.port.in.account.GetAccountUseCase;
import com.cuervo.application.port.in.account.GetAccountsByClientUseCase;
import com.cuervo.domain.enums.AccountStatus;
import com.cuervo.domain.model.Account;
import com.cuervo.infrastructure.web.dtoaccount.AccountResponse;
import com.cuervo.infrastructure.web.dtoaccount.CreateAccountRequest;
import com.cuervo.infrastructure.web.mapper.AccountWebMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AccountControllerTest {

    @Test
    void shouldCreateAccountSuccessfully() {

        CreateAccountUseCase createAccountUseCase =
                mock(CreateAccountUseCase.class);

        GetAccountUseCase getAccountUseCase =
                mock(GetAccountUseCase.class);

        GetAccountsByClientUseCase getAccountsByClientUseCase =
                mock(GetAccountsByClientUseCase.class);

        ChangeAccountStatusUseCase changeAccountStatusUseCase =
                mock(ChangeAccountStatusUseCase.class);

        CancelAccountUseCase cancelAccountUseCase =
                mock(CancelAccountUseCase.class);

        AccountWebMapper accountWebMapper =
                mock(AccountWebMapper.class);

        AccountController controller =
                new AccountController(
                        createAccountUseCase,
                        getAccountUseCase,
                        getAccountsByClientUseCase,
                        changeAccountStatusUseCase,
                        cancelAccountUseCase,
                        accountWebMapper
                );

        CreateAccountRequest request =
                mock(CreateAccountRequest.class);

        Account account = mock(Account.class);
        Account createdAccount = mock(Account.class);
        AccountResponse response = mock(AccountResponse.class);

        when(accountWebMapper.toDomain(request))
                .thenReturn(account);

        when(createAccountUseCase.create(account))
                .thenReturn(createdAccount);

        when(accountWebMapper.toResponse(createdAccount))
                .thenReturn(response);

        ResponseEntity<AccountResponse> result =
                controller.createAccount(request);

        assertEquals(201, result.getStatusCode().value());
        assertEquals(response, result.getBody());
    }

    @Test
    void shouldChangeAccountStatusSuccessfully() {

        CreateAccountUseCase createAccountUseCase =
                mock(CreateAccountUseCase.class);

        GetAccountUseCase getAccountUseCase =
                mock(GetAccountUseCase.class);

        GetAccountsByClientUseCase getAccountsByClientUseCase =
                mock(GetAccountsByClientUseCase.class);

        ChangeAccountStatusUseCase changeAccountStatusUseCase =
                mock(ChangeAccountStatusUseCase.class);

        CancelAccountUseCase cancelAccountUseCase =
                mock(CancelAccountUseCase.class);

        AccountWebMapper accountWebMapper =
                mock(AccountWebMapper.class);

        AccountController controller =
                new AccountController(
                        createAccountUseCase,
                        getAccountUseCase,
                        getAccountsByClientUseCase,
                        changeAccountStatusUseCase,
                        cancelAccountUseCase,
                        accountWebMapper
                );

        Account account = mock(Account.class);
        AccountResponse response = mock(AccountResponse.class);

        when(changeAccountStatusUseCase.change(
                1L,
                AccountStatus.INACTIVE
        )).thenReturn(account);

        when(accountWebMapper.toResponse(account))
                .thenReturn(response);

        ResponseEntity<AccountResponse> result =
                controller.changeStatus(
                        1L,
                        AccountStatus.INACTIVE
                );

        assertEquals(200, result.getStatusCode().value());
        assertEquals(response, result.getBody());
    }

    @Test
    void shouldGetAccountsByClientSuccessfully() {

        CreateAccountUseCase createAccountUseCase =
                mock(CreateAccountUseCase.class);

        GetAccountUseCase getAccountUseCase =
                mock(GetAccountUseCase.class);

        GetAccountsByClientUseCase getAccountsByClientUseCase =
                mock(GetAccountsByClientUseCase.class);

        ChangeAccountStatusUseCase changeAccountStatusUseCase =
                mock(ChangeAccountStatusUseCase.class);

        CancelAccountUseCase cancelAccountUseCase =
                mock(CancelAccountUseCase.class);

        AccountWebMapper accountWebMapper =
                mock(AccountWebMapper.class);

        AccountController controller =
                new AccountController(
                        createAccountUseCase,
                        getAccountUseCase,
                        getAccountsByClientUseCase,
                        changeAccountStatusUseCase,
                        cancelAccountUseCase,
                        accountWebMapper
                );

        Account account1 = mock(Account.class);
        Account account2 = mock(Account.class);

        AccountResponse response1 = mock(AccountResponse.class);
        AccountResponse response2 = mock(AccountResponse.class);

        when(getAccountsByClientUseCase.getByClientId(2L))
                .thenReturn(List.of(account1, account2));

        when(accountWebMapper.toResponse(account1))
                .thenReturn(response1);

        when(accountWebMapper.toResponse(account2))
                .thenReturn(response2);

        ResponseEntity<List<AccountResponse>> result =
                controller.getAccountsByClient(2L);

        assertEquals(200, result.getStatusCode().value());
        assertEquals(2, result.getBody().size());
        assertEquals(response1, result.getBody().get(0));
        assertEquals(response2, result.getBody().get(1));
    }
}