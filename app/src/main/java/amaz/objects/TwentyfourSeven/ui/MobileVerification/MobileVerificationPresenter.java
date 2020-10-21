package amaz.objects.TwentyfourSeven.ui.MobileVerification;

import android.content.Context;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.lang.ref.WeakReference;

import amaz.objects.TwentyfourSeven.data.models.User;
import amaz.objects.TwentyfourSeven.utilities.LocalSettings;

import amaz.objects.TwentyfourSeven.data.models.SocialLoginData;
import amaz.objects.TwentyfourSeven.data.models.responses.SocialLoginResponse;
import amaz.objects.TwentyfourSeven.data.repositories.UserRepository;
import amaz.objects.TwentyfourSeven.listeners.OnResponseListener;
import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import retrofit2.Response;

public class MobileVerificationPresenter extends BasePresenter {

    private UserRepository repository;
    private WeakReference<MobileVerificationView> view = new WeakReference<>(null);
    private LocalSettings localSettings;

    public MobileVerificationPresenter(UserRepository repository) {
        this.repository = repository;
    }

    public void setView(MobileVerificationView view,Context context) {
        this.view = new WeakReference<>(view);
        localSettings=  new LocalSettings(context);
    }

    public void verifyPhoneNumber(final String locale, String phoneWithCode, String code, String facebookToken, String googleToken) {
        final MobileVerificationView mobileVerificationView = view.get();
        mobileVerificationView.showLoading();
        repository.verifyPhoneNumber(locale, phoneWithCode, code, facebookToken, googleToken, new OnResponseListener() {

            @Override
            public void onSuccess(Response response) {
                //mobileVerificationView.hideLoading();
                SocialLoginData data = ((SocialLoginResponse) response.body()).getData();
                //mobileVerificationView.setUserData(data);
                registerOneSignalToken(data);

            }

            @Override
            public void onFailure() {
                mobileVerificationView.hideLoading();
                mobileVerificationView.showNetworkError();
            }

            @Override
            public void onAuthError() {
                mobileVerificationView.hideLoading();
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
                mobileVerificationView.showInvalidCodeError();
            }

            @Override
            public void onSuspendedUserError(String errorMessage) {
                mobileVerificationView.hideLoading();
                mobileVerificationView.showSuspededUserError(errorMessage);
            }
        });
    }

    public void verifyChangedPhoneNumber(String token, String locale, String phoneWithCode, String code) {
        final MobileVerificationView mobileVerificationView = view.get();
        mobileVerificationView.showLoading();
        repository.changeUserPhoneVerifyCode(token, locale, phoneWithCode, code, new OnResponseListener() {

            @Override
            public void onSuccess(Response response) {
                User data = ((SocialLoginResponse) response.body()).getData().getUser();
                mobileVerificationView.changeUserData(data);
                mobileVerificationView.refreshToken();
            }

            @Override
            public void onFailure() {
                mobileVerificationView.hideLoading();
                mobileVerificationView.showNetworkError();
            }

            @Override
            public void onAuthError() {
                mobileVerificationView.hideLoading();
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
                mobileVerificationView.showInvalidCodeError();
            }

            @Override
            public void onSuspendedUserError(String errorMessage) {
                mobileVerificationView.hideLoading();
                mobileVerificationView.showSuspededUserError(errorMessage);
            }
        });
    }

    public void resendVerificationCode(String locale, String phoneWithCode) {
        final MobileVerificationView mobileVerificationView = view.get();
        mobileVerificationView.showLoading();
        repository.requestVerificationCode(locale, phoneWithCode, new OnResponseListener() {
            @Override
            public void onSuccess(Response response) {
                mobileVerificationView.hideLoading();
                mobileVerificationView.showSuccessResend();

            }

            @Override
            public void onFailure() {
                mobileVerificationView.hideLoading();
                mobileVerificationView.showNetworkError();
            }

            @Override
            public void onAuthError() {
                mobileVerificationView.hideLoading();
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
                mobileVerificationView.showResendError(errorMessage);
            }

            @Override
            public void onSuspendedUserError(String errorMessage) {
                mobileVerificationView.hideLoading();
                mobileVerificationView.showSuspededUserError(errorMessage);
            }
        });
    }

