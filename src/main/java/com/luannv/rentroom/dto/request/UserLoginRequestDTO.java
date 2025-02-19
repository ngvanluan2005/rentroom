package com.luannv.rentroom.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class UserLoginRequestDTO {
    @NotEmpty(message = "FIELD_NOT_BLANK")
    private String username;
    @NotEmpty(message = "FIELD_NOT_BLANK")
    private String password;
}
