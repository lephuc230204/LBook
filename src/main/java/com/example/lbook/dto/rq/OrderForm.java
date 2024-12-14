package com.example.lbook.dto.rq;

import com.example.lbook.entity.Order;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderForm {
    @NotBlank(message = "Địa chỉ không được để trống")
    private String address;

    @NotBlank(message = "Số điện thoại không được để trống")
    private String phone;

    private String note;

    @NotNull(message = "Đơn vị vận chuyển là bắt buộc")
    private Order.ShippingUnit shippingUnit;

    @NotNull(message = "Phương thức thanh toán là bắt buộc")
    private Order.PaymentMethod paymentMethod;
}
