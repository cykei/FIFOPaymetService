package com.cykei.fifopaymentservice.user.service.dto;

import com.cykei.fifopaymentservice.user.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignUpRequest {
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String name;

    private String address;
    private String phone;

    public User toEntity() {
        return new User(name, email, password, address, phone);
    }

}
