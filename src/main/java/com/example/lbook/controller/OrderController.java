package com.example.lbook.controller;

import com.example.lbook.dto.rp.ResponseData;
import com.example.lbook.dto.rq.OrderForm;
import com.example.lbook.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody OrderForm form) {
        try {
            ResponseData<?> response = orderService.createOrder(form, form.getCartItemIds());
            return ResponseEntity.status(response.getStatus()).body(response);
        } catch (Exception e) {
            log.error("Error creating order: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

}
