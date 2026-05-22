package com.appfinanzas.appfinanzas_api.service;


import com.appfinanzas.appfinanzas_api.dto.request.CreateUserRequest;
import com.appfinanzas.appfinanzas_api.dto.response.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse createUser(CreateUserRequest request);

    List<UserResponse> getAllUsers();

    UserResponse getUserById(Long id);

    void deleteUser(Long id);
}