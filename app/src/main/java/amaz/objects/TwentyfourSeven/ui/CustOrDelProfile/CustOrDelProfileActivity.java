package amaz.objects.TwentyfourSeven.ui.CustOrDelProfile;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rey.material.widget.ProgressView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import amaz.objects.TwentyfourSeven.BaseActivity;
import amaz.objects.TwentyfourSeven.MApplication;
import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.adapters.MyReviewsAdapter;
import amaz.objects.TwentyfourSeven.data.models.Review;
import amaz.objects.TwentyfourSeven.data.models.User;
import amaz.objects.TwentyfourSeven.injection.Injection;
import amaz.objects.TwentyfourSeven.listeners.OnRefeshTokenResponse;
import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import amaz.objects.TwentyfourSeven.presenter.PresenterFactory;
import amaz.objects.TwentyfourSeven.ui.MyReviews.MyReviewsPresenter;
import amaz.objects.TwentyfourSeven.ui.RegisterOrLogin.RegisterOrLoginActivity;
import amaz.objects.TwentyfourSeven.utilities.Constants;
import amaz.objects.TwentyfourSeven.utilities.Fonts;
import amaz.objects.TwentyfourSeven.utilities.LanguageUtilities;
import amaz.objects.TwentyfourSeven.utilities.LocalSettings;
import amaz.objects.TwentyfourSeven.utilities.TokenUtilities;

public class CustOrDelProfileActivity extends BaseActivity implements MyReviewsPresenter.MyReviewsView, View.OnClickListener, OnRefeshTokenResponse {

    private TextView reviewsTitleTv, userNameTv, ordersNoTitleTv, ordersNoContentTv;
    //private TextView customerRateTitleTv, delegateRateTitleTv,
            //customerRateContentTv, delegateRateContentTv;
    private RatingBar custOrDelRateRb;
    private ImageView backIv, profileIv;
    private LinearLayout mainContentLl;
    //private LinearLayout customerRateLl, delegateRateLl, customerRateContentLl, delegateRateContentLl;
    private RecyclerView reviewsRv;
    private MyReviewsAdapter adapter;
    private ArrayList<Review> reviews = new ArrayList<>();
    private ProgressView loaderPv;
    private ProgressBar loadMorePb;

    private RelativeLayout errorRl;
    private ImageView errorIv;
    private TextView errorTv;

    private TextView noConnectionTitleTv, noConnectionMessageTv;
    private LinearLayout noConnectionLl;
    private Button reloadBtn;

    private LocalSettings localSettings;
    private Fonts fonts;

    private MyReviewsPresenter reviewsPresenter;
    private boolean isCustomerReviews;
    private int custOrDelId;


    @Override
    public void onPresenterAvailable(BasePresenter presenter) {
        reviewsPresenter = (MyReviewsPresenter) presenter;
        reviewsPresenter.setView(this);
        if (reviews.isEmpty()) {
            Log.e("omnia",isCustomerReviews+"");
            reviewsPresenter.getCustomerOrDelegateReviews(localSettings.getToken(), localSettings.getLocale(), custOrDelId,
                    isCustomerReviews);
        }
    }

    @Override
    public PresenterFactory.PresenterType getPresenterType() {
        return PresenterFactory.PresenterType.MyReviews;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localSettings = new LocalSettings(this);
        setLanguage();
        setContentView(R.layout.activity_cust_or_del_profile);
        getDataFromIntent();
        initialization();
        setFonts();
    }

    private void getDataFromIntent() {
        isCustomerReviews = getIntent().getBooleanExtra(Constants.IS_CUSTOMER_REVIEWS, false);
        custOrDelId = getIntent().getIntExtra(Constants.USER, 0);
        Log.e("omnia",custOrDelId+"  ");
    }

    private void setLanguage() {
        if (localSettings.getLocale().equals(Constants.ARABIC)) {
            LanguageUtilities.switchToArabicLocale(this);
        }
    }

