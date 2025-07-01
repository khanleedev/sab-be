package org.project.social_account_business.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.project.social_account_business.dto.ApiResponse;
import org.project.social_account_business.dto.ResponseListDto;
import org.project.social_account_business.dto.order.OrderDto;
import org.project.social_account_business.exception.MyBindingException;
import org.project.social_account_business.form.order.CreateOrderForm;
import org.project.social_account_business.model.criteria.OrderCriteria;
import org.project.social_account_business.service.order.OrderService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/v1/orders")
@Slf4j
public class OrderController extends ABasicController{
    final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponse<Long>> createOrder(@RequestBody @Valid CreateOrderForm createOrderForm, BindingResult bindingResult) {
        log.info("Creating order");
        if (bindingResult.hasErrors()) {
            if (bindingResult.hasErrors()) {
                throw new MyBindingException("[AccountController] " + Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
            }
        }
        orderService.createOrder(createOrderForm);
        return ResponseEntity.ok().body(new ApiResponse<>(HttpStatus.CREATED, "Order created successfully"));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<ResponseListDto<List<OrderDto>>>> listOrders(OrderCriteria orderCriteria, Pageable pageable) {
        return ResponseEntity.ok().body(new ApiResponse<>(HttpStatus.OK, "Get list order successfully!", orderService.getOrders(orderCriteria, pageable)));
    }
}
