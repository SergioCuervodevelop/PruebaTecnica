package com.cuervo.domain.model;

import com.cuervo.domain.enums.AccountStatus;
import com.cuervo.domain.enums.AccountType;
import com.cuervo.domain.exception.InvalidAmountException;
import com.cuervo.domain.exception.InvalidAccountStateException;
import com.cuervo.domain.exception.InsufficientBalanceException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Account {

    private Long id;
    private AccountType accountType;
    private String accountNumber;
    private AccountStatus status;
    private BigDecimal balance;
    private BigDecimal availableBalance;
    private Boolean gmfExempt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long clientId;
 //Constructor
    public Account(

            AccountType accountType,
            String accountNumber,
            Long clientId
    ) {
        this.accountType = accountType;
        this.accountNumber = accountNumber;
        this.clientId = clientId;

        this.status = AccountStatus.ACTIVE;
        this.balance = BigDecimal.ZERO;
        this.availableBalance = BigDecimal.ZERO;
        this.gmfExempt = false;

        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    //Type of account
    public void deposit(BigDecimal amount) {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException(
                    "Deposit amount must be greater than zero"
            );
        }

        if (status != AccountStatus.ACTIVE) {
            throw new InvalidAccountStateException(
                    "The account is not active"
            );
        }

        this.balance = this.balance.add(amount);
        this.availableBalance = this.availableBalance.add(amount);
        this.updatedAt = LocalDateTime.now();
    }

    public void withdraw(BigDecimal amount) {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException(
                    "Withdrawal amount must be greater than zero"
            );
        }

        if (status != AccountStatus.ACTIVE) {
            throw new InvalidAccountStateException(
                    "The account is not active"
            );
        }

        if (this.availableBalance.compareTo(amount) < 0) {
            throw new InsufficientBalanceException(
                    "Insufficient balance"
            );
        }

        this.balance = this.balance.subtract(amount);
        this.availableBalance = this.availableBalance.subtract(amount);
        this.updatedAt = LocalDateTime.now();
    }
  //Status of Account
  public void cancel() {

      if (status == AccountStatus.CANCELLED) {
          throw new InvalidAccountStateException(
                  "The account is already cancelled"
          );
      }

      if (balance.compareTo(BigDecimal.ZERO) > 0) {
          throw new InvalidAccountStateException(
                  "The account cannot be cancelled because it has a balance"
          );
      }

      this.status = AccountStatus.CANCELLED;
      this.updatedAt = LocalDateTime.now();
  }

    public void activate() {

        if (status == AccountStatus.CANCELLED) {
            throw new InvalidAccountStateException(
                    "A cancelled account cannot be activated"
            );
        }

        this.status = AccountStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

    public void deactivate() {

        if (status == AccountStatus.CANCELLED) {
            throw new InvalidAccountStateException(
                    "A cancelled account cannot be deactivated"
            );
        }

        this.status = AccountStatus.INACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(BigDecimal availableBalance) {
        this.availableBalance = availableBalance;
    }

    public Boolean getGmfExempt() {
        return gmfExempt;
    }

    public void setGmfExempt(Boolean gmfExempt) {
        this.gmfExempt = gmfExempt;
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

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
}
