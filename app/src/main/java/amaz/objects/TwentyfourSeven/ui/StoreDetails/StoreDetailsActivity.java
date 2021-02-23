package amaz.objects.TwentyfourSeven.ui.StoreDetails;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.MediaStoreSignature;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rey.material.widget.ProgressView;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import amaz.objects.TwentyfourSeven.BaseActivity;
import amaz.objects.TwentyfourSeven.MApplication;
import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.adapters.StoreWorkingHouresAdapter;
import amaz.objects.TwentyfourSeven.api.APIURLs;
import amaz.objects.TwentyfourSeven.data.models.Category;
import amaz.objects.TwentyfourSeven.data.models.OpeningHours;
import amaz.objects.TwentyfourSeven.data.models.Store;
import amaz.objects.TwentyfourSeven.data.models.StoreDetailsData;
import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import amaz.objects.TwentyfourSeven.presenter.PresenterFactory;
import amaz.objects.TwentyfourSeven.ui.RegisterOrLogin.RegisterOrLoginActivity;
import amaz.objects.TwentyfourSeven.ui.RequestFromStore.RequestFromStoreActivity;
import amaz.objects.TwentyfourSeven.utilities.AreasUtility;
import amaz.objects.TwentyfourSeven.utilities.Constants;
import amaz.objects.TwentyfourSeven.utilities.Fonts;
import amaz.objects.TwentyfourSeven.utilities.LanguageUtilities;
import amaz.objects.TwentyfourSeven.utilities.LocalSettings;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;

