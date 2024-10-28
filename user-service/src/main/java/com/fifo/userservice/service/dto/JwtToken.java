package com.fifo.userservice.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class JwtToken {
    private String grantType;
    private String accessToken;
    private String refreshToken;
}
