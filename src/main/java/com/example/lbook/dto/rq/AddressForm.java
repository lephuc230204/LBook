package com.example.lbook.dto.rq;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NonNull;

@Data
public class AddressForm {
    @NotNull(message = "Province ID must be not null")
    private int provinceId;

    @NotNull(message = "Province ID must be not null")
    private int districtId;

    @NotNull(message = "Province ID must be not null")
    private String wardId;

    @NotBlank(message = "fullAddress must be not null")
    @Size(max=255, message = "The full address cannot exceed 255 words")
    private String fullAddress;
}
