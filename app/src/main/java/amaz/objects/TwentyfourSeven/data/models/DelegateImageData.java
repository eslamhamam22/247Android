package amaz.objects.TwentyfourSeven.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DelegateImageData implements Serializable {

    @SerializedName("image")
    @Expose
    private ImageSizes image;

    @SerializedName("id")
    @Expose
    private long id;

    @SerializedName("type")
    @Expose
    private String type;

    public ImageSizes getImage() {
        return image;
    }

    public void setImage(ImageSizes data) {
        this.image = image;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
