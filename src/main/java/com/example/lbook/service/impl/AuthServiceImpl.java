package com.example.lbook.service.impl;

import com.example.lbook.dto.rp.AuthDto;
import com.example.lbook.dto.rp.ResponseData;
import com.example.lbook.dto.rp.ResponseError;
import com.example.lbook.dto.rq.SignInForm;
import com.example.lbook.dto.rq.SignUpForm;
import com.example.lbook.entity.Role;
import com.example.lbook.entity.User;
import com.example.lbook.middleware.JwtProvider;
import com.example.lbook.repository.RoleRepository;
import com.example.lbook.repository.UserRepository;
import com.example.lbook.service.AuthService;
import com.example.lbook.service.BlacklistTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Log4j2

public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final BlacklistTokenService blacklistTokenService;

    @Override
    public AuthDto login(SignInForm form) {
        // Kiểm tra xem người dùng có tồn tại không
        User user = userRepository.findByEmail(form.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + form.getEmail()));

        if (!user.getStatus().equals("ACTIVE")) {
            throw new IllegalArgumentException("Account is not active");
        }
        // Thực hiện xác thực
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(form.getEmail(), form.getPassword())
            );
        } catch (AuthenticationException e) {
            log.error("Authentication failed for email: {} with exception: {}", form.getEmail(), e.getMessage());
            throw new IllegalArgumentException("Invalid email or password");
        }

        // Nếu xác thực thành công
        String accessToken = jwtTokenProvider.generateToken(authentication);
        log.info("User {} logged in successfully with ", user.getEmail());
        String status = "success";
        String result = "Login successful";

        return AuthDto.from(user, accessToken, status, result);
    }

    @Override
    public ResponseData<String> register(SignUpForm form) {
        if (userRepository.existsByEmail(form.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        Role role = roleRepository.findByName("ROLE_USER").orElse(null);
        if(role == null){
            return new ResponseError<>(404,"Not found role ROLE_USER");
        }

        // Tạo mã xác thực
        String otpCode = String.format("%06d", new Random().nextInt(999999));

        // Tạo đối tượng User
        User user = User.builder()
                .username(form.getUsername())
                .email(form.getEmail())
                .password(passwordEncoder.encode(form.getPassword()))
                .role(role)
                .otpCode(otpCode)
                .status("INACTIVE") // Đặt trạng thái là "INACTIVE" cho đến khi xác thực email
                .build();

        // Lưu người dùng vào cơ sở dữ liệu
        userRepository.save(user);
        log.info("User {} registered", user.getEmail());

//        String veryfyCode = UUID.randomUUID().toString();

        // Gửi thông điệp đến Kafka để gửi email xác nhận
        kafkaTemplate.send("confirm-account-topic", String.format("email=%s,id=%s,otpCode=%s", user.getEmail(), user.getId(), otpCode)); // Chỉnh sửa ở đây
        return new ResponseData<>(200,"Success register new user. Please check your email for confirmation", "Id: " + user.getUserId());
    }


    @Override
    public String confirmUser(long userId, String otpCode) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Check if the OTP matches
        if (!otpCode.equals(user.getOtpCode())) {
            log.error("OTP does not match for userId={}", userId);
            throw new IllegalArgumentException("OTP is incorrect");
        }

        user.setStatus("ACTIVE");
        user.setCreatedDate(LocalDate.now());
        userRepository.save(user);
        return "Confirmed!";
    }


    @Override
    public ResponseData<String> logout(HttpServletRequest request, HttpServletResponse response) {
        // Lấy JWT từ header Authorization
        String token = jwtTokenProvider.getJwtFromRequest(request);

        // Kiểm tra xem token có hợp lệ không
        if (token != null && jwtTokenProvider.validateAccessToken(token)) {
            // Lấy ngày hết hạn từ token
            Date expiryDateFromToken = jwtTokenProvider.getExpiryDateFromToken(token);

            // Chuyển đổi từ java.util.Date sang java.time.LocalDateTime
            LocalDateTime expiryDate = expiryDateFromToken.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();

            // Lưu token vào danh sách đen
            blacklistTokenService.addTokenToBlacklist(token, expiryDate); // thêm token vào danh sách đen

            // Xóa cookie hoặc bất kỳ thông tin đăng nhập liên quan
            response.setHeader("Set-Cookie", "JSESSIONID=; HttpOnly; Path=/; Max-Age=0; Secure; SameSite=Strict");

            log.info("User logged out successfully with token: {}", token);
            return new ResponseData<>(200, "Đăng xuất thành công", null);
        }

        log.error("Logout failed: Invalid or expired token for request: {}", request.getRequestURI());
        return new ResponseError<>(400, "Token không hợp lệ hoặc đã hết hạn");
    }




}