package amaz.objects.TwentyfourSeven.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PickItemData {

    @SerializedName("item_price")
    @Expose
    private Double itemPrice;

    @SerializedName("vat")
    @Expose
    private double vat;

    @SerializedName("commission")
    @Expose
    private double commission;

    @SerializedName("delivery_price")
    @Expose
    private double shippingCost;

    public Double getItemPrice() {
        return itemPrice;
    }


    public double getVat() {
        return vat;
    }

    public double getCommission() {
        return commission;
    }

    public double getShippingCost() {
        return shippingCost;
    }
}
