package amaz.objects.TwentyfourSeven.data.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import amaz.objects.TwentyfourSeven.data.models.Order;

public class MyOrdersResponse extends BaseResponse {

    @SerializedName("data")
    @Expose
    private ArrayList<Order> orders;

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<Order> orders) {
        this.orders = orders;
    }
}
