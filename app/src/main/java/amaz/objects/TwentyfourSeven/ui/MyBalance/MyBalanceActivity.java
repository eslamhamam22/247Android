package amaz.objects.TwentyfourSeven.ui.MyBalance;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rey.material.widget.ProgressView;

import java.util.ArrayList;
import java.util.Locale;

import amaz.objects.TwentyfourSeven.BaseActivity;
import amaz.objects.TwentyfourSeven.MApplication;
import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.adapters.BalanceAdapter;
import amaz.objects.TwentyfourSeven.data.models.Order;
import amaz.objects.TwentyfourSeven.data.models.UserTransaction;
import amaz.objects.TwentyfourSeven.data.models.User;
import amaz.objects.TwentyfourSeven.data.models.Wallet;
import amaz.objects.TwentyfourSeven.injection.Injection;
import amaz.objects.TwentyfourSeven.listeners.OnOrderClickListener;
import amaz.objects.TwentyfourSeven.listeners.OnRefeshTokenResponse;
import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import amaz.objects.TwentyfourSeven.presenter.PresenterFactory;
import amaz.objects.TwentyfourSeven.ui.AddMoney.AddMoneyActivity;
import amaz.objects.TwentyfourSeven.ui.MyAccount.MainActivity;
import amaz.objects.TwentyfourSeven.ui.MyAccount.MyAccountFragment;
import amaz.objects.TwentyfourSeven.ui.OrderDetails.CustomerOrderDetails.CustomerOrderDetailsActivity;
import amaz.objects.TwentyfourSeven.ui.RegisterOrLogin.RegisterOrLoginActivity;
import amaz.objects.TwentyfourSeven.utilities.Constants;
import amaz.objects.TwentyfourSeven.utilities.Fonts;
import amaz.objects.TwentyfourSeven.utilities.LanguageUtilities;
import amaz.objects.TwentyfourSeven.utilities.LocalSettings;
import amaz.objects.TwentyfourSeven.utilities.TokenUtilities;

public class MyBalanceActivity extends BaseActivity implements MyBalancePresenter.MyBalanceView, View.OnClickListener,
         OnRefeshTokenResponse,OnOrderClickListener {

    private RecyclerView transcationRV;
    private BalanceAdapter adapter;
    private TextView title,tv_wallet_title,tv_total_money,tv_wallet_amount,tv_total_earing,tv_title_minus_money,tv_title_need_money,tv_add_money,tv_transaction_title;
    private Fonts fonts;
    private MyBalancePresenter myBalancePresenter;
    private LocalSettings localSettings;
    private ProgressView pvLoad;
    private ArrayList<UserTransaction> userTransactions = new ArrayList<>();
    private RelativeLayout errorRl;
    private ImageView backIv,errorIv;
    private TextView errorTv;
    private ProgressBar load_more;
    private RelativeLayout connection_error;
    private TextView noConnectionTitleTv, noConnectionMessageTv;
    private LinearLayout noConnectionLl;
    private Button reloadBtn;
    private RelativeLayout wallet_rv;
    private boolean firstConnect = false;
    private BroadcastReceiver mBroadcastReceiver;
    private LinearLayout addMoneyView;
    private NestedScrollView nestedScrollView;

    @Override
    public PresenterFactory.PresenterType getPresenterType() {
        return PresenterFactory.PresenterType.MYBALANCE;
    }

    @Override
    public void onPresenterAvailable(BasePresenter presenter) {
        myBalancePresenter = (MyBalancePresenter) presenter;
        myBalancePresenter.setView(this);
        if(userTransactions.isEmpty()){
            myBalancePresenter.getWalletDetails(localSettings.getLocale(),localSettings.getToken());
        }
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localSettings = new LocalSettings(this);
        setLanguage();
        setContentView(R.layout.activity_balance_details);
        initialization();
        setFonts();
        setBroadCast();
    }

   
    @Override
    public void onResume() {
        super.onResume();
        setLanguage();
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver,
                new IntentFilter(Constants.BROADCASTRECEVIERGENERATION));
        firstConnect = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);

    }

    private void initialization(){
        title = (TextView) findViewById(R.id.tv_title_balance_details);
        tv_wallet_title = (TextView) findViewById(R.id.tv_wallet_title);
        tv_wallet_amount = (TextView) findViewById(R.id.tv_wallet_amount);
        tv_title_minus_money = (TextView) findViewById(R.id.tv_title_minus_money);
        tv_title_need_money = (TextView) findViewById(R.id.tv_title_need_money);
        tv_add_money = (TextView) findViewById(R.id.tv_add_money);
        tv_transaction_title = (TextView) findViewById(R.id.tv_transaction_title);
        wallet_rv= (RelativeLayout) findViewById(R.id.wallet_rv);
        nestedScrollView =  findViewById(R.id.nestedScrollView);
        load_more = findViewById(R.id.load_more);
        addMoneyView = (LinearLayout) findViewById(R.id.addMoney_linear);
        transcationRV = (RecyclerView) findViewById(R.id.rv_transactions);
        tv_total_earing =  findViewById(R.id.tv_total_earing);
        tv_total_money =  findViewById(R.id.tv_total_money);

        backIv = findViewById(R.id.iv_back);
        if (localSettings.getLocale().equals(Constants.ARABIC)) {
            backIv.setImageResource(R.drawable.back_ar_ic);
        } else {
            backIv.setImageResource(R.drawable.back_ic);
        }
        backIv.setOnClickListener(this);

        fonts = MApplication.getInstance().getFonts();
        pvLoad =(ProgressView) findViewById(R.id.pv_load);

        transcationRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));

        adapter = new BalanceAdapter(this, userTransactions);
        transcationRV.setAdapter(adapter);
       // tv_transaction_title.setVisibility(View.GONE);
        transcationRV.setVisibility(View.GONE);
        transcationRV.setNestedScrollingEnabled(false);
