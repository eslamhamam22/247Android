package amaz.objects.TwentyfourSeven.data.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DirectPaymentAuthorizeResponse extends PaymentBaseResponse {

    @SerializedName("directPaymentAuthorizeV4ResponseMessage")
    @Expose
    private DirectPaymentAuthorizeResponseMessage DirectPaymentAuthorizeResponseMessage;

    public DirectPaymentAuthorizeResponseMessage getDirectPaymentAuthorizeResponseMessage() {
        return DirectPaymentAuthorizeResponseMessage;
    }

    public void setDirectPaymentAuthorizeResponseMessage(DirectPaymentAuthorizeResponseMessage directPaymentAuthorizeResponseMessage) {
        this.DirectPaymentAuthorizeResponseMessage = directPaymentAuthorizeResponseMessage;
    }

}
