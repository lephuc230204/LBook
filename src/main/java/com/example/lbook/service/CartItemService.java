package com.example.lbook.service;


import com.example.lbook.dto.rp.CartItemDto;
import com.example.lbook.dto.rp.ResponseData;
import com.example.lbook.dto.rq.CartItemForm;
import org.springframework.data.domain.Page;

public interface CartItemService {
    ResponseData<String> addCartItem(Long BookId, CartItemForm cartItemForm);

    ResponseData<String> removeCartItem(Long BookId, CartItemForm cartItemForm);

    ResponseData<String> deleteCartItem(Long cartItemId);

    ResponseData<Page<CartItemDto>> getCartItems(int page, int size);
}
