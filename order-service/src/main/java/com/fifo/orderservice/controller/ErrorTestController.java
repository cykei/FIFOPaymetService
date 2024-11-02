package com.fifo.orderservice.controller;

import com.fifo.orderservice.client.ProductClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ErrorTestController {
    private final ProductClient testClient;

    @GetMapping("/test/1")
    public ResponseEntity<String> test() {
        return testClient.case1();
    }

    @GetMapping("/test/2")
    public ResponseEntity<String> test2() {
        return testClient.case2();
    }

    @GetMapping("/test/3")
    public ResponseEntity<String> test3() {
        return testClient.case3();
    }


}
