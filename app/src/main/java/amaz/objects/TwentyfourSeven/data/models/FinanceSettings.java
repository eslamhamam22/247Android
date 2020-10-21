package amaz.objects.TwentyfourSeven.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FinanceSettings implements Serializable {

    @SerializedName("vat_type")
    @Expose
    private String vatType;

    @SerializedName("vat_value")
    @Expose
    private String vatValue;

    @SerializedName("commission_type")
    @Expose
    private String commissionType;

    @SerializedName("commission_value")
    @Expose
    private String commissionValue;

    @SerializedName("min_mileage_cost")
    @Expose
    private String minMileageCost;

    @SerializedName("max_mileage_cost")
    @Expose
    private String maxMileageCost;

    @SerializedName("min_fixed_cost")
    @Expose
    private String minFixedCost;

    public String getVatType() {
        return vatType;
    }

    public void setVatType(String vatType) {
        this.vatType = vatType;
    }

    public String getVatValue() {
        return vatValue;
    }

    public void setVatValue(String vatValue) {
        this.vatValue = vatValue;
    }

    public String getCommissionType() {
        return commissionType;
    }

    public void setCommissionType(String commissionType) {
        this.commissionType = commissionType;
    }

    public String getCommissionValue() {
        return commissionValue;
    }

    public void setCommissionValue(String commissionValue) {
        this.commissionValue = commissionValue;
    }

    public String getMinMileageCost() {
        return minMileageCost;
    }

    public void setMinMileageCost(String minMileageCost) {
        this.minMileageCost = minMileageCost;
    }

    public String getMaxMileageCost() {
        return maxMileageCost;
    }

    public void setMaxMileageCost(String maxMileageCost) {
        this.maxMileageCost = maxMileageCost;
    }

    public String getMinFixedCost() {
        return minFixedCost;
    }

    public void setMinFixedCost(String minFixedCost) {
        this.minFixedCost = minFixedCost;
    }
}
