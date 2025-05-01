package com.accesodatos.service.role;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accesodatos.dto.security.RoleRequestDto;
import com.accesodatos.dto.security.RoleResponseDto;
import com.accesodatos.entity.Role;
import com.accesodatos.exception.ResourceNotFoundException;
import com.accesodatos.mapper.security.RoleMapper;
import com.accesodatos.repository.security.RoleRepository;
import com.accesodatos.repository.security.UserRepository;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private RoleMapper roleMapper;
	@Autowired
	private UserRepository userRepository;

	@Override
	public RoleResponseDto createRole(RoleRequestDto roleRequestDto) {
		Role role = roleMapper.toRole(roleRequestDto);
		Role savedRole = roleRepository.save(role);
		return roleMapper.toRoleResponseDto(savedRole);
	}

	@Override
	public RoleResponseDto getRoleById(Long roleId) {
		Role role = roleRepository.findById(roleId)
				.orElseThrow(() -> new ResourceNotFoundException("Role with id: " + roleId + " not found"));
		return roleMapper.toRoleResponseDto(role);
	}

	@Override
	public List<RoleResponseDto> getAllRoles() {
		return roleRepository.findAll().stream().map(roleMapper::toRoleResponseDto).collect(Collectors.toList());
	}

	@Override
	public RoleResponseDto updateRole(Long roleId, RoleRequestDto roleRequestDto) {
		Role role = roleRepository.findById(roleId)
				.orElseThrow(() -> new ResourceNotFoundException("Role with id: " + roleId + " not found"));
		role.setName(roleRequestDto.getName());
		Role updatedRole = roleRepository.save(role);
		return roleMapper.toRoleResponseDto(updatedRole);
	}

	@Override
    public void deleteRole(Long roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role with id: " + roleId + " not found"));


        long userWithRole = userRepository.findAll().stream()
                .filter(user -> user.getRoles().contains(role))
                .count();

        if (userWithRole > 0) {
            throw new Error("can't delete the role '" + role.getName() + "' because it's associated with " + userWithRole + " users.");
        }

        roleRepository.delete(role);
    }
}