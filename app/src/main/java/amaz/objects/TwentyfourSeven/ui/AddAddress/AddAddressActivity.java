package amaz.objects.TwentyfourSeven.ui.AddAddress;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.rey.material.widget.ProgressView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import amaz.objects.TwentyfourSeven.BaseActivity;
import amaz.objects.TwentyfourSeven.MApplication;
import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.injection.Injection;
import amaz.objects.TwentyfourSeven.listeners.OnLocationChangeListener;
import amaz.objects.TwentyfourSeven.listeners.OnRefeshTokenResponse;
import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import amaz.objects.TwentyfourSeven.presenter.PresenterFactory;
import amaz.objects.TwentyfourSeven.ui.AddressSearch.AddressSearchActivity;
import amaz.objects.TwentyfourSeven.ui.RegisterOrLogin.RegisterOrLoginActivity;
import amaz.objects.TwentyfourSeven.utilities.Constants;
import amaz.objects.TwentyfourSeven.utilities.Fonts;
import amaz.objects.TwentyfourSeven.utilities.GPSTracker;
import amaz.objects.TwentyfourSeven.utilities.LanguageUtilities;
import amaz.objects.TwentyfourSeven.utilities.LocalSettings;
import amaz.objects.TwentyfourSeven.utilities.TokenUtilities;

