package com.example.lbook.dto.rp;

import com.example.lbook.entity.OrderItem;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemDto {
    private Long orderItemId;
    private String bookName;
    private int height;
    private int weight;
    private int length;
    private int width;
    private Long quantity;
    private double price;

    public static OrderItemDto fromEntity(OrderItem orderItem) {
        return OrderItemDto.builder()
                .orderItemId(orderItem.getOrderItemId())
                .bookName(orderItem.getBook().getBookName())
                .length(orderItem.getBook().getLength())
                .height(orderItem.getBook().getHeight())
                .weight(orderItem.getBook().getWeight())
                .width(orderItem.getBook().getWidth())
                .quantity(orderItem.getQuantity())
                .price(orderItem.getPrice())
                .build();
    }
}
