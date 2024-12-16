package com.example.lbook.controller;

import com.example.lbook.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    // Endpoint lấy danh sách tỉnh/thành phố
    @GetMapping("/provinces")
    public ResponseEntity<List<Map<String, Object>>> getProvinces() {
        List<Map<String, Object>> provinces = addressService.getProvinces();
        return ResponseEntity.ok(provinces);
    }

    // Endpoint lấy danh sách quận/huyện theo province_id
    @GetMapping("/districts")
    public ResponseEntity<List<Map<String, Object>>> getDistricts(@RequestParam("provinceId") int provinceId) {
        List<Map<String, Object>> districts = addressService.getDistricts(provinceId);
        return ResponseEntity.ok(districts);
    }

    // Endpoint lấy danh sách phường/xã theo district_id
    @GetMapping("/wards")
    public ResponseEntity<List<Map<String, Object>>> getWards(@RequestParam("districtId") int districtId) {
        List<Map<String, Object>> wards = addressService.getWards(districtId);
        return ResponseEntity.ok(wards);
    }
}
