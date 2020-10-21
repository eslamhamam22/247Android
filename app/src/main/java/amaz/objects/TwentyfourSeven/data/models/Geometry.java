package amaz.objects.TwentyfourSeven.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Geometry implements Serializable {

    @SerializedName("location")
    @Expose
    private PlaceLocation location;

    public Geometry(PlaceLocation location) {
        this.location = location;
    }

    public PlaceLocation getLocation() {
        return location;
    }

    public void setLocation(PlaceLocation location) {
        this.location = location;
    }
}
