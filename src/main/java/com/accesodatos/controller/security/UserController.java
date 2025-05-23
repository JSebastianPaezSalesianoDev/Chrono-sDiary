package com.accesodatos.controller.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accesodatos.dto.apiDto.ApiResponseDto;
import com.accesodatos.dto.security.PasswordResetRequest;
import com.accesodatos.dto.security.UserEventsResponse;
import com.accesodatos.dto.security.UserRequestDto;
import com.accesodatos.dto.security.UserResponseDto;
import com.accesodatos.service.security.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/users")
@Tag(name = "User", description = "Controller for managing users")
public class UserController {

    @Autowired
    private UserService userService;	
    @GetMapping("/ping")
    @Operation(summary = "Ping endpoint for User", description = "Returns pong...")
	public ResponseEntity<String> pong() {
		return ResponseEntity.ok("pong...");
	}
    
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)	
    @Operation(summary = "Create a new user", description = "Registers a new user in the system")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "User created successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })
    public ResponseEntity<ApiResponseDto<UserResponseDto>> createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        UserResponseDto createdUser = userService.createUser(userRequestDto);
        ApiResponseDto<UserResponseDto> response = new ApiResponseDto<>("User created successfully", HttpStatus.CREATED.value(), createdUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get user by ID", description = "Fetch a user by their ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User fetched successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    public ResponseEntity<ApiResponseDto<UserResponseDto>> getUserById(
        @Parameter(description = "User ID", required = true) @PathVariable Long userId) {
        UserResponseDto user = userService.getUserById(userId);
        ApiResponseDto<UserResponseDto> response = new ApiResponseDto<>("User fetched successfully", HttpStatus.OK.value(), user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all users", description = "Fetch all users in the system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Users fetched successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserResponseDto.class)))
    })
    public ResponseEntity<ApiResponseDto<List<UserResponseDto>>> getAllUsers() {
        List<UserResponseDto> users = userService.getAllUsers();
        ApiResponseDto<List<UserResponseDto>> response = new ApiResponseDto<>("Users fetched successfully", HttpStatus.OK.value(), users);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping(value = "/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update an existing user", description = "Updates a user's information")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User updated successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    public ResponseEntity<ApiResponseDto<UserResponseDto>> updateUser(
        @Parameter(description = "User ID", required = true) @PathVariable Long userId,
        @Valid @RequestBody UserRequestDto userRequestDto) {
        UserResponseDto updatedUser = userService.updateUser(userId, userRequestDto);
        ApiResponseDto<UserResponseDto> response = new ApiResponseDto<>("User updated successfully", HttpStatus.OK.value(), updatedUser);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Delete a user", description = "Deletes a user by their ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    public ResponseEntity<ApiResponseDto<Void>> deleteUser(
        @Parameter(description = "User ID", required = true) @PathVariable Long userId) {
        userService.deleteUser(userId);
        ApiResponseDto<Void> response = new ApiResponseDto<>("User deleted successfully", HttpStatus.NO_CONTENT.value(), null);
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }
    
    @GetMapping(value = "/event",produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all events for all users", description = "Fetch all events for all users")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User's events fetched successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserEventsResponse.class)))
    })
    public ResponseEntity<ApiResponseDto<List<UserEventsResponse>>> getUserEvents() {
        List<UserEventsResponse> events = userService.getUserEvents();
        ApiResponseDto<List<UserEventsResponse>> response = new ApiResponseDto<>("User's events fetched successfully", HttpStatus.OK.value(), events);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @PostMapping(value = "/reset-password", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Request password reset for a user via email", description = "Sends a password reset code to the user's email")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Password reset email sent"),
        @ApiResponse(responseCode = "400", description = "Email is required", content = @Content)
    })
    public ResponseEntity<ApiResponseDto<Void>> requestPasswordReset(
            @Valid @RequestBody PasswordResetRequest request) {

        if (request == null || request.getEmail() == null || request.getEmail().trim().isEmpty()) {
             ApiResponseDto<Void> response = new ApiResponseDto<>("El correo electrónico es requerido para restablecer la contraseña.", HttpStatus.BAD_REQUEST.value(), null);
             return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        userService.resetPassword(request.getEmail());
        ApiResponseDto<Void> response = new ApiResponseDto<>("Si tu correo electrónico está registrado, recibirás un correo con instrucciones.", HttpStatus.OK.value(), null);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }
    
    @PutMapping("/{userId}/toggle-admin")
    @Operation(summary = "Toggle admin role for a user", description = "Grants or removes admin role for a user")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User role toggled successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    public ResponseEntity<UserResponseDto> toggleUserAdminRole(
        @Parameter(description = "User ID", required = true) @PathVariable Long userId) {
        UserResponseDto updatedUser = userService.toggleAdminRole(userId);
        return ResponseEntity.ok(updatedUser);
    }
}