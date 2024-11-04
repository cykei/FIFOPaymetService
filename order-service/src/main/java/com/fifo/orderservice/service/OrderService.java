package com.fifo.orderservice.service;


import com.fifo.common.dto.PagingResponse;
import com.fifo.orderservice.client.ProductClient;
import com.fifo.orderservice.client.dto.ProductOptionResponse;
import com.fifo.orderservice.entity.Order;
import com.fifo.orderservice.entity.OrderProduct;
import com.fifo.orderservice.entity.Payment;
import com.fifo.orderservice.enums.OrderStatus;
import com.fifo.orderservice.enums.PaymentType;
import com.fifo.orderservice.mapper.OrderMapper;
import com.fifo.orderservice.repository.OrderProductRepository;
import com.fifo.orderservice.repository.OrderRepository;
import com.fifo.orderservice.repository.PaymentRepository;
import com.fifo.orderservice.service.dto.OrderCreateRequest;
import com.fifo.orderservice.service.dto.OrderDetailResponse;
import com.fifo.orderservice.service.dto.OrderRequest;
import com.fifo.orderservice.service.dto.OrderResponse;
import com.fifo.orderservice.util.OrderValidator;
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
    private final PaymentRepository paymentRepository;
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
    public ResponseEntity<Long> createOrder(OrderCreateRequest orderCreateRequest) {
        Map<Long, OrderRequest> orderedProductMap = mapOrderRequests(orderCreateRequest);
        Map<Long, ProductOptionResponse> productOptionMap = mapProductOptions(orderedProductMap.keySet());

        // 1. 주문서 위조 여부 검증
        int totalPrice = calculateTotalPrice(orderCreateRequest, productOptionMap);
        OrderValidator.validatePrice(totalPrice, orderCreateRequest);

        // 2. 재고차감
        List<OrderRequest> successOptionIds = lockAndDecreaseStock(orderedProductMap);

        // 3. Order, Payment 데이터 생성
        try {
            long orderId = createOrderRecord(orderCreateRequest, totalPrice);
            saveOrderProducts(orderCreateRequest, orderId, productOptionMap);
            createPayment(orderId, orderCreateRequest.getPaymentType());
            return ResponseEntity.ok(orderId);
        } catch (Exception e) {
            // 예상가능한 예외상황 : db 용량부족으로 order 저장 못하는 경우
            restoreStock(successOptionIds);
            throw new RuntimeException("주문 처리 중 에러 발생", e);
        }
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

    private Map<Long, OrderRequest> mapOrderRequests(OrderCreateRequest orderCreateRequest) {
        return orderCreateRequest.getOrderRequests().stream()
                .collect(Collectors.toMap(OrderRequest::getOptionId, Function.identity()));
    }

    private Map<Long, ProductOptionResponse> mapProductOptions(Set<Long> optionIds) {
        List<ProductOptionResponse> productOptions = productClient.findProductOptionsIn(optionIds);
        return productOptions.stream()
                .collect(Collectors.toMap(ProductOptionResponse::getOptionId, Function.identity()));
    }

    private int calculateTotalPrice(OrderCreateRequest orderCreateRequest, Map<Long, ProductOptionResponse> productOptionMap) {
        return orderCreateRequest.getOrderRequests().stream()
                .mapToInt(orderRequest -> productOptionMap.get(orderRequest.getOptionId()).getFinalProductPrice() * orderRequest.getCount())
                .sum();
    }

    private List<OrderRequest> lockAndDecreaseStock(Map<Long, OrderRequest> orderedProductMap) {
        List<OrderRequest> successOptionIds = new ArrayList<>();
        for (Long productOptionId : orderedProductMap.keySet()) {
            RLock lock = redissonClient.getLock(String.format(PRODUCT_LOCK_FORMAT, productOptionId));
            try {
                if (lock.tryLock(5, 1, TimeUnit.SECONDS)) {
                    log.info("락을 획득했다.{}", productOptionId);
                    boolean stockDecreased = decreaseStock(productOptionId, orderedProductMap.get(productOptionId));
                    if (stockDecreased) {
                        successOptionIds.add(orderedProductMap.get(productOptionId));
                    }
                }
            } catch (Exception e) {
                handleStockException(e, successOptionIds, "재고부족으로 요청 실패");
            } finally {
                try {
                    lock.unlock();
                    log.info("락을 해제했다: {}", productOptionId);
                } catch (Exception e) {
                    handleStockException(e, successOptionIds, "락 해제 실패");
                }
            }
        }
        return successOptionIds;
    }

    private boolean decreaseStock(Long productOptionId, OrderRequest orderRequest) {
        ResponseEntity<Boolean> result = productClient.decreaseStock(productOptionId, orderRequest.getCount());
        if (Boolean.TRUE.equals(result.getBody())) {
            return true;
        } else {
            throw new IllegalArgumentException("재고가 부족합니다.");
        }
    }
    private void handleStockException(Exception e, List<OrderRequest> successOptionIds, String message) {
        restoreStock(successOptionIds);
        log.warn(message, e);
        throw new RuntimeException(e);
    }

    private long createOrderRecord(OrderCreateRequest orderCreateRequest, int totalPrice) {
        Order order = new Order(
                orderCreateRequest.getUserId(),
                orderCreateRequest.getOrderAddress(),
                totalPrice,
                orderCreateRequest.getPaymentType());
        return orderRepository.save(order).getOrderId();
    }

    private void saveOrderProducts(OrderCreateRequest orderCreateRequest, long orderId, Map<Long, ProductOptionResponse> productOptionMap) {
        List<OrderProduct> orderProducts = orderCreateRequest.getOrderRequests().stream()
                .map(orderRequest -> new OrderProduct(
                        orderId,
                        productOptionMap.get(orderRequest.getOptionId()).getProductId(),
                        orderRequest.getOptionId(),
                        orderRequest.getCount(),
                        productOptionMap.get(orderRequest.getOptionId()).getFinalProductPrice()))
                .collect(Collectors.toList());
        orderProductRepository.saveAll(orderProducts);
    }

    private void createPayment(long orderId, PaymentType paymentType) {
        Payment payment = new Payment(orderId, paymentType);
        paymentRepository.save(payment);
    }

}
