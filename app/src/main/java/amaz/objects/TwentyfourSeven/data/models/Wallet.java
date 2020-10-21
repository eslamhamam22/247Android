package amaz.objects.TwentyfourSeven.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Wallet implements Serializable {



    @SerializedName("balance")
    @Expose
    private float balance = 0;

    @SerializedName("earnings")
    @Expose
    private float earnings = 0;

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public float getEarnings() {
        return earnings;
    }

    public void setEarnings(float earnings) {
        this.earnings = earnings;
    }
}
