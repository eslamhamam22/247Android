package amaz.objects.TwentyfourSeven.data.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import amaz.objects.TwentyfourSeven.data.models.CancelationReason;

public class CancelationReasonsResponse extends BaseResponse {

    @SerializedName("data")
    @Expose
    private ArrayList<CancelationReason> data;

    public ArrayList<CancelationReason> getData() {
        return data;
    }
}
