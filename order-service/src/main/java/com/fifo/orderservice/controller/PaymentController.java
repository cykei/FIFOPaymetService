package com.fifo.orderservice.controller;

import com.fifo.orderservice.config.UserId;
import com.fifo.orderservice.service.PaymentService;
import com.fifo.orderservice.service.dto.PaymentRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<String> processPayment(@RequestBody @Valid PaymentRequest request, @UserId Long userId) {
        if (Math.random() < 0.2) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Payment failed");
        }
        return paymentService.processPayment(request, userId);
    }
}
