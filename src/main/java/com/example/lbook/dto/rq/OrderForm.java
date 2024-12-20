package com.example.lbook.dto.rq;

import com.example.lbook.entity.Order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class OrderForm {
    @NotNull(message = "Address ID cannot be null.")
    private Long addressId;
    @NotNull(message = "Payment method cannot be null.")
    private Order.PaymentMedthodEnum paymentMethod;
    @NotBlank(message = "Phone number is required.")
    private String phone;
    private String voucher;
    @NotEmpty(message = "cartItemIds number is required.")
    @Size(min = 1, message = "At least one cart item ID is required.")
    private List<Long> cartItemIds;
    @NotBlank(message = "Shipping unit is required.")
    private String shippingUnit;
    private String note;
    private Order.Status status;
}
