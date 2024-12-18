package com.example.lbook.dto.rq;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignInForm {
    @NotBlank(message = "email must me not blank! ")
    @Email(message = "The email is not in the correct format! ")
    private String email;

    @NotBlank(message = "password must me not blank! ")
    private String password;
}