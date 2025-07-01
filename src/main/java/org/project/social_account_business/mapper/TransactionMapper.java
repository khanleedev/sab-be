package org.project.social_account_business.mapper;

import org.mapstruct.*;
import org.project.social_account_business.dto.transaction.TransactionDto;
import org.project.social_account_business.dto.transaction.TransactionForOrderDto;
import org.project.social_account_business.dto.transaction.TransactionForPaymentDto;
import org.project.social_account_business.model.Transaction;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {OrderMapper.class, PaymentTransactionMapper.class})
public interface TransactionMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "transactionId", source = "transactionId")
    @Mapping(target = "amountInCoin", source = "amountInCoin")
    @Mapping(target = "amountInCash", source = "amountInCash")
    @Mapping(target = "transactionCode", source = "transactionCode")
    @Mapping(target = "orderStatus", source = "orderStatus")
    @Mapping(target = "transactionType", source = "transactionType")
    @Named("fromEntityToDto")
    TransactionDto fromEntityToDto(Transaction transaction);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "amountInCoin", source = "amountInCoin")
    @Mapping(target = "orderStatus", source = "orderStatus")
    @Named("toEntityToDtoForOrder")
    TransactionForOrderDto toEntityToDtoForOrder(Transaction transaction);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "amountInCash", source = "amountInCash")
    @Mapping(target = "orderStatus", source = "orderStatus")
    @Named("toEntityToDtoForPayment")
    TransactionForPaymentDto toEntityToDtoForPayment(Transaction transaction);

    @IterableMapping(qualifiedByName = "fromEntityToDto", elementTargetType = TransactionDto.class)
    List<TransactionDto> fromEntitiesToDtos(List<Transaction> transactions);

    @IterableMapping(qualifiedByName = "toEntityToDtoForOrder", elementTargetType = TransactionForOrderDto.class)
    List<TransactionForOrderDto> toEntitiesToDtosForOrder(List<Transaction> transactions);

    @IterableMapping(qualifiedByName = "toEntityToDtoForPayment", elementTargetType = TransactionForPaymentDto.class)
    List<TransactionForPaymentDto> toEntitiesToDtosForPayment(List<Transaction> transactions);
}
