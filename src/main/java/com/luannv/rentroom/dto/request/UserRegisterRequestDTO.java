package com.luannv.rentroom.dto.request;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import static com.luannv.rentroom.constants.FieldConstants.PASSWORD_PATTERN;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegisterRequestDTO {
    @NotEmpty(message = "FIELD_NOT_BLANK")
    @Pattern(regexp = "^[A-Za-z0-9]{3,15}$", message = "USERNAME_INVALID")
    private String username;
    @NotEmpty(message = "FIELD_NOT_BLANK")
    private String firstName;
    @NotEmpty(message = "FIELD_NOT_BLANK")
    private String lastName;
    @NotEmpty(message = "FIELD_NOT_BLANK")
    @Email(message = "EMAIL_INVALID")
    private String email;
    @Lob
    private byte[] avatar;
//    private int isActivate = 0;
    private String address;
    @NotEmpty(message = "FIELD_NOT_BLANK")
    @Pattern(regexp = PASSWORD_PATTERN, message = "PASSWORD_INVALID")
    private String password;
    private int roleId = 3;
}
