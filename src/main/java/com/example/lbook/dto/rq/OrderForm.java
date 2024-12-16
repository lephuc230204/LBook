package com.example.lbook.dto.rq;

import com.example.lbook.entity.Order;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;


@Data
public class OrderForm {
    @NotNull
    private int provinceId;
    @NotNull
    private int districtId;
    @NotBlank
    private String wardId;

    private String numberHouse;
    @NotBlank
    private Order.PaymentMedthodEnum paymentMethod;
    @NotNull
    private String phone;

    private List<Long> cartItemIds;
    private String shippingUnit;
    private String note;
}

