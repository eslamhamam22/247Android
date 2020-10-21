package amaz.objects.TwentyfourSeven.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CardPayRegisterationData implements Serializable {

    @SerializedName("checkout_id")
    @Expose
    private String checkoutId;

    @SerializedName("redirect_url")
    @Expose
    private String redirectUrl;

    @SerializedName("api_url")
    @Expose
    private String apiUrl;

    @SerializedName("html")
    @Expose
    private String html;

    public String getCheckoutId() {
        return checkoutId;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public String getHtml() {
        return html;
    }
}
