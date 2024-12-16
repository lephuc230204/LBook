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
    private String numberHouse;
    private String phone;
    private LocalDate orderDate;
    private String shippingUnit;
    private Order.PaymentMedthodEnum paymentMethod;
    private String note;
    private List<Map<String, Object>> orderItems;  // Thay List<OrderItem> bằng List<Map<String, Object>>
    private double totalBookPrice;
    private double shippingFee;
    private double totalPrice;

    public static OrderDto fromEntity(Order order) {
        // Chuyển đổi OrderItem thành List<Map<String, Object>>
        List<Map<String, Object>> orderItemsMap = order.getOrderItems().stream()
                .map(orderItem -> {
                    Map<String, Object> map = Map.of(
                            "bookId", orderItem.getBook().getBookId(),
                            "bookTitle", orderItem.getBook().getBookName(),
                            "quantity", orderItem.getQuantity(),
                            "price", orderItem.getPrice()
                    );
                    return map;
                })
                .collect(Collectors.toList());

        return OrderDto.builder()
                .orderId(order.getOrderId())
                .provinceId(order.getAddress().getProvinceId())
                .districtId(order.getAddress().getDistrictId())
                .wardId(order.getAddress().getWardId())
                .numberHouse(order.getAddress().getNumberHouse())
                .phone(order.getPhone())
                .orderDate(order.getOrderDate())
                .shippingUnit(order.getShippingUnit())
                .paymentMethod(order.getPaymentMedthod())
                .note(order.getNote())
                .orderItems(orderItemsMap)  // Gán danh sách Map vào orderItems
                .totalBookPrice(order.getTotalBookPrice())
                .shippingFee(order.getShippingFee())
                .totalPrice(order.getTotalPrice())
                .build();
    }
}
