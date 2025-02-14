package com.luannv.rentroom.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class UserLoginRequestDTO {
    private String username;
    private String password;
}
