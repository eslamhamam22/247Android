package amaz.objects.TwentyfourSeven.utilities.tracking_module;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.utilities.LocalSettings;

public class TrackingService extends Service {

    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 120000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    private static final String CHANNEL_ID = "channel_01";
    private static final int NOTIFICATION_ID = 1234;

    public static final String LOCATION_BROADCAST_INTENT_ACTION = "UPDATE_LOCATION";

    private Location location;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    private NotificationManager notificationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        createLocationRequest();
        createLocationCallback();
        createNotificationChannnel();
        startForeground(NOTIFICATION_ID, getNotification());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        requestLocationUpdates();
        return START_STICKY;
    }

    @Override
    public void onLowMemory() {
        stopUsingGPS();
        stopForegroundService();
        super.onLowMemory();
    }

    @Override
    public void onDestroy() {
        stopUsingGPS();
        stopForegroundService();
        super.onDestroy();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        stopUsingGPS();
        stopForegroundService();
        super.onTaskRemoved(rootIntent);
    }

    private void createLocationRequest(){
        locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setSmallestDisplacement(0);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void createLocationCallback(){
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                onRecieveLocationUpdate(locationResult);
            }

        };
    }

    private void onRecieveLocationUpdate(LocationResult locationResult){
        if (locationResult == null) {
            return;
        }
        TrackingService.this.location = locationResult.getLastLocation();
        Log.e("onRecieveLocation:",getLatitude()+" "+getLongitude());
        notificationManager.notify(NOTIFICATION_ID, getNotification());
        Intent broadCastIntent = new Intent()
                .setAction(LOCATION_BROADCAST_INTENT_ACTION)
                .putExtra(location.getClass().getSimpleName(),location);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadCastIntent);
    }

    private void createNotificationChannnel(){
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Android O requires a Notification Channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence appName = getString(R.string.app_name);
            // Create the channel for the notification
            NotificationChannel notificationChannel =
                    new NotificationChannel(CHANNEL_ID, appName, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setSound(null, null);
            // Set the Notification Channel for the Notification Manager.
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private Notification getNotification(){
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID);
        notificationBuilder
                .setSmallIcon(R.mipmap.notification_ic)
                .setSound(null)
                .setOngoing(true);
        return notificationBuilder.build();
    }

    private void requestLocationUpdates() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    private void stopUsingGPS() {
        if(locationCallback != null && fusedLocationProviderClient != null){
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        }
    }

    private void stopForegroundService() {
        stopForeground(true);
        stopSelf();
    }

    private List<Address> getGeocoderAddresses() {
        if (location != null) {
            Geocoder geocoder = new Geocoder(this, new Locale("en"));
            try {
                return geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
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
        if (location != null){
            return location.getLongitude();
        }
        return 0;
    }

    private double getLatitude() {
        if (location != null){
            return location.getLatitude();
        }
        return 0;
    }

}
