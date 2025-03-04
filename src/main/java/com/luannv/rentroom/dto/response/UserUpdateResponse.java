package com.luannv.rentroom.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUpdateResponse {
  private String username;
  private String fullName;
  private String email;
  private String avatar;
  private String address;
  private String roleName;
}
