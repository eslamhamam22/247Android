package amaz.objects.TwentyfourSeven.ui.CarDetails;

import java.lang.ref.WeakReference;

import amaz.objects.TwentyfourSeven.data.models.CarDetailsData;
import amaz.objects.TwentyfourSeven.data.models.responses.CarDetailsResponse;
import amaz.objects.TwentyfourSeven.data.repositories.UserRepository;
import amaz.objects.TwentyfourSeven.listeners.OnResponseListener;
import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import retrofit2.Response;

public class CarDetailsPresenter extends BasePresenter {

    private UserRepository repository;
    private WeakReference<CarDetailsView> view = new WeakReference<>(null);

    public CarDetailsPresenter(UserRepository repository){
        this.repository = repository;
    }

    public void setView(CarDetailsView view){
        this.view = new WeakReference<>(view);
    }

    public void getCarDetails(String token, String locale){

        final CarDetailsView carDetailsView = view.get();
        carDetailsView.showLoading();
        repository.getCarDetails(token, locale, new OnResponseListener() {
            @Override
            public void onSuccess(Response response) {
                carDetailsView.hideLoading();
                carDetailsView.showCarDetailsData(((CarDetailsResponse)response.body()).getData());
            }

            @Override
            public void onFailure() {
                carDetailsView.hideLoading();
                carDetailsView.showNetworkError();
            }

            @Override
            public void onAuthError() {
                carDetailsView.hideLoading();
                carDetailsView.showInvalideTokenError();
            }

            @Override
            public void onInvalidTokenError() {
                carDetailsView.hideLoading();
            }

            @Override
            public void onServerError() {
                carDetailsView.hideLoading();
                carDetailsView.showServerError();
            }

            @Override
            public void onValidationError(String errorMessage) {
                carDetailsView.hideLoading();
                carDetailsView.showNoData();
            }

            @Override
            public void onSuspendedUserError(String errorMessage) {
                carDetailsView.hideLoading();
                carDetailsView.showSuspededUserError(errorMessage);
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

    public interface CarDetailsView {
        void showLoading();
        void hideLoading();
        void showInvalideTokenError();
        void showNetworkError();
        void showServerError();
        void showSuspededUserError(String errorMessage);
        void showNoData();
        void showCarDetailsData(CarDetailsData data);
    }
}
