package amaz.objects.TwentyfourSeven.data.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import amaz.objects.TwentyfourSeven.data.models.DirectPaymentConfirmData;

public class DirectPaymentConfirmV4ResponseMessage extends BaseResponse {

    @SerializedName("data")
    @Expose
    private DirectPaymentConfirmData data;

    public DirectPaymentConfirmData getData() {
        return data;
    }

    public void setData(DirectPaymentConfirmData data) {
        this.data = data;
    }
}
