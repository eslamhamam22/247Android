package amaz.objects.TwentyfourSeven.data.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import amaz.objects.TwentyfourSeven.data.models.UserTransaction;

public class MyBalanceResponse extends BaseResponse {

    @SerializedName("data")
    @Expose
    private ArrayList<UserTransaction> userTransactions;

    public ArrayList<UserTransaction> getUserTransactions() {
        return userTransactions;
    }

    public void setUserTransactions(ArrayList<UserTransaction> userTransactions) {
        this.userTransactions = userTransactions;
    }
}
