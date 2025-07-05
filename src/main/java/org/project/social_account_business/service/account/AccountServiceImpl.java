package org.project.social_account_business.service.account;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.project.social_account_business.constant.BetaConstant;
import org.project.social_account_business.constant.ErrorCode;
import org.project.social_account_business.dto.ResponseListDto;
import org.project.social_account_business.dto.account.AccountDto;
import org.project.social_account_business.exception.BadRequestException;
import org.project.social_account_business.exception.NotFoundException;
import org.project.social_account_business.form.account.CreateAccountForm;
import org.project.social_account_business.form.account.UpdateAccountForm;
import org.project.social_account_business.mapper.AccountMapper;
import org.project.social_account_business.model.Account;
import org.project.social_account_business.model.criteria.AccountCriteria;
import org.project.social_account_business.repository.AccountRepository;
import org.project.social_account_business.service.email.EmailService;
import org.project.social_account_business.service.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService {
    private final AccountMapper accountMapper;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final TransactionService transactionService;

    @Autowired
    public AccountServiceImpl(AccountMapper accountMapper, AccountRepository accountRepository, PasswordEncoder passwordEncoder, EmailService emailService, TransactionService transactionService) {
        this.accountMapper = accountMapper;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.transactionService = transactionService;
    }

    @Override
    @Transactional(readOnly = true)
    public Account findAccountByEmail(String email) {
        return accountRepository.findAccountByEmail(email).orElseThrow(
                () -> new NotFoundException("[AccountService] ❌ Account not found!", ErrorCode.ACCOUNT_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public AccountDto getAccountById(Long id) {
        return accountMapper.fromEntityToDto(accountRepository.findAccountById(id).orElseThrow(
                () -> new NotFoundException("[AccountService] ❌ Account not found!", ErrorCode.ACCOUNT_NOT_FOUND)));
    }

    @Override
    @Transactional(readOnly = true)
    public Account findById(Long id) {
        return accountRepository.findAccountById(id).orElseThrow(
                () -> new NotFoundException("[AccountService] ❌ Account not found!", ErrorCode.ACCOUNT_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public Account findAccountByUsername(String username) {
        return accountRepository.findAccountByUsername(username).orElseThrow(
                () -> new NotFoundException("[AccountService] ❌ Account not found!", ErrorCode.ACCOUNT_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public Account findAccountByUsernameAndPassword(String username, String password) {
        return accountRepository.findAccountByUsernameAndPassword(username, password).orElseThrow(
                () -> new NotFoundException("[AccountService] ❌ Account not found!", ErrorCode.ACCOUNT_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsAccountByEmail(String email) {
        return accountRepository.existsAccountByEmail(email);
    }

    @Override
    @Transactional
    public void createAdminAccount(CreateAccountForm createAccountForm) {
        if (existsAccountByEmail(createAccountForm.getEmail())) {
            throw new BadRequestException("[AccountService] ❌ Email already exists!", ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        val account = accountMapper.fromCreateAccountFormToEntity(createAccountForm);
        account.setPassword(passwordEncoder.encode(createAccountForm.getPassword()));
        account.setKind(BetaConstant.USER_KIND_ADMIN);
        accountRepository.save(account);
        log.info("[AccountService] ✅ Admin Account with email: " + createAccountForm.getEmail() + " created successfully!");
    }

    @Override
    @Transactional
    public void createUserAccount(CreateAccountForm createAccountForm) {
        if (accountRepository.existsAccountByEmail(createAccountForm.getEmail())) {
            throw new BadRequestException("[AccountService] ❌ Email already exists!", ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        if (accountRepository.existsAccountByPhoneNo(createAccountForm.getPhoneNo())) {
            throw new BadRequestException("[AccountService] ❌ Phone number registered!", ErrorCode.PHONE_NUMBER_ALREADY_EXISTS);
        }
        val account = accountMapper.fromCreateAccountFormToEntity(createAccountForm);
        account.setPassword(passwordEncoder.encode(createAccountForm.getPassword()));
        account.setKind(BetaConstant.USER_KIND_USER);
        account.setBalance(0.0);
        accountRepository.save(account);
        log.info("[AccountService] ✅ User Account with email: " + createAccountForm.getEmail() + " created successfully!");
    }

    @Override
    @Transactional
    public void updateAccount(UpdateAccountForm updateAccountForm) {
        val account = accountRepository.findAccountById(updateAccountForm.getId()).orElseThrow(
                () -> new NotFoundException("[AccountService] ❌ Account not found!", ErrorCode.ACCOUNT_NOT_FOUND));
        accountMapper.updateAccountFromUpdateAccountForm(account, updateAccountForm);
        account.setUsername(updateAccountForm.getUsername());
        accountRepository.save(account);
    }

    @Override
    @Transactional
    public void deleteAccount(Long id) {
        val account = accountRepository.findAccountById(id).orElseThrow(
                () -> new NotFoundException("[AccountService] ❌ Account not found!", ErrorCode.ACCOUNT_NOT_FOUND));
        accountRepository.delete(account);
        log.info("[AccountService] ✅ Account with ID: " + id + " deleted successfully!");
    }

    @Override
    @Transactional(readOnly = true)
    public AccountDto getAccountDto(String email) {
        return accountMapper.fromEntityToDto(findAccountByEmail(email));
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseListDto<List<AccountDto>> accountList(AccountCriteria accountCriteria, Pageable pageable) {
        Page<Account> accounts = accountRepository.findAll(accountCriteria.getSpecification(), pageable);
        return new ResponseListDto<>(accountMapper.fromEntitiesToDtos(accounts.getContent()), accounts.getTotalElements(), accounts.getTotalPages());
    }

    @Override
    public void save(Account account) {
        accountRepository.save(account);
    }

    @Override
    public void deactivateAccount(long accountId) {
        val account = accountRepository.findAccountById(accountId).orElseThrow(
                () -> new NotFoundException("[AccountService] ❌ Account not found!", ErrorCode.ACCOUNT_NOT_FOUND));
        if (account.getStatus() == BetaConstant.STATUS_ACTIVE) {
            account.setStatus(BetaConstant.STATUS_DELETE);
            accountRepository.save(account);
            log.info("[AccountService] ✅ Account with ID: " + accountId + " deactivated successfully!");
        } else {
            throw new BadRequestException("[AccountService] ❌ Account already deactivated!", ErrorCode.OBJECT_ALREADY_DEACTIVATED);
        }
    }

    @Override
    public void activateAccount(long accountId) {
        val account = accountRepository.findAccountById(accountId).orElseThrow(
                () -> new NotFoundException("[AccountService] ❌ Account not found!", ErrorCode.ACCOUNT_NOT_FOUND));
        if (account.getStatus() == BetaConstant.STATUS_DELETE) {
            account.setStatus(BetaConstant.STATUS_ACTIVE);
            accountRepository.save(account);
            log.info("[AccountService] ✅ Account with ID: " + accountId + " deactivated successfully!");
        } else {
            throw new BadRequestException("[AccountService] ❌ Account already activated!", ErrorCode.OBJECT_ALREADY_DEACTIVATED);
        }
    }

    @Override
    @Transactional
    public void updateAccountBalance(long accountId, double amount, String transactionCode) {
        val account = accountRepository.findAccountById(accountId).orElseThrow(
                () -> new NotFoundException("[AccountService] ❌ Account not found!", ErrorCode.ACCOUNT_NOT_FOUND));
        val transaction = transactionService.findTransactionByTransactionCode(transactionCode);
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);
        emailService.sendEmail(account.getEmail(), "Account Balance Update",
                emailService.getEmailApologizeForBalanceErrorTemplate(account.getUsername(), transactionCode, amount, transaction.getCreatedDate()), true);
    }
}
