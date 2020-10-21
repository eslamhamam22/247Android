package amaz.objects.TwentyfourSeven.data.repositories;

import amaz.objects.TwentyfourSeven.listeners.OnResponseListener;

public interface GoogleRepository {

    void getGoogleAccessToken(String clientId, String clientSecret, String authCode, OnResponseListener onGetAccessTokenResponse);
    void getNearestStores(double latitude, double longitude, String type, String locale, String rankby, String serverKey, final OnResponseListener onGetNearestStoresResponse);
    void getOpenedStores(double latitude, double longitude, String type, String locale, String rankby, int radius, boolean openNow, String serverKey, final OnResponseListener onGetNearestStoresResponse);
    void getNextNearStores(String nextNearToken, double latitude, double longitude, String type, String locale, String rankby, String serverKey, final OnResponseListener onGetNearestStoresResponse);
    void getNextOpenedStores(String nextOpenedToken, double latitude, double longitude, String type, String locale, String rankby, int radius, boolean openNow, String serverKey, final OnResponseListener onGetNearestStoresResponse);
    void getAllStores(double latitude, double longitude, String type, String locale, String rankby, int radius, String serverKey, final OnResponseListener onGetNearestStoresResponse);
    void getNextAllStores(String nextPageToken, double latitude, double longitude, String type, String locale, String rankby, int radius, String serverKey, final OnResponseListener onGetNearestStoresResponse);
    void getStoreWorkingHoures(String storeId, String fields, String locale, String serverKey, OnResponseListener onGetStoreWorkingHouresResponse);
    void getDirections(String origin, String destination, String mode, String serverKey, OnResponseListener onGetDirectionsResponse);

}
