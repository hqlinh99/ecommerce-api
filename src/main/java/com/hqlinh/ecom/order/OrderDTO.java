package com.hqlinh.ecom.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hqlinh.ecom.account.AccountDTO;
import com.hqlinh.ecom.order_item.OrderItemDTO;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

public abstract class OrderDTO {
    private Long id;
    @Min(value = 0L, message = "total amount can not be smaller than 0")
    private Long totalAmount;
    @NotBlank(message = "order status shouldn't be NULL OR EMPTY")
    private OrderStatus status;
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
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderIdDTO {
        private Long id;
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
    }
}

