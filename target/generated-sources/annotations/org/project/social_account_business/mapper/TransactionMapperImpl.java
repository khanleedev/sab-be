package org.project.social_account_business.mapper;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.project.social_account_business.dto.transaction.TransactionDto;
import org.project.social_account_business.dto.transaction.TransactionForOrderDto;
import org.project.social_account_business.dto.transaction.TransactionForPaymentDto;
import org.project.social_account_business.model.Transaction;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-05T15:57:26+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.9 (Oracle Corporation)"
)
@Component
public class TransactionMapperImpl implements TransactionMapper {

    @Override
    public TransactionDto fromEntityToDto(Transaction transaction) {
        if ( transaction == null ) {
            return null;
        }

        TransactionDto transactionDto = new TransactionDto();

        transactionDto.setId( transaction.getId() );
        transactionDto.setTransactionId( transaction.getTransactionId() );
        transactionDto.setAmountInCoin( transaction.getAmountInCoin() );
        transactionDto.setAmountInCash( transaction.getAmountInCash() );
        transactionDto.setTransactionCode( transaction.getTransactionCode() );
        if ( transaction.getOrderStatus() != null ) {
            transactionDto.setOrderStatus( transaction.getOrderStatus().name() );
        }
        if ( transaction.getTransactionType() != null ) {
            transactionDto.setTransactionType( transaction.getTransactionType().name() );
        }
        transactionDto.setStatus( transaction.getStatus() );
        if ( transaction.getModifiedDate() != null ) {
            transactionDto.setModifiedDate( LocalDateTime.ofInstant( transaction.getModifiedDate().toInstant(), ZoneId.of( "UTC" ) ) );
        }
        if ( transaction.getCreatedDate() != null ) {
            transactionDto.setCreatedDate( LocalDateTime.ofInstant( transaction.getCreatedDate().toInstant(), ZoneId.of( "UTC" ) ) );
        }
        transactionDto.setCreatedBy( transaction.getCreatedBy() );

        return transactionDto;
    }

    @Override
    public TransactionForOrderDto toEntityToDtoForOrder(Transaction transaction) {
        if ( transaction == null ) {
            return null;
        }

        TransactionForOrderDto transactionForOrderDto = new TransactionForOrderDto();

        transactionForOrderDto.setId( transaction.getId() );
        transactionForOrderDto.setAmountInCoin( transaction.getAmountInCoin() );
        if ( transaction.getOrderStatus() != null ) {
            transactionForOrderDto.setOrderStatus( transaction.getOrderStatus().name() );
        }
        transactionForOrderDto.setStatus( transaction.getStatus() );
        if ( transaction.getModifiedDate() != null ) {
            transactionForOrderDto.setModifiedDate( LocalDateTime.ofInstant( transaction.getModifiedDate().toInstant(), ZoneId.of( "UTC" ) ) );
        }
        if ( transaction.getCreatedDate() != null ) {
            transactionForOrderDto.setCreatedDate( LocalDateTime.ofInstant( transaction.getCreatedDate().toInstant(), ZoneId.of( "UTC" ) ) );
        }
        transactionForOrderDto.setCreatedBy( transaction.getCreatedBy() );

        return transactionForOrderDto;
    }

    @Override
    public TransactionForPaymentDto toEntityToDtoForPayment(Transaction transaction) {
        if ( transaction == null ) {
            return null;
        }

        TransactionForPaymentDto transactionForPaymentDto = new TransactionForPaymentDto();

        transactionForPaymentDto.setId( transaction.getId() );
        if ( transaction.getAmountInCash() != null ) {
            transactionForPaymentDto.setAmountInCash( transaction.getAmountInCash().doubleValue() );
        }
        if ( transaction.getOrderStatus() != null ) {
            transactionForPaymentDto.setOrderStatus( transaction.getOrderStatus().name() );
        }
        transactionForPaymentDto.setStatus( transaction.getStatus() );
        if ( transaction.getModifiedDate() != null ) {
            transactionForPaymentDto.setModifiedDate( LocalDateTime.ofInstant( transaction.getModifiedDate().toInstant(), ZoneId.of( "UTC" ) ) );
        }
        if ( transaction.getCreatedDate() != null ) {
            transactionForPaymentDto.setCreatedDate( LocalDateTime.ofInstant( transaction.getCreatedDate().toInstant(), ZoneId.of( "UTC" ) ) );
        }
        transactionForPaymentDto.setCreatedBy( transaction.getCreatedBy() );

        return transactionForPaymentDto;
    }

    @Override
    public List<TransactionDto> fromEntitiesToDtos(List<Transaction> transactions) {
        if ( transactions == null ) {
            return null;
        }

        List<TransactionDto> list = new ArrayList<TransactionDto>( transactions.size() );
        for ( Transaction transaction : transactions ) {
            list.add( fromEntityToDto( transaction ) );
        }

        return list;
    }

    @Override
    public List<TransactionForOrderDto> toEntitiesToDtosForOrder(List<Transaction> transactions) {
        if ( transactions == null ) {
            return null;
        }

        List<TransactionForOrderDto> list = new ArrayList<TransactionForOrderDto>( transactions.size() );
        for ( Transaction transaction : transactions ) {
            list.add( toEntityToDtoForOrder( transaction ) );
        }

        return list;
    }

    @Override
    public List<TransactionForPaymentDto> toEntitiesToDtosForPayment(List<Transaction> transactions) {
        if ( transactions == null ) {
            return null;
        }

        List<TransactionForPaymentDto> list = new ArrayList<TransactionForPaymentDto>( transactions.size() );
        for ( Transaction transaction : transactions ) {
            list.add( toEntityToDtoForPayment( transaction ) );
        }

        return list;
    }
}
