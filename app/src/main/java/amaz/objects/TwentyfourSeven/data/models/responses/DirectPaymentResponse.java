package amaz.objects.TwentyfourSeven.data.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DirectPaymentResponse extends PaymentBaseResponse {

    @SerializedName("directPaymentV4ResponseMessage")
    @Expose
    private DirectPaymentResponseMessage DirectPaymentResponseMessage;

    public DirectPaymentResponseMessage getDirectPaymentResponseMessage() {
        return DirectPaymentResponseMessage;
    }

    public void setDirectPaymentResponseMessage(DirectPaymentResponseMessage directPaymentResponseMessage) {
        this.DirectPaymentResponseMessage = directPaymentResponseMessage;
    }

}
