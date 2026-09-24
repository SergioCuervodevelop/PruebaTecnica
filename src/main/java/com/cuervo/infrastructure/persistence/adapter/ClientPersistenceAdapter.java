package com.cuervo.infrastructure.persistence.adapter;

import com.cuervo.domain.enums.IdentificationType;
import com.cuervo.domain.model.Client;
import com.cuervo.domain.port.out.ClientRepositoryPort;
import com.cuervo.infrastructure.persistence.entity.ClientEntity;
import com.cuervo.infrastructure.persistence.mapper.ClientMapper;
import com.cuervo.infrastructure.persistence.repository.ClientJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ClientPersistenceAdapter
        implements ClientRepositoryPort {

    private final ClientJpaRepository clientJpaRepository;
    private final ClientMapper clientMapper;

    public ClientPersistenceAdapter(
            ClientJpaRepository clientJpaRepository,
            ClientMapper clientMapper) {

        this.clientJpaRepository =
                clientJpaRepository;

        this.clientMapper =
                clientMapper;
    }

    @Override
    public Client save(Client client) {

        ClientEntity entity =
                clientMapper.toEntity(client);

        ClientEntity savedEntity =
                clientJpaRepository.save(entity);

        return clientMapper.toDomain(
                savedEntity
        );
    }

    @Override
    public Optional<Client>
    findByIdentificationTypeAndIdentificationNumber(
            IdentificationType identificationType,
            String identificationNumber) {

        return clientJpaRepository
                .findByIdentificationTypeAndIdentificationNumber(
                        identificationType,
                        identificationNumber
                )
                .map(clientMapper::toDomain);
    }

    @Override
    public List<Client> findAll() {

        return clientJpaRepository
                .findAll()
                .stream()
                .map(clientMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteByIdentificationTypeAndIdentificationNumber(
            IdentificationType identificationType,
            String identificationNumber) {

        clientJpaRepository
                .findByIdentificationTypeAndIdentificationNumber(
                        identificationType,
                        identificationNumber
                )
                .ifPresent(
                        clientJpaRepository::delete
                );
    }
}