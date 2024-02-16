package com.hqlinh.ecom.order_item;

public class OrderItemException {
    public static class OrderItemServiceBusinessException extends RuntimeException {
        public OrderItemServiceBusinessException(String message) {
            super(message);
        }
    }
}
