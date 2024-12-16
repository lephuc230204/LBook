package com.example.lbook.controller;

import com.example.lbook.service.impl.AddressService;
import com.example.lbook.service.impl.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/address")
public class AddressController {

    @Autowired
    private AddressService addressService;  // Inject AddressService
    @Autowired
    private ShippingService shippingService;

    // Endpoint để lấy ProvinceID từ tên tỉnh
    @GetMapping("/province-id")
    public ResponseEntity<String> getProvinceId(@RequestParam String provinceName) {
        try {
            // Gọi hàm getProvinceIdByName để lấy ProvinceID từ tên tỉnh
            int provinceId = addressService.getProvinceIdByName(provinceName);

            // Trả về ProvinceID
            return ResponseEntity.ok("Province ID: " + provinceId);
        } catch (RuntimeException e) {
            // Trả về lỗi nếu không tìm thấy tỉnh
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    // Endpoint để lấy DistrictID từ tên tỉnh và tên quận/huyện
    @GetMapping("/district-id")
    public ResponseEntity<String> getDistrictId(@RequestParam String provinceName, @RequestParam String districtName) {
        try {
            // Gọi hàm getDistrictIdByName để lấy DistrictID từ tên tỉnh và tên quận/huyện
            int districtId = addressService.getDistrictIdByName(provinceName, districtName);

            // Trả về DistrictID
            return ResponseEntity.ok("District ID: " + districtId);
        } catch (RuntimeException e) {
            // Trả về lỗi nếu không tìm thấy quận/huyện
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    // Endpoint để kiểm tra WardCode và DistrictID từ tên tỉnh, quận/huyện và phường/xã
    @GetMapping("/ward-code-id")
    public ResponseEntity<?> getWardCodeId(@RequestParam String provinceName,
                                           @RequestParam String districtName,
                                           @RequestParam String wardName) {
        try {
            // Gọi hàm getWardCodeByName để lấy districtId và wardCode
            String result = addressService.getWardCodeByName(provinceName, districtName, wardName);

            // Trả về kết quả nếu thành công
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            // Trả về lỗi với thông báo
            return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/provinces")
    public ResponseEntity<List<Map<String, Object>>> getAllProvinces() {
        try {
            List<Map<String, Object>> provinces = addressService.listAllProvinces();
            return ResponseEntity.ok(provinces);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonList(Collections.singletonMap("error", e.getMessage())));
        }
    }

    @GetMapping("/districts/{provinceId}")
    public ResponseEntity<List<Map<String, Object>>> getDistrictsByProvinceId(@PathVariable int provinceId) {
        try {
            List<Map<String, Object>> districts = addressService.listAllDistrictsByProvinceId(provinceId);
            return ResponseEntity.ok(districts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonList(Collections.singletonMap("error", e.getMessage())));
        }
    }

    @GetMapping("/wards/{districtId}")
    public ResponseEntity<List<Map<String, Object>>> getWardsByDistrictId(@PathVariable int districtId) {
        try {
            List<Map<String, Object>> wards = addressService.listAllWardsByDistrictId(districtId);
            return ResponseEntity.ok(wards);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonList(Collections.singletonMap("error", e.getMessage())));
        }
    }

//    @GetMapping("/calculateShip")
//    public ResponseEntity<Long> calculateShip(@RequestParam String provinceName,
//                                              @RequestParam String districtName,
//                                              @RequestParam String wardName) {
//        Long result = shippingService.calculateShip(provinceName, districtName, wardName);
//        return ResponseEntity.ok(result);
//    }
}