//        transcationRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if(!recyclerView.canScrollVertically(0) && newState == SCROLL_STATE_IDLE){
//                    if (myBalancePresenter.getMoreDataAvailable() && myBalancePresenter.getPage() != 1) {
//                        load_more.setVisibility(View.VISIBLE);
//                        myBalancePresenter.getTransactionHistory(localSettings.getToken(),localSettings.getLocale());
//                    }
//                }
//
//
//            }
//        });

        nestedScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged()
            {
                View view = nestedScrollView.getChildAt(nestedScrollView.getChildCount() - 1);

                int diff = (view.getBottom() - (nestedScrollView.getHeight() + nestedScrollView
                        .getScrollY()));

                if (diff == 0) {
                    // your pagination code
                    if (myBalancePresenter.getMoreDataAvailable() && myBalancePresenter.getPage() != 1) {
                        load_more.setVisibility(View.VISIBLE);
                        myBalancePresenter.getTransactionHistory(localSettings.getToken(),localSettings.getLocale());
                    }
                }
            }
        });
       
        errorRl = findViewById(R.id.rl_error);
        errorIv = findViewById(R.id.iv_error);
        errorTv = findViewById(R.id.tv_error);
        tv_add_money.setOnClickListener(this);
        noConnectionLl =  findViewById(R.id.ll_no_connection);
        connection_error =  findViewById(R.id.connection_error);
        noConnectionTitleTv = findViewById(R.id.tv_no_connection_title);
        noConnectionMessageTv = findViewById(R.id.tv_no_connection_message);
        reloadBtn = findViewById(R.id.btn_reload_page);
        reloadBtn.setOnClickListener(this);
        setWalletValue();
        
    }

    private void setWalletValue(){

        if (localSettings.getUser().getWallet_value()>= 0){
            wallet_rv.setBackgroundColor(getResources().getColor(R.color.honey_green));
            tv_wallet_amount.setTextColor(getResources().getColor(R.color.kelly_green));
            addMoneyView.setVisibility(View.GONE);
        }else{
            wallet_rv.setBackgroundColor(getResources().getColor(R.color.red_wallet));

            tv_wallet_amount.setTextColor(getResources().getColor(R.color.app_color));
            double value_pos = -1 * localSettings.getUser().getWallet_value();
            if (value_pos >= localSettings.getMaxMinusAmount()){
                tv_title_minus_money.setVisibility(View.GONE);
                tv_title_need_money.setText(getResources().getString(R.string.max_debit));
            }else{
                tv_title_minus_money.setVisibility(View.VISIBLE);
                tv_title_need_money.setText(getResources().getString(R.string.need_balance));
            }
            addMoneyView.setVisibility(View.VISIBLE);
        }
        addMoneyView.setVisibility(View.VISIBLE);
        tv_wallet_amount.setText(String.format(Locale.ENGLISH,"%.2f",localSettings.getUser().getWallet_value()) + " "+ getResources().getString(R.string.sar));
    }

    private void setFonts(){
        errorTv.setTypeface(fonts.customFont());
         title.setTypeface(fonts.customFontBD());
         tv_add_money.setTypeface(fonts.customFont());
         tv_title_minus_money.setTypeface(fonts.customFont());
         tv_title_need_money.setTypeface(fonts.customFont());
         tv_transaction_title.setTypeface(fonts.customFontBD());
         tv_wallet_amount.setTypeface(fonts.customFontBD());
         tv_wallet_title.setTypeface(fonts.customFont());
        tv_total_earing.setTypeface(fonts.customFont());
        tv_total_money.setTypeface(fonts.customFont());

        noConnectionTitleTv.setTypeface(fonts.customFontBD());
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
    public void showNoData() {
      //  tv_transaction_title.setVisibility(View.GONE);
        errorRl.setVisibility(View.VISIBLE);
        errorIv.setImageResource(R.drawable.no_active_stores);
        errorTv.setText(R.string.no_transaction);
        transcationRV.setVisibility(View.GONE);

    }

    @Override
    public void setWalletData(Wallet walletData) {
        tv_total_money.setText(String.format(Locale.ENGLISH,"%.2f",walletData.getEarnings()) + " "+ getResources().getString(R.string.sar));
       User user = localSettings.getUser();
       user.setWallet_value(walletData.getBalance());
       localSettings.setUser(user);
        tv_wallet_amount.setText(String.format(Locale.ENGLISH,"%.2f",localSettings.getUser().getWallet_value()) + " "+ getResources().getString(R.string.sar));

        nestedScrollView.setVisibility(View.VISIBLE);

    }

    @Override
    public void showNetworkError(boolean isView) {
        if (isView) {
            noConnectionLl.setVisibility(View.VISIBLE);
            connection_error.setVisibility(View.VISIBLE);
            reloadBtn.setEnabled(true);

        }else {
            if (load_more.getVisibility() == View.VISIBLE) {
                load_more.setVisibility(View.GONE);
            }
           Toast.makeText(this, R.string.connection_error, Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void setUserTransactions(ArrayList<UserTransaction> userTransactions) {
        if (load_more.getVisibility() == View.VISIBLE) {
            load_more.setVisibility(View.GONE);
        }
        noConnectionLl.setVisibility(View.GONE);
        connection_error.setVisibility(View.GONE);
        if (errorRl.getVisibility() == View.VISIBLE) {
            errorRl.setVisibility(View.GONE);
        }
        tv_transaction_title.setVisibility(View.VISIBLE);
        transcationRV.setVisibility(View.VISIBLE);
        this.userTransactions.clear();
        //adapter.notifyDataSetChanged();
        this.userTransactions.addAll(userTransactions);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void showServerError() {
        if (load_more.getVisibility() == View.VISIBLE) {
            load_more.setVisibility(View.GONE);
        }
        Toast.makeText(this, R.string.server_error, Toast.LENGTH_LONG).show();

    }

    @Override
    public void showSuspededUserError(String errorMessage) {
        if (load_more.getVisibility() == View.VISIBLE) {
            load_more.setVisibility(View.GONE);
        }
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
            localSettings.removeToken();
            localSettings.removeRefreshToken();
            localSettings.removeUser();
            switchToRegisterationOrLogin();

    }


    private void switchToRegisterationOrLogin(){

            Intent registerationOrLoginIntent = new Intent(this, RegisterOrLoginActivity.class);
            registerationOrLoginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(registerationOrLoginIntent);

    }

    private void switchToOrderDetails(Order order){
        Intent orderDetailsIntent;
        /*if (order.getStatus().equals("assigned") || order.getStatus().equals("delivery_in_progress") ||
                order.getStatus().equals("item_picked")){
            orderDetailsIntent = new Intent(this, OrderChatActivity.class);
        }
        else {*/
            orderDetailsIntent = new Intent(this, CustomerOrderDetailsActivity.class);
        orderDetailsIntent.putExtra(Constants.FROM_CUSTOMER_ORDERS, true);
        //}
        orderDetailsIntent.putExtra(Constants.ORDER, order);
        startActivity(orderDetailsIntent);


    }

   


    @Override
    public void hideTokenLoader() {
        hideLoading();
    }



    @Override
    public void onOrderClick(Order order) {
        switchToOrderDetails(order);
    }

    @Override
    public void onShowMore() {

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.iv_back:
                if (getIntent().getBooleanExtra(Constants.OPEN_BALANCE,false)){
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.putExtra(Constants.OPEN_FRAGMENT,MyAccountFragment.class.getSimpleName());
                    startActivity(intent);
                }else{
                    finish();
                }
                break;
            case R.id.tv_add_money:

                Intent addmoneyIntent = new Intent(this,AddMoneyActivity.class);
                startActivity(addmoneyIntent);
                break;
            case R.id.btn_reload_page:
                reloadBtn.setEnabled(false);
                myBalancePresenter.reset();
                myBalancePresenter.getWalletDetails(localSettings.getLocale(),localSettings.getToken());
                  break;
        }

    }

    public void setBroadCast() {
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.e("received", "received customer");
                if (intent.getAction().equals(Constants.BROADCASTRECEVIERGENERATION)) {
                    if (firstConnect) {
                        setWalletValue();
                        myBalancePresenter.reset();
                        MyBalanceActivity.this.userTransactions.clear();
                        adapter.notifyDataSetChanged();
                        myBalancePresenter.getWalletDetails(localSettings.getLocale(),localSettings.getToken());
                    }
                }
            }
        };
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (getIntent().getBooleanExtra(Constants.OPEN_BALANCE,false)){
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_NO_ANIMATION);
           intent.putExtra(Constants.OPEN_FRAGMENT,MyAccountFragment.class.getSimpleName());
            startActivity(intent);
        }else{
            finish();
        }
    }
}
