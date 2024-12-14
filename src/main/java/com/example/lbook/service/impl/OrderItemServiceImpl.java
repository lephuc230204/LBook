package com.example.lbook.service.impl;

import com.example.lbook.entity.Order;
import com.example.lbook.entity.OrderItem;
import com.example.lbook.entity.Book;
import com.example.lbook.entity.CartItem;
import com.example.lbook.repository.OrderItemRepository;
import com.example.lbook.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Override
    public List<OrderItem> createOrderItems(List<CartItem> cartItems, Order order) {
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            Book book = cartItem.getBook();
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setBook(book);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(book.getPrice() * cartItem.getQuantity());
            orderItems.add(orderItem);
        }

        orderItemRepository.saveAll(orderItems);

        return orderItems;
    }
}
