package com.luannv.rentroom.dto.response;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private String userId;
    private String username;
    private String fullName;
    private String email;
    private byte[] avatar;
    private String address;
    private int roleId = 3;
}
