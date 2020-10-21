package amaz.objects.TwentyfourSeven.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SocialLoginData {

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("refresh_token")
    @Expose
    private String refreshToken;

    @SerializedName("firebase_token")
    @Expose
    private String firebaseToken;

    @SerializedName("user")
    @Expose
    private User user;

    @SerializedName("registeredBefore")
    @Expose
    private boolean registeredBefore;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isRegisteredBefore() {
        return registeredBefore;
    }

    public void setRegisteredBefore(boolean registeredBefore) {
        this.registeredBefore = registeredBefore;
    }

    public String getFirebaseToken() {
        return firebaseToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }
}
