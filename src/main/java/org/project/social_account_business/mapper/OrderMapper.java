package org.project.social_account_business.mapper;

import org.mapstruct.*;
import org.project.social_account_business.dto.order.OrderDto;
import org.project.social_account_business.dto.order.ShortenOrderDto;
import org.project.social_account_business.form.order.CreateOrderForm;
import org.project.social_account_business.model.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
uses = {AccountMapper.class, TicketProductMapper.class, TransactionMapper.class})
public interface OrderMapper {
    @Mapping(target = "quantity", source = "quantity")
    @BeanMapping(ignoreByDefault = true)
    Order fromCreateOrderFormToEntity(CreateOrderForm createOrderForm);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "ticketProduct", source = "ticketProduct", qualifiedByName = "fromEntityToShortenDto")
    @Mapping(target = "account", source = "account", qualifiedByName = "fromEntityToAutoCompleteAccountDto")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "totalPrice", source = "totalPrice")
    @Named("fromEntityToDto")
    OrderDto fromEntityToDto(Order order);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "ticketProduct", source = "ticketProduct", qualifiedByName = "fromEntityToShortenDto")
    @Mapping(target = "account", source = "account", qualifiedByName = "fromEntityToAutoCompleteAccountDto")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "totalPrice", source = "totalPrice")
    @Named("fromEntityToShortenDto")
    ShortenOrderDto toShortenOrderDto(Order order);

    @IterableMapping(qualifiedByName = "fromEntityToDto", elementTargetType = OrderDto.class)
    List<OrderDto> fromEntitiesToDtos(List<Order> orders);
}
