package amaz.objects.TwentyfourSeven.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RouteDistance {

    @SerializedName("text")
    @Expose
    private String text;

    @SerializedName("value")
    @Expose
    private double value;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
