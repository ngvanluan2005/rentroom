package com.luannv.rentroom.service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import com.luannv.rentroom.dto.request.UserUpdateRequest;
import com.luannv.rentroom.dto.response.UserResponseDTO;
import com.luannv.rentroom.dto.response.UserUpdateResponse;
import com.luannv.rentroom.entity.UserEntity;
import com.luannv.rentroom.exception.ErrorCode;
import com.luannv.rentroom.exception.ListErrorException;
import com.luannv.rentroom.exception.SingleErrorException;
import com.luannv.rentroom.mapper.UserMapper;
import com.luannv.rentroom.mapper.UserUpdateMapper;
import com.luannv.rentroom.repository.RoleRepository;
import com.luannv.rentroom.repository.UserRepository;
import com.luannv.rentroom.utils.SecurityUtils;

// admin/moderator/user|luan.123
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserUpdateMapper userUpdateMapper;
    private final SecurityUtils securityUtils;
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper, SecurityUtils securityUtils,
            RoleRepository roleRepository, com.luannv.rentroom.mapper.UserUpdateMapper userUpdateMapper) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.securityUtils = securityUtils;
        this.roleRepository = roleRepository;
        this.userUpdateMapper = userUpdateMapper;
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
                .orElseThrow(() -> new SingleErrorException(ErrorCode.USERNAME_NOT_EXISTED));
        if (userEntity != null || userEntity.getAvatar().length != 0)
            return userEntity.getAvatar();
        return null;
    }

    public Map<Object, Object> validateUserUpdate(BindingResult bindingResult) {
        return bindingResult.getFieldErrors()
                .stream().collect(Collectors.toMap(FieldError::getField,
                        e -> ErrorCode.valueOf(e.getDefaultMessage()).getMessages(), (exist, replace) -> exist));
    }

    public UserUpdateResponse editUserInfo(String username, UserUpdateRequest userUpdateRequest,
            BindingResult bindingResult, MultipartFile file) {
        // all error catch from UserUpdateRequest
        Map<Object, Object> errors = validateUserUpdate(bindingResult);
        // real user;
        UserEntity userEntity = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new SingleErrorException(ErrorCode.USERNAME_NOT_EXISTED));
        // before update, check all fields, if error -> throw, if empty -> not update,
        if (userUpdateRequest.getPassword() != null)
            if (!userUpdateRequest.getPassword().equals(userUpdateRequest.getRepassword()))
                errors.put("repassword", ErrorCode.PASSWORD_CONFIRM.getMessages());
        if (!errors.isEmpty())
            throw new ListErrorException(ErrorCode.UPDATE_PROFILE_ERROR, errors);
        UserEntity userEntityConverted = this.userUpdateMapper.toEntity(userUpdateRequest);
        for (Field field : userEntityConverted.getClass().getDeclaredFields()) {
            if (field != null) {
                field.setAccessible(true);
                try {
                    Object object = field.get(userEntityConverted);
                    if (object != null) {
                        String setterName = "set" + field.getName().substring(0, 1).toUpperCase()
                                + field.getName().substring(1);
                        Method method = userEntity.getClass().getMethod(setterName, field.getType());
                        method.invoke(userEntity, object);
                    }
                } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException
                        | InvocationTargetException e) {
                }
            }
        }
        // isActivate is not exists, develop it.
        if (file != null && !file.isEmpty())
            try {
                userEntity.setAvatar(file.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        this.userRepository.save(userEntity);

        return this.userUpdateMapper.toResponseDTO(userEntity);
    }
}
