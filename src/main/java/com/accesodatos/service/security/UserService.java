package com.accesodatos.service.security;

import java.util.List;

import com.accesodatos.dto.security.UserEventsResponse;
import com.accesodatos.dto.security.UserRequestDto;
import com.accesodatos.dto.security.UserResponseDto;

public interface UserService {
    UserResponseDto createUser(UserRequestDto userRequestDto);
    UserResponseDto getUserById(Long userId);
    List<UserResponseDto> getAllUsers();
    List<UserEventsResponse> getUserEvents();
    UserResponseDto updateUser(Long userId, UserRequestDto userRequestDto);
    void deleteUser(Long userId);
    void resetPassword(String email);
    UserResponseDto toggleAdminRole(Long userId);
}