package amaz.objects.TwentyfourSeven.ui.CategoryStores;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rey.material.widget.ProgressView;

import amaz.objects.TwentyfourSeven.BaseActivity;
import amaz.objects.TwentyfourSeven.MApplication;
import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.adapters.StoresAdapter;
import amaz.objects.TwentyfourSeven.api.APIURLs;
import amaz.objects.TwentyfourSeven.data.models.Category;
import amaz.objects.TwentyfourSeven.data.models.Store;
import amaz.objects.TwentyfourSeven.data.models.responses.NearPlacesResponse;
import amaz.objects.TwentyfourSeven.listeners.OnLocationChangeListener;
import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import amaz.objects.TwentyfourSeven.presenter.PresenterFactory;
import amaz.objects.TwentyfourSeven.ui.AddressSearch.AddressSearchActivity;
import amaz.objects.TwentyfourSeven.ui.AllNearestStores.AllNearestStoresActivity;
import amaz.objects.TwentyfourSeven.utilities.Constants;
import amaz.objects.TwentyfourSeven.utilities.Fonts;
import amaz.objects.TwentyfourSeven.utilities.GPSTracker;
import amaz.objects.TwentyfourSeven.utilities.LanguageUtilities;
import amaz.objects.TwentyfourSeven.utilities.LocalSettings;
import amaz.objects.TwentyfourSeven.utilities.NetworkUtilities;

import java.util.ArrayList;
import java.util.Properties;

