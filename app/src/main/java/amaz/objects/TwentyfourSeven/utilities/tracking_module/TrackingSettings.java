package amaz.objects.TwentyfourSeven.utilities.tracking_module;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class TrackingSettings {

    private Context context;

    public static final int LOCATION_PERMISSION_REQUEST_CODE = 10;
    public static final int LOCATION_SETTINGS_REQUEST_CODE = 20;

    private static Intent trackingServiceIntent;

    public TrackingSettings(Context context) {
        this.context = context;
    }

    public void startTracking() {
        if (isLocationPermissionGranted()) {
            requestLocationPermission();
        } else {
            checkLocationSettings();
        }
    }

    private boolean isLocationPermissionGranted(){
        return  (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED);
    }

    private void requestLocationPermission(){
        ActivityCompat.requestPermissions((AppCompatActivity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
    }


    private void checkLocationSettings() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()

                .addLocationRequest(LocationRequest.create()
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY));
        Task<LocationSettingsResponse> result =
                LocationServices.getSettingsClient(context).checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    startTrackingService();
                } catch (ApiException e) {
                    //e.printStackTrace();
                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be fixed by showing the
                            // user a dialog.

                            try {
                                // Cast to a resolvable exception.
                                ResolvableApiException resolvable = (ResolvableApiException) e;
                                resolvable.startResolutionForResult(
                                        (AppCompatActivity) context,
                                        LOCATION_SETTINGS_REQUEST_CODE);


                            } catch (IntentSender.SendIntentException sendingIntentException) {
                                // Ignore the error.
                            }

                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way to fix the
                            // settings so we won't show the dialog.

                            break;

                        case LocationSettingsStatusCodes.SUCCESS:
                            startTrackingService();
                            break;

                    }
                }

            }
        });
    }

    private void startTrackingService(){
        if(trackingServiceIntent == null){
            trackingServiceIntent = new Intent(context, TrackingService.class);
        }
        ActivityCompat.startForegroundService(context, trackingServiceIntent);
    }

    public void stopTrackingService(){
        if(trackingServiceIntent != null){
            context.stopService(trackingServiceIntent);
        }

    }

}
