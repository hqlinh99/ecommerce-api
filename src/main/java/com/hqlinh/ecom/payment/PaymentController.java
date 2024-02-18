package com.hqlinh.ecom.payment;

import com.hqlinh.ecom.core.APIResponse;
import com.hqlinh.ecom.order.*;
import com.hqlinh.ecom.util.DTOUtil;
import com.hqlinh.ecom.util.ValueMapper;
import jakarta.persistence.NoResultException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {
    private final PaymentConfig paymentConfig;
    private final PaymentService paymentService;
    private final OrderService orderService;
    private final IOrderRepository orderRepository;
    private static String clientUrlCallback;

    @SneakyThrows
    @GetMapping(value = "/vnpay-callback")
    public void paymentCallback(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> fields) {
        PrintWriter out = response.getWriter();
        try {

        /*  IPN URL: Record payment results from VNPAY
        Implementation steps:
        Check checksum
        Find transactions (vnp_TxnRef) in the database (checkOrderId)
        Check the payment status of transactions before updating (checkOrderStatus)
        Check the amount (vnp_Amount) of transactions before updating (checkAmount)
        Update results to Database
        Return recorded results to VNPAY
        */

            // ex:  	PaymnentStatus = 0; pending
            //              PaymnentStatus = 1; success
            //              PaymnentStatus = 2; Faile

            //Begin process return from VNPAY
            for (Enumeration params = request.getParameterNames(); params.hasMoreElements(); ) {
                String fieldName = URLEncoder.encode((String) params.nextElement(), StandardCharsets.US_ASCII.toString());
                String fieldValue = URLEncoder.encode(request.getParameter(fieldName), StandardCharsets.US_ASCII.toString());
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    fields.put(fieldName, fieldValue);
                }
            }

            String vnp_SecureHash = request.getParameter("vnp_SecureHash");
            if (fields.containsKey("vnp_SecureHashType")) {
                fields.remove("vnp_SecureHashType");
            }
            if (fields.containsKey("vnp_SecureHash")) {
                fields.remove("vnp_SecureHash");
            }

            // Check checksum
            String signValue = paymentConfig.hashAllFields(fields);
            if (signValue.equals(vnp_SecureHash)) {
                Order orderResult = orderRepository.findById(Long.parseLong(request.getParameter("vnp_TxnRef")))
                        .orElseThrow(() -> new NoResultException("Order not found with id " + request.getParameter("vnp_TxnRef")));
                //boolean checkOrderId = true // vnp_TxnRef exists in your database
                //boolean checkAmount = true; // vnp_Amount is valid (Check vnp_Amount VNPAY returns compared to the amount of the code (vnp_TxnRef) in the Your database).
                //boolean checkOrderStatus = true; // PaymnentStatus = 0 (pending)

                if (orderResult != null) {
                    if (orderResult.getTotalAmount() * 24000 * 100 == Long.parseLong(request.getParameter("vnp_Amount"))) {
                        if (orderResult.getPayment().getStatus().equals(PaymentStatus.PAYING)) {
                            if ("00".equals(request.getParameter("vnp_ResponseCode"))) {
                                orderResult.getPayment().setStatus(PaymentStatus.PAID);
                                orderResult.setStatus(OrderStatus.APPROVED);
                                orderRepository.save(DTOUtil.map(orderResult, Order.class));
                                response.sendRedirect("http://localhost:8888/admin/#!/profile/" + orderResult.getAccount().getId());
                                //Here Code update PaymnentStatus = 1 into your Database
                            } else {
                                orderRepository.deleteById(orderResult.getId());
                                response.sendRedirect("http://localhost:8888");
                                // Here Code update PaymnentStatus = 2 into your Database
                            }
                            out.print("{\"RspCode\":\"00\",\"Message\":\"Confirm Success\"}");
                        } else {
                            out.print("{\"RspCode\":\"02\",\"Message\":\"Order already confirmed\"}");
                        }
                    } else {
                        out.print("{\"RspCode\":\"04\",\"Message\":\"Invalid Amount\"}");
                    }
                } else {
                    out.print("{\"RspCode\":\"01\",\"Message\":\"Order not Found\"}");
                }
            } else {
                out.print("{\"RspCode\":\"97\",\"Message\":\"Invalid Checksum\"}");
            }
        } catch (Exception e) {
            out.print("{\"RspCode\":\"99\",\"Message\":\"Unknow error\"}");
        }
    }
}
