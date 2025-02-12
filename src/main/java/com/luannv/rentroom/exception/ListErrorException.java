package com.luannv.rentroom.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ListErrorException extends RuntimeException {
    private final ErrorCode errorCode;
    private final Map<?, ?> errors;
    public ListErrorException(ErrorCode errorCode, Map<?, ?> errors) {
        super(errorCode.getMessages());
        this.errorCode = errorCode;
        this.errors = errors;
    }

}
