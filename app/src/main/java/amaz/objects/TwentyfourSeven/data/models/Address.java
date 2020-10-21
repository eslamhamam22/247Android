package amaz.objects.TwentyfourSeven.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Address implements Serializable {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("address_title")
    @Expose
    private String addressTitle;

    @SerializedName("address")
    @Expose
    private String addressDetails;

    @SerializedName("lat")
    @Expose
    private double latitude;

    @SerializedName("lng")
    @Expose
    private double longitude;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddressTitle() {
        return addressTitle;
    }

    public void setAddressTitle(String addressTitle) {
        this.addressTitle = addressTitle;
    }

    public String getAddressDetails() {
        return addressDetails;
    }

    public void setAddressDetails(String addressDetails) {
        this.addressDetails = addressDetails;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
