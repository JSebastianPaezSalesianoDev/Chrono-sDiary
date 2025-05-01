package com.accesodatos.mapper.security;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.accesodatos.dto.security.UserRequestDto;
import com.accesodatos.dto.security.UserResponseDto;
import com.accesodatos.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(source = "enabled", target = "isEnabled")
    UserEntity toUser(UserRequestDto userRequestDto);

    UserResponseDto toUserResponse(UserEntity user);
}