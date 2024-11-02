package com.fifo.orderservice.service;


import com.fifo.common.dto.PagingResponse;
import com.fifo.orderservice.client.ProductClient;
import com.fifo.orderservice.client.dto.ProductOptionResponse;
import com.fifo.orderservice.entity.Order;
import com.fifo.orderservice.entity.OrderProduct;
import com.fifo.orderservice.enums.OrderStatus;
import com.fifo.orderservice.mapper.OrderMapper;
import com.fifo.orderservice.repository.OrderProductRepository;
import com.fifo.orderservice.repository.OrderRepository;
import com.fifo.orderservice.service.dto.OrderCreateRequest;
import com.fifo.orderservice.service.dto.OrderDetailResponse;
import com.fifo.orderservice.service.dto.OrderRequest;
import com.fifo.orderservice.service.dto.OrderResponse;
import com.fifo.orderservice.util.OrderValidator;
import io.lettuce.core.RedisException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final OrderMapper orderMapper;

    private final ProductClient productClient;
    private final RedissonClient redissonClient;

    private final String PRODUCT_LOCK_FORMAT = "stock:productOption:%d";

    public PagingResponse<OrderResponse> getOrders(Long cursor, int size, long userId) {
        List<Order> orders = orderRepository.getOrders(userId, size, cursor);
        if (orders.isEmpty()) {
            return null;
        }

        Long nextCursor = null;
        if (orders.size() == size) {
            nextCursor = orders.get(orders.size() - 1).getOrderId();
        }

        return new PagingResponse<>(
                orders.size(),
                nextCursor,
                orderMapper.toResponses(orders)
        );
    }

    public List<OrderDetailResponse> getOrderDetail(long orderId, long userId) {
        Order order = getOrder(orderId);
        OrderValidator.validateUser(order, userId);

        List<OrderProduct> orderProducts = orderProductRepository.findByOrderId(orderId);
        return orderMapper.toDetailResponses(orderProducts);
    }


    @Transactional
    public long createOrder(OrderCreateRequest orderCreateRequest) {
        Map<Long, OrderRequest> orderedProductMap = orderCreateRequest.getOrderRequests().stream()
                .collect(Collectors.toMap(OrderRequest::getOptionId, Function.identity()));

        Set<Long> optionIds = orderedProductMap.keySet();

        log.info("optionIds: {}", optionIds);
        // 주문한 상품 가격조회
        List<ProductOptionResponse> productOptions = productClient.findProductOptionsIn(optionIds);

        Map<Long, ProductOptionResponse> productOptionMap = productOptions.stream()
                .collect(Collectors.toMap(ProductOptionResponse::getOptionId, Function.identity()));

        int totalPrice = 0;
        for (OrderRequest orderRequest : orderCreateRequest.getOrderRequests()) {
            long optionId = orderRequest.getOptionId();
            long optionCount = orderRequest.getCount();
            int finalPrice = productOptionMap.get(optionId).getFinalProductPrice();
            totalPrice += finalPrice * optionCount;
        }

        OrderValidator.validatePrice(totalPrice, orderCreateRequest);

        // 2. orderRequest 로 받은 물건들 락 획득 및 재고 차감
        List<OrderRequest> successOptionIds = new ArrayList<>();
        for (Long productOptionId : orderedProductMap.keySet()) {

            RLock lock = redissonClient.getLock(String.format(PRODUCT_LOCK_FORMAT, productOptionId));
            try {
                // 5초 동안 획득하려고 시도하고, 최대 1초동안 점유한다.
                if (lock.tryLock(5, 60, TimeUnit.SECONDS)) {
                    log.info("락을 획득했다." + productOptionId);
                    // 락을 얻었으면 재고감소
                    ResponseEntity<Boolean> result = productClient.decreaseStock(productOptionId, orderedProductMap.get(productOptionId).getCount());
                    if (result.getBody().equals(Boolean.TRUE)) {
                        successOptionIds.add(orderedProductMap.get(productOptionId));
                        log.info("재고 감소 성공" + productOptionId);
                    } else {
                        // 재고가 이미 0이거나 다른 원인으로 실패함.
                        throw new IllegalArgumentException("재고가 없습니다.");
                    }
                }
            } catch (InterruptedException e) {
                log.warn("인테럽이 발생했습니다: ", e);
                restoreStock(successOptionIds);
                throw new RuntimeException(e);
            } catch (RedisException e ) {
                log.warn("Redis 관련 에러가 발생했습니다: ", e);
                restoreStock(successOptionIds);
                throw new RuntimeException(e);
            } catch (Exception e) {
                log.error("에러가 발생했습니다: ", e);
                restoreStock(successOptionIds);
                throw new RuntimeException(e);
            }
            finally {
                log.info("락을 해제했다." + lock);
                lock.unlock();
            }
        }

        // 3. 주문생성
        Order order = new Order(orderCreateRequest.getUserId(), orderCreateRequest.getOrderAddress(), totalPrice);
        long orderId = orderRepository.save(order).getOrderId();


        List<OrderProduct> orderProducts = new ArrayList<>();
        for (OrderRequest orderRequest : orderCreateRequest.getOrderRequests()) {
            long optionId = orderRequest.getOptionId();
            long productId = productOptionMap.get(optionId).getProductId();
            int optionCount = orderRequest.getCount();
            int finalPrice = productOptionMap.get(optionId).getFinalProductPrice();
            OrderProduct orderProduct = new OrderProduct(orderId, productId, optionId, optionCount, finalPrice);
            orderProducts.add(orderProduct);
        }

        orderProductRepository.saveAll(orderProducts);
        return orderId;
    }

    @Transactional
    public String cancelOrder(long orderId, OrderStatus orderStatus, long userId) {
        Order order = getOrder(orderId);
        OrderValidator.validateUser(order, userId);
        OrderValidator.validateOrderStatus(order, orderStatus);

        if (order.getOrderStatus().equals(OrderStatus.ORDER_COMPLETE)) {
            order.updateOrderStatus(OrderStatus.ORDER_CANCEL);
            restoreStock(order);
            return makeCancelMessage(OrderStatus.ORDER_CANCEL);
        }

        if (order.getOrderStatus().equals(OrderStatus.DELIVERY_NOW)) {
            if (order.getCreatedAt().isAfter(LocalDateTime.now().minusDays(1))) {
                order.updateOrderStatus(OrderStatus.RETURN_REQUEST);
            }
            return makeCancelMessage(OrderStatus.RETURN_REQUEST);
        }

        return "잘못된 요청입니다";
    }

    @Transactional
    public void restoreStock(Order order) {
        List<OrderProduct> returnOrderProducts = orderProductRepository.findByOrderId(order.getOrderId());
        for (OrderProduct orderProduct : returnOrderProducts) {
            productClient.increaseStock(orderProduct.getOptionId(), orderProduct.getProductCount());
        }
    }

    public void restoreStock(List<OrderRequest> orderRequests) {
        for (OrderRequest orderRequest : orderRequests) {
            productClient.increaseStock(orderRequest.getOptionId(), orderRequest.getCount());
        }
    }

    public List<OrderDetailResponse> findOrderProductsByCreatedAtAfter(LocalDateTime dateTime) {
        List<OrderProduct> orderProducts = orderProductRepository.findByCreatedAtAfter(dateTime);
        return orderMapper.toDetailResponses(orderProducts);
    }

    private Order getOrder(long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 요청입니다"));
    }

    private String makeCancelMessage(OrderStatus orderStatus) {
        return String.format("%s 되었습니다", orderStatus.getDescription());
    }
}
