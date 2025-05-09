package com.accesodatos.dto.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor


public class AuthResponseDto {
	private Long id;
	private String accessToken;
	private String username;
	
	/*public AuthResponseDto(String accessToken, Long id) {
		this.accessToken = accessToken;
	}*/
}
