package com.example.lbook.service;

import com.example.lbook.dto.rq.OrderForm;
import com.example.lbook.entity.Address;
import com.example.lbook.entity.Order;

import java.util.List;
import java.util.Map;

public interface AddressService {
    // Phương thức tạo địa chỉ mới
    Address createAddress(OrderForm form, Order order);

    List<Map<String, Object>> getProvinces();

    List<Map<String, Object>> getDistricts(int provinceId);

    List<Map<String, Object>> getWards(int districtId);


}
