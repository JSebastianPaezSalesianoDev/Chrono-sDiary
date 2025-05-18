package com.accesodatos.service.security;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.accesodatos.dto.security.RoleResponseDto;
import com.accesodatos.dto.security.UserEventsResponse;
import com.accesodatos.dto.security.UserRequestDto;
import com.accesodatos.dto.security.UserResponseDto;
import com.accesodatos.entity.Role;
import com.accesodatos.entity.UserEntity;
import com.accesodatos.exception.ResourceNotFoundException;
import com.accesodatos.mapper.security.RoleMapper;
import com.accesodatos.mapper.security.UserMapper;
import com.accesodatos.repository.security.RoleRepository;
import com.accesodatos.repository.security.UserRepository;
import com.accesodatos.service.email.EmailService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository; 

    @Autowired
    private RoleMapper roleMapper; 
    @Autowired 
    private EmailService emailService;

    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        UserEntity user = userMapper.toUser(userRequestDto);
        user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));

        Set<Role> roles = new HashSet<>();
        if (userRequestDto.getRoleIds() != null) {
            for (Long roleId : userRequestDto.getRoleIds()) {
                Role role = roleRepository.findById(roleId)
                        .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + roleId));
                roles.add(role);
            }
        }
        Role defaultRole = roleRepository.findByName("USER").orElseThrow(() -> new ResourceNotFoundException("Default role 'USER' not found in database. Please ensure it exists."));
        roles.add(defaultRole);

        user.setRoles(roles); 



        UserEntity savedUser = userRepository.save(user);
        return mapUserEntityToDtoWithRoles(savedUser); 
    }

    @Override
    public UserResponseDto getUserById(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id: " + userId + " not found"));
        return mapUserEntityToDtoWithRoles(user);
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapUserEntityToDtoWithRoles) 
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDto updateUser(Long userId, UserRequestDto userRequestDto) {
        UserEntity existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id: " + userId + " not found"));

        UserEntity updatedUser = userMapper.toUser(userRequestDto); 
        existingUser.setUsername(updatedUser.getUsername());
        if (userRequestDto.getPassword() != null && !userRequestDto.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        }
        existingUser.setEnabled(updatedUser.isEnabled());
        existingUser.setAccountNoExpired(updatedUser.isAccountNoExpired());
        existingUser.setAccountNoLocked(updatedUser.isAccountNoLocked());
        existingUser.setCredentialNoExpired(updatedUser.isCredentialNoExpired());


        if (userRequestDto.getRoleIds() != null) {
            Set<Role> updatedRoles = new HashSet<>();
            for (Long roleId : userRequestDto.getRoleIds()) {
                Role role = roleRepository.findById(roleId)
                        .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + roleId));
                updatedRoles.add(role);
            }
            existingUser.setRoles(updatedRoles); 
        }

        UserEntity savedUser = userRepository.save(existingUser);
        return mapUserEntityToDtoWithRoles(savedUser);
    }

    @Override
    public void deleteUser(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id: " + userId + " not found"));
        userRepository.delete(user);
    }
    
    private UserResponseDto mapUserEntityToDtoWithRoles(UserEntity userEntity) {
        UserResponseDto userResponseDto = userMapper.toUserResponse(userEntity);
        if (userEntity.getRoles() != null) {
            Set<RoleResponseDto> roleResponseDtos = userEntity.getRoles().stream()
                    .map(roleMapper::toRoleResponseDto)
                    .collect(Collectors.toSet());
            userResponseDto.setRoles(roleResponseDtos);
        }
        return userResponseDto;
    }

	@Override
	public List<UserEventsResponse> getUserEvents() {
		return userRepository.findAll().stream()
				.map(userMapper::toUserEventsResponse).collect(Collectors.toList());
	}

	 @Override
	  
	    public void resetPassword(String email) {
	        if (email == null || email.trim().isEmpty()) {
	            System.out.println("email vacio");
	            return;
	        }

	        String trimmedEmail = email.trim();

	        Optional<UserEntity> userOptional = userRepository.findUserEntityByEmail(trimmedEmail);

	      

	        if (!userOptional.isPresent()) {
	            System.out.println( trimmedEmail);
	         
	            return;
	        }

	        UserEntity user = userOptional.get();


	        Random random = new Random();
	        int resetCode = 10000 + random.nextInt(90000); 
	        String resetCodeString = String.valueOf(resetCode);

	        try {
	            String to = user.getEmail();
	            String subject = "Chrono's Diary - Código para restablecer contraseña";
	            String body = String.format(
	                "¡Hola %s!\n\n" +
	                "Has solicitado un código para restablecer tu contraseña en Chrono's Diary.\n\n" +
	                "Tu código de restablecimiento es: %s\n\n" +
	                "Por favor, usa este código para completar el proceso. (Nota: La validación y uso de este código NO están implementados en esta versión simple).\n\n" +
	                user.getUsername() != null ? user.getUsername() : "Usuario", 
	                resetCodeString
	                
	            );
	            user.setPassword(resetCodeString);
	            userRepository.save(user); 
	            emailService.sendEmail(to, subject, body);
	            System.out.println("INFO: contraseña reseteada a" + trimmedEmail + " codigo: " + resetCodeString); 

	        } catch (Exception e) {
	         
	            System.err.println("ERROR:" + trimmedEmail + ": " + e.getMessage());

	        }

	    }



}