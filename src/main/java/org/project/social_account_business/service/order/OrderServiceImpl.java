package org.project.social_account_business.service.order;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.project.social_account_business.constant.ErrorCode;
import org.project.social_account_business.dto.ResponseListDto;
import org.project.social_account_business.dto.order.OrderDto;
import org.project.social_account_business.dto.order.ShortenOrderDto;
import org.project.social_account_business.exception.BadRequestException;
import org.project.social_account_business.exception.NotFoundException;
import org.project.social_account_business.form.order.CreateOrderForm;
import org.project.social_account_business.mapper.OrderMapper;
import org.project.social_account_business.model.Order;
import org.project.social_account_business.model.TicketProduct;
import org.project.social_account_business.model.TicketProductInfo;
import org.project.social_account_business.model.criteria.OrderCriteria;
import org.project.social_account_business.repository.OrderRepository;
import org.project.social_account_business.repository.TicketProductInfoRepository;
import org.project.social_account_business.service.account.AccountService;
import org.project.social_account_business.service.ticket_product.TicketProductService;
import org.project.social_account_business.service.transaction.TransactionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("orderService")
@Slf4j
public class OrderServiceImpl implements OrderService {
    final OrderRepository orderRepository;
    final OrderMapper orderMapper;
    final AccountService accountService;
    final TicketProductService ticketProductService;
    final TransactionService transactionService;
    final TicketProductInfoRepository ticketProductInfoRepository;

    public OrderServiceImpl(OrderRepository orderRepository, OrderMapper orderMapper, AccountService accountService, TicketProductService ticketProductService, TransactionService transactionService, TicketProductInfoRepository ticketProductInfoRepository) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.accountService = accountService;
        this.ticketProductService = ticketProductService;
        this.transactionService = transactionService;
        this.ticketProductInfoRepository = ticketProductInfoRepository;
    }

    @Override
    @Transactional
    public List<TicketProductInfo> createOrder(CreateOrderForm createOrderForm) {
        log.info("[OrderService] Creating order with userId: {}", createOrderForm.getAccountId());
        if (createOrderForm.getQuantity() <= 0) {
            throw new BadRequestException("[OrderService] Quantity must be greater than 0", ErrorCode.ORDER_QUANTITY_INVALID);
        }
        val ticketProduct = ticketProductService.findById(createOrderForm.getTicketProductId());
        if (ticketProduct.getQuantity() < createOrderForm.getQuantity()) {
            throw new BadRequestException("[OrderService] Not enough tickets available", ErrorCode.TICKET_PRODUCT_NOT_ENOUGH);
        }

        val account = accountService.findById(createOrderForm.getAccountId());
        double totalPrice = ticketProduct.getPrice().doubleValue() * createOrderForm.getQuantity();

        if (account.getBalance() < totalPrice) {
            throw new BadRequestException("[OrderService] Not enough balance", ErrorCode.ACOUNT_NOT_ENOUGH_BALANCE);
        }
        if (checkIfTotalTicketProductIsOutOfLimitation(account.getId(), createOrderForm.getQuantity(), ticketProduct)) {
            throw new BadRequestException("[OrderService] Exceeded max purchase limit", ErrorCode.TICKET_PRODUCT_MAX_PURCHASE);
        }
        ticketProduct.setQuantity(ticketProduct.getQuantity() - createOrderForm.getQuantity());
        ticketProductService.save(ticketProduct);
        account.setBalance(account.getBalance() - totalPrice);

        val order = orderMapper.fromCreateOrderFormToEntity(createOrderForm);
        order.setTotalPrice(totalPrice);
        order.setAccount(account);
        order.setTicketProduct(ticketProduct);
        orderRepository.save(order);
        List<TicketProductInfo> randomInfos = ticketProductInfoRepository
                .findAvailableRandomInfos(ticketProduct.getId(), createOrderForm.getQuantity());

        if (randomInfos.size() < createOrderForm.getQuantity()) {
            throw new BadRequestException("[OrderService] Not enough ticket available!", ErrorCode.TICKET_PRODUCT_INFO_NOT_ENOUGH);
        }

        randomInfos.forEach(info -> info.setIsSold(true));
        ticketProductInfoRepository.saveAll(randomInfos);

        transactionService.createTransactionForOrder(order, randomInfos);
        return randomInfos;
    }


    @Override
    @Transactional(readOnly = true)
    public OrderDto getOrder(Long orderId) {
        return orderRepository.findFirstById(orderId)
                .map(orderMapper::fromEntityToDto)
                .orElseThrow(() -> new NotFoundException("[OrderService] Order not found!", ErrorCode.ORDER_NOT_FOUND));
    }

    @Override
    public ShortenOrderDto getShortenOrder(Long orderId) {
        return orderMapper.toShortenOrderDto(orderRepository.findFirstById(orderId).orElseThrow(
                () -> new NotFoundException("[OrderService] Order not found!", ErrorCode.ORDER_NOT_FOUND)));
    }

    @Override
    public void deleteOrder(Long orderId) {
        val order = orderRepository.findFirstById(orderId)
                .orElseThrow(() -> new NotFoundException("[OrderService] Order not found!", ErrorCode.ORDER_NOT_FOUND));
        orderRepository.delete(order);
    }

    @Override
    public ResponseListDto<List<OrderDto>> getOrders(OrderCriteria orderCriteria, Pageable pageable) {
        Page<Order> orders = orderRepository.findAll(orderCriteria.getSpecification(), pageable);
        return new ResponseListDto<>(orderMapper.fromEntitiesToDtos(orders.getContent()), orders.getTotalElements(), orders.getTotalPages());
    }

    private boolean checkIfTotalTicketProductIsOutOfLimitation(Long accountId, Integer quantity, TicketProduct ticketProduct) {
        int totalPurchased = orderRepository.getTotalPurchasedQuantity(accountId, ticketProduct.getId());
        return totalPurchased + quantity > ticketProduct.getMaxPurchasePerAccount();
    }
}
