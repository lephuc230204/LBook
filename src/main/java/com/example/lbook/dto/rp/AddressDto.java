package com.example.lbook.dto.rp;

import com.example.lbook.entity.Address;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AddressDto {
    private Long addressId;

    private int provinceId;

    private int districtId;

    private String wardId;

    private String fullAddress;

    public static AddressDto fromEntity(Address address) {
        return AddressDto.builder()
                .addressId(address.getAddressId())
                .provinceId(address.getProvinceId())
                .districtId(address.getDistrictId())
                .wardId(address.getWardId())
                .fullAddress(address.getFullAddress())
                .build();
    }
}
