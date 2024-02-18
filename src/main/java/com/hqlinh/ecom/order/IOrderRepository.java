package com.hqlinh.ecom.order;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IOrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatus(OrderStatus status);

    List<Order> findAllByAccountId(Long accountId);

    List<Order> findAllByAccountIdAndStatus(Long accountId, OrderStatus status);

    Optional<Order> findByIdAndAccountId(Long id, Long accountId);
}
