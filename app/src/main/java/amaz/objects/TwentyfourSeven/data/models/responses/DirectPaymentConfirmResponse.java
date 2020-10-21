package amaz.objects.TwentyfourSeven.data.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DirectPaymentConfirmResponse extends PaymentBaseResponse {

    @SerializedName("directPaymentConfirmV4ResponseMessage")
    @Expose
    private DirectPaymentConfirmResponseMessage DirectPaymentConfirmResponseMessage;

    public DirectPaymentConfirmResponseMessage getDirectPaymentConfirmResponseMessage() {
        return DirectPaymentConfirmResponseMessage;
    }

    public void setDirectPaymentConfirmResponseMessage(DirectPaymentConfirmResponseMessage directPaymentConfirmResponseMessage) {
        this.DirectPaymentConfirmResponseMessage = directPaymentConfirmResponseMessage;
    }

}
