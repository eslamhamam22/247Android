package amaz.objects.TwentyfourSeven.ui.MyReviews;

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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rey.material.widget.ProgressView;

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
import amaz.objects.TwentyfourSeven.ui.RegisterOrLogin.RegisterOrLoginActivity;
import amaz.objects.TwentyfourSeven.utilities.Constants;
import amaz.objects.TwentyfourSeven.utilities.Fonts;
import amaz.objects.TwentyfourSeven.utilities.LanguageUtilities;
import amaz.objects.TwentyfourSeven.utilities.LocalSettings;
import amaz.objects.TwentyfourSeven.utilities.TokenUtilities;

public class MyReviewsActivity extends BaseActivity implements MyReviewsPresenter.MyReviewsView, View.OnClickListener, OnRefeshTokenResponse {


    private TextView myReviewsTitleTv;
    private ImageView backIv;
    private RelativeLayout mainContentRl;
    private RecyclerView myReviewsRv;
    private MyReviewsAdapter adapter;
    private ArrayList<Review> myReviews = new ArrayList<>();
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

    private MyReviewsPresenter myReviewsPresenter;
    private boolean isCustomerReviews;


    @Override
    public void onPresenterAvailable(BasePresenter presenter) {
        myReviewsPresenter = (MyReviewsPresenter) presenter;
        myReviewsPresenter.setView(this);
        if (myReviews.isEmpty()) {
            Log.e("omnia",isCustomerReviews+"");
            myReviewsPresenter.getCustomerOrDelegateReviews(localSettings.getToken(), localSettings.getLocale(), localSettings.getUser().getId(),
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
        setContentView(R.layout.activity_my_reviews);
        getDataFromIntent();
        initialization();
        setFonts();
    }

    private void getDataFromIntent() {
        isCustomerReviews = getIntent().getBooleanExtra(Constants.IS_CUSTOMER_REVIEWS, false);
        Log.e("omnia",localSettings.getUser().getId()+"  ");
    }

    private void setLanguage() {
        if (localSettings.getLocale().equals(Constants.ARABIC)) {
            LanguageUtilities.switchToArabicLocale(this);
        }
    }

    private void initialization() {
        myReviewsTitleTv = findViewById(R.id.tv_my_reviews_title);
        backIv = findViewById(R.id.iv_back);
        backIv.setOnClickListener(this);
        if (localSettings.getLocale().equals(Constants.ARABIC)) {
            backIv.setImageResource(R.drawable.back_ar_ic);
        } else {
            backIv.setImageResource(R.drawable.back_ic);
        }

        mainContentRl = findViewById(R.id.rl_main_content);
        myReviewsRv = findViewById(R.id.rv_my_reviews);
        myReviewsRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new MyReviewsAdapter(this, myReviews);
        myReviewsRv.setAdapter(adapter);
        myReviewsRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(0) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (myReviewsPresenter.isMoreDataAvailable() && myReviewsPresenter.getPage() != 1) {
                        myReviewsPresenter.getCustomerOrDelegateReviews(localSettings.getToken(), localSettings.getLocale(),
                                localSettings.getUser().getId(), isCustomerReviews);
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

        myReviewsTitleTv.setTypeface(fonts.customFontBD());

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

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.iv_back:
                finish();
                break;

            case R.id.btn_reload_page:
                myReviewsPresenter.getCustomerOrDelegateReviews(localSettings.getToken(), localSettings.getLocale(), localSettings.getUser().getId(), isCustomerReviews);
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
        errorRl.setVisibility(View.VISIBLE);
        errorIv.setImageResource(R.drawable.grayscale);
        errorTv.setText(R.string.no_data_error);
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
        this.myReviews.addAll(myReviews);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void hideTokenLoader() {
        hideLoading();
    }
}
