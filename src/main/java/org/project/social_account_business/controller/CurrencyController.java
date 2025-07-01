package org.project.social_account_business.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.project.social_account_business.dto.ApiResponse;
import org.project.social_account_business.dto.ResponseListDto;
import org.project.social_account_business.dto.currency.CurrencyDto;
import org.project.social_account_business.exception.MyBindingException;
import org.project.social_account_business.form.UpdateCurrencyForm;
import org.project.social_account_business.service.currency.CurrencyService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/currencies")
@Slf4j
public class CurrencyController {
    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public ResponseEntity<ApiResponse<String>> updateCurrency(@Valid @RequestBody UpdateCurrencyForm updateCurrencyForm, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            log.error("Error in updating currency: {}", bindingResult.getFieldError().getDefaultMessage());
            throw new MyBindingException(bindingResult.getFieldError().getDefaultMessage());
        }
        log.info("Updating currency");
        currencyService.updateCurrencyRate(updateCurrencyForm.getId(), updateCurrencyForm.getRate());
        return ResponseEntity.ok().body(new ApiResponse<>(HttpStatus.OK, "Currency updated successfully"));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<ResponseListDto<List<CurrencyDto>>>> getAllCurrencies(Pageable pageable) {
        log.info("Getting all currencies");
        return ResponseEntity.ok().body(new ApiResponse<>(HttpStatus.OK, "Get list currency successfully!", currencyService.getAllCurrencies(pageable)));
    }

    @GetMapping(value = "/id", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<CurrencyDto>> getCurrencyById(@RequestParam("id") Long id) {
        log.info("Getting currency by id: {}", id);
        return ResponseEntity.ok().body(new ApiResponse<>(HttpStatus.OK, "Get currency successfully!", currencyService.getCurrencyDetailById(id)));
    }

    @GetMapping(value = "/code", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<CurrencyDto>> getCurrencyByCode(@RequestParam("code") String code) {
        log.info("Getting currency by code: {}", code);
        return ResponseEntity.ok().body(new ApiResponse<>(HttpStatus.OK, "Get currency successfully!", currencyService.getCurrencyDetailByCode(code)));
    }
}
