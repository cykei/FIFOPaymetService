//package com.fifo.productservice.product.service;
//
//import com.cykei.fifopaymentservice.product.Product;
//import com.cykei.fifopaymentservice.product.ProductOption;
//import org.mockito.Mockito;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static org.mockito.Mockito.when;
//
//public class ProductFixture {
//    public static Product createProduct() {
//        Product product = Mockito.spy(new Product());
//        when(product.getProductId()).thenReturn(1L);
//        when(product.getName()).thenReturn("Test Product");
//        when(product.getImage()).thenReturn("test-image-url");
//        when(product.getPrice()).thenReturn(10000);
//
//        ProductOption option1 = Mockito.spy(new ProductOption());
//        when(option1.getId()).thenReturn(1L);
//        when(option1.getOptionName()).thenReturn("Option 1");
//
//        ProductOption option2 = Mockito.spy(new ProductOption());
//        when(option2.getId()).thenReturn(2L);
//        when(option2.getOptionName()).thenReturn("Option 2");
//
//        List<ProductOption> productOptions = Arrays.asList(option1, option2);
//        when(product.getProductOptions()).thenReturn(productOptions);
//
//        return product;
//    }
//}
