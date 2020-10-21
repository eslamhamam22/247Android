package amaz.objects.TwentyfourSeven.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PrayerTimes implements Serializable {

    @SerializedName("Fajr")
    @Expose
    private String fajr;

    @SerializedName("Dhuhr")
    @Expose
    private String duhr;

    @SerializedName("Asr")
    @Expose
    private String asr;

    @SerializedName("Maghrib")
    @Expose
    private String maghrib;

    @SerializedName("Isha")
    @Expose
    private String isha;

    public String getFajr() {
        return fajr;
    }

    public String getDuhr() {
        return duhr;
    }

    public String getAsr() {
        return asr;
    }

    public String getMaghrib() {
        return maghrib;
    }

    public String getIsha() {
        return isha;
    }
}
