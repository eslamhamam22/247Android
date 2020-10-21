package amaz.objects.TwentyfourSeven.data.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PaymentInquiryResponseMessage {

    @SerializedName("transactionList")
    @Expose
    private ArrayList<TransactionModel> TransactionList;

    public ArrayList<TransactionModel> getTransactionList() {
        return TransactionList;
    }

    public void setTransactionList(ArrayList<TransactionModel> transactionList) {
        this.TransactionList = transactionList;
    }

}
