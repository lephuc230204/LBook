package com.example.lbook.service;

import com.example.lbook.dto.rp.OrderDto;
import com.example.lbook.dto.rp.ResponseData;
import com.example.lbook.dto.rq.OrderForm;
import com.example.lbook.entity.OrderItem;

import java.util.List;

public interface OrderService {
    public ResponseData<OrderDto> createOrder(OrderForm form, List<Long> cartItemIds);

}
