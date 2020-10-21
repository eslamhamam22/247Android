package amaz.objects.TwentyfourSeven.ui.MyAccount;

import com.google.firebase.auth.FirebaseAuth;

import java.lang.ref.WeakReference;

import amaz.objects.TwentyfourSeven.data.repositories.UserRepository;
import amaz.objects.TwentyfourSeven.listeners.OnResponseListener;
import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import retrofit2.Response;

public class MainPresenter extends BasePresenter {

    private WeakReference<MainView> view = new WeakReference<>(null);
    private UserRepository repository;

    public MainPresenter(UserRepository repository){
        this.repository = repository;
    }

    public void setView(MainView view){
        this.view = new WeakReference<>(view);
    }

    public void unRegisterOneSignalToken(String token,String local,String regId){
        final MainView mobileVerificationView = view.get();
        mobileVerificationView.showLoading();
        repository.unRegisterDeviceToken(token, local,regId, new OnResponseListener() {

            @Override
            public void onSuccess(Response response) {

                mobileVerificationView.hideLoading();
                mobileVerificationView.navigateToRegisterationOrLogin();

            }

            @Override
            public void onFailure() {
                mobileVerificationView.hideLoading();
                mobileVerificationView.showNetworkError();
            }

            @Override
            public void onAuthError() {
                mobileVerificationView.hideLoading();
                mobileVerificationView.showInvalideTokenError();
            }

            @Override
            public void onInvalidTokenError() {
                mobileVerificationView.hideLoading();
            }

            @Override
            public void onServerError() {
                mobileVerificationView.hideLoading();
                mobileVerificationView.showServerError();
            }

            @Override
            public void onValidationError(String errorMessage) {
                mobileVerificationView.hideLoading();
                //  mobileVerificationView.showInvalidCodeError();
            }

            @Override
            public void onSuspendedUserError(String errorMessage) {
                mobileVerificationView.hideLoading();
                mobileVerificationView.showSuspededUserError(errorMessage);
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

    public interface MainView {
        void showLoading();
        void hideLoading();
        void showInvalideTokenError();
        void showNetworkError();
        void showServerError();
        void showSuspededUserError(String errorMessage);
        void navigateToRegisterationOrLogin();
    }
}
