package amaz.objects.TwentyfourSeven.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PageData {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("content")
    @Expose
    private String content;

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("slug")
    @Expose
    private String slug;

    @SerializedName("image")
    @Expose
    private ImageSizes image;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public ImageSizes getImage() {
        return image;
    }

    public void setImage(ImageSizes image) {
        this.image = image;
    }
}
