package amaz.objects.TwentyfourSeven.data.models.responses;

import amaz.objects.TwentyfourSeven.data.models.CategoriesData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoriesResponse extends BaseResponse {

    @SerializedName("data")
    @Expose
    private CategoriesData data;

    public CategoriesData getData() {
        return data;
    }

    public void setData(CategoriesData data) {
        this.data = data;
    }
}
