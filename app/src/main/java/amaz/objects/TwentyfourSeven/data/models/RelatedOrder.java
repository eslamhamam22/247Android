package amaz.objects.TwentyfourSeven.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RelatedOrder {

    @SerializedName("id")
    @Expose
    private int id;

    public int getId() {
        return id;
    }
}
