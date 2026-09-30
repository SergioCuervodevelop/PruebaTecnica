package com.cuervo.domain.model;

import com.cuervo.domain.enums.ClientStatus;
import com.cuervo.domain.enums.IdentificationType;
import com.cuervo.domain.exception.InvalidClientException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

public class Client {

    private Long id;
    private IdentificationType identificationType;
    private String identificationNumber;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthDate;
    private ClientStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Client(
            IdentificationType identificationType,
            String identificationNumber,
            String firstName,
            String lastName,
            String email,
            LocalDate birthDate) {

        if (!isAdult(birthDate)) {
            throw new InvalidClientException(
                    "Client must be at least 18 years old"
            );
        }

        this.identificationType = identificationType;
        this.identificationNumber = identificationNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.birthDate = birthDate;
        this.status = ClientStatus.ACTIVE;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    private Client() {
    }

    public static Client forUpdate(
            String firstName,
            String lastName,
            String email) {

        Client client = new Client();

        client.firstName = firstName;
        client.lastName = lastName;
        client.email = email;

        return client;
    }

    private boolean isAdult(LocalDate birthDate) {

        int age = Period
                .between(birthDate, LocalDate.now())
                .getYears();

        return age >= 18;
    }

    public void updateInformation(
            String firstName,
            String lastName,
            String email) {

        if (status == ClientStatus.DELETED) {
            throw new InvalidClientException(
                    "A deleted client cannot be updated"
            );
        }

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.updatedAt = LocalDateTime.now();
    }

    public void delete() {

        if (status == ClientStatus.DELETED) {
            throw new InvalidClientException(
                    "The client is already deleted"
            );
        }

        this.status = ClientStatus.DELETED;
        this.updatedAt = LocalDateTime.now();
    }

    public void restore() {

        if (status != ClientStatus.DELETED) {
            throw new InvalidClientException(
                    "Only a deleted client can be restored"
            );
        }

        this.status = ClientStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public IdentificationType getIdentificationType() {
        return identificationType;
    }

    public void setIdentificationType(IdentificationType identificationType) {
        this.identificationType = identificationType;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public ClientStatus getStatus() {
        return status;
    }

    public void setStatus(ClientStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}