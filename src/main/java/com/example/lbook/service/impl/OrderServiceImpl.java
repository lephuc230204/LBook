package com.example.lbook.service.impl;

import com.example.lbook.dto.rp.OrderDto;
import com.example.lbook.dto.rp.ResponseData;
import com.example.lbook.dto.rp.ResponseError;
import com.example.lbook.dto.rq.OrderForm;
import com.example.lbook.entity.Order;
import com.example.lbook.entity.CartItem;
import com.example.lbook.entity.OrderItem;
import com.example.lbook.entity.User;
import com.example.lbook.repository.CartItemRepository;
import com.example.lbook.repository.OrderRepository;
import com.example.lbook.repository.BookRepository;
import com.example.lbook.repository.UserRepository;
import com.example.lbook.service.OrderService;
import com.example.lbook.service.OrderItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ShippingService shippingService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    @Transactional
    public ResponseData<OrderDto> createOrder(OrderForm form) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            log.error("User not found");
            return new ResponseError<>(400, "User not found");
        }

        // Lấy danh sách CartItem từ cartItemIds
        List<CartItem> cartItems = cartItemRepository.findAllById(form.getCartItemIds());
        if (cartItems.isEmpty()) {
            return new ResponseError<>(400, "Invalid cart items");
        }

        Order order = new Order();
        order.setUser(user);
        order.setAddress(form.getAddress());
        order.setPhone(form.getPhone());
        order.setNote(form.getNote());
        order.setShippingUnit(form.getShippingUnit());
        order.setPaymentMethod(form.getPaymentMethod());
        order.setOrderDate(LocalDate.now());

        double totalBookPrice = 0;

        // Tạo OrderItems từ CartItems
        List<OrderItem> orderItems = orderItemService.createOrderItems(cartItems, order);
        order.setOrderItems(orderItems);

        for (OrderItem orderItem : orderItems) {
            totalBookPrice += orderItem.getPrice();
        }

        double shippingFee = shippingService.calculateShippingFee(form.getShippingUnit().toString(), form.getAddress(), 1.0);

        order.setTotalBookPrice(totalBookPrice);
        order.setShippingFee(shippingFee);
        order.setTotalPrice(totalBookPrice + shippingFee);

        // Lưu Order vào database
        orderRepository.save(order);

        return new ResponseData<>(200, "Order created successfully", OrderDto.fromEntity(order));
    }

}
