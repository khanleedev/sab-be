package org.project.social_account_business.service.transaction;

import lombok.extern.slf4j.Slf4j;
import org.project.social_account_business.config.SepayApiRestTemplateConfig;
import org.project.social_account_business.constant.BetaConstant;
import org.project.social_account_business.constant.ErrorCode;
import org.project.social_account_business.dto.ResponseListDto;
import org.project.social_account_business.dto.transaction.TransactionDto;
import org.project.social_account_business.dto.transaction.TransactionForOrderDto;
import org.project.social_account_business.dto.transaction.TransactionForPaymentDto;
import org.project.social_account_business.exception.BadRequestException;
import org.project.social_account_business.exception.NotFoundException;
import org.project.social_account_business.form.payment_transaction.CreateTopUpForm;
import org.project.social_account_business.mapper.TransactionMapper;
import org.project.social_account_business.model.*;
import org.project.social_account_business.model.criteria.TransactionCriteria;
import org.project.social_account_business.repository.TransactionRepository;
import org.project.social_account_business.service.currency.CurrencyService;
import org.project.social_account_business.service.email.EmailService;
import org.project.social_account_business.utils.Utils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.val;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service("transactionService")
@Slf4j
public class TransactionServiceImpl implements TransactionService {
    private static final String ORDER_TRANSACTION_PREFIX = "ORDER";
    private static final String PAYMENT_TRANSACTION_PREFIX = "PAYMENT";
    private final TransactionMapper transactionMapper;
    private final TransactionRepository transactionRepository;
    private final CurrencyService currencyService;
    private final EmailService emailService;
    private final SepayApiRestTemplateConfig sepayApiRestTemplateConfig;

    public TransactionServiceImpl(TransactionMapper transactionMapper, TransactionRepository transactionRepository, CurrencyService currencyService, EmailService emailService, SepayApiRestTemplateConfig sepayApiRestTemplateConfig) {
        this.transactionMapper = transactionMapper;
        this.transactionRepository = transactionRepository;
        this.currencyService = currencyService;
        this.emailService = emailService;
        this.sepayApiRestTemplateConfig = sepayApiRestTemplateConfig;
    }

    @Override
    @Transactional
    public String createTransactionForOrder(Order order) {
        val transaction = new Transaction();
        val coinAmount = order.getTotalPrice();
        transaction.setTransactionId(order.getId());
        transaction.setAmountInCoin(coinAmount);
        transaction.setAmountInCash(BigDecimal.valueOf(coinAmount / currencyService.getRateConverterByCode(BetaConstant.SEPAY_METHOD_CURRENCY_CODE)));
        transaction.setOrderStatus(TransactionStatus.PAID);
        transaction.setTransactionType(TransactionType.OUT);
        String transactionCode = Utils.generateCode(ORDER_TRANSACTION_PREFIX, new Date());
        transaction.setTransactionCode(transactionCode);
        transactionRepository.save(transaction);
        String username = order.getAccount().getAccountName();
        String email = order.getAccount().getEmail();
        String message = emailService.getEmailOrderCompleteTemplate(username, transactionCode, order.getTotalPrice(), order.getCreatedDate(), TransactionStatus.PAID.toString().toLowerCase(), order.getTicketProduct().getName(), order.getQuantity(), order.getTicketProduct().getItemCode());
        emailService.sendEmail(email, "Order Confirmation", message, true);
        return transactionCode;
    }

