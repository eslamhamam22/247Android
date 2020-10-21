package amaz.objects.TwentyfourSeven.data.repositories;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import amaz.objects.TwentyfourSeven.MApplication;
import amaz.objects.TwentyfourSeven.api.ApiEndPointInterface;
import amaz.objects.TwentyfourSeven.data.models.responses.BaseResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.CarDetailsResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.CardPayRegisterationResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.ComplaintsResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.ContactUsResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.MyAddressesResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.MyBalanceResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.MyBankAccountsResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.MyDelegateStateResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.MyNotificationResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.MyNotificationStateResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.MyOrdersResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.ProfileResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.ReviewsResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.SocialLoginResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.UploadDelegateImagesResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.WalletResponse;
import amaz.objects.TwentyfourSeven.listeners.OnResponseListener;
import amaz.objects.TwentyfourSeven.utilities.Constants;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkUserRepository implements UserRepository {

    private final int SERVER_ERROR = 500;
    private final int BAD_REQUEST_ERROR = 400;   // bad request
    private final int INVALID_TOKEN_ERROR = 401;   //invalid token
    private final int VALIDATION_ERROR = 422;
    private final int PAGE_NOT_FOUND_ERROR = 404;
    private final int UNAUTHORIZED_ERROR = 403;

    private ApiEndPointInterface apiEndPointInterface = MApplication.getInstance().getApi();
    private ApiEndPointInterface imageApiEndPointInterface = MApplication.getInstance().getImagesApi();

    @Override
    public void socialLogin(String locale, String facebookToken, String googleToken, final OnResponseListener onSocialLoginResponse) {

        Call<SocialLoginResponse> call = apiEndPointInterface.socialLogin(locale, facebookToken, googleToken);
        call.clone().enqueue(new Callback<SocialLoginResponse>() {
            @Override
            public void onResponse(Call<SocialLoginResponse> call, Response<SocialLoginResponse> response) {
                if (response.body() != null) {
                    onSocialLoginResponse.onSuccess(response);
                } else if (response.code() == BAD_REQUEST_ERROR) {
                    onSocialLoginResponse.onInvalidTokenError();
                } else if (response.code() == INVALID_TOKEN_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onSocialLoginResponse.onSuspendedUserError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == SERVER_ERROR) {
                    onSocialLoginResponse.onServerError();
                } else if (response.code() == UNAUTHORIZED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onSocialLoginResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    onSocialLoginResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<SocialLoginResponse> call, Throwable t) {
                onSocialLoginResponse.onFailure();
            }
        });
    }

    @Override
    public void requestVerificationCode(String locale, String phone, final OnResponseListener onRequestCodeResponse) {
        Call<BaseResponse> call = apiEndPointInterface.requestVerificationCode(locale, phone);
        call.clone().enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.body() != null) {
                    onRequestCodeResponse.onSuccess(response);
                } else if (response.code() == BAD_REQUEST_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readMobileError(errorBodyString);
                            onRequestCodeResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == INVALID_TOKEN_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onRequestCodeResponse.onSuspendedUserError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == SERVER_ERROR) {
                    onRequestCodeResponse.onServerError();
                } else if (response.code() == VALIDATION_ERROR) {
                    onRequestCodeResponse.onValidationError(null);
                } else if (response.code() == UNAUTHORIZED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onRequestCodeResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    onRequestCodeResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                onRequestCodeResponse.onFailure();
            }
        });
    }

    @Override
    public void verifyPhoneNumber(String locale, String phone, String code, String facebookToken, String googleToken, final OnResponseListener onVerifyPhoneResponse) {
        Call<SocialLoginResponse> call = apiEndPointInterface.verifyPhoneNumber(locale, phone, code, facebookToken, googleToken);
        call.clone().enqueue(new Callback<SocialLoginResponse>() {
            @Override
            public void onResponse(Call<SocialLoginResponse> call, Response<SocialLoginResponse> response) {
                if (response.body() != null) {
                    onVerifyPhoneResponse.onSuccess(response);
                } else if (response.code() == BAD_REQUEST_ERROR) {
                    onVerifyPhoneResponse.onInvalidTokenError();
                } else if (response.code() == INVALID_TOKEN_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onVerifyPhoneResponse.onSuspendedUserError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == SERVER_ERROR) {
                    onVerifyPhoneResponse.onServerError();
                } else if (response.code() == VALIDATION_ERROR) {
                    onVerifyPhoneResponse.onValidationError(null);
                } else if (response.code() == UNAUTHORIZED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onVerifyPhoneResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    onVerifyPhoneResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<SocialLoginResponse> call, Throwable t) {
                onVerifyPhoneResponse.onFailure();
            }
        });
    }

    @Override
    public void updateUserProfile(String token, String locale, String name, String birthdate, String city, String gender, File imageFile, final OnResponseListener onUpdateUserProfileResponse) {

        RequestBody nameRb = null;
        RequestBody birthdateRb = null;
        RequestBody cityRb = null;
        RequestBody genderRb = null;
        RequestBody imageRb = null;

        if (name != null) {
            nameRb = RequestBody.create(MediaType.parse("text/plain"), name);
        }
        if (birthdate != null) {
            birthdateRb = RequestBody.create(MediaType.parse("text/plain"), birthdate);
        }
        if (city != null) {
            cityRb = RequestBody.create(MediaType.parse("text/plain"), city);
        }
        if (gender != null) {
            genderRb = RequestBody.create(MediaType.parse("text/plain"), gender);
        }
        if (imageFile != null) {
            imageRb = RequestBody.create(MediaType.parse("image/jpeg"), imageFile);
        }
        Call<ProfileResponse> call;
        if (imageRb != null) {
            call = imageApiEndPointInterface.updateUserProfile(locale, token, nameRb, birthdateRb, cityRb, genderRb, imageRb);
        } else {
            call = apiEndPointInterface.updateUserProfile(locale, token, nameRb, birthdateRb, cityRb, genderRb, imageRb);
        }
        call.clone().enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.body() != null) {
                    onUpdateUserProfileResponse.onSuccess(response);
                } else if (response.code() == BAD_REQUEST_ERROR) {
                    onUpdateUserProfileResponse.onInvalidTokenError();
                } else if (response.code() == INVALID_TOKEN_ERROR) {
                    onUpdateUserProfileResponse.onAuthError();
                } else if (response.code() == SERVER_ERROR) {
                    onUpdateUserProfileResponse.onServerError();
                } else if (response.code() == VALIDATION_ERROR) {
                    onUpdateUserProfileResponse.onValidationError(null);
                } else if (response.code() == UNAUTHORIZED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onUpdateUserProfileResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    onUpdateUserProfileResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                onUpdateUserProfileResponse.onFailure();
            }
        });
    }

    @Override
    public void changeUserLanguage(String token, String locale, String language, final OnResponseListener onChangeLanguageResponse) {
        Call<ProfileResponse> call = apiEndPointInterface.changeUserLanguage(locale, token, language);
        call.clone().enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.body() != null) {
                    onChangeLanguageResponse.onSuccess(response);
                } else if (response.code() == BAD_REQUEST_ERROR) {
                    onChangeLanguageResponse.onInvalidTokenError();
                } else if (response.code() == INVALID_TOKEN_ERROR) {
                    onChangeLanguageResponse.onAuthError();
                } else if (response.code() == SERVER_ERROR) {
                    onChangeLanguageResponse.onServerError();
                } else if (response.code() == VALIDATION_ERROR) {
                    onChangeLanguageResponse.onValidationError(null);
                } else if (response.code() == UNAUTHORIZED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onChangeLanguageResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    onChangeLanguageResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                onChangeLanguageResponse.onFailure();
            }
        });
    }

    @Override
    public void changeUserPhoneRequestCode(String token, String locale, String phone, final OnResponseListener onRequestCodeResponse) {
        Call<BaseResponse> call = apiEndPointInterface.changeUserPhoneRequestCode(locale, token, phone);
        call.clone().enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.body() != null) {
                    onRequestCodeResponse.onSuccess(response);
                } else if (response.code() == BAD_REQUEST_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readMobileError(errorBodyString);
                            onRequestCodeResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == INVALID_TOKEN_ERROR) {
                    onRequestCodeResponse.onAuthError();
                } else if (response.code() == SERVER_ERROR) {
                    onRequestCodeResponse.onServerError();
                } else if (response.code() == VALIDATION_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readMobileError(errorBodyString);
                            onRequestCodeResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == UNAUTHORIZED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onRequestCodeResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    onRequestCodeResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                onRequestCodeResponse.onFailure();
            }
        });
    }

    @Override
    public void changeUserPhoneVerifyCode(String token, String locale, String phone, String code, final OnResponseListener onVerifyPhoneResponse) {
        Call<SocialLoginResponse> call = apiEndPointInterface.changeUserPhoneVerifyCode(locale, token, phone, code);
        call.clone().enqueue(new Callback<SocialLoginResponse>() {
            @Override
            public void onResponse(Call<SocialLoginResponse> call, Response<SocialLoginResponse> response) {
                if (response.body() != null) {
                    onVerifyPhoneResponse.onSuccess(response);
                } else if (response.code() == BAD_REQUEST_ERROR) {
                    onVerifyPhoneResponse.onInvalidTokenError();
                } else if (response.code() == INVALID_TOKEN_ERROR) {
                    onVerifyPhoneResponse.onAuthError();
                } else if (response.code() == SERVER_ERROR) {
                    onVerifyPhoneResponse.onServerError();
                } else if (response.code() == VALIDATION_ERROR) {
                    onVerifyPhoneResponse.onValidationError(null);
                } else if (response.code() == UNAUTHORIZED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onVerifyPhoneResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    onVerifyPhoneResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<SocialLoginResponse> call, Throwable t) {
                onVerifyPhoneResponse.onFailure();
            }
        });
    }

    @Override
    public void refreshToken(String token, String locale, String refreshToken, final OnResponseListener onRefreshTokenResponse) {
        Call<SocialLoginResponse> call = apiEndPointInterface.refreshToken(locale, token, refreshToken);
        call.clone().enqueue(new Callback<SocialLoginResponse>() {
            @Override
            public void onResponse(Call<SocialLoginResponse> call, Response<SocialLoginResponse> response) {
                if (response.body() != null) {
                    onRefreshTokenResponse.onSuccess(response);
                } else if (response.code() == SERVER_ERROR) {
                    onRefreshTokenResponse.onServerError();
                } else if (response.code() == INVALID_TOKEN_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onRefreshTokenResponse.onSuspendedUserError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == UNAUTHORIZED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onRefreshTokenResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    onRefreshTokenResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<SocialLoginResponse> call, Throwable t) {
                onRefreshTokenResponse.onFailure();
            }
        });
    }

    @Override
    public void getContactUsData(String locale, final OnResponseListener onGetContactUsDataResponse) {
        Call<ContactUsResponse> call = apiEndPointInterface.getContactUsData(locale);
        call.clone().enqueue(new Callback<ContactUsResponse>() {
            @Override
            public void onResponse(Call<ContactUsResponse> call, Response<ContactUsResponse> response) {
                if (response.body() != null) {
                    onGetContactUsDataResponse.onSuccess(response);
                } else if (response.code() == SERVER_ERROR) {
                    onGetContactUsDataResponse.onServerError();
                } else if (response.code() == PAGE_NOT_FOUND_ERROR) {
                    onGetContactUsDataResponse.onAuthError();
                } else if (response.code() == UNAUTHORIZED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onGetContactUsDataResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    onGetContactUsDataResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<ContactUsResponse> call, Throwable t) {
                onGetContactUsDataResponse.onFailure();
            }
        });
    }

    @Override
    public void getMyAddresses(String token, String locale, final OnResponseListener onGetMyAddressesResponse) {
        Call<MyAddressesResponse> call = apiEndPointInterface.getMyAddresses(locale, token);
        call.clone().enqueue(new Callback<MyAddressesResponse>() {
            @Override
            public void onResponse(Call<MyAddressesResponse> call, Response<MyAddressesResponse> response) {
                if (response.body() != null) {
                    onGetMyAddressesResponse.onSuccess(response);
                } else if (response.code() == INVALID_TOKEN_ERROR) {
                    onGetMyAddressesResponse.onAuthError();
                } else if (response.code() == SERVER_ERROR) {
                    onGetMyAddressesResponse.onServerError();
                } else if (response.code() == UNAUTHORIZED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onGetMyAddressesResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    onGetMyAddressesResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<MyAddressesResponse> call, Throwable t) {
                onGetMyAddressesResponse.onFailure();
            }
        });
    }

    @Override
    public void deleteAddress(String token, String locale, int addressId, final OnResponseListener onDeleteAddressResponse) {
        Call<BaseResponse> call = apiEndPointInterface.deleteAddress(locale, addressId, token);
        call.clone().enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.body() != null) {
                    onDeleteAddressResponse.onSuccess(response);
                } else if (response.code() == INVALID_TOKEN_ERROR) {
                    onDeleteAddressResponse.onAuthError();
                } else if (response.code() == SERVER_ERROR) {
                    onDeleteAddressResponse.onServerError();
                } else if (response.code() == UNAUTHORIZED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onDeleteAddressResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    onDeleteAddressResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                onDeleteAddressResponse.onFailure();
            }
        });
    }

    @Override
    public void addAddress(String token, String locale, String addressTitle, String addressDetails, double latitude, double longitude, final OnResponseListener onAddAddressResponse) {
        Call<BaseResponse> call = apiEndPointInterface.addAddress(locale, token, addressTitle, addressDetails, latitude, longitude);
        call.clone().enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.body() != null) {
                    onAddAddressResponse.onSuccess(response);
                } else if (response.code() == INVALID_TOKEN_ERROR) {
                    onAddAddressResponse.onAuthError();
                } else if (response.code() == SERVER_ERROR) {
                    onAddAddressResponse.onServerError();
                } else if (response.code() == UNAUTHORIZED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onAddAddressResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    onAddAddressResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                onAddAddressResponse.onFailure();
            }
        });
    }

    @Override
    public void uploadDelegateImages(String token, String locale, String imageType, File imageFile, final OnResponseListener onUploadImageResponse) {
        RequestBody imageTypeRb = null;
        RequestBody imageFileRb = null;

        if (imageType != null) {
            imageTypeRb = RequestBody.create(MediaType.parse("text/plain"), imageType);
        }

        if (imageFile != null) {
            imageFileRb = RequestBody.create(MediaType.parse("image/jpeg"), imageFile);
        }
        Call<UploadDelegateImagesResponse> call;
        if (imageFileRb != null) {
            call = imageApiEndPointInterface.uploadDelegateImages(locale, token, imageTypeRb, imageFileRb);
        } else {
            call = apiEndPointInterface.uploadDelegateImages(locale, token, imageTypeRb, imageFileRb);
        }
        call.clone().enqueue(new Callback<UploadDelegateImagesResponse>() {
            @Override
            public void onResponse(Call<UploadDelegateImagesResponse> call, Response<UploadDelegateImagesResponse> response) {
                if (response.body() != null) {
                    onUploadImageResponse.onSuccess(response);
                } else if (response.code() == INVALID_TOKEN_ERROR) {
                    onUploadImageResponse.onAuthError();
                } else if (response.code() == SERVER_ERROR) {
                    onUploadImageResponse.onServerError();
                } else if (response.code() == VALIDATION_ERROR) {
                    onUploadImageResponse.onValidationError(null);
                } else if (response.code() == UNAUTHORIZED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onUploadImageResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    onUploadImageResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<UploadDelegateImagesResponse> call, Throwable t) {
                onUploadImageResponse.onFailure();
            }
        });
    }

    @Override
    public void deleteDelegateImages(String token, String locale, long id, final OnResponseListener onDeleteImageResponse) {
        Call<BaseResponse> call = apiEndPointInterface.deleteDelegateImage(locale, id, token);
        call.clone().enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.body() != null) {
                    onDeleteImageResponse.onSuccess(response);
                } else if (response.code() == INVALID_TOKEN_ERROR) {
                    onDeleteImageResponse.onAuthError();
                } else if (response.code() == SERVER_ERROR) {
                    onDeleteImageResponse.onServerError();
                } else if (response.code() == UNAUTHORIZED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onDeleteImageResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    onDeleteImageResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                onDeleteImageResponse.onFailure();
            }
        });
    }

    @Override
    public void submitDelegateRequest(String token, String locale, String carDetails, String imagesIds, final OnResponseListener onSubmitRequestResponse) {
        Call<BaseResponse> call = apiEndPointInterface.submitDelegateRequest(locale, token, carDetails, imagesIds);
        call.clone().enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.body() != null) {
                    onSubmitRequestResponse.onSuccess(response);
                } else if (response.code() == INVALID_TOKEN_ERROR) {
                    onSubmitRequestResponse.onAuthError();
                } else if (response.code() == SERVER_ERROR) {
                    onSubmitRequestResponse.onServerError();
                } else if (response.code() == VALIDATION_ERROR) {
                    onSubmitRequestResponse.onValidationError(null);
                } else if (response.code() == UNAUTHORIZED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onSubmitRequestResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    onSubmitRequestResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                onSubmitRequestResponse.onFailure();
            }
        });
    }

    @Override
    public void getCarDetails(String token, String locale, final OnResponseListener onGetCarDetailsResponse) {
        Call<CarDetailsResponse> call = apiEndPointInterface.getCarDetails(locale, token);
        call.clone().enqueue(new Callback<CarDetailsResponse>() {
            @Override
            public void onResponse(Call<CarDetailsResponse> call, Response<CarDetailsResponse> response) {
                if (response.body() != null) {
                    onGetCarDetailsResponse.onSuccess(response);
                } else if (response.code() == INVALID_TOKEN_ERROR) {
                    onGetCarDetailsResponse.onAuthError();
                } else if (response.code() == SERVER_ERROR) {
                    onGetCarDetailsResponse.onServerError();
                } else if (response.code() == VALIDATION_ERROR) {
                    onGetCarDetailsResponse.onValidationError(null);
                } else if (response.code() == UNAUTHORIZED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onGetCarDetailsResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    onGetCarDetailsResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<CarDetailsResponse> call, Throwable t) {
                onGetCarDetailsResponse.onFailure();
            }
        });
    }

    @Override
    public void markNotificationAsRead(String token, String locale, final OnResponseListener onRefreshTokenResponse) {
        Call<BaseResponse> call = apiEndPointInterface.markAsRead(locale, token);
        call.clone().enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.body() != null) {
                    onRefreshTokenResponse.onSuccess(response);
                } else if (response.code() == SERVER_ERROR) {
                    onRefreshTokenResponse.onServerError();
                } else if (response.code() == UNAUTHORIZED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onRefreshTokenResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    onRefreshTokenResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                onRefreshTokenResponse.onFailure();
            }
        });
    }

    @Override
    public void setDeviceStat(String token, String locale, boolean state, final OnResponseListener onRefreshTokenResponse) {
        Call<MyNotificationStateResponse> call = apiEndPointInterface.setNotificationState(locale, token, state);
        call.clone().enqueue(new Callback<MyNotificationStateResponse>() {
            @Override
            public void onResponse(Call<MyNotificationStateResponse> call, Response<MyNotificationStateResponse> response) {
                if (response.body() != null) {
                    onRefreshTokenResponse.onSuccess(response);
                } else if (response.code() == SERVER_ERROR) {
                    onRefreshTokenResponse.onServerError();
                } else if (response.code() == UNAUTHORIZED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onRefreshTokenResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    onRefreshTokenResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<MyNotificationStateResponse> call, Throwable t) {
                onRefreshTokenResponse.onFailure();
            }
        });
    }

    @Override
    public void updateDelegateStatus(String token, String locale, String state, final OnResponseListener onRefreshTokenResponse) {
        Call<MyDelegateStateResponse> call = apiEndPointInterface.changeDelegateStatus(locale, token, state);
        call.clone().enqueue(new Callback<MyDelegateStateResponse>() {
            @Override
            public void onResponse(Call<MyDelegateStateResponse> call, Response<MyDelegateStateResponse> response) {
                if (response.body() != null) {
                    onRefreshTokenResponse.onSuccess(response);
                } else if (response.code() == SERVER_ERROR) {
                    onRefreshTokenResponse.onServerError();
                } else if (response.code() == UNAUTHORIZED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onRefreshTokenResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    onRefreshTokenResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<MyDelegateStateResponse> call, Throwable t) {
                onRefreshTokenResponse.onFailure();
            }
        });
    }

    @Override
    public void unRegisterDeviceToken(String token, String locale, String playerID, final OnResponseListener onRefreshTokenResponse) {
        Call<BaseResponse> call = apiEndPointInterface.unRegisterDeviceToken(locale, playerID, token);
        call.clone().enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.body() != null) {
                    onRefreshTokenResponse.onSuccess(response);
                } else if (response.code() == INVALID_TOKEN_ERROR) {
                    onRefreshTokenResponse.onAuthError();
                } else if (response.code() == SERVER_ERROR) {
                    onRefreshTokenResponse.onServerError();
                } else if (response.code() == UNAUTHORIZED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onRefreshTokenResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    onRefreshTokenResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                onRefreshTokenResponse.onFailure();
            }
        });
    }

    @Override
    public void getUserProfile(String token, String locale, final OnResponseListener onUpdateUserProfileResponse) {


        Call<ProfileResponse> call = apiEndPointInterface.getUserProfile(locale, token);
        call.clone().enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.body() != null) {
                    onUpdateUserProfileResponse.onSuccess(response);
                } else if (response.code() == BAD_REQUEST_ERROR) {
                    onUpdateUserProfileResponse.onInvalidTokenError();
                } else if (response.code() == INVALID_TOKEN_ERROR) {
                    onUpdateUserProfileResponse.onAuthError();
                } else if (response.code() == SERVER_ERROR) {
                    onUpdateUserProfileResponse.onServerError();
                } else if (response.code() == VALIDATION_ERROR) {
                    onUpdateUserProfileResponse.onValidationError(null);
                } else if (response.code() == UNAUTHORIZED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onUpdateUserProfileResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    onUpdateUserProfileResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                onUpdateUserProfileResponse.onFailure();
            }
        });
    }

    @Override
    public void getNotification(String token, String locale, int page, int limit, final OnResponseListener onSubmitRequestResponse) {
        Call<MyNotificationResponse> call = apiEndPointInterface.getNotification(locale, token, page, limit);
        call.clone().enqueue(new Callback<MyNotificationResponse>() {
            @Override
            public void onResponse(Call<MyNotificationResponse> call, Response<MyNotificationResponse> response) {
                if (response.body() != null) {
                    onSubmitRequestResponse.onSuccess(response);
                } else if (response.code() == INVALID_TOKEN_ERROR) {
                    onSubmitRequestResponse.onAuthError();
                } else if (response.code() == SERVER_ERROR) {
                    onSubmitRequestResponse.onServerError();
                } else if (response.code() == UNAUTHORIZED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onSubmitRequestResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    onSubmitRequestResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<MyNotificationResponse> call, Throwable t) {
                onSubmitRequestResponse.onFailure();
            }
        });
    }

    @Override
    public void registerDeviceToken(String token, String locale, String playerID, final OnResponseListener onRefreshTokenResponse) {
        Call<BaseResponse> call = apiEndPointInterface.sendDeviceId(locale, token, playerID);
        call.clone().enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.body() != null) {
                    onRefreshTokenResponse.onSuccess(response);
                } else if (response.code() == SERVER_ERROR) {
                    onRefreshTokenResponse.onServerError();
                } else if (response.code() == VALIDATION_ERROR) {
                    onRefreshTokenResponse.onServerError();
                } else if (response.code() == UNAUTHORIZED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onRefreshTokenResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    onRefreshTokenResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                onRefreshTokenResponse.onFailure();
            }
        });
    }

    @Override
    public void deregisterDevice(String locale, String playerId, final OnResponseListener onDeregisterDevice) {
        Call<BaseResponse> call = apiEndPointInterface.deregisterDevice(locale, playerId);
        call.clone().enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.body() != null) {
                    onDeregisterDevice.onSuccess(response);
                } else if (response.code() == SERVER_ERROR) {
                    onDeregisterDevice.onServerError();
                } else {
                    onDeregisterDevice.onFailure();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                onDeregisterDevice.onFailure();
            }
        });
    }

    @Override
    public void getCustomerOrDelegateReviews(String token, String locale, int customerId, int page, int limit, boolean isCustomerReviews,
                                             final OnResponseListener onGetCustmerReviewsResponse) {
        Call<ReviewsResponse> call;
        if (isCustomerReviews) {
            call = apiEndPointInterface.getCustomerReviews(locale, customerId, token, page, limit);
        } else {
            call = apiEndPointInterface.getDelegateReviews(locale, customerId, token, page, limit);
        }
        call.clone().enqueue(new Callback<ReviewsResponse>() {
            @Override
            public void onResponse(Call<ReviewsResponse> call, Response<ReviewsResponse> response) {
                if (response.body() != null) {
                    onGetCustmerReviewsResponse.onSuccess(response);
                } else if (response.code() == INVALID_TOKEN_ERROR) {
                    onGetCustmerReviewsResponse.onAuthError();
                } else if (response.code() == SERVER_ERROR) {
                    onGetCustmerReviewsResponse.onServerError();
                } else if (response.code() == UNAUTHORIZED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onGetCustmerReviewsResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    onGetCustmerReviewsResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<ReviewsResponse> call, Throwable t) {
                onGetCustmerReviewsResponse.onFailure();
            }
        });
    }

    @Override
    public void getComplaints(String token, String locale, int page, int limit, final OnResponseListener onGetComplaintsResponse) {
        Call<ComplaintsResponse> call = apiEndPointInterface.getComplaints(locale, token, page, limit);
        call.clone().enqueue(new Callback<ComplaintsResponse>() {
            @Override
            public void onResponse(Call<ComplaintsResponse> call, Response<ComplaintsResponse> response) {
                if (response.body() != null) {
                    onGetComplaintsResponse.onSuccess(response);
                } else if (response.code() == INVALID_TOKEN_ERROR) {
                    onGetComplaintsResponse.onAuthError();
                } else if (response.code() == SERVER_ERROR) {
                    onGetComplaintsResponse.onServerError();
                } else if (response.code() == UNAUTHORIZED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onGetComplaintsResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    onGetComplaintsResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<ComplaintsResponse> call, Throwable t) {
                onGetComplaintsResponse.onFailure();
            }
        });
    }

    @Override
    public void getCurrentUserOrder(String token, String locale, int page, int limit, final OnResponseListener onSubmitRequestResponse) {
        Call<MyOrdersResponse> call = apiEndPointInterface.getCurrentUserOrders(locale, token, page, limit);
        call.clone().enqueue(new Callback<MyOrdersResponse>() {
            @Override
            public void onResponse(Call<MyOrdersResponse> call, Response<MyOrdersResponse> response) {
                if (response.body() != null) {
                    onSubmitRequestResponse.onSuccess(response);
                } else if (response.code() == INVALID_TOKEN_ERROR) {
                    onSubmitRequestResponse.onAuthError();
                } else if (response.code() == SERVER_ERROR) {
                    onSubmitRequestResponse.onServerError();
                } else if (response.code() == UNAUTHORIZED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onSubmitRequestResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    onSubmitRequestResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<MyOrdersResponse> call, Throwable t) {
                onSubmitRequestResponse.onFailure();
            }
        });
    }

    @Override
    public void getHistoryUserOrder(String token, String locale, int page, int limit, final OnResponseListener onSubmitRequestResponse) {
        Call<MyOrdersResponse> call = apiEndPointInterface.getHistoryUserOrder(locale, token, page, limit);
        call.clone().enqueue(new Callback<MyOrdersResponse>() {
            @Override
            public void onResponse(Call<MyOrdersResponse> call, Response<MyOrdersResponse> response) {
                if (response.body() != null) {
                    onSubmitRequestResponse.onSuccess(response);
                } else if (response.code() == INVALID_TOKEN_ERROR) {
                    onSubmitRequestResponse.onAuthError();
                } else if (response.code() == SERVER_ERROR) {
                    onSubmitRequestResponse.onServerError();
                } else if (response.code() == UNAUTHORIZED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onSubmitRequestResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    onSubmitRequestResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<MyOrdersResponse> call, Throwable t) {
                onSubmitRequestResponse.onFailure();
            }
        });
    }

    @Override
    public void getHistoryDelegateOrder(String token, String locale, int page, int limit, final OnResponseListener onSubmitRequestResponse) {
        Call<MyOrdersResponse> call = apiEndPointInterface.getHistoryDelegateOrder(locale, token, page, limit);
        call.clone().enqueue(new Callback<MyOrdersResponse>() {
            @Override
            public void onResponse(Call<MyOrdersResponse> call, Response<MyOrdersResponse> response) {
                if (response.body() != null) {
                    onSubmitRequestResponse.onSuccess(response);
                } else if (response.code() == INVALID_TOKEN_ERROR) {
                    onSubmitRequestResponse.onAuthError();
                } else if (response.code() == SERVER_ERROR) {
                    onSubmitRequestResponse.onServerError();
                } else if (response.code() == UNAUTHORIZED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onSubmitRequestResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    onSubmitRequestResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<MyOrdersResponse> call, Throwable t) {
                onSubmitRequestResponse.onFailure();
            }
        });
    }

    @Override
    public void getCurrentDelegateOrder(String token, String locale, int page, int limit, final OnResponseListener onSubmitRequestResponse) {
        Call<MyOrdersResponse> call = apiEndPointInterface.getCurrentDelegateOrders(locale, token, page, limit);
        call.clone().enqueue(new Callback<MyOrdersResponse>() {
            @Override
            public void onResponse(Call<MyOrdersResponse> call, Response<MyOrdersResponse> response) {
                if (response.body() != null) {
                    onSubmitRequestResponse.onSuccess(response);
                } else if (response.code() == INVALID_TOKEN_ERROR) {
                    onSubmitRequestResponse.onAuthError();
                } else if (response.code() == SERVER_ERROR) {
                    onSubmitRequestResponse.onServerError();
                } else if (response.code() == UNAUTHORIZED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onSubmitRequestResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    onSubmitRequestResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<MyOrdersResponse> call, Throwable t) {
                onSubmitRequestResponse.onFailure();
            }
        });
    }

    @Override
    public void getTransactionHistory(String token, String locale, int page, int limit, final OnResponseListener onSubmitRequestResponse) {
        Call<MyBalanceResponse> call = apiEndPointInterface.getHistoryTransaction(locale, token, page, limit);
        call.clone().enqueue(new Callback<MyBalanceResponse>() {
            @Override
            public void onResponse(Call<MyBalanceResponse> call, Response<MyBalanceResponse> response) {
                if (response.body() != null) {
                    onSubmitRequestResponse.onSuccess(response);
                } else if (response.code() == INVALID_TOKEN_ERROR) {
                    onSubmitRequestResponse.onAuthError();
                } else if (response.code() == SERVER_ERROR) {
                    onSubmitRequestResponse.onServerError();
                } else if (response.code() == UNAUTHORIZED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onSubmitRequestResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    onSubmitRequestResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<MyBalanceResponse> call, Throwable t) {
                onSubmitRequestResponse.onFailure();
            }
        });
    }

    @Override
    public void getBankAccounts(String token, String locale, final OnResponseListener onSubmitRequestResponse) {
        Call<MyBankAccountsResponse> call = apiEndPointInterface.getBankAccount(locale, token);
        call.clone().enqueue(new Callback<MyBankAccountsResponse>() {
            @Override
            public void onResponse(Call<MyBankAccountsResponse> call, Response<MyBankAccountsResponse> response) {
                if (response.body() != null) {
                    onSubmitRequestResponse.onSuccess(response);
                } else if (response.code() == INVALID_TOKEN_ERROR) {
                    onSubmitRequestResponse.onAuthError();
                } else if (response.code() == SERVER_ERROR) {
                    onSubmitRequestResponse.onServerError();
                } else if (response.code() == UNAUTHORIZED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onSubmitRequestResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    onSubmitRequestResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<MyBankAccountsResponse> call, Throwable t) {
                onSubmitRequestResponse.onFailure();
            }
        });
    }

    @Override
    public void registerCardPayment(String token, String locale, double amount, final OnResponseListener onRegisterCardPaymentResponse) {
        Log.e("amount", amount + "omnia");
        if (amount < 0) {
            amount *= -1;
        }
        Call<CardPayRegisterationResponse> call = apiEndPointInterface.registerCardPayment(locale, token, amount);
        call.clone().enqueue(new Callback<CardPayRegisterationResponse>() {
            @Override
            public void onResponse(Call<CardPayRegisterationResponse> call, Response<CardPayRegisterationResponse> response) {
                if (response.body() != null) {
                    onRegisterCardPaymentResponse.onSuccess(response);
                } else if (response.code() == INVALID_TOKEN_ERROR) {
                    onRegisterCardPaymentResponse.onAuthError();
                } else if (response.code() == SERVER_ERROR) {
                    onRegisterCardPaymentResponse.onServerError();
                } else if (response.code() == UNAUTHORIZED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onRegisterCardPaymentResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    onRegisterCardPaymentResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<CardPayRegisterationResponse> call, Throwable t) {
                onRegisterCardPaymentResponse.onFailure();
            }
        });
    }

    @Override
    public void getWalletDetails(String token, String locale, final OnResponseListener onSubmitRequestResponse) {
        Call<WalletResponse> call = apiEndPointInterface.getWalletDetails(locale, token);
        call.clone().enqueue(new Callback<WalletResponse>() {
            @Override
            public void onResponse(Call<WalletResponse> call, Response<WalletResponse> response) {
                if (response.body() != null) {
                    onSubmitRequestResponse.onSuccess(response);
                } else if (response.code() == INVALID_TOKEN_ERROR) {
                    onSubmitRequestResponse.onAuthError();
                } else if (response.code() == SERVER_ERROR) {
                    onSubmitRequestResponse.onServerError();
                } else if (response.code() == UNAUTHORIZED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onSubmitRequestResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    onSubmitRequestResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<WalletResponse> call, Throwable t) {
                onSubmitRequestResponse.onFailure();
            }
        });
    }

    @Override
    public void uploadBankTransfer(String token, String locale, String accountNum, String amount, File imageFile, final OnResponseListener onUploadImageResponse) {
        RequestBody imageTypeRb = null, accountNumRB = null, amountRB = null;

        imageTypeRb = RequestBody.create(MediaType.parse("text/plain"), imageFile);
        accountNumRB = RequestBody.create(MediaType.parse("text/plain"), accountNum);
        amountRB = RequestBody.create(MediaType.parse("text/plain"), amount);


        Call<BaseResponse> call = imageApiEndPointInterface.uploadTransferBank(locale, token, accountNumRB, amountRB, imageTypeRb);


        call.clone().enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.body() != null) {
                    onUploadImageResponse.onSuccess(response);
                } else if (response.code() == INVALID_TOKEN_ERROR) {
                    onUploadImageResponse.onAuthError();
                } else if (response.code() == SERVER_ERROR) {
                    onUploadImageResponse.onServerError();
                } else if (response.code() == VALIDATION_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readAmountError(errorBodyString);
                            onUploadImageResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == UNAUTHORIZED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onUploadImageResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    onUploadImageResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                onUploadImageResponse.onFailure();
            }
        });
    }

    private String readErrorMessage(String errorBody) {
        JSONObject errorBodyJson;
        JSONObject errorJsonObject;
        String errorMessage = null;
        try {
            errorBodyJson = new JSONObject(errorBody);
            errorJsonObject = errorBodyJson.getJSONObject(Constants.ERROR);
            errorMessage = errorJsonObject.getString(Constants.DEFAULT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return errorMessage;
    }

    private String readMobileError(String errorBody) {
        JSONObject errorBodyJson;
        JSONObject errorJsonObject;
        String errorMessage = null;
        try {
            errorBodyJson = new JSONObject(errorBody);
            errorJsonObject = errorBodyJson.getJSONObject(Constants.ERROR);
            errorMessage = errorJsonObject.getString(Constants.MOBILE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return errorMessage;
    }

    private String readAmountError(String errorBody) {
        JSONObject errorBodyJson;
        JSONObject errorJsonObject;
        String errorMessage = null;
        try {
            errorBodyJson = new JSONObject(errorBody);
            errorJsonObject = errorBodyJson.getJSONObject(Constants.ERROR);
            errorMessage = errorJsonObject.getString(Constants.AMOUNT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return errorMessage;
    }
}
