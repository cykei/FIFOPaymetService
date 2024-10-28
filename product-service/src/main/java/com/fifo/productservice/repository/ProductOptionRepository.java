package com.fifo.productservice.repository;

import com.fifo.productservice.entity.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductOptionRepository extends JpaRepository<ProductOption, Long>, ProductOptionRepositoryCustom {
}
