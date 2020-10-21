package amaz.objects.TwentyfourSeven.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Delegate  implements Serializable {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("mobile")
    @Expose
    private String mobile;

    @SerializedName("image")
    @Expose
    private ImageSizes image;

    @SerializedName("rating")
    @Expose
    private float rating = 0;

    @SerializedName("delegate_rating")
    @Expose
    private float delegateRating = 0;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public ImageSizes getImage() {
        return image;
    }

    public float getRating() {
        return rating;
    }

    public float getDelegateRating() {
        return delegateRating;
    }
}

