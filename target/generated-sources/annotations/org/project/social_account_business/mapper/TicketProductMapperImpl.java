package org.project.social_account_business.mapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.project.social_account_business.dto.ticket.TicketDto;
import org.project.social_account_business.dto.ticket_product.ShortenTicketProductDto;
import org.project.social_account_business.dto.ticket_product.TicketProductDto;
import org.project.social_account_business.dto.ticket_product_info.TicketProductInfoDto;
import org.project.social_account_business.form.ticket_product.CreateTicketProductForm;
import org.project.social_account_business.form.ticket_product.UpdateTicketProductForm;
import org.project.social_account_business.model.Ticket;
import org.project.social_account_business.model.TicketProduct;
import org.project.social_account_business.model.TicketProductInfo;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-05T18:51:23+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.9 (Oracle Corporation)"
)
@Component
public class TicketProductMapperImpl implements TicketProductMapper {

    @Override
    public TicketProduct fromCreateTicketProductFormToEntity(CreateTicketProductForm createTicketProductForm) {
        if ( createTicketProductForm == null ) {
            return null;
        }

        TicketProduct.TicketProductBuilder ticketProduct = TicketProduct.builder();

        ticketProduct.name( createTicketProductForm.getName() );
        ticketProduct.description( createTicketProductForm.getDescription() );
        ticketProduct.quantity( createTicketProductForm.getQuantity() );
        if ( createTicketProductForm.getPrice() != null ) {
            ticketProduct.price( BigDecimal.valueOf( createTicketProductForm.getPrice() ) );
        }
        ticketProduct.maxPurchasePerAccount( createTicketProductForm.getMaxPurchasePerAccount() );

        return ticketProduct.build();
    }

    @Override
    public TicketProductDto fromEntityToDto(TicketProduct ticketProduct) {
        if ( ticketProduct == null ) {
            return null;
        }

        TicketProductDto ticketProductDto = new TicketProductDto();

        ticketProductDto.setId( ticketProduct.getId() );
        ticketProductDto.setName( ticketProduct.getName() );
        ticketProductDto.setDescription( ticketProduct.getDescription() );
        ticketProductDto.setQuantity( ticketProduct.getQuantity() );
        if ( ticketProduct.getPrice() != null ) {
            ticketProductDto.setPrice( ticketProduct.getPrice().doubleValue() );
        }
        ticketProductDto.setItemCode( ticketProduct.getItemCode() );
        ticketProductDto.setTicket( ticketToTicketDto( ticketProduct.getTicket() ) );
        ticketProductDto.setMaxPurchasePerAccount( ticketProduct.getMaxPurchasePerAccount() );
        ticketProductDto.setStatus( ticketProduct.getStatus() );
        if ( ticketProduct.getModifiedDate() != null ) {
            ticketProductDto.setModifiedDate( LocalDateTime.ofInstant( ticketProduct.getModifiedDate().toInstant(), ZoneId.of( "UTC" ) ) );
        }
        if ( ticketProduct.getCreatedDate() != null ) {
            ticketProductDto.setCreatedDate( LocalDateTime.ofInstant( ticketProduct.getCreatedDate().toInstant(), ZoneId.of( "UTC" ) ) );
        }
        ticketProductDto.setCreatedBy( ticketProduct.getCreatedBy() );

        return ticketProductDto;
    }

    @Override
    public ShortenTicketProductDto fromEntityToShortenDto(TicketProduct ticketProduct) {
        if ( ticketProduct == null ) {
            return null;
        }

        ShortenTicketProductDto shortenTicketProductDto = new ShortenTicketProductDto();

        shortenTicketProductDto.setId( ticketProduct.getId() );
        shortenTicketProductDto.setName( ticketProduct.getName() );
        if ( ticketProduct.getPrice() != null ) {
            shortenTicketProductDto.setPrice( ticketProduct.getPrice().doubleValue() );
        }
        shortenTicketProductDto.setItemCode( ticketProduct.getItemCode() );

        return shortenTicketProductDto;
    }

