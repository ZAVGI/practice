package com.example.finance.service;

import com.example.finance.dto.UserRegistrationDto;
import com.example.finance.dto.UserResponseDto;

public interface UserService {
    UserResponseDto register(UserRegistrationDto request);
}
