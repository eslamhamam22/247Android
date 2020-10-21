package amaz.objects.TwentyfourSeven.data.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import amaz.objects.TwentyfourSeven.data.models.PageData;

public class PageResponse extends BaseResponse {

    @SerializedName("data")
    @Expose
    private PageData data;

    public PageData getData() {
        return data;
    }

    public void setData(PageData data) {
        this.data = data;
    }
}
