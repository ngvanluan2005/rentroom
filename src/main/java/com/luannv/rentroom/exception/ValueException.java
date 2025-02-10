package com.luannv.rentroom.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValueException extends RuntimeException {
    private Error error;
    public ValueException(Error error) {
        super(error.getMessages());
        this.error = error;
    }
}
