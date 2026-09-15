package com.cuervo.infrastructure.web.dtoclient;

import com.cuervo.domain.enums.IdentificationType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CreateClientRequest(

        @NotNull(message = "Identification type is required")
        IdentificationType identificationType,

        @NotBlank(message = "Identification number is required")
        String identificationNumber,

        @NotBlank(message = "First name is required")
        @Size(min = 2, message = "First name must have at least 2 characters")
        String firstName,

        @NotBlank(message = "Last name is required")
        @Size(min = 2, message = "Last name must have at least 2 characters")
        String lastName,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        String email,

        @NotNull(message = "Birth date is required")
        LocalDate birthDate
) {
}