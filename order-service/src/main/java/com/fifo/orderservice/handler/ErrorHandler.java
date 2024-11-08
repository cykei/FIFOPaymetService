package com.fifo.orderservice.handler;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResponse handler(HttpServletRequest request, IllegalArgumentException e) {
        log.error("errorHandler - Illegal: {}", e.getMessage());
        return new ErrorResponse(request.getRequestURI(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handler(HttpServletRequest request, Exception e) {
        log.error("errorHandler - Exception: {}", e.getMessage());
        return new ErrorResponse(request.getRequestURI(), e.getLocalizedMessage());
    }


}
