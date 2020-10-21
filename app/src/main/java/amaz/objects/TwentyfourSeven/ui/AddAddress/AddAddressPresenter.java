package amaz.objects.TwentyfourSeven.ui.AddAddress;

import java.lang.ref.WeakReference;

import amaz.objects.TwentyfourSeven.data.repositories.UserRepository;
import amaz.objects.TwentyfourSeven.listeners.OnResponseListener;
import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import retrofit2.Response;

public class AddAddressPresenter extends BasePresenter {

    private UserRepository repository;
    private WeakReference<AddAddressView> view = new WeakReference<>(null);

    public AddAddressPresenter(UserRepository repository){
        this.repository = repository;
    }

    public void setView(AddAddressView view){
        this.view = new WeakReference<>(view);
    }

    public void addAddress(String token, String locale, String addressTitle, String addressDetails, double latitude, double longitude){
        final AddAddressView addAddressView = view.get();
        if (validateAddressTitle(addressTitle)) {
            addAddressView.showLoading();
            repository.addAddress(token, locale, addressTitle, addressDetails, latitude, longitude, new OnResponseListener() {
                @Override
                public void onSuccess(Response response) {
                    addAddressView.hideLoading();
                    addAddressView.navigateToMyAddresses();
                }

                @Override
                public void onFailure() {
                    addAddressView.hideLoading();
                    addAddressView.showNetworkError();
                }

                @Override
                public void onAuthError() {
                    addAddressView.hideLoading();
                    addAddressView.showInvalideTokenError();
                }

                @Override
                public void onInvalidTokenError() {
                    addAddressView.hideLoading();
                }

                @Override
                public void onServerError() {
                    addAddressView.hideLoading();
                    addAddressView.showServerError();
                }

                @Override
                public void onValidationError(String errorMessage) {
                    addAddressView.hideLoading();
                }

                @Override
                public void onSuspendedUserError(String errorMessage) {
                    addAddressView.hideLoading();
                    addAddressView.showSuspededUserError(errorMessage);
                }
            });
        }
    }

    private boolean validateAddressTitle(String addressTitle){
        AddAddressView addAddressView = view.get();
        boolean isValid = true;
        if (addressTitle.isEmpty()){
            isValid = false;
            addAddressView.showEmptyAddressNameError();
        }
        return isValid;
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

    public interface AddAddressView {
        void showLoading();
        void hideLoading();
        void showEmptyAddressNameError();
        void showInvalideTokenError();
        void showNetworkError();
        void showServerError();
        void showSuspededUserError(String errorMessage);
        void navigateToMyAddresses();
    }
}