public class StoreDetailsActivity extends BaseActivity implements View.OnClickListener,
        StoreDetailsPresenter.StoreDetailsView {

    private SupportMapFragment mapFragment;
    private TextView storeTitleTv, storeNameTv, storeAddressTv, openNowTv, distanceTv, workingHouresTv;
    private ImageView backIv, shareIv, storeImageIv, expandCollapseIv;
    private ExpandableLayout workingHouresEl;
    private RecyclerView workingHouresRv;
    private ArrayList<String> workingHouresList = new ArrayList<>();
    private StoreWorkingHouresAdapter adapter;
    private Button requestFromStoresBtn;
    private ProgressView loaderPv;
    private RelativeLayout mainContentRl;

    private LocalSettings localSettings;
    private Fonts fonts;

    private Store store;
    private double latitude, longitude;
    private String categoryImage;

    private String placeId;

    private boolean isExpanded = false;
    private LatLng currentLocation;
    private LatLng storeLocation;

    private StoreDetailsPresenter storeDetailsPresenter;

    @Override
    public void onPresenterAvailable(BasePresenter presenter) {
        storeDetailsPresenter = (StoreDetailsPresenter) presenter;
        storeDetailsPresenter.setView(this);
        if (workingHouresList.isEmpty()) {
            if (placeId == null) {
                if (store != null) {
                    storeDetailsPresenter.getStoreWorkingHoures(store.getPlaceId(), "opening_hours", localSettings.getLocale(),
                            getString(R.string.google_server_key));
                }
            } else {
                storeDetailsPresenter.getStoreWorkingHoures(placeId, "name,opening_hours,geometry,icon,vicinity,formatted_address,type,photo", localSettings.getLocale(),
                        getString(R.string.google_server_key));
            }
        }
    }

    @Override
    public PresenterFactory.PresenterType getPresenterType() {
        return PresenterFactory.PresenterType.StoreDetails;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localSettings = new LocalSettings(this);
        setLanguage();
        setContentView(R.layout.activity_store_details);
        getDataFromIntent();
        initialization();
        setFonts();

        //setContentView(R.layout.activity_store_details);
        //getDataFromIntent();
        //initialization();
        /*if (store != null) {
            setMap(store.getGeometry().getLocation().getLatitude(), store.getGeometry().getLocation().getLongitude());
        }
        setStoreData();*/
        //setFonts();

    }

    @Override
    protected void onStart() {
        super.onStart();
        Branch branch = Branch.getInstance();
        // Branch init
        branch.initSession(new Branch.BranchReferralInitListener() {
            @Override
            public void onInitFinished(JSONObject referringParams, BranchError error) {
                if (error == null) {
                    // params are the deep linked params associated with the link that the user clicked -> was re-directed to this app
                    // params will be empty if no data found
                    // ... insert custom logic here ...
                    Log.e("BRANCH SDK", referringParams.toString());
                    try {
                        placeId = referringParams.getString("id");
                        if (storeDetailsPresenter != null) {
                            storeDetailsPresenter.getStoreWorkingHoures(placeId, "name,opening_hours,geometry,icon,vicinity,formatted_address,type,photo", localSettings.getLocale(),
                                    getString(R.string.google_server_key));

                        }

                        Log.e("placeid", placeId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        if (store != null) {
                            setMap(store.getGeometry().getLocation().getLatitude(), store.getGeometry().getLocation().getLongitude());
                        }
                        setStoreData();
                    }
                } else {
                    Log.e("BRANCH SDK", error.getMessage());
                }
            }
        }, this.getIntent().getData(), this);
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.setIntent(intent);
    }

    private void setLanguage() {
        if (localSettings.getLocale().equals(Constants.ARABIC)) {
            LanguageUtilities.switchToArabicLocale(this);
        }
    }

    private void getDataFromIntent() {
        /*Intent intent = getIntent();
        Uri data = intent.getData();
        Log.e("uri data",data.toString());
        if (data != null) {
            placeId = data.getQueryParameter("$id");
        }
        else {*/
        if (this.placeId == null) {
            this.placeId = getIntent().getStringExtra(Constants.PLACE_ID);
        }
        this.latitude = getIntent().getDoubleExtra(Constants.LATITUDE, 0);
        this.longitude = getIntent().getDoubleExtra(Constants.LONGITUDE, 0);
        if (placeId == null) {
            this.store = (Store) getIntent().getSerializableExtra(Constants.STORE);
            this.categoryImage = getIntent().getStringExtra(Constants.CATEGORY);
        }
        //}

    }

    private void initialization() {
        storeTitleTv = findViewById(R.id.tv_store_title);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fr_map);
        backIv = findViewById(R.id.iv_back);
        backIv.setOnClickListener(this);
        if (localSettings.getLocale().equals(Constants.ARABIC)) {
            backIv.setImageResource(R.drawable.back_ar_ic);
        } else {
            backIv.setImageResource(R.drawable.back_ic);
        }
        shareIv = findViewById(R.id.iv_share);
        shareIv.setOnClickListener(this);
        storeImageIv = findViewById(R.id.iv_store_image);
        storeNameTv = findViewById(R.id.tv_store_name);
        storeAddressTv = findViewById(R.id.tv_store_address);
        openNowTv = findViewById(R.id.tv_open_now);
        distanceTv = findViewById(R.id.tv_distance);
        expandCollapseIv = findViewById(R.id.iv_expand_collapse);
        expandCollapseIv.setOnClickListener(this);
        workingHouresEl = findViewById(R.id.el_working_houres);
        workingHouresRv = findViewById(R.id.rv_working_houres);
        workingHouresRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new StoreWorkingHouresAdapter(this, workingHouresList);
        workingHouresRv.setAdapter(adapter);
        workingHouresTv = findViewById(R.id.tv_working_hours);
        workingHouresTv.setOnClickListener(this);
        requestFromStoresBtn = findViewById(R.id.btn_request_from_stores);
        requestFromStoresBtn.setOnClickListener(this);
        workingHouresEl.setVisibility(View.GONE);
        workingHouresTv.setVisibility(View.GONE);
        expandCollapseIv.setVisibility(View.GONE);
        loaderPv = findViewById(R.id.pv_load);
        mainContentRl = findViewById(R.id.rl_main_content);
        mainContentRl.setVisibility(View.GONE);
        showLoading();
    }

    private void setStoreData() {
        if (placeId == null) {
            mainContentRl.setVisibility(View.VISIBLE);
            hideLoading();
            storeTitleTv.setText("\u200E" + store.getName() + "\u200E");
            //Picasso.with(this).load(categoryImage).placeholder(R.drawable.grayscale).into(storeImageIv);
            if (!StoreDetailsActivity.this.isFinishing()) {
                Glide.with(this)
                        .load(APIURLs.BASE_URL + APIURLs.STORE_IMAGES + store.getPlaceId())
                        .signature(new MediaStoreSignature("", System.currentTimeMillis(), 0))
                        .apply(new RequestOptions().placeholder(R.drawable.grayscale).dontAnimate())
                        .error(Glide.with(this).load(categoryImage).apply(new RequestOptions().placeholder(R.drawable.grayscale).dontAnimate()))
                        .into(storeImageIv);

//            if (store.getPlacePhotos() != null && !store.getPlacePhotos().isEmpty()) {
//                Picasso.with(this)
//                        .load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=800&photoreference=" + store.getPlacePhotos().get(0).getPhotoReference()
//                                + "&key=" + getString(R.string.google_server_key))
//                        .placeholder(R.drawable.grayscale)
//                        .error(R.drawable.grayscale)
//                        .resize(40, 40)
//                        .into(storeImageIv);
//            } else {
//                Picasso.with(this).load(categoryImage).placeholder(R.drawable.grayscale).into(storeImageIv);
//            }
            }

            storeNameTv.setText("\u200E" + store.getName() + "\u200E");
            storeAddressTv.setText("\u200E" + store.getAddress() + "\u200E");

            if (store.getOpeningHours() != null) {
                if (store.getOpeningHours().isOpenNow() != null) {
                    if (store.getOpeningHours().isOpenNow()) {
                        openNowTv.setTextColor(ContextCompat.getColor(this, R.color.light_green));
                        openNowTv.setText(R.string.open_now);
                    } else {
                        openNowTv.setTextColor(ContextCompat.getColor(this, R.color.app_color));
                        openNowTv.setText(R.string.closed);
                    }
                }

            }


            Location storeLocation = new Location("storeLocation");
            storeLocation.setLatitude(store.getGeometry().getLocation().getLatitude());
            storeLocation.setLongitude(store.getGeometry().getLocation().getLongitude());

            Location currentLocation = new Location("currentLocation");
            currentLocation.setLatitude(latitude);
            currentLocation.setLongitude(longitude);

            float distance = currentLocation.distanceTo(storeLocation);
            if (distance < 1000) {
                distanceTv.setText((int) distance + " " + getString(R.string.meter));
            } else {
                distanceTv.setText((int) (distance / 1000) + " " + getString(R.string.kilometer));
            }

            Log.e("store_id", store.getPlaceId());
        }
    }

    private void setFonts() {
        fonts = MApplication.getInstance().getFonts();
        storeTitleTv.setTypeface(fonts.customFontBD());
        storeNameTv.setTypeface(fonts.customFont());
        storeAddressTv.setTypeface(fonts.customFont());
        openNowTv.setTypeface(fonts.customFont());
        distanceTv.setTypeface(fonts.customFont());
        workingHouresTv.setTypeface(fonts.customFontBD());
        requestFromStoresBtn.setTypeface(fonts.customFontBD());
    }

    private void setMap(double storeLat, double storeLng) {
        final LatLngBounds bounds;
        storeLocation = new LatLng(storeLat, storeLng);
        if (latitude != 0 && longitude != 0) {
            currentLocation = new LatLng(latitude, longitude);
            final LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(storeLocation);
            builder.include(currentLocation);
            bounds = builder.build();
        } else {
            bounds = null;
        }

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap googleMap) {
                googleMap.getUiSettings().setScrollGesturesEnabled(true);
                googleMap.clear();
                if (latitude != 0 && longitude != 0) {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    googleMap.setMyLocationEnabled(true);
                }

                googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                googleMap.getUiSettings().setRotateGesturesEnabled(false);
                if (currentLocation != null) {
                    if(localSettings.getLocale().equals(Constants.ARABIC)){
                        googleMap.addMarker(new MarkerOptions().position(currentLocation).icon(BitmapDescriptorFactory.fromResource(R.drawable.btn_blue_ar)));
                    }
                    else {
                        googleMap.addMarker(new MarkerOptions().position(currentLocation).icon(BitmapDescriptorFactory.fromResource(R.drawable.btn_blue_en)));
                    }
                }
                if(localSettings.getLocale().equals(Constants.ARABIC)){
                    googleMap.addMarker(new MarkerOptions().position(storeLocation).icon(BitmapDescriptorFactory.fromResource(R.drawable.btn_red_ar)));
                }
                else {
                    googleMap.addMarker(new MarkerOptions().position(storeLocation).icon(BitmapDescriptorFactory.fromResource(R.drawable.btn_red_en)));
                }
                if (currentLocation != null) {
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 17));
                }
                googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {
                        if (bounds != null) {
                            try{
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 270));
                            } catch (IllegalStateException ex){
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 200));
                            }
                            //googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 270));
                        } else {
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(storeLocation, 17));
                        }
                    }
                });

                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        String uriBegin = "geo:" + marker.getPosition().latitude + "," + marker.getPosition().longitude;
                        String query = marker.getPosition().latitude + "," + marker.getPosition().longitude;
                        String encodedQuery = Uri.encode(query);

                        String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                        Uri uri = Uri.parse(uriString);
                        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                        startActivity(intent);
                        return true;
                    }
                });


            }
        });


    }

    private void showWorkingHoursDialog() {
        workingHouresEl.toggle(true);
        if (isExpanded) {
            isExpanded = false;
            expandCollapseIv.setImageResource(R.drawable.anchor_upward);

        } else {
            isExpanded = true;
            expandCollapseIv.setImageResource(R.drawable.anchor_downward);
        }

    }

    private void switchToRequestFromStore() {
        Intent intent = new Intent(this, RequestFromStoreActivity.class);
        intent.putExtra(Constants.STORE, store);
        //intent.putExtra(Constants.LATITUDE,latitude);
        //intent.putExtra(Constants.LONGITUDE,longitude);
        intent.putExtra(Constants.CATEGORY, categoryImage);
        startActivity(intent);
    }

    private void switchToRegisterationOrLogin() {
        Intent registerationOrLoginIntent = new Intent(this, RegisterOrLoginActivity.class);
        registerationOrLoginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(registerationOrLoginIntent);
    }

    public void openSharePopup() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        if (store != null) {
            Log.e("link","https://247dev.app.link/bKw3Z6YxIV?$id=" + store.getPlaceId());
            shareIntent.putExtra(Intent.EXTRA_TEXT, "https://247dev.app.link/bKw3Z6YxIV?id=" + store.getPlaceId());
        } else {
            Log.e("link","https://247dev.app.link/bKw3Z6YxIV?$id=" + placeId);
            shareIntent.putExtra(Intent.EXTRA_TEXT, "https://247dev.app.link/bKw3Z6YxIV?id=" + placeId);
        }
        startActivity(Intent.createChooser(shareIntent, "Share link!"));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;

            case R.id.iv_share:
                openSharePopup();
                break;
            case R.id.iv_expand_collapse:
                showWorkingHoursDialog();
                break;

            case R.id.tv_working_hours:
                showWorkingHoursDialog();
                break;

            case R.id.btn_request_from_stores:
                if (localSettings.getToken() != null) {

                    LatLng pointToSEarch = new LatLng(storeLocation.latitude, storeLocation.longitude);
                    if (!AreasUtility.checkLocationInBlockedArea(this, pointToSEarch)) {
                        switchToRequestFromStore();
                    } else {
                        Toast.makeText(this, R.string.place_not_allow, Toast.LENGTH_LONG).show();

                    }
                } else {

                    switchToRegisterationOrLogin();
                }
                break;
        }
    }

    private void getStoreCategoryIcon(ArrayList<String> types) {
        ArrayList<Category> categories = localSettings.getCategories();
        boolean typeIsFound = false;
        if (categories != null) {
            for (int i = 0; i < categories.size(); i++) {
                if (types != null && !types.isEmpty()) {
                    if (types.get(0).equals(categories.get(i).getRelatedCategory())) {
                        typeIsFound = true;
                        if(!StoreDetailsActivity.this.isFinishing()){
                            //Picasso.with(this).load(categories.get(i).getIcon().getMedium()).placeholder(R.drawable.grayscale).into(storeImageIv);
                            Glide.with(this)
                                    .load(APIURLs.BASE_URL+APIURLs.STORE_IMAGES+store.getPlaceId())
                                    .signature(new MediaStoreSignature("", System.currentTimeMillis(),0))
                                    .apply(new RequestOptions().placeholder(R.drawable.grayscale).dontAnimate())
                                    .error(Glide.with(this).load(categoryImage).apply(new RequestOptions().placeholder(R.drawable.grayscale).dontAnimate()))
                                    .into(storeImageIv);
                        }
                        this.categoryImage = categories.get(i).getIcon().getSmall();
                        break;
                    }
                }
            }
        } else {
            if (localSettings.getDefaultCategory() != null) {
                if(!StoreDetailsActivity.this.isFinishing()){
                    //Picasso.with(this).load(localSettings.getDefaultCategory().getIcon().getMedium()).placeholder(R.drawable.grayscale).into(storeImageIv);
                    Glide.with(this)
                            .load(APIURLs.BASE_URL+APIURLs.STORE_IMAGES+store.getPlaceId())
                            .signature(new MediaStoreSignature("", System.currentTimeMillis(),0))
                            .apply(new RequestOptions().placeholder(R.drawable.grayscale).dontAnimate())
                            .error(Glide.with(this).load(categoryImage).apply(new RequestOptions().placeholder(R.drawable.grayscale).dontAnimate()))
                            .into(storeImageIv);
                }
            }
        }

        if (!typeIsFound) {
            if (localSettings.getDefaultCategory() != null) {
                if(!StoreDetailsActivity.this.isFinishing()){
                    //Picasso.with(this).load(localSettings.getDefaultCategory().getIcon().getMedium()).placeholder(R.drawable.grayscale).into(storeImageIv);
                    Glide.with(this)
                            .load(APIURLs.BASE_URL+APIURLs.STORE_IMAGES+store.getPlaceId())
                            .signature(new MediaStoreSignature("", System.currentTimeMillis(),0))
                            .apply(new RequestOptions().placeholder(R.drawable.grayscale).dontAnimate())
                            .error(Glide.with(this).load(categoryImage).apply(new RequestOptions().placeholder(R.drawable.grayscale).dontAnimate()))
                            .into(storeImageIv);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (workingHouresEl.isExpanded()) {
            workingHouresEl.collapse();
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public void showLoading() {
        if (placeId != null) {
            mainContentRl.setVisibility(View.GONE);
            loaderPv.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    @Override
    public void hideLoading() {
        if (placeId != null) {
            mainContentRl.setVisibility(View.VISIBLE);
            loaderPv.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    @Override
    public void hideWorkingHoures() {
        workingHouresEl.setVisibility(View.GONE);
        workingHouresTv.setVisibility(View.GONE);
        expandCollapseIv.setVisibility(View.GONE);
    }

    @Override
    public void showWorkingHoures(OpeningHours openingHours) {
        workingHouresEl.setVisibility(View.VISIBLE);
        workingHouresTv.setVisibility(View.VISIBLE);
        expandCollapseIv.setVisibility(View.VISIBLE);
        workingHouresList.clear();
        workingHouresList.addAll(openingHours.getWeakDays());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showStoreDetails(StoreDetailsData data) {
        if (placeId != null) {
            this.store = new Store(placeId, data.getName(), null, data.getGeometry(),
                    data.getOpeningHours(), data.getVicinity(), data.getPlacePhotos());
            setMap(data.getGeometry().getLocation().getLatitude(), data.getGeometry().getLocation().getLongitude());
            storeTitleTv.setText("\u200E" + data.getName() + "\u200E");
            getStoreCategoryIcon(data.getTypes());
//            if(store.getPlacePhotos() != null && !store.getPlacePhotos().isEmpty()){
//                Picasso.with(this)
//                        .load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=800&photoreference="+store.getPlacePhotos().get(0).getPhotoReference()
//                                +"&key="+getString(R.string.google_server_key))
//                        .placeholder(R.drawable.grayscale)
//                        .error(R.drawable.grayscale)
//                        .resize(50,50)
//                        .into(storeImageIv);
//            }
//            else {
//                getStoreCategoryIcon(data.getTypes());
//            }
            storeNameTv.setText("\u200E" + data.getName() + "\u200E");
            if (data.getVicinity() != null) {
                storeAddressTv.setText("\u200E" + data.getVicinity() + "\u200E");
            }
            if (data.getOpeningHours() != null) {
                if (data.getOpeningHours().isOpenNow() != null) {
                    if (data.getOpeningHours().isOpenNow()) {
                        openNowTv.setTextColor(ContextCompat.getColor(this, R.color.light_green));
                        openNowTv.setText(R.string.open_now);
                    } else {
                        openNowTv.setTextColor(ContextCompat.getColor(this, R.color.app_color));
                        openNowTv.setText(R.string.closed);
                    }
                }
            }

            if (latitude != 0 && longitude != 0) {
                Location storeLocation = new Location("storeLocation");
                storeLocation.setLatitude(data.getGeometry().getLocation().getLatitude());
                storeLocation.setLongitude(data.getGeometry().getLocation().getLongitude());

                Location currentLocation = new Location("currentLocation");
                currentLocation.setLatitude(latitude);
                currentLocation.setLongitude(longitude);

                float distance = currentLocation.distanceTo(storeLocation);
                if (distance < 1000) {
                    distanceTv.setText((int) distance + " " + getString(R.string.meter));
                } else {
                    distanceTv.setText((int) (distance / 1000) + " " + getString(R.string.kilometer));
                }
            }
        }
    }

}
