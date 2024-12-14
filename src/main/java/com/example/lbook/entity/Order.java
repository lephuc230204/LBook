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

//    private Voucher voucher;

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
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;

    private double totalBookPrice; // Tổng giá sách
    private double shippingFee; // Phí vận chuyển
    private double totalPrice;

}
