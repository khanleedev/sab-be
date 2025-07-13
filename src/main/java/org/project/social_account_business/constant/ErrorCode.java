package org.project.social_account_business.constant;

public class ErrorCode {
    public static final String DB_QUERY_ERROR = "DB_QUERY_ERROR";
    public static final String OBJECT_ALREADY_DEACTIVATED = "ENTITY_ERROR_0001";
    public static final String OBJECT_ALREADY_ACTIVATED = "ENTITY_ERROR_0002";
    // Account Error Codes
    public static final String ACCOUNT_NOT_FOUND = "ACCOUNT_ERROR_0001";
    public static final String ACCOUNT_USERNAME_EXISTED = "ACCOUNT_ERROR_0002";
    public static final String EMAIL_ALREADY_EXISTS = "ACCOUNT_ERROR_0003";
    public static final String PHONE_NUMBER_ALREADY_EXISTS = "ACCOUNT_ERROR_0004";
    public static final String ACOUNT_NOT_ENOUGH_BALANCE = "ACCOUNT_ERROR_0005";
    public static final String ACCOUNT_ERROR_WRONG_HASH_RESET_PASS = "ACCOUNT_ERROR_0005";
    // Token Error Codes
    public static final String TOKEN_EXPIRED = "TOKEN_ERROR_0001";
    public static final String TOKEN_UNSUPPORTED = "TOKEN_ERROR_0002";
    public static final String TOKEN_MALFORMED = "TOKEN_ERROR_0003";
    public static final String TOKEN_SIGNATURE = "TOKEN_ERROR_0004";
    public static final String TOKEN_ILLEGAL_ARGUMENT = "TOKEN_ERROR_0005";
    public static final String TOKEN_NOT_FOUND = "TOKEN_ERROR_0006";
    // OTP Error Codes
    public static final String OTP_NOT_FOUND = "OTP_ERROR_0001";
    public static final String EXPIRED_OTP = "OTP_ERROR_0002";
    public static final String OTP_ATTEMPT_LIMIT = "OTP_ERROR_0003";
    public static final String INVALID_OTP = "OTP_ERROR_0004";
    // Email Error Codes
    public static final String EMAIL_IS_REQUIRED = "EMAIL_ERROR_0001";
    public static final String EMAIL_NOT_FOUND = "EMAIL_ERROR_0002";

    // Ticket Error Codes
    public static final String TICKET_NOT_FOUND = "TICKET_ERROR_0001";
    public static final String TICKET_HAS_TICKET_PRODUCTS = "TICKET_ERROR_0002";
    public static final String TICKET_PRODUCT_MAX_PURCHASE = "TICKET_PRODUCT_ERROR_0003";

    // Ticket Product Error Codes
    public static final String TICKET_PRODUCT_NOT_FOUND = "TICKET_PRODUCT_ERROR_0001";
    public static final String TICKET_PRODUCT_NOT_ENOUGH = "TICKET_PRODUCT_ERROR_0002";

    // Order Error Codes
    public static final String ORDER_NOT_FOUND = "ORDER_ERROR_0001";
    public static final String ORDER_QUANTITY_INVALID = "ORDER_ERROR_0002";

    // Transaction Error Codes
    public static final String TRANSACTION_NOT_FOUND = "TRANSACTION_ERROR_0001";

    // Payment Transaction Error Codes
    public static final String PAYMENT_TRANSACTION_NOT_FOUND = "PAYMENT_TRANSACTION_ERROR_0001";
    // Report Error Codes
    public static final String REPORT_NOT_FOUND = "REPORT_ERROR_0001";
    // Currency Error Codes
    public static final String CURRENCY_NOT_FOUND = "CURRENCY_ERROR_0001";
    public static final String TICKET_PRODUCT_INFO_NOT_ENOUGH = "TICKET_PRODUCT_INFO_ERROR_0001";
    public static final String INVALID_TOKEN = "INVALID_TOKEN";
}
