package com.example.lbook.dto.rq;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangePasswordForm {
    @NotBlank(message = "oldPassword must be not blank!")
    private String oldPassword;
    @NotBlank(message = "new password must be not blank!")
    private String newPassword;
    @NotBlank(message = "confirm password must be not blank!")
    private String confirmPassword;
}