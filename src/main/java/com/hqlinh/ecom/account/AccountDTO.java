package com.hqlinh.ecom.account;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
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
    private String username;
    @NotBlank(message = "password shouldn't be NULL OR EMPTY")
    private String password;
    @NotBlank(message = "password shouldn't be NULL OR EMPTY")
    private String newPassword;
    private String avatar;
    private String biography;
    private Role role;
    private Number createdAt;
    private Number updatedAt;

    @Getter
    @Setter
    @EqualsAndHashCode
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AccountRequestDTO {
        private String firstName;
        private String lastName;
        private String email;
        private String username;
        private String password;
        private String avatar;
        private String biography;
        private Role role;
        private Number createdAt;
        private Number updatedAt;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AccountIdDTO {
        private Long id;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class AccountResponseDTO {
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private String username;
        private String avatar;
        private String biography;
        private Role role;
        private Number createdAt;
        private Number updatedAt;
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

