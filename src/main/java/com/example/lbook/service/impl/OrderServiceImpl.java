package com.example.lbook.service.impl;

import com.example.lbook.dto.rp.OrderDto;
import com.example.lbook.dto.rp.ResponseData;
import com.example.lbook.dto.rq.OrderForm;
import com.example.lbook.entity.Order;
import com.example.lbook.entity.CartItem;
import com.example.lbook.entity.OrderItem;
import com.example.lbook.repository.OrderRepository;
import com.example.lbook.repository.BookRepository;
import com.example.lbook.service.OrderService;
import com.example.lbook.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ShippingService shippingService;

    @Autowired
    private OrderItemService orderItemService;

    @Override
    @Transactional
    public ResponseData<OrderDto> createOrder(OrderForm form, List<CartItem> cartItems) {
        Order order = new Order();
        order.setAddress(form.getAddress());
        order.setPhone(form.getPhone());
        order.setNote(form.getNote());
        order.setShippingUnit(form.getShippingUnit());
        order.setPaymentMethod(form.getPaymentMethod());
        order.setOrderDate(LocalDate.now());

        double totalBookPrice = 0;

        List<OrderItem> orderItems = orderItemService.createOrderItems(cartItems, order);
        order.setOrderItems(orderItems);

        for (OrderItem orderItem : orderItems) {
            totalBookPrice += orderItem.getPrice();
        }

        double shippingFee = shippingService.calculateShippingFee(form.getShippingUnit().name(), form.getAddress(), 1.0);

        order.setTotalBookPrice(totalBookPrice);
        order.setShippingFee(shippingFee);
        order.setTotalPrice(totalBookPrice + shippingFee);

        // Save the order to the repository
        orderRepository.save(order);
        return new ResponseData<>(200, "Order created successfully", OrderDto.fromEntity(order));
    }
}
