package amaz.objects.TwentyfourSeven.data.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import amaz.objects.TwentyfourSeven.data.models.BlockedArea;
import amaz.objects.TwentyfourSeven.data.models.CategoriesData;

public class BlockedAreasResponse extends BaseResponse {

    @SerializedName("data")
    @Expose
    private SettingsResponse data = new SettingsResponse();

    public SettingsResponse getData() {
        return data;
    }

    public void setData(SettingsResponse data) {
        this.data = data;
    }
}
