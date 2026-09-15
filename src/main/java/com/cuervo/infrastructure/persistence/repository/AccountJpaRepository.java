package com.cuervo.infrastructure.persistence.repository;

import com.cuervo.domain.enums.AccountType;
import com.cuervo.infrastructure.persistence.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountJpaRepository extends JpaRepository<AccountEntity, Long> {

    Optional<AccountEntity> findByAccountNumber(String accountNumber);

    boolean existsByClient_Id(Long clientId);

    boolean existsByClient_IdAndAccountType(
            Long clientId,
            AccountType accountType
    );

    List<AccountEntity> findByClient_Id(Long clientId);
}