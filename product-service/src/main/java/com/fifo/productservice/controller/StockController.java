package com.fifo.productservice.controller;

import com.fifo.productservice.service.StockService;
import com.fifo.productservice.service.dto.StockResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/stock")
@RequiredArgsConstructor
public class StockController {
    private final StockService stockService;

    @GetMapping("initialize")
    public ResponseEntity<String> initialize() {
        String message = stockService.initialize();
        return ResponseEntity.ok(message);
    }

    @GetMapping("all")
    public ResponseEntity<List<StockResponse>> getAllStock() {
        return stockService.getAllStock();
    }

}
