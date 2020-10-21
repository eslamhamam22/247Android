package amaz.objects.TwentyfourSeven.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Offer implements Serializable {

    @SerializedName("id")
    @Expose
    private long id;

    @SerializedName("cost")
    @Expose
    private double shippingCost;

    @SerializedName("vat")
    @Expose
    private double vat;

    @SerializedName("commission")
    @Expose
    private double commission;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("dist_to_pickup")
    @Expose
    private double distanceToPickup;

    @SerializedName("dist_to_delivery")
    @Expose
    private double distanceToDelivery;

    @SerializedName("delegate_address")
    @Expose
    private String delegateAddress;

    @SerializedName("delegate")
    @Expose
    private Delegate delegate;

    public long getId() {
        return id;
    }

    public double getShippingCost() {
        return shippingCost;
    }

    public double getVat() {
        return vat;
    }

    public double getCommission() {
        return commission;
    }

    public String getStatus() {
        return status;
    }

    public double getDistanceToPickup() {
        return distanceToPickup;
    }

    public double getDistanceToDelivery() {
        return distanceToDelivery;
    }

    public String getDelegateAddress() {
        return delegateAddress;
    }

    public Delegate getDelegate() {
        return delegate;
    }
}
