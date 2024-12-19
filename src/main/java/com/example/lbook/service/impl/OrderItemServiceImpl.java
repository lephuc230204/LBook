package com.example.lbook.service.impl;

import com.example.lbook.dto.rp.ResponseData;
import com.example.lbook.dto.rp.ResponseError;
import com.example.lbook.entity.Order;
import com.example.lbook.entity.OrderItem;
import com.example.lbook.entity.Book;
import com.example.lbook.entity.CartItem;
import com.example.lbook.repository.BookRepository;
import com.example.lbook.repository.OrderItemRepository;
import com.example.lbook.service.OrderItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class OrderItemServiceImpl implements OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private BookRepository bookRepository;

    @Override
    public List<OrderItem> createOrderItems(List<CartItem> cartItems, Order order) {
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            Book book = cartItem.getBook();
            if(cartItem.getAmount() > book.getCurrentQuantity()){
                log.error("The book quantity is not enough");
                throw new IllegalArgumentException(
                        "The book \"" + book.getBookName() + "\" does not have enough quantity. Available: "
                                + book.getCurrentQuantity() + ", Requested: " + cartItem.getAmount()
                );
            }
            book.setCurrentQuantity(book.getCurrentQuantity() - cartItem.getAmount());
            bookRepository.save(book);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setBook(book);
            orderItem.setQuantity(cartItem.getAmount());
            orderItem.setPrice(book.getPrice() * cartItem.getAmount());
            orderItems.add(orderItem);
        }

        return orderItems;
    }
}
