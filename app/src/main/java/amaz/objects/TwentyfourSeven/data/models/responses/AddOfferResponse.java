package amaz.objects.TwentyfourSeven.data.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import amaz.objects.TwentyfourSeven.data.models.Offer;

public class AddOfferResponse extends BaseResponse {

    @SerializedName("data")
    @Expose
    private Offer offer;

    public Offer getOffer() {
        return offer;
    }
}
