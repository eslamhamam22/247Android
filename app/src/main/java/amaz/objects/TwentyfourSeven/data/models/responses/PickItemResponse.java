package amaz.objects.TwentyfourSeven.data.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import amaz.objects.TwentyfourSeven.data.models.PickItemData;

public class PickItemResponse extends BaseResponse {

    @SerializedName("data")
    @Expose
    private PickItemData data;

    public PickItemData getData() {
        return data;
    }
}
