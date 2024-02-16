package com.hqlinh.ecom.order;

public class OrderException {
    public static class OrderServiceBusinessException extends RuntimeException {
        public OrderServiceBusinessException(String message) {
            super(message);
        }
    }
}
