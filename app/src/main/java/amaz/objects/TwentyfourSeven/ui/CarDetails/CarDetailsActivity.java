package amaz.objects.TwentyfourSeven.ui.CarDetails;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rey.material.widget.ProgressView;
import com.squareup.picasso.Picasso;

import amaz.objects.TwentyfourSeven.BaseActivity;
import amaz.objects.TwentyfourSeven.MApplication;
import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.data.models.CarDetailsData;
import amaz.objects.TwentyfourSeven.injection.Injection;
import amaz.objects.TwentyfourSeven.listeners.OnRefeshTokenResponse;
import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import amaz.objects.TwentyfourSeven.presenter.PresenterFactory;
import amaz.objects.TwentyfourSeven.ui.RegisterOrLogin.RegisterOrLoginActivity;
import amaz.objects.TwentyfourSeven.utilities.Constants;
import amaz.objects.TwentyfourSeven.utilities.Fonts;
import amaz.objects.TwentyfourSeven.utilities.LanguageUtilities;
import amaz.objects.TwentyfourSeven.utilities.LocalSettings;
import amaz.objects.TwentyfourSeven.utilities.TokenUtilities;

public class CarDetailsActivity extends BaseActivity implements CarDetailsPresenter.CarDetailsView, View.OnClickListener, OnRefeshTokenResponse {

    private LinearLayout mainContentLl;
    private TextView titleTv, carBrandTv, licenseTv, idTv, carFrontTv, carBackTv;
    private ImageView licenseIv, idIv, carFrontIv, carBackIv, backIv;
    private ProgressView loaderPv;

    private PopupWindow previewPopupWindow;
    private ImageView popupCloseIv;
    private ImageView popupPreviewIv;

    private TextView noConnectionTitleTv, noConnectionMessageTv;
    private LinearLayout noConnectionLl;
    private Button reloadBtn;

    private LocalSettings localSettings;
    private Fonts fonts;

    private CarDetailsPresenter carDetailsPresenter;
    private CarDetailsData data;

    @Override
    public void onPresenterAvailable(BasePresenter presenter) {
        carDetailsPresenter = (CarDetailsPresenter) presenter;
        carDetailsPresenter.setView(this);
        if (data == null) {
            carDetailsPresenter.getCarDetails(localSettings.getToken(), localSettings.getLocale());
        }

    }

    @Override
    public PresenterFactory.PresenterType getPresenterType() {
        return PresenterFactory.PresenterType.CarDetails;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localSettings = new LocalSettings(this);
        setLanguage();
        setContentView(R.layout.activity_car_details);
        initialization();
        initializePreviewPopup();
        setFonts();
    }

    private void initialization() {
        titleTv = findViewById(R.id.tv_car_details_title);
        backIv = findViewById(R.id.iv_back);
        if (localSettings.getLocale().equals(Constants.ARABIC)) {
            backIv.setImageResource(R.drawable.back_ar_ic);
        } else {
            backIv.setImageResource(R.drawable.back_ic);
        }
        backIv.setOnClickListener(this);
        mainContentLl = findViewById(R.id.ll_main_content);
        mainContentLl.setVisibility(View.GONE);
        carBrandTv = findViewById(R.id.tv_car_brand);
        licenseIv = findViewById(R.id.iv_license);
        licenseIv.setOnClickListener(this);
        licenseTv = findViewById(R.id.tv_license);
        idIv = findViewById(R.id.iv_id);
        idIv.setOnClickListener(this);
        idTv = findViewById(R.id.tv_id);
        carFrontIv = findViewById(R.id.iv_car_front);
        carFrontIv.setOnClickListener(this);
        carFrontTv = findViewById(R.id.tv_car_front);
        carBackIv = findViewById(R.id.iv_car_back);
        carBackIv.setOnClickListener(this);
        carBackTv = findViewById(R.id.tv_car_back);
        loaderPv = findViewById(R.id.pv_load);

        noConnectionLl = findViewById(R.id.ll_no_connection);
        noConnectionTitleTv = findViewById(R.id.tv_no_connection_title);
        noConnectionMessageTv = findViewById(R.id.tv_no_connection_message);
        reloadBtn = findViewById(R.id.btn_reload_page);
        reloadBtn.setOnClickListener(this);
    }

    private void initializePreviewPopup() {
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_delegate_images_preview, null, false);
        previewPopupWindow = new PopupWindow(v, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        popupPreviewIv = v.findViewById(R.id.iv_preview);
        popupCloseIv = v.findViewById(R.id.iv_close);
        popupCloseIv.setOnClickListener(this);
    }