public class AddAddressActivity extends BaseActivity implements View.OnClickListener, OnLocationChangeListener,
        AddAddressPresenter.AddAddressView , OnRefeshTokenResponse {

    private TextView searchedAddressTv, addressNameTv, pinNoteTv;
    private ImageView backIv, currentLocationIv, markerIv;
    private LinearLayout markerLl;
    private SupportMapFragment mapFragment;
    private Button addAddressBtn;
    private ProgressView loaderPv;
    private GPSTracker tracker;
    private double longitude, latitude;
    private String countryNameCode;
    private String selectedAddress;
    private LatLng currentLocation;
    private boolean fromMove = false;
    private boolean fromCurrentLocation = false;
    private boolean isAnimated = false;

    private LocalSettings localSettings;
    private Fonts fonts;

    private AddAddressPresenter addAddressPresenter;

    @Override
    public void onPresenterAvailable(BasePresenter presenter) {
        addAddressPresenter = (AddAddressPresenter) presenter;
        addAddressPresenter.setView(this);
    }

    @Override
    public PresenterFactory.PresenterType getPresenterType() {
        return PresenterFactory.PresenterType.AddAddress;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localSettings = new LocalSettings(this);
        setLanguage();
        setContentView(R.layout.activity_add_address);
        initialization();
        setFonts();
    }

    private void setLanguage() {
        if (localSettings.getLocale().equals(Constants.ARABIC)) {
            LanguageUtilities.switchToArabicLocale(this);
        }
    }

    private void initialization() {
        backIv = findViewById(R.id.iv_back);
        if (localSettings.getLocale().equals(Constants.ARABIC)) {
            backIv.setImageResource(R.drawable.back_ar_ic);
        } else {
            backIv.setImageResource(R.drawable.back_ic);
        }
        backIv.setOnClickListener(this);
        searchedAddressTv = findViewById(R.id.tv_searched_address);
        searchedAddressTv.setOnClickListener(this);

        markerLl = findViewById(R.id.ll_marker);
        pinNoteTv = findViewById(R.id.tv_pin_note);
        markerIv = findViewById(R.id.iv_marker);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fr_map);

        currentLocationIv = findViewById(R.id.iv_current_location);
        currentLocationIv.setOnClickListener(this);

        addressNameTv = findViewById(R.id.tv_address_name);
        addressNameTv.setVisibility(View.INVISIBLE);
        addressNameTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().isEmpty()){
                    addAddressBtn.setAlpha((float) 0.5);
                    addAddressBtn.setEnabled(false);
                }
                else {
                    addAddressBtn.setAlpha(1);
                    addAddressBtn.setEnabled(true);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        addAddressBtn = findViewById(R.id.btn_add_address);
        addAddressBtn.setEnabled(false);
        addAddressBtn.setOnClickListener(this);
        loaderPv = findViewById(R.id.pv_load);

        tracker = new GPSTracker(this, this, false);
        tracker.getLocation();
    }

    private void setFonts() {
        fonts = MApplication.getInstance().getFonts();
        searchedAddressTv.setTypeface(fonts.customFont());
        pinNoteTv.setTypeface(fonts.customFont());
        addressNameTv.setTypeface(fonts.customFont());
        addAddressBtn.setTypeface(fonts.customFontBD());
    }

    private void setMap() {
        //if (this.latitude != 0.0 && this.longitude != 0.0) {
            currentLocation = new LatLng(latitude, longitude);
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(final GoogleMap googleMap) {
                    googleMap.clear();
                    googleMap.setMyLocationEnabled(true);
                    googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                    googleMap.getUiSettings().setRotateGesturesEnabled(false);
                    googleMap.getUiSettings().setScrollGesturesEnabled(true);
                    Log.e("omnia","moveCamera");
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 17));
                    googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                        @Override
                        public void onCameraIdle() {
                            pinNoteTv.setText(R.string.add_address_release_note);
                            markerLl.animate().setDuration(100).translationY(0).setListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animator) {

                                }

                                @Override
                                public void onAnimationEnd(Animator animator) {
                                    isAnimated = false;
                                }

                                @Override
                                public void onAnimationCancel(Animator animator) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animator) {

                                }
                            });
                            if (fromMove || fromCurrentLocation) {
                                Log.e("omnia","fromMove");
                                currentLocation = googleMap.getCameraPosition().target;
                                String addressLine = getAddressLine(currentLocation.latitude, currentLocation.longitude);
                                if (addressLine.isEmpty()){
                                    if (selectedAddress != null && !selectedAddress.isEmpty()){
                                        searchedAddressTv.setText(selectedAddress);
                                    }
                                }
                                else {
                                    searchedAddressTv.setText(addressLine);
                                }

                                if (!searchedAddressTv.getText().toString().isEmpty()){
                                    addressNameTv.setVisibility(View.VISIBLE);
                                    //addAddressBtn.setVisibility(View.VISIBLE);
                                }
                                else {
                                    addressNameTv.setVisibility(View.INVISIBLE);
                                    //addAddressBtn.setVisibility(View.INVISIBLE);
                                }
                                selectedAddress = "";
                                fromMove = false;
                                fromCurrentLocation = false;
                            }

                        }
                    });

                    googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
                        @Override
                        public void onCameraMove() {
                            pinNoteTv.setText(R.string.add_address_move_note);
                            fromMove = true;
                            if (!isAnimated) {
                                markerLl.animate().setDuration(100).translationY(-50).setListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animator) {
                                        isAnimated = true;
                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animator) {

                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animator) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animator) {

                                    }
                                });
                            }
                        }
                    });

                    googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng latLng) {
                            hideKeyboard();
                        }
                    });
                }
            });
        //}
    }

    private String getAddressLine(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, new Locale(localSettings.getLocale()));
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            String addressLine = "";
            if (addresses != null && addresses.size() > 0) {
                if (addresses.get(0).getSubThoroughfare() != null) {
                    addressLine = addresses.get(0).getSubThoroughfare();
                }

                if (addresses.get(0).getThoroughfare() != null) {
                    addressLine = addressLine + " " + addresses.get(0).getThoroughfare();
                }
                return addressLine;
            }
        } catch (IOException e) {
            return "";
        }
        return "";
    }

    private void switchToAddressSearch(){
        Intent addressSearchIntent = new Intent(this,AddressSearchActivity.class);
        addressSearchIntent.putExtra(Constants.COUNTRY_NAME_CODE,countryNameCode);
        addressSearchIntent.putExtra(Constants.FROM_ADDRESS, true);
        addressSearchIntent.putExtra(Constants.SOURCE, this.getClass().getSimpleName());
        startActivityForResult(addressSearchIntent,Constants.ADDRESS_SEARCH_REQUEST);
    }

    private void switchToRegisterationOrLogin(){
        Intent registerationOrLoginIntent = new Intent(this,RegisterOrLoginActivity.class);
        registerationOrLoginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(registerationOrLoginIntent);
    }

    private void hideKeyboard() {
        View focusedView = this.getCurrentFocus();
        if (focusedView != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                hideKeyboard();
                finish();
                break;

            case R.id.iv_current_location:
                fromCurrentLocation = true;
                tracker.getLocation();
                break;

            case R.id.tv_searched_address:
                hideKeyboard();
                switchToAddressSearch();
                break;

            case R.id.btn_add_address:
                hideKeyboard();
                addAddressPresenter.addAddress(localSettings.getToken(),localSettings.getLocale(),addressNameTv.getText().toString(),searchedAddressTv.getText().toString(),this.latitude,this.longitude);
                break;
        }
    }

    @Override
    public void setAddressData(String countryNameCode, String city, double latitude, double longitude) {
        //Log.e("countryNameCode",countryNameCode);
        this.countryNameCode = countryNameCode;
        this.latitude = latitude;
        this.longitude = longitude;
        setMap();
    }

    @Override
    public void showActivateGPS() {

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
                tracker.getLocation();
            } else {

            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.LOCATION_SETTINGS_REQUEST) {
            tracker.getLocation();
        }
        else if (requestCode == Constants.CHECK_LOCATION_SETTINGS_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                tracker.getLocation();
            } else {

            }
        }
        else if (requestCode == Constants.ADDRESS_SEARCH_REQUEST){
            if (resultCode == Activity.RESULT_OK){
                this.latitude = data.getDoubleExtra(Constants.LATITUDE,0);
                this.longitude = data.getDoubleExtra(Constants.LONGITUDE,0);
                this.selectedAddress = data.getStringExtra(Constants.SELECTED_PLACE_DESCRIPTION);
                Log.e("latlong",latitude+" "+longitude);
                setMap();
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
    public void showEmptyAddressNameError() {
        Toast.makeText(this, R.string.empty_address_error, Toast.LENGTH_LONG).show();
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

    @Override
    public void navigateToMyAddresses() {
        finish();
    }

    @Override
    public void hideTokenLoader() {
        hideLoading();
    }
}
