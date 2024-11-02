package com.fifo.orderservice.service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryRequest {
    String address;
    String name;
    String phone;
    String memo;
}
