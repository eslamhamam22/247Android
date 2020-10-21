package amaz.objects.TwentyfourSeven.data.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import amaz.objects.TwentyfourSeven.data.models.CarDetailsData;

public class CarDetailsResponse extends BaseResponse {

    @SerializedName("data")
    @Expose
    private CarDetailsData data;

    public CarDetailsData getData() {
        return data;
    }

    public void setData(CarDetailsData data) {
        this.data = data;
    }
}
