package amaz.objects.TwentyfourSeven.data.repositories;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import amaz.objects.TwentyfourSeven.MApplication;
import amaz.objects.TwentyfourSeven.api.ApiEndPointInterface;
import amaz.objects.TwentyfourSeven.data.models.responses.BlockedAreasResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.CategoriesResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.HowToUseResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.PageResponse;
import amaz.objects.TwentyfourSeven.listeners.OnResponseListener;
import amaz.objects.TwentyfourSeven.utilities.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkGeneralRepository implements GeneralRepository {

    private final int SERVER_ERROR = 500;
    private final int BAD_REQUEST_ERROR = 400;   // bad request
    private final int PAGE_NOT_FOUND_ERROR = 404;
    private final int UNAUTHORIZED_ERROR = 403;
    private final int INVALID_TOKEN_ERROR = 401;   //invalid token

    private ApiEndPointInterface apiEndPointInterface = MApplication.getInstance().getApi();

    @Override
    public void getPageData(String locale, String page, final OnResponseListener onGetPageResponse) {
        Call<PageResponse> call = apiEndPointInterface.getPageData(locale, page);
        call.clone().enqueue(new Callback<PageResponse>() {
            @Override
            public void onResponse(Call<PageResponse> call, Response<PageResponse> response) {
                if (response.body() != null){
                    onGetPageResponse.onSuccess(response);
                }
                else if(response.code() == BAD_REQUEST_ERROR){
                    onGetPageResponse.onInvalidTokenError();
                }
                else if(response.code() == PAGE_NOT_FOUND_ERROR){
                    onGetPageResponse.onAuthError();
                }
                else if(response.code() == SERVER_ERROR){
                    onGetPageResponse.onServerError();
                }
                else {
                    onGetPageResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<PageResponse> call, Throwable t) {
                onGetPageResponse.onFailure();
            }
        });
    }

    @Override
    public void getHowToUseData(String locale, final OnResponseListener onGetHowToUseResponse) {
        Call<HowToUseResponse> call = apiEndPointInterface.getHowToUseData(locale);
        call.clone().enqueue(new Callback<HowToUseResponse>() {
            @Override
            public void onResponse(Call<HowToUseResponse> call, Response<HowToUseResponse> response) {
                if (response.body() != null){
                    onGetHowToUseResponse.onSuccess(response);
                }
                else if(response.code() == PAGE_NOT_FOUND_ERROR){
                    onGetHowToUseResponse.onAuthError();
                }
                else if(response.code() == SERVER_ERROR){
                    onGetHowToUseResponse.onServerError();
                }
                else {
                    onGetHowToUseResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<HowToUseResponse> call, Throwable t) {
                onGetHowToUseResponse.onFailure();
            }
        });
    }

    @Override
    public void getHowToBecomeADelegateData(String locale, final OnResponseListener onGetHowToUseResponse) {
        Call<HowToUseResponse> call = apiEndPointInterface.getHowToBecomeADelegateData(locale);
        call.clone().enqueue(new Callback<HowToUseResponse>() {
            @Override
            public void onResponse(Call<HowToUseResponse> call, Response<HowToUseResponse> response) {
                if (response.body() != null){
                    onGetHowToUseResponse.onSuccess(response);
                }
                else if(response.code() == PAGE_NOT_FOUND_ERROR){
                    onGetHowToUseResponse.onAuthError();
                }
                else if(response.code() == SERVER_ERROR){
                    onGetHowToUseResponse.onServerError();
                }
                else {
                    onGetHowToUseResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<HowToUseResponse> call, Throwable t) {
                onGetHowToUseResponse.onFailure();
            }
        });
    }

    @Override
    public void getAllCategories(String locale, final OnResponseListener onGetCategoriesResponse) {
        Call<CategoriesResponse> call = apiEndPointInterface.getCategories(locale);
        call.clone().enqueue(new Callback<CategoriesResponse>() {
            @Override
            public void onResponse(Call<CategoriesResponse> call, Response<CategoriesResponse> response) {
                if (response.body() != null){
                    onGetCategoriesResponse.onSuccess(response);
                }
                else if(response.code() == SERVER_ERROR){
                    onGetCategoriesResponse.onServerError();
                }
                else {
                    onGetCategoriesResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<CategoriesResponse> call, Throwable t) {
                onGetCategoriesResponse.onFailure();
            }
        });
    }

    @Override
    public void getAllBlockedArea(String locale, final OnResponseListener onGetCategoriesResponse) {
        Call<BlockedAreasResponse> call = apiEndPointInterface.getBlockedAreas(locale);
        call.clone().enqueue(new Callback<BlockedAreasResponse>() {
            @Override
            public void onResponse(Call<BlockedAreasResponse> call, Response<BlockedAreasResponse> response) {
                if (response.body() != null){
                    onGetCategoriesResponse.onSuccess(response);
                }
                else if(response.code() == SERVER_ERROR){
                    onGetCategoriesResponse.onServerError();
                }
                else if(response.code() == INVALID_TOKEN_ERROR){
                    try {
                        if (response.errorBody()!= null){
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onGetCategoriesResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else if (response.code() == UNAUTHORIZED_ERROR){
                    try {
                        if (response.errorBody()!= null){
                            String errorBodyString = response.errorBody().string();
                            String errorMessage = readErrorMessage(errorBodyString);
                            onGetCategoriesResponse.onValidationError(errorMessage);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    onGetCategoriesResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<BlockedAreasResponse> call, Throwable t) {
                onGetCategoriesResponse.onFailure();
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
