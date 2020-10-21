package amaz.objects.TwentyfourSeven.data.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransactionModel {

    @SerializedName("merchantID")
    @Expose
    private String MerchantID;

    @SerializedName("branchID")
    @Expose
    private String BranchID;

    @SerializedName("tellerID")
    @Expose
    private String TellerID;

    @SerializedName("deviceID")
    @Expose
    private String DeviceID;

    @SerializedName("refNum")
    @Expose
    private String RefNum;

    @SerializedName("sTCPayRefNum")
    @Expose
    private String STCPayRefNum;

    @SerializedName("amountReversed")
    @Expose
    private int AmountReversed;

    @SerializedName("amount")
    @Expose
    private int Amount;

    @SerializedName("paymentDate")
    @Expose
    private String PaymentDate;

    @SerializedName("paymentStatus")
    @Expose
    private int PaymentStatus;

    @SerializedName("paymentStatusDesc")
    @Expose
    private String PaymentStatusDesc;

    public String getMerchantID() {
        return MerchantID;
    }

    public void setMerchantID(String merchantID) {
        this.MerchantID = merchantID;
    }

    public String getBranchID() {
        return BranchID;
    }

    public void setBranchID(String branchID) { this.BranchID = branchID; }

    public String getTellerID() {
        return TellerID;
    }

    public void setTellerID(String tellerID) { this.TellerID = tellerID; }

    public String getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(String deviceID) { this.DeviceID = deviceID; }

    public String getRefNum() {
        return RefNum;
    }

    public void setRefNum(String refNum) { this.RefNum = refNum; }

    public String getSTCPayRefNum() {
        return STCPayRefNum;
    }

    public void setSTCPayRefNum(String sTCPayRefNum) { this.STCPayRefNum = sTCPayRefNum; }

    public int getAmountReversed() {
        return AmountReversed;
    }

    public void setAmountReversed(int amountReversed) { this.AmountReversed = amountReversed; }

    public int getAmount() {
        return Amount;
    }

    public void setAmount(int amount) {
        this.Amount = amount;
    }

    public String getPaymentDate() {
        return PaymentDate;
    }

    public void setPaymentDate(String paymentDate) { this.PaymentDate = paymentDate; }

    public int getPaymentStatus() {
        return PaymentStatus;
    }

    public void setPaymentStatus(int paymentStatus) {
        this.PaymentStatus = paymentStatus;
    }

    public String getPaymentStatusDesc() {
        return PaymentStatusDesc;
    }

    public void setPaymentStatusDesc(String paymentStatusDesc) { this.PaymentStatusDesc = paymentStatusDesc; }

}
