package amaz.objects.TwentyfourSeven.data.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import amaz.objects.TwentyfourSeven.data.models.Wallet;

public class WalletResponse extends BaseResponse {

    @SerializedName("data")
    @Expose
    private Wallet data = new Wallet();

    public Wallet getData() {
        return data;
    }

    public void setData(Wallet data) {
        this.data = data;
    }
}
