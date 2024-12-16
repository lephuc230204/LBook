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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", nullable = false) // Ánh xạ với khóa ngoại address_id
    private Address address; // Địa chỉ giao hàng ProvineId, DistrictId, WardId

    private PaymentMedthodEnum paymentMedthod;
    public enum PaymentMedthodEnum {
        MOMO,BANK
    }

    private String shippingUnit;

    @Column(nullable = false)
    private String phone; // Số điện thoại nhận hàng

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Người dùng đặt hàng

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems; // Chi tiết các sản phẩm trong đơn hàng

    @Column(nullable = false)
    private LocalDate orderDate; // Ngày đặt hàng

    @Column(nullable = false)
    private String note; // Ghi chú của khách hàng

    @Column(nullable = false)
    private double totalBookPrice; // Tổng giá trị sản phẩm (chưa tính phí vận chuyển)

    @Column(nullable = false)
    private double shippingFee; // Phí vận chuyển

    @Column(nullable = false)
    private double totalPrice; // Tổng giá trị đơn hàng (bao gồm phí vận chuyển)
}
