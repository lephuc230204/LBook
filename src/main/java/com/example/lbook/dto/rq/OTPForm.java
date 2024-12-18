package com.example.lbook.dto.rq;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OTPForm {
    @NotBlank(message = "Otp code must be not blank")
    private String otpCode;
}
