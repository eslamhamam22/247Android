package amaz.objects.TwentyfourSeven.ui.AllNearestStores;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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

import amaz.objects.TwentyfourSeven.BaseActivity;
import amaz.objects.TwentyfourSeven.MApplication;
import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.adapters.StoresAdapter;
import amaz.objects.TwentyfourSeven.api.APIURLs;
import amaz.objects.TwentyfourSeven.data.models.Category;
import amaz.objects.TwentyfourSeven.data.models.Store;
import amaz.objects.TwentyfourSeven.data.models.responses.NearPlacesResponse;
import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import amaz.objects.TwentyfourSeven.presenter.PresenterFactory;
import amaz.objects.TwentyfourSeven.ui.AddressSearch.AddressSearchActivity;
import amaz.objects.TwentyfourSeven.ui.CategoryStores.CategoryStoresPresenter;
import amaz.objects.TwentyfourSeven.utilities.Constants;
import amaz.objects.TwentyfourSeven.utilities.Fonts;
import amaz.objects.TwentyfourSeven.utilities.LanguageUtilities;
import amaz.objects.TwentyfourSeven.utilities.LocalSettings;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE;

public class AllNearestStoresActivity extends BaseActivity implements CategoryStoresPresenter.NearestStoresView,
        View.OnClickListener {

    private TextView nearestStoresTitleTv, noConnectionTitleTv, noConnectionMessageTv;
    private RecyclerView nearestStoresRv;
    private ImageView backIv, searchIv;
    private ProgressView loaderPv;
    private ProgressBar loadMorePb;
    private LinearLayout noConnectionLl;
    private Button reloadBtn;

    private RelativeLayout errorRl;
    private ImageView errorIv;
    private TextView errorTv;


    private StoresAdapter nearestStoresAdapter;
    private ArrayList<Store> nearestStoresList = new ArrayList<>();

    private LocalSettings localSettings;
    private Fonts fonts;

    private CategoryStoresPresenter categoryStoresPresenter;

    private Category category;
    private NearPlacesResponse nearPlacesResponse;
    private double latitude,longitude;
    private String countryNameCode;
    private String nextPageToken;
    private boolean isNearestStores;


    @Override
    public void onPresenterAvailable(BasePresenter presenter) {
        categoryStoresPresenter = (CategoryStoresPresenter) presenter;
        categoryStoresPresenter.setView(this);
        if (!isNearestStores){
            categoryStoresPresenter.getAllStores(latitude, longitude, category.getRelatedCategory(), localSettings.getLocale(),"prominence", 5000, getString(R.string.google_server_key));
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
        setContentView(R.layout.activity_all_nearest_stores);
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
        this.isNearestStores = getIntent().getBooleanExtra(Constants.IS_NEAREST_STORES,false);
        this.category = (Category) getIntent().getSerializableExtra(Constants.CATEGORY);
        this.latitude = getIntent().getDoubleExtra(Constants.LATITUDE,0);
        this.longitude = getIntent().getDoubleExtra(Constants.LONGITUDE,0);
        this.countryNameCode = getIntent().getStringExtra(Constants.COUNTRY_NAME_CODE);
        if (isNearestStores){
            this.nearPlacesResponse = (NearPlacesResponse) getIntent().getSerializableExtra(Constants.STORES);
            this.nearestStoresList.addAll(this.nearPlacesResponse.getResults());
            this.nextPageToken = this.nearPlacesResponse.getNextPageToken();
        }
    }

    private void initialization() {
        nearestStoresTitleTv = findViewById(R.id.tv_stores_title);
        backIv = findViewById(R.id.iv_back);
        backIv.setOnClickListener(this);
        if (localSettings.getLocale().equals(Constants.ARABIC)) {
            backIv.setImageResource(R.drawable.back_ar_ic);
        } else {
            backIv.setImageResource(R.drawable.back_ic);
        }
        searchIv = findViewById(R.id.iv_search);
        searchIv.setOnClickListener(this);

        nearestStoresRv = findViewById(R.id.rv_nearest_stores);
        nearestStoresRv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        nearestStoresRv.setNestedScrollingEnabled(false);
        nearestStoresAdapter = new StoresAdapter(this,nearestStoresList, category.getIcon().getSmall());
        nearestStoresAdapter.setCurrentLongitude(longitude);
        nearestStoresAdapter.setCurrentLatitude(latitude);
        nearestStoresRv.setAdapter(nearestStoresAdapter);

        loaderPv = findViewById(R.id.pv_load);
        loadMorePb = findViewById(R.id.pb_load_more);

        noConnectionLl = findViewById(R.id.ll_no_connection);
        noConnectionTitleTv = findViewById(R.id.tv_no_connection_title);
        noConnectionMessageTv = findViewById(R.id.tv_no_connection_message);
        reloadBtn = findViewById(R.id.btn_reload_page);
        reloadBtn.setOnClickListener(this);

        errorRl = findViewById(R.id.rl_error);
        errorIv = findViewById(R.id.iv_error);
        errorTv = findViewById(R.id.tv_error);

        nearestStoresRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(!recyclerView.canScrollVertically(1) && newState == SCROLL_STATE_IDLE){
                    if (nextPageToken!=null){
                        if (isNearestStores){
                            categoryStoresPresenter.getNextNearStores(nextPageToken,
                                    latitude, longitude, category.getRelatedCategory(), localSettings.getLocale(),"distance",
                                    getString(R.string.google_server_key));
                        }
                        else {
                            categoryStoresPresenter.getNextAllStores(nextPageToken,
                                    latitude, longitude, category.getRelatedCategory(), localSettings.getLocale(),"prominence", 5000,
                                    getString(R.string.google_server_key));
                        }

                    }

                }
            }
        });

        if (isNearestStores){
            nearestStoresTitleTv.setText(R.string.all_nearest_stores);
        }
        else {
            nearestStoresTitleTv.setText(R.string.all_stores);
        }

    }

    private void setFonts() {
        fonts = MApplication.getInstance().getFonts();
        nearestStoresTitleTv.setTypeface(fonts.customFontBD());
        noConnectionTitleTv.setTypeface(fonts.customFontBD());
        noConnectionMessageTv.setTypeface(fonts.customFont());
        reloadBtn.setTypeface(fonts.customFontBD());
        errorTv.setTypeface(fonts.customFont());
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;

            case R.id.iv_search:
                switchToSearch();
                break;

            case R.id.btn_reload_page:
                if (!isNearestStores){
                    categoryStoresPresenter.getAllStores(latitude, longitude, category.getRelatedCategory(), localSettings.getLocale(),"prominence", 5000,
                            getString(R.string.google_server_key));
                }
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
        if (nextPageToken != null){
            Toast.makeText(this, R.string.connection_error, Toast.LENGTH_LONG).show();
        }
        else {
            nearestStoresRv.setVisibility(View.GONE);
            noConnectionLl.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void showServerError() {
        Toast.makeText(this, R.string.server_error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showNoData() {
        errorRl.setVisibility(View.VISIBLE);
        errorIv.setImageResource(R.drawable.no_active_stores);
        errorTv.setText(R.string.no_stores);
    }

    @Override
    public void showStoresData(NearPlacesResponse response, String rankBy) {
        nearestStoresRv.setVisibility(View.VISIBLE);
        noConnectionLl.setVisibility(View.GONE);
        this.nextPageToken = response.getNextPageToken();
        nearestStoresList.addAll(response.getResults());
        nearestStoresAdapter.notifyDataSetChanged();
        String filename = "myfile";
        String fileContents = "Hello world!";
        File file = new File(this.getFilesDir(), filename);
        try (FileOutputStream fos = this.openFileOutput(filename, Context.MODE_PRIVATE)) {
            fos.write(fileContents.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
