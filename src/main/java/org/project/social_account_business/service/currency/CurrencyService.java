package org.project.social_account_business.service.currency;

import org.project.social_account_business.dto.ResponseListDto;
import org.project.social_account_business.dto.currency.CurrencyDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CurrencyService {
    Double getRateConverterByCode(String code);
    CurrencyDto getCurrencyDetailByCode(String code);
    CurrencyDto getCurrencyDetailById(Long id);
    ResponseListDto<List<CurrencyDto>> getAllCurrencies(Pageable pageable);
    void updateCurrencyRate(long id, double rate);
}
