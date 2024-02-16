package com.hqlinh.ecom.order_item;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hqlinh.ecom.order.OrderDTO;
import com.hqlinh.ecom.product.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

public abstract class OrderItemDTO {
    private Long id;
    private Integer quantity;
    private Long price;
    private ProductDTO product;
    private OrderDTO order;
    private Number createdAt;
    private Number updatedAt;


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderItemRequestDTO {
        private Integer quantity;
        private ProductDTO.ProductIdDTO product;
        private Number createdAt = new Date().getTime();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderItemIdDTO {
        private Long id;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class OrderItemResponseDTO {
        private Long id;
        private Integer quantity;
        private Long price;
        private ProductDTO.ProductResponseDTO product;
        @JsonIgnoreProperties(value = "orderItems")
        private OrderDTO.OrderResponseDTO order;
    }
}

