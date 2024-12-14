package com.example.lbook.controller;

import com.example.lbook.dto.rp.OrderDto;
import com.example.lbook.dto.rp.ResponseData;
import com.example.lbook.dto.rq.OrderForm;
import com.example.lbook.entity.CartItem;
import com.example.lbook.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("")
    public ResponseEntity<ResponseData<OrderDto>> createOrder(@RequestBody @Valid OrderForm form, @RequestBody List<CartItem> cartItems) {
        return ResponseEntity.ok(orderService.createOrder(form, cartItems));
    }
}
