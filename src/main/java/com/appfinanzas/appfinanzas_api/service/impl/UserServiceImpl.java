package com.appfinanzas.appfinanzas_api.service.impl;


import com.appfinanzas.appfinanzas_api.dto.request.CreateUserRequest;
import com.appfinanzas.appfinanzas_api.dto.response.UserResponse;
import com.appfinanzas.appfinanzas_api.entity.User;
import com.appfinanzas.appfinanzas_api.enums.SubscriptionType;
import com.appfinanzas.appfinanzas_api.exception.BusinessException;
import com.appfinanzas.appfinanzas_api.exception.ResourceNotFoundException;
import com.appfinanzas.appfinanzas_api.repository.UserRepository;
import com.appfinanzas.appfinanzas_api.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponse createUser(CreateUserRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Ya existe un usuario registrado con el email: " + request.getEmail());
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setSubscriptionType(SubscriptionType.FREE);
        user.setActive(true);

        User savedUser = userRepository.save(user);

        return mapToResponse(savedUser);
    }

    @Override
    public List<UserResponse> getAllUsers() {

        List<User> users = userRepository.findAll();
        List<UserResponse> response = new ArrayList<>();

        for (User user : users) {
            response.add(mapToResponse(user));
        }

        return response;
    }

    @Override
    public UserResponse getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));

        return mapToResponse(user);
    }

    @Override
    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));

        user.setActive(false);

        userRepository.save(user);
    }

    private UserResponse mapToResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getSubscriptionType().name(),
                user.getActive(),
                user.getCreatedAt()
        );
    }
}