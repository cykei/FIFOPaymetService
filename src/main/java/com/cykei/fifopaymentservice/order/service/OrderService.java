package com.cykei.fifopaymentservice.order.service;

import com.cykei.fifopaymentservice.order.Order;
import com.cykei.fifopaymentservice.order.OrderProduct;
import com.cykei.fifopaymentservice.order.dto.OrderCreateRequest;
import com.cykei.fifopaymentservice.order.dto.OrderDetailResponse;
import com.cykei.fifopaymentservice.order.dto.OrderRequest;
import com.cykei.fifopaymentservice.order.dto.PagingOrderResponse;
import com.cykei.fifopaymentservice.order.enums.OrderStatus;
import com.cykei.fifopaymentservice.order.mapper.OrderMapper;
import com.cykei.fifopaymentservice.order.repository.OrderProductRepository;
import com.cykei.fifopaymentservice.order.repository.OrderRepository;
import com.cykei.fifopaymentservice.product.repository.ProductOptionRepository;
import com.cykei.fifopaymentservice.product.repository.ProductRepository;
import com.cykei.fifopaymentservice.product.repository.dto.ProductOptionDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final ProductRepository productRepository;
    private final ProductOptionRepository productOptionRepository;
    private final OrderMapper orderMapper;

    public PagingOrderResponse getOrders(Long cursor, int size, long userId) {
        List<Order> orders = orderRepository.getOrders(userId, size, cursor);
        if (orders.isEmpty()) {
            return null;
        }

        Long nextCursor = null;
        if (orders.size() == size) {
            nextCursor = orders.get(orders.size() - 1).getOrderId();
        }

        return new PagingOrderResponse(
                orders.size(),
                nextCursor,
                orderMapper.toResponses(orders)
        );
    }

    public List<OrderDetailResponse> getOrderDetail(long orderId, long userId) {
        Order order = getOrder(orderId);
        validateUser(order, userId);

        List<OrderProduct> orderProducts = orderProductRepository.findByOrderId(orderId);
        return orderMapper.toDetailResponses(orderProducts);
    }


    @Transactional
    public long createOrder(OrderCreateRequest orderCreateRequest, long userId) {
        List<Long> optionIds = orderCreateRequest.getOrderRequests().stream()
                .map(OrderRequest::getOptionId)
                .collect(Collectors.toList());

        // 주문한 상품 가격조회
        List<ProductOptionDto> productOptions = productRepository.findProductOptionsIn(optionIds);
        Map<Long, ProductOptionDto> productOptionMap = productOptions.stream()
                .collect(Collectors.toMap(ProductOptionDto::getOptionId, Function.identity()));

        int totalPrice = 0;
        for (OrderRequest orderRequest : orderCreateRequest.getOrderRequests()) {
            long optionId = orderRequest.getOptionId();
            long optionCount = orderRequest.getCount();
            int finalPrice = productOptionMap.get(optionId).getFinalProductPrice();
            totalPrice += finalPrice * optionCount;
        }

        validatePrice(totalPrice, orderCreateRequest.getTotalPrice());


        Order order = new Order(userId, orderCreateRequest.getOrderAddress(), totalPrice);
        long orderId = orderRepository.save(order).getOrderId();


        List<OrderProduct> orderProducts = new ArrayList<>();
        for (OrderRequest orderRequest : orderCreateRequest.getOrderRequests()) {
            long optionId = orderRequest.getOptionId();
            long productId = productOptionMap.get(optionId).getProductId();
            int optionCount = orderRequest.getCount();
            int finalPrice = productOptionMap.get(optionId).getFinalProductPrice();
            OrderProduct orderProduct = new OrderProduct(orderId, productId, optionId, optionCount, finalPrice);
            orderProducts.add(orderProduct);
            productOptionRepository.decreaseStock(optionId, optionCount);
        }

        orderProductRepository.saveAll(orderProducts);
        return orderId;
    }

    @Transactional
    public String cancelOrder(long orderId, long userId) {
        Order order = getOrder(orderId);
        validateUser(order, userId);

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
            productOptionRepository.increaseStock(orderProduct.getOptionId(), orderProduct.getProductCount());
        }
    }

    private Order getOrder(long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 요청입니다"));
    }

    private void validateUser(Order order, long userId) {
        if (order.getUserId() != userId) throw new IllegalArgumentException("권한이 없습니다");
    }

    private void validatePrice(long expectedPrice, long sourcePrice) {
        if (expectedPrice != sourcePrice) throw new IllegalArgumentException("주문에 실패했습니다");
    }

    private String makeCancelMessage(OrderStatus orderStatus) {
        return String.format("%s 되었습니다", orderStatus.getDescription());
    }
}
