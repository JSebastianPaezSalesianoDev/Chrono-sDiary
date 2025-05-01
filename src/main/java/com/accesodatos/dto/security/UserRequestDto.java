package com.accesodatos.dto.security;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRequestDto {

    @NotBlank(message = "Username cannot be blank")
    private String username;
    private String password; 
    private boolean isEnabled = true; 
    private boolean accountNoExpired = true; 
    private boolean accountNoLocked = true;
    private boolean credentialNoExpired = true;
    private Set<Long> roleIds; 
}