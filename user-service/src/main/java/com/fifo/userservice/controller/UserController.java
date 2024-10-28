package com.fifo.userservice.controller;

import com.fifo.userservice.config.UserId;
import com.fifo.userservice.service.UserService;
import com.fifo.userservice.service.dto.JwtToken;
import com.fifo.userservice.service.dto.UserRequest;
import com.fifo.userservice.service.dto.UserSignUpRequest;
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

    @PostMapping("update-password")
    public void updatePassword(@UserId Long userId, @RequestBody UserRequest request) {
        userService.updatePassword(userId, request);
    }

    @PostMapping("test")
    public String test() {
        return "success";
    }
}
