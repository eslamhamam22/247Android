package amaz.objects.TwentyfourSeven.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Complaint {

    @SerializedName("id")
    @Expose
    private long id;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("related_order")
    @Expose
    private RelatedOrder relatedOrder;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public RelatedOrder getRelatedOrder() {
        return relatedOrder;
    }

    public String getStatus() {
        return status;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
