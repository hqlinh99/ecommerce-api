package com.hqlinh.sachapi.core;

import java.sql.SQLIntegrityConstraintViolationException;

public class CustomException {
    public static class DuplicatedException extends SQLIntegrityConstraintViolationException {
        public DuplicatedException(String reason) {
            super(reason);
        }
    }
}
