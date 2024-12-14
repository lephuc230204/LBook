package com.example.lbook.dto.rp;

import com.example.lbook.entity.CartItem;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class CartItemDto {
    private Long cartItemId;
    private Long cartId;
    private Long bookId;
    private String bookName;
    private Double price;
    private Long amount;
    private String image;
    private LocalDate createDate;

    public static CartItemDto toDto(CartItem cartItem) {
        return CartItemDto.builder()
                .cartItemId(cartItem.getCartItemId())
                .cartId(cartItem.getCart().getCartId())
                .bookId(cartItem.getBook().getBookId())
                .bookName(cartItem.getBook().getBookName())
                .price(cartItem.getBook().getPrice())
                .amount(cartItem.getBook().getQuantity())
                .image(cartItem.getBook().getImage())
                .build();
    }
}
