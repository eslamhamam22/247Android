package amaz.objects.TwentyfourSeven.data.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import amaz.objects.TwentyfourSeven.data.models.DirectPaymentAuthorizeData;

public class DirectPaymentAuthorizeV4ResponseMessage extends BaseResponse {

    @SerializedName("data")
    @Expose
    private DirectPaymentAuthorizeData data;

    public DirectPaymentAuthorizeData getData() {
        return data;
    }

    public void setData(DirectPaymentAuthorizeData data) {
        this.data = data;
    }
}
