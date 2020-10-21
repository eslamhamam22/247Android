package amaz.objects.TwentyfourSeven.ui.Settings;

import java.lang.ref.WeakReference;

import amaz.objects.TwentyfourSeven.data.models.User;
import amaz.objects.TwentyfourSeven.data.models.responses.ProfileResponse;
import amaz.objects.TwentyfourSeven.data.repositories.UserRepository;
import amaz.objects.TwentyfourSeven.listeners.OnResponseListener;
import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import retrofit2.Response;

public class SettingsPresenter extends BasePresenter {

    private UserRepository repository;
    private WeakReference<SettingsView> view = new WeakReference<>(null);

    public SettingsPresenter(UserRepository repository){
        this.repository = repository;
    }

    public void setView(SettingsView view){
        this.view = new WeakReference<>(view);
    }

    public void changeUserLanguage(String token, String locale, String language){
        final SettingsView settingsView = view.get();
        settingsView.showLoading();
        repository.changeUserLanguage(token, locale, language, new OnResponseListener() {
            @Override
            public void onSuccess(Response response) {
                settingsView.hideLoading();
                settingsView.setUserData(((ProfileResponse)response.body()).getData());
                settingsView.showSuccess();
            }

            @Override
            public void onFailure() {
                settingsView.hideLoading();
                settingsView.showNetworkError();
            }

            @Override
            public void onAuthError() {
                settingsView.hideLoading();
            }

            @Override
            public void onInvalidTokenError() {
                settingsView.hideLoading();
                settingsView.showInvalideTokenError();
            }

            @Override
            public void onServerError() {
                settingsView.hideLoading();
                settingsView.showServerError();
            }

            @Override
            public void onValidationError(String errorMessage) {
                settingsView.hideLoading();
            }

            @Override
            public void onSuspendedUserError(String errorMessage) {
                settingsView.hideLoading();
                settingsView.showSuspededUserError(errorMessage);
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

    public interface SettingsView {
        void showLoading();
        void hideLoading();
        void showInvalideTokenError();
        void showNetworkError();
        void showServerError();
        void showSuspededUserError(String errorMessage);
        void setUserData(User user);
        void showSuccess();
    }
}
