package com.example.lbook.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.Map;

@Service
public class ShippingService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${shipping.api.ghn.token}")
    private String apiToken;

    @Value("${shipping.api.ghn.shopId}")
    private String shopId;

    public double calculateShippingFee(int fromDistrictId, String fromWardCode, int toDistrictId, String toWardCode,
                                       double weight, double length, double width, double height, int serviceId) {
        // Thiết lập giá trị mặc định là 1 cho các tham số nếu không có giá trị cụ thể
        weight = (weight <= 0) ? 1.0 : weight;
        length = (length <= 0) ? 1.0 : length;
        width = (width <= 0) ? 1.0 : width;
        height = (height <= 0) ? 1.0 : height;
        serviceId = (serviceId <= 0) ? 1 : serviceId;  // Mặc định cho mã dịch vụ là 1

        String apiUrl = "https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee";

        // Tạo payload cho API
        Map<String, Object> requestPayload = createRequestPayload(fromDistrictId, fromWardCode, toDistrictId, toWardCode,
                weight, length, width, height, serviceId);

        try {
            // Tạo headers và thêm Authorization Bearer Token và ShopId
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + apiToken);
            headers.set("ShopId", shopId);

            // Tạo HttpEntity với payload và headers
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestPayload, headers);

            // Gửi yêu cầu POST với RestTemplate và nhận phản hồi
            ResponseEntity<Map> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, Map.class);

            if (response != null && response.getBody() != null && response.getBody().containsKey("data")) {
                Map<String, Object> responseData = (Map<String, Object>) response.getBody().get("data");
                double serviceFee = (double) responseData.get("service_fee");
                return serviceFee;
            } else {
                throw new IllegalStateException("Invalid response from shipping API");
            }
        } catch (Exception ex) {
            throw new RuntimeException("Error while calculating shipping fee: " + ex.getMessage(), ex);
        }
    }

    private Map<String, Object> createRequestPayload(int fromDistrictId, String fromWardCode, int toDistrictId,
                                                     String toWardCode, double weight, double length,
                                                     double width, double height, int serviceId) {
        Map<String, Object> payload = new HashMap<>();

        // Địa chỉ và thông tin vận chuyển
        payload.put("from_district_id", fromDistrictId);
        payload.put("from_ward_code", fromWardCode);
        payload.put("to_district_id", toDistrictId);
        payload.put("to_ward_code", toWardCode);

        // Thông tin kích thước và trọng lượng
        payload.put("weight", (int) weight);  // Trọng lượng tính bằng gram
        payload.put("length", (int) length);  // Chiều dài tính bằng cm
        payload.put("width", (int) width);    // Chiều rộng tính bằng cm
        payload.put("height", (int) height);  // Chiều cao tính bằng cm

        // Thông tin dịch vụ
        payload.put("service_id", serviceId);
        payload.put("insurance_value", 0);  // Mặc định không có bảo hiểm
        payload.put("cod_failed_amount", 0); // Mặc định không có phí COD thất bại

        return payload;
    }
}
