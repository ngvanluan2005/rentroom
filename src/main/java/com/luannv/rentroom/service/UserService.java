package com.luannv.rentroom.service;

import com.luannv.rentroom.dto.request.UserRequestDTO;
import com.luannv.rentroom.dto.response.UserResponseDTO;
import com.luannv.rentroom.entity.UserEntity;
import com.luannv.rentroom.exception.ErrorCode;
import com.luannv.rentroom.exception.ValueException;
import com.luannv.rentroom.mapper.UserMapper;
import com.luannv.rentroom.repository.UserRepository;
import com.luannv.rentroom.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.luannv.rentroom.constants.UrlConstants.API_USER;

// admin/moderator/user|luan.123
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final SecurityUtils securityUtils;
    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper, SecurityUtils securityUtils) {
        this.userMapper=userMapper;
        this.userRepository = userRepository;
        this.securityUtils = securityUtils;
    }
    // not validation
    public UserResponseDTO addUser(UserRequestDTO userRequestDTO, MultipartFile multipartFile) {
        UserEntity userEntity = this.userMapper.toEntity(userRequestDTO);
        if (multipartFile != null && !multipartFile.isEmpty()) {
            try {
                UserResponseDTO userResponseDTO = this.userMapper.toResponseDTO(userEntity);
                userResponseDTO.setAvatar(API_USER+"/"+userEntity.getUsername()+"/avatar");
                userEntity.setAvatar(multipartFile.getBytes());
                UserEntity saved = this.userRepository.save(userEntity);
                userResponseDTO.setUserId(saved.getId());
                return userResponseDTO;
            } catch (IOException e) {
                throw new RuntimeException("ErrorCode: ", e);
            }
        }
        UserEntity saved = this.userRepository.save(userEntity);
        return this.userMapper.toResponseDTO(saved);
    }

    public List<UserResponseDTO> getAll() {
        return this.userRepository.findAll().stream().map(user -> this.userMapper.toResponseDTO(user)).collect(Collectors.toList());
    }
    public UserResponseDTO getUserByUsername(String username) {
        UserEntity userEntity = this.userRepository.findByUsername(username).orElseThrow(() -> new ValueException(ErrorCode.USERNAME_NOT_EXISTED));
        return this.userMapper.toResponseDTO(userEntity);
    }
    // put update after have validator
    public void deleteByUsername(String username) {
        UserEntity userEntity = this.userRepository.findByUsername(username).orElseThrow(() -> new ValueException(ErrorCode.USERNAME_NOT_EXISTED));
        this.userRepository.delete(userEntity);
    }

    public byte[] getUserAvatar(String username) {
        UserEntity userEntity = this.userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(ErrorCode.USERNAME_NOT_EXISTED.getMessages()));
        if (userEntity != null || userEntity.getAvatar().length != 0)
            return userEntity.getAvatar();
        return new byte[1];
    }
}
