package amaz.objects.TwentyfourSeven.data.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import amaz.objects.TwentyfourSeven.data.models.Address;

public class MyAddressesResponse extends BaseResponse {

    @SerializedName("data")
    @Expose
    private ArrayList<Address> addresses;

    public ArrayList<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(ArrayList<Address> addresses) {
        this.addresses = addresses;
    }
}
