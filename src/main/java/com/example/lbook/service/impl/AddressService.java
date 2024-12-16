package com.example.lbook.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AddressService {

    private static final String BASE_URL_PROVINCE = "https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/province";
    private static final String BASE_URL_DISTRICT = "https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/district";
    private static final String BASE_URL_WARD = "https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/ward";

    private final RestTemplate restTemplate;

    @Value("${shipping.api.ghn.token}")
    private String apiToken;

    @Value("${shipping.api.ghn.shopId}")
    private String shopId;


    public AddressService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Hàm tìm ProvinceID từ tên tỉnh
    public int getProvinceIdByName(String provinceName) {
        // Tạo headers với Token
        HttpHeaders headers = createHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Gửi yêu cầu GET đến API của GHN để lấy danh sách tỉnh
        ResponseEntity<Map> response = restTemplate.exchange(BASE_URL_PROVINCE, HttpMethod.GET, entity, Map.class);

        if (response.getBody() != null) {
            List<Map<String, Object>> provinces = (List<Map<String, Object>>) response.getBody().get("data");

            // Duyệt qua danh sách tỉnh và tìm theo tên tỉnh
            for (Map<String, Object> province : provinces) {
                if (provinceName.equalsIgnoreCase((String) province.get("ProvinceName"))) {
                    return ((Number) province.get("ProvinceID")).intValue();
                }
            }
        }
        throw new RuntimeException("Province not found: " + provinceName);
    }

    // Hàm tìm DistrictID từ tên tỉnh và tên quận/huyện
    public int getDistrictIdByName(String provinceName, String districtName) {
        // Lấy ProvinceID từ tên tỉnh
        int provinceId = getProvinceIdByName(provinceName);

        // Tạo headers với Token
        HttpHeaders headers = createHeaders();
        headers.set("Content-Type", "application/json");

        // Tạo body cho yêu cầu lấy danh sách quận/huyện
        String body = "{\"province_id\":" + provinceId + "}";
        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        // Gửi yêu cầu GET để lấy danh sách các quận/huyện của tỉnh
        ResponseEntity<Map> response = restTemplate.exchange(BASE_URL_DISTRICT, HttpMethod.POST, entity, Map.class);

        if (response.getBody() != null) {
            List<Map<String, Object>> districts = (List<Map<String, Object>>) response.getBody().get("data");

            // Duyệt qua danh sách quận/huyện và tìm theo tên quận/huyện
            for (Map<String, Object> district : districts) {
                if (districtName.equalsIgnoreCase((String) district.get("DistrictName"))) {
                    return ((Number) district.get("DistrictID")).intValue();
                }
            }
        }
        throw new RuntimeException("District not found: " + districtName);
    }

    public String getWardCodeByName(String provinceName, String districtName, String wardName) {
        // Lấy DistrictID từ tên tỉnh và tên quận/huyện
        int districtId = getDistrictIdByName(provinceName, districtName);

        // Tạo headers với Token
        HttpHeaders headers = createHeaders();
        headers.set("Content-Type", "application/json");

        // Tạo body cho yêu cầu lấy danh sách các phường/xã trong quận/huyện
        String body = "{\"district_id\":" + districtId + "}";
        log.info("districtId: " + districtId); // Log ID quận/huyện

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        // Gửi yêu cầu POST đến API để lấy danh sách các phường/xã
        ResponseEntity<Map> response = restTemplate.exchange(BASE_URL_WARD, HttpMethod.POST, entity, Map.class);

        // Kiểm tra xem dữ liệu trả về có hợp lệ không
        if (response.getBody() != null) {
            List<Map<String, Object>> wards = (List<Map<String, Object>>) response.getBody().get("data");
            for (Map<String, Object> ward : wards) {
                // So sánh tên phường/xã với tên đã nhập
                if (wardName.equalsIgnoreCase((String) ward.get("WardName"))) {
                    // Tìm thấy phường/xã, trả về WardCode dưới dạng int
                    String wardCodeString = (String) ward.get("WardCode");
                    try {
                        return wardCodeString; // Chuyển đổi từ String sang int
                    } catch (NumberFormatException e) {
                        throw new RuntimeException("Invalid WardCode format for: " + wardName);
                    }
                }
            }
        }

        throw new RuntimeException("Ward not found: " + wardName);
    }



    // Tạo HttpHeaders với Token
    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Token", apiToken);  // Đặt token vào headers
        return headers;
    }

    public List<Map<String, Object>> listAllProvinces() {
        HttpHeaders headers = createHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(BASE_URL_PROVINCE, HttpMethod.GET, entity, Map.class);

        if (response.getBody() != null) {
            List<Map<String, Object>> provinces = (List<Map<String, Object>>) response.getBody().get("data");
            if (provinces != null && !provinces.isEmpty()) {
                return provinces.stream()
                        .map(province -> Map.of(
                                "ProvinceName", province.get("ProvinceName"),
                                "ProvinceID", province.get("ProvinceID")
                        ))
                        .collect(Collectors.toList());
            }
        }
        throw new RuntimeException("Failed to fetch province data");
    }

    public List<Map<String, Object>> listAllDistrictsByProvinceId(int provinceId) {
        // Tạo headers với Token
        HttpHeaders headers = createHeaders();
        headers.set("Content-Type", "application/json");

        // Tạo body cho yêu cầu lấy danh sách quận/huyện
        String body = "{\"province_id\":" + provinceId + "}";
        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        // Gửi yêu cầu POST để lấy danh sách các quận/huyện của tỉnh
        ResponseEntity<Map> response = restTemplate.exchange(BASE_URL_DISTRICT, HttpMethod.POST, entity, Map.class);

        if (response.getBody() != null) {
            List<Map<String, Object>> districts = (List<Map<String, Object>>) response.getBody().get("data");

            // Kiểm tra nếu có dữ liệu huyện
            if (districts != null && !districts.isEmpty()) {
                // Tạo danh sách các huyện với tên và ID
                List<Map<String, Object>> districtList = new ArrayList<>();
                for (Map<String, Object> district : districts) {
                    Map<String, Object> districtData = new HashMap<>();
                    districtData.put("DistrictName", district.get("DistrictName"));
                    districtData.put("DistrictID", district.get("DistrictID"));
                    districtList.add(districtData);
                }
                return districtList; // Trả về danh sách các huyện
            }
        }
        throw new RuntimeException("Failed to fetch district data for province ID: " + provinceId);
    }

    public List<Map<String, Object>> listAllWardsByDistrictId(int districtId) {
        // Tạo headers với Token
        HttpHeaders headers = createHeaders();
        headers.set("Content-Type", "application/json");

        // Tạo body cho yêu cầu lấy danh sách phường/xã
        String body = "{\"district_id\":" + districtId + "}";
        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        // Gửi yêu cầu POST để lấy danh sách các phường/xã trong quận/huyện
        ResponseEntity<Map> response = restTemplate.exchange(BASE_URL_WARD, HttpMethod.POST, entity, Map.class);

        if (response.getBody() != null) {
            List<Map<String, Object>> wards = (List<Map<String, Object>>) response.getBody().get("data");

            // Kiểm tra nếu có dữ liệu xã
            if (wards != null && !wards.isEmpty()) {
                // Tạo danh sách các xã với tên và mã code
                List<Map<String, Object>> wardList = new ArrayList<>();
                for (Map<String, Object> ward : wards) {
                    Map<String, Object> wardData = new HashMap<>();
                    wardData.put("WardName", ward.get("WardName"));
                    wardData.put("WardCode", ward.get("WardCode"));
                    wardList.add(wardData);
                }
                return wardList; // Trả về danh sách các xã
            }
        }
        throw new RuntimeException("Failed to fetch ward data for district ID: " + districtId);
    }


}

