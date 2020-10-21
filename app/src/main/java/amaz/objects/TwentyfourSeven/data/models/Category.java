package amaz.objects.TwentyfourSeven.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Category implements Serializable {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("icon")
    @Expose
    private ImageSizes icon;

    @SerializedName("related_category")
    @Expose
    private String relatedCategory;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ImageSizes getIcon() {
        return icon;
    }

    public void setIcon(ImageSizes icon) {
        this.icon = icon;
    }

    public String getRelatedCategory() {
        return relatedCategory;
    }

    public void setRelatedCategory(String relatedCategory) {
        this.relatedCategory = relatedCategory;
    }

}
