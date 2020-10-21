package amaz.objects.TwentyfourSeven.ui.HowToUse;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.google.android.material.tabs.TabLayout;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rey.material.widget.ProgressView;

import java.util.ArrayList;

import amaz.objects.TwentyfourSeven.utilities.Constants;
import amaz.objects.TwentyfourSeven.utilities.Fonts;
import amaz.objects.TwentyfourSeven.BaseActivity;
import amaz.objects.TwentyfourSeven.MApplication;
import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.adapters.SliderAdapter;
import amaz.objects.TwentyfourSeven.data.models.Slider;
import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import amaz.objects.TwentyfourSeven.presenter.PresenterFactory;
import amaz.objects.TwentyfourSeven.ui.DelegateRequest.DelegateRequestActivity;
import amaz.objects.TwentyfourSeven.ui.RegisterOrLogin.RegisterOrLoginActivity;
import amaz.objects.TwentyfourSeven.utilities.LanguageUtilities;
import amaz.objects.TwentyfourSeven.utilities.LocalSettings;
import pl.pzienowicz.autoscrollviewpager.AutoScrollViewPager;

public class HowToUseActivity extends BaseActivity implements View.OnClickListener, HowToUsePresenter.HowToUseView {

    private ImageView backIv;
    private RelativeLayout mainContentRl;
    private AutoScrollViewPager useVp;
    private TabLayout indicatorTl;
    private TextView skipTv;
    private ImageView nextIv;
    private Button submitRequestBtn;
    private ProgressView loaderPv;

    private SliderAdapter adapter;
    private ArrayList<Slider> pagerItems = new ArrayList<>();

    private TextView noConnectionTitleTv, noConnectionMessageTv;
    private LinearLayout noConnectionLl;
    private Button reloadBtn;

    private LocalSettings localSettings;
    private Fonts fonts;

    private HowToUsePresenter howToUsePresenter;

    private String page;
    private boolean fromSettings;

    @Override
    public void onPresenterAvailable(BasePresenter presenter) {
        howToUsePresenter = (HowToUsePresenter) presenter;
        howToUsePresenter.setView(this);
        if (page.equals(Constants.HOW_TO_USE)){
            howToUsePresenter.getHowToUseData(localSettings.getLocale());
        }
        else {
            howToUsePresenter.getHowToBecomeADelegateData(localSettings.getLocale());
        }


    }

    @Override
    public PresenterFactory.PresenterType getPresenterType() {
        return PresenterFactory.PresenterType.HowToUse;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localSettings = new LocalSettings(this);
        setLanguage();
        setContentView(R.layout.activity_how_to_use);
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
        fromSettings = getIntent().getBooleanExtra(Constants.FROM_SETTINGS,false);
        page = getIntent().getStringExtra(Constants.PAGE);
    }

    private void initialization() {

        backIv = findViewById(R.id.iv_back);
        backIv.setOnClickListener(this);
        if (localSettings.getLocale().equals(Constants.ARABIC)) {
            backIv.setImageResource(R.drawable.back_ar_red_ic);
        } else {
            backIv.setImageResource(R.drawable.back_red_ic);
        }
        mainContentRl = findViewById(R.id.rl_main_content);
        mainContentRl.setVisibility(View.GONE);
        useVp = findViewById(R.id.vp_use);
        indicatorTl = findViewById(R.id.tl_indicator);
        skipTv = findViewById(R.id.tv_skip);
        skipTv.setOnClickListener(this);
        nextIv = findViewById(R.id.iv_next);
        nextIv.setOnClickListener(this);
        submitRequestBtn = findViewById(R.id.btn_submit_request);
        submitRequestBtn.setOnClickListener(this);
        loaderPv = findViewById(R.id.pv_load);

        adapter = new SliderAdapter(this,localSettings.getLocale(),pagerItems);
        useVp.setAdapter(adapter);
        indicatorTl.setupWithViewPager(useVp);
        if (localSettings.getLocale().equals(Constants.ARABIC)){
            nextIv.setImageResource(R.drawable.back_red_ic);
        }
        else {
            nextIv.setImageResource(R.drawable.back_ar_red_ic);
        }

        if (page.equals(Constants.HOW_TO_USE)){
            skipTv.setVisibility(View.VISIBLE);
            nextIv.setVisibility(View.VISIBLE);
            submitRequestBtn.setVisibility(View.GONE);
            backIv.setVisibility(View.INVISIBLE);
        }
        else {
            skipTv.setVisibility(View.GONE);
            nextIv.setVisibility(View.GONE);
            submitRequestBtn.setVisibility(View.VISIBLE);
            backIv.setVisibility(View.VISIBLE);
        }
        if (localSettings.getLocale().equals(Constants.ARABIC)){
            useVp.setRotationY(180);
            indicatorTl.setRotationY(180);
        }

        noConnectionLl = findViewById(R.id.ll_no_connection);
        noConnectionTitleTv = findViewById(R.id.tv_no_connection_title);
        noConnectionMessageTv = findViewById(R.id.tv_no_connection_message);
        reloadBtn = findViewById(R.id.btn_reload_page);
        reloadBtn.setOnClickListener(this);
    }

    private void setFonts(){
        fonts = MApplication.getInstance().getFonts();
        skipTv.setTypeface(fonts.customFont());
        submitRequestBtn.setTypeface(fonts.customFontBD());

        noConnectionTitleTv.setTypeface(fonts.customFontBD());
        noConnectionMessageTv.setTypeface(fonts.customFont());
        reloadBtn.setTypeface(fonts.customFontBD());
    }

    private void setIndicatorMargins(){

        for(int i=0; i < indicatorTl.getTabCount(); i++) {

            View tab = ((ViewGroup) indicatorTl.getChildAt(0)).getChildAt(i);
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tab.getLayoutParams();
            p.setMargins(10,0,10,0);
            tab.requestLayout();
        }
    }

    private void switchToRegisterationOrLogin(){
        Intent registerationOrLoginIntent = new Intent(this,RegisterOrLoginActivity.class);
        startActivity(registerationOrLoginIntent);
        finish();
    }

    private void switchToDelegateRequest(){
        Intent delegateRequestIntent = new Intent(this,DelegateRequestActivity.class);
        startActivity(delegateRequestIntent);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;

            case R.id.tv_skip:

                if (fromSettings){
                    finish();
                }
                else {
                    switchToRegisterationOrLogin();
                }
                break;

            case R.id.iv_next:
                if (useVp.getCurrentItem() == pagerItems.size()-1){
                    useVp.setCurrentItem(0);
                }
                else {
                    useVp.setCurrentItem(useVp.getCurrentItem()+1);
                }
                break;

            case R.id.btn_submit_request:
                switchToDelegateRequest();
                break;

            case R.id.btn_reload_page:
                if (page.equals(Constants.HOW_TO_USE)){
                    howToUsePresenter.getHowToUseData(localSettings.getLocale());
                }
                else {
                    howToUsePresenter.getHowToBecomeADelegateData(localSettings.getLocale());
                }                break;
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
    public void showNoData() {
        Toast.makeText(this, R.string.no_data_error, Toast.LENGTH_LONG).show();
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
    public void showHowToUseData(ArrayList<Slider> howToUseData) {
        if (noConnectionLl.getVisibility() == View.VISIBLE) {
            noConnectionLl.setVisibility(View.GONE);
        }
        mainContentRl.setVisibility(View.VISIBLE);
        pagerItems.clear();
        pagerItems.addAll(howToUseData);
        adapter.notifyDataSetChanged();
        setIndicatorMargins();
    }
}


