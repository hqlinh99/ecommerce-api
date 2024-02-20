package com.hqlinh.ecom.order;

import com.hqlinh.ecom.account.Account;
import com.hqlinh.ecom.account.IAccountRepository;
import com.hqlinh.ecom.account.Role;
import com.hqlinh.ecom.order_item.IOrderItemRepository;
import com.hqlinh.ecom.payment.PaymentConfig;
import com.hqlinh.ecom.product.IProductRepository;
import com.hqlinh.ecom.util.DTOUtil;
import jakarta.persistence.NoResultException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final IOrderRepository orderRepository;
    private final IOrderItemRepository orderItemRepository;
    private final IProductRepository productRepository;
    private final IAccountRepository accountRepository;
    private final Environment environment;
    private final HttpServletRequest httpServletRequest;
    private final PaymentConfig paymentConfig;

    @SneakyThrows
    public OrderDTO.OrderResponseDTO create(OrderDTO.OrderRequestDTO orderRequestDTO) {
        OrderDTO.OrderResponseDTO orderResponseDTO;
        try {
            log.info("OrderService::create new order execution started...");

            //CHECK DUPLICATE PRODUCT IN THE ORDER
            long countDistinct = orderRequestDTO.getOrderItems()
                    .stream()
                    .mapToLong(orderItem -> orderItem.getProduct().getId())
                    .distinct()
                    .count();
            if (countDistinct != orderRequestDTO.getOrderItems().size())
                throw new IllegalStateException("duplicate product in the order request");

            //CHECK PRODUCT EXISTED
            orderRequestDTO.getOrderItems().forEach(orderItem -> {
                if (!productRepository.existsById(orderItem.getProduct().getId()))
                    throw new NoResultException("cannot find product with id " + orderItem.getProduct().getId());
            });

            //CHECK ACCOUNT EXISTED
            if (!accountRepository.existsById(orderRequestDTO.getAccount().getId()))
                throw new NoResultException("cannot find account with id " + orderRequestDTO.getAccount().getId());

            //EXECUTE
            //calculate total amount
            long totalAmount = orderRequestDTO.getOrderItems().stream().mapToLong(orderItem -> {
                long productPrice = productRepository.getPriceById(orderItem.getProduct().getId());
                return productPrice * orderItem.getQuantity();
            }).sum();

            Order order = DTOUtil.map(orderRequestDTO, Order.class);
            order.setTotalAmount(totalAmount);
            Order orderResult = orderRepository.saveAndFlush(order);

            //update order items
            orderResult.getOrderItems().stream().forEach(orderItem -> {
                //set order id to the order item again
                orderItem.setOrder(orderResult);
                //set price to the order item
                orderItem.setPrice(productRepository.getPriceById(orderItem.getProduct().getId()));
                orderItemRepository.save(orderItem);
            });

            orderResponseDTO = DTOUtil.map(orderResult, OrderDTO.OrderResponseDTO.class);
        } catch (OrderException.OrderServiceBusinessException ex) {
            log.error("Exception occurred while persisting a new order to database , Exception message {}", ex.getMessage());
            throw ex;
        }

        log.info("OrderService::create execution ended...");
        return orderResponseDTO;
    }

    @SneakyThrows
    public OrderDTO.OrderResponseDTO createWithVNPAY(OrderDTO.OrderRequestDTO orderRequestDTO) {
        OrderDTO.OrderResponseDTO orderResult = create(orderRequestDTO);
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", environment.getProperty("application.vnpay.terminal-id"));
        vnp_Params.put("vnp_Amount", String.valueOf(orderResult.getTotalAmount() * 24000 * 100));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", orderResult.getId().toString());
        vnp_Params.put("vnp_OrderType", "billpayment");
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString() + "/api/v1/vnpay-callback");
        vnp_Params.put("vnp_IpAddr", httpServletRequest.getRemoteAddr());

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        vnp_Params.put("vnp_CreateDate", formatter.format(cld.getTime()));
        vnp_Params.put("vnp_OrderInfo", "order= " + orderResult.getId() + ";payment= " + orderResult.getPayment().getId());
        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        //Add Params of 2.1.0 Version
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
        //Build data to hash and querystring
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = paymentConfig.hmacSHA512(Objects.requireNonNull(environment.getProperty("application.vnpay.secret-key")), hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        orderResult.getPayment().setUrlVNPAY("https://sandbox.vnpayment.vn/paymentv2/vpcpay.html?" + queryUrl);
        return orderResult;
    }

    public List<OrderDTO.OrderResponseDTO> getOrders() {
        List<OrderDTO.OrderResponseDTO> orderResponseDTOS;
        try {
            log.info("OrderService::getOrders execution started...");
            Account account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            List<Order> orderList = account.getRole().equals(Role.CUSTOMER)
                    ? orderRepository.findAllByAccountId(account.getId())
                    : orderRepository.findAll();
            orderResponseDTOS = orderList.isEmpty() ? Collections.emptyList() : DTOUtil.mapList(orderList, OrderDTO.OrderResponseDTO.class);
        } catch (OrderException.OrderServiceBusinessException ex) {
            log.error("Exception occurred while retrieving orders from database , Exception message {}", ex.getMessage());
            throw ex;
        }

        log.info("OrderService::getOrderById execution ended...");
        return orderResponseDTOS;
    }

    public List<OrderDTO.OrderResponseDTO> getOrdersByStatus(OrderStatus orderStatus) {
        List<OrderDTO.OrderResponseDTO> orderResponseDTOS;
        try {
            log.info("OrderService::getOrders execution started...");
            Account account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            List<Order> orderList = account.getRole().equals(Role.CUSTOMER)
                    ? orderRepository.findAllByAccountIdAndStatus(account.getId(), orderStatus)
                    : orderRepository.findByStatus(orderStatus);
            orderResponseDTOS = orderList.isEmpty() ? Collections.emptyList() : DTOUtil.mapList(orderList, OrderDTO.OrderResponseDTO.class);
        } catch (OrderException.OrderServiceBusinessException ex) {
            log.error("Exception occurred while retrieving orders from database , Exception message {}", ex.getMessage());
            throw ex;
        }

        log.info("OrderService::getOrderById execution ended...");
        return orderResponseDTOS;
    }

    public OrderDTO.OrderResponseDTO getOrderById(Long orderId) {
        OrderDTO.OrderResponseDTO orderResponseDTO;
        try {
            log.info("OrderService::getOrderById execution started...");
            Account account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            Order order = account.getRole().equals(Role.CUSTOMER)
                    ? orderRepository.findByIdAndAccountId(orderId, account.getId()).orElseThrow(() -> new NoResultException("Order not found with id " + orderId))
                    : orderRepository.findById(orderId).orElseThrow(() -> new NoResultException("Order not found with id " + orderId));
            orderResponseDTO = DTOUtil.map(order, OrderDTO.OrderResponseDTO.class);
        } catch (OrderException.OrderServiceBusinessException ex) {
            log.error("Exception occurred while retrieving order {} from database , Exception message {}", orderId, ex.getMessage());
            throw ex;
        }

        log.info("OrderService::getOrderById execution ended...");
        return orderResponseDTO;
    }

    public OrderDTO.OrderResponseDTO updateStatusOrderById(Long orderId, OrderDTO.OrderStatusRequestDTO orderStatusRequestDTO) {
        OrderDTO.OrderResponseDTO orderResponseDTO;
        try {
            log.info("OrderService::updateStatusOrderById execution started...");

            //CHECK EXISTED
            Order existOrder = DTOUtil.map(getOrderById(orderId), Order.class);

            //EXECUTE
            existOrder.setStatus(orderStatusRequestDTO.getStatus());
            existOrder.setUpdatedAt(orderStatusRequestDTO.getUpdatedAt());
            Order orderResult = orderRepository.save(existOrder);

            orderResponseDTO = DTOUtil.map(orderResult, OrderDTO.OrderResponseDTO.class);
        } catch (OrderException.OrderServiceBusinessException ex) {
            log.error("Exception occurred while persisting order to database, Exception message {}", ex.getMessage());
            throw ex;
        }

        log.info("OrderService::updateStatusOrderById execution ended...");
        return orderResponseDTO;
    }
//
//    public void deleteOrderById(Long orderId) {
//        try {
//            log.info("OrderService::deleteOrderById execution started...");
//
//            //CHECK EXIST
//            Order existOrder = DTOUtil.map(getOrderById(orderId), Order.class);
//
//            //EXECUTE
//            orderRepository.delete(existOrder);
//            String fileNameWithExtension = existOrder.getName() + existOrder.getExtension();
//            File file = new File(System.getProperty("user.dir") + UPLOAD_DIR + "/" + fileNameWithExtension);
//            file.delete();
//        } catch (OrderException.OrderServiceBusinessException ex) {
//            log.error("Exception occurred while deleting order {} from database, Exception message {}", orderId, ex.getMessage());
//            throw ex;
//        }
//
//        log.info("OrderService::deleteOrderById execution ended...");
//    }
//
//    private String createFileName(String fileName) {
//        int i = 1;
//        StringBuilder newFileName = new StringBuilder(fileName);
//        while (orderRepository.existsByName(newFileName.toString())) {
//            newFileName.setLength(0);
//            newFileName.append(fileName).append("-").append(i++);
//        }
//        return newFileName.toString();
//    }
}
