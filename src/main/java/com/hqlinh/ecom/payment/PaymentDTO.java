package com.hqlinh.ecom.payment;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public abstract class PaymentDTO {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PaymentRequestDTO {
        private Long totalAmount;
        private String clientUrlCallback;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PaymentResponseDTO {
        private String urlVNPAY;
    }
}
