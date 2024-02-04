package com.hqlinh.sachapi.account;

import org.springframework.security.core.AuthenticationException;

public class AccountException {
    public static class AccountServiceBusinessException extends RuntimeException {
        public AccountServiceBusinessException() {
        }

        public AccountServiceBusinessException(String message) {
            super(message);
        }

        public AccountServiceBusinessException(String message, Throwable cause) {
            super(message, cause);
        }

        public AccountServiceBusinessException(Throwable cause) {
            super(cause);
        }

        public AccountServiceBusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }

    public static class InvalidPasswordException extends AuthenticationException {
        public InvalidPasswordException(String explanation) {
            super(explanation);
        }

        public InvalidPasswordException(String msg, Throwable cause) {
            super(msg, cause);
        }
    }
}
