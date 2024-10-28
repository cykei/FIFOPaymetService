package com.fifo.productservice.client.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class WishResponse {
    private long wishId;
    private long userId;
    private long productId;
    private LocalDateTime createdAt;
}
