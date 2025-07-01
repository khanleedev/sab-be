package org.project.social_account_business.service.payment_transaction;

import org.project.social_account_business.dto.payment_transaction.PaypalCaptureDto;
import org.project.social_account_business.dto.payment_transaction.PaypalResponseDto;
import org.project.social_account_business.form.payment_transaction.PaypalRequestForm;

import java.io.IOException;

public interface PaypalService {
    PaypalResponseDto createPaypalOrder(PaypalRequestForm paypalRequestForm, long accountId) throws IOException;
    PaypalCaptureDto capturePaypalOrder(String orderId) throws IOException;
    void handlePaypalWebhook(String payload, String authAlgo, String certUrl, String transmissionId, String transmissionSig, String transmissionTime) throws IOException;
}