    public void resendChangedPhoneVerificationCode(String token, String locale, String phoneWithCode) {
        final MobileVerificationView mobileVerificationView = view.get();
        mobileVerificationView.showLoading();
        repository.changeUserPhoneRequestCode(token, locale, phoneWithCode, new OnResponseListener() {
            @Override
            public void onSuccess(Response response) {
                mobileVerificationView.hideLoading();
                mobileVerificationView.showSuccessResend();

            }

            @Override
            public void onFailure() {
                mobileVerificationView.hideLoading();
                mobileVerificationView.showNetworkError();
            }

            @Override
            public void onAuthError() {
                mobileVerificationView.hideLoading();
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
                mobileVerificationView.showResendError(errorMessage);
            }

            @Override
            public void onSuspendedUserError(String errorMessage) {
                mobileVerificationView.hideLoading();
                mobileVerificationView.showSuspededUserError(errorMessage);
            }
        });
    }

    public void refreshToken(final String token, String locale, String refreshToken){
        final MobileVerificationView mobileVerificationView = view.get();
        repository.refreshToken(token, locale, refreshToken, new OnResponseListener() {

            @Override
            public void onSuccess(Response response) {
                mobileVerificationView.hideLoading();
                String newToken = ((SocialLoginResponse) response.body()).getData().getToken();
                String newRefreshToken = ((SocialLoginResponse) response.body()).getData().getRefreshToken();
                mobileVerificationView.changeUserTokens(newToken,newRefreshToken);
                mobileVerificationView.navigateToAccountDetails();
            }

            @Override
            public void onFailure() {
                mobileVerificationView.hideLoading();
                mobileVerificationView.showNetworkError();
            }

            @Override
            public void onAuthError() {
                mobileVerificationView.hideLoading();
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
                mobileVerificationView.showInvalidCodeError();
            }

            @Override
            public void onSuspendedUserError(String errorMessage) {
                mobileVerificationView.hideLoading();
                mobileVerificationView.showSuspededUserError(errorMessage);
            }
        });
    }

    public void registerOneSignalToken(final SocialLoginData data){
        final MobileVerificationView mobileVerificationView = view.get();
        repository.registerDeviceToken(data.getToken(), localSettings.getLocale(), localSettings.getRegisteredToken(), new OnResponseListener() {

            @Override
            public void onSuccess(Response response) {
                //mobileVerificationView.hideLoading();
                mobileVerificationView.setUserData(data);
                if (data.isRegisteredBefore()) {
                    //mobileVerificationView.hideLoading();
                    //mobileVerificationView.navigateToStores();
                    if (localSettings.getFirebaseToken() != null && !localSettings.getFirebaseToken().isEmpty()){
                        firebaseSignIn(true);
                    }
                    else {
                        mobileVerificationView.hideLoading();
                        mobileVerificationView.navigateToStores();
                    }

                } else {
                    if (localSettings.getFirebaseToken() != null && !localSettings.getFirebaseToken().isEmpty()){
                        firebaseSignIn(false);
                    }
                    else {
                        mobileVerificationView.hideLoading();
                        mobileVerificationView.navigateToAccountDetails();
                    }
                }
            }

            @Override
            public void onFailure() {
                mobileVerificationView.hideLoading();
                mobileVerificationView.showNetworkError();
            }

            @Override
            public void onAuthError() {

                mobileVerificationView.hideLoading();
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
                mobileVerificationView.showInvalidCodeError();
            }

            @Override
            public void onSuspendedUserError(String errorMessage) {
                mobileVerificationView.hideLoading();
                mobileVerificationView.showSuspededUserError(errorMessage);
            }
        });
    }

    public void firebaseSignIn(final boolean isDataFilled){
        final MobileVerificationView mobileVerificationView = view.get();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCustomToken(localSettings.getFirebaseToken()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mobileVerificationView.hideLoading();
                if (task.isSuccessful()){
                    if(isDataFilled){
                        mobileVerificationView.navigateToStores();
                    }
                    else {
                        mobileVerificationView.navigateToAccountDetails();
                    }
                }
                else {
                    mobileVerificationView.navigateToAccountDetails();
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

    public interface MobileVerificationView {
        void showLoading();

        void hideLoading();

        void showInvalidCodeError();

        void setUserData(SocialLoginData data);

        void changeUserData(User user);

        void navigateToStores();

        void navigateToAccountDetails();

        void showSuccessResend();

        void showResendError(String resendError);

        void showNetworkError();

        void showServerError();

        void showSuspededUserError(String errorMessage);

        void refreshToken();

        void changeUserTokens(String token, String refreshToken);
    }
}
