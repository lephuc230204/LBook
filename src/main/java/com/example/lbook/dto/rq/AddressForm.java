package com.example.lbook.dto.rq;

import lombok.Data;

@Data
public class AddressForm {
    private int provinceId;
    private int districtId;
    private String wardId;
    private String fullAddress;
}
