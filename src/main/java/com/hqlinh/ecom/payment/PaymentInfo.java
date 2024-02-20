package com.hqlinh.ecom.payment;

import com.hqlinh.ecom.payment.PaymentMethod;
import com.hqlinh.ecom.payment.PaymentStatus;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;

@Getter
@Setter
public class PaymentInfo {
    private String id = RandomStringUtils.randomAlphabetic(6).toLowerCase();
    private PaymentMethod method = PaymentMethod.CASH;
    private PaymentStatus status = PaymentStatus.PAYING;
    private String urlVNPAY;
}
