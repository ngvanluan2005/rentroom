package com.luannv.rentroom.service;

import com.luannv.rentroom.dto.request.UserRequestDTO;
import com.luannv.rentroom.dto.response.UserResponseDTO;
import com.luannv.rentroom.entity.UserEntity;
import com.luannv.rentroom.exception.ErrorCode;
import com.luannv.rentroom.exception.ValueException;
import com.luannv.rentroom.mapper.UserMapper;
import com.luannv.rentroom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
// admin/moderator/user|luan.123
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userMapper=userMapper;
        this.userRepository = userRepository;
    }
    // not validation
    public UserResponseDTO addUser(UserRequestDTO userRequestDTO, MultipartFile multipartFile) {
        UserEntity userEntity = this.userMapper.toEntity(userRequestDTO);
        if (multipartFile != null && !multipartFile.isEmpty()) {
            try {
                UserResponseDTO userResponseDTO = this.userMapper.toResponseDTO(userEntity);
                userResponseDTO.setAvatar(multipartFile.getBytes());
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

    public Optional<UserEntity> getUserById(String id) {
        return this.userRepository.findById(id);
    }
}
