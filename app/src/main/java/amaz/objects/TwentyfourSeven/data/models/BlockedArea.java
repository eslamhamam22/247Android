package amaz.objects.TwentyfourSeven.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BlockedArea implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("encoded_path")
    @Expose
    private String encoded_path;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEncoded_path() {
        return encoded_path;
    }

    public void setEncoded_path(String encoded_path) {
        this.encoded_path = encoded_path;
    }
}
