package com.luannv.rentroom.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum Error {
    UNCATEGORIZED_EXCEPTION(-1, "Uncategorized error.", HttpStatus.BAD_REQUEST),
    ENUM_ISNOTEXIST(-2, "Admin: Enum does not exist.", HttpStatus.BAD_REQUEST),
    FIELD_NOT_BLANK(3001, "The field can not be blank.", HttpStatus.BAD_REQUEST),
    ROLE_NOTFOUND(1000, "Role not found.", HttpStatus.BAD_REQUEST),
    ROLE_INVALID(1001, "Role can not be empty!", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(2000, "Password must be at least 8 characters!", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(3000, "Username is not valid.", HttpStatus.BAD_REQUEST),
    FILE_HANDLE(-3, "File handle error.", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID(4000, "Email is not valid.", HttpStatus.BAD_REQUEST)
    ;
    private final Integer code;
    private final String messages;
    private final HttpStatus httpStatus;
}
