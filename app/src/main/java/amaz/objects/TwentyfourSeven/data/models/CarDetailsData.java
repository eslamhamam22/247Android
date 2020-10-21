package amaz.objects.TwentyfourSeven.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CarDetailsData {

    @SerializedName("driver_license_image")
    @Expose
    private ImageSizes delegateLicense;

    @SerializedName("national_id_image")
    @Expose
    private ImageSizes delegateId;

    @SerializedName("car_front_image")
    @Expose
    private ImageSizes carFront;

    @SerializedName("car_back_image")
    @Expose
    private ImageSizes carBack;

    @SerializedName("id")
    @Expose
    private long id;

    @SerializedName("car_details")
    @Expose
    private String carDetails;

    @SerializedName("active")
    @Expose
    private boolean active;

    public ImageSizes getDelegateLicense() {
        return delegateLicense;
    }

    public void setDelegateLicense(ImageSizes delegateLicense) {
        this.delegateLicense = delegateLicense;
    }

    public ImageSizes getDelegateId() {
        return delegateId;
    }

    public void setDelegateId(ImageSizes delegateId) {
        this.delegateId = delegateId;
    }

    public ImageSizes getCarFront() {
        return carFront;
    }

    public void setCarFront(ImageSizes carFront) {
        this.carFront = carFront;
    }

    public ImageSizes getCarBack() {
        return carBack;
    }

    public void setCarBack(ImageSizes carBack) {
        this.carBack = carBack;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCarDetails() {
        return carDetails;
    }

    public void setCarDetails(String carDetails) {
        this.carDetails = carDetails;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
