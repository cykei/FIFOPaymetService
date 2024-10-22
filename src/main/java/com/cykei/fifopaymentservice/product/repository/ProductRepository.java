package com.cykei.fifopaymentservice.product.repository;

import com.cykei.fifopaymentservice.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {

    @Override
    Optional<Product> findById(Long productId);

    List<Product> findProductsByProductIdIn(List<Long> productIds);
}
