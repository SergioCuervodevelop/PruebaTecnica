package com.cuervo.infrastructure.persistence.adapter;

import com.cuervo.domain.enums.AccountType;
import com.cuervo.domain.model.Account;
import com.cuervo.domain.port.out.AccountRepositoryPort;
import com.cuervo.infrastructure.persistence.entity.AccountEntity;
import com.cuervo.infrastructure.persistence.mapper.AccountMapper;
import com.cuervo.infrastructure.persistence.repository.AccountJpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

@Repository
public class AccountPersistenceAdapter  implements AccountRepositoryPort {

    private final AccountJpaRepository accountJpaRepository;
    private final AccountMapper accountMapper;

    public AccountPersistenceAdapter(
            AccountJpaRepository accountJpaRepository,
            AccountMapper accountMapper) {

        this.accountJpaRepository = accountJpaRepository;
        this.accountMapper = accountMapper;
    }

    @Override
    public Account save(Account account) {

        AccountEntity entity = accountMapper.toEntity(account);

        AccountEntity savedEntity =
                accountJpaRepository.save(entity);

        return accountMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Account> findById(Long id) {

        return accountJpaRepository.findById(id)
                .map(accountMapper::toDomain);
    }

    @Override
    public Optional<Account> findByAccountNumber(
            String accountNumber) {

        return accountJpaRepository
                .findByAccountNumber(accountNumber)
                .map(accountMapper::toDomain);
    }

    @Override
    public boolean existsByClientId(Long clientId) {

        return accountJpaRepository.existsByClient_Id(clientId);
    }

    @Override
    public void deleteById(Long id) {

        accountJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByClientIdAndAccountType(
            Long clientId,
            AccountType accountType) {

        return accountJpaRepository
                .existsByClient_IdAndAccountType(
                        clientId,
                        accountType
                );
    }
    @Override
    public List<Account> findByClientId(Long clientId) {
        return accountJpaRepository.findByClient_Id(clientId)
                .stream()
                .map(accountMapper::toDomain)
                .collect(Collectors.toList());
    }
}