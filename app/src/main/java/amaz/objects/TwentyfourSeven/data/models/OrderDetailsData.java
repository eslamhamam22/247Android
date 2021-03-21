package amaz.objects.TwentyfourSeven.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class OrderDetailsData {

    @SerializedName("order")
    @Expose
    private Order order;

    @SerializedName("free_commission")
    @Expose
    private boolean freeCommission;

    public Order getOrder() {
        return order;
    }

    public boolean isFreeCommission() {
        return freeCommission;
    }

    @SerializedName("customer_tokens")
    @Expose
    private ArrayList<String> customerTokens;

    public ArrayList<String> getCustomerTokens() {
        return customerTokens;
    }

    @SerializedName("delegate_tokens")
    @Expose
    private ArrayList<String> delegateTokens;

    public ArrayList<String> getDelegateTokens() {
        return delegateTokens;
    }

}
