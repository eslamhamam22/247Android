package amaz.objects.TwentyfourSeven.ui.PayViaCreditCard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import amaz.objects.TwentyfourSeven.BaseActivity;
import amaz.objects.TwentyfourSeven.MApplication;
import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.data.models.CancelationReason;
import amaz.objects.TwentyfourSeven.data.models.CardPayRegisterationData;
import amaz.objects.TwentyfourSeven.data.models.Offer;
import amaz.objects.TwentyfourSeven.data.models.Order;
import amaz.objects.TwentyfourSeven.data.models.Route;
import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import amaz.objects.TwentyfourSeven.presenter.PresenterFactory;
import amaz.objects.TwentyfourSeven.ui.MyBalance.MyBalanceActivity;
import amaz.objects.TwentyfourSeven.ui.OrderChat.OrderChatActivity;
import amaz.objects.TwentyfourSeven.ui.OrderChat.OrderChatPresenter;
import amaz.objects.TwentyfourSeven.ui.OrderDetails.OrderDetailsPresenter;
import amaz.objects.TwentyfourSeven.utilities.Constants;
import amaz.objects.TwentyfourSeven.utilities.Fonts;
import amaz.objects.TwentyfourSeven.utilities.LanguageUtilities;
import amaz.objects.TwentyfourSeven.utilities.LocalSettings;

public class PayViaCreditCardActivity extends BaseActivity implements View.OnClickListener, OrderDetailsPresenter.OrderDetailsView {

