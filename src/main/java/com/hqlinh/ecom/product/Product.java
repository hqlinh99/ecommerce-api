package com.hqlinh.ecom.product;

import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "description", columnDefinition = "text")
    private String description;
    @Column(name = "product_type")
    private String productType;
    @Column(name = "quantity")
    private Integer quantity;
    @Column(name = "price")
    private Long price;

    @Type(JsonType.class)
    @Column(name = "images", columnDefinition = "json")
    private ArrayList<String> images;

    @Column(name = "created_at")
    private Number createdAt;
    @Column(name = "updated_at")
    private Number updatedAt;
}
