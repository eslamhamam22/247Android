package amaz.objects.TwentyfourSeven.data.repositories;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import amaz.objects.TwentyfourSeven.MApplication;
import amaz.objects.TwentyfourSeven.api.ApiEndPointInterface;
import amaz.objects.TwentyfourSeven.data.models.responses.DirectPaymentAuthorizeResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.DirectPaymentConfirmResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.DirectPaymentResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.PaymentInquiryResponse;
import amaz.objects.TwentyfourSeven.listeners.OnResponseListener;
import amaz.objects.TwentyfourSeven.utilities.Constants;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkPaymentRepository implements PaymentRepository {

    private final int SERVER_ERROR = 500;
    private final int BAD_REQUEST_ERROR = 400;   // bad request
    private final int INVALID_TOKEN_ERROR = 401;   //invalid token
    private final int VALIDATION_ERROR = 422;
    private final int PAGE_NOT_FOUND_ERROR = 404;
    private final int UNAUTHORIZED_ERROR = 403;

    private ApiEndPointInterface apiEndPointInterface = MApplication.getInstance().getPaymentStcApi();

    @Override
    public void stcDirectPayment(String token, final OnResponseListener onStcDirectPaymentResponse) {
        Call<DirectPaymentResponse> call = apiEndPointInterface.postStcDirectPayment(token);
        call.clone().enqueue(new Callback<DirectPaymentResponse>() {
            @Override
            public void onResponse(Call<DirectPaymentResponse> call, Response<DirectPaymentResponse> response) {
                if (response.body() != null) {
                    onStcDirectPaymentResponse.onSuccess(response);
                } else if (response.code() == INVALID_TOKEN_ERROR) {
                    onStcDirectPaymentResponse.onAuthError();
                } else if (response.code() == SERVER_ERROR) {
                    onStcDirectPaymentResponse.onServerError();
                } else if (response.code() == UNAUTHORIZED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onStcDirectPaymentResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    onStcDirectPaymentResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<DirectPaymentResponse> call, Throwable t) {
                onStcDirectPaymentResponse.onFailure();
            }
        });
    }

    @Override
    public void stcDirectPaymentAuthorize(String token, final OnResponseListener onStcDirectPaymentAuthorizeResponse) {
        Call<DirectPaymentAuthorizeResponse> call = apiEndPointInterface.postStcDirectPaymentAuthorize(token);
        call.clone().enqueue(new Callback<DirectPaymentAuthorizeResponse>() {
            @Override
            public void onResponse(Call<DirectPaymentAuthorizeResponse> call, Response<DirectPaymentAuthorizeResponse> response) {
                if (response.body() != null) {
                    onStcDirectPaymentAuthorizeResponse.onSuccess(response);
                } else if (response.code() == INVALID_TOKEN_ERROR) {
                    onStcDirectPaymentAuthorizeResponse.onAuthError();
                } else if (response.code() == SERVER_ERROR) {
                    onStcDirectPaymentAuthorizeResponse.onServerError();
                } else if (response.code() == UNAUTHORIZED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onStcDirectPaymentAuthorizeResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    onStcDirectPaymentAuthorizeResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<DirectPaymentAuthorizeResponse> call, Throwable t) {
                onStcDirectPaymentAuthorizeResponse.onFailure();
            }
        });
    }

    @Override
    public void stcDirectPaymentConfirm(String token, final OnResponseListener onStcDirectPaymentConfirmResponse) {
        Call<DirectPaymentConfirmResponse> call = apiEndPointInterface.postStcDirectPaymentConfirm(token);
        call.clone().enqueue(new Callback<DirectPaymentConfirmResponse>() {
            @Override
            public void onResponse(Call<DirectPaymentConfirmResponse> call, Response<DirectPaymentConfirmResponse> response) {
                if (response.body() != null) {
                    onStcDirectPaymentConfirmResponse.onSuccess(response);
                } else if (response.code() == INVALID_TOKEN_ERROR) {
                    onStcDirectPaymentConfirmResponse.onAuthError();
                } else if (response.code() == SERVER_ERROR) {
                    onStcDirectPaymentConfirmResponse.onServerError();
                } else if (response.code() == UNAUTHORIZED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onStcDirectPaymentConfirmResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    onStcDirectPaymentConfirmResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<DirectPaymentConfirmResponse> call, Throwable t) {
                onStcDirectPaymentConfirmResponse.onFailure();
            }
        });
    }

    @Override
    public void stcPaymentInquiry(String token, final OnResponseListener onStcPaymentInquiryResponse) {
        Call<PaymentInquiryResponse> call = apiEndPointInterface.postStcPaymentInquiry(token);
        call.clone().enqueue(new Callback<PaymentInquiryResponse>() {
            @Override
            public void onResponse(Call<PaymentInquiryResponse> call, Response<PaymentInquiryResponse> response) {
                if (response.body() != null) {
                    onStcPaymentInquiryResponse.onSuccess(response);
                } else if (response.code() == INVALID_TOKEN_ERROR) {
                    onStcPaymentInquiryResponse.onAuthError();
                } else if (response.code() == SERVER_ERROR) {
                    onStcPaymentInquiryResponse.onServerError();
                } else if (response.code() == UNAUTHORIZED_ERROR) {
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onStcPaymentInquiryResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    onStcPaymentInquiryResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<PaymentInquiryResponse> call, Throwable t) {
                onStcPaymentInquiryResponse.onFailure();
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

}
