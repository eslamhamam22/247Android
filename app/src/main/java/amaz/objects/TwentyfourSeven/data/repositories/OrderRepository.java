package amaz.objects.TwentyfourSeven.data.repositories;

import java.io.File;

import amaz.objects.TwentyfourSeven.listeners.OnResponseListener;

public interface OrderRepository {

    void uploadOrderImages(String token, String locale, File imageFile, final OnResponseListener onUploadImageResponse);
    void deleteOrderImages(String token, String locale, long id, final OnResponseListener onUploadImageResponse);
    void createOrder(String token, String locale, String orderDescription, int fromType, double fromLat, double fromLng, String fromAddress,
                     double toLat, double toLng, String toAddress, String storeName, String imagesIds, int deliveryDuration,
                     String couponCode, boolean isReorder, final OnResponseListener onCeateOrderResponse);
    void uploadeOrdervoice(String token, String locale, File voiceFile, final OnResponseListener onUploadVoiceResponse);
    void deleteOrderVoice(String token, String locale, long id, final OnResponseListener onDeleteOrderVoiceResponse);
    void getCustomerOrderDetails(String token, String locale, int id, final OnResponseListener onGetCustomerOrderDetailsResponse);
    void getDelegateOrderDetails(String token, String locale, int id, final OnResponseListener onGetDelegateOrderDetailsResponse);
    void addOffer(String token, String locale, int id, double shippingCost, double distToPickUp, double distToDelivery, double delegateLat,
                  double delegateLng, final OnResponseListener onAddOfferResponse);

    void cancelOffer(String token, String locale, String id, final OnResponseListener onCancelresponse);
    void acceptOffer(String token, String locale, long id, final OnResponseListener onAcceptOfferResponse);
    void rejectOffer(String token, String locale, long id, final OnResponseListener onRejectOfferResponse);
    void researchDelegates(String token, String locale, int id, final OnResponseListener onResearchDelegatesResponse);
    void startRide(String token, String locale, int id, final OnResponseListener onStartRideResponse);
    void pickItem(String token, String locale, int id, double itemPrice, final OnResponseListener onPickItemResponse);
    void pickItem(String token, String locale, int id, final OnResponseListener onPickItemResponse);
    void deliverOrder(String token, String locale, int id, String appVersion, final OnResponseListener onDeliverOrderResponse);
    void cancelOrder(String token, String locale, String id,boolean isDelegate,int reason, final OnResponseListener onCancelresponse);
    void delegateRateOrder(String token, String locale, int id, int rateNum, String comment, boolean isDelegateRate, final OnResponseListener onDelegateRateOrderResponse);
    void uploadChatImage(String token, String locale, File imageFile,String id, final OnResponseListener onUploadImageResponse);
    void submitComplaint(String token, String locale, int orderId, String title, String description, final OnResponseListener onSubmitComplaintResponse);
    void getCancelationReasons(String locale, int customerOrDelegate, final OnResponseListener onGetCancelationReasonsResponse);
    void ignoreOrder(String token, String locale, int id, final OnResponseListener onIgnoreOrderResponse);
    void getPrayerTimes(String locale, OnResponseListener onGetPrayerTimesResponse);
    void validateCoupon(String token, String locale, String code, OnResponseListener onValidateCouponResponse);

}
