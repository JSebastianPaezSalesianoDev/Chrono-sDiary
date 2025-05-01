package com.accesodatos.service.role;

import java.util.List;

import com.accesodatos.dto.security.RoleRequestDto;
import com.accesodatos.dto.security.RoleResponseDto;

public interface RoleService {
    RoleResponseDto createRole(RoleRequestDto roleRequestDto);
    RoleResponseDto getRoleById(Long roleId);
    List<RoleResponseDto> getAllRoles();
    RoleResponseDto updateRole(Long roleId, RoleRequestDto roleRequestDto);
    void deleteRole(Long roleId);
}