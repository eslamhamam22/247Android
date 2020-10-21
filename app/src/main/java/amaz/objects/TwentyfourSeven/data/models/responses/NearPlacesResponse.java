package amaz.objects.TwentyfourSeven.data.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

import amaz.objects.TwentyfourSeven.data.models.Store;


public class NearPlacesResponse implements Serializable {

    @SerializedName("next_page_token")
    @Expose
    private String nextPageToken;

    @SerializedName("results")
    @Expose
    private ArrayList<Store> results;

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public ArrayList<Store> getResults() {
        return results;
    }

    public void setResults(ArrayList<Store> results) {
        this.results = results;
    }
}
