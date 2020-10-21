package amaz.objects.TwentyfourSeven.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;

public class NotificationData implements Serializable {

    @SerializedName("notifications")
    @Expose
    private ArrayList<Notification> notifications = new ArrayList<>();

    public ArrayList<Notification> getNotification() {
        return notifications;
    }

    public void setNotifications(ArrayList<Notification> notifications) {
        this.notifications = notifications;
    }
    private String  last_seen_at = "";
    private int unseen_count = 0;

    public String getLast_seen_at() {
        return last_seen_at;
    }

    public void setLast_seen_at(String last_seen_at) {
        this.last_seen_at = last_seen_at;
    }

    public int getUnseen_notifications_count() {
        return unseen_count;
    }

    public void setUnseen_notifications_count(int unseen_notifications_count) {
        this.unseen_count = unseen_notifications_count;
    }
}
