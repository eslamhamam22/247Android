package amaz.objects.TwentyfourSeven.data.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import amaz.objects.TwentyfourSeven.data.models.SocialLoginData;

import amaz.objects.TwentyfourSeven.data.models.SocialLoginData;

public class SocialLoginResponse extends BaseResponse {

    @SerializedName("data")
    @Expose
    private SocialLoginData data;

    public SocialLoginData getData() {
        return data;
    }

    public void setData(SocialLoginData data) {
        this.data = data;
    }
}