    private void initialization() {
        reviewsTitleTv = findViewById(R.id.tv_my_reviews_title);
        if (isCustomerReviews){
            reviewsTitleTv.setText(R.string.customer_profile);
        }
        else {
            reviewsTitleTv.setText(R.string.delegate_profile);
        }
        backIv = findViewById(R.id.iv_back);
        backIv.setOnClickListener(this);
        if (localSettings.getLocale().equals(Constants.ARABIC)) {
            backIv.setImageResource(R.drawable.back_ar_ic);
        } else {
            backIv.setImageResource(R.drawable.back_ic);
        }

        profileIv = findViewById(R.id.iv_profile);
        userNameTv = findViewById(R.id.tv_user_name);
        custOrDelRateRb = findViewById(R.id.rb_cust_del_rate);
        ordersNoTitleTv = findViewById(R.id.tv_orders_no_title);
        ordersNoContentTv = findViewById(R.id.tv_orders_no_content);
        /*customerRateLl = findViewById(R.id.ll_customer_rating);
        customerRateContentLl = findViewById(R.id.ll_customer_rate_content);
        customerRateContentLl.setOnClickListener(this);
        customerRateTitleTv = findViewById(R.id.tv_customer_rate_title);
        customerRateContentTv = findViewById(R.id.tv_customer_rate_content);

        delegateRateLl = findViewById(R.id.ll_delegate_rating);
        delegateRateContentLl = findViewById(R.id.ll_delegate_rate_content);
        delegateRateContentLl.setOnClickListener(this);
        delegateRateTitleTv = findViewById(R.id.tv_delegate_rate_title);
        delegateRateContentTv = findViewById(R.id.tv_delegate_rate_content);*/

        if (isCustomerReviews){
            //delegateRateLl.setVisibility(View.GONE);
            //customerRateLl.setVisibility(View.VISIBLE);
        }
        else {
            //delegateRateLl.setVisibility(View.VISIBLE);
            //customerRateLl.setVisibility(View.GONE);
        }

        mainContentLl = findViewById(R.id.ll_main_content);
        mainContentLl.setVisibility(View.GONE);
        reviewsRv = findViewById(R.id.rv_my_reviews);
        reviewsRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new MyReviewsAdapter(this, reviews);
        reviewsRv.setAdapter(adapter);
        reviewsRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(0) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (reviewsPresenter.isMoreDataAvailable() && reviewsPresenter.getPage() != 1) {
                        reviewsPresenter.getCustomerOrDelegateReviews(localSettings.getToken(), localSettings.getLocale(),
                                custOrDelId, isCustomerReviews);
                    }
                }
            }
        });

        loaderPv = findViewById(R.id.pv_load);
        loadMorePb = findViewById(R.id.load_more_pb);

        errorRl = findViewById(R.id.rl_error);
        errorIv = findViewById(R.id.iv_error);
        errorTv = findViewById(R.id.tv_error);

        noConnectionLl = findViewById(R.id.ll_no_connection);
        noConnectionTitleTv = findViewById(R.id.tv_no_connection_title);
        noConnectionMessageTv = findViewById(R.id.tv_no_connection_message);
        reloadBtn = findViewById(R.id.btn_reload_page);
        reloadBtn.setOnClickListener(this);
    }


    private void setFonts() {
        fonts = MApplication.getInstance().getFonts();

        reviewsTitleTv.setTypeface(fonts.customFontBD());

        userNameTv.setTypeface(fonts.customFont());
        ordersNoTitleTv.setTypeface(fonts.customFont());
        ordersNoContentTv.setTypeface(fonts.customFont());
        /*customerRateTitleTv.setTypeface(fonts.customFont());
        customerRateContentTv.setTypeface(fonts.customFontBD());
        delegateRateTitleTv.setTypeface(fonts.customFont());
        delegateRateContentTv.setTypeface(fonts.customFontBD());*/

        errorTv.setTypeface(fonts.customFont());

        noConnectionTitleTv.setTypeface(fonts.customFontBD());
        noConnectionMessageTv.setTypeface(fonts.customFont());
        reloadBtn.setTypeface(fonts.customFontBD());
    }

    private void switchToRegisterationOrLogin() {
        Intent registerationOrLoginIntent = new Intent(this, RegisterOrLoginActivity.class);
        registerationOrLoginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(registerationOrLoginIntent);
    }

    private void setCustOrDelData(User user){
        if(user.getImage() != null){
            Picasso.with(this).load(user.getImage().getMedium()).placeholder(R.drawable.avatar).error(R.drawable.avatar).into(profileIv);
        }
        else {
            profileIv.setImageResource(R.drawable.avatar);
        }
        userNameTv.setText(user.getName());
        if (isCustomerReviews){
            ordersNoTitleTv.setText(R.string.orders_no);
            ordersNoContentTv.setText(String.valueOf(user.getOrdersNo()));
            if (user.getRating() != null){
                //customerRateContentTv.setText(String.format("%.2f", user.getRating()));
                custOrDelRateRb.setRating(user.getRating().floatValue());
            }
            else {
                //customerRateContentTv.setText(String.format("%.2f", 5.0));
                custOrDelRateRb.setRating(5);
            }
        }
        else {
            ordersNoTitleTv.setText(R.string.deliveries_no);
            ordersNoContentTv.setText(String.valueOf(user.getDelegateOrdersNo()));
            if (user.getDelegateRating() != null){
                //delegateRateContentTv.setText(String.format("%.2f", user.getDelegateRating()));
                custOrDelRateRb.setRating(user.getDelegateRating().floatValue());
            }
            else {
                //delegateRateContentTv.setText(String.format("%.2f", 5.0));
                custOrDelRateRb.setRating(5);
            }
        }

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.iv_back:
                finish();
                break;

            case R.id.btn_reload_page:
                reviewsPresenter.getCustomerOrDelegateReviews(localSettings.getToken(), localSettings.getLocale(), custOrDelId, isCustomerReviews);
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
    public void showLoadMore() {
        loadMorePb.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadMore() {
        loadMorePb.setVisibility(View.GONE);
    }

    @Override
    public void showInvalideTokenError() {
        TokenUtilities.refreshToken(this,
                Injection.provideUserRepository(),
                localSettings.getToken(),
                localSettings.getLocale(),
                localSettings.getRefreshToken(),
                this);
    }

    @Override
    public void showToastNetworkError() {
        Toast.makeText(this, R.string.connection_error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showScreenNetworkError() {
        noConnectionLl.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNoData(User user) {
        mainContentLl.setVisibility(View.VISIBLE);
        reviewsRv.setVisibility(View.GONE);
        setCustOrDelData(user);
        errorRl.setVisibility(View.VISIBLE);
        errorIv.setImageResource(R.drawable.grayscale);
        errorTv.setText(R.string.no_reviews);
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
    public void showMyReviews(ArrayList<Review> myReviews, User user) {
        if (noConnectionLl.getVisibility() == View.VISIBLE) {
            noConnectionLl.setVisibility(View.GONE);
        }

        if (errorRl.getVisibility() == View.VISIBLE) {
            errorRl.setVisibility(View.GONE);
        }
        mainContentLl.setVisibility(View.VISIBLE);
        setCustOrDelData(user);
        this.reviews.addAll(myReviews);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void hideTokenLoader() {
        hideLoading();
    }
}
