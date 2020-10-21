package amaz.objects.TwentyfourSeven.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("image")
    @Expose
    private ImageSizes image;

    @SerializedName("mobile")
    @Expose
    private String mobile;

    @SerializedName("birthdate")
    @Expose
    private String birthDate;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("gender")
    @Expose
    private String gender;

    @SerializedName("city")
    @Expose
    private String city;

    @SerializedName("lang")
    @Expose
    private String language;

    @SerializedName("is_delegate")
    @Expose
    private boolean isDelegate;

    @SerializedName("balance")
    @Expose
    private Double wallet_value = 0.0;

    @SerializedName("delegate_details")
    @Expose
    private DelegateActivationRequest isActiveDelegate = new DelegateActivationRequest();

    @SerializedName("has_delegate_request")
    @Expose
    private boolean hasDelegateRequest;

    @SerializedName("rating")
    @Expose
    private Double rating;

    @SerializedName("delegate_rating")
    @Expose
    private Double delegateRating;

    @SerializedName("orders_count")
    @Expose
    private int ordersNo;

    @SerializedName("delegate_orders_count")
    @Expose
    private int delegateOrdersNo;

    @SerializedName("unseen_notifications_count")
    @Expose
    private int unseen_notifications_count = 0;

    @SerializedName("notifications_enabled")
    @Expose
    private boolean notifications_enabled= true;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ImageSizes getImage() {
        return image;
    }

    public void setImage(ImageSizes image) {
        this.image = image;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean isDelegate() {
        return isDelegate;
    }

    public void setDelegate(boolean delegate) {
        isDelegate = delegate;
    }

    public Double getWallet_value() {
        return wallet_value;
    }

    public void setWallet_value(double wallet_value) {
        this.wallet_value = wallet_value;
    }

    public DelegateActivationRequest getIsActiveDelegate() {
        return isActiveDelegate;
    }

    public void setIsActiveDelegate(DelegateActivationRequest isActiveDelegate) {
        this.isActiveDelegate = isActiveDelegate;
    }

    public boolean isHasDelegateRequest() {
        return hasDelegateRequest;
    }

    public void setHasDelegateRequest(boolean hasDelegateRequest) {
        this.hasDelegateRequest = hasDelegateRequest;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Double getDelegateRating() {
        return delegateRating;
    }

    public void setDelegateRating(Double delegateRating) {
        this.delegateRating = delegateRating;
    }

    public int getOrdersNo() {
        return ordersNo;
    }

    public void setOrdersNo(int ordersNo) {
        this.ordersNo = ordersNo;
    }

    public int getDelegateOrdersNo() {
        return delegateOrdersNo;
    }

    public void setDelegateOrdersNo(int delegateOrdersNo) {
        this.delegateOrdersNo = delegateOrdersNo;
    }

    public int getUnseen_notifications_count() {
        return unseen_notifications_count;
    }

    public void setUnseen_notifications_count(int unseen_notifications_count) {
        this.unseen_notifications_count = unseen_notifications_count;
    }

    public boolean isNotifications_enabled() {
        return notifications_enabled;
    }

    public void setNotifications_enabled(boolean notifications_enabled) {
        this.notifications_enabled = notifications_enabled;
    }
}
