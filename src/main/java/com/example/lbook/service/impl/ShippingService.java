package com.example.lbook.service.impl;

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
@Slf4j
@Service
public class ShippingService {

    @Autowired
    private RestTemplate restTemplate;

    private static final String BASE_URL = "https://online-gateway.ghn.vn/shiip/public-api/master-data";

    @Value("${shipping.api.ghn.token}")
    private String apiToken;

    @Value("${shipping.api.ghn.shopId}")
    private String shopId;

    public double calculateShippingFee(String provinceName, String districtName, String wardName,
                                       double weight, double length, double width, double height, int serviceId) {
        // Cập nhật các giá trị mặc định nếu giá trị không hợp lệ
        weight = Math.max(weight, 1.0);
        length = Math.max(length, 1.0);
        width = Math.max(width, 1.0);
        height = Math.max(height, 1.0);
        serviceId = Math.max(serviceId, 1);

        // Gọi phương thức để tạo payload với các thông số từ createRequestPayload
        Map<String, Object> requestPayload = createRequestPayload(provinceName, districtName, wardName);

        // Cập nhật thông số từ payload vào request
        requestPayload.put("weight", weight);
        requestPayload.put("length", length);
        requestPayload.put("width", width);
        requestPayload.put("height", height);
        requestPayload.put("service_id", serviceId);

        String apiUrl = "https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee";

        try {
            HttpHeaders headers = createHeaders();
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestPayload, headers);

            ResponseEntity<Map> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, Map.class);

            if (response != null && response.getBody() != null && response.getBody().containsKey("data")) {
                Map<String, Object> responseData = (Map<String, Object>) response.getBody().get("data");
                return ((Number) responseData.get("service_fee")).doubleValue();
            } else {
                throw new IllegalStateException("Invalid response from shipping API");
            }
        } catch (Exception ex) {
            throw new RuntimeException("Error while calculating shipping fee: " + ex.getMessage(), ex);
        }
    }

    public Map<String, Object> createRequestPayload(String provinceName, String districtName, String wardName) {
        Map<String, Object> payload = new HashMap<>();

        log.info(" lâấy dict, warcode");
        // Lấy DistrictID và WardCode từ getDistrictAndWard
        Map<String, Object> districtAndWard = getDistrictAndWard(provinceName, districtName, wardName);
        int toDistrictId = (int) districtAndWard.get("DistrictID");
        String toWardCode = (String) districtAndWard.get("WardCode");

        // Gán vào payload
        payload.put("to_district_id", toDistrictId);
        payload.put("to_ward_code", toWardCode);

        // Các thông số còn lại để trống hoặc giá trị mặc định
        payload.put("from_district_id", null);
        payload.put("from_ward_code", null);
        payload.put("weight", null);
        payload.put("length", null);
        payload.put("width", null);
        payload.put("height", null);
        payload.put("service_id", null);
        payload.put("insurance_value", 0);
        payload.put("cod_failed_amount", 0);

        return payload;
    }

    public Map<String, Object> getDistrictAndWard(String provinceName, String districtName, String wardName) {
        HttpHeaders headers = createHeaders();
        log.info("lấy tỉnh");
        int provinceId = getProvinceIdByName(provinceName, headers);
        int districtId = getDistrictIdByName(provinceId, districtName, headers);
        String wardCode = getWardCodeByName(districtId, wardName, headers);

        return Map.of("DistrictID", districtId, "WardCode", wardCode);
    }

    private int getProvinceIdByName(String provinceName, HttpHeaders headers) {
        String url = BASE_URL + "/province";
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        if (response.getBody() != null) {
            List<Map<String, Object>> provinces = (List<Map<String, Object>>) response.getBody().get("data");
            for (Map<String, Object> province : provinces) {
                if (provinceName.equals(province.get("ProvinceName"))) {
                    return ((Number) province.get("ProvinceID")).intValue();
                }
            }
        }
        throw new RuntimeException("Province not found: " + provinceName);
    }

    private int getDistrictIdByName(int provinceId, String districtName, HttpHeaders headers) {
        String url = BASE_URL + "/district";
        Map<String, Object> requestPayload = Map.of("province_id", provinceId);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestPayload, headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);

        if (response.getBody() != null) {
            List<Map<String, Object>> districts = (List<Map<String, Object>>) response.getBody().get("data");
            for (Map<String, Object> district : districts) {
                if (districtName.equals(district.get("DistrictName"))) {
                    return ((Number) district.get("DistrictID")).intValue();
                }
            }
        }
        throw new RuntimeException("District not found: " + districtName);
    }

    private String getWardCodeByName(int districtId, String wardName, HttpHeaders headers) {
        String url = BASE_URL + "/ward?district_id=" + districtId;
        HttpEntity<String> entity = new HttpEntity<>(headers);
        log.info("lấy warn");
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        if (response.getBody() != null) {
            List<Map<String, Object>> wards = (List<Map<String, Object>>) response.getBody().get("data");
            for (Map<String, Object> ward : wards) {
                if (wardName.equals(ward.get("WardName"))) {
                    return (String) ward.get("WardCode");
                }
            }
        }
        throw new RuntimeException("Ward not found: " + wardName);
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Token", apiToken);
        headers.set("ShopId", shopId);
        headers.set("Content-Type", "application/json");
        return headers;
    }
}