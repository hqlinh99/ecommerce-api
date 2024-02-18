package com.hqlinh.ecom.payment;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static javax.xml.crypto.dsig.SignatureMethod.HMAC_SHA512;

@Component
public class PaymentConfig {
    @Value("${application.vnpay.secret-key}")
    private String vnpaySecretKey;

    @SneakyThrows
    public String hashAllFields(Map fields) {
        List fieldNames = new ArrayList(fields.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) fields.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(fieldValue);
                if (itr.hasNext()) hashData.append('&');
            }
        }
        return hmacSHA512(vnpaySecretKey, hashData.toString());
    }

    @SneakyThrows
    public String hmacSHA512(String vnpaySecretKey, String hashData) {
        Mac hmacSHA512 = Mac.getInstance("HmacSHA512");
        SecretKeySpec secretKey = new SecretKeySpec(vnpaySecretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
        hmacSHA512.init(secretKey);
        byte[] hash = hmacSHA512.doFinal(hashData.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hash);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xFF & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
