package amaz.objects.TwentyfourSeven.data.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import amaz.objects.TwentyfourSeven.data.models.ReviewsData;

public class ReviewsResponse extends BaseResponse {

    @SerializedName("data")
    @Expose
    private ReviewsData data;

    public ReviewsData getData() {
        return data;
    }
}
