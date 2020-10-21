package amaz.objects.TwentyfourSeven.ui.AddressSearch;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.compat.PlaceBufferResponse;
import com.google.android.libraries.places.compat.Places;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;

import java.util.concurrent.TimeUnit;

import amaz.objects.TwentyfourSeven.MApplication;
import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.adapters.AddressSearchAdapter;
import amaz.objects.TwentyfourSeven.listeners.OnLocationChangeListener;
import amaz.objects.TwentyfourSeven.ui.AddAddress.AddAddressActivity;
import amaz.objects.TwentyfourSeven.ui.MapSelectDestination.MapSelectDestinationActivity;
import amaz.objects.TwentyfourSeven.ui.StoreDetails.StoreDetailsActivity;
import amaz.objects.TwentyfourSeven.utilities.Constants;
import amaz.objects.TwentyfourSeven.utilities.Fonts;
import amaz.objects.TwentyfourSeven.utilities.GPSTracker;
import amaz.objects.TwentyfourSeven.utilities.LanguageUtilities;
import amaz.objects.TwentyfourSeven.utilities.LocalSettings;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class AddressSearchActivity extends AppCompatActivity implements View.OnClickListener,
        RecyclerView.OnItemTouchListener, OnLocationChangeListener {
    private EditText searchEt, nearByEt;
    private ImageView backIv, clearLocationIv, clearAddressIv, nearMeIv;
    private RecyclerView placesRv;
    private AddressSearchAdapter adapter;
    private RelativeLayout errorRl, nearByRl;
    private ImageView errorIv;
    private TextView errorTv, noConnectionTitleTv, noConnectionMessageTv;
    private Button reloadBtn;
    private LinearLayout noConnectionLl;
    private LocalSettings localSettings;
    private Fonts fonts;
    private GPSTracker tracker;
    private double latitude, longitude;
    private String countryNameCode;
    private boolean fromAddress;
    private String source;
    private boolean fromNearBy, fromSelection, pickupLocationSearch, isAddressSelected;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localSettings = new LocalSettings(this);
        setLanguage();
        setContentView(R.layout.activity_address_search);
        getDataFromIntent();
        initialization();
        setFonts();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void getDataFromIntent() {
        pickupLocationSearch = getIntent().getBooleanExtra(Constants.PICKUP_LOCATION_SEARCH, false);
        fromAddress = getIntent().getBooleanExtra(Constants.FROM_ADDRESS, false);
        if (!fromAddress) {
            latitude = getIntent().getDoubleExtra(Constants.LATITUDE, 0);
            longitude = getIntent().getDoubleExtra(Constants.LONGITUDE, 0);
        }
        countryNameCode = getIntent().getStringExtra(Constants.COUNTRY_NAME_CODE);
        source = getIntent().getStringExtra(Constants.SOURCE);
    }

    private void setLanguage() {
        if (localSettings.getLocale().equals(Constants.ARABIC)) {
            LanguageUtilities.switchToArabicLocale(this);
        }
    }

    private void initialization() {
        searchEt = findViewById(R.id.et_search);
        nearByEt = findViewById(R.id.et_nearby);
        nearMeIv = findViewById(R.id.iv_near_me);
        nearMeIv.setOnClickListener(this);
        if (!fromAddress && !pickupLocationSearch) {
            searchEt.setHint(R.string.search_name);
            nearByEt.setVisibility(View.VISIBLE);
            nearMeIv.setVisibility(View.VISIBLE);
        } else {
            searchEt.setHint(R.string.search_location);
            nearByEt.setVisibility(View.GONE);
            nearMeIv.setVisibility(View.GONE);
        }
        clearLocationIv = findViewById(R.id.iv_clear_location);
        clearLocationIv.setOnClickListener(this);
        clearAddressIv = findViewById(R.id.iv_clear_address);
        clearAddressIv.setOnClickListener(this);
        RxTextView.textChangeEvents(searchEt)
                .onBackpressureLatest()
                .debounce(250, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<TextViewTextChangeEvent>() {
                    @Override
                    public void call(TextViewTextChangeEvent textViewTextChangeEvent) {
                        try {
                            if (!isAddressSelected) {
                                adapter.setLatitude(latitude);
                                adapter.setLongitude(longitude);
                                //nearByEt.setText("");
                                nearByEt.setHint(R.string.near_by);
                            }

                            fromNearBy = false;
                            adapter.setFromNearBy(fromNearBy);
                            Log.e("charsec1", searchEt.getText().toString());
                            adapter.filterData(searchEt.getText().toString());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
        nearByRl = findViewById(R.id.rl_nearby);
        RxTextView.textChangeEvents(nearByEt)
                .onBackpressureLatest()
                .debounce(250, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<TextViewTextChangeEvent>() {
                    @Override
                    public void call(TextViewTextChangeEvent textViewTextChangeEvent) {
                        if (!fromSelection) {
                            fromNearBy = true;
                            adapter.setFromNearBy(fromNearBy);
                            Log.e("charsec2", searchEt.getText().toString());
                            adapter.filterData(nearByEt.getText().toString());
                        } else {
                            fromSelection = false;
                            if (!searchEt.getText().toString().isEmpty()) {
                                fromNearBy = false;
                                adapter.setFromNearBy(fromNearBy);
                                Log.e("charsec3", searchEt.getText().toString());
                                adapter.filterData(searchEt.getText().toString());
                            }
                        }

                        if (nearByEt.getText().toString().isEmpty()) {
                            clearAddressIv.setVisibility(View.GONE);
                            nearByRl.setBackgroundResource(R.drawable.dark_red_corners);
                            fromSelection = false;
                            if (!searchEt.getText().toString().isEmpty()) {
                                fromNearBy = false;
                                adapter.setFromNearBy(fromNearBy);
                                Log.e("charsec4", searchEt.getText().toString());
                                adapter.filterData(searchEt.getText().toString());
                            }
                        } else {
                            clearAddressIv.setVisibility(View.VISIBLE);
                            nearByRl.setBackgroundResource(R.drawable.white_corners);
                        }
                    }
                });
        backIv = findViewById(R.id.iv_back);
        if (localSettings.getLocale().equals(Constants.ARABIC)) {
            backIv.setImageResource(R.drawable.back_ar_ic);
        } else {
            backIv.setImageResource(R.drawable.back_ic);
        }
        backIv.setOnClickListener(this);
        placesRv = findViewById(R.id.rv_places);
        placesRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        if (fromAddress) {
            adapter = new AddressSearchAdapter(this, countryNameCode, fromAddress);
        } else if (pickupLocationSearch) {
            adapter = new AddressSearchAdapter(this, countryNameCode, pickupLocationSearch);
        } else {
            adapter = new AddressSearchAdapter(this, latitude, longitude, countryNameCode, fromAddress);
        }
        placesRv.setAdapter(adapter);
        placesRv.addOnItemTouchListener(this);
        errorRl = findViewById(R.id.rl_error);
        errorIv = findViewById(R.id.iv_error);
        errorTv = findViewById(R.id.tv_error);
        noConnectionLl = findViewById(R.id.ll_no_connection);
        noConnectionTitleTv = findViewById(R.id.tv_no_connection_title);
        noConnectionMessageTv = findViewById(R.id.tv_no_connection_message);
        reloadBtn = findViewById(R.id.btn_reload_page);
        reloadBtn.setOnClickListener(this);
        tracker = new GPSTracker(this, this, false);
        tracker.getLocation();
    }

    /*private synchronized void setUpGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }*/

    private void setFonts() {
        fonts = MApplication.getInstance().getFonts();
        searchEt.setTypeface(fonts.customFont());
        nearByEt.setTypeface(fonts.customFont());
        errorTv.setTypeface(fonts.customFont());
        noConnectionTitleTv.setTypeface(fonts.customFontBD());
        noConnectionMessageTv.setTypeface(fonts.customFont());
        reloadBtn.setTypeface(fonts.customFontBD());
    }

    private void hideKeyboard() {
        View focusedView = this.getCurrentFocus();
        if (focusedView != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
        }
    }

    public void showNoData() {
        noConnectionLl.setVisibility(View.GONE);
        errorRl.setVisibility(View.VISIBLE);
        errorIv.setImageResource(R.drawable.grayscale);
        errorTv.setText(R.string.no_data_error);
    }

    public void showNetworkError() {
        errorRl.setVisibility(View.GONE);
        noConnectionLl.setVisibility(View.VISIBLE);
    }

    public void hideNodata() {
        errorRl.setVisibility(View.GONE);
    }

    public void hideNetworkError() {
        noConnectionLl.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_clear_location:
                searchEt.setText("");
                break;
            case R.id.btn_reload_page:

                fromNearBy = false;
                adapter.setFromNearBy(fromNearBy);
                adapter.filterData(searchEt.getText().toString());

                break;
            case R.id.iv_near_me:
                fromNearBy = true;
                adapter.setFromNearBy(fromNearBy);
                adapter.setLatitude(latitude);
                adapter.setLongitude(longitude);
                fromSelection = true;
                nearByEt.setText("");
                nearByEt.setHint(R.string.near_by);
                break;
            case R.id.iv_clear_address:
                isAddressSelected = false;
                fromNearBy = true;
                adapter.setFromNearBy(fromNearBy);
                adapter.setLatitude(latitude);
                adapter.setLongitude(longitude);
                fromSelection = true;
                nearByEt.setText("");
                nearByEt.setHint(R.string.near_by);
                break;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull final RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
            if (child != null) {
                try {
                    final AddressSearchAdapter.PlaceAutocomplete placeAutoComplete = adapter.getItem(recyclerView.getChildLayoutPosition(child));
                    final String placeId = placeAutoComplete.getPlaceId().toString();
                    Task<PlaceBufferResponse> task = Places.getGeoDataClient(this)
                            .getPlaceById(placeId);

                    task.addOnSuccessListener(new OnSuccessListener<PlaceBufferResponse>() {
                        @Override
                        public void onSuccess(PlaceBufferResponse places) {
                            if (places.getCount() == 1) {
                                Log.d("lat", String.valueOf(places.get(0).getLatLng().latitude));
                                Log.d("lng", String.valueOf(places.get(0).getLatLng().longitude));
                                Log.e("source", source + "  " + AddAddressActivity.class.getSimpleName());
                                if (source.equals(AddAddressActivity.class.getSimpleName()) || source.equals(MapSelectDestinationActivity.class.getSimpleName())) {
                                    Intent result = new Intent();
                                    result.putExtra(Constants.LONGITUDE, places.get(0).getLatLng().longitude);
                                    result.putExtra(Constants.LATITUDE, places.get(0).getLatLng().latitude);
                                    result.putExtra(Constants.SELECTED_PLACE_DESCRIPTION, placeAutoComplete.getDescription());
                                    setResult(AppCompatActivity.RESULT_OK, result);
                                    hideKeyboard();
                                    finish();
                                } else {
                                    if (fromNearBy) {
                                        adapter.setLatitude(places.get(0).getLatLng().latitude);
                                        adapter.setLongitude(places.get(0).getLatLng().longitude);
                                        fromSelection = true;
                                        isAddressSelected = true;
                                        nearByEt.setText(placeAutoComplete.getDescription());
                                        nearByEt.clearFocus();

                                    } else {
                                        Intent storeDetailsIntent = new Intent(AddressSearchActivity.this, StoreDetailsActivity.class);
                                        storeDetailsIntent.putExtra(Constants.PLACE_ID, placeId);
                                        storeDetailsIntent.putExtra(Constants.LATITUDE, latitude);
                                        storeDetailsIntent.putExtra(Constants.LONGITUDE, longitude);
                                        hideKeyboard();
                                        startActivity(storeDetailsIntent);
                                    }
                                }
                            }
                            else {

                            }
                        }
                    });

                    return true;
                } catch (NullPointerException ignored) {
                    // Ignore this (see above) - since we're now in an unknown state let's clear all selection
                    // (which is still better than an arbitrary crash that we can't control):
                    placesRv.clearFocus();
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {
    }

    @Override
    public void setAddressData(String countryNameCode, String city, double latitude, double longitude) {
        //if (latitude != 0 && longitude != 0 && countryNameCode != null){
        this.latitude = latitude;
        this.longitude = longitude;
        this.countryNameCode = countryNameCode;
        adapter.setLatitude(latitude);
        adapter.setLongitude(longitude);
        adapter.setCountryNameCode(countryNameCode);
        //}
    }

    @Override
    public void showActivateGPS() {
    }

    @Override
    public void openLocationSettings() {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.LOCATION_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                tracker.getLocation();
            }
        }
    }
}
