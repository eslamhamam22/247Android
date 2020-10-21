package amaz.objects.TwentyfourSeven.data.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import amaz.objects.TwentyfourSeven.data.models.DelegateActivationRequest;
import amaz.objects.TwentyfourSeven.data.models.User;

public class MyDelegateStateResponse extends BaseResponse {

    @SerializedName("data")
    @Expose
    public DelegateActivationRequest delegateActivationRequest;


}
