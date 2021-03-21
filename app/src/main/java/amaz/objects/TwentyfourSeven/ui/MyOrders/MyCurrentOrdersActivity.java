package amaz.objects.TwentyfourSeven.ui.MyOrders;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.rey.material.widget.ProgressView;

import org.zakariya.stickyheaders.StickyHeaderLayoutManager;

import java.util.ArrayList;

import amaz.objects.TwentyfourSeven.BaseActivity;
import amaz.objects.TwentyfourSeven.BaseFragment;
import amaz.objects.TwentyfourSeven.MApplication;
import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.adapters.CurrentOrderAdapter;
import amaz.objects.TwentyfourSeven.adapters.OrdersAdapter;
import amaz.objects.TwentyfourSeven.data.models.DelegateActivationRequest;
import amaz.objects.TwentyfourSeven.data.models.Order;
import amaz.objects.TwentyfourSeven.data.models.OrdersSection;
import amaz.objects.TwentyfourSeven.data.models.Store;
import amaz.objects.TwentyfourSeven.data.models.User;
import amaz.objects.TwentyfourSeven.injection.Injection;
import amaz.objects.TwentyfourSeven.listeners.OnOrderClickListener;
import amaz.objects.TwentyfourSeven.listeners.OnRefeshTokenResponse;
import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import amaz.objects.TwentyfourSeven.presenter.PresenterFactory;
import amaz.objects.TwentyfourSeven.ui.OrderDetails.CustomerOrderDetails.CustomerOrderDetailsActivity;
import amaz.objects.TwentyfourSeven.ui.OrderDetails.DelegateOrderDetails.DelegateOrderDetailsActivity;
import amaz.objects.TwentyfourSeven.ui.OrderDetails.OrderDetailsActivity;
import amaz.objects.TwentyfourSeven.ui.RegisterOrLogin.RegisterOrLoginActivity;
import amaz.objects.TwentyfourSeven.utilities.Constants;
import amaz.objects.TwentyfourSeven.utilities.Fonts;
import amaz.objects.TwentyfourSeven.utilities.LanguageUtilities;
import amaz.objects.TwentyfourSeven.utilities.LocalSettings;
import amaz.objects.TwentyfourSeven.utilities.TokenUtilities;

public class MyCurrentOrdersActivity extends BaseActivity implements MyCurrentOrdersPresenter.MyCurrentOrderView , View.OnClickListener,
         OnRefeshTokenResponse,OnOrderClickListener {

    private RecyclerView orderaRecycleView;
    private CurrentOrderAdapter adapter;
    private TextView title;
    private Fonts fonts;
    private MyCurrentOrdersPresenter myOrdersPresenter;
    private LocalSettings localSettings;
    private ProgressBar loadMore;
    private ArrayList<Order> ordersSections = new ArrayList<>();
    private ProgressView loaderPv;
    private RelativeLayout errorRl;
    private ImageView errorIv,backIv;
    private TextView errorTv;


    @Override
    public PresenterFactory.PresenterType getPresenterType() {
        return PresenterFactory.PresenterType.MyCurrentOrder;
    }

    @Override
    public void onPresenterAvailable(BasePresenter presenter) {
        myOrdersPresenter = (MyCurrentOrdersPresenter) presenter;
        myOrdersPresenter.setView(this);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localSettings = new LocalSettings(this);
        setLanguage();
        setContentView(R.layout.activity_current_orders);
        initialization();
        getDataFromIntent();

        setFonts();
    }

   
    @Override
    public void onResume() {
        super.onResume();
        setLanguage();
    }

    private void initialization(){
        title = (TextView) findViewById(R.id.tv_my_order_title);
        orderaRecycleView = (RecyclerView) findViewById(R.id.rv_current_order);
        backIv = findViewById(R.id.iv_back);
        if (localSettings.getLocale().equals(Constants.ARABIC)) {
            backIv.setImageResource(R.drawable.back_ar_ic);
        } else {
            backIv.setImageResource(R.drawable.back_ic);
        }
        backIv.setOnClickListener(this);

        fonts = MApplication.getInstance().getFonts();
        loadMore =(ProgressBar) findViewById(R.id.load_more);

        orderaRecycleView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));

        adapter = new CurrentOrderAdapter(this,ordersSections,this);
        orderaRecycleView.setAdapter(adapter);

        orderaRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(0) && newState == RecyclerView.SCROLL_STATE_IDLE){
                    if (myOrdersPresenter.getMoreDataAvailable() && myOrdersPresenter.getPage() != 1) {
                                loadMore.setVisibility(View.VISIBLE);
                                myOrdersPresenter.getCurrentOrders(localSettings.getLocale(), localSettings.getToken());
                    }
                }
            }
        });
       
        loaderPv = findViewById(R.id.pv_load);
        errorRl = findViewById(R.id.rl_error);
        errorIv = findViewById(R.id.iv_error);
        errorTv = findViewById(R.id.tv_error);

        
    }

    private void setFonts(){
         errorTv.setTypeface(fonts.customFont());
         title.setTypeface(fonts.customFontBD());

    }

    private void setLanguage() {

            if (localSettings.getLocale().equals(Constants.ARABIC)) {
                LanguageUtilities.switchToArabicLocale(this);
            }
        

    }

   


    @Override
    public void hideLoading() {
        loadMore.setVisibility(View.GONE);
        loaderPv.setVisibility(View.GONE);
        
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
    public void setOrders(ArrayList<Order> currentOrders) {

            if (loadMore.getVisibility() == View.VISIBLE) {
                loadMore.setVisibility(View.GONE);
            }
            if (errorRl.getVisibility() == View.VISIBLE) {
                errorRl.setVisibility(View.GONE);
            }

            this.ordersSections.addAll(currentOrders);
            adapter.notifyDataSetChanged();
        
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

    private void getDataFromIntent() {

            ArrayList<Order> orders = new ArrayList<>();
            orders = (ArrayList<Order>) getIntent().getSerializableExtra(Constants.ORDERS);
            this.ordersSections.clear();
            this.ordersSections.addAll(orders);
            adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.iv_back:
                finish();
                break;
        }

    }
}
