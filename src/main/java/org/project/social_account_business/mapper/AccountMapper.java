package org.project.social_account_business.mapper;

import org.project.social_account_business.dto.account.AccountDto;
import org.project.social_account_business.model.Account;
import org.mapstruct.*;
import org.project.social_account_business.dto.account.ShortenAccountDto;
import org.project.social_account_business.form.account.CreateAccountForm;
import org.project.social_account_business.form.account.UpdateAccountForm;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AccountMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "phoneNo", source = "phoneNo")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "email", source = "email")
    @BeanMapping(ignoreByDefault = true)
    Account fromCreateAccountFormToEntity(CreateAccountForm createAccountForm);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "username", source = "accountName")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "phoneNo", source = "phoneNo")
    @Mapping(target = "balance", source = "balance")
    @Named("fromEntityToDto")
    AccountDto fromEntityToDto(Account account);

    @IterableMapping(qualifiedByName = "fromEntityToDto", elementTargetType = AccountDto.class)
    List<AccountDto> fromEntitiesToDtos(List<Account> accounts);

    @Mapping(target = "phoneNo", source = "phoneNo")
    @BeanMapping(ignoreByDefault = true)
    void updateAccountFromUpdateAccountForm(@MappingTarget Account account, UpdateAccountForm updateAccountForm);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "phone", source = "phoneNo")
    @Mapping(target = "username", source = "accountName")
    @Named("fromEntityToAutoCompleteAccountDto")
    ShortenAccountDto fromEntityToAutoCompleteAccountDto(Account account);
}