    private void setFonts() {
        fonts = MApplication.getInstance().getFonts();
        titleTv.setTypeface(fonts.customFontBD());
        carBrandTv.setTypeface(fonts.customFont());
        licenseTv.setTypeface(fonts.customFont());
        idTv.setTypeface(fonts.customFont());
        carFrontTv.setTypeface(fonts.customFont());
        carBackTv.setTypeface(fonts.customFont());

        noConnectionTitleTv.setTypeface(fonts.customFontBD());
        noConnectionMessageTv.setTypeface(fonts.customFont());
        reloadBtn.setTypeface(fonts.customFontBD());
    }

    private void setLanguage() {
        if (localSettings.getLocale().equals(Constants.ARABIC)) {
            LanguageUtilities.switchToArabicLocale(this);
        }
    }

    private void switchToRegisterationOrLogin() {
        Intent registerationOrLoginIntent = new Intent(this, RegisterOrLoginActivity.class);
        registerationOrLoginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(registerationOrLoginIntent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.iv_back:
                finish();
                break;

            case R.id.iv_close:
                previewPopupWindow.dismiss();
                break;

            case R.id.iv_license:
                if (data != null && data.getDelegateLicense() != null) {
                    Picasso.with(this).load(data.getDelegateLicense().getBig()).placeholder(R.drawable.upload_default).into(popupPreviewIv);
                    previewPopupWindow.showAtLocation(mainContentLl, Gravity.CENTER, 0, 0);
                }
                break;

            case R.id.iv_id:
                if (data != null && data.getDelegateId() != null) {
                    Picasso.with(this).load(data.getDelegateId().getBig()).placeholder(R.drawable.upload_default).into(popupPreviewIv);
                    previewPopupWindow.showAtLocation(mainContentLl, Gravity.CENTER, 0, 0);
                }
                break;

            case R.id.iv_car_front:
                if (data != null && data.getCarFront() != null) {
                    Picasso.with(this).load(data.getCarFront().getBig()).placeholder(R.drawable.upload_default).into(popupPreviewIv);
                    previewPopupWindow.showAtLocation(mainContentLl, Gravity.CENTER, 0, 0);
                }
                break;

            case R.id.iv_car_back:
                if (data != null && data.getCarBack() != null) {
                    Picasso.with(this).load(data.getCarBack().getBig()).placeholder(R.drawable.upload_default).into(popupPreviewIv);
                    previewPopupWindow.showAtLocation(mainContentLl, Gravity.CENTER, 0, 0);
                }
                break;

            case R.id.btn_reload_page:
                carDetailsPresenter.getCarDetails(localSettings.getToken(), localSettings.getLocale());
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (previewPopupWindow.isShowing()) {
            previewPopupWindow.dismiss();
        } else {
            super.onBackPressed();
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
    public void showInvalideTokenError() {
        Log.e("authError", "authentication error");
        TokenUtilities.refreshToken(this,
                Injection.provideUserRepository(),
                localSettings.getToken(),
                localSettings.getLocale(),
                localSettings.getRefreshToken(),
                this);
    }

    @Override
    public void showNetworkError() {
        noConnectionLl.setVisibility(View.VISIBLE);
    }

    @Override
    public void showServerError() {
        Toast.makeText(this, R.string.server_error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showSuspededUserError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        localSettings.removeToken();
        localSettings.removeRefreshToken();
        localSettings.removeUser();
        switchToRegisterationOrLogin();

    }

    @Override
    public void showNoData() {
        Toast.makeText(this, R.string.no_data_error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showCarDetailsData(CarDetailsData data) {
        if (noConnectionLl.getVisibility() == View.VISIBLE) {
            noConnectionLl.setVisibility(View.GONE);
        }
        this.data = data;
        mainContentLl.setVisibility(View.VISIBLE);
        if (data != null) {
            Picasso.with(this).load(data.getDelegateLicense().getSmall()).placeholder(R.drawable.grayscale).into(licenseIv);
            Picasso.with(this).load(data.getDelegateId().getSmall()).placeholder(R.drawable.grayscale).into(idIv);
            Picasso.with(this).load(data.getCarFront().getSmall()).placeholder(R.drawable.grayscale).into(carFrontIv);
            Picasso.with(this).load(data.getCarBack().getSmall()).placeholder(R.drawable.grayscale).into(carBackIv);
            carBrandTv.setText(data.getCarDetails());
        }
    }


    @Override
    public void hideTokenLoader() {
        hideLoading();
    }
}
