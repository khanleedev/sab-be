package org.project.social_account_business.service.currency;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.project.social_account_business.constant.ErrorCode;
import org.project.social_account_business.dto.ResponseListDto;
import org.project.social_account_business.dto.currency.CurrencyDto;
import org.project.social_account_business.exception.NotFoundException;
import org.project.social_account_business.model.Currency;
import org.project.social_account_business.repository.CurrencyRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("currencyService")
@Slf4j
public class CurrencyServiceImpl implements CurrencyService {
    final CurrencyRepository currencyRepository;

    public CurrencyServiceImpl(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "currency", key = "#code")
    public Double getRateConverterByCode(String code) {
        return (currencyRepository.findFirstByCode(code)
                .orElseThrow(() -> new NotFoundException("[CurrencyService] Currency not found with code: " + code)))
                .getRate();
    }

    @Override
    @Transactional(readOnly = true)
    public CurrencyDto getCurrencyDetailByCode(String code) {
        return currencyRepository.findFirstByCode(code)
                .map(currency -> new CurrencyDto(currency.getId(), currency.getCode(), currency.getName(), currency.getRate(), currency.getBonusRate()))
                .orElseThrow(() -> new NotFoundException("[CurrencyService] Currency not found with code: " + code));
    }

    @Override
    @Transactional(readOnly = true)
    public CurrencyDto getCurrencyDetailById(Long id) {
        return currencyRepository.findById(id)
                .map(currency -> new CurrencyDto(currency.getId(), currency.getCode(), currency.getName(), currency.getRate(), currency.getBonusRate()))
                .orElseThrow(() -> new NotFoundException("[CurrencyService] Currency not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseListDto<List<CurrencyDto>> getAllCurrencies(Pageable pageable) {
        Page<Currency> currencies = currencyRepository.findAllActiveCurrency(pageable);
        List<CurrencyDto> currencyDtos = currencies
                .map(currency -> new CurrencyDto(currency.getId(), currency.getCode(), currency.getName(), currency.getRate(), currency.getBonusRate()))
                .getContent();
        return new ResponseListDto<>(currencyDtos, currencies.getTotalElements(), currencies.getTotalPages());
    }

    @Override
    @Transactional
    public void updateCurrencyRate(long id, double rate) {
        val currency = currencyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("[CurrencyService] Currency not found with id: " + id, ErrorCode.CURRENCY_NOT_FOUND));
        currency.setRate(rate);
        currencyRepository.save(currency);
    }
}
