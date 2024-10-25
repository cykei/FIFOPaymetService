package com.cykei.fifopaymentservice.product.repository;

import com.cykei.fifopaymentservice.product.Product;
import com.cykei.fifopaymentservice.product.repository.dto.ProductDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @DisplayName("1번 카테고리 상품 조회")
    @Test
    void getProducts() {
        List<ProductDto> products = productRepository.findProductDtosByCategoryId(1L, null, 10);
        assertThat(products.size()).isEqualTo(4);
        assertThat(products.get(0).getProductId()).isEqualTo(5);
        assertThat(products.get(0).getOptions()).containsExactlyInAnyOrder("그레이", "베이지");
    }

    @DisplayName("개별조회시 ProductOption 매핑 검증")
    @Test
    void getProduct() {
        Product product = productRepository.findById(1L).orElseThrow();
        assertThat(product.getProductOptions()).isNotEmpty();
    }
}