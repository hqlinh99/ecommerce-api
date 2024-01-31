package com.hqlinh.sachapi.account;

public class AccountException {
    public static class AccountServiceBusinessException extends RuntimeException {
        public AccountServiceBusinessException(String message) {
            super(message);
        }
    }

    public static class AccountNotFoundException extends AccountServiceBusinessException {
        public AccountNotFoundException(String message) {
            super(message);
        }
    }

    public static class AccountAlreadyExistsException extends AccountServiceBusinessException {
        public AccountAlreadyExistsException(String message) {
            super(message);
        }
    }
}
