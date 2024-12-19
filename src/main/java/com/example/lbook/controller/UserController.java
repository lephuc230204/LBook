package com.example.lbook.controller;

import com.example.lbook.dto.rp.ResponseData;
import com.example.lbook.dto.rp.UserBasic;
import com.example.lbook.dto.rp.UserDto;
import com.example.lbook.dto.rq.ChangePasswordForm;
import com.example.lbook.dto.rq.UserForm;
import com.example.lbook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @PutMapping
    public ResponseEntity<ResponseData<String>> updateMe(Principal principal, @RequestBody UserForm userForm) {
        return ResponseEntity.ok(userService.updateMe(principal, userForm));
    }
    @PutMapping("/change-password")
    public ResponseEntity<ResponseData<String>> changePassword(@RequestBody ChangePasswordForm request, Principal connectedUser){
        return ResponseEntity.ok(userService.changePassword(request,connectedUser));
    };
}
