package amaz.objects.TwentyfourSeven.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DelegateGeoFireLocation {

    @SerializedName("g")
    @Expose
    private String key;

    @SerializedName("l")
    @Expose
    private DelegateGeoFireLatLng delegateLatLng;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public DelegateGeoFireLatLng getDelegateLatLng() {
        return delegateLatLng;
    }

    public void setDelegateLatLng(DelegateGeoFireLatLng delegateLatLng) {
        this.delegateLatLng = delegateLatLng;
    }
}
