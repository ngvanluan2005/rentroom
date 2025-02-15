package com.luannv.rentroom.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(-1, "Uncategorized error.", HttpStatus.INTERNAL_SERVER_ERROR),
    ENUM_ISNOTEXIST(-2, "Admin: Enum does not exist.", HttpStatus.BAD_REQUEST),
    FILE_HANDLE(-3, "File handle error.", HttpStatus.INTERNAL_SERVER_ERROR),

    FIELD_NOT_BLANK(1001, "The field can not be blank.", HttpStatus.BAD_REQUEST),
    ROLE_NOTFOUND(1002, "Role not found.", HttpStatus.NOT_FOUND),
    ROLE_INVALID(1003, "Role can not be empty!", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1004, "Password must be at least 8 characters!", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1005, "Username is not valid.", HttpStatus.BAD_REQUEST),
    USERNAME_EXISTED(1006, "Username has been used.", HttpStatus.CONFLICT),
    USERNAME_NOT_EXISTED(1007, "User not found.", HttpStatus.NOT_FOUND),
    USER_DELETE_FAIL(1008, "Failed to delete user.", HttpStatus.INTERNAL_SERVER_ERROR),
    EMAIL_INVALID(1009, "Email is not valid.", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTED(2000, "Email has been used.", HttpStatus.CONFLICT),
    VALIDATION_FAILED(2001, "Validation failed", HttpStatus.BAD_REQUEST),
    LOGIN_FAILED(2002, "Invalid username or password", HttpStatus.UNAUTHORIZED),
    ;
    private final Integer code;
    private final String messages;
    private final HttpStatus httpStatus;
}
