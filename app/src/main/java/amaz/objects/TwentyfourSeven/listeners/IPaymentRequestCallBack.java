package amaz.objects.TwentyfourSeven.listeners;

import amaz.objects.TwentyfourSeven.data.models.PayFortData;

/**
 * Created by Asmaagaber on 4/29/18.
 */

public interface IPaymentRequestCallBack {
 void onPaymentRequestResponse(int responseType, PayFortData responseData);
 void setToken(String token);

}