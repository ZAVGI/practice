package com.example.finance.service.impl;

import com.example.finance.dto.UserRegistrationDto;
import com.example.finance.dto.UserResponseDto;
import com.example.finance.entity.User;
import com.example.finance.repository.UserRepository;
import com.example.finance.service.UserService;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto register(UserRegistrationDto request) {
        userRepository.findByUsername(request.getUsername()).ifPresent(user -> {
            throw new EntityExistsException("Username already exists");
        });

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());

        User saved = userRepository.save(user);
        return UserResponseDto.builder()
                .id(saved.getId())
                .username(saved.getUsername())
                .email(saved.getEmail())
                .build();
    }
}
