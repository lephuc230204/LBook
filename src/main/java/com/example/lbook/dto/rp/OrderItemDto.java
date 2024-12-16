package com.example.lbook.dto.rp;

import com.example.lbook.entity.OrderItem;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemDto {
    private Long orderItemId;
    private Long orderId;
    private String bookName;
    private Long quantity;
    private double price;

    public static OrderItemDto fromEntity(OrderItem orderItem) {
        return OrderItemDto.builder()
                .orderItemId(orderItem.getOrderItemId())
                .orderId(orderItem.getOrder().getOrderId())
                .bookName(orderItem.getBook().getBookName())  // Assuming 'name' is the book name field
                .quantity(orderItem.getQuantity())
                .price(orderItem.getPrice())
                .build();
    }
}
