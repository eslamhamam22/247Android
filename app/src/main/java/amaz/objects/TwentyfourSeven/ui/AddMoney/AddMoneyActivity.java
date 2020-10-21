package amaz.objects.TwentyfourSeven.ui.AddMoney;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.payfort.sdk.android.dependancies.models.FortRequest;
import com.rey.material.widget.ProgressView;

import java.util.ArrayList;
import java.util.Locale;

import amaz.objects.TwentyfourSeven.BaseActivity;
import amaz.objects.TwentyfourSeven.MApplication;
import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.data.models.CardPayRegisterationData;
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

    private TextView add_money_title, tv_amount, tv_credit, tv_select_payment, tv_bank_transfer, tv_sadad;
    private Fonts fonts;
    private AddMoneyPresenter addMoneyPresenter;
    private LocalSettings localSettings;
    private ProgressView pvLoad;
    private ArrayList<UserTransaction> userTransactions = new ArrayList<>();
    private ImageView backIv, errorIv;
    private TextView errorTv;
    private RelativeLayout bankTransferRV, creditCardLayout;
    private boolean firstConnect = false;
    private BroadcastReceiver mBroadcastReceiver;

    //public FortCallBackManager fortCallback = null;
// payment
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

        //initilizePayFortSDK();

    }

    @Override
    public void onResume() {
        super.onResume();
        setLanguage();
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver,
                new IntentFilter(Constants.BROADCASTRECEVIERGENERATION));
        firstConnect = true;
        bankTransferRV.setBackground(getResources().getDrawable(R.drawable.bg_nr));
        creditCardLayout.setBackground(getResources().getDrawable(R.drawable.bg_nr));
        if(localSettings.getUser().getWallet_value() == 0){
            finish();
        }
    }

    private void initialization() {
        add_money_title = (TextView) findViewById(R.id.tv_add_money);
        tv_amount = (TextView) findViewById(R.id.tv_amount);
        tv_select_payment = (TextView) findViewById(R.id.tv_select_payment);
        tv_credit = (TextView) findViewById(R.id.tv_credit);
        tv_sadad = (TextView) findViewById(R.id.tv_sadad);
        tv_bank_transfer = (TextView) findViewById(R.id.tv_bank_transfer);
        bankTransferRV = (RelativeLayout) findViewById(R.id.bankTransferRV);
        creditCardLayout = (RelativeLayout) findViewById(R.id.creditCardLayout);
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
        setAmountValue();

    }

    private void setFonts() {
        tv_amount.setTypeface(fonts.customFontBD());
        tv_bank_transfer.setTypeface(fonts.customFont());
        tv_sadad.setTypeface(fonts.customFont());
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
    public void showSuccessRegisterCardPay(CardPayRegisterationData data) {
        Intent intent = new Intent(this, PayViaCreditCardActivity.class);
        intent.putExtra("card_pay_data", data);
        startActivity(intent);
//        Set<String> paymentBrands = new LinkedHashSet<String>();
//
//        paymentBrands.add("VISA");
//        paymentBrands.add("MASTER");
//
//        CheckoutSettings checkoutSettings = new CheckoutSettings(data.getCheckoutId(), paymentBrands);
////        Intent intent = checkoutSettings.createCheckoutActivityIntent(this);
////
////        startActivityForResult(intent, CheckoutActivity.REQUEST_CODE_CHECKOUT);
//        checkoutSettings.setLocale("en");
////        Intent intent = new Intent(this, CheckoutActivity.class);
////        intent.putExtra(CheckoutActivity.CHECKOUT_SETTINGS, checkoutSettings);
////        startActivityForResult(intent, CheckoutActivity.CHECKOUT_ACTIVITY);
//
//        PaymentParams paymentParams = null;
//        try {
//            paymentParams = new CardPaymentParams(data.getCheckoutId(), "VISA","4111111111111111",
//                    "TEST","05","21","100");
//            Transaction transaction = null;
//
//            try {
//                transaction = new Transaction(paymentParams);
//                binder.submitTransaction(transaction);
//            } catch (PaymentException ee) {
//                /* error occurred */
//            }
//        } catch (PaymentException e) {
//            e.printStackTrace();
//        }

// Set shopper result URL


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
        switch (view.getId()) {

            case R.id.iv_back:
                finish();
                break;

            case R.id.bankTransferRV:
                bankTransferRV.setBackground(getResources().getDrawable(R.drawable.bg_ac));
                Intent intent = new Intent(this, BankTransferActivity.class);
                startActivity(intent);
                break;

            case R.id.creditCardLayout:
                creditCardLayout.setBackground(getResources().getDrawable(R.drawable.bg_ac));
                //requestForPayfortPayment();
                addMoneyPresenter.registerCardPayment(localSettings.getLocale(), localSettings.getToken(), localSettings.getUser().getWallet_value());
                break;
        }

    }

//    private void initilizePayFortSDK() {
//        fortCallback = FortCallback.Factory.create();
//    }


    public void setAmountValue() {
        tv_amount.setText(String.format(Locale.ENGLISH, "%.2f", localSettings.getUser().getWallet_value()) + " " + getResources().getString(R.string.sar));

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PayFortPayment.RESPONSE_PURCHASE) {
//            fortCallback.onActivityResult(requestCode, resultCode, data);
//        }
    }

//    private void requestForPayfortPayment() {
//        PayFortData payFortData = new PayFortData();
//            payFortData.amount = String.valueOf((int) (Float.parseFloat("500") * 100));// Multiplying with 100, bcz amount should not be in decimal format
//            payFortData.command = PayFortPayment.PURCHASE;
//            payFortData.currency = PayFortPayment.CURRENCY_TYPE;
//        payFortData.paymentOption = "VISA";
//
//        payFortData.customerEmail = "readyandroid@gmail.com";
//            payFortData.language = PayFortPayment.LANGUAGE_TYPE;
//            payFortData.merchantReference = String.valueOf(System.currentTimeMillis());
//
//            PayFortPayment payFortPayment = new PayFortPayment(this, this.fortCallback, this);
//            payFortPayment.requestForPayment(payFortData);
//
//    }

//    @Override
//    public void onPaymentRequestResponse(int responseType, final PayFortData responseData) {
//        if (responseType == PayFortPayment.RESPONSE_GET_TOKEN) {
//            Toast.makeText(this, "Token not generated", Toast.LENGTH_SHORT).show();
//            Log.e("onPaymentResponse", "Token not generated");
//        } else if (responseType == PayFortPayment.RESPONSE_PURCHASE_CANCEL) {
//            Toast.makeText(this, "Payment cancelled", Toast.LENGTH_SHORT).show();
//            Log.e("onPaymentResponse", "Payment cancelled");
//        } else if (responseType == PayFortPayment.RESPONSE_PURCHASE_FAILURE) {
//            Toast.makeText(this, "Payment failed", Toast.LENGTH_SHORT).show();
//            Log.e("onPaymentResponse", "Payment failed");
//        } else {
//            Toast.makeText(this, "Payment successful", Toast.LENGTH_SHORT).show();
//            Log.e("onPaymentResponse", "Payment successful");
//        }
//    }

//    @Override
//    public void setToken(String token) {
//
//        FortRequest fortrequest = new FortRequest();
//        fortrequest.setRequestMap(collectRequestMap(token));
//        fortrequest.setShowResponsePage(true); // to [display/use] the SDK response page
//    }
//    private Map<String, Object> collectRequestMap(String sdkToken) {
//        Map<String, Object> requestMap = new HashMap<>();
//        requestMap.put("command", "PURCHASE");
//        requestMap.put("customer_email", "Sam@gmail.com");
//        requestMap.put("currency", "SAR");
//        requestMap.put("amount", "100");
//        requestMap.put("language", "en");
//        requestMap.put("merchant_reference", String.valueOf(System.currentTimeMillis()));
//        requestMap.put("customer_name", "Sam");
//        requestMap.put("customer_ip", "172.150.16.10");
//        requestMap.put("payment_option", "VISA");
//        requestMap.put("response_message", "Insufficient Funds");
//        requestMap.put("eci", "ECOMMERCE");
//        requestMap.put("token_name",FortSdk.getDeviceId(AddMoneyActivity.this));
//        requestMap.put("sdk_token",sdkToken);
//        requestMap.put("order_description", "DESCRIPTION");
//        return requestMap;
//    }

}