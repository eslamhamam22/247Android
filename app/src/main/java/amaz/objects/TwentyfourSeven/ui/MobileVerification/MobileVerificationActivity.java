package amaz.objects.TwentyfourSeven.ui.MobileVerification;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rey.material.widget.ProgressView;

import amaz.objects.TwentyfourSeven.BaseActivity;
import amaz.objects.TwentyfourSeven.MApplication;
import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.data.models.SocialLoginData;
import amaz.objects.TwentyfourSeven.data.models.User;
import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import amaz.objects.TwentyfourSeven.presenter.PresenterFactory;
import amaz.objects.TwentyfourSeven.ui.AccountDetails.AccountDetailsActivity;
import amaz.objects.TwentyfourSeven.ui.MyAccount.MainActivity;
import amaz.objects.TwentyfourSeven.ui.RegisterOrLogin.RegisterOrLoginActivity;
import amaz.objects.TwentyfourSeven.utilities.Constants;
import amaz.objects.TwentyfourSeven.utilities.Fonts;
import amaz.objects.TwentyfourSeven.utilities.LanguageUtilities;
import amaz.objects.TwentyfourSeven.utilities.LocalSettings;

public class MobileVerificationActivity extends BaseActivity implements MobileVerificationPresenter.MobileVerificationView, View.OnClickListener {

    private ImageView backIv;
    private TextView verifyMobileTv, fourDigitsTv, resendTv;
    private EditText verificationCodeEt;
    private View digitOneView,digitTwoView,digitThreeView,digitFourView;
    private Button submitBtn;
    private ProgressView loaderPv;

    private LocalSettings localSettings;
    private Fonts fonts;

    private MobileVerificationPresenter verificationPresenter;
    private String googleToken;
    private String facebookToken;
    private String phoneWithCode;
    private String city;
    private boolean fromUpdate = false;

    @Override
    public void onPresenterAvailable(BasePresenter presenter) {
        verificationPresenter = (MobileVerificationPresenter) presenter;
        verificationPresenter.setView(this,this);
    }

    @Override
    public PresenterFactory.PresenterType getPresenterType() {
        return PresenterFactory.PresenterType.MobileVerification;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localSettings = new LocalSettings(this);
        setLanguage();
        setContentView(R.layout.activity_mobile_verification);
        getDataFromIntent();
        initialization();
        setFonts();
    }

    private void setLanguage() {
        if (localSettings.getLocale().equals(Constants.ARABIC)) {
            LanguageUtilities.switchToArabicLocale(this);
        }
    }

    private void getDataFromIntent(){
        googleToken = getIntent().getStringExtra(Constants.GOOGLE_TOKEN);
        facebookToken = getIntent().getStringExtra(Constants.FACEBOOK_TOKEN);
        phoneWithCode = getIntent().getStringExtra(Constants.PHONE_WITH_CODE);
        city = getIntent().getStringExtra(Constants.CITY);

        Log.e("verification",googleToken+"  "+facebookToken+"  "+phoneWithCode+"  "+city);
    }

