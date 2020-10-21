package amaz.objects.TwentyfourSeven.data.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import amaz.objects.TwentyfourSeven.data.models.PrayerTimes;

public class PrayerTimesResponse extends BaseResponse {

    @SerializedName("data")
    @Expose
    private PrayerTimes prayerTimes;

    public PrayerTimes getPrayerTimes() {
        return prayerTimes;
    }
}
