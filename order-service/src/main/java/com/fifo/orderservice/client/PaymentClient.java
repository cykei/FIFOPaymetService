package com.fifo.orderservice.client;

import com.fifo.orderservice.service.dto.PaymentRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PaymentClient {
    public void payment(long orderId, PaymentRequest paymentRequest) {
      log.info("{} payment successed. ", orderId);
    }
}
