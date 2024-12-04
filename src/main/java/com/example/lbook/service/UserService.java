package com.example.lbook.service;



import com.example.lbook.dto.rp.ResponseData;
import com.example.lbook.dto.rp.UserBasic;
import com.example.lbook.dto.rp.UserDto;
import com.example.lbook.dto.rq.ChangePasswordForm;
import com.example.lbook.dto.rq.UserForm;

import java.security.Principal;
import java.util.List;

public interface UserService {
    ResponseData<List<UserDto>> getAll();
    ResponseData<List<UserDto>> searchUser(String query);
    ResponseData<UserDto> getById(Long id);
    ResponseData<String> changePassword(ChangePasswordForm request, Principal connectedUser);
    ResponseData<String> update(Long id, UserForm form);
    ResponseData<String> delete(Long id);
    ResponseData<UserBasic> getMe(Principal principal);
    ResponseData<String> updateMe(Principal principal, UserForm form);
}
