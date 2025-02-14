package com.luannv.rentroom.dto.request;

import com.luannv.rentroom.entity.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.time.LocalDateTime;

import static com.luannv.rentroom.constants.FieldConstants.PASSWORD_PATTERN;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegisterRequestDTO {
    @NotEmpty(message = "FIELD_NOT_BLANK")
    @Pattern(regexp = "^(?![_-])[A-Za-z0-9_-]{3,15}(?<![-_])$", message = "USERNAME_INVALID")
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
