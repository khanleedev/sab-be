package org.project.social_account_business.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.project.social_account_business.constant.BetaConstant;
import org.project.social_account_business.dto.ApiResponse;
import org.project.social_account_business.dto.ResponseListDto;
import org.project.social_account_business.dto.account.AccountDto;
import org.project.social_account_business.exception.MyBindingException;
import org.project.social_account_business.form.account.CreateAccountForm;
import org.project.social_account_business.form.account.UpdateAccountForm;
import org.project.social_account_business.form.account.UpdateBalanceForm;
import org.project.social_account_business.model.criteria.AccountCriteria;
import org.project.social_account_business.service.account.AccountService;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/v1/accounts")
@Slf4j
public class AccountController extends ABasicController {
    private final AccountService accountService;
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping(value = "/admins", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<String>> createAdmin(@Valid @RequestBody CreateAccountForm createAccountForm, BindingResult bindingResult) {
        log.info("Creating admin account");
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(HttpStatus.BAD_REQUEST, "Invalid admin account form", bindingResult.getAllErrors().toString()));
        }
        accountService.createAdminAccount(createAccountForm);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.CREATED, "Admin account created successfully"));
    }

    @PostMapping(value = "/users/register", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<ApiResponse<String>> createUser(@Valid @RequestBody CreateAccountForm createAccountForm, BindingResult bindingResult) {
        log.info("Creating user account");
        if (bindingResult.hasErrors()) {
            throw new MyBindingException("[AccountController] " + Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        accountService.createUserAccount(createAccountForm);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.CREATED, "User account created successfully"));
    }

    @PatchMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<String>> updateAccount(@Valid @RequestBody UpdateAccountForm updateAccountForm, BindingResult bindingResult) {
        log.info("Updating account");
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(HttpStatus.BAD_REQUEST, "Invalid account update form", bindingResult.getAllErrors().toString()));
        }
        accountService.updateAccount(updateAccountForm);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, "Account updated successfully"));
    }

    @PatchMapping(value = "/balance",produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<ApiResponse<T>> updateAccountBalance(@Valid @RequestBody UpdateBalanceForm updateBalanceForm, BindingResult bindingResult){
        log.info("Updating account balance");
        if (bindingResult.hasErrors()) {
            throw new MyBindingException("[AccountController] " + Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        accountService.updateAccountBalance(updateBalanceForm.getAccountId(), updateBalanceForm.getBalance(), updateBalanceForm.getTransactionCode());
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, "Account balance updated successfully"));
    }

    @DeleteMapping(value = "/{accId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteAccount(@PathVariable("accId") @Min(10) Long id) {
        log.info("Deleting account");
        accountService.deleteAccount(id);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, "Account deleted successfully"));
    }


    @GetMapping(value = "/{accId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<EntityModel<AccountDto>>> getAccount(@PathVariable("accId") @Min(10) Long id) {
        log.info("Getting account");
        AccountDto accountDto = accountService.getAccountById(id);
        EntityModel<AccountDto> entityModel = EntityModel.of(accountDto,
                linkTo(methodOn(AccountController.class).getAccount(id)).withSelfRel(),
                linkTo(methodOn(AccountController.class).accountList(new AccountCriteria(), Pageable.unpaged() )).withRel("account-list"),
                linkTo(methodOn(AccountController.class).getMe()).withRel("get-me"),
                linkTo(methodOn(AccountController.class).updateAccount(new UpdateAccountForm(), null)).withRel("update-account"),
                linkTo(methodOn(AccountController.class).deleteAccount(id)).withRel("delete-account"),
                linkTo(methodOn(AccountController.class).createAdmin(new CreateAccountForm(), null)).withRel("create-admin"),
                linkTo(methodOn(AccountController.class).createUser(new CreateAccountForm(), null)).withRel("create-user"),
                linkTo(methodOn(AccountController.class).deactivateAccount(id)).withRel("deactivate-account"),
                linkTo(methodOn(AccountController.class).activateAccount(id)).withRel("activate-account"));
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, "Account retrieved successfully", entityModel));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<ResponseListDto<List<AccountDto>>>> accountList(AccountCriteria accountCriteria, Pageable pageable) {
        log.info("Getting account list");
        accountCriteria.setStatus(BetaConstant.STATUS_ACTIVE);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, "Account list retrieved successfully", accountService.accountList(accountCriteria, pageable)));
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping(value = "/get-me", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<AccountDto>> getMe() {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, "Account ID retrieved successfully!", getCurrentUser()));
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PatchMapping(value = "/deactivate-account/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<String>> deactivateAccount(@PathVariable("id") Long accountId){
        log.info("Deactivating account");
        accountService.deactivateAccount(accountId);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, "Account deactivated successfully"));
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PatchMapping(value = "/activate-account/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<String>> activateAccount(@PathVariable("id") Long accountId){
        log.info("Activating account");
        accountService.deactivateAccount(accountId);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, "Account deactivated successfully"));
    }
}
