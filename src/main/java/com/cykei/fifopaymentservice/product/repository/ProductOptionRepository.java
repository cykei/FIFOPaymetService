package com.cykei.fifopaymentservice.product.repository;

import com.cykei.fifopaymentservice.product.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductOptionRepository extends JpaRepository<ProductOption, Long>, ProductOptionRepositoryCustom {
}
