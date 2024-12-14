package com.example.lbook.dto.rp;

import com.example.lbook.entity.Order;
import com.example.lbook.entity.OrderItem;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Builder
@Data
public class OrderDto {
    private Long orderId;
    private String address;
    private String phone;
    private LocalDate orderDate;
    private Order.ShippingUnit shippingUnit;
    private Order.PaymentMethod paymentMethod;
    private String note;
    private List<OrderItem> orderItems;
    private double totalBookPrice; // Tổng giá sách
    private double shippingFee; // Phí vận chuyển
    private double totalPrice;

    public static OrderDto fromEntity(Order order) {
        return OrderDto.builder()
                .orderId(order.getOrderId())
                .address(order.getAddress())
                .phone(order.getPhone())
                .orderDate(order.getOrderDate())
                .shippingUnit(order.getShippingUnit())
                .paymentMethod(order.getPaymentMethod())
                .note(order.getNote())
                .orderItems(order.getOrderItems())
                .totalBookPrice(order.getTotalBookPrice())
                .shippingFee(order.getShippingFee())
                .totalPrice(order.getTotalPrice())

                .build();
    }
}
