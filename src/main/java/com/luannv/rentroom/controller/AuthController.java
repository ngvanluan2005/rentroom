package com.luannv.rentroom.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.luannv.rentroom.dto.request.IntrospectRequest;
import com.luannv.rentroom.dto.request.LogoutRequest;
import com.luannv.rentroom.dto.request.UserLoginRequestDTO;
import com.luannv.rentroom.dto.request.UserRegisterRequestDTO;
import com.luannv.rentroom.dto.response.ApiResponse;
import com.luannv.rentroom.dto.response.AuthenticationResponse;
import com.luannv.rentroom.dto.response.IntrospectResponse;
import com.luannv.rentroom.dto.response.UserResponseDTO;
import com.luannv.rentroom.exception.ErrorCode;
import com.luannv.rentroom.exception.ListErrorException;
import com.luannv.rentroom.exception.SingleErrorException;
import com.luannv.rentroom.service.AuthService;
import com.nimbusds.jose.JOSEException;

import jakarta.validation.Valid;

@RestController
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = "/register", consumes = { "multipart/form-data" })
    public ApiResponse<UserResponseDTO, ?> createUser(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @Valid @ModelAttribute UserRegisterRequestDTO userRequestDTO, BindingResult bindingResult) {
        if (file == null)
            System.err.println(">> file null");
        Map<?, ?> additionalErrors = this.authService.validateUserRequest(bindingResult, userRequestDTO);
        if (bindingResult.hasErrors() || !additionalErrors.isEmpty()) {
            throw new ListErrorException(ErrorCode.VALIDATION_FAILED, additionalErrors);
        }

        try {
            UserResponseDTO responseDTO = this.authService.addUser(userRequestDTO, file);
            ApiResponse<UserResponseDTO, ?> apiResponse = new ApiResponse<>();
            apiResponse.setCode(HttpStatus.OK.value());
            apiResponse.setResult(responseDTO);
            return apiResponse;

        } catch (IOException e) {
            throw new RuntimeException("Failed to process file", e);
        }
    }

    @PostMapping("/login")
    public ApiResponse<String, AuthenticationResponse> loginUser(
            @Valid @RequestBody UserLoginRequestDTO userLoginRequestDTO, BindingResult bindingResult) {
        AuthenticationResponse authenticationResponse = this.authService.loginUserValidate(userLoginRequestDTO);
        if (bindingResult.hasErrors() || authenticationResponse == null)
            throw new SingleErrorException(ErrorCode.LOGIN_FAILED);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setResult(authenticationResponse);
        return apiResponse;
    }

    @PostMapping("/logout")
    public ApiResponse<Long, String> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        this.authService.logout(request);
        return ApiResponse.<Long, String>builder()
                .messages("Logout success!")
                .result(System.currentTimeMillis())
                .build();
    }

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse, ?> verifyToken(@RequestBody IntrospectRequest introspectRequest) {
        IntrospectResponse introspectResponse = this.authService.introspect(introspectRequest);
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        String messages = "Invalid token.";
        if (introspectResponse.isValid()) {
            httpStatus = HttpStatus.OK;
            messages = "Valid token.";
        }
        return ApiResponse.<IntrospectResponse, String>builder()
                .result(introspectResponse)
                .code(httpStatus.value())
                .messages(messages)
                .build();
    }

}
