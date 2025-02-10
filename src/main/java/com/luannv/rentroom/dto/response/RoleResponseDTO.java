package com.luannv.rentroom.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class RoleResponseDTO {
    private int id;
    private String name;
}
