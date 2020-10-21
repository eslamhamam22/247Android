package amaz.objects.TwentyfourSeven.ui.MapSelectDestination;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.MotionEventCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import amaz.objects.TwentyfourSeven.MApplication;
import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.data.models.Address;
import amaz.objects.TwentyfourSeven.listeners.OnLocationChangeListener;
import amaz.objects.TwentyfourSeven.ui.AddressSearch.AddressSearchActivity;
import amaz.objects.TwentyfourSeven.ui.MyAddresses.MyAddressesActivity;
import amaz.objects.TwentyfourSeven.ui.RequestFromStore.RequestFromStoreActivity;
import amaz.objects.TwentyfourSeven.utilities.AreasUtility;
import amaz.objects.TwentyfourSeven.utilities.Constants;
import amaz.objects.TwentyfourSeven.utilities.Fonts;
import amaz.objects.TwentyfourSeven.utilities.GPSTracker;
import amaz.objects.TwentyfourSeven.utilities.LanguageUtilities;
import amaz.objects.TwentyfourSeven.utilities.LocalSettings;
import amaz.objects.TwentyfourSeven.utilities.NetworkUtilities;

public class MapSelectDestinationActivity extends AppCompatActivity implements View.OnClickListener, OnLocationChangeListener {

    private TextView whereToTitle, toTv, destinationTv, pinNoteTv, activateGpsTitle, activateGpsMessage;
    private ImageView backIv, userAddressesIv, currentLocationIv, markerIv;

    private SupportMapFragment mapFragment;
    private Button confirmDestinationBtn, activateGpsBtn;
    private LinearLayout activateGpsLl;
    private RelativeLayout mainContentRl;
    private LinearLayout markerLl;
    private GPSTracker tracker;
    private double longitude, latitude;
    private String countryNameCode;
    private String selectedAddress;
    private LatLng currentLocation;
    private boolean fromMove = false;
    private boolean fromCurrentLocation = false;
    private boolean fromSearch = false;
    private boolean fromPickup = false;
    private boolean fromSource = false;
    private boolean isAnimated = false;

