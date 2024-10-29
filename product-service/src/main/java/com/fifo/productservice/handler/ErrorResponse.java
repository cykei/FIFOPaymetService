package com.fifo.productservice.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ErrorResponse {
    private String requestUrl;
    private String message;
}