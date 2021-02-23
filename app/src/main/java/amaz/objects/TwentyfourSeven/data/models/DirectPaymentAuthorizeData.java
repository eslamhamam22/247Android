package amaz.objects.TwentyfourSeven.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DirectPaymentAuthorizeData implements Serializable {

    @SerializedName("checkoutData")
    @Expose
    private DirectPaymentAuthorizeCheckoutData checkoutData;

    public DirectPaymentAuthorizeCheckoutData getCheckoutData() {
        return checkoutData;
    }

    public void setCheckoutData(DirectPaymentAuthorizeCheckoutData checkoutData) {
        this.checkoutData = checkoutData;
    }
}
