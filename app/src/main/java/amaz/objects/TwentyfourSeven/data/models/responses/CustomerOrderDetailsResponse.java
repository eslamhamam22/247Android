package amaz.objects.TwentyfourSeven.data.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import amaz.objects.TwentyfourSeven.data.models.Order;
import amaz.objects.TwentyfourSeven.data.models.OrderDetailsData;

public class CustomerOrderDetailsResponse extends BaseResponse {

    @SerializedName("data")
    @Expose
    private OrderDetailsData data;

    public OrderDetailsData getData() {
        return data;
    }
}