    @Override
    public List<TicketProductDto> fromEntitiesToDtos(List<TicketProduct> ticketProducts) {
        if ( ticketProducts == null ) {
            return null;
        }

        List<TicketProductDto> list = new ArrayList<TicketProductDto>( ticketProducts.size() );
        for ( TicketProduct ticketProduct : ticketProducts ) {
            list.add( fromEntityToDto( ticketProduct ) );
        }

        return list;
    }

    @Override
    public void updateTicketProductFromCreateTicketProductForm(TicketProduct ticketProduct, UpdateTicketProductForm updateTicketProductForm) {
        if ( updateTicketProductForm == null ) {
            return;
        }

        if ( updateTicketProductForm.getName() != null ) {
            ticketProduct.setName( updateTicketProductForm.getName() );
        }
        if ( updateTicketProductForm.getDescription() != null ) {
            ticketProduct.setDescription( updateTicketProductForm.getDescription() );
        }
        if ( updateTicketProductForm.getQuantity() != null ) {
            ticketProduct.setQuantity( updateTicketProductForm.getQuantity() );
        }
        if ( updateTicketProductForm.getPrice() != null ) {
            ticketProduct.setPrice( BigDecimal.valueOf( updateTicketProductForm.getPrice() ) );
        }
        if ( updateTicketProductForm.getMaxPurchasePerAccount() != null ) {
            ticketProduct.setMaxPurchasePerAccount( updateTicketProductForm.getMaxPurchasePerAccount() );
        }
    }

    @Override
    public TicketProductInfoDto fromEntityToTicketProductInfoDto(TicketProductInfo ticketProductInfo) {
        if ( ticketProductInfo == null ) {
            return null;
        }

        TicketProductInfoDto ticketProductInfoDto = new TicketProductInfoDto();

        ticketProductInfoDto.setId( ticketProductInfo.getId() );
        ticketProductInfoDto.setUid( ticketProductInfo.getUid() );
        ticketProductInfoDto.setPass( ticketProductInfo.getPass() );
        ticketProductInfoDto.setTwoFA( ticketProductInfo.getTwoFA() );
        ticketProductInfoDto.setMail( ticketProductInfo.getMail() );
        ticketProductInfoDto.setPassMail( ticketProductInfo.getPassMail() );
        ticketProductInfoDto.setMailVerify( ticketProductInfo.getMailVerify() );
        ticketProductInfoDto.setIsSold( ticketProductInfo.getIsSold() );

        return ticketProductInfoDto;
    }

    @Override
    public List<TicketProductInfoDto> fromEntitiesToTicketProductInfoDtos(List<TicketProductInfo> ticketProductInfos) {
        if ( ticketProductInfos == null ) {
            return null;
        }

        List<TicketProductInfoDto> list = new ArrayList<TicketProductInfoDto>( ticketProductInfos.size() );
        for ( TicketProductInfo ticketProductInfo : ticketProductInfos ) {
            list.add( fromEntityToTicketProductInfoDto( ticketProductInfo ) );
        }

        return list;
    }

    protected TicketDto ticketToTicketDto(Ticket ticket) {
        if ( ticket == null ) {
            return null;
        }

        TicketDto ticketDto = new TicketDto();

        ticketDto.setStatus( ticket.getStatus() );
        if ( ticket.getModifiedDate() != null ) {
            ticketDto.setModifiedDate( LocalDateTime.ofInstant( ticket.getModifiedDate().toInstant(), ZoneId.of( "UTC" ) ) );
        }
        if ( ticket.getCreatedDate() != null ) {
            ticketDto.setCreatedDate( LocalDateTime.ofInstant( ticket.getCreatedDate().toInstant(), ZoneId.of( "UTC" ) ) );
        }
        ticketDto.setCreatedBy( ticket.getCreatedBy() );
        ticketDto.setId( ticket.getId() );
        ticketDto.setTitle( ticket.getTitle() );

        return ticketDto;
    }
}
