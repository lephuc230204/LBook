package com.example.lbook.controller;


import com.example.lbook.dto.rp.CartDto;
import com.example.lbook.dto.rp.ResponseData;
import com.example.lbook.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("api/v1/cart")
@RestController
public class CartController {
    private final CartService cartService;

    @GetMapping
    public ResponseEntity<ResponseData<CartDto>> getCartByUserId(){
        return ResponseEntity.ok(cartService.getCartByUserId());
    }
}
