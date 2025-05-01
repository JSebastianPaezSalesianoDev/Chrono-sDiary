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
import com.accesodatos.dto.security.RoleRequestDto;
import com.accesodatos.dto.security.RoleResponseDto;
import com.accesodatos.service.role.RoleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/roles")
@Tag(name = "Role", description = "Controller for managing roles")
public class RoleController {
	
    @Autowired
    private RoleService roleService;
	@GetMapping("/ping")
	public ResponseEntity<String> pong() {
		return ResponseEntity.ok("pong...");
	}

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new role")
    public ResponseEntity<ApiResponseDto<RoleResponseDto>> createRole(@Valid @RequestBody RoleRequestDto roleRequestDto) {
        RoleResponseDto createdRole = roleService.createRole(roleRequestDto);
        ApiResponseDto<RoleResponseDto> response = new ApiResponseDto<>("Role created successfully", HttpStatus.CREATED.value(), createdRole);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{roleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get role by ID")
    public ResponseEntity<ApiResponseDto<RoleResponseDto>> getRoleById(@PathVariable Long roleId) {
        RoleResponseDto role = roleService.getRoleById(roleId);
        ApiResponseDto<RoleResponseDto> response = new ApiResponseDto<>("Role fetched successfully", HttpStatus.OK.value(), role);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all roles")
    public ResponseEntity<ApiResponseDto<List<RoleResponseDto>>> getAllRoles() {
        List<RoleResponseDto> roles = roleService.getAllRoles();
        ApiResponseDto<List<RoleResponseDto>> response = new ApiResponseDto<>("Roles fetched successfully", HttpStatus.OK.value(), roles);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping(value = "/{roleId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update an existing role")
    public ResponseEntity<ApiResponseDto<RoleResponseDto>> updateRole(@PathVariable Long roleId, @Valid @RequestBody RoleRequestDto roleRequestDto) {
        RoleResponseDto updatedRole = roleService.updateRole(roleId, roleRequestDto);
        ApiResponseDto<RoleResponseDto> response = new ApiResponseDto<>("Role updated successfully", HttpStatus.OK.value(), updatedRole);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{roleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Delete a role")
    public ResponseEntity<ApiResponseDto<Void>> deleteRole(@PathVariable Long roleId) {
        roleService.deleteRole(roleId);
        ApiResponseDto<Void> response = new ApiResponseDto<>("Role deleted successfully", HttpStatus.NO_CONTENT.value(), null);
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }
}