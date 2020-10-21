package amaz.objects.TwentyfourSeven.data.models;

import java.io.Serializable;

public class Notification implements Serializable {
    public String link_to = "";
    public String created_at = "";
    public String id = "";
    public String message = "";
    public NotificationOrder order;

}

