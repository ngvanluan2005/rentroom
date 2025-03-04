package com.luannv.rentroom.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import static com.luannv.rentroom.constants.FieldConstants.PASSWORD_PATTERN;

@Setter
@Getter
public class UserUpdateRequest {
    @JsonProperty(access = Access.READ_ONLY)
    @JsonIgnore
    private String username;
    // @NotEmpty(message = "FIELD_NOT_BLANK")
    private String firstName;
    // @NotEmpty(message = "FIELD_NOT_BLANK")
    private String lastName;
    // @NotEmpty(message = "FIELD_NOT_BLANK")
    @Email(message = "EMAIL_INVALID")
    private String email;
    private String address;
    // @NotEmpty(message = "FIELD_NOT_BLANK")
    @Pattern(regexp = PASSWORD_PATTERN, message = "PASSWORD_INVALID")
    private String password;
    private String repassword;
    private int roleId;
}
