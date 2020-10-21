package amaz.objects.TwentyfourSeven.data.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DirectPaymentAuthorizeResponseMessage {

    @SerializedName("otpReference")
    @Expose
    private String OtpReference;

    @SerializedName("sTCPayPmtReference")
    @Expose
    private String STCPayPmtReference;

    @SerializedName("expiryDuration")
    @Expose
    private int ExpiryDuration;

    public String getOtpReference() {
        return OtpReference;
    }

    public void setOtpReference(String otpReference) {
        this.OtpReference = otpReference;
    }

    public String getSTCPayPmtReference() {
        return STCPayPmtReference;
    }

    public void setSTCPayPmtReference(String sTCPayPmtReference) {
        this.STCPayPmtReference = sTCPayPmtReference;
    }

    public int getExpiryDuration() {
        return ExpiryDuration;
    }

    public void setExpiryDuration(int expiryDuration) {
        this.ExpiryDuration = expiryDuration;
    }

}
