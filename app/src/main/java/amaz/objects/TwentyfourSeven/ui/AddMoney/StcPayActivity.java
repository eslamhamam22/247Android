package amaz.objects.TwentyfourSeven.ui.AddMoney;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.gson.JsonObject;
import com.rey.material.widget.ProgressView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import amaz.objects.TwentyfourSeven.BaseActivity;
import amaz.objects.TwentyfourSeven.MApplication;
import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.api.APIURLs;
import amaz.objects.TwentyfourSeven.data.models.CardPayRegisterationData;
import amaz.objects.TwentyfourSeven.data.models.DirectPaymentAuthorizeCheckoutData;
import amaz.objects.TwentyfourSeven.data.models.DirectPaymentAuthorizeData;
import amaz.objects.TwentyfourSeven.data.models.DirectPaymentConfirmData;
import amaz.objects.TwentyfourSeven.data.models.Order;
import amaz.objects.TwentyfourSeven.data.models.responses.DirectPaymentAuthorizeResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.DirectPaymentAuthorizeResponseMessage;
import amaz.objects.TwentyfourSeven.data.models.responses.DirectPaymentConfirmResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.DirectPaymentResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.PaymentInquiryResponse;
import amaz.objects.TwentyfourSeven.injection.Injection;
import amaz.objects.TwentyfourSeven.listeners.OnRefeshTokenResponse;
import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import amaz.objects.TwentyfourSeven.presenter.PresenterFactory;
import amaz.objects.TwentyfourSeven.ui.PayViaCreditCard.PayViaCreditCardActivity;
import amaz.objects.TwentyfourSeven.ui.RegisterOrLogin.RegisterOrLoginActivity;
import amaz.objects.TwentyfourSeven.utilities.Constants;
import amaz.objects.TwentyfourSeven.utilities.Fonts;
import amaz.objects.TwentyfourSeven.utilities.LanguageUtilities;
import amaz.objects.TwentyfourSeven.utilities.LocalSettings;
import amaz.objects.TwentyfourSeven.utilities.TokenUtilities;

public class StcPayActivity extends BaseActivity implements AddMoneyPresenter.AddMoneyView, View.OnClickListener, OnRefeshTokenResponse {

    private TextView tv_title, tv_amount_title, tv_amount, tv_send, tv_stc_phone_number, tv_stc_code;
    private EditText et_stc_phone_number, et_stc_code;
    private Fonts fonts;
    private AddMoneyPresenter stcPayPresenter;
    private LocalSettings localSettings;
    private ProgressView pvLoad;
    private ImageView backIv, errorIv;
    private TextView errorTv;
    private RelativeLayout errorRl, mainContentRV;
    private TextView noConnectionTitleTv, noConnectionMessageTv;
    private LinearLayout noConnectionLl, stcCodeLl;
    private Button reloadBtn;
    private DirectPaymentAuthorizeCheckoutData resultPaymentAuthorize;
    private Order order;
    private Double amount;

    @Override
    public PresenterFactory.PresenterType getPresenterType() {
        return PresenterFactory.PresenterType.ADDMONEY;
    }

