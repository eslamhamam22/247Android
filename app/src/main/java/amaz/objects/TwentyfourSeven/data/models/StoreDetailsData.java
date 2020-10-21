package amaz.objects.TwentyfourSeven.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class StoreDetailsData {

    @SerializedName("formatted_address")
    @Expose
    private String formattedAddress;

    @SerializedName("geometry")
    @Expose
    private Geometry geometry;

    @SerializedName("icon")
    @Expose
    private String icon;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("opening_hours")
    @Expose
    private OpeningHours openingHours;

    @SerializedName("vicinity")
    @Expose
    private String vicinity;

    @SerializedName("types")
    @Expose
    private ArrayList<String> types;

    @SerializedName("photos")
    @Expose
    private ArrayList<PlacePhoto> placePhotos;

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OpeningHours getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(OpeningHours openingHours) {
        this.openingHours = openingHours;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public ArrayList<String> getTypes() {
        return types;
    }

    public void setTypes(ArrayList<String> types) {
        this.types = types;
    }

    public ArrayList<PlacePhoto> getPlacePhotos() {
        return placePhotos;
    }

    public void setPlacePhotos(ArrayList<PlacePhoto> placePhotos) {
        this.placePhotos = placePhotos;
    }
}
