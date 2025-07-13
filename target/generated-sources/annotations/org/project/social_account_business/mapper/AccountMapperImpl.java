package org.project.social_account_business.mapper;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.project.social_account_business.dto.account.AccountDto;
import org.project.social_account_business.dto.account.ShortenAccountDto;
import org.project.social_account_business.form.account.CreateAccountForm;
import org.project.social_account_business.form.account.UpdateAccountForm;
import org.project.social_account_business.model.Account;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-13T09:11:59+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.9 (Oracle Corporation)"
)
@Component
public class AccountMapperImpl implements AccountMapper {

    @Override
    public Account fromCreateAccountFormToEntity(CreateAccountForm createAccountForm) {
        if ( createAccountForm == null ) {
            return null;
        }

        Account.AccountBuilder account = Account.builder();

        account.phoneNo( createAccountForm.getPhoneNo() );
        account.username( createAccountForm.getUsername() );
        account.email( createAccountForm.getEmail() );

        return account.build();
    }

    @Override
    public AccountDto fromEntityToDto(Account account) {
        if ( account == null ) {
            return null;
        }

        Long id = null;
        String username = null;
        String email = null;
        String phoneNo = null;
        Long balance = null;

        id = account.getId();
        username = account.getAccountName();
        email = account.getEmail();
        phoneNo = account.getPhoneNo();
        if ( account.getBalance() != null ) {
            balance = account.getBalance().longValue();
        }

        AccountDto accountDto = new AccountDto( id, username, email, phoneNo, balance );

        accountDto.setStatus( account.getStatus() );
        if ( account.getModifiedDate() != null ) {
            accountDto.setModifiedDate( LocalDateTime.ofInstant( account.getModifiedDate().toInstant(), ZoneId.of( "UTC" ) ) );
        }
        if ( account.getCreatedDate() != null ) {
            accountDto.setCreatedDate( LocalDateTime.ofInstant( account.getCreatedDate().toInstant(), ZoneId.of( "UTC" ) ) );
        }
        accountDto.setCreatedBy( account.getCreatedBy() );

        return accountDto;
    }

    @Override
    public List<AccountDto> fromEntitiesToDtos(List<Account> accounts) {
        if ( accounts == null ) {
            return null;
        }

        List<AccountDto> list = new ArrayList<AccountDto>( accounts.size() );
        for ( Account account : accounts ) {
            list.add( fromEntityToDto( account ) );
        }

        return list;
    }

    @Override
    public void updateAccountFromUpdateAccountForm(Account account, UpdateAccountForm updateAccountForm) {
        if ( updateAccountForm == null ) {
            return;
        }

        if ( updateAccountForm.getPhoneNo() != null ) {
            account.setPhoneNo( updateAccountForm.getPhoneNo() );
        }
    }

    @Override
    public ShortenAccountDto fromEntityToAutoCompleteAccountDto(Account account) {
        if ( account == null ) {
            return null;
        }

        ShortenAccountDto shortenAccountDto = new ShortenAccountDto();

        shortenAccountDto.setId( account.getId() );
        shortenAccountDto.setEmail( account.getEmail() );
        shortenAccountDto.setPhone( account.getPhoneNo() );
        shortenAccountDto.setUsername( account.getAccountName() );

        return shortenAccountDto;
    }
}