    private LocalSettings localSettings;
    private Fonts fonts;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localSettings = new LocalSettings(this);
        setLanguage();
        setContentView(R.layout.activity_map_select_destination);
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
        fromPickup = getIntent().getBooleanExtra(Constants.FROM_PICKUP, false);
        fromSource = getIntent().getBooleanExtra(Constants.SOURCE, false);
    }

    private void initialization() {
        mainContentRl = findViewById(R.id.rl_main_content);
        backIv = findViewById(R.id.iv_back);
        if (localSettings.getLocale().equals(Constants.ARABIC)) {
            backIv.setImageResource(R.drawable.back_ar_ic);
        } else {
            backIv.setImageResource(R.drawable.back_ic);
        }
        backIv.setOnClickListener(this);
        whereToTitle = findViewById(R.id.tv_where_title);
        userAddressesIv = findViewById(R.id.iv_user_addresses);
        userAddressesIv.setOnClickListener(this);
        toTv = findViewById(R.id.tv_to);
        destinationTv = findViewById(R.id.tv_destination);
        destinationTv.setOnClickListener(this);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fr_map);

        markerLl = findViewById(R.id.ll_marker);
        pinNoteTv = findViewById(R.id.tv_pin_note);
        markerIv = findViewById(R.id.iv_marker);

        currentLocationIv = findViewById(R.id.iv_current_location);
        currentLocationIv.setOnClickListener(this);

        confirmDestinationBtn = findViewById(R.id.btn_confirm_destination);
        confirmDestinationBtn.setEnabled(false);
        confirmDestinationBtn.setOnClickListener(this);

        activateGpsLl = findViewById(R.id.ll_activate_gps);
        activateGpsTitle = findViewById(R.id.tv_activate_gps_title);
        activateGpsMessage = findViewById(R.id.tv_activate_gps_message);
        activateGpsBtn = findViewById(R.id.btn_activate_gps);
        activateGpsBtn.setOnClickListener(this);

        tracker = new GPSTracker(this, this, true);
        activateGpsLl.setVisibility(View.GONE);
        mainContentRl.setVisibility(View.VISIBLE);
        tracker.getLocation();

        destinationTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().isEmpty()) {
                    confirmDestinationBtn.setAlpha((float) 0.5);
                    confirmDestinationBtn.setEnabled(false);
                } else {
                    confirmDestinationBtn.setAlpha(1);
                    confirmDestinationBtn.setEnabled(true);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        if (fromPickup || fromSource) {
            whereToTitle.setText(R.string.pickup_from);
            toTv.setText(R.string.from);
            destinationTv.setHint(R.string.pickup_point);
            confirmDestinationBtn.setText(R.string.confirm_pickup);
            confirmDestinationBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(this, R.drawable.delivery_ic), null, null, null);
            markerIv.setImageResource(R.drawable.select_store_pin_map);
            pinNoteTv.setBackgroundResource(R.drawable.red_corners);
        } else {
            whereToTitle.setText(R.string.where_to);
            toTv.setText(R.string.to);
            destinationTv.setHint(R.string.destination);
            confirmDestinationBtn.setText(R.string.confirm_destination);
            confirmDestinationBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(this, R.drawable.white_destination_ic), null, null, null);
            markerIv.setImageResource(R.drawable.select_destination_pin_map);
            pinNoteTv.setBackgroundResource(R.drawable.blue_corners);
        }

    }

    private void setFonts() {
        fonts = MApplication.getInstance().getFonts();
        whereToTitle.setTypeface(fonts.customFontBD());
        toTv.setTypeface(fonts.customFont());
        destinationTv.setTypeface(fonts.customFont());
        pinNoteTv.setTypeface(fonts.customFont());
        confirmDestinationBtn.setTypeface(fonts.customFontBD());

        activateGpsTitle.setTypeface(fonts.customFontBD());
        activateGpsMessage.setTypeface(fonts.customFont());
        activateGpsBtn.setTypeface(fonts.customFontBD());
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
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 17));
                googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                    @Override
                    public void onCameraIdle() {
                        //fromMove = true;
                        if (fromPickup || fromSource) {
                            pinNoteTv.setText(R.string.pickup_release_note);
                        } else {
                            pinNoteTv.setText(R.string.delivery_release_note);
                        }

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
                        Log.e("from",fromMove+"   "+fromCurrentLocation);
                        if (fromMove || fromCurrentLocation || fromSearch) {
                            currentLocation = googleMap.getCameraPosition().target;
                            String addressLine = getAddressLine(currentLocation.latitude, currentLocation.longitude);
                            //if (addressLine.isEmpty()) {
                            if (selectedAddress != null && !selectedAddress.isEmpty()) {
                                destinationTv.setText(selectedAddress);
                            } else {
                                destinationTv.setText(addressLine);
                            }
                            //} else {

                            //}
                            selectedAddress = "";
                            fromMove = false;
                            fromCurrentLocation = false;
                            fromSearch = false;
                        }
                    }

                });

                /*googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
                    @Override
                    public void onCameraMove() {


                    }
                });*/

                googleMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
                    @Override
                    public void onCameraMoveStarted(int reason) {
                        if (reason == REASON_GESTURE) {
                            // The user gestured on the map.
                            if (fromPickup || fromSource) {
                                pinNoteTv.setText(R.string.pickup_move_note);
                            } else {
                                pinNoteTv.setText(R.string.delivery_move_note);
                            }

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
                    }
                });


            }
        });

    }

    private String getAddressLine(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, new Locale(localSettings.getLocale()));
        try {
            List<android.location.Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            String addressLine = "";
            if (addresses != null && addresses.size() > 0) {
                addressLine = addresses.get(0).getAddressLine(0);
                return addressLine;
            }
        } catch (IOException e) {
            return "";
        }
        return "";
    }

    private void switchToAddressSearch() {
        Intent addressSearchIntent = new Intent(this, AddressSearchActivity.class);
        addressSearchIntent.putExtra(Constants.COUNTRY_NAME_CODE, countryNameCode);
        addressSearchIntent.putExtra(Constants.SOURCE, this.getClass().getSimpleName());
        addressSearchIntent.putExtra(Constants.PICKUP_LOCATION_SEARCH, true);
        startActivityForResult(addressSearchIntent, Constants.ADDRESS_SEARCH_REQUEST);
        fromSearch = true;
    }

    private void switchToRequestFromStore(Address address) {
        if (fromPickup) {
            if (address != null) {
                Intent requestFromStoreIntent = new Intent(this, RequestFromStoreActivity.class);
                requestFromStoreIntent.putExtra(Constants.FROM_PICKUP, fromPickup);
                requestFromStoreIntent.putExtra(Constants.ADDRESS, address);
                startActivity(requestFromStoreIntent);
                finish();
            } else {
                Intent requestFromStoreIntent = new Intent(this, RequestFromStoreActivity.class);
                requestFromStoreIntent.putExtra(Constants.FROM_PICKUP, fromPickup);
                requestFromStoreIntent.putExtra(Constants.LATITUDE, currentLocation.latitude);
                requestFromStoreIntent.putExtra(Constants.LONGITUDE, currentLocation.longitude);
                requestFromStoreIntent.putExtra(Constants.SELECTED_PLACE_DESCRIPTION, destinationTv.getText().toString());
                startActivity(requestFromStoreIntent);
                finish();
            }
        } else {
            Intent result = new Intent();
            result.putExtra(Constants.LATITUDE, currentLocation.latitude);
            result.putExtra(Constants.LONGITUDE, currentLocation.longitude);
            result.putExtra(Constants.SELECTED_PLACE_DESCRIPTION, destinationTv.getText().toString());
            setResult(AppCompatActivity.RESULT_OK, result);
            finish();
        }

    }

    private void switchToMyAddresses() {
        Intent myAddressesIntent = new Intent(this, MyAddressesActivity.class);
        myAddressesIntent.putExtra(Constants.DESTINATION, true);
        startActivityForResult(myAddressesIntent, Constants.MY_ADDRESSES_REQUEST);
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

            case R.id.tv_destination:
                hideKeyboard();
                switchToAddressSearch();
                break;

            case R.id.btn_confirm_destination:
                LatLng pointToSEarch = new LatLng(currentLocation.latitude, currentLocation.longitude);
                if (!AreasUtility.checkLocationInBlockedArea(this, pointToSEarch)) {
                    hideKeyboard();
                    switchToRequestFromStore(null);
                } else {
                    hideKeyboard();
                    Toast.makeText(this, R.string.place_not_allow, Toast.LENGTH_LONG).show();

                }

                break;

            case R.id.btn_activate_gps:
                openLocationSettings();
                break;

            case R.id.iv_user_addresses:
                switchToMyAddresses();
                break;
        }
    }

    @Override
    public void setAddressData(String countryNameCode, String city, double latitude, double longitude) {
        //if (latitude != 0 && longitude != 0) {
        this.countryNameCode = countryNameCode;
        this.latitude = latitude;
        this.longitude = longitude;
        setMap();
        //}
        /*else {
            if (NetworkUtilities.isInternetConnection(this)) {
                activateGpsLl.setVisibility(View.VISIBLE);
                mainContentRl.setVisibility(View.GONE);
            } else {
                activateGpsLl.setVisibility(View.GONE);
                mainContentRl.setVisibility(View.VISIBLE);
            }
        }*/

    }

    @Override
    public void showActivateGPS() {
        activateGpsLl.setVisibility(View.VISIBLE);
        mainContentRl.setVisibility(View.GONE);
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
                activateGpsLl.setVisibility(View.GONE);
                mainContentRl.setVisibility(View.VISIBLE);
                tracker.getLocation();
            } else {

            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.LOCATION_SETTINGS_REQUEST) {
            activateGpsLl.setVisibility(View.GONE);
            mainContentRl.setVisibility(View.VISIBLE);
            tracker.getLocation();
        } else if (requestCode == Constants.CHECK_LOCATION_SETTINGS_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                activateGpsLl.setVisibility(View.GONE);
                mainContentRl.setVisibility(View.VISIBLE);
                tracker.getLocation();
            } else {

            }
        } else if (requestCode == Constants.ADDRESS_SEARCH_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                this.latitude = data.getDoubleExtra(Constants.LATITUDE, 0);
                this.longitude = data.getDoubleExtra(Constants.LONGITUDE, 0);
                this.selectedAddress = data.getStringExtra(Constants.SELECTED_PLACE_DESCRIPTION);
                Log.e("selectedAddress", latitude + " " + longitude + selectedAddress);
                setMap();

            }
        } else {
            if (resultCode == Activity.RESULT_OK) {
                Address address = (Address) data.getSerializableExtra(Constants.ADDRESS);
                if (fromPickup) {
                    switchToRequestFromStore(address);
                } else {
                    Intent result = new Intent();
                    result.putExtra(Constants.ADDRESS, address);
                    setResult(AppCompatActivity.RESULT_OK, result);
                    finish();
                }

            }
        }
    }


}

