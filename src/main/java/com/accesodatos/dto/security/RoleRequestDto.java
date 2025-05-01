package com.accesodatos.dto.security;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoleRequestDto {

    @NotBlank(message = "Role name cannot be blank")
    @Size(max = 20, message = "Role name cannot exceed 20 characters")
    private String name;
}