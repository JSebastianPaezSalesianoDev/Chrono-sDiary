package com.accesodatos.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.accesodatos.jwt.JwtAuthenticationFilter;
import com.accesodatos.service.security.UserDetailsServiceImpl;


@Configuration
@EnableMethodSecurity
public class SecurityConfig {

	

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
				.csrf(csrf -> csrf.disable())
				.cors(Customizer.withDefaults())
				.authorizeHttpRequests(auth ->
												auth.requestMatchers(HttpMethod.GET,
																		"/api/auth/ping", 
																		"/doc/swagger-ui/**",
																		"/doc/swagger-ui.html",
																		"/v3/api-docs/**").permitAll() 
				.requestMatchers(HttpMethod.GET, "/api/v1/city/ping").hasAnyRole("USER","DEVELOPER","ADMIN","INVITED")
				.requestMatchers(HttpMethod.GET, "/api/v1/banks/ping").hasAnyRole("USER","DEVELOPER","ADMIN","INVITED")
				.requestMatchers(HttpMethod.GET, "/api/v1/customers/ping").hasAnyRole("USER","DEVELOPER","ADMIN","INVITED")
				.requestMatchers(HttpMethod.GET, "/api/v1/auth/ping").hasAnyRole("USER","DEVELOPER","ADMIN","INVITED")
				.requestMatchers(HttpMethod.GET, "/api/v1/roles/ping").hasAnyRole("USER","DEVELOPER","ADMIN","INVITED")
				.requestMatchers(HttpMethod.GET, "/api/v1/users/ping").hasAnyRole("USER","DEVELOPER","ADMIN","INVITED")
				.requestMatchers(HttpMethod.GET, "/api/v1/**").hasAnyRole("USER","DEVELOPER","ADMIN") 
				.requestMatchers(HttpMethod.POST,"/api/v1/**" ).hasRole("ADMIN") 
				.requestMatchers(HttpMethod.PUT, "/api/v1/**").hasAnyRole("DEVELOPER","ADMIN") 
				.requestMatchers(HttpMethod.DELETE, "/api/v1/**").hasRole("ADMIN") 
				.requestMatchers("/api/v1/users/**").hasRole("ADMIN")
				.requestMatchers("/api/v1/roles/**").hasRole("ADMIN")
				.requestMatchers(HttpMethod.POST, "/api/v1/auth/**").permitAll() 
				.anyRequest().authenticated()
				)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
				.httpBasic(Customizer.withDefaults()) 
				.build();
	}
	
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception { // se puede inyectar por constructor el authCon

		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	AuthenticationProvider authenticationProvider(UserDetailsServiceImpl userDetailsService) throws Exception {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

		provider.setPasswordEncoder(passwordEncoder());
		provider.setUserDetailsService(userDetailsService);

		return provider;
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter();
	}


}