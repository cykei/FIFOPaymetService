package com.cykei.fifopaymentservice.order.mapper;

import com.cykei.fifopaymentservice.order.Order;
import com.cykei.fifopaymentservice.order.OrderProduct;
import com.cykei.fifopaymentservice.order.dto.OrderDetailResponse;
import com.cykei.fifopaymentservice.order.dto.OrderResponse;
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
