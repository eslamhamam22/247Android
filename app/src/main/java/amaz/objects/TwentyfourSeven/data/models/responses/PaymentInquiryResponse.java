package amaz.objects.TwentyfourSeven.data.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentInquiryResponse extends PaymentBaseResponse {

    @SerializedName("paymentInquiryV4ResponseMessage")
    @Expose
    private PaymentInquiryResponseMessage PaymentInquiryResponseMessage;

    public PaymentInquiryResponseMessage getPaymentInquiryResponseMessage() {
        return PaymentInquiryResponseMessage;
    }

    public void setPaymentInquiryResponseMessage(PaymentInquiryResponseMessage paymentInquiryResponseMessage) {
        this.PaymentInquiryResponseMessage = paymentInquiryResponseMessage;
    }

}
