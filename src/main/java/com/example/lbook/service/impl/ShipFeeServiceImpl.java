package com.example.lbook.service.impl;

import com.example.lbook.entity.Address;
import com.example.lbook.entity.OrderItem;
import com.example.lbook.service.ShipFeeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ShipFeeServiceImpl implements ShipFeeService {

    private static final String GHN_API_URL = "https://online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee";

    @Value("${ghn.token}")
    private String GHN_API_TOKEN;

    @Value("${ghn.shopId}")
    private String GHN_API_SHOP_ID;

    @Override
    public int calculateShipFee(List<OrderItem> orderItems, Address address) {
        String payload = buildGhnRequestPayload(orderItems, address);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Token", GHN_API_TOKEN);
        headers.set("ShopId", GHN_API_SHOP_ID);

        HttpEntity<String> request = new HttpEntity<>(payload, headers);

        ResponseEntity<Map> response = restTemplate.exchange(GHN_API_URL, HttpMethod.POST, request, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            Map<String, Object> data = (Map<String, Object>) response.getBody().get("data");
            return (int) data.get("total");
        } else {
            if (response.getBody() != null) {
                Map<String, Object> errorResponse = (Map<String, Object>) response.getBody();
                String errorMessage = (String) errorResponse.get("message");
                String errorCode = (String) errorResponse.get("code_message");

                log.error("Error response from GHN API: Status Code: {} | Message: {} | Code Message: {}",
                        response.getStatusCode(), errorMessage, errorCode);
            } else {
                log.error("Error response from GHN API: Status Code: {} | No response body", response.getStatusCode());
            }
            throw new RuntimeException("Error while calculating shipping fee.");
        }
    }


    private String buildGhnRequestPayload(List<OrderItem> orderItems, Address address) {
        Map<String, Object> payload = new HashMap<>();
        payload.put( "service_type_id", 2);
        payload.put("to_district_id", address.getDistrictId());
        payload.put("to_ward_code", address.getWardId());
        payload.put("height", calculateTotalHeight(orderItems));
        payload.put("length", checkMaxLength(orderItems));
        payload.put("weight", calculateTotalWeight(orderItems));
        payload.put("width", calculateTotalLWidth(orderItems));
        payload.put("insurance_value", totalPrice(orderItems));
        payload.put("cod_failed_amount", 2000);
        payload.put("coupon", null);

        List<Map<String, Object>> items = orderItems.stream()
                .map(item -> {
            Map<String, Object> bookItem = new HashMap<>();
            bookItem.put("name", item.getBook().getBookName());
            bookItem.put("quantity", item.getQuantity());
            bookItem.put("height", item.getBook().getHeight());
            bookItem.put("weight", item.getBook().getWeight());
            bookItem.put("length", item.getBook().getLength());
            bookItem.put("width", item.getBook().getWidth());
            return bookItem; }
                   ).toList();

        payload.put("items", items);

        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonPayload = mapper.writeValueAsString(payload);
            log.info("Generated GHN Request Payload: {}", jsonPayload);
            return jsonPayload;
        } catch (JsonProcessingException e) {
            log.error("Error while building GHN payload: {}", e.getMessage());
            return "{}";
        }
    }

    private int calculateTotalHeight(List<OrderItem> orderItems) {
        return orderItems.stream()
                .mapToInt(item -> (int) (item.getBook().getHeight() * item.getQuantity()))
                .sum();
    }

    private int calculateTotalWeight(List<OrderItem> orderItems) {
        return orderItems.stream()
                .mapToInt(item -> (int) (item.getBook().getWeight() * item.getQuantity()))
                .sum();
    }

    private int calculateTotalLWidth(List<OrderItem> orderItems) {
        return orderItems.stream()
                .mapToInt(item -> (int) (item.getBook().getWidth() * item.getQuantity()))
                .sum();
    }

    private int checkMaxLength(List<OrderItem> orderItems) {
        return orderItems.stream()
                .mapToInt(item ->  item.getBook().getLength())
                .max()
                .orElse(10);
    }


    private int totalPrice(List<OrderItem> orderItems) {
        return orderItems.stream()
                .mapToInt(item -> (int) (item.getBook().getPrice() * item.getQuantity()))
                .sum();
    }



}
