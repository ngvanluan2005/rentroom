package com.luannv.rentroom.mapper;

import com.luannv.rentroom.dto.request.UserRegisterRequestDTO;
import com.luannv.rentroom.dto.response.UserResponseDTO;
import com.luannv.rentroom.entity.UserEntity;
import com.luannv.rentroom.utils.StringHandleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static com.luannv.rentroom.constants.UrlConstants.AVATAR_FORMAT_URL;
import static com.luannv.rentroom.constants.UrlConstants.DEFAULT_AVATAR;

@Component
public class UserMapper implements GenericMapper<UserEntity, UserRegisterRequestDTO, UserResponseDTO> {
    private final PasswordEncoder passwordEncoder;
    private final StringHandleUtils stringHandleUtils;

    @Autowired
    public UserMapper(PasswordEncoder passwordEncoder, StringHandleUtils stringHandleUtils) {
        this.passwordEncoder = passwordEncoder;
        this.stringHandleUtils = stringHandleUtils;
    }

    @Override
    public UserEntity toEntity(UserRegisterRequestDTO userRequestDTO) {
        if (userRequestDTO == null)
            return null;

        UserEntity.UserEntityBuilder builder = UserEntity.builder();

        if (userRequestDTO.getFirstName() != null && userRequestDTO.getLastName() != null) {
            builder.fullName(this.stringHandleUtils.toCapitalizeString(
                    userRequestDTO.getFirstName() + " " + userRequestDTO.getLastName()));
        }
        if (userRequestDTO.getPassword() != null) {
            builder.password(passwordEncoder.encode(userRequestDTO.getPassword()));
        }
        if (userRequestDTO.getAddress() != null) {
            builder.address(userRequestDTO.getAddress());
        }
        if (userRequestDTO.getUsername() != null) {
            builder.username(userRequestDTO.getUsername());
        }
        if (userRequestDTO.getEmail() != null) {
            builder.email(userRequestDTO.getEmail());
        }

        return builder.build();
    }

    @Override
    public UserResponseDTO toResponseDTO(UserEntity userEntity) {
        if (userEntity == null)
            return null;

        String avatarLink = (userEntity.getAvatar() != null)
                ? String.format(AVATAR_FORMAT_URL, userEntity.getUsername())
                : DEFAULT_AVATAR;

        UserResponseDTO.UserResponseDTOBuilder builder = UserResponseDTO.builder();

        if (userEntity.getId() != null) {
            builder.userId(userEntity.getId());
        }
        if (userEntity.getFullName() != null) {
            builder.fullName(userEntity.getFullName());
        }
        if (userEntity.getAddress() != null) {
            builder.address(userEntity.getAddress());
        }
        if (userEntity.getEmail() != null) {
            builder.email(userEntity.getEmail());
        }
        if (userEntity.getUsername() != null) {
            builder.username(userEntity.getUsername());
        }
        builder.avatar(avatarLink);

        if (userEntity.getRole() != null && userEntity.getRole().getName() != null) {
            builder.roleName(userEntity.getRole().getName());
        }
        builder.isActivate(0);
        return builder.build();
    }
}
