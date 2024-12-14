package com.example.lbook.service.impl;

import com.example.lbook.dto.rp.CartDto;
import com.example.lbook.dto.rp.ResponseData;
import com.example.lbook.dto.rp.ResponseError;
import com.example.lbook.entity.Cart;
import com.example.lbook.entity.User;
import com.example.lbook.repository.CartRepository;
import com.example.lbook.repository.UserRepository;
import com.example.lbook.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseData<CartDto> getCartByUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            log.error("User not found for email: {}", email);
            return new ResponseError<>(404, "User not found");
        }

        Cart cart = cartRepository.findByUser_UserId(user.getUserId());

        return new ResponseData<>(200,"Get cart successfully", CartDto.toDto(cart));
    }
}
