package amaz.objects.TwentyfourSeven.data.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import amaz.objects.TwentyfourSeven.data.models.ContactUsData;

public class ContactUsResponse extends BaseResponse {

    @SerializedName("data")
    @Expose
    private ContactUsData data;

    public ContactUsData getData() {
        return data;
    }

    public void setData(ContactUsData data) {
        this.data = data;
    }
}
