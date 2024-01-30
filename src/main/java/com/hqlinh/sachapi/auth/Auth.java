package com.hqlinh.sachapi.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

public class Auth {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthenticationResponse {
        private String accessToken;
        private String refreshToken;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthenticationRequest {
        private String email;
        private String password;
    }
}

