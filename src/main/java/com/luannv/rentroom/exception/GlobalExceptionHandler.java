package com.luannv.rentroom.exception;

import com.luannv.rentroom.dto.response.ApiResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleException(RuntimeException re) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(Error.UNCATEGORIZED_EXCEPTION.getCode());
        apiResponse.setMessages(Error.UNCATEGORIZED_EXCEPTION.getMessages());
        return ResponseEntity.badRequest().body(apiResponse);
    }
    @ExceptionHandler(ValueException.class)
    public ResponseEntity<ApiResponse> handleValueException(ValueException ve) {
        ApiResponse apiResponse = new ApiResponse();
        Error error = ve.getError();
        apiResponse.setCode(error.getCode());
        apiResponse.setMessages(error.getMessages());
        return ResponseEntity.badRequest().body(apiResponse);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleInputException(MethodArgumentNotValidException re) {
        String enumKey = re.getFieldError().getDefaultMessage();
        ApiResponse apiResponse = new ApiResponse();
        try {
            Error error = Error.valueOf(enumKey);
            apiResponse.setCode(error.getCode());
            apiResponse.setMessages(error.getMessages());
        } catch(Exception e) {
            apiResponse.setCode(Error.ENUM_ISNOTEXIST.getCode());
            apiResponse.setMessages(Error.ENUM_ISNOTEXIST.getMessages());
        }
        return ResponseEntity.badRequest().body(apiResponse);
    }
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse> handleInputException(IllegalStateException ise) {
        String enumKey = ise.getMessage();
        ApiResponse apiResponse = new ApiResponse();
        try {
            Error error = Error.valueOf(enumKey);
            apiResponse.setCode(error.getCode());
            apiResponse.setMessages(error.getMessages());
        } catch(Exception e) {
            apiResponse.setCode(Error.ENUM_ISNOTEXIST.getCode());
            apiResponse.setMessages(Error.ENUM_ISNOTEXIST.getMessages());
        }
        return ResponseEntity.badRequest().body(apiResponse);
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse> handleInputException(DataIntegrityViolationException dive) {
        System.out.println(dive.getCause());
        String enumKey = dive.getMessage();
        ApiResponse apiResponse = new ApiResponse();
        try {
            Error error = Error.valueOf(enumKey);
            apiResponse.setCode(error.getCode());
            apiResponse.setMessages(error.getMessages());
        } catch(Exception e) {
            apiResponse.setCode(Error.ENUM_ISNOTEXIST.getCode());
            apiResponse.setMessages(Error.ENUM_ISNOTEXIST.getMessages());
        }
        return ResponseEntity.badRequest().body(apiResponse);
    }
}
