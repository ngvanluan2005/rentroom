package com.luannv.rentroom.controller;

import com.luannv.rentroom.dto.request.UserRequestDTO;
import com.luannv.rentroom.dto.response.ApiResponse;
import com.luannv.rentroom.dto.response.UserResponseDTO;
import com.luannv.rentroom.exception.ErrorCode;
import com.luannv.rentroom.exception.ListErrorException;
import com.luannv.rentroom.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

import static com.luannv.rentroom.constants.UrlConstants.API_USER;
import static com.luannv.rentroom.constants.UrlConstants.DEFAULT_AVATAR;

@RestController
@RequestMapping(API_USER)
public class UserController {

    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping(consumes = {"application/json", "multipart/form-data"})
    public ApiResponse<UserResponseDTO, ?> createUser(@RequestParam(value = "file", required = false) MultipartFile file,
                                                      @Valid @ModelAttribute UserRequestDTO userRequestDTO, BindingResult bindingResult) {
        Map<?, ?> additionalErrors = this.userService.validateUserRequest(bindingResult, userRequestDTO);
        if (bindingResult.hasErrors() || !additionalErrors.isEmpty()) {
            System.out.println(1);
            throw new ListErrorException(ErrorCode.VALIDATION_FAILED, additionalErrors);
        }
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setResult(this.userService.addUser(userRequestDTO, file));
        return apiResponse;
    }
    @GetMapping
    public List<UserResponseDTO> getAllUser() {
        return this.userService.getAll();
    }
    @GetMapping("/{username}/avatar")
    // ? in responseEntity like byte[] or null value
    public ResponseEntity<?> getUserAvatar(@PathVariable String username) {
        byte[] ref = this.userService.getUserAvatar(username);
        if (ref == null)
            return new ResponseEntity<>(DEFAULT_AVATAR, HttpStatus.OK);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "image/jpeg");
        return new ResponseEntity<>(ref, httpHeaders, HttpStatus.OK);
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
