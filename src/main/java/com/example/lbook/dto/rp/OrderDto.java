package com.example.lbook.dto.rp;

import com.example.lbook.entity.Order;
import com.example.lbook.entity.OrderItem;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Builder
@Data
public class OrderDto {
    private Long orderId;
    private int provinceId;
    private int districtId;
    private String wardId;
    private String fullAddress;
    private String phone;
    private LocalDate orderDate;
    private String shippingUnit;
    private Order.PaymentMedthodEnum paymentMethod;
    private String note;
    private List<OrderItemDto> orderItems;
    private double totalBookPrice;
    private double shippingFee;
    private double discount;
    private double totalPrice;

    public static OrderDto fromEntity(Order order) {
        return OrderDto.builder()
                .orderId(order.getOrderId())
                .provinceId(order.getAddress().getProvinceId())
                .districtId(order.getAddress().getDistrictId())
                .wardId(order.getAddress().getWardId())
                .fullAddress(order.getAddress().getFullAddress())
                .phone(order.getPhone())
                .orderDate(order.getOrderDate())
                .shippingUnit(order.getShippingUnit())
                .paymentMethod(order.getPaymentMedthod())
                .note(order.getNote())
                .orderItems(order.getOrderItems().stream()
                        .map(OrderItemDto::fromEntity)
                        .collect(Collectors.toList()))
                .totalBookPrice(order.getTotalBookPrice())
                .shippingFee(order.getShippingFee())
                .discount(order.getTotalBookPrice()+order.getShippingFee()-order.getTotalPrice())
                .totalPrice(order.getTotalPrice())
                .build();
    }
}
