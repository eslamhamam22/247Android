package amaz.objects.TwentyfourSeven.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DirectionLeg {

    @SerializedName("distance")
    @Expose
    private RouteDistance distance;

    public RouteDistance getDistance() {
        return distance;
    }

    public void setDistance(RouteDistance distance) {
        this.distance = distance;
    }
}
