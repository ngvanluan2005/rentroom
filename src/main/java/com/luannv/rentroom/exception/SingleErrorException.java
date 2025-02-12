package com.luannv.rentroom.exception;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
@Getter
public class SingleErrorException extends RuntimeException{
    private  final ErrorCode errorCode;

    @Autowired
    public SingleErrorException(ErrorCode errorCode) {
        super(errorCode.getMessages());
        this.errorCode = errorCode;
    }

}
