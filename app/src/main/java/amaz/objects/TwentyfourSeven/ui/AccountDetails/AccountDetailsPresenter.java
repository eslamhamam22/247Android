package amaz.objects.TwentyfourSeven.ui.AccountDetails;

import java.io.File;
import java.lang.ref.WeakReference;

import amaz.objects.TwentyfourSeven.data.models.User;
import amaz.objects.TwentyfourSeven.data.models.responses.ProfileResponse;
import amaz.objects.TwentyfourSeven.data.repositories.UserRepository;
import amaz.objects.TwentyfourSeven.listeners.OnResponseListener;
import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import retrofit2.Response;

public class AccountDetailsPresenter extends BasePresenter {

    private UserRepository repository;
    private WeakReference<AccountDetailsView> view = new WeakReference<>(null);

    public AccountDetailsPresenter(UserRepository repository) {
        this.repository = repository;
    }

    public void setView(AccountDetailsView view){
        this.view = new WeakReference<>(view);
    }

    public void updateUserProfile(String token, String locale, String fullName, String birthDate, String gender, String city, File imageFile){
        final AccountDetailsView accountDetailsView = view.get();
        if (validateFullName(fullName)){
            accountDetailsView.showLoading();
            repository.updateUserProfile(token, locale, fullName, birthDate, city, gender, imageFile, new OnResponseListener() {
                @Override
                public void onSuccess(Response response) {
                    accountDetailsView.hideLoading();
                    accountDetailsView.setUserData(((ProfileResponse) response.body()).getData());
                    accountDetailsView.navigateToStores();
                }

                @Override
                public void onFailure() {
                    accountDetailsView.hideLoading();
                    accountDetailsView.showNetworkError();
                }

                @Override
                public void onAuthError() {
                    accountDetailsView.hideLoading();
                }

                @Override
                public void onInvalidTokenError() {
                    accountDetailsView.hideLoading();
                    accountDetailsView.showInvalideTokenError();
                }

                @Override
                public void onServerError() {
                    accountDetailsView.hideLoading();
                    accountDetailsView.showServerError();
                }

                @Override
                public void onValidationError(String errorMessage) {
                    accountDetailsView.hideLoading();
                }

                @Override
                public void onSuspendedUserError(String errorMessage) {
                    accountDetailsView.hideLoading();
                    accountDetailsView.showSuspededUserError(errorMessage);
                }
            });
        }
    }

    private boolean validateFullName(String fullName){
        AccountDetailsView accountDetailsView = view.get();
        boolean isValid = true;
        if (fullName.isEmpty()){
            isValid = false;
            accountDetailsView.showEmptyFullNameError();
        }
        /*else {
            if (!ValidationsUtilities.isValidName(fullName)){
                isValid = false;
                accountDetailsView.showInvalidFullNameError();
            }
        }*/
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

    public interface AccountDetailsView {

        void showLoading();
        void hideLoading();
        void showEmptyFullNameError();
        void showInvalidFullNameError();
        void showInvalideTokenError();
        void showNetworkError();
        void showServerError();
        void showSuspededUserError(String errorMessage);
        void showSuccessUpdate();
        void setUserData(User user);
        void navigateToStores();

    }
}
