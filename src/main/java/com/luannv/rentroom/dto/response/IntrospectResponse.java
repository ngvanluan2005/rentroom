package com.luannv.rentroom.dto.response;

import lombok.*;

@Setter
@Getter // get status response to set http status
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IntrospectResponse {
    private boolean valid;
}
