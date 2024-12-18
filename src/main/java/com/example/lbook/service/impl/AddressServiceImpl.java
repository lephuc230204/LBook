package com.example.lbook.service.impl;

import com.example.lbook.dto.rp.AddressDto;
import com.example.lbook.dto.rp.ResponseData;
import com.example.lbook.dto.rp.ResponseError;
import com.example.lbook.dto.rq.AddressForm;
import com.example.lbook.dto.rq.OrderForm;
import com.example.lbook.entity.Address;
import com.example.lbook.entity.Order;
import com.example.lbook.entity.User;
import com.example.lbook.repository.AddressRepository;
import com.example.lbook.repository.UserRepository;
import com.example.lbook.service.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class AddressServiceImpl implements AddressService {

    @Value("${ghn.token}")
    private String ghnToken;

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseData<AddressDto> createAddress(AddressForm form) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            log.error("User not found");
            return new ResponseError<>(400, "User not found");
        }

        Address address = Address.builder()
                .user(user)
                .provinceId(form.getProvinceId())
                .districtId(form.getDistrictId())
                .wardId(form.getWardId())
                .fullAddress(form.getFullAddress())
                .build();
        addressRepository.save(address);
        return new ResponseData<>(200,"success", AddressDto.fromEntity(address));
    }

    // Lấy danh sách tỉnh/thành phố
    @Override
    public List<Map<String, Object>> getProvinces() {
        String url = "https://online-gateway.ghn.vn/shiip/public-api/master-data/province";
        HttpHeaders headers = new HttpHeaders();
        headers.set("token", ghnToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, request, Map.class);

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

        Map<String, Integer> payload = Map.of("province_id", provinceId);
        HttpEntity<Map<String, Integer>> request = new HttpEntity<>(payload, headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);

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
        HttpEntity<Map<String, Integer>> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);

        Map<String, Object> responseBody = response.getBody();

        List<Map<String, Object>> wards = (List<Map<String, Object>>) responseBody.get("data");

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
