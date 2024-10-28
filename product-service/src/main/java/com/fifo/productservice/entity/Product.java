package com.fifo.productservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@ToString
@Getter
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", nullable = false)
    private long productId;

    private String name;

    private String image;

    private int price;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private List<ProductOption> productOptions;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
