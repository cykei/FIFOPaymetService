package com.fifo.productservice.service;

import com.fifo.productservice.entity.ProductOption;
import com.fifo.productservice.repository.ProductOptionRepository;
import com.fifo.productservice.service.dto.StockResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class StockService {

    private final ProductOptionRepository productOptionRepository;
    private final RedisTemplate<String, String> redisTemplate;

    public String initialize() {
        List<ProductOption> productOptionList = productOptionRepository.findAll();
        for (ProductOption productOption : productOptionList) {
            initializeStock(productOption.getId(), productOption.getProductCount());
        }
        return "상품재고 레디스 등록 완료";
    }

    private void initializeStock(Long productOptionId, int stock) {
        String key = String.format("stock:productOption:%d", productOptionId);
        redisTemplate.opsForValue().set(key, String.valueOf(stock));
    }

    public ResponseEntity<List<StockResponse>> getAllStock() {
        List<StockResponse> stockResponses = new ArrayList<>();
        List<ProductOption> productOptionList = productOptionRepository.findAll();
        for (ProductOption productOption : productOptionList) {
            Integer stockCount = Integer.parseInt(getStock(productOption.getId()));
            StockResponse stockResponse = new StockResponse(productOption.getId(), stockCount);
            stockResponses.add(stockResponse);
        }
        return ResponseEntity.ok(stockResponses);
    }

    private String getStock(Long productOptionId) {
        String key = String.format("stock:productOption:%d", productOptionId);
        return redisTemplate.opsForValue().get(key);
    }

    @Transactional
    @Scheduled(cron = "0 * * * * *")
    public void syncFromRedisToDB(){
        List<ProductOption> productOptionList = productOptionRepository.findAll();
        for (ProductOption productOption : productOptionList) {
            String result = getStock(productOption.getId());
            if (result == null) {
                log.warn("[CRITICAL ERROR] {} stock is null", productOption.getProductId());
                continue;
            }
            int stockCount = Integer.parseInt(result);
            productOption.updateProductCount(stockCount);
        }
    }
}
