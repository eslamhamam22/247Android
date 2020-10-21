package amaz.objects.TwentyfourSeven.utilities;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.data.models.responses.SocialLoginResponse;
import amaz.objects.TwentyfourSeven.data.repositories.UserRepository;
import amaz.objects.TwentyfourSeven.listeners.OnRefeshTokenResponse;
import amaz.objects.TwentyfourSeven.listeners.OnResponseListener;
import amaz.objects.TwentyfourSeven.ui.MobileVerification.MobileVerificationPresenter;
import amaz.objects.TwentyfourSeven.ui.MyAccount.MainActivity;
import amaz.objects.TwentyfourSeven.ui.RegisterOrLogin.RegisterOrLoginActivity;
import retrofit2.Response;

public class TokenUtilities {

    public static void refreshToken(final Context context, UserRepository repository, final String token, String locale, String refreshToken, final OnRefeshTokenResponse onRefeshTokenResponse){
        final LocalSettings localSettings = new LocalSettings(context);
        repository.refreshToken(token, locale, refreshToken, new OnResponseListener() {


            @Override
            public void onSuccess(Response response) {
                //onRefeshTokenResponse.hideTokenLoader();
                String newToken = ((SocialLoginResponse) response.body()).getData().getToken();
                String newRefreshToken = ((SocialLoginResponse) response.body()).getData().getRefreshToken();
                String newFirebaseToken = ((SocialLoginResponse) response.body()).getData().getFirebaseToken();
                localSettings.setToken(newToken);
                localSettings.setRefreshToken(newRefreshToken);
                localSettings.setFirebaseToken(newFirebaseToken);
                //switchToStores(context);
                if (localSettings.getFirebaseToken() != null && !localSettings.getFirebaseToken().isEmpty()){
                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.signInWithCustomToken(newFirebaseToken).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                onRefeshTokenResponse.hideTokenLoader();
                                switchToStores(context);
                            }
                            else {
                                onRefeshTokenResponse.hideTokenLoader();
                            }
                        }
                    });
                }
                else {
                    onRefeshTokenResponse.hideTokenLoader();
                    switchToStores(context);
                }


            }

            @Override
            public void onFailure() {
                onRefeshTokenResponse.hideTokenLoader();
                Toast.makeText(context, R.string.connection_error,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAuthError() {
                onRefeshTokenResponse.hideTokenLoader();
                switchToRegisterationOrLogin(context);
            }

            @Override
            public void onInvalidTokenError() {
                onRefeshTokenResponse.hideTokenLoader();
            }

            @Override
            public void onServerError() {
                onRefeshTokenResponse.hideTokenLoader();
                Toast.makeText(context, R.string.server_error,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onValidationError(String errorMessage) {
                onRefeshTokenResponse.hideTokenLoader();
            }

            @Override
            public void onSuspendedUserError(String errorMessage) {
                onRefeshTokenResponse.hideTokenLoader();
                Toast.makeText(context, errorMessage,Toast.LENGTH_LONG).show();
                switchToRegisterationOrLogin(context);
            }
        });
    }

    private static void switchToStores(Context context){
        Intent mainIntent = new Intent(context,MainActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(mainIntent);
    }

    private static void switchToRegisterationOrLogin(Context context){
        LocalSettings localSettings = new LocalSettings(context);
        localSettings.removeToken();
        localSettings.removeRefreshToken();
        localSettings.removeUser();
        Intent registerationOrLoginIntent = new Intent(context,RegisterOrLoginActivity.class);
        registerationOrLoginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(registerationOrLoginIntent);
    }

}
