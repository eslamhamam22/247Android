package amaz.objects.TwentyfourSeven.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
}
