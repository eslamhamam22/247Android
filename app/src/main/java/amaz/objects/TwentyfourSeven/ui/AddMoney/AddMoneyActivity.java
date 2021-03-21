package amaz.objects.TwentyfourSeven.ui.AddMoney;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.payfort.sdk.android.dependancies.models.FortRequest;
import com.oppwa.mobile.connect.checkout.dialog.CheckoutActivity;
import com.oppwa.mobile.connect.checkout.meta.CheckoutSettings;
import com.oppwa.mobile.connect.exception.PaymentError;
import com.oppwa.mobile.connect.exception.PaymentException;
import com.oppwa.mobile.connect.provider.Connect;
import com.oppwa.mobile.connect.provider.Transaction;
import com.oppwa.mobile.connect.provider.TransactionType;
import com.oppwa.mobile.connect.service.ConnectService;
import com.oppwa.mobile.connect.service.IProviderBinder;
import com.rey.material.widget.ProgressView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

import amaz.objects.TwentyfourSeven.BaseActivity;
import amaz.objects.TwentyfourSeven.MApplication;
import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.api.APIURLs;
import amaz.objects.TwentyfourSeven.api.Callback;
import amaz.objects.TwentyfourSeven.api.Checkout;
import amaz.objects.TwentyfourSeven.data.models.CardPayRegisterationData;
import amaz.objects.TwentyfourSeven.data.models.DirectPaymentAuthorizeData;
import amaz.objects.TwentyfourSeven.data.models.DirectPaymentConfirmData;
import amaz.objects.TwentyfourSeven.data.models.Order;
import amaz.objects.TwentyfourSeven.data.models.UserTransaction;
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
//import com.payfort.fort.android.sdk.base.FortSdk;
//import com.payfort.fort.android.sdk.base.callbacks.FortCallBackManager;
//import com.payfort.fort.android.sdk.base.callbacks.FortCallback;

