package com.cykei.fifopaymentservice.product.repository;

import com.cykei.fifopaymentservice.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom{
}
