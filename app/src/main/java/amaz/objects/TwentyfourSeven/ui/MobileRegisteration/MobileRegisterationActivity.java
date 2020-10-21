package amaz.objects.TwentyfourSeven.ui.MobileRegisteration;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;
import com.rey.material.widget.ProgressView;

import amaz.objects.TwentyfourSeven.BaseActivity;
import amaz.objects.TwentyfourSeven.MApplication;
import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.listeners.OnLocationChangeListener;
import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import amaz.objects.TwentyfourSeven.presenter.PresenterFactory;
import amaz.objects.TwentyfourSeven.ui.MobileVerification.MobileVerificationActivity;
import amaz.objects.TwentyfourSeven.ui.RegisterOrLogin.RegisterOrLoginActivity;
import amaz.objects.TwentyfourSeven.utilities.Constants;
import amaz.objects.TwentyfourSeven.utilities.Fonts;
import amaz.objects.TwentyfourSeven.utilities.GPSTracker;
import amaz.objects.TwentyfourSeven.utilities.LanguageUtilities;
import amaz.objects.TwentyfourSeven.utilities.LocalSettings;

public class MobileRegisterationActivity extends BaseActivity implements MobileRegisterationPresenter.MobileRegisterationView,
        View.OnClickListener, OnLocationChangeListener {

    private ImageView backIv;
    private CountryCodePicker pickCountryCcp;
    private TextView registerMobileTv, countryCodeTv;
    private EditText phoneNumberEt;
    private Button verifyBtn;
    private ProgressView loaderPv,locationLoaderPv;

    private GPSTracker tracker;
    private String city;

    private LocalSettings localSettings;
    private Fonts fonts;

    private MobileRegisterationPresenter mobileRegisterationPresenter;
    private String googleToken;
    private String facebookToken;


    @Override
    public void onPresenterAvailable(BasePresenter presenter) {
        mobileRegisterationPresenter = (MobileRegisterationPresenter) presenter;
        mobileRegisterationPresenter.setView(this);
    }

    @Override
    public PresenterFactory.PresenterType getPresenterType() {
        return PresenterFactory.PresenterType.MobileRegisteration;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.background_blue));
        localSettings = new LocalSettings(this);
        setLanguage();
        setContentView(R.layout.activity_mobile_registeration);
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
    }

    private void initialization(){
        backIv = findViewById(R.id.iv_back);
        if(localSettings.getLocale().equals(Constants.ARABIC)){
            backIv.setImageResource(R.drawable.back_ar_ic);
        }
        else {
            backIv.setImageResource(R.drawable.back_ic);
        }
        backIv.setOnClickListener(this);
        registerMobileTv = findViewById(R.id.tv_register_mobile);
        if (localSettings.getToken() != null){
            registerMobileTv.setText(R.string.change_phone);
        }
        else {
            registerMobileTv.setText(R.string.register_mobile);
        }
        pickCountryCcp = findViewById(R.id.ccp_pick_country);
        pickCountryCcp.changeDefaultLanguage(CountryCodePicker.Language.ENGLISH);
        countryCodeTv = findViewById(R.id.tv_country_code);
        phoneNumberEt = findViewById(R.id.et_phone_number);
        verifyBtn = findViewById(R.id.btn_verify);
        verifyBtn.setOnClickListener(this);
        loaderPv = findViewById(R.id.pv_load);
        locationLoaderPv = findViewById(R.id.pv_location_loader);
        countryCodeTv.setText(pickCountryCcp.getFullNumberWithPlus());
        pickCountryCcp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countryCodeTv.setText(pickCountryCcp.getFullNumberWithPlus());
            }
        });
        locationLoaderPv.setVisibility(View.VISIBLE);
        tracker = new GPSTracker(this,this, false);
        tracker.getLocation();
    }

    private void setFonts(){
        fonts = MApplication.getInstance().getFonts();
        registerMobileTv.setTypeface(fonts.customFontBD());
        countryCodeTv.setTypeface(fonts.customFont());
        phoneNumberEt.setTypeface(fonts.customFont());
        verifyBtn.setTypeface(fonts.customFontBD());
    }

    private void switchToMobileVerification(){
        Intent mobileVerificationIntent = new Intent(this,MobileVerificationActivity.class);
        mobileVerificationIntent.putExtra(Constants.GOOGLE_TOKEN,googleToken);
        mobileVerificationIntent.putExtra(Constants.FACEBOOK_TOKEN,facebookToken);
        mobileVerificationIntent.putExtra(Constants.PHONE_WITH_CODE,countryCodeTv.getText().toString()+phoneNumberEt.getText().toString());
        mobileVerificationIntent.putExtra(Constants.CITY,this.city);
        startActivity(mobileVerificationIntent);
    }

    private void switchToRegisterationOrLogin(){
        Intent registerationOrLoginIntent = new Intent(this,RegisterOrLoginActivity.class);
        registerationOrLoginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(registerationOrLoginIntent);
    }

    @Override
    public void onClick(View view) {
        View focusedView = this.getCurrentFocus();
        if (focusedView != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
        }
        switch (view.getId()){
            case R.id.btn_verify:
                if (localSettings.getToken() != null){
                    mobileRegisterationPresenter.changeUserPhoneNumber(localSettings.getToken(), localSettings.getLocale(), countryCodeTv.getText().toString(), phoneNumberEt.getText().toString());
                }
                else {
                    mobileRegisterationPresenter.requestVerificationCode(localSettings.getLocale(),countryCodeTv.getText().toString(), phoneNumberEt.getText().toString());
                }
                break;

            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void setAddressData(String countryNameCode, String city, double latitude, double longitude) {
        locationLoaderPv.setVisibility(View.GONE);
        this.city = city;
        pickCountryCcp.setCountryForNameCode(countryNameCode);
    }

    @Override
    public void showActivateGPS() {

    }

    @Override
    public void openLocationSettings() {
        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), Constants.LOCATION_SETTINGS_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.LOCATION_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationLoaderPv.setVisibility(View.VISIBLE);
                tracker.getLocation();
            }
            else {
                locationLoaderPv.setVisibility(View.GONE);
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.LOCATION_SETTINGS_REQUEST) {
            locationLoaderPv.setVisibility(View.VISIBLE);
            tracker.getLocation();
        }
        else if (requestCode == Constants.CHECK_LOCATION_SETTINGS_REQUEST){
            if (resultCode == Activity.RESULT_OK){
                locationLoaderPv.setVisibility(View.VISIBLE);
                tracker.getLocation();
            }
            else {
                locationLoaderPv.setVisibility(View.GONE);
            }
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
    public void showEmptyPhoneError() {
        if (!this.isDestroyed() && !this.isFinishing()){
            Toast.makeText(this,R.string.empty_phone_error,Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void showValidationError(String errorMessage) {
        if (!this.isDestroyed() && !this.isFinishing()){
            if (errorMessage == null){
                Toast.makeText(this,R.string.invalid_phone_number,Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(this,errorMessage,Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    public void navigateToMobileVerification() {
        if (!this.isDestroyed() && !this.isFinishing()){
            switchToMobileVerification();
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
}
