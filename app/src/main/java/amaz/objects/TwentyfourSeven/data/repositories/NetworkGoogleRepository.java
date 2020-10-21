package amaz.objects.TwentyfourSeven.data.repositories;

import amaz.objects.TwentyfourSeven.MApplication;
import amaz.objects.TwentyfourSeven.api.ApiEndPointInterface;
import amaz.objects.TwentyfourSeven.data.models.GoogleTokenRequestBody;
import amaz.objects.TwentyfourSeven.data.models.responses.DirectionsResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.GoogleAccessTokenResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.NearPlacesResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.StoreDetailsResponse;
import amaz.objects.TwentyfourSeven.listeners.OnResponseListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkGoogleRepository implements GoogleRepository {

    private final int SERVER_ERROR = 500;
    private ApiEndPointInterface googleApiEndPointInterface = MApplication.getInstance().getGoogleApi();
    private ApiEndPointInterface placesApiEndPointInterface = MApplication.getInstance().getPlacesApi();

    @Override
    public void getGoogleAccessToken(String clientId, String clientSecret, String authCode, final OnResponseListener onGetAccessTokenResponse) {
        GoogleTokenRequestBody requestBody = new GoogleTokenRequestBody("authorization_code",clientId,clientSecret,null,authCode);
        Call<GoogleAccessTokenResponse> call = googleApiEndPointInterface.getGoogleAccessToken(requestBody);
        call.clone().enqueue(new Callback<GoogleAccessTokenResponse>() {
            @Override
            public void onResponse(Call<GoogleAccessTokenResponse> call, Response<GoogleAccessTokenResponse> response) {
                if (response.body() != null){
                    onGetAccessTokenResponse.onSuccess(response);
                }
                else if(response.code() == SERVER_ERROR){
                    onGetAccessTokenResponse.onServerError();
                }
                else {
                    onGetAccessTokenResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<GoogleAccessTokenResponse> call, Throwable t) {
                onGetAccessTokenResponse.onFailure();
            }
        });
    }

    @Override
    public void getNearestStores(double latitude, double longitude, String type, String locale, String rankby, String serverKey, final OnResponseListener onGetNearestStoresResponse) {
        Call<NearPlacesResponse> call = placesApiEndPointInterface.getNearestStores(latitude+","+longitude,type,locale,rankby,serverKey);
        call.clone().enqueue(new Callback<NearPlacesResponse>() {
            @Override
            public void onResponse(Call<NearPlacesResponse> call, Response<NearPlacesResponse> response) {

                if (response.body() != null){
                    onGetNearestStoresResponse.onSuccess(response);
                }
                else if(response.code() == SERVER_ERROR){
                    onGetNearestStoresResponse.onServerError();
                }
                else {
                    onGetNearestStoresResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<NearPlacesResponse> call, Throwable t) {
                onGetNearestStoresResponse.onFailure();
            }
        });
    }

    @Override
    public void getOpenedStores(double latitude, double longitude, String type, String locale, String rankby, int radius, boolean openNow, String serverKey, final OnResponseListener onGetOpenedStoresResponse) {
        Call<NearPlacesResponse> call = placesApiEndPointInterface.getOpenedStores(latitude+","+longitude,type,locale,rankby,radius,openNow,serverKey);
        call.clone().enqueue(new Callback<NearPlacesResponse>() {
            @Override
            public void onResponse(Call<NearPlacesResponse> call, Response<NearPlacesResponse> response) {
                if (response.body() != null){
                    onGetOpenedStoresResponse.onSuccess(response);
                }
                else if(response.code() == SERVER_ERROR){
                    onGetOpenedStoresResponse.onServerError();
                }
                else {
                    onGetOpenedStoresResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<NearPlacesResponse> call, Throwable t) {
                onGetOpenedStoresResponse.onFailure();
            }
        });
    }

    @Override
    public void getNextNearStores(String nextNearToken, double latitude, double longitude, String type, String locale, String rankby, String serverKey, final OnResponseListener onGetNextOpenedStoresResponse) {
        Call<NearPlacesResponse> call = placesApiEndPointInterface.getNextNearStores(nextNearToken, latitude+","+longitude,type,locale,rankby,serverKey);
        call.clone().enqueue(new Callback<NearPlacesResponse>() {
            @Override
            public void onResponse(Call<NearPlacesResponse> call, Response<NearPlacesResponse> response) {
                if (response.body() != null){
                    onGetNextOpenedStoresResponse.onSuccess(response);
                }
                else if(response.code() == SERVER_ERROR){
                    onGetNextOpenedStoresResponse.onServerError();
                }
                else {
                    onGetNextOpenedStoresResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<NearPlacesResponse> call, Throwable t) {
                onGetNextOpenedStoresResponse.onFailure();
            }
        });
    }

    @Override
    public void getNextOpenedStores(String nextOpenedToken,double latitude, double longitude, String type, String locale, String rankby, int radius, boolean openNow, String serverKey, final OnResponseListener onGetOpenedStoresResponse) {
        Call<NearPlacesResponse> call = placesApiEndPointInterface.getNextOpenedStores(nextOpenedToken, latitude+","+longitude,type,locale,rankby,radius,openNow,serverKey);
        call.clone().enqueue(new Callback<NearPlacesResponse>() {
            @Override
            public void onResponse(Call<NearPlacesResponse> call, Response<NearPlacesResponse> response) {
                if (response.body() != null){
                    onGetOpenedStoresResponse.onSuccess(response);
                }
                else if(response.code() == SERVER_ERROR){
                    onGetOpenedStoresResponse.onServerError();
                }
                else {
                    onGetOpenedStoresResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<NearPlacesResponse> call, Throwable t) {
                onGetOpenedStoresResponse.onFailure();
            }
        });
    }

    @Override
    public void getAllStores(double latitude, double longitude, String type, String locale, String rankby, int radius, String serverKey, final OnResponseListener onGetAllStoresResponse) {
        Call<NearPlacesResponse> call = placesApiEndPointInterface.getAllStores(latitude+","+longitude,type,locale,rankby,radius,serverKey);
        call.clone().enqueue(new Callback<NearPlacesResponse>() {
            @Override
            public void onResponse(Call<NearPlacesResponse> call, Response<NearPlacesResponse> response) {
                if (response.body() != null){
                    onGetAllStoresResponse.onSuccess(response);
                }
                else if(response.code() == SERVER_ERROR){
                    onGetAllStoresResponse.onServerError();
                }
                else {
                    onGetAllStoresResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<NearPlacesResponse> call, Throwable t) {
                onGetAllStoresResponse.onFailure();
            }
        });
    }

    @Override
    public void getNextAllStores(String nextPageToken, double latitude, double longitude, String type, String locale, String rankby, int radius, String serverKey, final OnResponseListener onGetNextAllStoresResponse) {
        Call<NearPlacesResponse> call = placesApiEndPointInterface.getNextAllStores(nextPageToken, latitude+","+longitude,type,locale,rankby,radius,serverKey);
        call.clone().enqueue(new Callback<NearPlacesResponse>() {
            @Override
            public void onResponse(Call<NearPlacesResponse> call, Response<NearPlacesResponse> response) {
                if (response.body() != null){
                    onGetNextAllStoresResponse.onSuccess(response);
                }
                else if(response.code() == SERVER_ERROR){
                    onGetNextAllStoresResponse.onServerError();
                }
                else {
                    onGetNextAllStoresResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<NearPlacesResponse> call, Throwable t) {
                onGetNextAllStoresResponse.onFailure();
            }
        });
    }

    @Override
    public void getStoreWorkingHoures(String storeId, String fields, String locale, String serverKey, final OnResponseListener onGetStoreWorkingHouresResponse) {
        Call<StoreDetailsResponse> call = placesApiEndPointInterface.getStoreWorkingHoures(storeId, fields, locale, serverKey);
        call.clone().enqueue(new Callback<StoreDetailsResponse>() {
            @Override
            public void onResponse(Call<StoreDetailsResponse> call, Response<StoreDetailsResponse> response) {
                if (response.body() != null){
                    onGetStoreWorkingHouresResponse.onSuccess(response);
                }
                else if(response.code() == SERVER_ERROR){
                    onGetStoreWorkingHouresResponse.onServerError();
                }
                else {
                    onGetStoreWorkingHouresResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<StoreDetailsResponse> call, Throwable t) {
                onGetStoreWorkingHouresResponse.onFailure();
            }
        });
    }

    @Override
    public void getDirections(String origin, String destination, String mode, String serverKey, final OnResponseListener onGetDirectionsResponse) {
        Call<DirectionsResponse> call = placesApiEndPointInterface.getDirections(origin, destination, mode, serverKey);
        call.clone().enqueue(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                if (response.body() != null){
                    onGetDirectionsResponse.onSuccess(response);
                }
                else if(response.code() == SERVER_ERROR){
                    onGetDirectionsResponse.onServerError();
                }
                else {
                    onGetDirectionsResponse.onFailure();
                }
            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                onGetDirectionsResponse.onFailure();
            }
        });
    }
}