public class AddMoneyActivity extends BaseActivity implements
        //IPaymentRequestCallBack,
        AddMoneyPresenter.AddMoneyView,
        View.OnClickListener,
        OnRefeshTokenResponse {

    private TextView add_money_title, tv_amount, tv_credit, tv_select_payment, tv_bank_transfer, tv_sadad, tv_stcPay;
    private Fonts fonts;
    private AddMoneyPresenter addMoneyPresenter;
    private LocalSettings localSettings;
    private ProgressView pvLoad;
    private ArrayList<UserTransaction> userTransactions = new ArrayList<>();
    private ImageView backIv, errorIv;
    private TextView errorTv;
    private RelativeLayout bankTransferRV, creditCardLayout, sadadRV, stcPayRV;
    private boolean firstConnect = false;
    private BroadcastReceiver mBroadcastReceiver;
    private Order order;
    private IProviderBinder binder;
    private ServiceConnection serviceConnection;
    private Double amount;

    @Override
    public PresenterFactory.PresenterType getPresenterType() {
        return PresenterFactory.PresenterType.ADDMONEY;
    }

    @Override
    public void onPresenterAvailable(BasePresenter presenter) {
        addMoneyPresenter = (AddMoneyPresenter) presenter;
        addMoneyPresenter.setView(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localSettings = new LocalSettings(this);
        setLanguage();
        setContentView(R.layout.activity_add_money);
        initialization();
        setFonts();
        setBroadCast();
        initEnv();

    }

    @Override
    public void onResume() {
        super.onResume();
        setLanguage();
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver,
                new IntentFilter(Constants.BROADCASTRECEVIERGENERATION));
        firstConnect = true;
        bankTransferRV.setBackground(getResources().getDrawable(R.drawable.bg_nr));
        sadadRV.setBackground(getResources().getDrawable(R.drawable.bg_nr));
        stcPayRV.setBackground(getResources().getDrawable(R.drawable.bg_nr));
        creditCardLayout.setBackground(getResources().getDrawable(R.drawable.bg_nr));
        if (localSettings.getUser().getWallet_value() == 0) {
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, ConnectService.class);
        startService(intent);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(serviceConnection);
        stopService(new Intent(this, ConnectService.class));
    }

    private void initialization() {
        add_money_title = (TextView) findViewById(R.id.tv_add_money);
        tv_amount = (TextView) findViewById(R.id.tv_amount);
        tv_select_payment = (TextView) findViewById(R.id.tv_select_payment);
        tv_credit = (TextView) findViewById(R.id.tv_credit);
        tv_sadad = (TextView) findViewById(R.id.tv_sadad);
        tv_bank_transfer = (TextView) findViewById(R.id.tv_bank_transfer);
        sadadRV = (RelativeLayout) findViewById(R.id.sadadRV);
        stcPayRV = (RelativeLayout) findViewById(R.id.stcPayRV);
        tv_stcPay = (TextView) findViewById(R.id.tv_stcPay);
        bankTransferRV = (RelativeLayout) findViewById(R.id.bankTransferRV);
        creditCardLayout = (RelativeLayout) findViewById(R.id.creditCardLayout);
        sadadRV.setOnClickListener(this);
        stcPayRV.setOnClickListener(this);
        bankTransferRV.setOnClickListener(this);
        creditCardLayout.setOnClickListener(this);


        backIv = findViewById(R.id.iv_back);
        pvLoad = findViewById(R.id.pv_load);
        if (localSettings.getLocale().equals(Constants.ARABIC)) {
            backIv.setImageResource(R.drawable.back_ar_ic);
        } else {
            backIv.setImageResource(R.drawable.back_ic);
        }
        backIv.setOnClickListener(this);

        fonts = MApplication.getInstance().getFonts();

        if (getIntent() != null && getIntent().getSerializableExtra(Constants.ORDER) != null)
            order = (Order) getIntent().getSerializableExtra(Constants.ORDER);
        if (order != null)
            bankTransferRV.setVisibility(View.GONE);

        setAmountValue();

    }

    private void setFonts() {
        tv_amount.setTypeface(fonts.customFontBD());
        tv_bank_transfer.setTypeface(fonts.customFont());
        tv_sadad.setTypeface(fonts.customFont());
        tv_stcPay.setTypeface(fonts.customFont());
        tv_credit.setTypeface(fonts.customFont());
        tv_select_payment.setTypeface(fonts.customFontBD());
        add_money_title.setTypeface(fonts.customFont());
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

    }

    @Override
    public void showSuccessStcConfirm(DirectPaymentConfirmData data) {

    }

    @Override
    public void showSuccessRegisterCardPay(CardPayRegisterationData data) {
        /*Intent intent = new Intent(this, PayViaCreditCardActivity.class);
        intent.putExtra("card_pay_data", data);
        startActivity(intent);*/
        configCheckout(data.getCheckoutId());
        //gotoWebView(data.getCheckoutId());
    }

    private void gotoWebView(String checkoutId) {
        Intent intent = new Intent(this, PayViaCreditCardActivity.class);
        intent.putExtra("checkoutId", checkoutId);
        if (order != null && order.getId() > 0)
            intent.putExtra("orderId", order.getId());
        startActivity(intent);
        finish();
    }

    private void initEnv() {
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                binder = (IProviderBinder) service;
                try {
                    binder.initializeProvider(Connect.ProviderMode.LIVE);
                } catch (PaymentException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                binder = null;
            }
        };
    }

    private void configCheckout(String checkoutId) {
        //java.lang.SecurityException: getDeviceId: The user 10153 does not meet the requirements to access device identifiers.
        Set<String> paymentBrands = new LinkedHashSet<String>();
        paymentBrands.add("VISA");
        CheckoutSettings checkoutSettings = new CheckoutSettings(checkoutId, paymentBrands);
        checkoutSettings.setLocale("en");
        checkoutSettings.setWebViewEnabledFor3DSecure(true);
        Intent intent = new Intent(this, CheckoutActivity.class);
        intent.putExtra(CheckoutActivity.CHECKOUT_SETTINGS, checkoutSettings);
        startActivityForResult(intent, CheckoutActivity.CHECKOUT_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case CheckoutActivity.RESULT_OK:
                /* transaction completed */
                Transaction transaction = data.getParcelableExtra(CheckoutActivity.CHECKOUT_RESULT_TRANSACTION);
                /* resource path if needed */
                String resourcePath = data.getStringExtra(CheckoutActivity.CHECKOUT_RESULT_RESOURCE_PATH);
                String checkoutId = data.getStringExtra(CheckoutActivity.EXTRA_CHECKOUT_ID);
                if (transaction.getTransactionType() == TransactionType.SYNC) {
                    /* check the result of synchronous transaction */
                    gotoWebView(checkoutId);
                } else {
                    /* wait for the asynchronous transaction callback in the onNewIntent() */
                }
                break;
            case CheckoutActivity.RESULT_CANCELED:
                Toast.makeText(this, "Shoper cancelled transaction", Toast.LENGTH_LONG).show();
                break;
            case CheckoutActivity.RESULT_ERROR:
                PaymentError error = data.getParcelableExtra(CheckoutActivity.CHECKOUT_RESULT_ERROR);
                Toast.makeText(this, error.getErrorMessage(), Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getScheme().equals("devsupport")) {
            String checkoutId = intent.getData().getQueryParameter("id");
            gotoWebView(checkoutId);
        }
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
        int orderId = 0;
        if (order != null && order.getId() > 0)
            orderId = order.getId();
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;

            case R.id.bankTransferRV:
                bankTransferRV.setBackground(getResources().getDrawable(R.drawable.bg_ac));
                Intent intent = new Intent(this, BankTransferActivity.class);
                startActivity(intent);
                break;

            case R.id.sadadRV:
                sadadRV.setBackground(getResources().getDrawable(R.drawable.bg_ac));
                Intent intentSadad = new Intent(this, BankTransferActivity.class);
                startActivity(intentSadad);
                break;

            case R.id.stcPayRV:
                stcPayRV.setBackground(getResources().getDrawable(R.drawable.bg_ac));
                Intent intentStc = new Intent(this, StcPayActivity.class);
                if (order != null && order.getId() > 0)
                    intentStc.putExtra(Constants.ORDER, order);
                startActivity(intentStc);
                break;

            case R.id.creditCardLayout:
                creditCardLayout.setBackground(getResources().getDrawable(R.drawable.bg_ac));
                //addMoneyPresenter.registerCardPayment(localSettings.getLocale(), localSettings.getToken(), amount, orderId);
                addMoneyPresenter.getCheckoutId(localSettings.getLocale(), localSettings.getToken(), amount, orderId);
                break;
        }

    }

    public void setAmountValue() {
        if (order != null)
            amount = order.getTotalPrice();
        else
            amount = localSettings.getUser().getWallet_value();
        tv_amount.setText(String.format(Locale.ENGLISH, "%.2f", amount) + " " + getResources().getString(R.string.sar));
    }

    public void setBroadCast() {
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.e("received", "received customer");
                if (intent.getAction().equals(Constants.BROADCASTRECEVIERGENERATION)) {
                    if (firstConnect) {
                        setAmountValue();
                    }
                }
            }
        };
    }

}
