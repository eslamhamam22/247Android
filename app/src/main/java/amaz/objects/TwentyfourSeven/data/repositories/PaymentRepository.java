package amaz.objects.TwentyfourSeven.data.repositories;

import org.json.JSONObject;

import java.io.File;

import amaz.objects.TwentyfourSeven.listeners.OnResponseListener;

public interface PaymentRepository {

    void stcDirectPayment(String merchantId, JSONObject payment, final OnResponseListener onStcDirectPaymentResponse);

    void stcDirectPaymentAuthorize(String merchantId, JSONObject payment, final OnResponseListener onStcDirectPaymentAuthorizeResponse);

    void stcDirectPaymentConfirm(String merchantId, JSONObject payment, final OnResponseListener onStcDirectPaymentConfirmResponse);

    void stcPaymentInquiry(String merchantId, JSONObject payment, final OnResponseListener onStcPaymentInquiryResponse);

}
