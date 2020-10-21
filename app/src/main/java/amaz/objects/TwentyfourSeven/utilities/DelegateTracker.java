package amaz.objects.TwentyfourSeven.utilities;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import amaz.objects.TwentyfourSeven.listeners.OnLocationChangeListener;

public class DelegateTracker extends Service implements LocationListener {

    private Context context;
    private OnLocationChangeListener onLocationChange;
    private LocationManager manager;
    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 20; // 20 meters
    private static final long MIN_TIME_BW_UPDATES = 1000 * 10; // 10 seconds
    private Location location;
    private LocalSettings localSettings;

    public DelegateTracker(Context context, OnLocationChangeListener onLocationChange) {
        this.context = context;
        this.onLocationChange = onLocationChange;
        this.localSettings = new LocalSettings(context);
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        onLocationChange.setAddressData(getCountryNameCode(), getCity(), getLatitude(), getLongitude());
        Log.e("onlocation","onlocationchange");
        //stopUsingGPS();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
    }

    @Override
    public void onProviderDisabled(String s) {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void getLocation() {
        manager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((AppCompatActivity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constants.LOCATION_PERMISSION_REQUEST);
        } else {
            isGPSEnabled = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (isNetworkEnabled || isGPSEnabled) {
                checkLocationSettings();

            } else {
                /*if (showActivateGPS){
                    onLocationChange.showActivateGPS();
                }
                else {
                    openGPSDialog();
                }*/

            }
        }
    }

    private void checkLocationSettings() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(LocationRequest.create().setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY));
        Task<LocationSettingsResponse> result =
                LocationServices.getSettingsClient(context).checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    requestLocationUpdatesFromProvider();
                } catch (ApiException e) {
                    //e.printStackTrace();
                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be fixed by showing the
                            // user a dialog.

                            try {
                                // Cast to a resolvable exception.
                                ResolvableApiException resolvable = (ResolvableApiException) e;
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().

                                /*if (context instanceof MobileRegisterationActivity){
                                    resolvable.startResolutionForResult(
                                            (MobileRegisterationActivity) context,
                                            Constants.CHECK_LOCATION_SETTINGS_REQUEST);
                                }
                                else if (context instanceof AddAddressActivity){
                                    resolvable.startResolutionForResult(
                                            (AddAddressActivity) context,
                                            Constants.CHECK_LOCATION_SETTINGS_REQUEST);
                                }*/

                                resolvable.startResolutionForResult(
                                        (AppCompatActivity) context,
                                        Constants.CHECK_LOCATION_SETTINGS_REQUEST);

                            } catch (IntentSender.SendIntentException sendingIntentException) {
                                // Ignore the error.
                            }

                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way to fix the
                            // settings so we won't show the dialog.

                            break;

                        case LocationSettingsStatusCodes.SUCCESS:
                            requestLocationUpdatesFromProvider();
                            break;

                    }
                }

            }
        });
    }

    private void requestLocationUpdatesFromProvider() {
        if (isNetworkEnabled) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            /*if (manager != null) {
                location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location != null) {
                    Log.e("lat",getLatitude()+" "+getLongitude());
                    onLocationChange.setAddressData(getCountryNameCode(), getCity(), getLatitude(), getLongitude());
                }
            }*/
        }

        if (isGPSEnabled) {
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            /*if (manager != null) {
                location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    Log.e("lat",getLatitude()+" "+getLongitude());
                    onLocationChange.setAddressData(getCountryNameCode(), getCity(), getLatitude(), getLongitude());
                }
            }*/
        }
    }


    private List<Address> getGeocoderAddresses() {

        if (location != null) {
            Geocoder geocoder = new Geocoder(context, new Locale(localSettings.getLocale()));
            try {
                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                return addresses;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private String getCountryNameCode() {
        List<Address> addresses = getGeocoderAddresses();
        if (addresses != null && !addresses.isEmpty()) {
            Address address = addresses.get(0);
            return address.getCountryCode();
        }
        return null;
    }

    private String getCity() {
        List<Address> addresses = getGeocoderAddresses();
        if (addresses != null && !addresses.isEmpty()) {
            Address address = addresses.get(0);
            return address.getLocality();
        }
        return null;
    }

    private double getLongitude() {
        /*List<Address> addresses = getGeocoderAddresses();
        if (addresses != null && !addresses.isEmpty()) {
            Address address = addresses.get(0);
            double longitude = address.getLongitude();
            return longitude;
        }*/
        if (location != null){
            return location.getLongitude();
        }
        return 0;
    }

    private double getLatitude() {
        /*List<Address> addresses = getGeocoderAddresses();
        if (addresses != null && !addresses.isEmpty()) {
            Address address = addresses.get(0);
            double latitude = address.getLatitude();
            return latitude;
        }*/
        if (location != null){
            return location.getLatitude();
        }
        return 0;
    }

    public void stopUsingGPS() {
        if (manager != null) {
            manager.removeUpdates(DelegateTracker.this);
            manager = null;
        }
    }

    /*private void openGPSDialog() {
        final Fonts fonts = MApplication.getInstance().getFonts();
        final AlertDialog openGPS = new AlertDialog.Builder(context)
                .setTitle(context.getResources().getString(R.string.gps_dialog_title))
                .setMessage(context.getResources().getString(R.string.gps_dialog_message))
                .setPositiveButton(context.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).setNegativeButton(context.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onLocationChange.openLocationSettings();
                        dialogInterface.cancel();
                    }
                }).create();

        openGPS.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                TextView title = openGPS.findViewById(R.id.alertTitle);
                TextView message = openGPS.findViewById(android.R.id.message);
                Button btnYes = openGPS.getButton(DialogInterface.BUTTON_POSITIVE);
                Button btnNo = openGPS.getButton(DialogInterface.BUTTON_NEGATIVE);
                title.setTypeface(fonts.customFont());
                message.setTypeface(fonts.customFont());
                btnYes.setTypeface(fonts.customFont());
                btnNo.setTypeface(fonts.customFont());
            }
        });
        openGPS.show();
    }*/
}
