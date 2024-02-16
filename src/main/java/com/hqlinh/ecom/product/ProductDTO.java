package com.hqlinh.ecom.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

public abstract class ProductDTO {
    private Long id;
    @NotBlank(message = "product name shouldn't be NULL OR EMPTY")
    private String name;

    private String description;

    @NotBlank(message = "product type shouldn't be NULL OR EMPTY")
    private String productType;

    @Min(value = 1, message = "quantity is not defined")
    private int quantity;

    @Min(value = 200, message = "product price can't be less than 200")
    @Max(value = 500000, message = "product price can't be more than 500000")
    private long price;
    private List<String> images;
    private Number createdAt;
    private Number updatedAt;


    @Getter
    @Setter
    @EqualsAndHashCode
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProductRequestDTO {
        private String name;
        private String description;
        private String productType;
        private Integer quantity;
        private Long price;
        private List<String> images;
        private Number createdAt;
        private Number updatedAt;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProductIdDTO {
        private Long id;
    }

    @Getter
    @Setter
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ProductResponseDTO extends ProductRequestDTO {
        private Long id;
    }
}

