package amaz.objects.TwentyfourSeven.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

import amaz.objects.TwentyfourSeven.adapters.OrderImagesAdapter;

public class Order implements Serializable {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("search_delegates_result")
    @Expose
    private Boolean delegateSearchResult;

    @SerializedName("from_address")
    @Expose
    private String from_address;

    @SerializedName("to_address")
    @Expose
    private String to_address;

    @SerializedName("store_name")
    @Expose
    private String store_name;

    @SerializedName("created_at")
    @Expose
    private String created_at;

    @SerializedName("search_delegates_started_at")
    @Expose
    private String delegateSearchStartedAt;

    @SerializedName("delegate")
    @Expose
    private Delegate delegate;

    @SerializedName("from_lat")
    @Expose
    private double fromLat;

    @SerializedName("from_lng")
    @Expose
    private double fromLng;

    @SerializedName("to_lat")
    @Expose
    private double toLat;

    @SerializedName("to_lng")
    @Expose
    private double toLng;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("images")
    @Expose
    private ArrayList<DelegateImageData> images;

    @SerializedName("finance_settings")
    @Expose
    private FinanceSettings financeSettings;

    @SerializedName("offers")
    @Expose
    private ArrayList<Offer> offers;

    @SerializedName("created_by")
    @Expose
    private OrderCustomer createdBy;

    @SerializedName("vat")
    @Expose
    private double vat;

    @SerializedName("commission")
    @Expose
    private double commission;

    @SerializedName("delivery_price")
    @Expose
    private Double shippingCost;

    @SerializedName("item_price")
    @Expose
    private Double itemPrice;

    @SerializedName("total_price")
    @Expose
    private Double totalPrice;

    @SerializedName("actual_paid")
    @Expose
    private Double actualPaid;

    @SerializedName("is_rated")
    @Expose
    private boolean isRated;

    @SerializedName("from_type")
    @Expose
    private int fromType;

    @SerializedName("cancel_reason")
    @Expose
    private CancelationReason cancelationReason;

    @SerializedName("delivery_duration")
    @Expose
    private int deliveryDuration;

    @SerializedName("discount")
    @Expose
    private Double discount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getDelegateSearchResult() {
        return delegateSearchResult;
    }

    public void setDelegateSearchResult(Boolean delegateSearchResult) {
        this.delegateSearchResult = delegateSearchResult;
    }

    public String getFrom_address() {
        return from_address;
    }

    public void setFrom_address(String from_address) {
        this.from_address = from_address;
    }

    public String getTo_address() {
        return to_address;
    }

    public void setTo_address(String to_address) {
        this.to_address = to_address;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getDelegateSearchStartedAt() {
        return delegateSearchStartedAt;
    }

    public void setDelegateSearchStartedAt(String delegateSearchStartedAt) {
        this.delegateSearchStartedAt = delegateSearchStartedAt;
    }

    public Delegate getDelegate() {
        return delegate;
    }

    public void setDelegate(Delegate delegate) {
        this.delegate = delegate;
    }

    public double getFromLat() {
        return fromLat;
    }

    public void setFromLat(double fromLat) {
        this.fromLat = fromLat;
    }

    public double getFromLng() {
        return fromLng;
    }

    public void setFromLng(double fromLng) {
        this.fromLng = fromLng;
    }

    public double getToLat() {
        return toLat;
    }

    public void setToLat(double toLat) {
        this.toLat = toLat;
    }

    public double getToLng() {
        return toLng;
    }

    public void setToLng(double toLng) {
        this.toLng = toLng;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<DelegateImageData> getImages() {
        return images;
    }

    public void setImages(ArrayList<DelegateImageData> images) {
        this.images = images;
    }

    public FinanceSettings getFinanceSettings() {
        return financeSettings;
    }

    public void setFinanceSettings(FinanceSettings financeSettings) {
        this.financeSettings = financeSettings;
    }

    public ArrayList<Offer> getOffers() {
        return offers;
    }

    public void setOffers(ArrayList<Offer> offers) {
        this.offers = offers;
    }

    public OrderCustomer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(OrderCustomer createdBy) {
        this.createdBy = createdBy;
    }

    public double getVat() {
        return vat;
    }

    public void setVat(double vat) {
        this.vat = vat;
    }

    public double getCommission() {
        return commission;
    }

    public void setCommission(double commission) {
        this.commission = commission;
    }

    public Double getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(Double shippingCost) {
        this.shippingCost = shippingCost;
    }

    public Double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(Double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getActualPaid() {
        return actualPaid;
    }

    public void setActualPaid(Double actualPaid) {
        this.actualPaid = actualPaid;
    }

    public boolean isRated() {
        return isRated;
    }

    public void setRated(boolean rated) {
        isRated = rated;
    }

    public int getFromType() {
        return fromType;
    }

    public void setFromType(int fromType) {
        this.fromType = fromType;
    }

    public CancelationReason getCancelationReason() {
        return cancelationReason;
    }

    public void setCancelationReason(CancelationReason cancelationReason) {
        this.cancelationReason = cancelationReason;
    }

    public int getDeliveryDuration() {
        return deliveryDuration;
    }

    public void setDeliveryDuration(int deliveryDuration) {
        this.deliveryDuration = deliveryDuration;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }
}


