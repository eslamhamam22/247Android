package amaz.objects.TwentyfourSeven.data.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import amaz.objects.TwentyfourSeven.data.models.BankAccount;
import amaz.objects.TwentyfourSeven.data.models.BlockedArea;

public class SettingsResponse extends BaseResponse {

    @SerializedName("blocked_areas")
    @Expose
    private ArrayList<BlockedArea> blocked_areas;

    @SerializedName("max_negative_delegate_wallet")
    @Expose
    private float max_negative_delegate_wallet;

    @SerializedName("app_link")
    @Expose
    private String appLink;

    public ArrayList<BlockedArea> getBlocked_areas() {
        return blocked_areas;
    }

    public void setBlocked_areas(ArrayList<BlockedArea> blocked_areas) {
        this.blocked_areas = blocked_areas;
    }

    public float getMax_negative_delegate_wallet() {
        return max_negative_delegate_wallet;
    }

    public void setMax_negative_delegate_wallet(float max_negative_delegate_wallet) {
        this.max_negative_delegate_wallet = max_negative_delegate_wallet;
    }

    public String getAppLink() {
        return appLink;
    }

    public void setAppLink(String appLink) {
        this.appLink = appLink;
    }
}
