package com.cykei.fifopaymentservice.product.service;

import com.cykei.fifopaymentservice.product.dto.ProductDto;
import com.cykei.fifopaymentservice.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    ProductRepository productRepository;

    @Test
    void getProducts() {
        List<ProductDto> products = productRepository.findProductsByCategoryId(1L, null, 10);
        assertThat(products.size()).isEqualTo(4);
        assertThat(products.get(0).getProductId()).isEqualTo(5);
        assertThat(products.get(0).getOptions()).containsExactlyInAnyOrder(new String[] {"그레이", "베이지"});

    }
}