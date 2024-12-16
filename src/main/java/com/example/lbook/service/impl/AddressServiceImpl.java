package com.example.lbook.service.impl;

import com.example.lbook.dto.rq.OrderForm;
import com.example.lbook.entity.Address;
import com.example.lbook.entity.Order;
import com.example.lbook.repository.AddressRepository;
import com.example.lbook.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AddressServiceImpl implements AddressService {

    @Value("${ghn.token}")
    private String ghnToken;

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private AddressRepository addressRepository;

    @Override
    public Address createAddress(OrderForm form, Order order ) {
        Address address = Address.builder()
                .provinceId(form.getProvinceId())
                .districtId(form.getDistrictId())
                .wardId(form.getWardId())
                .numberHouse(form.getNumberHouse())
                .orders(List.of(order))
                .build();
        addressRepository.save(address);
        return address;
    }

    // Lấy danh sách tỉnh/thành phố
    @Override
    public List<Map<String, Object>> getProvinces() {
        String url = "https://online-gateway.ghn.vn/shiip/public-api/master-data/province";
        HttpHeaders headers = new HttpHeaders();
        headers.set("token", ghnToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        Map<String, Object> responseBody = response.getBody();
        List<Map<String, Object>> allProvinces = (List<Map<String, Object>>) responseBody.get("data");

        // Tạo danh sách chỉ chứa các trường ProvinceID và ProvinceName
        List<Map<String, Object>> filteredProvinces = new ArrayList<>();
        for (Map<String, Object> province : allProvinces) {
            Map<String, Object> filteredProvince = new HashMap<>();

            filteredProvince.put("ProvinceID", province.get("ProvinceID"));
            filteredProvince.put("ProvinceName", province.get("ProvinceName"));
            filteredProvinces.add(filteredProvince);
        }

        return filteredProvinces;
    }

    // Lấy danh sách quận/huyện theo province_id
    @Override
    public List<Map<String, Object>> getDistricts(int provinceId) {
        String url = "https://online-gateway.ghn.vn/shiip/public-api/master-data/district";
        HttpHeaders headers = new HttpHeaders();
        headers.set("token", ghnToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Integer> body = Map.of("province_id", provinceId);
        HttpEntity<Map<String, Integer>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);

        Map<String, Object> responseBody = response.getBody();

        List<Map<String, Object>> districts = (List<Map<String, Object>>) responseBody.get("data");

        List<Map<String, Object>> filteredDistricts = new ArrayList<>();
        for (Map<String, Object> district : districts) {
            Map<String, Object> filteredDistrict = new HashMap<>();
            filteredDistrict.put("DistrictID", district.get("DistrictID"));
            filteredDistrict.put("ProvinceID", district.get("ProvinceID"));
            filteredDistrict.put("DistrictName", district.get("DistrictName"));

            filteredDistricts.add(filteredDistrict);
        }

        return filteredDistricts;
    }

    // Lấy danh sách phường/xã theo district_id
    @Override
    public List<Map<String, Object>> getWards(int districtId) {
        String url = "https://online-gateway.ghn.vn/shiip/public-api/master-data/ward";
        HttpHeaders headers = new HttpHeaders();
        headers.set("token", ghnToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Integer> body = Map.of("district_id", districtId);
        HttpEntity<Map<String, Integer>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);

        Map<String, Object> responseBody = response.getBody();

        // Lấy danh sách các phường/xã từ phản hồi
        List<Map<String, Object>> wards = (List<Map<String, Object>>) responseBody.get("data");

        // Tạo một danh sách mới chỉ chứa các trường cần thiết
        List<Map<String, Object>> filteredWards = new ArrayList<>();
        for (Map<String, Object> ward : wards) {
            Map<String, Object> filteredWard = new HashMap<>();
            filteredWard.put("WardCode", ward.get("WardCode"));
            filteredWard.put("DistrictID", ward.get("DistrictID"));
            filteredWard.put("WardName", ward.get("WardName"));

            filteredWards.add(filteredWard);
        }

        return filteredWards;
    }

}
