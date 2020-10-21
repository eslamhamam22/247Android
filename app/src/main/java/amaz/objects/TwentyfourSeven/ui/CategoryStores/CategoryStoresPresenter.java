package amaz.objects.TwentyfourSeven.ui.CategoryStores;

import amaz.objects.TwentyfourSeven.data.models.Store;
import amaz.objects.TwentyfourSeven.data.models.responses.NearPlacesResponse;
import amaz.objects.TwentyfourSeven.data.repositories.GoogleRepository;
import amaz.objects.TwentyfourSeven.listeners.OnResponseListener;
import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import retrofit2.Response;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class CategoryStoresPresenter extends BasePresenter {

    private GoogleRepository repository;
    private WeakReference<NearestStoresView> view = new WeakReference<>(null);
    private ArrayList<Store> allStores = new ArrayList<>();

    public CategoryStoresPresenter(GoogleRepository repository){
        this.repository = repository;
    }

    public void setView(NearestStoresView view){
        this.view = new WeakReference<>(view);
    }

    public void getNearestStores(double latitude, double longitude, String type, String locale, final String rankby, String serverKey){
        final NearestStoresView nearestStoresView = this.view.get();
        nearestStoresView.showLoading();
        repository.getNearestStores(latitude, longitude, type, locale, rankby, serverKey, new OnResponseListener() {
            @Override
            public void onSuccess(Response response) {
                nearestStoresView.hideLoading();
                NearPlacesResponse nearPlacesResponse = (NearPlacesResponse)response.body();
                nearestStoresView.showStoresData(nearPlacesResponse, rankby);
            }

            @Override
            public void onFailure() {
                nearestStoresView.hideLoading();
                nearestStoresView.showNetworkError();
            }

            @Override
            public void onAuthError() {

            }

            @Override
            public void onInvalidTokenError() {

            }

            @Override
            public void onServerError() {
                nearestStoresView.hideLoading();
                nearestStoresView.showServerError();
            }

            @Override
            public void onValidationError(String errorMessage) {

            }

            @Override
            public void onSuspendedUserError(String errorMessage) {

            }
        });
    }

    public void getOpenedStores(double latitude, double longitude, String type, String locale, final String rankby, int radius, boolean openNow, String serverKey){
        final NearestStoresView nearestStoresView = this.view.get();
        nearestStoresView.showLoading();
        repository.getOpenedStores(latitude, longitude, type, locale, rankby, radius, openNow, serverKey, new OnResponseListener() {
            @Override
            public void onSuccess(Response response) {
                nearestStoresView.hideLoading();
                NearPlacesResponse nearPlacesResponse = (NearPlacesResponse)response.body();
                if (nearPlacesResponse.getResults().isEmpty()){
                    nearestStoresView.showNoData();
                }
                else {
                    nearestStoresView.showStoresData(nearPlacesResponse, rankby);
                }

            }

            @Override
            public void onFailure() {
                nearestStoresView.hideLoading();
                nearestStoresView.showNetworkError();
            }

            @Override
            public void onAuthError() {

            }

            @Override
            public void onInvalidTokenError() {

            }

            @Override
            public void onServerError() {
                nearestStoresView.hideLoading();
                nearestStoresView.showServerError();
            }

            @Override
            public void onValidationError(String errorMessage) {

            }

            @Override
            public void onSuspendedUserError(String errorMessage) {

            }
        });
    }

    public void getAllStores(double latitude, double longitude, String type, String locale, final String rankby, int radius, String serverKey){
        final NearestStoresView nearestStoresView = this.view.get();
        nearestStoresView.showLoading();
        repository.getAllStores(latitude, longitude, type, locale, rankby, radius, serverKey, new OnResponseListener() {
            @Override
            public void onSuccess(Response response) {
                nearestStoresView.hideLoading();
                NearPlacesResponse nearPlacesResponse = (NearPlacesResponse)response.body();
                if (nearPlacesResponse.getResults().isEmpty() && allStores.isEmpty()){
                    nearestStoresView.showNoData();
                }
                else {
                    allStores.addAll(nearPlacesResponse.getResults());
                    nearestStoresView.showStoresData(nearPlacesResponse, rankby);
                }

            }

            @Override
            public void onFailure() {
                nearestStoresView.hideLoading();
                nearestStoresView.showNetworkError();
            }

            @Override
            public void onAuthError() {

            }

            @Override
            public void onInvalidTokenError() {

            }

            @Override
            public void onServerError() {
                nearestStoresView.hideLoading();
                nearestStoresView.showServerError();
            }

            @Override
            public void onValidationError(String errorMessage) {

            }

            @Override
            public void onSuspendedUserError(String errorMessage) {

            }
        });
    }

    public void getNextNearStores(String nextPageToken, double latitude, double longitude, String type, String locale, final String rankby, String serverKey){
        final NearestStoresView nearestStoresView = this.view.get();
        nearestStoresView.showLoadMore();
        repository.getNextNearStores(nextPageToken, latitude, longitude, type, locale, rankby, serverKey, new OnResponseListener() {
            @Override
            public void onSuccess(Response response) {
                nearestStoresView.hideLoadMore();
                NearPlacesResponse nearPlacesResponse = (NearPlacesResponse)response.body();
                if (nearPlacesResponse.getResults().isEmpty() && allStores.isEmpty()){
                    nearestStoresView.showNoData();
                }
                else {
                    allStores.addAll(nearPlacesResponse.getResults());
                    nearestStoresView.showStoresData(nearPlacesResponse, rankby);
                }

            }

            @Override
            public void onFailure() {
                nearestStoresView.hideLoadMore();
                nearestStoresView.showNetworkError();
            }

            @Override
            public void onAuthError() {

            }

            @Override
            public void onInvalidTokenError() {

            }

            @Override
            public void onServerError() {
                nearestStoresView.hideLoadMore();
                nearestStoresView.showServerError();
            }

            @Override
            public void onValidationError(String errorMessage) {

            }

            @Override
            public void onSuspendedUserError(String errorMessage) {

            }
        });
    }

    public void getNextOpenedStores(String nextPageToken, double latitude, double longitude, String type, String locale, final String rankby, int radius, boolean openNow, String serverKey){
        final NearestStoresView nearestStoresView = this.view.get();
        nearestStoresView.showLoadMore();
        repository.getNextOpenedStores(nextPageToken, latitude, longitude, type, locale, rankby, radius, openNow, serverKey, new OnResponseListener() {
            @Override
            public void onSuccess(Response response) {
                nearestStoresView.hideLoadMore();
                NearPlacesResponse nearPlacesResponse = (NearPlacesResponse)response.body();
                nearestStoresView.showStoresData(nearPlacesResponse, rankby);
            }

            @Override
            public void onFailure() {
                nearestStoresView.hideLoadMore();
                nearestStoresView.showNetworkError();
            }

            @Override
            public void onAuthError() {

            }

            @Override
            public void onInvalidTokenError() {

            }

            @Override
            public void onServerError() {
                nearestStoresView.hideLoadMore();
                nearestStoresView.showServerError();
            }

            @Override
            public void onValidationError(String errorMessage) {

            }

            @Override
            public void onSuspendedUserError(String errorMessage) {

            }
        });
    }

    public void getNextAllStores(String nextPageToken, double latitude, double longitude, String type, String locale, final String rankby, int radius, String serverKey){
        final NearestStoresView nearestStoresView = this.view.get();
        nearestStoresView.showLoadMore();
        repository.getNextAllStores(nextPageToken, latitude, longitude, type, locale, rankby, radius, serverKey, new OnResponseListener() {
            @Override
            public void onSuccess(Response response) {
                nearestStoresView.hideLoadMore();
                NearPlacesResponse nearPlacesResponse = (NearPlacesResponse)response.body();
                nearestStoresView.showStoresData(nearPlacesResponse, rankby);
            }

            @Override
            public void onFailure() {
                nearestStoresView.hideLoadMore();
                nearestStoresView.showNetworkError();
            }

            @Override
            public void onAuthError() {

            }

            @Override
            public void onInvalidTokenError() {

            }

            @Override
            public void onServerError() {
                nearestStoresView.hideLoadMore();
                nearestStoresView.showServerError();
            }

            @Override
            public void onValidationError(String errorMessage) {

            }

            @Override
            public void onSuspendedUserError(String errorMessage) {

            }
        });
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {

    }

    public interface NearestStoresView {
        void showLoading();
        void hideLoading();
        void showLoadMore();
        void hideLoadMore();
        void showNetworkError();
        void showServerError();
        void showNoData();
        void showStoresData(NearPlacesResponse nearPlacesResponse, String rankBy);
    }
}
