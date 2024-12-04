package com.example.lbook.controller;


import com.example.lbook.dto.rp.ResponseData;
import com.example.lbook.dto.rp.ResponseError;
import com.example.lbook.dto.rq.OTPForm;
import com.example.lbook.dto.rq.SignInForm;
import com.example.lbook.dto.rq.SignUpForm;
import com.example.lbook.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {
    private final AuthService authService;


    @PostMapping("/login")
    public ResponseEntity login(@RequestBody SignInForm form){return ResponseEntity.ok(authService.login(form));
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseData<String>> register(@RequestBody SignUpForm form){return ResponseEntity.ok(authService.register(form));}

    @PostMapping("/logout")
    public ResponseEntity<ResponseData<String>> logout(HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.ok(authService.logout(request, response));
    }

    @PostMapping("/confirm/{userId}")
    public ResponseData<String> confirm(@Min(1) @PathVariable Long userId, @RequestBody OTPForm otpForm) {
        log.info("Confirm user, userId={}, otpCode={}", userId, otpForm.getOtpCode());

        try {
            authService.confirmUser(userId, otpForm.getOtpCode());
            return new ResponseData<>(200, "User has confirmed successfully");
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(400, "Confirm was failed");
        }
    }

}