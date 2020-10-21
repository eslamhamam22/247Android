package amaz.objects.TwentyfourSeven.ui.RegisterOrLogin;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.rey.material.widget.ProgressView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import amaz.objects.TwentyfourSeven.BaseActivity;
import amaz.objects.TwentyfourSeven.MApplication;
import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.api.APIURLs;
import amaz.objects.TwentyfourSeven.data.models.SocialLoginData;
import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import amaz.objects.TwentyfourSeven.presenter.PresenterFactory;
import amaz.objects.TwentyfourSeven.ui.MobileRegisteration.MobileRegisterationActivity;
import amaz.objects.TwentyfourSeven.ui.MyAccount.MainActivity;
import amaz.objects.TwentyfourSeven.ui.Pages.PagesActivity;
import amaz.objects.TwentyfourSeven.utilities.Constants;
import amaz.objects.TwentyfourSeven.utilities.Fonts;
import amaz.objects.TwentyfourSeven.utilities.LanguageUtilities;
import amaz.objects.TwentyfourSeven.utilities.LocalSettings;

public class RegisterOrLoginActivity extends BaseActivity implements RegisterOrLoginPresenter.RegisterOrLoginView,
        View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private TextView registerOrLoginTv, agreeTv, termsAndConditionsTv, skipLoginTv;
    private Button mobileLoginBtn, facebookLoginBtn, googleLoginBtn;
    private ProgressView loaderPv;
    private LocalSettings localSettings;
    private Fonts fonts;

    private String GOOGLE_CLIENT_ID;
    private String GOOGLE_CLIENT_SECRET;
    private GoogleSignInOptions googleSignInOptions;
    private GoogleApiClient googleApiClient;
    private String googleServerAuthCode;

    private CallbackManager callbackManager;

    private RegisterOrLoginPresenter registerOrLoginPresenter;

    @Override
    public void onPresenterAvailable(BasePresenter presenter) {
        registerOrLoginPresenter = (RegisterOrLoginPresenter) presenter;
        registerOrLoginPresenter.setView(this, this);
        registerOrLoginPresenter.deregisterDevice(localSettings.getLocale(), localSettings.getRegisteredToken());
    }

    @Override
    public PresenterFactory.PresenterType getPresenterType() {
        return PresenterFactory.PresenterType.RegiterOrLogin;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GOOGLE_CLIENT_ID = getString(R.string.client_id);
        GOOGLE_CLIENT_SECRET = getString(R.string.secret_id);
        localSettings = new LocalSettings(this);
        setLanguage();
        setContentView(R.layout.activity_register_or_login);
        firebaseLogOut();
        initialization();
        googlePlusLoginInitialization();
        facebookLoginInitialization();
        setFonts();
    }

    private void setLanguage() {
        if (localSettings.getLocale().equals(Constants.ARABIC)) {
            LanguageUtilities.switchToArabicLocale(this);
        }
    }

    private void initialization() {
        registerOrLoginTv = findViewById(R.id.tv_register_or_login);
        mobileLoginBtn = findViewById(R.id.btn_login_mobile);
        mobileLoginBtn.setOnClickListener(this);
        facebookLoginBtn = findViewById(R.id.btn_login_facebook);
        facebookLoginBtn.setOnClickListener(this);
        googleLoginBtn = findViewById(R.id.btn_login_google);
        googleLoginBtn.setOnClickListener(this);
        agreeTv = findViewById(R.id.tv_agree);
        termsAndConditionsTv = findViewById(R.id.tv_terms_conditions);
        termsAndConditionsTv.setOnClickListener(this);
        skipLoginTv = findViewById(R.id.tv_skip_login);
        skipLoginTv.setOnClickListener(this);
        loaderPv = findViewById(R.id.pv_load);
    }

    private void googlePlusLoginInitialization() {
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //.requestScopes(new Scope(Scopes.DRIVE_APPFOLDER))
                .requestServerAuthCode(GOOGLE_CLIENT_ID).build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();
    }

    private void facebookLoginInitialization() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                Log.e("token", accessToken.getToken());
                registerOrLoginPresenter.socialLogin(localSettings.getLocale(), accessToken.getToken(), null);
            }

            @Override
            public void onCancel() {
                if (AccessToken.getCurrentAccessToken() != null) {
                    LoginManager.getInstance().logOut();
                    LoginManager.getInstance().logInWithReadPermissions(RegisterOrLoginActivity.this, null);
                }
            }

            @Override
            public void onError(FacebookException error) {
                if (error instanceof FacebookAuthorizationException) {
                    if (AccessToken.getCurrentAccessToken() != null) {
                        LoginManager.getInstance().logOut();
                        LoginManager.getInstance().logInWithReadPermissions(RegisterOrLoginActivity.this, null);
                    }
                } else {
                    Toast.makeText(RegisterOrLoginActivity.this, R.string.connection_error, Toast.LENGTH_LONG).show();
                }
            }
        });

        if (AccessToken.getCurrentAccessToken() != null) {
            LoginManager.getInstance().logOut();
        }

        try {
            PackageInfo info = getPackageManager().getPackageInfo("amaz.objects.TwentyfourSeven", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

    }

    private void firebaseLogOut() {
        FirebaseAuth.getInstance().signOut();
    }

    private void setFonts() {
        fonts = MApplication.getInstance().getFonts();
        registerOrLoginTv.setTypeface(fonts.customFontBD());
        mobileLoginBtn.setTypeface(fonts.customFontBD());
        facebookLoginBtn.setTypeface(fonts.customFontBD());
        googleLoginBtn.setTypeface(fonts.customFontBD());
        agreeTv.setTypeface(fonts.customFont());
        termsAndConditionsTv.setTypeface(fonts.customFont());
        skipLoginTv.setTypeface(fonts.customFont());
    }

    private void switchToMobileRegisteration(String facebookToken, String googleToken) {
        Intent mobileRegisterationIntent = new Intent(this, MobileRegisterationActivity.class);
        if (facebookToken != null) {
            mobileRegisterationIntent.putExtra(Constants.FACEBOOK_TOKEN, facebookToken);
        }
        if (googleToken != null) {
            mobileRegisterationIntent.putExtra(Constants.GOOGLE_TOKEN, googleToken);
        }

        startActivity(mobileRegisterationIntent);
    }

    private void switchToTermsAndConditions() {
        Intent termsAndConditionsIntent = new Intent(this, PagesActivity.class);
        termsAndConditionsIntent.putExtra(Constants.PAGE, Constants.TERMS_AND_CONDITIONS);
        startActivity(termsAndConditionsIntent);
    }

    private void switchToRegisterationOrLogin() {
        Intent registerationOrLoginIntent = new Intent(this, RegisterOrLoginActivity.class);
        registerationOrLoginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(registerationOrLoginIntent);
    }

    private void switchToStores(boolean fromSkip) {
        Intent storesIntent = new Intent(this, MainActivity.class);
        if (!fromSkip) {
            storesIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        startActivity(storesIntent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login_mobile:
                switchToMobileRegisteration(null, null);
                break;
            case R.id.btn_login_facebook:
                LoginManager.getInstance().logInWithReadPermissions(RegisterOrLoginActivity.this, null);
                break;
            case R.id.btn_login_google:
                Intent sighInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(sighInIntent, Constants.GOOGLE_REQUEST);
                break;
            case R.id.tv_terms_conditions:
                switchToTermsAndConditions();
                break;
            case R.id.tv_skip_login:
                switchToStores(true);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.GOOGLE_REQUEST) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInWithGooglePlusResult(result);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        hideLoading();
    }

    private void handleSignInWithGooglePlusResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            this.googleServerAuthCode = account.getServerAuthCode();
            registerOrLoginPresenter.getGoogleAccessToken(GOOGLE_CLIENT_ID, GOOGLE_CLIENT_SECRET, this.googleServerAuthCode);
        } else {
            Toast.makeText(this, R.string.connection_error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void showLoading() {
        loaderPv.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public void hideLoading() {
        loaderPv.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public void navigateToMobileRegisteration(String facebookToken, String googleToken) {
        switchToMobileRegisteration(facebookToken, googleToken);
    }


    @Override
    public void navigateToStores() {
        switchToStores(false);
    }

    @Override
    public void setGoogleAccessToken(String googleAccessToken) {
        Log.e("omnia", googleAccessToken);
        registerOrLoginPresenter.socialLogin(localSettings.getLocale(), null, googleAccessToken);
    }

    @Override
    public void setUserData(SocialLoginData data) {
        localSettings.setToken(data.getToken());
        localSettings.setRefreshToken(data.getRefreshToken());
        localSettings.setFirebaseToken(data.getFirebaseToken());
        localSettings.setUser(data.getUser());
    }

    @Override
    public void showNetworkError() {
        Toast.makeText(this, R.string.connection_error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showServerError() {
        Toast.makeText(this, R.string.server_error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showSuspededUserError(String errorMessage) {
        Log.e("unauthorized", errorMessage);
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }
}
