package com.fifo.wishservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
public class Wish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wish_id", nullable = false)
    private long wishId;

    private long userId;

    private long productId;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
