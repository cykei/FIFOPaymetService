package com.fifo.productservice.repository;

import com.fifo.productservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {

    @Override
    Optional<Product> findById(Long productId);
}
