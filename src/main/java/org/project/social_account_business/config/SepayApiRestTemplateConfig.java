package org.project.social_account_business.config;

import lombok.Getter;
import lombok.Setter;
import org.project.social_account_business.constant.BetaConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
@ConfigurationProperties(prefix = "sepay")
@Getter
@Setter
public class SepayApiRestTemplateConfig {
    @Value("${sepay.bank.accountNo}")
    private String accountNumber;
    @Value("${sepay.bank.bankName}")
    private String bankName;

    public String generateQRCodeUrl(BigDecimal amount, String transactionCode) {
        StringBuilder qrCode = new StringBuilder();
        qrCode.append(BetaConstant.QR_SEPAY_URL_BASE);
        qrCode.append("acc=").append(accountNumber).append("&");
        qrCode.append("bank=").append(bankName).append("&");
        qrCode.append("amount=").append(amount).append("&");
        qrCode.append("des=").append("SAB TOP UP FOR ACCOUNT ").append(transactionCode)
                .append("&template=compact");
        return qrCode.toString();
    }
}
