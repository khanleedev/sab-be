package org.project.social_account_business.constant;

public class BetaConstant {
    public static final Integer STATUS_ACTIVE = 1;
    public static final Integer STATUS_PENDING = 0;
    public static final Integer STATUS_LOCK = -1;
    public static final Integer STATUS_DELETE = -2;
    /*
    User kind
     */
    public static final Integer USER_KIND_ADMIN = 1;
    public static final Integer USER_KIND_USER = 2;
    public static final Integer MAX_ATTEMPT_FORGET_PWD = 5;

    private BetaConstant() {
        throw new IllegalStateException("Utility class");
    }
    public static final String DATE_TIME_CONSTANT_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_CONSTANT_FORMAT = "yyyy-MM-dd";
    public static final String TIME_CONSTANT_FORMAT = "HH:mm:ss";

    // External currency API
    public static final String CONVERT_CURRENCY_END_POINT = "https://api.freecurrencyapi.com/v1/latest?apikey=";
    public static final String QR_SEPAY_URL_BASE = "https://qr.sepay.vn/img?";
    public static final String SEPAY_METHOD_CURRENCY_CODE = "VND";
    public static final String VISA_METHOD_CURRENCY_CODE = "USD";
}
