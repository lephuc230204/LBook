package com.example.lbook.service;

import com.example.lbook.entity.Address;
import com.example.lbook.entity.OrderItem;

import java.util.List;

public interface ShipFeeService {
    int calculateShipFee(List<OrderItem> orderItems, Address address);
}
