package amaz.objects.TwentyfourSeven.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CategoriesData {

    @SerializedName("categories")
    @Expose
    private ArrayList<Category> categories;

    @SerializedName("default_category")
    @Expose
    private Category defaultCategory;

    @SerializedName("banner")
    @Expose
    private bannerData banner;

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    public bannerData getBanner() {
        return banner;
    }

    public void setBanner(bannerData banner) {
        this.banner = banner;
    }

    public Category getDefaultCategory() {
        return defaultCategory;
    }

    public void setDefaultCategory(Category defaultCategory) {
        this.defaultCategory = defaultCategory;
    }
}
