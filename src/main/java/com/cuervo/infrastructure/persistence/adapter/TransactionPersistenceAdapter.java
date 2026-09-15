package com.cuervo.infrastructure.persistence.adapter;

import com.cuervo.domain.model.Transaction;
import com.cuervo.domain.port.out.TransactionRepositoryPort;
import com.cuervo.infrastructure.persistence.entity.TransactionEntity;
import com.cuervo.infrastructure.persistence.mapper.TransactionMapper;
import com.cuervo.infrastructure.persistence.repository.TransactionJpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;


@Repository
public class TransactionPersistenceAdapter
        implements TransactionRepositoryPort {

    private final TransactionJpaRepository transactionJpaRepository;
    private final TransactionMapper transactionMapper;

    public TransactionPersistenceAdapter(
            TransactionJpaRepository transactionJpaRepository,
            TransactionMapper transactionMapper) {

        this.transactionJpaRepository = transactionJpaRepository;
        this.transactionMapper = transactionMapper;
    }

    @Override
    public Transaction save(Transaction transaction) {

        TransactionEntity entity =
                transactionMapper.toEntity(transaction);

        TransactionEntity savedEntity =
                transactionJpaRepository.save(entity);

        return transactionMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Transaction> findById(Long id) {

        return transactionJpaRepository.findById(id)
                .map(transactionMapper::toDomain);
    }

    @Override
    public List<Transaction> findByAccountId(Long accountId) {

        return transactionJpaRepository.findByAccount_Id(accountId)
                .stream()
                .map(transactionMapper::toDomain)
                .collect(Collectors.toList());
    }
}