package amaz.objects.TwentyfourSeven.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DirectPaymentAuthorizeCheckoutData implements Serializable {

    @SerializedName("otpReference")
    @Expose
    private String otpReference;

    @SerializedName("sTCPayPmtReference")
    @Expose
    private String sTCPayPmtReference;

    @SerializedName("refNum")
    @Expose
    private String refNum;

    @SerializedName("error")
    @Expose
    private String error;

    public String getOtpReference() {
        return otpReference;
    }

    public String getSTCPayPmtReference() {
        return sTCPayPmtReference;
    }

    public String getRefNum() {
        return refNum;
    }

    public String getError() {
        return error;
    }

}
