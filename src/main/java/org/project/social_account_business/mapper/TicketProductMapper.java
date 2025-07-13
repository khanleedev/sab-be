package org.project.social_account_business.mapper;

import org.project.social_account_business.dto.ticket_product.ShortenTicketProductDto;
import org.project.social_account_business.dto.ticket_product.TicketProductDto;
import org.project.social_account_business.dto.ticket_product_info.TicketProductInfoDto;
import org.project.social_account_business.form.ticket_product.CreateTicketProductForm;
import org.project.social_account_business.form.ticket_product.UpdateTicketProductForm;
import org.project.social_account_business.model.TicketProduct;
import org.mapstruct.*;
import org.project.social_account_business.model.TicketProductInfo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TicketProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "maxPurchasePerAccount", source = "maxPurchasePerAccount")
    @BeanMapping(ignoreByDefault = true)
    TicketProduct fromCreateTicketProductFormToEntity(CreateTicketProductForm createTicketProductForm);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "itemCode", source = "itemCode")
    @Mapping(target = "ticket", source = "ticket")
    @Mapping(target = "maxPurchasePerAccount", source = "maxPurchasePerAccount")
    @Named("fromEntityToDto")
    TicketProductDto fromEntityToDto(TicketProduct ticketProduct);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "itemCode", source = "itemCode")
    @Named("fromEntityToShortenDto")
    ShortenTicketProductDto fromEntityToShortenDto(TicketProduct ticketProduct);

    @IterableMapping(qualifiedByName = "fromEntityToDto", elementTargetType = TicketProductDto.class)
    List<TicketProductDto> fromEntitiesToDtos(List<TicketProduct> ticketProducts);

    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "maxPurchasePerAccount", source = "maxPurchasePerAccount")
    @BeanMapping(ignoreByDefault = true)
    void updateTicketProductFromCreateTicketProductForm(@MappingTarget TicketProduct ticketProduct, UpdateTicketProductForm updateTicketProductForm);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "uid", source = "uid")
    @Mapping(target = "pass", source = "pass")
    @Mapping(target = "twoFA", source = "twoFA")
    @Mapping(target = "mail", source = "mail")
    @Mapping(target = "passMail", source = "passMail")
    @Mapping(target = "mailVerify", source = "mailVerify")
    @Mapping(target = "isSold", source = "isSold")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToTicketProductInfoDto")
    TicketProductInfoDto fromEntityToTicketProductInfoDto(TicketProductInfo ticketProductInfo);

    @IterableMapping(qualifiedByName = "fromEntityToTicketProductInfoDto", elementTargetType = TicketProductInfoDto.class)
    List<TicketProductInfoDto> fromEntitiesToTicketProductInfoDtos(List<TicketProductInfo> ticketProductInfos);
}
