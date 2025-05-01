package com.accesodatos.dto.security;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthLoginRequestDto {

	@NotBlank
	private String username;
	
	@NotBlank
	private String password;
}
