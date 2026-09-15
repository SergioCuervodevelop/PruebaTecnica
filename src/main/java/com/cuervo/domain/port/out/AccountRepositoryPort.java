package com.cuervo.domain.port.out;

import com.cuervo.domain.enums.AccountType;
import com.cuervo.domain.model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepositoryPort {

    Account save(Account account);

    Optional<Account> findById(Long id);

    Optional<Account> findByAccountNumber(String accountNumber);

    boolean existsByClientId(Long clientId);

    boolean existsByClientIdAndAccountType(
            Long clientId,
            AccountType accountType
    );

    List<Account> findByClientId(Long clientId);

    void deleteById(Long id);
}