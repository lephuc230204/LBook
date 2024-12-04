package com.example.lbook.dto.rq;

import lombok.Data;

@Data
public class SignInForm {
    private String email;
    private String password;
}