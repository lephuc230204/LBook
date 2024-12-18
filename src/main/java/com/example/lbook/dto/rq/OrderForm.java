package com.example.lbook.dto.rq;

import com.example.lbook.entity.Order;

import lombok.Data;

import java.util.List;

@Data
public class OrderForm {
    private Long addressId;
    private Order.PaymentMedthodEnum paymentMethod;
    private String phone;
    private List<Long> cartItemIds;
    private String shippingUnit;
    private String note;
}
