package amaz.objects.TwentyfourSeven.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContactUsData {

    @SerializedName("mobile")
    @Expose
    private String mobile;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("fax_number")
    @Expose
    private String fax;

    @SerializedName("lat")
    @Expose
    private double latitude;

    @SerializedName("lng")
    @Expose
    private double longitude;

    @SerializedName("address_en")
    @Expose
    private String englishAddress;

    @SerializedName("address_ar")
    @Expose
    private String arabicAddress;

    @SerializedName("address_map")
    @Expose
    private String mapAddress;

    @SerializedName("facebook_url")
    @Expose
    private String facebookUrl;

    @SerializedName("twitter_url")
    @Expose
    private String twitterUrl;

    @SerializedName("google_url")
    @Expose
    private String googleUrl;

    @SerializedName("instagram_url")
    @Expose
    private String instagramUrl;

    @SerializedName("site_url")
    @Expose
    private String siteUrl;


    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
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

    public String getEnglishAddress() {
        return englishAddress;
    }

    public void setEnglishAddress(String englishAddress) {
        this.englishAddress = englishAddress;
    }

    public String getArabicAddress() {
        return arabicAddress;
    }

    public void setArabicAddress(String arabicAddress) {
        this.arabicAddress = arabicAddress;
    }

    public String getMapAddress() {
        return mapAddress;
    }

    public void setMapAddress(String mapAddress) {
        this.mapAddress = mapAddress;
    }

    public String getFacebookUrl() {
        return facebookUrl;
    }

    public void setFacebookUrl(String facebookUrl) {
        this.facebookUrl = facebookUrl;
    }

    public String getTwitterUrl() {
        return twitterUrl;
    }

    public void setTwitterUrl(String twitterUrl) {
        this.twitterUrl = twitterUrl;
    }

    public String getGoogleUrl() {
        return googleUrl;
    }

    public void setGoogleUrl(String googleUrl) {
        this.googleUrl = googleUrl;
    }

    public String getInstagramUrl() {
        return instagramUrl;
    }

    public void setInstagramUrl(String instagramUrl) {
        this.instagramUrl = instagramUrl;
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }
}
