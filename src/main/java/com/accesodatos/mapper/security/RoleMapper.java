package com.accesodatos.mapper.security;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.accesodatos.dto.security.RoleRequestDto;
import com.accesodatos.dto.security.RoleResponseDto;
import com.accesodatos.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleResponseDto toRoleResponseDto(Role role);

    @Mapping(target = "id", ignore = true) 
    Role toRole(RoleRequestDto roleRequestDto);
}