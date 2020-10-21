package amaz.objects.TwentyfourSeven.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class OpeningHours implements Serializable {

    @SerializedName("open_now")
    @Expose
    private Boolean openNow;

    @SerializedName("weekday_text")
    @Expose
    private ArrayList<String> weakDays;

    public Boolean isOpenNow() {
        return openNow;
    }

    public void setOpenNow(Boolean openNow) {
        this.openNow = openNow;
    }

    public ArrayList<String> getWeakDays() {
        return weakDays;
    }

    public void setWeakDays(ArrayList<String> weakDays) {
        this.weakDays = weakDays;
    }
}
