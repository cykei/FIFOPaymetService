package com.cykei.fifopaymentservice.user.controller;

import com.cykei.fifopaymentservice.user.service.UserService;
import com.cykei.fifopaymentservice.user.service.dto.JwtToken;
import com.cykei.fifopaymentservice.user.service.dto.UserRequest;
import com.cykei.fifopaymentservice.user.service.dto.UserSignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("signup")
    public void signUp(@RequestBody UserSignUpRequest request){
        userService.signUp(request);
    }

    @PostMapping("login")
    public JwtToken login(@RequestBody UserRequest request) {
        return userService.login(request);
    }

//    @PostMapping("update-password")
//    public void updatePassword(@RequestBody UserRequest request) {
//        userService.updatePassword(request);
//    }

    @PostMapping("test")
    public String test() {
        return "success";
    }
}
