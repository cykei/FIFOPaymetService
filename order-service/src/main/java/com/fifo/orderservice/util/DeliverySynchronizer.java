package com.fifo.orderservice.util;

import com.fifo.orderservice.entity.Order;
import com.fifo.orderservice.enums.OrderStatus;
import com.fifo.orderservice.repository.OrderRepository;
import com.fifo.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliverySynchronizer {
    private final OrderRepository orderRepository;
    private final OrderService orderService;

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void synchronize() {

        // 주문완료 D+1 -> 배송중
        List<Order> completedOrders = orderRepository.findByOrderStatus(OrderStatus.ORDER_COMPLETE);
        for (Order order : completedOrders) {
            order.updateOrderStatus(OrderStatus.DELIVERY_NOW);
        }
        orderRepository.saveAll(completedOrders);

        // 주문완료 D+2 -> 배송완료
        List<Order> deliveryOrders = orderRepository.findByOrderStatus(OrderStatus.DELIVERY_NOW);
        for (Order order : deliveryOrders) {
            order.updateOrderStatus(OrderStatus.DELIVERY_COMPLETE);
        }
        orderRepository.saveAll(deliveryOrders);

        // 반품 신청 후 D+1 -> 재고반영 + 반품완료
        List<Order> returnOrders = orderRepository.findByOrderStatus(OrderStatus.RETURN_REQUEST);
        for (Order order : returnOrders) {
            orderService.restoreStock(order);
        }
        orderRepository.saveAll(returnOrders);
    }
}
