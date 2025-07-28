package org.project.social_account_business.mapper;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.project.social_account_business.dto.order.OrderDto;
import org.project.social_account_business.dto.order.ShortenOrderDto;
import org.project.social_account_business.form.order.CreateOrderForm;
import org.project.social_account_business.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-26T20:59:10+0700",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.42.50.v20250628-1110, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class OrderMapperImpl implements OrderMapper {

    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private TicketProductMapper ticketProductMapper;

    @Override
    public Order fromCreateOrderFormToEntity(CreateOrderForm createOrderForm) {
        if ( createOrderForm == null ) {
            return null;
        }

        Order.OrderBuilder order = Order.builder();

        order.quantity( createOrderForm.getQuantity() );

        return order.build();
    }

    @Override
    public OrderDto fromEntityToDto(Order order) {
        if ( order == null ) {
            return null;
        }

        OrderDto orderDto = new OrderDto();

        orderDto.setId( order.getId() );
        orderDto.setTicketProduct( ticketProductMapper.fromEntityToShortenDto( order.getTicketProduct() ) );
        orderDto.setAccount( accountMapper.fromEntityToAutoCompleteAccountDto( order.getAccount() ) );
        orderDto.setQuantity( order.getQuantity() );
        orderDto.setTotalPrice( order.getTotalPrice() );
        orderDto.setCreatedBy( order.getCreatedBy() );
        if ( order.getCreatedDate() != null ) {
            orderDto.setCreatedDate( LocalDateTime.ofInstant( order.getCreatedDate().toInstant(), ZoneId.of( "UTC" ) ) );
        }
        if ( order.getModifiedDate() != null ) {
            orderDto.setModifiedDate( LocalDateTime.ofInstant( order.getModifiedDate().toInstant(), ZoneId.of( "UTC" ) ) );
        }
        orderDto.setStatus( order.getStatus() );

        return orderDto;
    }

    @Override
    public ShortenOrderDto toShortenOrderDto(Order order) {
        if ( order == null ) {
            return null;
        }

        ShortenOrderDto shortenOrderDto = new ShortenOrderDto();

        shortenOrderDto.setId( order.getId() );
        shortenOrderDto.setTicketProduct( ticketProductMapper.fromEntityToShortenDto( order.getTicketProduct() ) );
        shortenOrderDto.setAccount( accountMapper.fromEntityToAutoCompleteAccountDto( order.getAccount() ) );
        shortenOrderDto.setQuantity( order.getQuantity() );
        shortenOrderDto.setTotalPrice( order.getTotalPrice() );

        return shortenOrderDto;
    }

    @Override
    public List<OrderDto> fromEntitiesToDtos(List<Order> orders) {
        if ( orders == null ) {
            return null;
        }

        List<OrderDto> list = new ArrayList<OrderDto>( orders.size() );
        for ( Order order : orders ) {
            list.add( fromEntityToDto( order ) );
        }

        return list;
    }
}
