package amaz.objects.TwentyfourSeven.ui.MobileRegisteration;

import java.lang.ref.WeakReference;

import amaz.objects.TwentyfourSeven.data.repositories.UserRepository;
import amaz.objects.TwentyfourSeven.listeners.OnResponseListener;
import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import retrofit2.Response;

public class MobileRegisterationPresenter extends BasePresenter {

    private UserRepository repository;
    private WeakReference<MobileRegisterationView> view = new WeakReference<>(null);

    public MobileRegisterationPresenter(UserRepository repository) {
        this.repository = repository;
    }

    public void setView(MobileRegisterationView view){
        this.view = new WeakReference<>(view);
    }

    public void requestVerificationCode(String locale, final String countryCode, final String phone){
        final MobileRegisterationView mobileRegisterationView = view.get();
        if (validatePhone(phone)){
            mobileRegisterationView.showLoading();
            repository.requestVerificationCode(locale, countryCode+phone, new OnResponseListener() {
                @Override
                public void onSuccess(Response response) {
                    mobileRegisterationView.hideLoading();
                    mobileRegisterationView.navigateToMobileVerification();
                }

                @Override
                public void onFailure() {
                    mobileRegisterationView.hideLoading();
                    mobileRegisterationView.showNetworkError();
                }

                @Override
                public void onAuthError() {
                    mobileRegisterationView.hideLoading();
                }

                @Override
                public void onInvalidTokenError() {
                    mobileRegisterationView.hideLoading();
                }

                @Override
                public void onServerError() {
                    mobileRegisterationView.hideLoading();
                    mobileRegisterationView.showServerError();
                }

                @Override
                public void onValidationError(String errorMessage) {
                    mobileRegisterationView.hideLoading();
                    mobileRegisterationView.showValidationError(errorMessage);
                }

                @Override
                public void onSuspendedUserError(String errorMessage) {
                    mobileRegisterationView.hideLoading();
                    mobileRegisterationView.showSuspededUserError(errorMessage);
                }
            });
        }
        else {
            mobileRegisterationView.showEmptyPhoneError();
        }
    }

    public void changeUserPhoneNumber(String token, String locale, final String countryCode, final String phone){
        final MobileRegisterationView mobileRegisterationView = view.get();
        if (validatePhone(phone)){
            mobileRegisterationView.showLoading();
            repository.changeUserPhoneRequestCode(token, locale, countryCode+phone, new OnResponseListener() {
                @Override
                public void onSuccess(Response response) {
                    mobileRegisterationView.hideLoading();
                    mobileRegisterationView.navigateToMobileVerification();
                }

                @Override
                public void onFailure() {
                    mobileRegisterationView.hideLoading();
                    mobileRegisterationView.showNetworkError();
                }

                @Override
                public void onAuthError() {
                    mobileRegisterationView.hideLoading();
                }

                @Override
                public void onInvalidTokenError() {
                    mobileRegisterationView.hideLoading();
                }

                @Override
                public void onServerError() {
                    mobileRegisterationView.hideLoading();
                    mobileRegisterationView.showServerError();
                }

                @Override
                public void onValidationError(String errorMessage) {
                    mobileRegisterationView.hideLoading();
                    mobileRegisterationView.showValidationError(errorMessage);
                }

                @Override
                public void onSuspendedUserError(String errorMessage) {
                    mobileRegisterationView.hideLoading();
                    mobileRegisterationView.showSuspededUserError(errorMessage);
                }
            });
        }
        else {
            mobileRegisterationView.showEmptyPhoneError();
        }
    }

    private boolean validatePhone(String phone){
        boolean isValid = true;
        if (phone.isEmpty()){
            isValid = false;
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

    public interface MobileRegisterationView {
        void showLoading();
        void hideLoading();
        void showEmptyPhoneError();
        void showValidationError(String errorMessage);
        void navigateToMobileVerification();
        void showNetworkError();
        void showServerError();
        void showSuspededUserError(String errorMessage);
    }
}
