package com.example.lbook.dto.rp;


import com.example.lbook.entity.Cart;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class CartDto {
    private Long cartId;
    private Long userId;
    private LocalDate createdDate;

    public static CartDto toDto(Cart cart) {;
        return CartDto.builder()
                .cartId(cart.getCartId())
                .userId(cart.getUser().getUserId())
                .createdDate(LocalDate.now())
                .build();
    }
}
