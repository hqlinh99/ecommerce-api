package com.hqlinh.ecom.order;

import com.hqlinh.ecom.core.APIResponse;
import com.hqlinh.ecom.util.ValidationUtil;
import com.hqlinh.ecom.util.ValueMapper;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    private final OrderService orderService;
    private final Validator validator;

    @PostMapping(value = "/order")
    public ResponseEntity<?> createNewOrder(@RequestBody OrderDTO.OrderRequestDTO orderRequestDTO) {
        //Validate
        ValidationUtil.validate(orderRequestDTO, OrderDTO.class);

        log.info("OrderController::createNewOrder request body: {}", ValueMapper.jsonAsString(orderRequestDTO));
        OrderDTO.OrderResponseDTO orderResponseDTO = orderService.create(orderRequestDTO);
        APIResponse<?> response = APIResponse
                .builder()
                .status("SUCCESS")
                .result(orderResponseDTO)
                .build();
        log.info("OrderController::createNewOrder response: {}", ValueMapper.jsonAsString(response));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping(value = "/orders")
    public ResponseEntity<?> getOrders() {
        List<OrderDTO.OrderResponseDTO> orderResponseDTOS = orderService.getOrders();
        APIResponse<?> response = APIResponse
                .builder()
                .status("SUCCESS")
                .result(orderResponseDTOS)
                .build();
        log.info("OrderController::getOrders response: {}", ValueMapper.jsonAsString(response));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

//    @GetMapping(value = "/order/{orderId}")
//    public ResponseEntity<?> getOrderById(@PathVariable Long orderId) {
//        log.info("OrderController::getOrderById is {}", orderId);
//        OrderDTO.OrderResponseDTO productResponseDTO = orderService.getOrderById(orderId);
//        APIResponse<?> response = APIResponse
//                .builder()
//                .status("SUCCESS")
//                .result(productResponseDTO)
//                .build();
//        log.info("OrderController::getOrderById response: {}", ValueMapper.jsonAsString(response));
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
////
//    @PatchMapping(value = "/order/{orderId}")
//    public ResponseEntity<?> updateNameOrderById(@PathVariable Long orderId, @RequestBody OrderDTO.NameOrderRequest fileName) throws MethodArgumentNotValidException {
//        //Validate
//        ValidationUtil.validate(fileName, OrderDTO.class);
//
//        log.info("OrderController::updateNameOrderById is {}", orderId);
//        OrderDTO.OrderResponseDTO productResponseDTO = orderService.updateNameOrderById(orderId, fileName);
//        APIResponse<?> response = APIResponse
//                .builder()
//                .status("SUCCESS")
//                .result(productResponseDTO)
//                .build();
//        log.info("OrderController::updateNameOrderById response: {}", ValueMapper.jsonAsString(response));
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
//    @DeleteMapping(value = "/order/{orderId}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void deleteOrderById(@PathVariable Long orderId) {
//        log.info("OrderController::deleteOrderById is {}", orderId);
//        orderService.deleteOrderById(orderId);
//        log.info("OrderController::deleteOrderById is ended successfully");
//    }
}
