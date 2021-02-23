package amaz.objects.TwentyfourSeven.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DirectPaymentConfirmCheckoutData implements Serializable {

    @SerializedName("refNum")
    @Expose
    private String refNum;

    @SerializedName("sTCPayRefNum")
    @Expose
    private String sTCPayRefNum;

    @SerializedName("tokenId")
    @Expose
    private String tokenId;

    @SerializedName("paymentStatus")
    @Expose
    private int paymentStatus;

    @SerializedName("paymentStatusDesc")
    @Expose
    private String paymentStatusDesc;

    @SerializedName("error")
    @Expose
    private String error;

    public String getRefNum() {
        return refNum;
    }

    public String getSTCPayRefNum() {
        return sTCPayRefNum;
    }

    public String getTokenId() {
        return tokenId;
    }

    public int getPaymentStatus() {
        return paymentStatus;
    }

    public String getPaymentStatusDesc() {
        return paymentStatusDesc;
    }

    public String getError() {
        return error;
    }

}