    private ImageView backIv;
    private WebView htmlHyperPayWv;
    private TextView creditCardTitleTv;
    private Fonts fonts;
    private LocalSettings localSettings;
    private CardPayRegisterationData cardPayData;
    private int orderId = 0, userId = 0;
    private String stcRefNum;
    private String checkoutId;
    private Double amount;
    private BroadcastReceiver mBroadcastReceiver;
    private boolean firstConnect = false;
    private OrderDetailsPresenter orderDetailsPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localSettings = new LocalSettings(this);
        setLanguage();
        setContentView(R.layout.activity_pay_via_creditcard);
        getDataFromIntent();
        initialization();
        setFonts();
        setBroadCast();
    }

    public void setBroadCast() {
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.e("received", "received customer");
                // checking for type intent filter
                if (intent.getAction().equals(Constants.BROADCASTRECEVIERGENERATION)) {
                    if (firstConnect) {
                        String link_to = intent.getStringExtra("link_to");
                        Log.e("link_to_chat", link_to);
                        if (link_to.equals("order_details")) {
                            String orderNotificationData = intent.getStringExtra("order_data");
                            if (orderNotificationData != null) {
                                try {
                                    JSONObject orderNotificationJson = new JSONObject(orderNotificationData);
                                    if (orderNotificationJson.getInt("id") == orderId && orderNotificationJson.get("status").equals("delivery_in_progress")) {
                                        orderDetailsPresenter.getCustomerOrderDetails(localSettings.getToken(), localSettings.getLocale(), orderId);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else if (link_to.equals("account")) {
                            String accountData = intent.getStringExtra("account");
                            if (accountData != null) {
                                try {
                                    JSONObject orderNotificationJson = new JSONObject(accountData);
                                    switchToMyBalance();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        };
    }

    private void switchToMyBalance() {
        Intent myBAlanceIntent = new Intent(this, MyBalanceActivity.class);
        startActivity(myBAlanceIntent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver,
                new IntentFilter(Constants.BROADCASTRECEVIERGENERATION));
        firstConnect = true;
        setLanguage();
    }

    private void initialization() {
        htmlHyperPayWv = findViewById(R.id.wv_html_hyperpay);
        creditCardTitleTv = findViewById(R.id.tv_credit_card_title);
        htmlHyperPayWv.getSettings().setJavaScriptEnabled(true);
        htmlHyperPayWv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        htmlHyperPayWv.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // do your handling codes here, which url is the requested url
                // probably you need to open that url rather than redirect:
                //https://app.24-7-delivery.com/payment-cards/payment/callback/?id=DE9084A34A5E0485779AD46281FDECAC.prod01-vm-tx09&resourcePath=%2Fv1%2Fcheckouts%2FDE9084A34A5E0485779AD46281FDECAC.prod01-vm-tx09%2Fpayment

                Log.e("redirect", url);
                view.loadUrl(url);
                return false; // then it is not handled by default action
            }
        });
        if (stcRefNum != null && !stcRefNum.isEmpty()) {
            String url = "https://app.24-7-delivery.com/payment-cards/payment/callback/?stcRef=" + stcRefNum + "&orderId=" + orderId + "&userId=" + userId + "&amount=" + amount;
            htmlHyperPayWv.loadUrl(url);
        } else if (checkoutId != null && !checkoutId.isEmpty()) {
            String url = "https://app.24-7-delivery.com/payment-cards/payment/callback/?id=" + checkoutId + "&resourcePath=/v1/checkouts/" + checkoutId + "/payment";
            htmlHyperPayWv.loadUrl(url);
        } else
            htmlHyperPayWv.loadDataWithBaseURL(null, cardPayData.getHtml(), "text/html", null, null);

        backIv = findViewById(R.id.iv_back);
        backIv.setOnClickListener(this);
        if (localSettings.getLocale().equals(Constants.ARABIC)) {
            backIv.setImageResource(R.drawable.back_ar_ic);
        } else {
            backIv.setImageResource(R.drawable.back_ic);
        }

        fonts = MApplication.getInstance().getFonts();

    }

    private void setFonts() {
        creditCardTitleTv.setTypeface(fonts.customFont());
    }

    private void setLanguage() {

        if (localSettings.getLocale().equals(Constants.ARABIC)) {
            LanguageUtilities.switchToArabicLocale(this);
        }
    }

    private void getDataFromIntent() {
        cardPayData = (CardPayRegisterationData) getIntent().getSerializableExtra("card_pay_data");
        orderId = getIntent().getIntExtra("orderId", 0);
        userId = getIntent().getIntExtra("userId", 0);
        stcRefNum = getIntent().getStringExtra("stcRefNum");
        checkoutId = getIntent().getStringExtra("checkoutId");
        amount = getIntent().getDoubleExtra("amount", 0);
    }

//    private String getWebViewData(){
//        return "<html>\n" +
//                "    \n" +
//                "    <head>\n" +
//                "            <script src=\"https://test.oppwa.com/v1/paymentWidgets.js?checkoutId="+checkoutId+"\"></script>\n" +
//                "            <style>\n" +
//                "                body {\n" +
//                "                    background-color:#f6f6f5;\n" +
//                "                }\n" +
//                "            </style>\n" +
//                "    </head>\n" +
//                "    <body>\n" +
//                "\n" +
//                "            <form action=\"https://247dev.objectsdev.com/payment-card/payment/callback/\" class=\"paymentWidgets\" data-brands=\"VISA MASTER AMEX\"></form>\n" +
//                "\n" +
//                "            <script>\n" +
//                "                var wpwlOptions = {style:\"card\"}            \n" +
//                "            </script>\n" +
//                "\n" +
//                "\n" +
//                "\n" +
//                "    </body>\n" +
//                "</html>";
//    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_back) {
            finish();
        }
    }

    @Override
    public void onPresenterAvailable(BasePresenter presenter) {
        orderDetailsPresenter = (OrderDetailsPresenter) presenter;
        orderDetailsPresenter.setView(this);
    }

    @Override
    public PresenterFactory.PresenterType getPresenterType() {
        return PresenterFactory.PresenterType.OrderDetails;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showPopupLoading() {

    }

    @Override
    public void hidePopupLoading() {

    }

    @Override
    public void showRatePopupLoading() {

    }

    @Override
    public void hideRatePopupLoading() {

    }

    @Override
    public void showInvalideTokenError() {

    }

    @Override
    public void showScreenNetworkError() {

    }

    @Override
    public void showToastNetworkError() {
        Toast.makeText(this, R.string.connection_error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showServerError() {
        Toast.makeText(this, R.string.server_error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showSuspededUserError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showNotAvailableOrderError(String errorMessage) {

    }

    @Override
    public void showAcceptOfferError(String errorMessage) {

    }

    @Override
    public void showOrderDetails(Order order) {
        Intent orderChatIntent = new Intent(this, OrderChatActivity.class);
        orderChatIntent.putExtra(Constants.ORDER, order);
        orderChatIntent.putExtra(Constants.FROM_CUSTOMER_ORDERS, true);
        startActivity(orderChatIntent);
        finish();
    }

    @Override
    public void showFreeCommission(boolean freeCommission) {

    }

    @Override
    public void showMapData(double fromLat, double fromLng, double toLat, double toLng) {

    }

    @Override
    public void showOrderDirections(Route route, boolean isDelegateDirection) {

    }

    @Override
    public void showDefaultDistance(boolean isDelegateDirection) {

    }

    @Override
    public void showSuccessAddOffer(Offer offer) {

    }

    @Override
    public void sucessCancel() {

    }

    @Override
    public void showOrderCancelledSucessfully() {

    }

    @Override
    public void showSuccessAcceptOffer(Order order) {

    }

    @Override
    public void showSuccessRejectOffer(Order order) {

    }

    @Override
    public void showSuccessOrderRate(Order order) {

    }

    @Override
    public void showSuccessReorder(Order order) {

    }

    @Override
    public void showDelegateCurrentLocation(double latitue, double longitude) {

    }

    @Override
    public void showBlockedAreaError() {

    }

    @Override
    public void showCancelationReasons(ArrayList<CancelationReason> cancelationReasons) {

    }

    @Override
    public void showSuccessIgnoreOrder() {

    }

    @Override
    public void showServerErrorMessage(String errorMessage) {

    }
}
