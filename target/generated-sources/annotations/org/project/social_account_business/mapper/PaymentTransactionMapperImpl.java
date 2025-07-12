package org.project.social_account_business.mapper;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.project.social_account_business.dto.payment_transaction.PaymentTransactionDto;
import org.project.social_account_business.dto.payment_transaction.ShortenPaymentTransactionDto;
import org.project.social_account_business.form.payment_transaction.CreatePaymentTransactionForm;
import org.project.social_account_business.model.PaymentTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-13T00:52:44+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.9 (Oracle Corporation)"
)
@Component
public class PaymentTransactionMapperImpl implements PaymentTransactionMapper {

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public PaymentTransaction fromCreatePaymentTransactionFormToEntity(CreatePaymentTransactionForm createPaymentTransactionForm) {
        if ( createPaymentTransactionForm == null ) {
            return null;
        }

        PaymentTransaction.PaymentTransactionBuilder paymentTransaction = PaymentTransaction.builder();

        paymentTransaction.gateway( createPaymentTransactionForm.getGateway() );
        paymentTransaction.accountNumber( createPaymentTransactionForm.getAccountNumber() );
        paymentTransaction.transactionDate( createPaymentTransactionForm.getTransactionDate() );
        paymentTransaction.amountIn( createPaymentTransactionForm.getAmountIn() );
        paymentTransaction.accumulated( createPaymentTransactionForm.getAccumulated() );
        paymentTransaction.code( createPaymentTransactionForm.getCode() );
        paymentTransaction.description( createPaymentTransactionForm.getDescription() );
        paymentTransaction.referenceNumber( createPaymentTransactionForm.getReferenceNumber() );
        paymentTransaction.sepayTransactionId( createPaymentTransactionForm.getSepayTransactionId() );
        paymentTransaction.callbackUrl( createPaymentTransactionForm.getCallbackUrl() );

        return paymentTransaction.build();
    }

    @Override
    public PaymentTransactionDto fromEntityToDto(PaymentTransaction paymentTransaction) {
        if ( paymentTransaction == null ) {
            return null;
        }

        PaymentTransactionDto paymentTransactionDto = new PaymentTransactionDto();

        paymentTransactionDto.setId( paymentTransaction.getId() );
        paymentTransactionDto.setGateway( paymentTransaction.getGateway() );
        if ( paymentTransaction.getTransactionDate() != null ) {
            paymentTransactionDto.setTransactionDate( new SimpleDateFormat().format( paymentTransaction.getTransactionDate() ) );
        }
        paymentTransactionDto.setAccountNumber( paymentTransaction.getAccountNumber() );
        paymentTransactionDto.setAccount( accountMapper.fromEntityToAutoCompleteAccountDto( paymentTransaction.getAccount() ) );
        paymentTransactionDto.setAmountIn( paymentTransaction.getAmountIn() );
        paymentTransactionDto.setAccumulated( paymentTransaction.getAccumulated() );
        paymentTransactionDto.setCode( paymentTransaction.getCode() );
        paymentTransactionDto.setDescription( paymentTransaction.getDescription() );
        paymentTransactionDto.setReferenceNumber( paymentTransaction.getReferenceNumber() );
        paymentTransactionDto.setCallbackUrl( paymentTransaction.getCallbackUrl() );
        paymentTransactionDto.setSepayTransactionId( paymentTransaction.getSepayTransactionId() );
        paymentTransactionDto.setStatus( paymentTransaction.getStatus() );
        if ( paymentTransaction.getModifiedDate() != null ) {
            paymentTransactionDto.setModifiedDate( LocalDateTime.ofInstant( paymentTransaction.getModifiedDate().toInstant(), ZoneId.of( "UTC" ) ) );
        }
        if ( paymentTransaction.getCreatedDate() != null ) {
            paymentTransactionDto.setCreatedDate( LocalDateTime.ofInstant( paymentTransaction.getCreatedDate().toInstant(), ZoneId.of( "UTC" ) ) );
        }
        paymentTransactionDto.setCreatedBy( paymentTransaction.getCreatedBy() );

        return paymentTransactionDto;
    }

    @Override
    public ShortenPaymentTransactionDto fromEntityToShortenDto(PaymentTransaction paymentTransaction) {
        if ( paymentTransaction == null ) {
            return null;
        }

        ShortenPaymentTransactionDto shortenPaymentTransactionDto = new ShortenPaymentTransactionDto();

        shortenPaymentTransactionDto.setId( paymentTransaction.getId() );
        if ( paymentTransaction.getTransactionDate() != null ) {
            shortenPaymentTransactionDto.setTransactionDate( new SimpleDateFormat().format( paymentTransaction.getTransactionDate() ) );
        }
        shortenPaymentTransactionDto.setAccountNumber( paymentTransaction.getAccountNumber() );
        shortenPaymentTransactionDto.setReferenceNumber( paymentTransaction.getReferenceNumber() );
        shortenPaymentTransactionDto.setSepayTransactionId( paymentTransaction.getSepayTransactionId() );
        shortenPaymentTransactionDto.setCallbackUrl( paymentTransaction.getCallbackUrl() );

        return shortenPaymentTransactionDto;
    }

    @Override
    public List<PaymentTransactionDto> fromEntitiesToDtos(List<PaymentTransaction> paymentTransactions) {
        if ( paymentTransactions == null ) {
            return null;
        }

        List<PaymentTransactionDto> list = new ArrayList<PaymentTransactionDto>( paymentTransactions.size() );
        for ( PaymentTransaction paymentTransaction : paymentTransactions ) {
            list.add( fromEntityToDto( paymentTransaction ) );
        }

        return list;
    }
}
