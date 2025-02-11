package com.luannv.rentroom.mapper;

import com.luannv.rentroom.dto.request.UserRequestDTO;
import com.luannv.rentroom.dto.response.UserResponseDTO;
import com.luannv.rentroom.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class UserMapper implements GenericMapper<UserEntity, UserRequestDTO, UserResponseDTO> {
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserEntity toEntity(UserRequestDTO userRequestDTO) {
        return UserEntity.builder()
                .fullName(userRequestDTO.getFirstName() + " " + userRequestDTO.getLastName())
                .password(passwordEncoder.encode(userRequestDTO.getPassword()))
                .address(userRequestDTO.getAddress())
                .username(userRequestDTO.getUsername())
                .email(userRequestDTO.getEmail())
                .build();
    }

    @Override
    public UserResponseDTO toResponseDTO(UserEntity userEntity) {
        return UserResponseDTO.builder()
                .userId(userEntity.getId())
                .fullName(userEntity.getFullName())
                .address(userEntity.getAddress())
                .email(userEntity.getEmail())
                .username(userEntity.getUsername())
                .avatar(userEntity.getAvatar())
                .roleId(3)
                .build();
    }
}
