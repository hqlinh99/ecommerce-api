package com.hqlinh.sachapi.auth;

public class AuthException {
    public static class AuthServiceBusinessException extends RuntimeException {
        public AuthServiceBusinessException(String message) {
            super(message);
        }
    }

    public static class AuthNotFoundException extends RuntimeException {
        public AuthNotFoundException(String message) {
            super(message);
        }
    }

    public static class RefreshTokenExpiredException extends RuntimeException {
        public RefreshTokenExpiredException(String message) {
            super(message);
        }
    }
}
