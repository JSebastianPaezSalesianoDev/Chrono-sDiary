package com.accesodatos.service.security;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.accesodatos.dto.security.AuthLoginRequestDto;
import com.accesodatos.dto.security.AuthResponseDto;
import com.accesodatos.entity.Role;
import com.accesodatos.entity.UserEntity;
import com.accesodatos.exception.ResourceNotFoundException;
import com.accesodatos.jwt.JwtTokenProvider;
import com.accesodatos.repository.security.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	JwtTokenProvider jwtTokenProvider;
	
	public Collection<GrantedAuthority> mapToAuthorities(Set<Role> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_".concat(role.getName())))
																			.collect(Collectors.toList());
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		System.out.println("username-->" + username);
		
		UserEntity userEntity = userRepository.findUserEntityByUsername(username)
											  .orElseThrow(() -> new ResourceNotFoundException("Username: " + username + " not found!"));
		
		
		return new User(userEntity.getUsername(),
						userEntity.getPassword(),
						userEntity.isEnabled(),
						userEntity.isAccountNoExpired(),
						userEntity.isCredentialNoExpired(),
						userEntity.isAccountNoLocked(),
						mapToAuthorities(userEntity.getRoles())
						);
	}

	private Authentication authenticate(String username, String password) {
		System.out.println("authenticate -->" + username);
		UserDetails userDetails = this.loadUserByUsername(username);
		
		if (!passwordEncoder.matches(password, userDetails.getPassword())) {
			throw new BadCredentialsException("Invalid username or password");
		}
		
		return new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(), userDetails.getAuthorities());
	}
	
	public AuthResponseDto login(AuthLoginRequestDto authLoginRequest) {
		
		System.out.println("Auth--> " + authLoginRequest.getUsername());
		
		Authentication authentication = this.authenticate(authLoginRequest.getUsername(), authLoginRequest.getPassword());
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String accessToken = jwtTokenProvider.generateToken(authentication);
		
		return new AuthResponseDto(accessToken);
	}
	
}
