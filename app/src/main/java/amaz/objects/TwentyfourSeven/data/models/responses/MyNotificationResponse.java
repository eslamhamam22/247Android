package amaz.objects.TwentyfourSeven.data.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import amaz.objects.TwentyfourSeven.data.models.NotificationData;

public class MyNotificationResponse extends BaseResponse {

    @SerializedName("data")
    @Expose
    private NotificationData NotificationData;

    public amaz.objects.TwentyfourSeven.data.models.NotificationData getNotificationData() {
        return NotificationData;
    }

    public void setNotificationData(amaz.objects.TwentyfourSeven.data.models.NotificationData notificationData) {
        NotificationData = notificationData;
    }
}
