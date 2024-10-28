package com.fifo.orderservice.mapper;

import com.fifo.orderservice.service.dto.OrderDetailResponse;
import com.fifo.orderservice.service.dto.OrderResponse;
import com.fifo.orderservice.entity.OrderProduct;
import com.fifo.orderservice.entity.Order;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring")
public interface OrderMapper {

    @Named("toResponse")
    OrderResponse toResponse(Order order);

    @IterableMapping(qualifiedByName = "toResponse")
    List<OrderResponse> toResponses(List<Order> orders);

    @Named("toDetailResponse")
    OrderDetailResponse toDetailResponse(OrderProduct orderProduct);

    @IterableMapping(qualifiedByName = "toDetailResponse")
    List<OrderDetailResponse> toDetailResponses(List<OrderProduct> orderProducts);
}
