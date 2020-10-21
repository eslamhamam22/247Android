package amaz.objects.TwentyfourSeven.data.models;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CancelationReason  implements Serializable  {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("title")
    @Expose
    private String title;

    public CancelationReason(int id, String title){
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @NonNull
    @Override
    public String toString() {
        return this.title;
    }
}
