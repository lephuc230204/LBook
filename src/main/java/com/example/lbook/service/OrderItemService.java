package com.example.lbook.service;

import com.example.lbook.entity.Order;
import com.example.lbook.entity.OrderItem;
import com.example.lbook.entity.CartItem;

import java.util.List;

public interface OrderItemService {

    List<OrderItem> createOrderItems(List<CartItem> cartItems, Order order);
}
