package com.hqlinh.sachapi.product;

public class ProductException {
    public static class ProductServiceBusinessException extends RuntimeException {
        public ProductServiceBusinessException(String message) {
            super(message);
        }
    }
}
