package com.hqlinh.ecom.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IProductRepository extends JpaRepository<Product, Long> {
    @Query("select e.price from Product e where e.id = ?1")
    Long getPriceById(Long id);
}