public class CategoryStoresActivity extends BaseActivity implements CategoryStoresPresenter.NearestStoresView,
        View.OnClickListener, OnLocationChangeListener {

    private NestedScrollView mainSv;
    private RelativeLayout mainContentRl, noActiveStoresRl;
    private LinearLayout activateGpsLl, noConnectionLl;
    private TextView nearestStoresTitleTv, nearByTv, moreNearStoresTv, allActiveStoresTv, allStoresTv, activateGpsTitle, activateGpsMessage,
            noConnectionTitleTv, noConnectionMessageTv, noActiveStoresTv;

    private RecyclerView nearestStoresRv, activeStoresRv;
    private ImageView backIv, searchIv;
    private Button activateGpsBtn, reloadBtn;
    private ProgressView loaderPv;
    private ProgressBar loadMorePb;

    private StoresAdapter nearestStoresAdapter;
    private ArrayList<Store> nearestStoresList = new ArrayList<>();
    private StoresAdapter activeStoresAdapter;
    private ArrayList<Store> activeStoresList = new ArrayList<>();

    private LocalSettings localSettings;
    private Fonts fonts;

    private CategoryStoresPresenter categoryStoresPresenter;
    private Category category;
    private NearPlacesResponse nearPlacesResponse;

    private GPSTracker tracker;
    private double longitude, latitude;
    private String countryNameCode;

    private String nextPageToken;
    private boolean isNetworkCalled = false;

    @Override
    public void onPresenterAvailable(BasePresenter presenter) {
        categoryStoresPresenter = (CategoryStoresPresenter) presenter;
        categoryStoresPresenter.setView(this);
        if (!isNetworkCalled && latitude != 0 && longitude != 0){
            categoryStoresPresenter.getNearestStores(latitude, longitude, category.getRelatedCategory(), localSettings.getLocale(), "distance",
                    getString(R.string.google_server_key));
            categoryStoresPresenter.getOpenedStores(latitude, longitude, category.getRelatedCategory(), localSettings.getLocale(), "prominence", 5000, true,
                    getString(R.string.google_server_key));
            isNetworkCalled = true;
        }
    }

    @Override
    public PresenterFactory.PresenterType getPresenterType() {
        return PresenterFactory.PresenterType.NearestStores;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localSettings = new LocalSettings(this);
        setLanguage();
        setContentView(R.layout.activity_nearest_stores);
        getDataFromIntent();
        initialization();
        setFonts();
    }

    private void setLanguage() {
        if (localSettings.getLocale().equals(Constants.ARABIC)) {
            LanguageUtilities.switchToArabicLocale(this);
        }
    }

    private void getDataFromIntent() {
        this.category = (Category) getIntent().getSerializableExtra(Constants.CATEGORY);
    }

    private void initialization() {
        mainSv = findViewById(R.id.sv_main);
        mainContentRl = findViewById(R.id.rl_main_content);
        mainContentRl.setVisibility(View.GONE);
        nearestStoresTitleTv = findViewById(R.id.tv_stores_title);
        nearestStoresTitleTv.setText(category.getName());
        backIv = findViewById(R.id.iv_back);
        backIv.setOnClickListener(this);
        if (localSettings.getLocale().equals(Constants.ARABIC)) {
            backIv.setImageResource(R.drawable.back_ar_ic);
        } else {
            backIv.setImageResource(R.drawable.back_ic);
        }

        searchIv = findViewById(R.id.iv_search);
        searchIv.setOnClickListener(this);

        nearByTv = findViewById(R.id.tv_near_by);
        moreNearStoresTv = findViewById(R.id.tv_more_near_by);
        moreNearStoresTv.setOnClickListener(this);
        allActiveStoresTv = findViewById(R.id.tv_active_stores);
        allStoresTv = findViewById(R.id.tv_all_stores);
        allStoresTv.setOnClickListener(this);

        nearestStoresRv = findViewById(R.id.rv_nearest_stores);
        nearestStoresRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        nearestStoresRv.setNestedScrollingEnabled(false);
        nearestStoresAdapter = new StoresAdapter(this, nearestStoresList, category.getIcon().getSmall());
        nearestStoresRv.setAdapter(nearestStoresAdapter);

        activeStoresRv = findViewById(R.id.rv_active_stores);
        activeStoresRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        activeStoresRv.setNestedScrollingEnabled(false);
        activeStoresAdapter = new StoresAdapter(this, activeStoresList, category.getIcon().getSmall());
        activeStoresRv.setAdapter(activeStoresAdapter);

        activateGpsLl = findViewById(R.id.ll_activate_gps);
        activateGpsTitle = findViewById(R.id.tv_activate_gps_title);
        activateGpsMessage = findViewById(R.id.tv_activate_gps_message);
        activateGpsBtn = findViewById(R.id.btn_activate_gps);
        activateGpsBtn.setOnClickListener(this);

        noConnectionLl = findViewById(R.id.ll_no_connection);
        noConnectionTitleTv = findViewById(R.id.tv_no_connection_title);
        noConnectionMessageTv = findViewById(R.id.tv_no_connection_message);
        reloadBtn = findViewById(R.id.btn_reload_page);
        reloadBtn.setOnClickListener(this);

        noActiveStoresRl = findViewById(R.id.rl_error);
        noActiveStoresTv = findViewById(R.id.tv_error);

        loaderPv = findViewById(R.id.pv_load);
        loadMorePb = findViewById(R.id.pb_load_more);

        tracker = new GPSTracker(this, this, true);
        showLoading();
        activateGpsLl.setVisibility(View.GONE);
        tracker.getLocation();

        /*mainSv.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged()
            {
                View view = mainSv.getChildAt(mainSv.getChildCount() - 1);

                int diff = (view.getBottom() - (mainSv.getHeight() + mainSv
                        .getScrollY()));

                if (diff == 0) {
                    // your pagination code
                    if (nextPageToken!=null){
                        categoryStoresPresenter.getNextOpenedStores(nextPageToken, latitude, longitude, category.getRelatedCategory(), localSettings.getLocale(), "prominence", 5000, true, getString(R.string.google_server_key));

                    }
                }
            }
        });*/

    }


    private void setFonts() {
        fonts = MApplication.getInstance().getFonts();
        nearestStoresTitleTv.setTypeface(fonts.customFontBD());
        nearByTv.setTypeface(fonts.customFontBD());
        moreNearStoresTv.setTypeface(fonts.customFont());
        allActiveStoresTv.setTypeface(fonts.customFontBD());
        allStoresTv.setTypeface(fonts.customFont());
        activateGpsTitle.setTypeface(fonts.customFontBD());
        activateGpsMessage.setTypeface(fonts.customFont());
        activateGpsBtn.setTypeface(fonts.customFontBD());

        noConnectionTitleTv.setTypeface(fonts.customFontBD());
        noConnectionMessageTv.setTypeface(fonts.customFont());
        reloadBtn.setTypeface(fonts.customFontBD());
        noActiveStoresTv.setTypeface(fonts.customFont());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;

            case R.id.iv_search:
                switchToSearch();
                break;

            case R.id.btn_activate_gps:
                openLocationSettings();
                break;

            case R.id.btn_reload_page:
                showLoading();
                activateGpsLl.setVisibility(View.GONE);
                tracker.getLocation();
                break;

            case R.id.tv_more_near_by:
                switchToAllStores(true);
                break;

            case R.id.tv_all_stores:
                switchToAllStores(false);
                break;
        }

    }

    private void switchToAllStores(boolean isNearestStores) {
        Intent intent = new Intent(this, AllNearestStoresActivity.class);
        intent.putExtra(Constants.IS_NEAREST_STORES,isNearestStores);
        intent.putExtra(Constants.CATEGORY, category);
        intent.putExtra(Constants.LATITUDE, latitude);
        intent.putExtra(Constants.LONGITUDE, longitude);
        intent.putExtra(Constants.COUNTRY_NAME_CODE, countryNameCode);
        if (isNearestStores){
            intent.putExtra(Constants.STORES, nearPlacesResponse);
        }
        startActivity(intent);
    }

    private void switchToSearch() {
        Intent intent = new Intent(this, AddressSearchActivity.class);
        intent.putExtra(Constants.LATITUDE, latitude);
        intent.putExtra(Constants.LONGITUDE, longitude);
        intent.putExtra(Constants.COUNTRY_NAME_CODE, countryNameCode);
        intent.putExtra(Constants.FROM_ADDRESS, false);
        intent.putExtra(Constants.SOURCE, this.getClass().getSimpleName());
        startActivity(intent);
    }

    @Override
    public void setAddressData(String countryNameCode, String city, double latitude, double longitude) {
        //if (isFirstTime) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.countryNameCode = countryNameCode;

        //if (latitude != 0 && longitude != 0) {
            nearestStoresAdapter.setCurrentLatitude(latitude);
            nearestStoresAdapter.setCurrentLongitude(longitude);
            activeStoresAdapter.setCurrentLatitude(latitude);
            activeStoresAdapter.setCurrentLongitude(longitude);
            if (categoryStoresPresenter != null){
                categoryStoresPresenter.getNearestStores(latitude, longitude, category.getRelatedCategory(), localSettings.getLocale(), "distance",
                        getString(R.string.google_server_key));
                categoryStoresPresenter.getOpenedStores(latitude, longitude, category.getRelatedCategory(), localSettings.getLocale(), "prominence", 5000, true,
                        getString(R.string.google_server_key));
            }

        /*} else {
            if (NetworkUtilities.isInternetConnection(this)) {
                hideLoading();
                activateGpsLl.setVisibility(View.VISIBLE);
                noConnectionLl.setVisibility(View.GONE);

            } else {
                hideLoading();
                activateGpsLl.setVisibility(View.GONE);
                noConnectionLl.setVisibility(View.VISIBLE);
            }*/
        //}
        //}
    }

    @Override
    public void showActivateGPS() {
        hideLoading();
        activateGpsLl.setVisibility(View.VISIBLE);
        noConnectionLl.setVisibility(View.GONE);
    }

    @Override
    public void openLocationSettings() {
        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), Constants.LOCATION_SETTINGS_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.LOCATION_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showLoading();
                activateGpsLl.setVisibility(View.GONE);
                tracker.getLocation();
            } else {
                finish();
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.LOCATION_SETTINGS_REQUEST) {
            //if (resultCode == Activity.RESULT_OK){
                showLoading();
                activateGpsLl.setVisibility(View.GONE);
                tracker.getLocation();
            //}
            /*else {
                hideLoading();
                activateGpsLl.setVisibility(View.VISIBLE);
                noConnectionLl.setVisibility(View.GONE);
            }*/
        } else if (requestCode == Constants.CHECK_LOCATION_SETTINGS_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                showLoading();
                activateGpsLl.setVisibility(View.GONE);
                tracker.getLocation();
            }
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
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public void hideLoadMore() {
        loadMorePb.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public void showNetworkError() {
        mainContentRl.setVisibility(View.GONE);
        activateGpsLl.setVisibility(View.GONE);
        noConnectionLl.setVisibility(View.VISIBLE);
    }

    @Override
    public void showServerError() {
        Toast.makeText(this, R.string.server_error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showNoData() {
        noActiveStoresRl.setVisibility(View.VISIBLE);
    }

    @Override
    public void showStoresData(NearPlacesResponse nearPlacesResponse, String rankBy) {
        mainContentRl.setVisibility(View.VISIBLE);
        activateGpsLl.setVisibility(View.GONE);
        noConnectionLl.setVisibility(View.GONE);
        if (rankBy.equals("distance")) {
            if (nearPlacesResponse.getResults().isEmpty()){
                nearByTv.setVisibility(View.GONE);
                moreNearStoresTv.setVisibility(View.GONE);
                nearestStoresRv.setVisibility(View.GONE);
            }
            else {
                nearByTv.setVisibility(View.VISIBLE);
                moreNearStoresTv.setVisibility(View.VISIBLE);
                nearestStoresRv.setVisibility(View.VISIBLE);
                this.nearPlacesResponse = nearPlacesResponse;
                nearestStoresList.clear();
                for (int i = 0; i < nearPlacesResponse.getResults().size() && i <= 1; i++) {
                    nearestStoresList.add(nearPlacesResponse.getResults().get(i));
                }
                nearestStoresAdapter.notifyDataSetChanged();
            }

        } else {
            this.nextPageToken = nearPlacesResponse.getNextPageToken();
            activeStoresList.clear();
            activeStoresList.addAll(nearPlacesResponse.getResults());
            activeStoresAdapter.notifyDataSetChanged();
        }

    }
}