    @Override
    @Transactional
    public String createTransactionForSepayPayment(CreateTopUpForm form) {
        val amount = form.getAmount();
        if (amount.doubleValue() % 10000 != 0 || amount.doubleValue() < 10000) {
            throw new BadRequestException("[PaymentTransactionService] Amount must be a multiple of 10,000");
        }
        val transaction = new Transaction();
        transaction.setAmountInCash(form.getAmount());
        transaction.setAmountInCoin(form.getAmount().doubleValue() * currencyService.getRateConverterByCode(BetaConstant.SEPAY_METHOD_CURRENCY_CODE));
        transaction.setOrderStatus(TransactionStatus.PENDING);
        transaction.setTransactionType(TransactionType.IN);
        String transactionCode = Utils.generateCode(PAYMENT_TRANSACTION_PREFIX, new Date());
        transaction.setTransactionCode(transactionCode);
        transactionRepository.save(transaction);
        return sepayApiRestTemplateConfig.generateQRCodeUrl(amount, transactionCode);
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionDto getTransactionById(Long id) {
        return transactionMapper.fromEntityToDto(transactionRepository.findFirstById(id).orElseThrow(
                () -> new NotFoundException("[Transaction Service] Transaction not found with id: " + id, ErrorCode.TRANSACTION_NOT_FOUND)
        ));
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseListDto<List<TransactionForOrderDto>> getTransactionsForOrder(Pageable pageable, TransactionCriteria criteria) {
        criteria.setTransactionType(TransactionType.OUT.toString());
        Page<Transaction> transactions = transactionRepository.findAll(criteria.getSpecification(), pageable);
        return new ResponseListDto<>(transactionMapper.toEntitiesToDtosForOrder(transactions.getContent()), transactions.getTotalElements(), transactions.getTotalPages());
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseListDto<List<TransactionForPaymentDto>> getTransactionsForPayment(Pageable pageable, TransactionCriteria criteria) {
        criteria.setTransactionType(TransactionType.IN.toString());
        Page<Transaction> transactions = transactionRepository.findAll(criteria.getSpecification(), pageable);
        return new ResponseListDto<>(transactionMapper.toEntitiesToDtosForPayment(transactions.getContent()), transactions.getTotalElements(), transactions.getTotalPages());
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseListDto<List<TransactionDto>> getAllTransactions(Pageable pageable, TransactionCriteria criteria) {
        Page<Transaction> transactions = transactionRepository.findAll(criteria.getSpecification(), pageable);
        return new ResponseListDto<>(transactionMapper.fromEntitiesToDtos(transactions.getContent()), transactions.getTotalElements(), transactions.getTotalPages());
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionDto getTransactionDetailsById(Long id) {
        return transactionRepository.findFirstById(id).map(transactionMapper::fromEntityToDto).orElseThrow(
                () -> new NotFoundException("[Transaction Service] Transaction not found with id: " + id, ErrorCode.TRANSACTION_NOT_FOUND));
    }

    @Override
    public void saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    @Override
    @Transactional(readOnly = true)
    public Transaction findTransactionById(Long id) {
        return transactionRepository.findFirstById(id).orElseThrow(
                () -> new NotFoundException("[Transaction Service] Transaction not found with id: " + id, ErrorCode.TRANSACTION_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public Transaction findTransactionByTransactionCode(String transactionCode) {
        return transactionRepository.findFirstByTransactionCode(transactionCode).orElseThrow(
                () -> new NotFoundException("[Transaction Service] Transaction not found with transaction code: " + transactionCode, ErrorCode.TRANSACTION_NOT_FOUND));
    }

    @Override
    public Transaction findTransactionByTransactionId(Long transactionId) {
        return transactionRepository.findTransactionByTransactionId(transactionId).orElseThrow(
                () -> new NotFoundException("[Transaction Service] Transaction not found with transaction ID: " + transactionId, ErrorCode.TRANSACTION_NOT_FOUND));
    }

    @Override
    public ResponseListDto<List<TransactionForOrderDto>> getAllTransactionsForOrderByAccountId(Pageable pageable, long accountId) {
        Page<Transaction> transactions = transactionRepository.findAllTransactionForOrderByAccountId(accountId, pageable);
        return new ResponseListDto<>(transactions.getContent().stream().map(transactionMapper::toEntityToDtoForOrder).collect(Collectors.toList()), transactions.getTotalElements(), transactions.getTotalPages());
    }

    @Override
    public ResponseListDto<List<TransactionForPaymentDto>> getAllTransactionsForPaymentByAccountId(Pageable pageable, long accountId) {
        Page<Transaction> transactions = transactionRepository.findAllTransactionForPaymentByAccountId(accountId, pageable);
        return new ResponseListDto<>(transactions.getContent().stream().map(transactionMapper::toEntityToDtoForPayment).collect(Collectors.toList()), transactions.getTotalElements(), transactions.getTotalPages());
    }
}