    private void initialization(){
        backIv = findViewById(R.id.iv_back);
        if (localSettings.getLocale().equals(Constants.ENGLISH)){
            backIv.setImageResource(R.drawable.back_red_ic);
        }
        else {
            backIv.setImageResource(R.drawable.back_ar_red_ic);
        }
        backIv.setOnClickListener(this);
        verifyMobileTv = findViewById(R.id.tv_verify_mobile);
        fourDigitsTv = findViewById(R.id.tv_four_digits);
        verificationCodeEt = findViewById(R.id.et_verification_code);
        digitOneView = findViewById(R.id.view_digit_one);
        digitTwoView = findViewById(R.id.view_digit_two);
        digitThreeView = findViewById(R.id.view_digit_three);
        digitFourView = findViewById(R.id.view_digit_four);
        submitBtn = findViewById(R.id.btn_submit);
        submitBtn.setOnClickListener(this);
        submitBtn.setEnabled(false);
        resendTv = findViewById(R.id.tv_resend);
        resendTv.setOnClickListener(this);
        loaderPv = findViewById(R.id.pv_load);
        verificationCodeEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().isEmpty()){
                    digitOneView.setBackgroundColor(ContextCompat.getColor(MobileVerificationActivity.this,R.color.line_gray));
                    digitTwoView.setBackgroundColor(ContextCompat.getColor(MobileVerificationActivity.this,R.color.line_gray));
                    digitThreeView.setBackgroundColor(ContextCompat.getColor(MobileVerificationActivity.this,R.color.line_gray));
                    digitFourView.setBackgroundColor(ContextCompat.getColor(MobileVerificationActivity.this,R.color.line_gray));
                    submitBtn.setAlpha((float) 0.5);
                    submitBtn.setEnabled(false);
                }
                else {
                    digitOneView.setBackgroundColor(ContextCompat.getColor(MobileVerificationActivity.this,R.color.hint_blue));
                    digitTwoView.setBackgroundColor(ContextCompat.getColor(MobileVerificationActivity.this,R.color.hint_blue));
                    digitThreeView.setBackgroundColor(ContextCompat.getColor(MobileVerificationActivity.this,R.color.hint_blue));
                    digitFourView.setBackgroundColor(ContextCompat.getColor(MobileVerificationActivity.this,R.color.hint_blue));
                    submitBtn.setAlpha(1);
                    if (charSequence.toString().length() == 4){
                        submitBtn.setEnabled(true);
                        hideKeyBoard();
                        if (localSettings.getToken() != null){
                            fromUpdate = true;
                            verificationPresenter.verifyChangedPhoneNumber(localSettings.getToken(),localSettings.getLocale(),phoneWithCode,verificationCodeEt.getText().toString());
                        }
                        else {
                            verificationPresenter.verifyPhoneNumber(localSettings.getLocale(),phoneWithCode,verificationCodeEt.getText().toString(),facebookToken,googleToken);
                        }
                    }
                    else {
                        submitBtn.setAlpha((float) 0.5);
                        submitBtn.setEnabled(false);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setFonts(){
        fonts = MApplication.getInstance().getFonts();
        verifyMobileTv.setTypeface(fonts.customFontBD());
        fourDigitsTv.setTypeface(fonts.customFont());
        verificationCodeEt.setTypeface(fonts.customFont());
        submitBtn.setTypeface(fonts.customFontBD());
        resendTv.setTypeface(fonts.customFont());
    }

    private void switchToStores(){
        Intent mainIntent = new Intent(this,MainActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainIntent);
    }

    private void switchToAccountDetails(){
        Intent accountDetailsIntent = new Intent(this,AccountDetailsActivity.class);
        accountDetailsIntent.putExtra(Constants.FROM_VERIFICATION,true);
        accountDetailsIntent.putExtra(Constants.FROM_UPDATE,fromUpdate);
        accountDetailsIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(accountDetailsIntent);
    }

    private void switchToRegisterationOrLogin(){
        Intent registerationOrLoginIntent = new Intent(this,RegisterOrLoginActivity.class);
        registerationOrLoginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(registerationOrLoginIntent);
    }

    private void hideKeyBoard(){
        View focusedView = this.getCurrentFocus();
        if (focusedView != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
        }
    }

    @Override
    public void onClick(View view) {
        hideKeyBoard();
        switch (view.getId()){
            case R.id.btn_submit:
                if (localSettings.getToken() != null){
                    fromUpdate = true;
                    verificationPresenter.verifyChangedPhoneNumber(localSettings.getToken(),localSettings.getLocale(),phoneWithCode,verificationCodeEt.getText().toString());
                }
                else {
                    verificationPresenter.verifyPhoneNumber(localSettings.getLocale(),phoneWithCode,verificationCodeEt.getText().toString(),facebookToken,googleToken);
                }
                break;

            case R.id.tv_resend:
                if (localSettings.getToken() != null){
                    fromUpdate = true;
                    verificationPresenter.resendChangedPhoneVerificationCode(localSettings.getToken(),localSettings.getLocale(),phoneWithCode);
                }
                else {
                    verificationPresenter.resendVerificationCode(localSettings.getLocale(),phoneWithCode);
                }
                break;

            case R.id.iv_back:
                finish();
                break;
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
    public void showInvalidCodeError() {
        if (!this.isDestroyed() && !this.isFinishing()){
            Toast.makeText(this,R.string.invalid_verification_code,Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void setUserData(SocialLoginData data) {
        if (!this.isDestroyed() && !this.isFinishing()){
            localSettings.setToken(data.getToken());
            localSettings.setRefreshToken(data.getRefreshToken());
            localSettings.setFirebaseToken(data.getFirebaseToken());
            data.getUser().setCity(this.city);
            localSettings.setUser(data.getUser());
        }

    }

    @Override
    public void changeUserData(User user) {
        if (!this.isDestroyed() && !this.isFinishing()){
            user.setCity(this.city);
            localSettings.setUser(user);
        }

    }

    @Override
    public void navigateToStores() {
        if (!this.isDestroyed() && !this.isFinishing()){
            switchToStores();
        }

    }

    @Override
    public void navigateToAccountDetails() {
        if (!this.isDestroyed() && !this.isFinishing()){
            switchToAccountDetails();
        }

    }

    @Override
    public void showSuccessResend() {
        //Toast.makeText(this,R.string.success_send_code,Toast.LENGTH_LONG).show();

    }

    @Override
    public void showResendError(String resendError) {
        if (!this.isDestroyed() && !this.isFinishing()){
            Toast.makeText(this,resendError,Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void showNetworkError() {
        if (!this.isDestroyed() && !this.isFinishing()){
            Toast.makeText(this,R.string.connection_error,Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void showServerError() {
        if (!this.isDestroyed() && !this.isFinishing()){
            Toast.makeText(this,R.string.server_error,Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void showSuspededUserError(String errorMessage) {
        if (!this.isDestroyed() && !this.isFinishing()){
            Toast.makeText(this,errorMessage,Toast.LENGTH_LONG).show();
            switchToRegisterationOrLogin();
        }

    }

    @Override
    public void refreshToken() {
        if (!this.isDestroyed() && !this.isFinishing()){
            verificationPresenter.refreshToken(localSettings.getToken(),localSettings.getLocale(),localSettings.getRefreshToken());
        }

    }

    @Override
    public void changeUserTokens(String token, String refreshToken) {
        if (!this.isDestroyed() && !this.isFinishing()){
            localSettings.setToken(token);
            localSettings.setRefreshToken(refreshToken);
        }

    }
}
