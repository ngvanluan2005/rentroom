package com.luannv.rentroom.mapper;

import com.luannv.rentroom.dto.response.RoleResponseDTO;
import com.luannv.rentroom.dto.request.RoleRequestDTO;
import com.luannv.rentroom.entity.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper implements GenericMapper<Role, RoleRequestDTO, RoleResponseDTO>{
    @Override
    public Role toEntity(RoleRequestDTO roleRequestDTO) {
        return Role
                .builder()
                .name(roleRequestDTO.getName())
                .build();
    }
    @Override
    public RoleResponseDTO toResponseDTO(Role role) {
        return RoleResponseDTO
                .builder()
                .id(role.getId())
                .name(role.getName())
                .build();
    }
}
