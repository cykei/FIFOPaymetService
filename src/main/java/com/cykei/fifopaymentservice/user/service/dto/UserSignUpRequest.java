package com.cykei.fifopaymentservice.user.service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignUpRequest {
    @Email
    @NotBlank
    private String email;

    @Size(min = 8, max = 12)
    @NotBlank
    private String password;

    @NotBlank
    private String name;

    private String address;

    private String phone;

}
