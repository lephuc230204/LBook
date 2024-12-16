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
        // Lấy user đang đăng nhập
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            log.error("User not found");
            return new ResponseError<>(400, "User not found");
        }

        // Kiểm tra cartItemIds
        if (cartItemIds == null || cartItemIds.isEmpty()) {
            log.error("Cart items are empty");
            return new ResponseError<>(400, "Cart items cannot be empty");
        }

        // Lấy danh sách CartItems từ cartItemIds
        List<CartItem> cartItems = cartItemRepository.findAllByCartItemIdIn(cartItemIds);
        if (cartItems.isEmpty()) {
            log.error("No valid CartItems found");
            return new ResponseError<>(400, "No valid CartItems found");
        }

        // Tạo Order
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDate.now());
        order.setPhone(form.getPhone());
        order.setPaymentMedthod(form.getPaymentMethod());
        order.setShippingUnit(form.getShippingUnit());
        order.setNote(form.getNote());

        // Tạo hoặc tìm Address
        Address address = addressRepository.findByDistrictIdAndProvinceIdAndWardIdAndNumberHouse(
                form.getDistrictId(), form.getProvinceId(), form.getWardId(), form.getNumberHouse()).orElse(null);

        if (address == null) {
            address = new Address();
            address.setDistrictId(form.getDistrictId());
            address.setProvinceId(form.getProvinceId());
            address.setWardId(form.getWardId());
            address.setNumberHouse(form.getNumberHouse());
            addressRepository.save(address);
        }

        // Liên kết địa chỉ với đơn hàng
        order.setAddress(address);

        // Tính toán tổng giá trị đơn hàng
        double totalBookPrice = cartItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getAmount())
                .sum();


        // Chuyển đổi CartItem thành OrderItem và liên kết với Order
        List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setBook(cartItem.getBook());
            orderItem.setQuantity(cartItem.getAmount());
            orderItem.setPrice(cartItem.getBook().getPrice());
            orderItem.setOrder(order); // Set the order reference here
            return orderItem;
        }).toList();

        double shippingFee = shipFeeServiceImpl.calculateShipFee(orderItems, address);

        order.setTotalBookPrice(totalBookPrice);
        order.setShippingFee(shippingFee);
        order.setTotalPrice(totalBookPrice + shippingFee);

        // Lưu Order trước
        orderRepository.save(order);


        // Lưu các OrderItem
        orderItemRepository.saveAll(orderItems);

        return new ResponseData<>(200, "Success", OrderDto.fromEntity(order));
    }



}


