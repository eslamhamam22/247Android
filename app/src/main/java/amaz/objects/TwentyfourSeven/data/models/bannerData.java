package amaz.objects.TwentyfourSeven.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class bannerData {

    @SerializedName("image")
    @Expose
    private ImageSizes image;

    public ImageSizes getImage() {
        return image;
    }

    public void setImage(ImageSizes image) {
        this.image = image;
    }
}
