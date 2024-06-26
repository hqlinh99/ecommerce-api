package com.hqlinh.ecom.order;

import com.hqlinh.ecom.account.Account;
import com.hqlinh.ecom.order_item.OrderItem;
import com.hqlinh.ecom.payment.PaymentInfo;
import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "total_amount")
    private Long totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status = OrderStatus.PENDING;

    @Column(name = "note")
    private String note;

    @Type(JsonType.class)
    @Column(name = "payment", columnDefinition = "json")
    private PaymentInfo payment;

    @Column(name = "created_at")
    private Number createdAt;
    @Column(name = "updated_at")
    private Number updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private Set<OrderItem> orderItems;
}
