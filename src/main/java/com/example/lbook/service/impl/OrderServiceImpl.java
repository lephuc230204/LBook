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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

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

    @Autowired
    private RestTemplate restTemplate; // Đảm bảo RestTemplate đã được cấu hình

    @Value("${map.google.key}")
    private String googleMapsApiKey; // Đọc từ application.properties
    private static final int FROM_DISTRICT_ID = 1442; // Mã quận 1
    private static final String FROM_WARD_CODE = "1"; // Mã phường 1


    // Phương thức lấy quận và phường từ Google Maps API
    private String getDistrictAndWard(String address) {
        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + address + "&key=" + googleMapsApiKey;
        String response = restTemplate.getForObject(url, String.class);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(response);
            JsonNode results = jsonResponse.get("results");

            if (results != null && results.size() > 0) {
                JsonNode addressComponents = results.get(0).get("address_components");

                for (JsonNode component : addressComponents) {
                    String longName = component.get("long_name").asText();
                    JsonNode types = component.get("types");

                    if (types != null) {
                        for (JsonNode type : types) {
                            if ("administrative_area_level_2".equals(type.asText())) {
                                return longName; // Giả sử long_name là tên quận
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error parsing Google Maps response: " + e.getMessage());
        }

        return null; // Nếu không tìm thấy quận
    }

    @Override
    @Transactional
    public ResponseData<OrderDto> createOrder(OrderForm form) {
        // Lấy thông tin người dùng từ authentication
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            log.error("User not found");
            return new ResponseError<>(400, "User not found");
        }

        // Lấy danh sách CartItem từ cartItemIds
        List<CartItem> cartItems = cartItemRepository.findAllByCartItemIdIn(form.getCartItemIds());
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

        order = orderRepository.save(order);

        // Tạo OrderItems từ CartItems
        List<OrderItem> orderItems = orderItemService.createOrderItems(cartItems, order);
        order.setOrderItems(orderItems);

        for (OrderItem orderItem : orderItems) {
            totalBookPrice += orderItem.getPrice();
        }

        // Tìm quận và phường từ địa chỉ người dùng
        String districtAndWard = getDistrictAndWard(form.getAddress()); // Lấy thông tin quận và phường từ địa chỉ người dùng

        // Tính phí vận chuyển từ quận 1 (quán) đến địa chỉ khách hàng
        int toDistrictId = getDistrictIdFromString(districtAndWard); // Giả sử bạn có hàm này để lấy mã quận từ tên quận
        String toWardCode = getWardCodeFromString(districtAndWard); // Giả sử bạn có hàm này để lấy mã phường

        // Tính phí vận chuyển
        double shippingFee = shippingService.calculateShippingFee(
                FROM_DISTRICT_ID, FROM_WARD_CODE,
                toDistrictId, toWardCode, 1.0, 1.0, 1.0, 1.0, 1);

        order.setTotalBookPrice(totalBookPrice);
        order.setShippingFee(shippingFee);
        order.setTotalPrice(totalBookPrice + shippingFee);

        // Save the updated Order with OrderItems
        orderRepository.save(order);

        return new ResponseData<>(200, "Order created successfully", OrderDto.fromEntity(order));
    }

    // Các phương thức giả định để lấy mã quận và phường từ tên
    private int getDistrictIdFromString(String district) {
        // Bạn có thể tạo logic để ánh xạ quận và phường
        return 1442; // Ví dụ: mã quận 1
    }

    private String getWardCodeFromString(String district) {
        // Ánh xạ mã phường từ quận
        return "1"; // Ví dụ: mã phường
    }
}
