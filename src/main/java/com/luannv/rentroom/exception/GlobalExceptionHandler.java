package com.luannv.rentroom.exception;

import com.luannv.rentroom.dto.response.ApiResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
    @ExceptionHandler(ValueException.class)
    public ResponseEntity<ApiResponse> handleValueException(ValueException ve) {
        ApiResponse apiResponse = new ApiResponse();
        ErrorCode errorCode = ve.getErrorCode();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessages(errorCode.getMessages());
        return ResponseEntity.badRequest().body(apiResponse);
    }
//    @ExceptionHandler(ValueException.class)
//    public ResponseEntity<ApiResponse> handleValueException(ValueException ve) {
//        ApiResponse apiResponse = new ApiResponse();
//        ErrorCode errorCode = ve.getErrorCode();
//        apiResponse.setCode(errorCode.getCode());
//        apiResponse.setMessages(errorCode.getMessages());
//        return null;
//    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ApiResponse> handleInputException(MethodArgumentNotValidException re) {
//        String enumKey = re.getFieldError().getDefaultMessage();
//        ApiResponse apiResponse = new ApiResponse();
//        try {
//            ErrorCode errorCode = ErrorCode.valueOf(enumKey);
//            apiResponse.setCode(errorCode.getCode());
//            apiResponse.setMessages(errorCode.getMessages());
//        } catch(Exception e) {
//            apiResponse.setCode(ErrorCode.ENUM_ISNOTEXIST.getCode());
//            apiResponse.setMessages(ErrorCode.ENUM_ISNOTEXIST.getMessages());
//        }
//        return ResponseEntity.badRequest().body(apiResponse);
//    }
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse> handleInputException(IllegalStateException ise) {
        String enumKey = ise.getMessage();
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
