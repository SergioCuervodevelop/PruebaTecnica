package com.cuervo.infrastructure.config;

import com.cuervo.application.port.in.account.*;
import com.cuervo.application.port.in.client.*;
import com.cuervo.application.port.in.transaction.CreateTransactionUseCase;
import com.cuervo.application.port.in.transaction.GetTransactionsByAccountUseCase;
import com.cuervo.application.port.in.transaction.TransferMoneyUseCase;
import com.cuervo.application.service.account.*;
import com.cuervo.application.service.client.*;
import com.cuervo.application.service.transaction.CreateTransactionService;
import com.cuervo.application.service.transaction.GetTransactionsByAccountService;
import com.cuervo.application.service.transaction.TransferMoneyService;
import com.cuervo.domain.port.out.AccountNumberGeneratorPort;
import com.cuervo.domain.port.out.AccountRepositoryPort;
import com.cuervo.domain.port.out.ClientRepositoryPort;
import com.cuervo.domain.port.out.TransactionRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {


    @Bean
    public CreateClientUseCase createClientUseCase(
            ClientRepositoryPort clientRepositoryPort) {

        return new CreateClientService(
                clientRepositoryPort
        );
    }

    @Bean
    public GetClientUseCase getClientUseCase(
            ClientRepositoryPort clientRepositoryPort) {

        return new GetClientService(
                clientRepositoryPort
        );
    }

    @Bean
    public UpdateClientUseCase updateClientUseCase(
            ClientRepositoryPort clientRepositoryPort) {

        return new UpdateClientService(
                clientRepositoryPort
        );
    }

    @Bean
    public DeleteClientUseCase deleteClientUseCase(
            ClientRepositoryPort clientRepositoryPort,
            AccountRepositoryPort accountRepositoryPort) {

        return new DeleteClientService(
                clientRepositoryPort,
                accountRepositoryPort
        );
    }

    @Bean
    public GetClientSummaryUseCase getClientSummaryUseCase(
            ClientRepositoryPort clientRepositoryPort,
            AccountRepositoryPort accountRepositoryPort) {

        return new GetClientSummaryService(
                clientRepositoryPort,
                accountRepositoryPort
        );
    }



    @Bean
    public CreateAccountUseCase createAccountUseCase(
            AccountRepositoryPort accountRepositoryPort,
            ClientRepositoryPort clientRepositoryPort,
            AccountNumberGeneratorPort accountNumberGeneratorPort) {

        return new CreateAccountService(
                accountRepositoryPort,
                clientRepositoryPort,
                accountNumberGeneratorPort
        );
    }

    @Bean
    public GetAccountUseCase getAccountUseCase(
            AccountRepositoryPort accountRepositoryPort) {

        return new GetAccountService(
                accountRepositoryPort
        );
    }

    @Bean
    public GetAccountsByClientUseCase getAccountsByClientUseCase(
            AccountRepositoryPort accountRepositoryPort,
            ClientRepositoryPort clientRepositoryPort) {

        return new GetAccountsByClientService(
                accountRepositoryPort,
                clientRepositoryPort
        );
    }

    @Bean
    public ChangeAccountStatusUseCase changeAccountStatusUseCase(
            AccountRepositoryPort accountRepositoryPort) {

        return new ChangeAccountStatusService(
                accountRepositoryPort
        );
    }

    @Bean
    public CancelAccountUseCase cancelAccountUseCase(
            AccountRepositoryPort accountRepositoryPort) {

        return new CancelAccountService(
                accountRepositoryPort
        );
    }

    @Bean
    public CreateTransactionUseCase createTransactionUseCase(
            TransactionRepositoryPort transactionRepositoryPort,
            AccountRepositoryPort accountRepositoryPort) {

        return new CreateTransactionService(
                transactionRepositoryPort,
                accountRepositoryPort
        );
    }

    @Bean
    public GetTransactionsByAccountUseCase getTransactionsByAccountUseCase(
            TransactionRepositoryPort transactionRepositoryPort,
            AccountRepositoryPort accountRepositoryPort) {

        return new GetTransactionsByAccountService(
                transactionRepositoryPort,
                accountRepositoryPort
        );
    }

    @Bean
    public TransferMoneyUseCase transferMoneyUseCase(
            AccountRepositoryPort accountRepositoryPort,
            TransactionRepositoryPort transactionRepositoryPort) {

        return new TransferMoneyService(
                accountRepositoryPort,
                transactionRepositoryPort
        );
    }
}