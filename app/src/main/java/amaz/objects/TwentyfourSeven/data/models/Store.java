package amaz.objects.TwentyfourSeven.data.models;

import com.google.common.collect.ArrayListMultimap;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Store implements Serializable {

    @SerializedName("place_id")
    @Expose
    private String placeId;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("icon")
    @Expose
    private String icon;

    @SerializedName("geometry")
    @Expose
    private Geometry geometry;

    @SerializedName("opening_hours")
    @Expose
    private OpeningHours openingHours;

    @SerializedName("vicinity")
    @Expose
    private String address;

    @SerializedName("photos")
    @Expose
    private ArrayList<PlacePhoto> placePhotos;

    private boolean isLoaded = false;

    public Store(String placeId, String name, String icon, Geometry geometry, OpeningHours openingHours, String address, ArrayList<PlacePhoto> placePhotos) {
        this.placeId = placeId;
        this.name = name;
        this.icon = icon;
        this.geometry = geometry;
        this.openingHours = openingHours;
        this.address = address;
        this.placePhotos = placePhotos;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public OpeningHours getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(OpeningHours openingHours) {
        this.openingHours = openingHours;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ArrayList<PlacePhoto> getPlacePhotos() {
        return placePhotos;
    }

    public void setPlacePhotos(ArrayList<PlacePhoto> placePhotos) {
        this.placePhotos = placePhotos;
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public void setLoaded(boolean loaded) {
        isLoaded = loaded;
    }
}
