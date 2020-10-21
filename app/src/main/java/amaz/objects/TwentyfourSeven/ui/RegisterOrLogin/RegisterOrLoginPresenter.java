package amaz.objects.TwentyfourSeven.ui.RegisterOrLogin;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.lang.ref.WeakReference;

import amaz.objects.TwentyfourSeven.data.models.SocialLoginData;
import amaz.objects.TwentyfourSeven.data.models.responses.GoogleAccessTokenResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.SocialLoginResponse;
import amaz.objects.TwentyfourSeven.data.repositories.GoogleRepository;
import amaz.objects.TwentyfourSeven.data.repositories.UserRepository;
import amaz.objects.TwentyfourSeven.listeners.OnResponseListener;
import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import amaz.objects.TwentyfourSeven.utilities.LocalSettings;
import retrofit2.Response;

public class RegisterOrLoginPresenter extends BasePresenter {

    private UserRepository userRepository;
    private GoogleRepository googleRepository;
    private WeakReference<RegisterOrLoginView> view = new WeakReference<>(null);
    private LocalSettings localSettings;

    public RegisterOrLoginPresenter(UserRepository userRepository,GoogleRepository googleRepository){
        this.userRepository = userRepository;
        this.googleRepository = googleRepository;
    }

    public void setView(RegisterOrLoginView view, Context context){
        this.view = new WeakReference<>(view);
        localSettings = new LocalSettings(context);
    }

    public void socialLogin(String locale, final String facebookToken, final String googleToken){

        final RegisterOrLoginView registerationOrLoginView = view.get();
        registerationOrLoginView.showLoading();
        userRepository.socialLogin(locale, facebookToken, googleToken, new OnResponseListener() {
            @Override
            public void onSuccess(Response response) {
                SocialLoginData data = ((SocialLoginResponse)response.body()).getData();
                if (data.isRegisteredBefore()){
                    //registerationOrLoginView.setUserData(data);
                    registerOneSignalToken(data);

                }
                else {
                    registerationOrLoginView.hideLoading();
                    registerationOrLoginView.navigateToMobileRegisteration(facebookToken, googleToken);
                }
            }

            @Override
            public void onFailure() {
                registerationOrLoginView.hideLoading();
                registerationOrLoginView.showNetworkError();
            }

            @Override
            public void onAuthError() {
                registerationOrLoginView.hideLoading();
            }

            @Override
            public void onInvalidTokenError() {
                registerationOrLoginView.hideLoading();
            }

            @Override
            public void onServerError() {
                registerationOrLoginView.hideLoading();
                registerationOrLoginView.showServerError();
            }

            @Override
            public void onValidationError(String errorMessage) {
                registerationOrLoginView.hideLoading();
            }

            @Override
            public void onSuspendedUserError(String errorMessage) {
                registerationOrLoginView.hideLoading();
                registerationOrLoginView.showSuspededUserError(errorMessage);
            }
        });
    }

    public void getGoogleAccessToken(String clientId, String clientSecret, String authCode){
        final RegisterOrLoginView registerationOrLoginView = view.get();
        registerationOrLoginView.showLoading();
        googleRepository.getGoogleAccessToken(clientId, clientSecret, authCode, new OnResponseListener() {
            @Override
            public void onSuccess(Response response) {
                //registerationOrLoginView.hideLoading();
                GoogleAccessTokenResponse googleAccessTokenResponse = (GoogleAccessTokenResponse)response.body();
                registerationOrLoginView.setGoogleAccessToken(googleAccessTokenResponse.getAccessToken());
            }

            @Override
            public void onFailure() {
                registerationOrLoginView.hideLoading();
                registerationOrLoginView.showNetworkError();
            }

            @Override
            public void onAuthError() {
                registerationOrLoginView.hideLoading();
            }

            @Override
            public void onInvalidTokenError() {
                registerationOrLoginView.hideLoading();
            }

            @Override
            public void onServerError() {
                registerationOrLoginView.hideLoading();
                registerationOrLoginView.showServerError();
            }

            @Override
            public void onValidationError(String errorMessage) {
                registerationOrLoginView.hideLoading();
            }

            @Override
            public void onSuspendedUserError(String errorMessage) {
                registerationOrLoginView.hideLoading();
                registerationOrLoginView.showSuspededUserError(errorMessage);
            }
        });
    }

    public void registerOneSignalToken(final SocialLoginData data){
        final RegisterOrLoginView registerationOrLoginView = view.get();
        String token = data.getToken();
        userRepository.registerDeviceToken(token, localSettings.getLocale(), localSettings.getRegisteredToken(), new OnResponseListener() {

            @Override
            public void onSuccess(Response response) {
                //registerationOrLoginView.hideLoading();
                registerationOrLoginView.setUserData(data);
                //registerationOrLoginView.navigateToStores();
                if (localSettings.getFirebaseToken() != null && !localSettings.getFirebaseToken().isEmpty()){
                    firebaseSignIn();
                }
                else {
                    registerationOrLoginView.hideLoading();
                    registerationOrLoginView.navigateToStores();
                }

            }

            @Override
            public void onFailure() {
                registerationOrLoginView.hideLoading();
                registerationOrLoginView.showNetworkError();
            }

            @Override
            public void onAuthError() {
                registerationOrLoginView.hideLoading();
            }

            @Override
            public void onInvalidTokenError() {
                registerationOrLoginView.hideLoading();
            }

            @Override
            public void onServerError() {
                registerationOrLoginView.hideLoading();
                registerationOrLoginView.showServerError();
            }

            @Override
            public void onValidationError(String errorMessage) {
                registerationOrLoginView.hideLoading();
                // registerationOrLoginView.showInvalidCodeError();
            }

            @Override
            public void onSuspendedUserError(String errorMessage) {
                registerationOrLoginView.hideLoading();
                registerationOrLoginView.showSuspededUserError(errorMessage);
            }
        });
    }

    public void deregisterDevice(String locale, String playerId){
        userRepository.deregisterDevice(locale, playerId, new OnResponseListener() {
            @Override
            public void onSuccess(Response response) {
                Log.e("onSuccess","onSuccess");
            }

            @Override
            public void onFailure() {
                Log.e("onFailure","onFailure");
            }

            @Override
            public void onAuthError() {

            }

            @Override
            public void onInvalidTokenError() {

            }

            @Override
            public void onServerError() {
                Log.e("onServerError","onServerError");
            }

            @Override
            public void onValidationError(String errorMessage) {

            }

            @Override
            public void onSuspendedUserError(String errorMessage) {

            }
        });
    }

    public void firebaseSignIn(){
        final RegisterOrLoginView registerationOrLoginView = view.get();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCustomToken(localSettings.getFirebaseToken()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    registerationOrLoginView.hideLoading();
                    registerationOrLoginView.navigateToStores();
                }
                else {
                    registerationOrLoginView.hideLoading();
                }
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

    public interface RegisterOrLoginView {
        void showLoading();
        void hideLoading();
        void navigateToMobileRegisteration(String facebookToken, String googleToken);
        void navigateToStores();
        void setGoogleAccessToken(String googleAccessToken);
        void setUserData(SocialLoginData data);
        void showNetworkError();
        void showServerError();
        void showSuspededUserError(String errorMessage);

    }
}
