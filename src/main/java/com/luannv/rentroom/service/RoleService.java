package com.luannv.rentroom.service;

import com.luannv.rentroom.dto.response.RoleResponseDTO;
import com.luannv.rentroom.dto.request.RoleRequestDTO;
import com.luannv.rentroom.entity.Role;
import com.luannv.rentroom.exception.ErrorCode;
import com.luannv.rentroom.exception.ValueException;
import com.luannv.rentroom.mapper.RoleMapper;
import com.luannv.rentroom.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService {
    private RoleRepository roleRepository;
    private RoleMapper roleMapper;

    @Autowired
    public RoleService(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
        this.roleRepository = roleRepository;
    }
    public RoleResponseDTO addRole(RoleRequestDTO roleRequestDTO) {
        Role role = this.roleMapper.toEntity(roleRequestDTO);
        Role savedRole = this.roleRepository.save(role);
        return this.roleMapper.toResponseDTO(savedRole);
    }
    public List<RoleResponseDTO> getAllRoles() {
        return this.roleRepository.findAll()
                .stream()
                .map(role -> this.roleMapper.toResponseDTO(role))
                .collect(Collectors.toList());
    }
    public RoleResponseDTO getRoleById(Integer id) {
        return this.roleMapper
                .toResponseDTO(
                        this.roleRepository.findById(id)
                                .orElseThrow(() -> new ValueException(ErrorCode.ROLE_NOTFOUND)));
    }
    public RoleResponseDTO updateRoleById(Integer id, RoleRequestDTO roleRequestDTO) {
        Role role = this.roleRepository.findById(id).orElseThrow(()-> new ValueException(ErrorCode.ROLE_NOTFOUND));
        role.setName(roleRequestDTO.getName());
        Role savedRole = this.roleRepository.save(role);
        return this.roleMapper.toResponseDTO(savedRole);
    }
    public void deleteRoleById(Integer id) {
        Role role = this.roleRepository.findById(id).orElseThrow(()-> new ValueException(ErrorCode.ROLE_NOTFOUND));
        this.roleRepository.deleteById(role.getId());
    }
}
