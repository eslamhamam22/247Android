package amaz.objects.TwentyfourSeven.data.repositories;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import amaz.objects.TwentyfourSeven.MApplication;
import amaz.objects.TwentyfourSeven.api.ApiEndPointInterface;
import amaz.objects.TwentyfourSeven.data.models.responses.AddOfferResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.BaseResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.CancelationReasonsResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.CustomerOrderDetailsResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.PrayerTimesResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.UploadDelegateImagesResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.VoiceNoteResponse;
import amaz.objects.TwentyfourSeven.listeners.OnResponseListener;
import amaz.objects.TwentyfourSeven.utilities.Constants;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkOrderRepository implements OrderRepository {

    private final int SERVER_ERROR = 500;
    private final int INVALID_TOKEN_ERROR = 401;   //invalid token
    private final int VALIDATION_ERROR = 422;
    private final int UNAUTHORIZED_ERROR = 403;
    private final int ACTION_DENIED_ERROR = 409;

    private ApiEndPointInterface apiEndPointInterface = MApplication.getInstance().getApi();
    private ApiEndPointInterface imageApiEndPointInterface = MApplication.getInstance().getImagesApi();

    @Override
    public void uploadOrderImages(String token, String locale, File imageFile, final OnResponseListener onUploadImageResponse) {
        RequestBody imageFileRb = null;

        if (imageFile != null) {
            imageFileRb = RequestBody.create(MediaType.parse("image/jpeg"), imageFile);
        }
        Call<UploadDelegateImagesResponse> call;
        if (imageFileRb != null) {
            call = imageApiEndPointInterface.uploadOrderImages(locale, token, imageFileRb);
        } else {
            call = apiEndPointInterface.uploadOrderImages(locale, token, imageFileRb);
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
                } else if (response.code() == ACTION_DENIED_ERROR) {
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
    public void deleteOrderImages(String token, String locale, long id, final OnResponseListener onDeleteImageResponse) {
        Call<BaseResponse> call = apiEndPointInterface.deleteOrderImage(locale, id, token);
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
                } else if (response.code() == ACTION_DENIED_ERROR) {
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
    public void createOrder(String token, String locale, String orderDescription, int fromType, double fromLat, double fromLng, String fromAddress,
                            double toLat, double toLng, String toAddress, String storeName, String imagesIds, int deliveryDuration, String couponCode,
                            boolean isReorder, final OnResponseListener onCeateOrderResponse) {
        Call<CustomerOrderDetailsResponse> call = apiEndPointInterface.createOrder(locale, token, orderDescription, fromType, fromLat, fromLng, fromAddress,
                toLat, toLng, toAddress, storeName, imagesIds, isReorder, deliveryDuration, couponCode);

        call.clone().enqueue(new Callback<CustomerOrderDetailsResponse>() {
            @Override
            public void onResponse(Call<CustomerOrderDetailsResponse> call, Response<CustomerOrderDetailsResponse> response) {
                if (response.body() != null) {
                    onCeateOrderResponse.onSuccess(response);
                } else if (response.code() == INVALID_TOKEN_ERROR) {
                    onCeateOrderResponse.onAuthError();
                } else if (response.code() == SERVER_ERROR) {
                    onCeateOrderResponse.onServerError();
                } else if (response.code() == VALIDATION_ERROR) {

                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readFirstErrorMessage(errorBodyString);
                            onCeateOrderResponse.onValidationError(errorMessage.substring(errorMessage.indexOf(":") + 1));
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else if (response.code() == UNAUTHORIZED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onCeateOrderResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == ACTION_DENIED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onCeateOrderResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    onCeateOrderResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<CustomerOrderDetailsResponse> call, Throwable t) {
                onCeateOrderResponse.onFailure();
            }
        });
    }

    @Override
    public void uploadeOrdervoice(String token, String locale, File voiceFile, final OnResponseListener onUploadVoiceResponse) {

        RequestBody voiceFileRb = null;

        if (voiceFile != null) {
            voiceFileRb = RequestBody.create(MediaType.parse("audio/wav"), voiceFile);
        }
        Call<VoiceNoteResponse> call = apiEndPointInterface.uploadOrderVoice(locale, token, voiceFileRb);
        call.clone().enqueue(new Callback<VoiceNoteResponse>() {
            @Override
            public void onResponse(Call<VoiceNoteResponse> call, Response<VoiceNoteResponse> response) {
                if (response.body() != null) {
                    onUploadVoiceResponse.onSuccess(response);
                } else if (response.code() == INVALID_TOKEN_ERROR) {
                    onUploadVoiceResponse.onAuthError();
                } else if (response.code() == SERVER_ERROR) {
                    onUploadVoiceResponse.onServerError();
                } else if (response.code() == VALIDATION_ERROR) {
                    onUploadVoiceResponse.onValidationError(null);

                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readFileError(errorBodyString);
                            Log.e("error", errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else if (response.code() == UNAUTHORIZED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onUploadVoiceResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == ACTION_DENIED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onUploadVoiceResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    onUploadVoiceResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<VoiceNoteResponse> call, Throwable t) {
                onUploadVoiceResponse.onFailure();
            }
        });
    }

    @Override
    public void deleteOrderVoice(String token, String locale, long id, final OnResponseListener onDeleteOrderVoiceResponse) {
        Call<BaseResponse> call = apiEndPointInterface.deleteOrderVoice(locale, id, token);
        call.clone().enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.body() != null) {
                    onDeleteOrderVoiceResponse.onSuccess(response);
                } else if (response.code() == INVALID_TOKEN_ERROR) {
                    onDeleteOrderVoiceResponse.onAuthError();
                } else if (response.code() == SERVER_ERROR) {
                    onDeleteOrderVoiceResponse.onServerError();
                } else if (response.code() == UNAUTHORIZED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onDeleteOrderVoiceResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == ACTION_DENIED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onDeleteOrderVoiceResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    onDeleteOrderVoiceResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                onDeleteOrderVoiceResponse.onFailure();
            }
        });
    }

    @Override
    public void getCustomerOrderDetails(String token, String locale, int id, final OnResponseListener onGetCustomerOrderDetailsResponse) {
        Call<CustomerOrderDetailsResponse> call = apiEndPointInterface.getCustomerOrderDetails(locale, id, token);
        call.clone().enqueue(new Callback<CustomerOrderDetailsResponse>() {
            @Override
            public void onResponse(Call<CustomerOrderDetailsResponse> call, Response<CustomerOrderDetailsResponse> response) {
                if (response.body() != null) {
                    onGetCustomerOrderDetailsResponse.onSuccess(response);
                } else if (response.code() == INVALID_TOKEN_ERROR) {
                    onGetCustomerOrderDetailsResponse.onAuthError();
                } else if (response.code() == SERVER_ERROR) {
                    onGetCustomerOrderDetailsResponse.onServerError();
                } else if (response.code() == UNAUTHORIZED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onGetCustomerOrderDetailsResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == ACTION_DENIED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onGetCustomerOrderDetailsResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    onGetCustomerOrderDetailsResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<CustomerOrderDetailsResponse> call, Throwable t) {
                onGetCustomerOrderDetailsResponse.onFailure();
            }
        });
    }

    @Override
    public void getDelegateOrderDetails(String token, String locale, int id, final OnResponseListener onGetDelegateOrderDetailsResponse) {
        Call<CustomerOrderDetailsResponse> call = apiEndPointInterface.getDelegateOrderDetails(locale, id, token);
        call.clone().enqueue(new Callback<CustomerOrderDetailsResponse>() {
            @Override
            public void onResponse(Call<CustomerOrderDetailsResponse> call, Response<CustomerOrderDetailsResponse> response) {
                if (response.body() != null) {
                    onGetDelegateOrderDetailsResponse.onSuccess(response);
                } else if (response.code() == INVALID_TOKEN_ERROR) {
                    onGetDelegateOrderDetailsResponse.onAuthError();
                } else if (response.code() == SERVER_ERROR) {
                    onGetDelegateOrderDetailsResponse.onServerError();
                } else if (response.code() == UNAUTHORIZED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onGetDelegateOrderDetailsResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == ACTION_DENIED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onGetDelegateOrderDetailsResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    onGetDelegateOrderDetailsResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<CustomerOrderDetailsResponse> call, Throwable t) {
                onGetDelegateOrderDetailsResponse.onFailure();
            }
        });
    }

    @Override
    public void addOffer(String token, String locale, int id, double shippingCost, double distToPickUp, double distToDelivery, double delegateLat,
                         double delegateLng, final OnResponseListener onAddOfferResponse) {
        Call<AddOfferResponse> call = apiEndPointInterface.addOffer(locale, id, token, shippingCost, distToPickUp, distToDelivery, delegateLat, delegateLng);

        call.clone().enqueue(new Callback<AddOfferResponse>() {
            @Override
            public void onResponse(Call<AddOfferResponse> call, Response<AddOfferResponse> response) {
                if (response.body() != null) {
                    onAddOfferResponse.onSuccess(response);
                } else if (response.code() == INVALID_TOKEN_ERROR) {
                    onAddOfferResponse.onAuthError();
                } else if (response.code() == SERVER_ERROR) {
                    onAddOfferResponse.onServerError();
                } else if (response.code() == UNAUTHORIZED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onAddOfferResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == ACTION_DENIED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onAddOfferResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    onAddOfferResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<AddOfferResponse> call, Throwable t) {
                onAddOfferResponse.onFailure();
            }
        });
    }

    @Override
    public void cancelOffer(String token, String locale, String id
            , final OnResponseListener onAddOfferResponse) {
        Call<BaseResponse> call = apiEndPointInterface.cancelOffer(locale, id,token);

        call.clone().enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.body() != null) {
                    onAddOfferResponse.onSuccess(response);
                } else if (response.code() == INVALID_TOKEN_ERROR) {
                    onAddOfferResponse.onAuthError();
                } else if (response.code() == SERVER_ERROR) {
                    onAddOfferResponse.onServerError();
                } else if (response.code() == UNAUTHORIZED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onAddOfferResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == ACTION_DENIED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onAddOfferResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    onAddOfferResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                onAddOfferResponse.onFailure();
            }
        });
    }

    @Override
    public void cancelOrder(String token, String locale, String id
            , boolean isDelegate, int reason, final OnResponseListener onAddOfferResponse) {
        Call<BaseResponse> call;
        if (isDelegate) {
            call = apiEndPointInterface.cancelDelegateOrder(locale, id, token, reason);
        } else {
            call = apiEndPointInterface.cancelOrder(locale, id, token, reason);

        }

        call.clone().enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.body() != null) {
                    onAddOfferResponse.onSuccess(response);
                } else if (response.code() == INVALID_TOKEN_ERROR) {
                    onAddOfferResponse.onAuthError();
                } else if (response.code() == SERVER_ERROR) {
                    onAddOfferResponse.onServerError();
                } else if (response.code() == UNAUTHORIZED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onAddOfferResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == ACTION_DENIED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onAddOfferResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    onAddOfferResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                onAddOfferResponse.onFailure();
            }
        });
    }

    @Override
    public void researchDelegates(String token, String locale, int id, final OnResponseListener onResearchDelegatesResponse) {
        Call<CustomerOrderDetailsResponse> call = apiEndPointInterface.researchDelegates(locale, id, token);

        call.clone().enqueue(new Callback<CustomerOrderDetailsResponse>() {
            @Override
            public void onResponse(Call<CustomerOrderDetailsResponse> call, Response<CustomerOrderDetailsResponse> response) {
                if (response.body() != null) {
                    onResearchDelegatesResponse.onSuccess(response);
                } else if (response.code() == INVALID_TOKEN_ERROR) {
                    onResearchDelegatesResponse.onAuthError();
                } else if (response.code() == SERVER_ERROR) {
                    onResearchDelegatesResponse.onServerError();
                } else if (response.code() == UNAUTHORIZED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onResearchDelegatesResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == ACTION_DENIED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onResearchDelegatesResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    onResearchDelegatesResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<CustomerOrderDetailsResponse> call, Throwable t) {
                onResearchDelegatesResponse.onFailure();
            }
        });
    }

    @Override
    public void acceptOffer(String token, String locale, long id, final OnResponseListener onAcceptOfferResponse) {
        Call<CustomerOrderDetailsResponse> call = apiEndPointInterface.acceptOffer( locale, id,token);

        call.clone().enqueue(new Callback<CustomerOrderDetailsResponse>() {
            @Override
            public void onResponse(Call<CustomerOrderDetailsResponse> call, Response<CustomerOrderDetailsResponse> response) {
                if (response.body() != null) {
                    onAcceptOfferResponse.onSuccess(response);
                } else if (response.code() == INVALID_TOKEN_ERROR) {
                    onAcceptOfferResponse.onAuthError();
                } else if (response.code() == SERVER_ERROR) {
                    onAcceptOfferResponse.onServerError();
                } else if (response.code() == UNAUTHORIZED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onAcceptOfferResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == ACTION_DENIED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onAcceptOfferResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    onAcceptOfferResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<CustomerOrderDetailsResponse> call, Throwable t) {
                onAcceptOfferResponse.onFailure();
            }
        });
    }

    @Override
    public void rejectOffer(String token, String locale, long id, final OnResponseListener onRejectOfferResponse) {
        Call<CustomerOrderDetailsResponse> call = apiEndPointInterface.rejectOffer( locale, id,token);

        call.clone().enqueue(new Callback<CustomerOrderDetailsResponse>() {
            @Override
            public void onResponse(Call<CustomerOrderDetailsResponse> call, Response<CustomerOrderDetailsResponse> response) {
                if (response.body() != null) {
                    onRejectOfferResponse.onSuccess(response);
                } else if (response.code() == INVALID_TOKEN_ERROR) {
                    onRejectOfferResponse.onAuthError();
                } else if (response.code() == SERVER_ERROR) {
                    onRejectOfferResponse.onServerError();
                } else if (response.code() == UNAUTHORIZED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onRejectOfferResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == ACTION_DENIED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onRejectOfferResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    onRejectOfferResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<CustomerOrderDetailsResponse> call, Throwable t) {
                onRejectOfferResponse.onFailure();
            }
        });
    }

    @Override
    public void startRide(String token, String locale, int id, final OnResponseListener onStartRideResponse) {
        Call<BaseResponse> call = apiEndPointInterface.startRide(locale, id, token);

        call.clone().enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.body() != null) {
                    onStartRideResponse.onSuccess(response);
                } else if (response.code() == INVALID_TOKEN_ERROR) {
                    onStartRideResponse.onAuthError();
                } else if (response.code() == SERVER_ERROR) {
                    onStartRideResponse.onServerError();
                } else if (response.code() == UNAUTHORIZED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onStartRideResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == ACTION_DENIED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onStartRideResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    onStartRideResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                onStartRideResponse.onFailure();
            }
        });
    }

    @Override
    public void pickItem(String token, String locale, int id, double itemPrice, final OnResponseListener onPickItemResponse) {
        Call<CustomerOrderDetailsResponse> call = apiEndPointInterface.pickItem(locale, id, token, itemPrice);

        call.clone().enqueue(new Callback<CustomerOrderDetailsResponse>() {
            @Override
            public void onResponse(Call<CustomerOrderDetailsResponse> call, Response<CustomerOrderDetailsResponse> response) {
                if (response.body() != null) {
                    onPickItemResponse.onSuccess(response);
                } else if (response.code() == INVALID_TOKEN_ERROR) {
                    onPickItemResponse.onAuthError();
                } else if (response.code() == SERVER_ERROR) {
                    onPickItemResponse.onServerError();
                } else if (response.code() == UNAUTHORIZED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onPickItemResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == ACTION_DENIED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onPickItemResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    onPickItemResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<CustomerOrderDetailsResponse> call, Throwable t) {
                onPickItemResponse.onFailure();
            }
        });
    }

    @Override
    public void pickItem(String token, String locale, int id, final OnResponseListener onPickItemResponse) {
        Call<CustomerOrderDetailsResponse> call = apiEndPointInterface.pickItem(locale, id, token);

        call.clone().enqueue(new Callback<CustomerOrderDetailsResponse>() {
            @Override
            public void onResponse(Call<CustomerOrderDetailsResponse> call, Response<CustomerOrderDetailsResponse> response) {
                if (response.body() != null) {
                    onPickItemResponse.onSuccess(response);
                } else if (response.code() == INVALID_TOKEN_ERROR) {
                    onPickItemResponse.onAuthError();
                } else if (response.code() == SERVER_ERROR) {
                    onPickItemResponse.onServerError();
                } else if (response.code() == UNAUTHORIZED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onPickItemResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == ACTION_DENIED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onPickItemResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    onPickItemResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<CustomerOrderDetailsResponse> call, Throwable t) {
                onPickItemResponse.onFailure();
            }
        });
    }

    @Override
    public void deliverOrder(String token, String locale, int id, String appVersion, final OnResponseListener onDeliverOrderResponse) {
        Call<BaseResponse> call = apiEndPointInterface.deliverOrder(locale, id, token, appVersion);

        call.clone().enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.body() != null) {
                    onDeliverOrderResponse.onSuccess(response);
                } else if (response.code() == INVALID_TOKEN_ERROR) {
                    onDeliverOrderResponse.onAuthError();
                } else if (response.code() == SERVER_ERROR) {
                    onDeliverOrderResponse.onServerError();
                } else if (response.code() == UNAUTHORIZED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onDeliverOrderResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == ACTION_DENIED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onDeliverOrderResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    onDeliverOrderResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                onDeliverOrderResponse.onFailure();
            }
        });
    }

    @Override
    public void delegateRateOrder(String token, String locale, int id, int rateNum, String comment, boolean isDlegateRate, final OnResponseListener onDelegateRateOrderResponse) {

        Call<CustomerOrderDetailsResponse> call;
        if (isDlegateRate) {
            call = apiEndPointInterface.delegateRateOrder(locale, id, token, rateNum, comment);
        } else {
            call = apiEndPointInterface.customerRateOrder(locale, id, token, rateNum, comment);
        }
        call.clone().enqueue(new Callback<CustomerOrderDetailsResponse>() {
            @Override
            public void onResponse(Call<CustomerOrderDetailsResponse> call, Response<CustomerOrderDetailsResponse> response) {
                if (response.body() != null) {
                    onDelegateRateOrderResponse.onSuccess(response);
                } else if (response.code() == INVALID_TOKEN_ERROR) {
                    onDelegateRateOrderResponse.onAuthError();
                } else if (response.code() == SERVER_ERROR) {
                    onDelegateRateOrderResponse.onServerError();
                } else if (response.code() == UNAUTHORIZED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onDelegateRateOrderResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == ACTION_DENIED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onDelegateRateOrderResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    onDelegateRateOrderResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<CustomerOrderDetailsResponse> call, Throwable t) {
                onDelegateRateOrderResponse.onFailure();
            }
        });
    }

    @Override
    public void uploadChatImage(String token, String locale, File imageFile, String id, final OnResponseListener onUploadImageResponse) {
        RequestBody imageFileRb = null;

        if (imageFile != null) {
            imageFileRb = RequestBody.create(MediaType.parse("image/jpeg"), imageFile);
        }
        Call<UploadDelegateImagesResponse> call = imageApiEndPointInterface.uploadChatImage(locale, imageFileRb, id, token);


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
                } else if (response.code() == ACTION_DENIED_ERROR) {
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
    public void submitComplaint(String token, String locale, int orderId, String title, String description, final OnResponseListener onSubmitComplaintResponse) {
        Call<BaseResponse> call = apiEndPointInterface.submitComplaint(locale, orderId, token, title, description);
        call.clone().enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.body() != null) {
                    onSubmitComplaintResponse.onSuccess(response);
                } else if (response.code() == INVALID_TOKEN_ERROR) {
                    onSubmitComplaintResponse.onAuthError();
                } else if (response.code() == SERVER_ERROR) {
                    onSubmitComplaintResponse.onServerError();
                } else if (response.code() == UNAUTHORIZED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onSubmitComplaintResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == ACTION_DENIED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onSubmitComplaintResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == VALIDATION_ERROR) {
                    onSubmitComplaintResponse.onValidationError(null);
                } else {
                    onSubmitComplaintResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                onSubmitComplaintResponse.onFailure();
            }
        });
    }

    @Override
    public void getCancelationReasons(String locale, int customerOrDelegate, final OnResponseListener onGetCancelationReasonsResponse) {
        Call<CancelationReasonsResponse> call = apiEndPointInterface.getOrderCancelationReasons(locale, customerOrDelegate);
        call.clone().enqueue(new Callback<CancelationReasonsResponse>() {
            @Override
            public void onResponse(Call<CancelationReasonsResponse> call, Response<CancelationReasonsResponse> response) {
                if (response.body() != null) {
                    onGetCancelationReasonsResponse.onSuccess(response);
                } else if (response.code() == SERVER_ERROR) {
                    onGetCancelationReasonsResponse.onServerError();
                } else {
                    onGetCancelationReasonsResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<CancelationReasonsResponse> call, Throwable t) {
                onGetCancelationReasonsResponse.onFailure();
            }
        });
    }

    @Override
    public void ignoreOrder(String token, String locale, int id, final OnResponseListener onIgnoreOrderResponse) {
        Call<BaseResponse> call = apiEndPointInterface.ignoreOrder(locale, id, token);

        call.clone().enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.body() != null) {
                    onIgnoreOrderResponse.onSuccess(response);
                } else if (response.code() == INVALID_TOKEN_ERROR) {
                    onIgnoreOrderResponse.onAuthError();
                } else if (response.code() == SERVER_ERROR) {
                    onIgnoreOrderResponse.onServerError();
                } else if (response.code() == UNAUTHORIZED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onIgnoreOrderResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == ACTION_DENIED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onIgnoreOrderResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    onIgnoreOrderResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                onIgnoreOrderResponse.onFailure();
            }
        });
    }

    @Override
    public void getPrayerTimes(String locale, final OnResponseListener onGetPrayerTimesResponse) {
        Call<PrayerTimesResponse> call = apiEndPointInterface.getPrayerTimes(locale);
        call.clone().enqueue(new Callback<PrayerTimesResponse>() {
            @Override
            public void onResponse(Call<PrayerTimesResponse> call, Response<PrayerTimesResponse> response) {
                if (response.body() != null) {
                    onGetPrayerTimesResponse.onSuccess(response);
                } else if (response.code() == SERVER_ERROR) {
                    onGetPrayerTimesResponse.onServerError();
                } else {
                    onGetPrayerTimesResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<PrayerTimesResponse> call, Throwable t) {
                onGetPrayerTimesResponse.onFailure();
            }
        });
    }

    @Override
    public void validateCoupon(String token, String locale, String code, final OnResponseListener onValidateCouponResponse) {
        Call<BaseResponse> call = apiEndPointInterface.validateCoupon(locale, token, code);
        call.clone().enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.body() != null) {
                    onValidateCouponResponse.onSuccess(response);
                } else if (response.code() == INVALID_TOKEN_ERROR) {
                    onValidateCouponResponse.onAuthError();
                } else if (response.code() == SERVER_ERROR) {
                    onValidateCouponResponse.onServerError();
                } else if (response.code() == UNAUTHORIZED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onValidateCouponResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == VALIDATION_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readCodeErrorMessage(errorBodyString);
                            onValidateCouponResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    onValidateCouponResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                onValidateCouponResponse.onFailure();
            }
        });
    }

    private String readFileError(String errorBody) {
        JSONObject errorBodyJson;
        JSONObject errorJsonObject;
        String errorMessage = null;
        try {
            errorBodyJson = new JSONObject(errorBody);
            errorJsonObject = errorBodyJson.getJSONObject(Constants.ERROR);
            errorMessage = errorJsonObject.getString("voicenoteFile");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return errorMessage;
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

    private String readCodeErrorMessage(String errorBody) {
        JSONObject errorBodyJson;
        JSONObject errorJsonObject;
        String errorMessage = null;
        try {
            errorBodyJson = new JSONObject(errorBody);
            errorJsonObject = errorBodyJson.getJSONObject(Constants.ERROR);
            errorMessage = errorJsonObject.getString(Constants.CODE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return errorMessage;
    }

    private String readFirstErrorMessage(String errorBody) {
        JSONObject errorBodyJson;
        JSONObject errorJsonObject;
        String errorMessage = null;
        try {
            errorBodyJson = new JSONObject(errorBody);
            errorJsonObject = errorBodyJson.getJSONObject(Constants.ERROR);
            if (errorJsonObject.keys().hasNext()) {
                errorMessage = errorJsonObject.keys().next() + ":" + errorJsonObject.getString(errorJsonObject.keys().next());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return errorMessage;
    }

}
