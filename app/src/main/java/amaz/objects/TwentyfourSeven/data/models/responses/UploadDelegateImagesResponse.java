package amaz.objects.TwentyfourSeven.data.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import amaz.objects.TwentyfourSeven.data.models.DelegateImageData;


public class UploadDelegateImagesResponse extends BaseResponse {

    @SerializedName("data")
    @Expose
    private DelegateImageData data;

    public DelegateImageData getData() {
        return data;
    }

    public void setData(DelegateImageData data) {
        this.data = data;
    }
}
