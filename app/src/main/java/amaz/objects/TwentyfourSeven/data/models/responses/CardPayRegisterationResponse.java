package amaz.objects.TwentyfourSeven.data.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import amaz.objects.TwentyfourSeven.data.models.CardPayRegisterationData;

public class CardPayRegisterationResponse extends BaseResponse {

    @SerializedName("data")
    @Expose
    private CardPayRegisterationData data;

    public CardPayRegisterationData getData() {
        return data;
    }

    public void setData(CardPayRegisterationData data) {
        this.data = data;
    }
}
