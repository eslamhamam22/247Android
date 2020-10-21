package amaz.objects.TwentyfourSeven.utilities;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.LocationCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.api.APIURLs;
import amaz.objects.TwentyfourSeven.listeners.OnLocationChangeListener;

public class DelegateLocationService extends Service implements OnLocationChangeListener {

    private DatabaseReference delegatesLocationsDBRef, delegatesDBRef;
    private GeoFire geoFire;

    private LocalSettings localSettings;
    private DelegateTracker tracker;
    private double latitude, longitude;
    private ArrayList<LatLng> pathList = new ArrayList<>();
    private String pathStr;

    private Timer timer;

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(1, new Notification());
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initialization();
        getDelegateLocation();
        //startTimer();
        return START_STICKY;
    }

    private void initialization(){
        Log.e("omnia","database2");
        localSettings = new LocalSettings(this);
        delegatesLocationsDBRef = FirebaseDatabase.getInstance().getReference().child(APIURLs.DELEGATES_LOCATIONS);
        delegatesDBRef = FirebaseDatabase.getInstance().getReference().child(APIURLs.DELEGATES);
        geoFire = new GeoFire(delegatesLocationsDBRef);
        tracker = new DelegateTracker(this, this);
    }

    private void getDelegateLocation(){
        tracker.getLocation();
    }

    private void updateDelegateLocation(){
        if (localSettings.getUser() != null){
            geoFire.setLocation(String.valueOf(localSettings.getUser().getId()), new GeoLocation(latitude, longitude), new GeoFire.CompletionListener() {
                @Override
                public void onComplete(String key, DatabaseError error) {
                    Log.e("omnia","database");
                }
            });
            geoFire.getLocation("", new LocationCallback() {
                @Override
                public void onLocationResult(String key, GeoLocation location) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            delegatesDBRef.child(String.valueOf(localSettings.getUser().getId())).child(Constants.UPDATED_AT).setValue(ServerValue.TIMESTAMP);
            delegatesDBRef.child(String.valueOf(localSettings.getUser().getId())).child(Constants.ORDER).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot orderChild : dataSnapshot.getChildren()){
                        if (orderChild.getKey().equals(Constants.STATUS)){
                            if (orderChild.getValue(String.class).equals("in_progress") ||
                                    orderChild.getValue(String.class).equals("delivery_in_progress")){
                                        dataSnapshot.getRef().child("path").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.getValue(String.class) == null){
                                            pathList.clear();
                                            pathList.add(new LatLng(latitude, longitude));
                                            pathStr = PolyUtil.encode(pathList);
                                            dataSnapshot.getRef().setValue(pathStr);
                                        }
                                        else {
                                            pathList.clear();
                                            pathList.addAll(PolyUtil.decode(dataSnapshot.getValue(String.class)));
                                            if (!PolyUtil.containsLocation(latitude, longitude, pathList, false)){
                                                pathList.add(new LatLng(latitude, longitude));
                                            }
                                            pathStr = PolyUtil.encode(pathList);
                                            dataSnapshot.getRef().setValue(pathStr);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    private void startTimer() {
        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                getDelegateLocation();
            }
        };
        timer.scheduleAtFixedRate(timerTask,0,1000*60*2);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void setAddressData(String countryNameCode, String city, double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        updateDelegateLocation();
    }

    @Override
    public void openLocationSettings() {

    }

    @Override
    public void showActivateGPS() {

    }
}
