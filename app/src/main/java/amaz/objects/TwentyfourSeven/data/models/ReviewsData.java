package amaz.objects.TwentyfourSeven.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ReviewsData {

    @SerializedName("user")
    @Expose
    private User user;

    @SerializedName("reviews")
    @Expose
    private ArrayList<Review> reviews;

    public User getUser() {
        return user;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }
}
