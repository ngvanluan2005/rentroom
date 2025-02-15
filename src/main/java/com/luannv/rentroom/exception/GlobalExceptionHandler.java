package com.luannv.rentroom.exception;

import com.luannv.rentroom.dto.response.ApiResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleException(RuntimeException re) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        apiResponse.setMessages(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessages());
        return ResponseEntity.badRequest().body(apiResponse);
    }
    @ExceptionHandler(ListErrorException.class)
    public ResponseEntity<ApiResponse<Integer, Map<?, ?>>> handleValueException(ListErrorException ve) {
        ApiResponse<Integer, Map<?, ?>> apiResponse = new ApiResponse<>();
        apiResponse.setCode(HttpStatus.BAD_REQUEST.value());
        apiResponse.setMessages(ve.getErrors());
        return ResponseEntity.badRequest().body(apiResponse);
    }
    @ExceptionHandler(SingleErrorException.class)
    public ResponseEntity<ApiResponse<Integer, String>> handleValueException(SingleErrorException see) {
        ApiResponse<Integer, String> apiResponse = new ApiResponse<>();
        apiResponse.setCode(HttpStatus.BAD_REQUEST.value());
        apiResponse.setMessages(see.getErrorCode().getMessages());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse> handleInputException(IllegalStateException ise) {
        String enumKey = ise.getMessage();
        ApiResponse apiResponse = new ApiResponse();
        try {
            ErrorCode errorCode = ErrorCode.valueOf(enumKey);
            apiResponse.setCode(errorCode.getCode());
            apiResponse.setMessages(errorCode.getMessages());
            return ResponseEntity.status(errorCode.getHttpStatus()).body(apiResponse);
        } catch(Exception e) {
            apiResponse.setCode(ErrorCode.ENUM_ISNOTEXIST.getCode());
            apiResponse.setMessages(ErrorCode.ENUM_ISNOTEXIST.getMessages());
        }
        return ResponseEntity.status(ErrorCode.ENUM_ISNOTEXIST.getHttpStatus()).body(apiResponse);
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse> handleInputException(DataIntegrityViolationException dive) {
        System.out.println(dive.getCause());
        String enumKey = dive.getMessage();
        ApiResponse apiResponse = new ApiResponse();
        try {
            ErrorCode errorCode = ErrorCode.valueOf(enumKey);
            apiResponse.setCode(errorCode.getCode());
            apiResponse.setMessages(errorCode.getMessages());
        } catch(Exception e) {
            apiResponse.setCode(ErrorCode.ENUM_ISNOTEXIST.getCode());
            apiResponse.setMessages(ErrorCode.ENUM_ISNOTEXIST.getMessages());
        }
        return ResponseEntity.badRequest().body(apiResponse);
    }
}
