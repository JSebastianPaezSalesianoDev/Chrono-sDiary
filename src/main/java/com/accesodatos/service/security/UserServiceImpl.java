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

    /**
     * Crea un nuevo usuario y le asigna roles.
     */
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

    /**
     * Obtiene un usuario por su ID.
     */
    @Override
    public UserResponseDto getUserById(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id: " + userId + " not found"));
        return mapUserEntityToDtoWithRoles(user);
    }

    /**
     * Obtiene todos los usuarios registrados.
     */
    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapUserEntityToDtoWithRoles) 
                .collect(Collectors.toList());
    }

    /**
     * Actualiza los datos de un usuario existente.
     */
    @Override
    public UserResponseDto updateUser(Long userId, UserRequestDto userRequestDto) {
        UserEntity existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id: " + userId + " not found"));

        UserEntity updatedUser = userMapper.toUser(userRequestDto); 
        existingUser.setUsername(updatedUser.getUsername());
        if (userRequestDto.getPassword() != null && !userRequestDto.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        }
        if (userRequestDto.getEmail() != null && !userRequestDto.getEmail().trim().isEmpty()) {
            String newEmail = userRequestDto.getEmail().trim();
            if (!newEmail.equalsIgnoreCase(existingUser.getEmail())) {
                Optional<UserEntity> userByNewEmail = userRepository.findUserEntityByEmail(newEmail);
                if (userByNewEmail.isPresent() && !userByNewEmail.get().getId().equals(userId)) {
                    throw new RuntimeException("Error: Email " + newEmail + " is already in use by another account.");
                }
                existingUser.setEmail(newEmail);
            }
        }
        existingUser.setEnabled(updatedUser.isEnabled());
        existingUser.setAccountNoExpired(updatedUser.isAccountNoExpired());
        existingUser.setAccountNoLocked(updatedUser.isAccountNoLocked());

        UserEntity savedUser = userRepository.save(existingUser);
        return mapUserEntityToDtoWithRoles(savedUser);
    }
 
    /**
     * Elimina un usuario por su ID.
     */
    @Override
    public void deleteUser(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id: " + userId + " not found"));

        user.getRoles().clear();
        user.getEvents().clear(); 

        userRepository.delete(user);
    }

    /**
     * Mapea un UserEntity a UserResponseDto incluyendo los roles.
     */
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

    /**
     * Obtiene todos los eventos de todos los usuarios.
     */
	@Override
	public List<UserEventsResponse> getUserEvents() {
		return userRepository.findAll().stream()
				.map(userMapper::toUserEventsResponse).collect(Collectors.toList());
	}

    /**
     * Restablece la contraseña de un usuario y envía un código por email.
     */
	 @Override
	 public void resetPassword(String email) {
	     if (email == null || email.trim().isEmpty()) {
	         return;
	     }

	     String trimmedEmail = email.trim();

	     Optional<UserEntity> userOptional = userRepository.findUserEntityByEmail(trimmedEmail);

	     if (!userOptional.isPresent()) {
	         System.out.println("INFO: Password reset requested for unknown email: " + trimmedEmail);
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
	             "Por favor, usa este código para completar el proceso.",
	             user.getUsername() != null ? user.getUsername() : "Usuario",
	             resetCodeString
	         );

	         user.setPassword(passwordEncoder.encode(resetCodeString));
	         userRepository.save(user);

	         emailService.sendEmail(to, subject, body);

	         System.out.println("INFO: Contraseña reseteada y correo enviado a " + trimmedEmail + " con código: " + resetCodeString);

	     } catch (Exception e) {
	         System.err.println("ERROR: Fallo al procesar o enviar correo de restablecimiento para " + trimmedEmail + ": " + e.getMessage());
	         throw new RuntimeException("Error processing or sending password reset email", e);
	     }
	 }

    /**
     * Alterna el rol de admin para un usuario (añade o quita el rol ADMIN).
     */
	    @Override
	    public UserResponseDto toggleAdminRole(Long userId) {
	        UserEntity user = userRepository.findById(userId)
	                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

	        Role adminRole = roleRepository.findByName("ADMIN")
	                .orElseThrow(() -> new ResourceNotFoundException("Role ADMIN not found. Ensure it exists in the database."));
	        Role userRole = roleRepository.findByName("USER")
	                .orElseThrow(() -> new ResourceNotFoundException("Role USER not found. Ensure it exists in the database."));

	        Set<Role> currentRoles = user.getRoles();
	        if (currentRoles == null) {
	            currentRoles = new HashSet<>(); 
	        }

	        boolean userIsCurrentlyAdmin = currentRoles.contains(adminRole);

	        if (userIsCurrentlyAdmin) {
	            
	            currentRoles.remove(adminRole);
	            currentRoles.add(userRole); 
	            System.out.println("INFO: User " + user.getUsername() + " (ID: " + userId + ") removed from ADMIN role, set to USER.");
	        } else {
	           
	            currentRoles.add(adminRole);
	            currentRoles.remove(userRole); 
	            System.out.println("INFO: User " + user.getUsername() + " (ID: " + userId + ") granted ADMIN role.");
	        }

	        user.setRoles(currentRoles);
	        UserEntity savedUser = userRepository.save(user);
	        return mapUserEntityToDtoWithRoles(savedUser);
	    }
	    
	    


}