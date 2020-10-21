package amaz.objects.TwentyfourSeven.utilities.tracking_module;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import androidx.annotation.NonNull;
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

import amaz.objects.TwentyfourSeven.api.APIURLs;
import amaz.objects.TwentyfourSeven.utilities.Constants;
import amaz.objects.TwentyfourSeven.utilities.LocalSettings;

public class TrackingReceiver extends BroadcastReceiver {

    private DatabaseReference delegatesLocationsDBRef, delegatesDBRef;
    private GeoFire geoFire;

    private LocalSettings localSettings;
    private double latitude, longitude;
    private ArrayList<LatLng> pathList = new ArrayList<>();
    private String pathStr;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction() != null &&
                intent.getAction().equals(TrackingService.LOCATION_BROADCAST_INTENT_ACTION)){
            Location location = intent.getParcelableExtra(Location.class.getSimpleName());
            if(location != null){
                Log.e("location", location.getLatitude()+ " "+location.getLongitude());
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                if(localSettings == null && delegatesLocationsDBRef == null && delegatesDBRef == null &&
                        geoFire == null) {
                    initialization(context);
                }
                updateDelegateLocation();
            }
        }
    }

    private void initialization(Context context){
            Log.e("omnia","database2");
            localSettings = new LocalSettings(context);
            delegatesLocationsDBRef = FirebaseDatabase.getInstance().getReference().child(APIURLs.DELEGATES_LOCATIONS);
            delegatesDBRef = FirebaseDatabase.getInstance().getReference().child(APIURLs.DELEGATES);
            geoFire = new GeoFire(delegatesLocationsDBRef);
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
}
