package com.example.lbook.service;


import com.example.lbook.dto.rp.CartDto;
import com.example.lbook.dto.rp.ResponseData;

public interface CartService {
    ResponseData<CartDto> getCartByUserId();
}
