package org.project.social_account_business.service.order;

import org.project.social_account_business.dto.ResponseListDto;
import org.project.social_account_business.dto.order.OrderDto;
import org.project.social_account_business.dto.order.ShortenOrderDto;
import org.project.social_account_business.form.order.CreateOrderForm;
import org.project.social_account_business.model.criteria.OrderCriteria;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    void createOrder(CreateOrderForm createOrderForm);

    OrderDto getOrder(Long orderId);

    ShortenOrderDto getShortenOrder(Long orderId);

    void deleteOrder(Long orderId);

    ResponseListDto<List<OrderDto>> getOrders(OrderCriteria orderCriteria, Pageable pageable);
}
