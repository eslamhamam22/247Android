package amaz.objects.TwentyfourSeven.data.repositories;

import java.io.File;

import amaz.objects.TwentyfourSeven.listeners.OnResponseListener;

public interface UserRepository {

    void socialLogin(String locale, String facebookToken, String googleToken, OnResponseListener onSocialLoginResponse);

    void requestVerificationCode(String locale, String phone, OnResponseListener onRequestCodeResponse);

    void verifyPhoneNumber(String locale, String phone, String code, String facebookToken, String googleToken, OnResponseListener onVerifyPhoneResponse);

    void updateUserProfile(String token, String locale, String name, String birthdate, String city, String gender, File imageFile,
                           OnResponseListener onUpdateUserProfileResponse);

    void changeUserLanguage(String token, String locale, String language, OnResponseListener onChangeLanguageResponse);

    void changeUserPhoneRequestCode(String token, String locale, String phone, OnResponseListener onRequestCodeResponse);

    void changeUserPhoneVerifyCode(String token, String locale, String phone, String code, final OnResponseListener onVerifyPhoneResponse);

    void refreshToken(String token, String locale, String refreshToken, final OnResponseListener onRefreshTokenResponse);

    void getContactUsData(String locale, final OnResponseListener onGetContactUsDataResponse);

    void getMyAddresses(String token, String locale, final OnResponseListener onGetMyAddressesResponse);

    void deleteAddress(String token, String locale, int addressId, final OnResponseListener onDeleteAddressResponse);

    void addAddress(String token, String locale, String addressTitle, String addressDetails, double latitude, double longitude, final OnResponseListener onAddAddressResponse);

    void uploadDelegateImages(String token, String locale, String imageType, File imageFile, final OnResponseListener onUploadImageResponse);

    void deleteDelegateImages(String token, String locale, long id, final OnResponseListener onUploadImageResponse);

    void submitDelegateRequest(String token, String locale, String carDetails, String imagesIds, final OnResponseListener onSubmitRequestResponse);

    void getCarDetails(String token, String locale, final OnResponseListener onGetCarDetailsResponse);

    void unRegisterDeviceToken(String token, String locale, String playerId, final OnResponseListener onRefreshTokenResponse);

    void getUserProfile(String token, String locale,
                        OnResponseListener onUpdateUserProfileResponse);

    void getNotification(String token, String locale, int page, int limit, final OnResponseListener onSubmitRequestResponse);

    void registerDeviceToken(String token, String locale, String plauerId, final OnResponseListener onRefreshTokenResponse);

    void deregisterDevice(String locale, String playerId, final OnResponseListener onDeregisterDevice);

    void markNotificationAsRead(String token, String locale, final OnResponseListener onRefreshTokenResponse);

    void setDeviceStat(String token, String locale, boolean state, final OnResponseListener onRefreshTokenResponse);

    void updateDelegateStatus(String token, String locale, String state, final OnResponseListener onRefreshTokenResponse);

    void getCurrentUserOrder(String token, String locale, int page, int limit, final OnResponseListener onSubmitRequestResponse);

    void getHistoryUserOrder(String token, String locale, int page, int limit, final OnResponseListener onSubmitRequestResponse);

    void getCurrentDelegateOrder(String token, String locale, int page, int limit, final OnResponseListener onSubmitRequestResponse);

    void getHistoryDelegateOrder(String token, String locale, int page, int limit, final OnResponseListener onSubmitRequestResponse);

    void getCustomerOrDelegateReviews(String token, String locale, int customerId, int page, int limit, boolean isCustomerReviews, final OnResponseListener onGetCustmerReviewsResponse);

    void getComplaints(String token, String locale, int page, int limit, final OnResponseListener onGetComplaintsResponse);

    void getTransactionHistory(String token, String locale, int page, int limit, final OnResponseListener onSubmitRequestResponse);

    void getBankAccounts(String token, String locale, final OnResponseListener onSubmitRequestResponse);

    void uploadBankTransfer(String token, String locale, String accountNum, String amount, File imageFile, final OnResponseListener onUploadImageResponse);

    void getWalletDetails(String token, String locale, final OnResponseListener onSubmitRequestResponse);

    void registerCardPayment(String token, String locale, double amount, final OnResponseListener onRegisterCardPaymentResponse);

}
