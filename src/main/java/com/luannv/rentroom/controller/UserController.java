package com.luannv.rentroom.controller;

import static com.luannv.rentroom.constants.UrlConstants.API_USER;
import static com.luannv.rentroom.constants.UrlConstants.DEFAULT_AVATAR;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.luannv.rentroom.dto.request.UserUpdateRequest;
import com.luannv.rentroom.dto.response.ApiResponse;
import com.luannv.rentroom.dto.response.UserResponseDTO;
import com.luannv.rentroom.dto.response.UserUpdateResponse;
import com.luannv.rentroom.exception.ErrorCode;
import com.luannv.rentroom.service.UserService;

import jakarta.validation.Valid;

@RestController

@RequestMapping(API_USER)
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    // @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
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
            apiResponse.setMessages(ErrorCode.USER_DELETE_FAIL.getMessages());
        }

        return apiResponse;
    }

    @PutMapping("/{username}")
    public ResponseEntity<ApiResponse<Object, String>> editUser(@PathVariable String username,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @Valid @ModelAttribute UserUpdateRequest userUpdateRequest, BindingResult bindingResult) {

        UserUpdateResponse userUpdateResponse = this.userService.editUserInfo(username, userUpdateRequest,
                bindingResult, file);
        ApiResponse<Object, String> apiResponse = ApiResponse.<Object, String>builder()
                .result(userUpdateResponse)
                .messages("Update success")
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
