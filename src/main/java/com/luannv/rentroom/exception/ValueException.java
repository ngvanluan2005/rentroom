package com.luannv.rentroom.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValueException extends RuntimeException {
    private ErrorCode errorCode;
    public ValueException(ErrorCode errorCode) {
        super(errorCode.getMessages());
        this.errorCode = errorCode;
    }
}
