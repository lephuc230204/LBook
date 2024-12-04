package com.example.lbook.service;

import com.example.lbook.dto.rp.AuthDto;
import com.example.lbook.dto.rp.ResponseData;
import com.example.lbook.dto.rq.SignInForm;
import com.example.lbook.dto.rq.SignUpForm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    AuthDto login(SignInForm form);
    ResponseData<String> register(SignUpForm form);
    String confirmUser(long userId, String otpCode);
    ResponseData<String> logout(HttpServletRequest request, HttpServletResponse response);
}