package org.project.social_account_business.mapper;

import org.mapstruct.*;
import java.util.List;
import org.project.social_account_business.dto.payment_transaction.PaymentTransactionDto;
import org.project.social_account_business.dto.payment_transaction.ShortenPaymentTransactionDto;
import org.project.social_account_business.form.payment_transaction.CreatePaymentTransactionForm;
import org.project.social_account_business.model.PaymentTransaction;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {AccountMapper.class, TransactionMapper.class})
public interface PaymentTransactionMapper {
    @Mapping(target = "gateway", source = "gateway")
    @Mapping(target = "accountNumber", source = "accountNumber")
    @Mapping(target = "transactionDate", source = "transactionDate")
    @Mapping(target = "amountIn", source = "amountIn")
    @Mapping(target = "accumulated", source = "accumulated")
    @Mapping(target = "code", source = "code")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "referenceNumber", source = "referenceNumber")
    @Mapping(target = "sepayTransactionId", source = "sepayTransactionId")
    @Mapping(target = "callbackUrl", source = "callbackUrl")
    @BeanMapping(ignoreByDefault = true)
    PaymentTransaction fromCreatePaymentTransactionFormToEntity(CreatePaymentTransactionForm createPaymentTransactionForm);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "gateway", source = "gateway")
    @Mapping(target = "transactionDate", source = "transactionDate")
    @Mapping(target = "accountNumber", source = "accountNumber")
    @Mapping(target = "account", source = "account", qualifiedByName = "fromEntityToAutoCompleteAccountDto")
    @Mapping(target = "amountIn", source = "amountIn")
    @Mapping(target = "accumulated", source = "accumulated")
    @Mapping(target = "code", source = "code")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "referenceNumber", source = "referenceNumber")
    @Mapping(target = "callbackUrl", source = "callbackUrl")
    @Mapping(target = "sepayTransactionId", source = "sepayTransactionId")
    @Named("fromEntityToDto")
    PaymentTransactionDto fromEntityToDto(PaymentTransaction paymentTransaction);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "transactionDate", source = "transactionDate")
    @Mapping(target = "accountNumber", source = "accountNumber")
    @Mapping(target = "referenceNumber", source = "referenceNumber")
    @Mapping(target = "sepayTransactionId", source = "sepayTransactionId")
    @Mapping(target = "callbackUrl", source = "callbackUrl")
    @Named("fromEntityToShortenDto")
    ShortenPaymentTransactionDto fromEntityToShortenDto(PaymentTransaction paymentTransaction);

    @IterableMapping(qualifiedByName = "fromEntityToDto", elementTargetType = PaymentTransactionDto.class)
    List<PaymentTransactionDto> fromEntitiesToDtos(List<PaymentTransaction> paymentTransactions);
}
