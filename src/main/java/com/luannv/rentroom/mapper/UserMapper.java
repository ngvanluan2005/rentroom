package com.luannv.rentroom.mapper;

import com.luannv.rentroom.dto.request.UserRequestDTO;
import com.luannv.rentroom.dto.response.UserResponseDTO;
import com.luannv.rentroom.entity.UserEntity;
import com.luannv.rentroom.utils.StringHandleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static com.luannv.rentroom.constants.UrlConstants.API_USER;
import static com.luannv.rentroom.constants.UrlConstants.DEFAULT_AVATAR;

@Component
public class UserMapper implements GenericMapper<UserEntity, UserRequestDTO, UserResponseDTO> {
    private final PasswordEncoder passwordEncoder;
    private final StringHandleUtils stringHandleUtils;
    @Autowired
    public UserMapper(PasswordEncoder passwordEncoder, StringHandleUtils stringHandleUtils) {
        this.passwordEncoder = passwordEncoder;
        this.stringHandleUtils = stringHandleUtils;
    }

    @Override
    public UserEntity toEntity(UserRequestDTO userRequestDTO) {

        return UserEntity.builder()
                .fullName(this.stringHandleUtils.toCapitalizeString(userRequestDTO.getFirstName())
                        + " " +
                        this.stringHandleUtils.toCapitalizeString(userRequestDTO.getLastName()))
                .password(passwordEncoder.encode(userRequestDTO.getPassword()))
                .address(userRequestDTO.getAddress())
                .username(userRequestDTO.getUsername())
                .email(userRequestDTO.getEmail())
                .build();
    }

    @Override
    public UserResponseDTO toResponseDTO(UserEntity userEntity) {
        String avatarLink = API_USER+"/"+userEntity.getUsername() + "/avatar";
//        if (userEntity.getAvatar().length==0)
//            avatarLink = "";
        if (userEntity.getAvatar() == null) {
            avatarLink = DEFAULT_AVATAR;
        }
        return UserResponseDTO.builder()
                .userId(userEntity.getId())
                .fullName(userEntity.getFullName())
                .address(userEntity.getAddress())
                .email(userEntity.getEmail())
                .username(userEntity.getUsername())
                .avatar(avatarLink)
                .roleName(userEntity.getRole().getName())
                .isActivate(userEntity.getIsActivate())
                .build();
    }
}
