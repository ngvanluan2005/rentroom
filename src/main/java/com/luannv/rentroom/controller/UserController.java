package com.luannv.rentroom.controller;

import com.luannv.rentroom.dto.request.UserRequestDTO;
import com.luannv.rentroom.dto.response.ApiResponse;
import com.luannv.rentroom.dto.response.UserResponseDTO;
import com.luannv.rentroom.entity.UserEntity;
import com.luannv.rentroom.exception.ErrorCode;
import com.luannv.rentroom.repository.UserRepository;
import com.luannv.rentroom.service.UserService;
import jakarta.validation.Valid;
import jdk.jfr.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    public static String getCurrentRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() ||
        authentication instanceof AnonymousAuthenticationToken)
            return null;
        return authentication.getAuthorities().iterator().next().getAuthority();
    }
    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

//    @PostMapping
//    public ApiResponse<UserResponseDTO> createUser(@Valid @ModelAttribute UserRequestDTO userRequestDTO, @RequestParam(value = "file", required = false) MultipartFile multipartFile) {
//        ApiResponse apiResponse = new ApiResponse();
//        UserResponseDTO userResponseDTO = this.userService.addUser(userRequestDTO, multipartFile);
//        apiResponse.setCode(HttpStatus.OK.value());
//        apiResponse.setResult(userResponseDTO);
//        return apiResponse;
//    }

    @PostMapping(consumes = {"application/json", "multipart/form-data"})
    public ApiResponse<UserResponseDTO, ?> createUser(@RequestParam(value = "file", required = false) MultipartFile file,
                                                   @Valid @ModelAttribute UserRequestDTO userRequestDTO) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setResult(this.userService.addUser(userRequestDTO, file));
        return apiResponse;
    }
    @GetMapping
    public List<UserResponseDTO> getAllUser() {
        System.out.println(getCurrentRole());
        return this.userService.getAll();
    }
    @GetMapping("/{username}")
    public UserResponseDTO getUserByUsername(@PathVariable String username) {
        return this.userService.getUserByUsername(username);
    }
    // put after have validation
    @DeleteMapping("/{username}")
    public ApiResponse<String, ?> deleteUserByUsername(@PathVariable String username) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            this.userService.deleteByUsername(username);
            apiResponse.setCode(HttpStatus.OK.value());
            apiResponse.setMessages("Delete User: " + username + " success!");
        } catch (Exception e) {
            apiResponse.setCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessages(ErrorCode.USER_DELETE_FAIL);
        }

        return apiResponse;
    }
}
