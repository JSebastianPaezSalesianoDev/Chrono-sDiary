package com.accesodatos.controller.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accesodatos.dto.apiDto.ApiResponseDto;
import com.accesodatos.dto.security.UserRequestDto;
import com.accesodatos.dto.security.UserResponseDto;
import com.accesodatos.service.security.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User", description = "Controller for managing users")
public class UserController {

    @Autowired
    private UserService userService;	
    @GetMapping("/ping")
	public ResponseEntity<String> pong() {
		return ResponseEntity.ok("pong...");
	}
    

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new user")
    public ResponseEntity<ApiResponseDto<UserResponseDto>> createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        UserResponseDto createdUser = userService.createUser(userRequestDto);
        ApiResponseDto<UserResponseDto> response = new ApiResponseDto<>("User created successfully", HttpStatus.CREATED.value(), createdUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get user by ID")
    public ResponseEntity<ApiResponseDto<UserResponseDto>> getUserById(@PathVariable Long userId) {
        UserResponseDto user = userService.getUserById(userId);
        ApiResponseDto<UserResponseDto> response = new ApiResponseDto<>("User fetched successfully", HttpStatus.OK.value(), user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all users")
    public ResponseEntity<ApiResponseDto<List<UserResponseDto>>> getAllUsers() {
        List<UserResponseDto> users = userService.getAllUsers();
        ApiResponseDto<List<UserResponseDto>> response = new ApiResponseDto<>("Users fetched successfully", HttpStatus.OK.value(), users);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping(value = "/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update an existing user")
    public ResponseEntity<ApiResponseDto<UserResponseDto>> updateUser(@PathVariable Long userId, @Valid @RequestBody UserRequestDto userRequestDto) {
        UserResponseDto updatedUser = userService.updateUser(userId, userRequestDto);
        ApiResponseDto<UserResponseDto> response = new ApiResponseDto<>("User updated successfully", HttpStatus.OK.value(), updatedUser);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Delete a user")
    public ResponseEntity<ApiResponseDto<Void>> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        ApiResponseDto<Void> response = new ApiResponseDto<>("User deleted successfully", HttpStatus.NO_CONTENT.value(), null);
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }
}