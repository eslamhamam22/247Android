package amaz.objects.TwentyfourSeven.ui.StoreDetails;

import java.lang.ref.WeakReference;

import amaz.objects.TwentyfourSeven.data.models.OpeningHours;
import amaz.objects.TwentyfourSeven.data.models.StoreDetailsData;
import amaz.objects.TwentyfourSeven.data.models.responses.StoreDetailsResponse;
import amaz.objects.TwentyfourSeven.data.repositories.GoogleRepository;
import amaz.objects.TwentyfourSeven.listeners.OnResponseListener;
import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import retrofit2.Response;

public class StoreDetailsPresenter extends BasePresenter {

    private WeakReference<StoreDetailsView> view = new WeakReference<>(null);
    private GoogleRepository repository;

    public StoreDetailsPresenter(GoogleRepository repository){
        this.repository = repository;
    }

    public void setView(StoreDetailsView view){
        this.view = new WeakReference<>(view);
    }

    public void getStoreWorkingHoures(String storeId, String fields, String locale, String serverKey){
        final StoreDetailsView storeDetailsView = this.view.get();
        storeDetailsView.showLoading();
        repository.getStoreWorkingHoures(storeId, fields, locale, serverKey, new OnResponseListener() {
            @Override
            public void onSuccess(Response response) {
                storeDetailsView.hideLoading();
                StoreDetailsResponse storeDetailsResponse = (StoreDetailsResponse) response.body();
                if (storeDetailsResponse.getStoreDetailsData() != null && storeDetailsResponse.getStoreDetailsData().getOpeningHours() != null){
                    storeDetailsView.showWorkingHoures(storeDetailsResponse.getStoreDetailsData().getOpeningHours());
                }
                storeDetailsView.showStoreDetails(storeDetailsResponse.getStoreDetailsData());
            }

            @Override
            public void onFailure() {
                storeDetailsView.hideLoading();
                storeDetailsView.hideWorkingHoures();
            }

            @Override
            public void onAuthError() {

            }

            @Override
            public void onInvalidTokenError() {

            }

            @Override
            public void onServerError() {
                storeDetailsView.hideLoading();
                storeDetailsView.hideWorkingHoures();
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

    public interface StoreDetailsView {
        void showLoading();
        void hideLoading();
        void hideWorkingHoures();
        void showWorkingHoures(OpeningHours openingHours);
        void showStoreDetails(StoreDetailsData data);
    }
}
