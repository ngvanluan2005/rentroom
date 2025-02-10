package com.luannv.rentroom.service;

import com.luannv.rentroom.dto.request.UserRequestDTO;
import com.luannv.rentroom.dto.response.UserResponseDTO;
import com.luannv.rentroom.entity.UserEntity;
import com.luannv.rentroom.mapper.UserMapper;
import com.luannv.rentroom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userMapper=userMapper;
        this.userRepository = userRepository;
    }
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
                throw new RuntimeException("Error: ", e);
            }
        }
        UserEntity saved = this.userRepository.save(userEntity);
        return this.userMapper.toResponseDTO(saved);
    }

    public List<UserResponseDTO> getAll() {
        return this.userRepository.findAll().stream().map(user -> this.userMapper.toResponseDTO(user)).collect(Collectors.toList());
    }
}
