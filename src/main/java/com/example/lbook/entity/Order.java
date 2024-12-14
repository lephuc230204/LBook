package com.example.lbook.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    private String address;
    private String phone;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

//    private Voucher voucher;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;

    private LocalDate orderDate;

    @Enumerated(EnumType.STRING)
    private ShippingUnit shippingUnit;
    public enum ShippingUnit {
        GHTK, GHN, VIETTEL_POST
    }

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    public enum PaymentMethod {
        VNPAY, BANK, MOMO
    }

    private String note;
    private double totalBookPrice;
    private double shippingFee;
    private double totalPrice;

}
