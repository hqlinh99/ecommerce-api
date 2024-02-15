package com.hqlinh.sachapi.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

public abstract class ProductDTO {
    @NotBlank(message = "product name shouldn't be NULL OR EMPTY")
    private String name;

    private String description;

    @NotBlank(message = "product type shouldn't be NULL OR EMPTY")
    private String productType;

    @Min(value = 1, message = "quantity is not defined")
    private int quantity;

    @Min(value = 200, message = "product price can't be less than 200")
    @Max(value = 500000, message = "product price can't be more than 500000")
    private double price;

    private String supplierName;

    @NotBlank(message = "supplier code shouldn't be NULL OR EMPTY")
    private String supplierCode;
    private List<String> images;


    @Getter
    @Setter
    @EqualsAndHashCode
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProductRequestDTO {
        @NotBlank(message = "product name shouldn't be NULL OR EMPTY")
        private String name;
        private String description;
        private String productType;
        private int quantity;
        private double price;
        private String supplierName;
        private String supplierCode;
        private List<String> images;

    }

    @Getter
    @Setter
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ProductResponseDTO extends ProductRequestDTO {
        private long id;
    }
}

