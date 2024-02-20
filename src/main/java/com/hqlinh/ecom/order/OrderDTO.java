package com.hqlinh.ecom.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hqlinh.ecom.account.AccountDTO;
import com.hqlinh.ecom.order_item.OrderItemDTO;
import com.hqlinh.ecom.payment.PaymentInfo;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

public abstract class OrderDTO {
    private Long id;
    @Min(value = 0L, message = "total amount can not be smaller than 0")
    private Long totalAmount;
    @NotNull(message = "order status shouldn't be NULL")
    private OrderStatus status;

    private PaymentInfo payment;
    private String note;
    private Number createdAt;
    private Number updatedAt;
    @NotNull(message = "must be a customer in the order")
    private AccountDTO.AccountRequestDTO account;
    @NotEmpty(message = "must be at least one order item in the order")
    private Set<OrderItemDTO.OrderItemRequestDTO> orderItems;



    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderRequestDTO {
        private Number createdAt = new Date().getTime();
        private AccountDTO.AccountIdDTO account;
        private Set<OrderItemDTO.OrderItemRequestDTO> orderItems;
        private String note;
        private PaymentInfo payment;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderIdRequestDTO {
        private Long id;
    }
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderStatusRequestDTO {
        private OrderStatus status;
        private Number updatedAt = new Date().getTime();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class OrderResponseDTO {
        private Long id;
        private Long totalAmount;
        private Number createdAt;
        private Number updatedAt;
        private AccountDTO.AccountResponseDTO account;
        @JsonIgnoreProperties(value = "order")
        private Set<OrderItemDTO.OrderItemResponseDTO> orderItems;
        private OrderStatus status;
        private String note;
        private PaymentInfo payment;
    }
}

