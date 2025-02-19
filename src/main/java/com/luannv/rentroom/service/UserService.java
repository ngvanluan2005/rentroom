package com.luannv.rentroom.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.luannv.rentroom.dto.response.UserResponseDTO;
import com.luannv.rentroom.entity.UserEntity;
import com.luannv.rentroom.exception.ErrorCode;
import com.luannv.rentroom.exception.SingleErrorException;
import com.luannv.rentroom.mapper.UserMapper;
import com.luannv.rentroom.repository.RoleRepository;
import com.luannv.rentroom.repository.UserRepository;
import com.luannv.rentroom.utils.SecurityUtils;

// admin/moderator/user|luan.123
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final SecurityUtils securityUtils;
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper, SecurityUtils securityUtils,
            RoleRepository roleRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.securityUtils = securityUtils;
        this.roleRepository = roleRepository;
    }

    public List<UserResponseDTO> getAll() {

        return this.userRepository.findAll().stream().map(user -> this.userMapper.toResponseDTO(user))
                .collect(Collectors.toList());
    }

    public UserResponseDTO getUserByUsername(String username) {
        UserEntity userEntity = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new SingleErrorException(ErrorCode.USERNAME_NOT_EXISTED));
        return this.userMapper.toResponseDTO(userEntity);
    }

    // put update after have validator
    public void deleteByUsername(String username) {
        UserEntity userEntity = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new SingleErrorException(ErrorCode.USERNAME_NOT_EXISTED));
        this.userRepository.delete(userEntity);
    }

    public byte[] getUserAvatar(String username) {
        UserEntity userEntity = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(ErrorCode.USERNAME_NOT_EXISTED.getMessages()));
        if (userEntity != null || userEntity.getAvatar().length != 0)
            return userEntity.getAvatar();
        return null;
    }

    public UserUpdateResponse editUserInfo(String username) {
        UserEntity userEntity = this.userRepository.findByUsername(username);

    }
}
