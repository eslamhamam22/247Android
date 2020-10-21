package amaz.objects.TwentyfourSeven.data.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import amaz.objects.TwentyfourSeven.data.models.Complaint;

public class ComplaintsResponse extends BaseResponse {

    @SerializedName("data")
    @Expose
    private ArrayList<Complaint> complaints;

    public ArrayList<Complaint> getComplaints() {
        return complaints;
    }

}
