package amaz.objects.TwentyfourSeven.data.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import amaz.objects.TwentyfourSeven.data.models.Slider;

public class HowToUseResponse extends BaseResponse {

    @SerializedName("data")
    @Expose
    private ArrayList<Slider> data;

    public ArrayList<Slider> getData() {
        return data;
    }

    public void setData(ArrayList<Slider> data) {
        this.data = data;
    }
}
