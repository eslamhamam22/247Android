package amaz.objects.TwentyfourSeven.data.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import amaz.objects.TwentyfourSeven.data.models.StoreDetailsData;

public class StoreDetailsResponse {

    @SerializedName("result")
    @Expose
    private StoreDetailsData storeDetailsData;

    public StoreDetailsData getStoreDetailsData() {
        return storeDetailsData;
    }

    public void setStoreDetailsData(StoreDetailsData storeDetailsData) {
        this.storeDetailsData = storeDetailsData;
    }
}
