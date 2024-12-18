package com.example.lbook.service.impl;


import com.example.lbook.dto.rp.OrderDto;
import com.example.lbook.dto.rp.ResponseData;
import com.example.lbook.dto.rp.ResponseError;
import com.example.lbook.dto.rq.OrderForm;
import com.example.lbook.entity.*;
import com.example.lbook.repository.*;
import com.example.lbook.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class OrderServiceimpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartItemRepository cartItemRepository ;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private ShipFeeServiceImpl shipFeeServiceImpl;
    @Autowired
    private OrderItemRepository orderItemRepository;

    @Override
    public ResponseData<OrderDto> createOrder(OrderForm form, List<Long> cartItemIds) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            log.error("User not found");
            return new ResponseError<>(400, "User not found");
        }

        if (cartItemIds == null || cartItemIds.isEmpty()) {
            log.error("Cart items are empty");
            return new ResponseError<>(400, "Cart items cannot be empty");
        }

        List<CartItem> cartItems = cartItemRepository.findAllByCartItemIdIn(cartItemIds);
        if (cartItems.isEmpty()) {
            log.error("No valid CartItems found");
            return new ResponseError<>(400, "No valid CartItems found");
        }
        Address address = addressRepository.findById(form.getAddressId()).orElse(null);
        if (address == null) {
            log.error("Address not found");
            return new ResponseError<>(400, "Address not found");
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDate.now());
        order.setPhone(form.getPhone());
        order.setPaymentMedthod(form.getPaymentMethod());
        order.setShippingUnit(form.getShippingUnit());
        order.setNote(form.getNote());

//        Address address = addressRepository.findByDistrictIdAndProvinceIdAndWardIdAndFullAddress(
//                form.getDistrictId(), form.getProvinceId(), form.getWardId(), form.getFullAddress()).orElse(null);
//
//        if (address == null) {
//            address = new Address();
//            address.setUser(user);
//            address.setDistrictId(form.getDistrictId());
//            address.setProvinceId(form.getProvinceId());
//            address.setWardId(form.getWardId());
//            address.setFullAddress(form.getFullAddress());
//            addressRepository.save(address);
//        }
        order.setAddress(address);
        double totalBookPrice = cartItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getAmount())
                .sum();

        List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setBook(cartItem.getBook());
            orderItem.setQuantity(cartItem.getAmount());
            orderItem.setPrice(cartItem.getBook().getPrice());
            orderItem.setOrder(order);
            return orderItem;
        }).toList();

        double shippingFee = shipFeeServiceImpl.calculateShipFee(orderItems, address);

        order.setTotalBookPrice(totalBookPrice);
        order.setShippingFee(shippingFee);
        order.setTotalPrice(totalBookPrice + shippingFee);
        order.setOrderItems(orderItems);
        orderRepository.save(order);
        orderItemRepository.saveAll(orderItems);
        return new ResponseData<>(200, "Success", OrderDto.fromEntity(order));
    }



}


