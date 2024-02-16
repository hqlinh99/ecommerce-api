package com.hqlinh.ecom.order;

import com.hqlinh.ecom.order_item.IOrderItemRepository;
import com.hqlinh.ecom.order_item.OrderItem;
import com.hqlinh.ecom.product.IProductRepository;
import com.hqlinh.ecom.util.DTOUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final IOrderRepository orderRepository;
    private final IOrderItemRepository orderItemRepository;
    private final IProductRepository productRepository;

    public OrderDTO.OrderResponseDTO create(OrderDTO.OrderRequestDTO orderRequestDTO) {
        OrderDTO.OrderResponseDTO orderResponseDTO;
        try {
            log.info("OrderService::create new order execution started...");

            //EXECUTE
            Order order = DTOUtil.map(orderRequestDTO, Order.class);
            Order orderResult = orderRepository.save(order);

            //UPDATE ORDER ITEMS
            orderResult.getOrderItems().forEach(orderItem -> {
                //SET ORDER TO THE ORDER ITEM AGAIN
                orderItem.setOrder(orderResult);
                //SET PRODUCT PRICE TO THE ORDER ITEM AGAIN
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

    public List<OrderDTO.OrderResponseDTO> getOrders() {
        List<OrderDTO.OrderResponseDTO> orderResponseDTOS;
        try {
            log.info("OrderService::getOrders execution started...");

            List<Order> orderList = orderRepository.findAll();
            orderResponseDTOS = orderList.isEmpty() ? Collections.emptyList() : DTOUtil.mapList(orderList, OrderDTO.OrderResponseDTO.class);
        } catch (OrderException.OrderServiceBusinessException ex) {
            log.error("Exception occurred while retrieving orders from database , Exception message {}", ex.getMessage());
            throw ex;
        }

        log.info("OrderService::getOrderById execution ended...");
        return orderResponseDTOS;
    }
//
//    public OrderDTO.OrderResponseDTO getOrderById(Long fileUploadId) {
//        OrderDTO.OrderResponseDTO fileUploadResponseDTO;
//        try {
//            log.info("OrderService::getOrderById execution started...");
//
//            Order fileUpload = orderRepository.findById(fileUploadId).orElseThrow(() -> new NoResultException("Order not found with id " + fileUploadId));
//            fileUploadResponseDTO = DTOUtil.map(fileUpload, OrderDTO.OrderResponseDTO.class);
//        } catch (OrderException.OrderServiceBusinessException ex) {
//            log.error("Exception occurred while retrieving fileUpload {} from database , Exception message {}", fileUploadId, ex.getMessage());
//            throw ex;
//        }
//
//        log.info("OrderService::getOrderById execution ended...");
//        return fileUploadResponseDTO;
//    }
//
//    //
//    public OrderDTO.OrderResponseDTO updateNameOrderById(Long fileUploadId, OrderDTO.NameOrderRequest fileName) {
//        OrderDTO.OrderResponseDTO fileUploadResponseDTO;
//        try {
//            log.info("OrderService::updateNameOrderById execution started...");
//
//            //CHECK EXISTED
//            Order existOrder = DTOUtil.map(getOrderById(fileUploadId), Order.class);
//
//            //EXECUTE
//            if (existOrder.getName().equals(fileName.getName()))
//                fileUploadResponseDTO = DTOUtil.map(existOrder, OrderDTO.OrderResponseDTO.class);
//            else {
//                //get old file name
//                String oldFileNameWithExtension = existOrder.getName() + existOrder.getExtension();
//
//                //check existed file name
//                String newFileName = createFileName(fileName.getName());
//                String newFileNameWithExtension = newFileName + existOrder.getExtension();
//
//                //update new file name in database
//                existOrder.setName(newFileName);
//                existOrder.setUrl(
//                        ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString()
//                                + PATH.replace("**", "") + newFileNameWithExtension);
//                Order fileUploadResult = orderRepository.save(existOrder);
//
//                //update file name in directory
//                File oldFile = new File(System.getProperty("user.dir") + UPLOAD_DIR + "/" + oldFileNameWithExtension);
//                File newFile = new File(System.getProperty("user.dir") + UPLOAD_DIR + "/" + newFileNameWithExtension);
//                if (!oldFile.renameTo(newFile))
//                    log.error("Update file name failed");
//
//                fileUploadResponseDTO = DTOUtil.map(fileUploadResult, OrderDTO.OrderResponseDTO.class);
//            }
//        } catch (OrderException.OrderServiceBusinessException ex) {
//            log.error("Exception occurred while persisting fileUpload to database, Exception message {}", ex.getMessage());
//            throw ex;
//        }
//
//        log.info("OrderService::updateNameOrderById execution ended...");
//        return fileUploadResponseDTO;
//    }
//
//    public void deleteOrderById(Long fileUploadId) {
//        try {
//            log.info("OrderService::deleteOrderById execution started...");
//
//            //CHECK EXIST
//            Order existOrder = DTOUtil.map(getOrderById(fileUploadId), Order.class);
//
//            //EXECUTE
//            orderRepository.delete(existOrder);
//            String fileNameWithExtension = existOrder.getName() + existOrder.getExtension();
//            File file = new File(System.getProperty("user.dir") + UPLOAD_DIR + "/" + fileNameWithExtension);
//            file.delete();
//        } catch (OrderException.OrderServiceBusinessException ex) {
//            log.error("Exception occurred while deleting fileUpload {} from database, Exception message {}", fileUploadId, ex.getMessage());
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
