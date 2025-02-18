package com.luannv.rentroom.controller;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.Map;

@RestController
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = "/register", consumes = {"application/json", "multipart/form-data"})
    public ApiResponse<UserResponseDTO, ?> createUser(@RequestParam(value = "file", required = false) MultipartFile file,
                                                      @Valid @ModelAttribute UserRegisterRequestDTO userRequestDTO, BindingResult bindingResult) {
        Map<?, ?> additionalErrors = this.authService.validateUserRequest(bindingResult, userRequestDTO);
        if (bindingResult.hasErrors() || !additionalErrors.isEmpty()) {
            System.out.println(1);
            throw new ListErrorException(ErrorCode.VALIDATION_FAILED, additionalErrors);
        }
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(HttpStatus.OK.value());
        apiResponse.setResult(this.authService.addUser(userRequestDTO, file));
        return apiResponse;
    }
    @PostMapping("/login")
    public ApiResponse<String, AuthenticationResponse> loginUser(@Valid @RequestBody UserLoginRequestDTO userLoginRequestDTO, BindingResult bindingResult) {
        AuthenticationResponse authenticationResponse = this.authService.loginUserValidate(userLoginRequestDTO, bindingResult);
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
