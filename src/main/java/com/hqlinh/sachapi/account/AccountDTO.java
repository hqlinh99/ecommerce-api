package com.hqlinh.sachapi.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

public abstract class AccountDTO {
    private Long id;
    @NotBlank(message = "account First Name shouldn't be NULL OR EMPTY")
    private String firstName;
    @NotBlank(message = "account Last Name shouldn't be NULL OR EMPTY")
    private String lastName;
    @NotBlank(message = "email shouldn't be NULL OR EMPTY")
    @Email(message = "invalid email address")
    private String email;
    @NotBlank(message = "password shouldn't be NULL OR EMPTY")
    private String password;
    @NotBlank(message = "password shouldn't be NULL OR EMPTY")
    private String newPassword;
    private Role role;

    @Getter
    @Setter
    @EqualsAndHashCode
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AccountRequestDTO {
        private String firstName;
        private String lastName;
        private String email;
        private String password;
        private Role role;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class AccountResponseDTO {
        private long id;
        private String firstName;
        private String lastName;
        private String email;
        private Role role;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PasswordRequest {
        private String password;
        private String newPassword;
    }
}

