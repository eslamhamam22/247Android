package amaz.objects.TwentyfourSeven.data.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import amaz.objects.TwentyfourSeven.data.models.BankAccount;

public class MyBankAccountsResponse extends BaseResponse {

    @SerializedName("data")
    @Expose
    private ArrayList<BankAccount> bankAccounts;

    public ArrayList<BankAccount> getData() {
        return bankAccounts;
    }

    public void setData(ArrayList<BankAccount> bankAccounts) {
        this.bankAccounts = bankAccounts;
    }
}
