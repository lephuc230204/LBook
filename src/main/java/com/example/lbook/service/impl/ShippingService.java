package com.example.lbook.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class ShippingService {
    @Autowired
    private RestTemplate restTemplate;

    public double calculateShippingFee(String shippingUnit, String address, double weight) {
        String apiUrl = getApiUrlForUnit(shippingUnit);

        // Tạo payload cho API
        Map<String, Object> requestPayload = new HashMap<>();
        requestPayload.put("address", address);
        requestPayload.put("weight", weight);

        try {
            // Gửi yêu cầu POST đến API bên thứ ba
            Map<String, Object> response = restTemplate.postForObject(apiUrl, requestPayload, Map.class);

            if (response != null && response.containsKey("shippingFee")) {
                // Lấy phí vận chuyển từ phản hồi
                return Double.parseDouble(response.get("shippingFee").toString());
            } else {
                throw new IllegalStateException("Invalid response from shipping API");
            }
        } catch (Exception ex) {
            throw new RuntimeException("Error while calculating shipping fee: " + ex.getMessage(), ex);
        }
    }

    private String getApiUrlForUnit(String shippingUnit) {
        switch (shippingUnit.toUpperCase()) {
            case "GHN":
                return "https://api.ghn.vn/v1/shipping/calculate";
            case "GHTK":
                return "https://services.ghtk.vn/shipping/calculate";
            case "VIETTEL_POST":
                return "https://api.viettelpost.vn/shipping/calculate";
            default:
                throw new IllegalArgumentException("Unsupported shipping unit: " + shippingUnit);
        }
    }
}
