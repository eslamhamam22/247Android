package amaz.objects.TwentyfourSeven.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Review {

    @SerializedName("id")
    @Expose
    private long id;

    @SerializedName("rating")
    @Expose
    private int rate;

    @SerializedName("comment")
    @Expose
    private String comment;

    @SerializedName("created_by")
    @Expose
    private OrderCustomer createdBy;

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    public long getId() {
        return id;
    }

    public int getRate() {
        return rate;
    }

    public String getComment() {
        return comment;
    }

    public OrderCustomer getCreatedBy() {
        return createdBy;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
