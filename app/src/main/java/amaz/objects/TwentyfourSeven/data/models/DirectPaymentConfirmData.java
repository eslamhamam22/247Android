package amaz.objects.TwentyfourSeven.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DirectPaymentConfirmData implements Serializable {

    @SerializedName("checkoutData")
    @Expose
    private DirectPaymentConfirmCheckoutData checkoutData;

    public DirectPaymentConfirmCheckoutData getCheckoutData() {
        return checkoutData;
    }

    public void setCheckoutData(DirectPaymentConfirmCheckoutData checkoutData) {
        this.checkoutData = checkoutData;
    }

}
