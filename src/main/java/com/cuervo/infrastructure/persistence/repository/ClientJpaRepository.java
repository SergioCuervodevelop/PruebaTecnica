package com.cuervo.infrastructure.persistence.repository;

import com.cuervo.infrastructure.persistence.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientJpaRepository extends JpaRepository<ClientEntity, Long> {

    Optional<ClientEntity> findByIdentificationNumber(String identificationNumber);
}