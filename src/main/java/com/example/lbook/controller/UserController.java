package com.example.lbook.controller;

import com.example.lbook.dto.rp.ResponseData;
import com.example.lbook.dto.rp.UserBasic;
import com.example.lbook.dto.rp.UserDto;
import com.example.lbook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("api/v1/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("")
    public ResponseEntity<ResponseData<UserBasic>> getInfo(Principal principal) {
        return ResponseEntity.ok(userService.getMe(principal));
    }
}
