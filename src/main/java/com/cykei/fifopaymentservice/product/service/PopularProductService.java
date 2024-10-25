package com.cykei.fifopaymentservice.product.service;

import com.cykei.fifopaymentservice.order.OrderProduct;
import com.cykei.fifopaymentservice.order.repository.OrderProductRepository;
import com.cykei.fifopaymentservice.product.dto.ProductResponse;
import com.cykei.fifopaymentservice.product.mapper.ProductMapper;
import com.cykei.fifopaymentservice.product.repository.ProductRepository;
import com.cykei.fifopaymentservice.product.repository.dto.ProductDto;
import com.cykei.fifopaymentservice.wish.Wish;
import com.cykei.fifopaymentservice.wish.repository.WishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PopularProductService {

    private final OrderProductRepository orderProductRepository;
    private final WishRepository wishRepository;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public List<ProductResponse> getPopularProducts(int size) {

        LocalDateTime referenceDate = LocalDateTime.now().minusDays(7);
        // 1. 최근 일주일동안 사람들이 주문한 Product 를 가져온다.
        List<OrderProduct> orderProducts = orderProductRepository.findByCreatedAtAfter(referenceDate);

        // 2. 최근 일주일동안 사람들이 찜목록에 넣은 Product 를 가져온다.
        List<Wish> wishes = wishRepository.findByCreatedAtAfter(referenceDate);

        // 3. productId 에 대해 주문상품 * 2 + 찜목록 상품 * 1 의 가중치로 인기도를 산정하여 상위 size개만 가져온다.
        List<Long> productIds = calculatePopularScore(orderProducts, wishes, size);

        // 4. ProductResponse 를 만든다.
        List<ProductDto> products = productRepository.findProductDtosByProductIdIn(productIds);

        RedisTemplate<String, String> template = new RedisTemplate<>();

        return productMapper.toResponses(products);
    }

    List<Long> calculatePopularScore(List<OrderProduct> orderProducts, List<Wish> wishes, int size) {
        Map<Long, Long> orderProductMap = orderProducts.stream()
                .collect(Collectors.groupingBy(OrderProduct::getProductId, Collectors.counting()));
        Map<Long, Long> wishMap = wishes.stream()
                .collect(Collectors.groupingBy(Wish::getProductId, Collectors.counting()));

        Map<Long, Long> popularityMap = new HashMap<>();

        // 주문 상품 가중치 계산 (2배)
        for (Map.Entry<Long, Long> entry : orderProductMap.entrySet()) {
            Long productId = entry.getKey();
            Long orderScore = entry.getValue() * 2;

            popularityMap.put(productId, orderScore);
        }

        // 찜목록 가중치 계산 (1배)
        for (Map.Entry<Long, Long> entry : wishMap.entrySet()) {
            Long productId = entry.getKey();
            Long wishScore = entry.getValue(); // 찜목록은 가중치 1배

            popularityMap.merge(productId, wishScore, Long::sum);
        }

        // 4. 인기도 순으로 정렬해서 productId 반환
        return popularityMap.entrySet()
                .stream()
                .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
                .limit(size)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
