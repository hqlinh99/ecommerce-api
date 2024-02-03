package com.hqlinh.sachapi.account;

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
}
