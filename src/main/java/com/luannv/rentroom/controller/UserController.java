package com.luannv.rentroom.controller;

import com.luannv.rentroom.dto.request.UserRequestDTO;
import com.luannv.rentroom.dto.response.ApiResponse;
import com.luannv.rentroom.dto.response.UserResponseDTO;
import com.luannv.rentroom.repository.UserRepository;
import com.luannv.rentroom.service.UserService;
import jakarta.validation.Valid;
import jdk.jfr.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
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
    public ApiResponse<?> createUser(@RequestParam(value = "file", required = false) MultipartFile file,
                                                   @Valid @ModelAttribute UserRequestDTO userRequestDTO, BindingResult bindingResult) {
        ApiResponse apiResponse = new ApiResponse();
        if (bindingResult.hasErrors()) {
            System.out.println(">> BUG: " + bindingResult.getFieldError().getField() + " " + bindingResult.getFieldError().getDefaultMessage());
        } else {
            System.out.println("NO bug");
        }
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setResult(this.userService.addUser(userRequestDTO, file));
        return apiResponse;
    }
    @GetMapping
    public List<UserResponseDTO> getAllUser() {
        return this.userService.getAll();
    }
}
