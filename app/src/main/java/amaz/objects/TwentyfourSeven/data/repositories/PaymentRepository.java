package amaz.objects.TwentyfourSeven.data.repositories;

import java.io.File;

import amaz.objects.TwentyfourSeven.listeners.OnResponseListener;

public interface PaymentRepository {

    void stcDirectPayment(String token, final OnResponseListener onStcDirectPaymentResponse);

    void stcDirectPaymentAuthorize(String token, final OnResponseListener onStcDirectPaymentAuthorizeResponse);

    void stcDirectPaymentConfirm(String token, final OnResponseListener onStcDirectPaymentConfirmResponse);

    void stcPaymentInquiry(String token, final OnResponseListener onStcPaymentInquiryResponse);

}
