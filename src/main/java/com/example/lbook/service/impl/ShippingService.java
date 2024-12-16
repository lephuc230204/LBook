package com.example.lbook.service.impl;

import com.example.lbook.dto.rq.OrderForm;
import com.example.lbook.entity.CartItem;
import com.example.lbook.repository.CartItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ShippingService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private AddressService addressService;
    @Autowired
    private CartItemRepository cartItemRepository;

    private static final String BASE_URL = "https://online-gateway.ghn.vn/shiip/public-api/master-data";

    @Value("${shipping.api.ghn.token}")
    private String apiToken;

    @Value("${shipping.api.ghn.shopId}")
    private String shopId;

    public Long calculateShip(OrderForm form) {
        try {
            // Lấy `district_id` từ tên tỉnh và huyện
            int districtId = addressService.getDistrictIdByName(form.getProvince(), form.getDistrict());
            String wardCode = addressService.getWardCodeByName(form.getProvince(), form.getDistrict(),form.getWard() );


            // Tạo headers với Token và ShopId
            HttpHeaders headers = new HttpHeaders();
            headers.set("Token", apiToken);
            headers.set("ShopId", shopId);

            // Lấy danh sách các CartItem từ repository
            List<CartItem> cartItems = cartItemRepository.findAllByCartItemIdIn(form.getCartItemIds());

            log.info("Shipping calculation request headers: {}", headers);

            // Tạo body request
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("service_type_id", 2); // ID dịch vụ giao hàng
            requestBody.put("to_district_id", districtId); // ID huyện
            requestBody.put("to_ward_code", wardCode); // Mã phường/xã
            requestBody.put("weight", form.getWeight());

            // Tạo danh sách các items từ cartItems
            List<Map<String, Object>> items = cartItems.stream()
                    .map(cartItem -> {
                        Map<String, Object> itemMap = new HashMap<>();
                        itemMap.put("quantity", (Object) cartItem.getAmount()); // Ép kiểu vào Object
                        itemMap.put("weight", (Object) cartItem.getWeight());   // Ép kiểu vào Object
                        return itemMap;
                    })
                    .collect(Collectors.toList());
            // Thêm danh sách items vào requestBody
            requestBody.put("items", items);

            log.info("Shipping calculation request body: {}", requestBody);

            // Gửi request đến GHN API
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            String feeUrl = "https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee";

            ResponseEntity<Map> response = restTemplate.exchange(feeUrl, HttpMethod.POST, entity, Map.class);

            // Kiểm tra phản hồi từ API
            if (response.getBody() != null && response.getBody().get("data") != null) {
                Map<String, Object> data = (Map<String, Object>) response.getBody().get("data");
                return ((Number) data.get("total")).longValue(); // Lấy tổng phí từ response
            }
            throw new RuntimeException("Failed to calculate shipping fee");
        } catch (Exception e) {
            log.error("Error in calculateShip: {}", e.getMessage(), e);
            throw new RuntimeException("An error occurred while calculating shipping fee");
        }
    }

}