    @Override
    public void onPresenterAvailable(BasePresenter presenter) {
        stcPayPresenter = (AddMoneyPresenter) presenter;
        stcPayPresenter.setView(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localSettings = new LocalSettings(this);
        setLanguage();
        setContentView(R.layout.activity_stc_pay);
        getDataFromIntent();
        initialization();
        setFonts();
    }

    @Override
    public void onResume() {
        super.onResume();
        setLanguage();
    }

    private void initialization() {
        tv_title = (TextView) findViewById(R.id.tv_title_stc_pay);
        tv_amount_title = (TextView) findViewById(R.id.tv_amount_title);
        tv_amount = (TextView) findViewById(R.id.tv_amount);
        tv_send = (TextView) findViewById(R.id.tv_send_stc_pay);
        tv_stc_phone_number = (TextView) findViewById(R.id.tv_stc_phone_number);
        et_stc_phone_number = (EditText) findViewById(R.id.et_stc_phone_number);
        tv_stc_code = (TextView) findViewById(R.id.tv_stc_code);
        et_stc_code = (EditText) findViewById(R.id.et_stc_code);
        backIv = findViewById(R.id.iv_back);
        pvLoad = findViewById(R.id.pv_load);
        if (localSettings.getLocale().equals(Constants.ARABIC)) {
            backIv.setImageResource(R.drawable.back_ar_ic);
        } else {
            backIv.setImageResource(R.drawable.back_ic);
        }
        backIv.setOnClickListener(this);

        errorRl = findViewById(R.id.rl_error);
        errorIv = findViewById(R.id.iv_error);
        errorTv = findViewById(R.id.tv_error);
        mainContentRV = findViewById(R.id.mainContentRV);
        stcCodeLl = findViewById(R.id.ll_stc_code);
        stcCodeLl.setVisibility(View.GONE);
        noConnectionLl = findViewById(R.id.ll_no_connection);
        noConnectionTitleTv = findViewById(R.id.tv_no_connection_title);
        noConnectionMessageTv = findViewById(R.id.tv_no_connection_message);
        reloadBtn = findViewById(R.id.btn_reload_page);
        reloadBtn.setOnClickListener(this);
        tv_send.setOnClickListener(this);
        fonts = MApplication.getInstance().getFonts();

        setAmountValue();
    }

    private void getDataFromIntent() {
        if (getIntent() != null && getIntent().getSerializableExtra(Constants.ORDER) != null)
            order = (Order) getIntent().getSerializableExtra(Constants.ORDER);
    }

    public void setAmountValue() {
        if (order != null)
            amount = order.getTotalPrice();
        else
            amount = localSettings.getUser().getWallet_value();
        tv_amount.setText(String.format(Locale.ENGLISH, "%.2f", amount) + " " + getResources().getString(R.string.sar));
        et_stc_phone_number.setText(localSettings.getUser().getMobile());
    }

    private void setFonts() {
        tv_amount_title.setTypeface(fonts.customFont());
        tv_amount.setTypeface(fonts.customFont());
        tv_title.setTypeface(fonts.customFontBD());
        tv_send.setTypeface(fonts.customFontBD());
        tv_stc_phone_number.setTypeface(fonts.customFontBD());
        et_stc_phone_number.setTypeface(fonts.customFont());
        tv_stc_code.setTypeface(fonts.customFontBD());
        et_stc_code.setTypeface(fonts.customFont());
        errorTv.setTypeface(fonts.customFont());
        noConnectionTitleTv.setTypeface(fonts.customFont());
        noConnectionMessageTv.setTypeface(fonts.customFont());
        reloadBtn.setTypeface(fonts.customFontBD());
    }

    private void setLanguage() {
        if (localSettings.getLocale().equals(Constants.ARABIC)) {
            LanguageUtilities.switchToArabicLocale(this);
        }
    }

    @Override
    public void hideLoading() {
        pvLoad.setVisibility(View.GONE);
    }

    @Override
    public void showLoading() {
        pvLoad.setVisibility(View.VISIBLE);
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
    public void showSuccessStcAuthorize(DirectPaymentAuthorizeData data) {
        if (errorRl.getVisibility() == View.VISIBLE) {
            errorRl.setVisibility(View.GONE);
        }
        mainContentRV.setVisibility(View.VISIBLE);
        noConnectionLl.setVisibility(View.GONE);
        if (data.getCheckoutData().getError() != null && data.getCheckoutData().getError() != "") {
            Toast.makeText(this, R.string.invalid_phone_number, Toast.LENGTH_LONG).show();
        } else {
            stcCodeLl.setVisibility(View.VISIBLE);
            resultPaymentAuthorize = data.getCheckoutData();
            et_stc_phone_number.setEnabled(false);
            Toast.makeText(this, R.string.success_send_code, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void showSuccessStcConfirm(DirectPaymentConfirmData data) {
        if (data.getCheckoutData().getError() != null && data.getCheckoutData().getError() != "") {
            Toast.makeText(this, R.string.invalid_verification_code, Toast.LENGTH_LONG).show();
        } else if (data.getCheckoutData().getPaymentStatus() == 2) {
            Intent intent = new Intent(this, PayViaCreditCardActivity.class);
            if (order != null)
                intent.putExtra("orderId", order.getId());
            intent.putExtra("userId", localSettings.getUser().getId());
            intent.putExtra("stcRefNum", data.getCheckoutData().getRefNum());
            intent.putExtra("amount", amount);
            startActivity(intent);
        } else
            Toast.makeText(this, data.getCheckoutData().getPaymentStatusDesc(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void showSuccessRegisterCardPay(CardPayRegisterationData data) {

    }

    @Override
    public void showNetworkError() {
        mainContentRV.setVisibility(View.GONE);
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

    private void switchToRegisterationOrLogin() {
        Intent registerationOrLoginIntent = new Intent(this, RegisterOrLoginActivity.class);
        registerationOrLoginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(registerationOrLoginIntent);
    }

    @Override
    public void hideTokenLoader() {
        hideLoading();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_reload_page:

                break;
            case R.id.tv_send_stc_pay:
                if (stcCodeLl.getVisibility() == View.GONE && et_stc_phone_number.getText().toString().isEmpty())
                    Toast.makeText(this, getResources().getString(R.string.empty_phone_error), Toast.LENGTH_LONG).show();
                else if (stcCodeLl.getVisibility() == View.GONE && !et_stc_phone_number.getText().toString().isEmpty()) {
                    stcPayPresenter.postStcDirectPaymentAuthorize(localSettings.getLocale(), localSettings.getToken(), et_stc_phone_number.getText().toString(), amount);
                } else if (stcCodeLl.getVisibility() == View.VISIBLE && et_stc_code.getText().toString().isEmpty())
                    Toast.makeText(this, getResources().getString(R.string.four_digits_code), Toast.LENGTH_LONG).show();
                else if (stcCodeLl.getVisibility() == View.VISIBLE && !et_stc_code.getText().toString().isEmpty()) {
                    JSONObject json = new JSONObject();
                    try {
                        stcPayPresenter.postStcDirectPaymentConfirm(localSettings.getLocale(), localSettings.getToken(),
                                resultPaymentAuthorize.getOtpReference(), et_stc_code.getText().toString(),
                                resultPaymentAuthorize.getSTCPayPmtReference(), resultPaymentAuthorize.getRefNum());
                    } catch (Exception e) {

                    }
                }
                break;
        }

    }

}
