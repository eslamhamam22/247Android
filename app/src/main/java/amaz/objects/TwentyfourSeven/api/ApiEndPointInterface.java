package amaz.objects.TwentyfourSeven.api;

import org.json.JSONObject;

import java.util.HashMap;

import amaz.objects.TwentyfourSeven.data.models.GoogleTokenRequestBody;
import amaz.objects.TwentyfourSeven.data.models.responses.AddOfferResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.BaseResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.BlockedAreasResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.CancelationReasonsResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.CarDetailsResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.CardPayRegisterationResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.CategoriesResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.ComplaintsResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.ContactUsResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.CustomerOrderDetailsResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.DirectPaymentAuthorizeResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.DirectPaymentAuthorizeV4ResponseMessage;
import amaz.objects.TwentyfourSeven.data.models.responses.DirectPaymentConfirmResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.DirectPaymentConfirmV4ResponseMessage;
import amaz.objects.TwentyfourSeven.data.models.responses.DirectPaymentResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.DirectionsResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.GoogleAccessTokenResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.HowToUseResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.MyAddressesResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.MyBalanceResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.MyBankAccountsResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.MyDelegateStateResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.MyNotificationResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.MyNotificationStateResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.MyOrdersResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.NearPlacesResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.PaymentInquiryResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.PrayerTimesResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.ReviewsResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.StoreDetailsResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.PageResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.ProfileResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.SocialLoginResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.UploadDelegateImagesResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.VoiceNoteResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.WalletResponse;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiEndPointInterface {

    @POST(APIURLs.GOOGLE_AUTH)
    Call<GoogleAccessTokenResponse> getGoogleAccessToken(
            @Body GoogleTokenRequestBody body);

    @FormUrlEncoded
    @POST("{local}/" + APIURLs.SOCIAL_LOGIN)
    Call<SocialLoginResponse> socialLogin(
            @Path("local") String local,
            @Field("facebookToken") String facebookToken,
            @Field("googleToken") String googleToken);

    @FormUrlEncoded
    @POST("{local}/" + APIURLs.REQUEST_VERIFICATION_CODE)
    Call<BaseResponse> requestVerificationCode(
            @Path("local") String local,
            @Field("mobile") String mobile);

    @FormUrlEncoded
    @POST("{local}/" + APIURLs.VERIFY_PHONE)
    Call<SocialLoginResponse> verifyPhoneNumber(
            @Path("local") String local,
            @Field("mobile") String mobile,
            @Field("code") String code,
            @Field("facebookToken") String facebookToken,
            @Field("googleToken") String googleToken);

    @Multipart
    @POST("{local}/" + APIURLs.UPDATE_USER_PROFILE)
    Call<ProfileResponse> updateUserProfile(@Path("local") String local,
                                            @Query("token") String token,
                                            @Part("name") RequestBody name,
                                            @Part("birthdate") RequestBody birthdate,
                                            @Part("city") RequestBody city,
                                            @Part("gender") RequestBody gender,
                                            @Part("imageFile\";filename=\"Pic.jpg\" ") RequestBody image);

    @FormUrlEncoded
    @POST("{local}/" + APIURLs.CHANGE_LANGUAGE)
    Call<ProfileResponse> changeUserLanguage(@Path("local") String local,
                                             @Query("token") String token,
                                             @Field("lang") String language);

    @GET("{local}/" + APIURLs.GET_PAGES + "/{slug}")
    Call<PageResponse> getPageData(@Path("local") String local,
                                   @Path("slug") String slug);

    @FormUrlEncoded
    @POST("{local}/" + APIURLs.CHANGE_PHONE_REQUEST_CODE)
    Call<BaseResponse> changeUserPhoneRequestCode(@Path("local") String local,
                                                  @Query("token") String token,
                                                  @Field("mobile") String mobile);

    @FormUrlEncoded
    @POST("{local}/" + APIURLs.CHANGE_PHONE_VERIFY_CODE)
    Call<SocialLoginResponse> changeUserPhoneVerifyCode(@Path("local") String local,
                                                        @Query("token") String token,
                                                        @Field("mobile") String mobile,
                                                        @Field("code") String code);

    @FormUrlEncoded
    @POST("{local}/" + APIURLs.REFRESH_TOKEN)
    Call<SocialLoginResponse> refreshToken(@Path("local") String local,
                                           @Query("token") String token,
                                           @Field("refresh_token") String refreshToken);

    @GET("{local}/" + APIURLs.CONTACT_US)
    Call<ContactUsResponse> getContactUsData(@Path("local") String local);

    @GET("{locale}/" + APIURLs.MY_ADDRESSES)
    Call<MyAddressesResponse> getMyAddresses(@Path("locale") String local,
                                             @Query("token") String token);

    @DELETE("{locale}/" + APIURLs.MY_ADDRESSES + "/{id}")
    Call<BaseResponse> deleteAddress(@Path("locale") String local,
                                     @Path("id") int id,
                                     @Query("token") String token);

    @FormUrlEncoded
    @POST("{locale}/" + APIURLs.MY_ADDRESSES)
    Call<BaseResponse> addAddress(@Path("locale") String local,
                                  @Query("token") String token,
                                  @Field("addressTitle") String addressTitle,
                                  @Field("address") String address,
                                  @Field("lat") double lat,
                                  @Field("lng") double lng);

    @GET("{locale}/" + APIURLs.HOW_TO_USE)
    Call<HowToUseResponse> getHowToUseData(@Path("locale") String local);

    @GET("{locale}/" + APIURLs.HOW_TO_BECOME_A_DELEGATE)
    Call<HowToUseResponse> getHowToBecomeADelegateData(@Path("locale") String local);

    @Multipart
    @POST("{local}/" + APIURLs.UPLOAD_DELEGATE_IMAGES)
    Call<UploadDelegateImagesResponse> uploadDelegateImages(@Path("local") String local,
                                                            @Query("token") String token,
                                                            @Part("type") RequestBody type,
                                                            @Part("imageFile\";filename=\"Pic.jpg\" ") RequestBody image);

    @DELETE("{local}/" + APIURLs.UPLOAD_DELEGATE_IMAGES + "/{id}")
    Call<BaseResponse> deleteDelegateImage(@Path("local") String local,
                                           @Path("id") long id,
                                           @Query("token") String token);

    @FormUrlEncoded
    @POST("{local}/" + APIURLs.SUBMIT_DELEGATE_REQUEST)
    Call<BaseResponse> submitDelegateRequest(@Path("local") String local,
                                             @Query("token") String token,
                                             @Field("carDetails") String carDetails,
                                             @Field("idNumber") String idNumber,
                                             @Field("images") String images);

    @GET("{locale}/" + APIURLs.GET_CAR_DETAILS)
    Call<CarDetailsResponse> getCarDetails(@Path("locale") String local,
                                           @Query("token") String token);


    @GET("{local}/" + APIURLs.GET_NOTIFICATION)
    Call<MyNotificationResponse> getNotification(@Path("local") String local,
                                                 @Query("token") String token,
                                                 @Query("page") int page,
                                                 @Query("limit") int limit);

    @POST("{local}/" + APIURLs.MARKREAD)
    Call<BaseResponse> markAsRead(@Path("local") String local,
                                  @Query("token") String token);

    @FormUrlEncoded
    @POST("{local}/" + APIURLs.GET_NOTIFICATION)
    Call<MyNotificationStateResponse> setNotificationState(@Path("local") String local,
                                                           @Query("token") String token,
                                                           @Field("state") boolean state
    );

    @FormUrlEncoded
    @POST("{local}/" + APIURLs.CHANGE_DELEGATE_STATUS)
    Call<MyDelegateStateResponse> changeDelegateStatus(@Path("local") String local,
                                                       @Query("token") String token,
                                                       @Field("state") String state
    );


    @DELETE("{local}/" + APIURLs.REGISTERDEVICE + "/{playerId}")
    Call<BaseResponse> unRegisterDeviceToken(@Path("local") String local,
                                             @Path("playerId") String playerId,
                                             @Query("token") String token);

    @GET("{local}/" + APIURLs.UPDATE_USER_PROFILE)
    Call<ProfileResponse> getUserProfile(@Path("local") String local,
                                         @Query("token") String token);

    @FormUrlEncoded
    @POST("{local}/" + APIURLs.REGISTERDEVICE)
    Call<BaseResponse> sendDeviceId(@Path("local") String local,
                                    @Query("token") String token,
                                    @Field("playerId") String playerId);

    @GET("{local}/" + APIURLs.GET_CATEGORIES)
    Call<CategoriesResponse> getCategories(@Path("local") String local);

    @GET(APIURLs.GET_NEAR_PLACES)
    Call<NearPlacesResponse> getNearestStores(@Query("location") String location,
                                              @Query("type") String type,
                                              @Query("language") String language,
                                              @Query("rankby") String rankby,
                                              @Query("key") String key);

    @GET(APIURLs.GET_NEAR_PLACES)
    Call<NearPlacesResponse> getOpenedStores(@Query("location") String location,
                                             @Query("type") String type,
                                             @Query("language") String language,
                                             @Query("rankby") String rankby,
                                             @Query("radius") int radius,
                                             @Query("opennow") boolean openNow,
                                             @Query("key") String key);

    @GET(APIURLs.GET_NEAR_PLACES)
    Call<NearPlacesResponse> getNextNearStores(@Query("pagetoken") String nextPageToken,
                                               @Query("location") String location,
                                               @Query("type") String type,
                                               @Query("language") String language,
                                               @Query("rankby") String rankby,
                                               @Query("key") String key);

    @GET(APIURLs.GET_NEAR_PLACES)
    Call<NearPlacesResponse> getNextOpenedStores(@Query("pagetoken") String nextPageToken,
                                                 @Query("location") String location,
                                                 @Query("type") String type,
                                                 @Query("language") String language,
                                                 @Query("rankby") String rankby,
                                                 @Query("radius") int radius,
                                                 @Query("opennow") boolean openNow,
                                                 @Query("key") String key);

    @GET(APIURLs.GET_NEAR_PLACES)
    Call<NearPlacesResponse> getAllStores(@Query("location") String location,
                                          @Query("type") String type,
                                          @Query("language") String language,
                                          @Query("rankby") String rankby,
                                          @Query("radius") int radius,
                                          @Query("key") String key);

    @GET(APIURLs.GET_NEAR_PLACES)
    Call<NearPlacesResponse> getNextAllStores(@Query("pagetoken") String nextPageToken,
                                              @Query("location") String location,
                                              @Query("type") String type,
                                              @Query("language") String language,
                                              @Query("rankby") String rankby,
                                              @Query("radius") int radius,
                                              @Query("key") String key);

    @GET(APIURLs.GET_STORE_WORKING_HOURES)
    Call<StoreDetailsResponse> getStoreWorkingHoures(@Query("placeid") String placeid,
                                                     @Query("fields") String fields,
                                                     @Query("language") String language,
                                                     @Query("key") String key);

    @Multipart
    @POST("{local}/" + APIURLs.ORDER_IMAGES)
    Call<UploadDelegateImagesResponse> uploadOrderImages(@Path("local") String local,
                                                         @Query("token") String token,
                                                         @Part("imageFile\";filename=\"Pic.jpg\" ") RequestBody image);

    @DELETE("{local}/" + APIURLs.ORDER_IMAGES + "/{id}")
    Call<BaseResponse> deleteOrderImage(@Path("local") String local,
                                        @Path("id") long id,
                                        @Query("token") String token);

    @FormUrlEncoded
    @POST("{locale}/" + APIURLs.CREATE_ORDER)
    Call<CustomerOrderDetailsResponse> createOrder(@Path("locale") String local,
                                                   @Query("token") String token,
                                                   @Field("description") String orderDescription,
                                                   @Field("fromType") int fromType,
                                                   @Field("fromLat") double fromLat,
                                                   @Field("fromLng") double fromLng,
                                                   @Field("fromAddress") String fromAddress,
                                                   @Field("toLat") double toLat,
                                                   @Field("toLng") double toLng,
                                                   @Field("toAddress") String toAddress,
                                                   @Field("storeName") String storeName,
                                                   @Field("images") String imagesIds,
                                                   @Field("is_reorder") boolean isReorder,
                                                   @Field("deliveryDuration") int deliveryDuration,
                                                   @Field("couponCode") String couponCode);

    @Multipart
    @POST("{local}/" + APIURLs.ORDER_VOICE)
    Call<VoiceNoteResponse> uploadOrderVoice(@Path("local") String local,
                                             @Query("token") String token,
                                             @Part("voicenoteFile\";filename=\"record.wav\" ") RequestBody voiceNote);

    @DELETE("{local}/" + APIURLs.ORDER_VOICE + "/{id}")
    Call<BaseResponse> deleteOrderVoice(@Path("local") String local,
                                        @Path("id") long id,
                                        @Query("token") String token);

    @GET("{local}/" + APIURLs.GET_CURRENT_USER_ORDER)
    Call<MyOrdersResponse> getCurrentUserOrders(@Path("local") String local,
                                                @Query("token") String token,
                                                @Query("page") int page,
                                                @Query("limit") int limit);

    @GET("{local}/" + APIURLs.GET_History_USER_ORDER)
    Call<MyOrdersResponse> getHistoryUserOrder(@Path("local") String local,
                                               @Query("token") String token,
                                               @Query("page") int page,
                                               @Query("limit") int limit);

    @GET("{local}/" + APIURLs.GET_CURRENT_DELEGTE_ORDER)
    Call<MyOrdersResponse> getCurrentDelegateOrders(@Path("local") String local,
                                                    @Query("token") String token,
                                                    @Query("page") int page,
                                                    @Query("limit") int limit);

    @GET("{local}/" + APIURLs.GET_History_DELEGTE_ORDER)
    Call<MyOrdersResponse> getHistoryDelegateOrder(@Path("local") String local,
                                                   @Query("token") String token,
                                                   @Query("page") int page,
                                                   @Query("limit") int limit);

    @GET("{local}/" + APIURLs.GET_CUSTOMER_ORDER_DETAILS + "/{id}")
    Call<CustomerOrderDetailsResponse> getCustomerOrderDetails(@Path("local") String local,
                                                               @Path("id") int id,
                                                               @Query("token") String token);

    @GET("{local}/" + APIURLs.GET_DELEGATE_ORDER_DETAILS + "/{id}")
    Call<CustomerOrderDetailsResponse> getDelegateOrderDetails(@Path("local") String local,
                                                               @Path("id") int id,
                                                               @Query("token") String token);

    @GET(APIURLs.GET_DIRECTIONS)
    Call<DirectionsResponse> getDirections(@Query("origin") String origin,
                                           @Query("destination") String destination,
                                           @Query("mode") String mode,
                                           @Query("key") String key);

    @FormUrlEncoded
    @POST("{locale}/" + APIURLs.GET_DELEGATE_ORDER_DETAILS + "/{id}" + APIURLs.ADD_OFFER)
    Call<AddOfferResponse> addOffer(@Path("locale") String local,
                                    @Path("id") int orderId,
                                    @Query("token") String token,
                                    @Field("cost") double shippingCost,
                                    @Field("distToPickup") double distToPickup,
                                    @Field("distToDelivery") double distToDelivery,
                                    @Field("delegateLat") double delegateLat,
                                    @Field("delegateLng") double delegateLng);

    @POST("{locale}/" + APIURLs.GET_DELEGATE_ORDER_DETAILS + APIURLs.ADD_OFFER + "/{id}" + APIURLs.CANCELOFFER)
    Call<BaseResponse> cancelOffer(@Path("locale") String local,
                                   @Path("id") String offerId,
                                   @Query("token") String token);

    @POST("{locale}/" + APIURLs.GET_CUSTOMER_ORDER_DETAILS + APIURLs.ADD_OFFER + "/{id}" + APIURLs.ACCEPT_OFFER)
    Call<CustomerOrderDetailsResponse> acceptOffer(@Path("locale") String local,
                                                   @Path("id") long offerId,
                                                   @Query("token") String token);

    @POST("{locale}/" + APIURLs.GET_CUSTOMER_ORDER_DETAILS + APIURLs.ADD_OFFER + "/{id}" + APIURLs.REJECT_OFFER)
    Call<CustomerOrderDetailsResponse> rejectOffer(@Path("locale") String local,
                                                   @Path("id") long offerId,
                                                   @Query("token") String token);

    @POST("{locale}/" + APIURLs.GET_CUSTOMER_ORDER_DETAILS + "/{id}" + APIURLs.DELEGATES_RESEARCH)
    Call<CustomerOrderDetailsResponse> researchDelegates(@Path("locale") String local,
                                                         @Path("id") long orderId,
                                                         @Query("token") String token);

    @FormUrlEncoded
    @POST("{locale}/" + APIURLs.GET_CUSTOMER_ORDER_DETAILS + "/{id}" + APIURLs.CANCELOFFER)
    Call<BaseResponse> cancelOrder(@Path("locale") String local,
                                   @Path("id") String cancelOrder,
                                   @Query("token") String token,
                                   @Field("cancelReason") int cancel_reason);

    @FormUrlEncoded
    @POST("{locale}/" + APIURLs.GET_DELEGATE_ORDER_DETAILS + "/{id}" + APIURLs.CANCELOFFER)
    Call<BaseResponse> cancelDelegateOrder(@Path("locale") String local,
                                           @Path("id") String cancelOrder,
                                           @Query("token") String token,
                                           @Field("cancelReason") int cancel_reason);

    @POST("{locale}/" + APIURLs.GET_DELEGATE_ORDER_DETAILS + "/{id}" + APIURLs.START_RIDE)
    Call<BaseResponse> startRide(@Path("locale") String local,
                                 @Path("id") int orderId,
                                 @Query("token") String token);

    @FormUrlEncoded
    @POST("{locale}/" + APIURLs.GET_DELEGATE_ORDER_DETAILS + "/{id}" + APIURLs.PICK_ITEM)
    Call<CustomerOrderDetailsResponse> pickItem(@Path("locale") String local,
                                                @Path("id") int orderId,
                                                @Query("token") String token,
                                                @Field("item_price") double itemPrice);

    @POST("{locale}/" + APIURLs.GET_DELEGATE_ORDER_DETAILS + "/{id}" + APIURLs.PICK_ITEM)
    Call<CustomerOrderDetailsResponse> pickItem(@Path("locale") String local,
                                                @Path("id") int orderId,
                                                @Query("token") String token);

    @FormUrlEncoded
    @POST("{locale}/" + APIURLs.GET_DELEGATE_ORDER_DETAILS + "/{id}" + APIURLs.DELIVER_ORDER)
    Call<BaseResponse> deliverOrder(@Path("locale") String local,
                                    @Path("id") int orderId,
                                    @Query("token") String token,
                                    @Field("delegate_app_version") String appVersion);

    @FormUrlEncoded
    @POST("{locale}/" + APIURLs.GET_DELEGATE_ORDER_DETAILS + "/{id}" + APIURLs.RATE_ORDER)
    Call<CustomerOrderDetailsResponse> delegateRateOrder(@Path("locale") String local,
                                                         @Path("id") int orderId,
                                                         @Query("token") String token,
                                                         @Field("rating") int rateNum,
                                                         @Field("comment") String comment);

    @FormUrlEncoded
    @POST("{locale}/" + APIURLs.GET_CUSTOMER_ORDER_DETAILS + "/{id}" + APIURLs.RATE_ORDER)
    Call<CustomerOrderDetailsResponse> customerRateOrder(@Path("locale") String local,
                                                         @Path("id") int orderId,
                                                         @Query("token") String token,
                                                         @Field("rating") int rateNum,
                                                         @Field("comment") String comment);

    @Multipart
    @POST("{local}/" + APIURLs.CREATE_ORDER + "/{id}" + APIURLs.CHAT_IMAGES)
    Call<UploadDelegateImagesResponse> uploadChatImage(@Path("local") String local,
                                                       @Part("imageFile\";filename=\"Pic.jpg\" ") RequestBody image,
                                                       @Path("id") String id,
                                                       @Query("token") String token);

    @GET("{local}/" + APIURLs.GET_AREAS)
    Call<BlockedAreasResponse> getBlockedAreas(@Path("local") String local);

    @GET("{local}/" + APIURLs.USERS + "/{id}" + APIURLs.GET_CUSTOMER_REVIEWS)
    Call<ReviewsResponse> getCustomerReviews(@Path("local") String local,
                                             @Path("id") int customerId,
                                             @Query("token") String token,
                                             @Query("page") int page,
                                             @Query("limit") int limit);

    @GET("{local}/" + APIURLs.USERS + "/{id}" + APIURLs.GET_DELEGATE_REVIEWS)
    Call<ReviewsResponse> getDelegateReviews(@Path("local") String local,
                                             @Path("id") int delegateId,
                                             @Query("token") String token,
                                             @Query("page") int page,
                                             @Query("limit") int limit);

    @GET("{local}/" + APIURLs.GET_COMPLAINTS)
    Call<ComplaintsResponse> getComplaints(@Path("local") String local,
                                           @Query("token") String token,
                                           @Query("page") int page,
                                           @Query("limit") int limit);

    @FormUrlEncoded
    @POST("{locale}/" + APIURLs.CREATE_ORDER + "/{id}" + APIURLs.SUBMIT_COMPLAINT)
    Call<BaseResponse> submitComplaint(@Path("locale") String local,
                                       @Path("id") int orderId,
                                       @Query("token") String token,
                                       @Field("title") String title,
                                       @Field("description") String description);

    @GET("{local}/" + APIURLs.GET_HISTORY_TRANSACTION)
    Call<MyBalanceResponse> getHistoryTransaction(@Path("local") String local,
                                                  @Query("token") String token,
                                                  @Query("page") int page,
                                                  @Query("limit") int limit);

    @GET("{local}/" + APIURLs.GET_BANK_ACCOUNTS)
    Call<MyBankAccountsResponse> getBankAccount(@Path("local") String local,
                                                @Query("token") String token);


    @Multipart
    @POST("{local}/" + APIURLs.SEND_BANK_TRANSFER)
    Call<BaseResponse> uploadTransferBank(@Path("local") String local,
                                          @Query("token") String token,
                                          @Part("bankAccount") RequestBody bankAccount,
                                          @Part("amount") RequestBody amount,
                                          @Part("imageFile\";filename=\"Pic.jpg\" ") RequestBody image);

    @GET("{local}/" + APIURLs.GET_WALLET_DETAILS)
    Call<WalletResponse> getWalletDetails(@Path("local") String local,
                                          @Query("token") String token);

    @DELETE("{local}/" + APIURLs.DEREGISTER_DEVICE + "/{playerId}")
    Call<BaseResponse> deregisterDevice(@Path("local") String local,
                                        @Path("playerId") String playerId);

    @GET("{local}/" + APIURLs.GET_ORDER_CANCELATION_REASONS)
    Call<CancelationReasonsResponse> getOrderCancelationReasons(@Path("local") String local,
                                                                @Query("type") int customerOrDelegate);


    @POST("{locale}/" + APIURLs.DELEGATE_ORDERS + "/{id}" + APIURLs.IGNORE_ORDER)
    Call<BaseResponse> ignoreOrder(@Path("locale") String local,
                                   @Path("id") int orderId,
                                   @Query("token") String token);

    @GET("{local}/" + APIURLs.GET_PRAYER_TIMES)
    Call<PrayerTimesResponse> getPrayerTimes(@Path("local") String local);

    @FormUrlEncoded
    @POST("{locale}/" + APIURLs.VALIDATE_COUPON)
    Call<BaseResponse> validateCoupon(@Path("locale") String local,
                                      @Query("token") String token,
                                      @Field("code") String code);

    @FormUrlEncoded
    @POST("{locale}/" + APIURLs.REGISTER_CARD_PAYMENT)
    Call<CardPayRegisterationResponse> registerCardPayment(@Path("locale") String local,
                                                           @Query("token") String token,
                                                           @Field("amount") double amount,
                                                           @Field("orderId") int orderId);

    @FormUrlEncoded
    @POST("{locale}/" + APIURLs.REGISTER_CARD_PAYMENT)
    Call<CardPayRegisterationResponse> getCheckoutId(@Path("locale") String local,
                                                           @Query("token") String token,
                                                           @Field("amount") double amount,
                                                           @Field("orderId") int orderId,
                                                           @Field("byapple") boolean byapple);

    @FormUrlEncoded
    @POST("{locale}/" + APIURLs.REGISTER_CARD_PAYMENT)
    Call<DirectPaymentAuthorizeV4ResponseMessage> postStcDirectPaymentAuthorize(@Path("locale") String local,
                                                                                @Query("token") String token,
                                                                                @Field("mobile") String mobile,
                                                                                @Field("amount") String amount,
                                                                                @Field("bystcAuthorize") boolean authorize);

    @FormUrlEncoded
    @POST("{locale}/" + APIURLs.REGISTER_CARD_PAYMENT)
    Call<DirectPaymentConfirmV4ResponseMessage> postStcDirectPaymentConfirm(@Path("locale") String local,
                                                                            @Query("token") String token,
                                                                            @Field("otpReference") String otpReference,
                                                                            @Field("otpValue") String otpValue,
                                                                            @Field("sTCPayPmtReference") String sTCPayPmtReference,
                                                                            @Field("tokenReference") String tokenReference,
                                                                            @Field("bystcConfirm") boolean authorize);

    @POST(APIURLs.STC_DirectPayment)
    Call<DirectPaymentResponse> postStcDirectPayment(@Header("X-ClientCode") String merchantId,
                                                     @Body HashMap<String, JSONObject> body);

    @POST(APIURLs.STC_DirectPaymentAuthorize)
    Call<DirectPaymentAuthorizeResponse> postStcDirectPaymentAuthorize(@Header("X-ClientCode") String merchantId,
                                                                       @Body HashMap<String, JSONObject> body);

    @POST(APIURLs.STC_DirectPaymentConfirm)
    Call<DirectPaymentConfirmResponse> postStcDirectPaymentConfirm(@Header("X-ClientCode") String merchantId,
                                                                   @Body HashMap<String, JSONObject> body);

    @POST(APIURLs.STC_PaymentInquiry)
    Call<PaymentInquiryResponse> postStcPaymentInquiry(@Header("X-ClientCode") String merchantId,
                                                       @Field("PaymentInquiryV4RequestMessage") JSONObject payment);

}
