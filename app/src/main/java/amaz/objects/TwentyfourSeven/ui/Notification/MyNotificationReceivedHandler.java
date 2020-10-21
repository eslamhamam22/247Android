package amaz.objects.TwentyfourSeven.ui.Notification;

import android.content.Intent;
import android.content.SharedPreferences;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.onesignal.OSNotification;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import amaz.objects.TwentyfourSeven.MApplication;
import amaz.objects.TwentyfourSeven.data.models.User;
import amaz.objects.TwentyfourSeven.utilities.Constants;

import amaz.objects.TwentyfourSeven.utilities.LocalSettings;


public class MyNotificationReceivedHandler  implements OneSignal.NotificationReceivedHandler {

    private Boolean force_update_notification;
    private FirebaseAnalytics mFirebaseAnalytics;
    SharedPreferences pref;
    private LocalSettings localSettings;


    @Override
    public void notificationReceived(OSNotification notification) {
        JSONObject data = notification.payload.additionalData;

        String customKey;
        System.out.println("notification data  " + "here");
        try {
            if (data != null) {
                //While sending a Push notification from OneSignal dashboard
                Log.e("notification data  " , data.toString());
                localSettings = new LocalSettings(MApplication.getInstance());

                if (data.has("link_to")) {
                    String link_to = data.get("link_to").toString();
                    Log.e("link_to",link_to);
                    Intent registrationComplete = new Intent(Constants.BROADCASTRECEVIERGENERATION);
                    registrationComplete.putExtra("link_to", link_to);

                    if (data.has("order")){
                        registrationComplete.putExtra("order_data", data.get("order").toString());
                    }

                    if (link_to.equals("account")) {

                    } else if (link_to.equals("orders")) {

                    }
                    else if(link_to.equals("delegate_order_details")){

                    }
                    if (data.has("has_app_notif")) {
                        if (data.getBoolean("has_app_notif")) {
                            localSettings.setIsHAsNotificationUnSeen(data.getBoolean("has_app_notif"));
                        }

                    }

                    if (data.has("balance")) {
                        Log.e("balance","balance"+Double.parseDouble(data.getString("balance")));
                        User user = localSettings.getUser();
                        user.setWallet_value(Double.parseDouble(data.getString("balance")));
                        localSettings.setUser(user);
                    }

                    if (data.has("user")) {
                        String user_str = data.get("user").toString();

                        JSONObject user_obj = new JSONObject(user_str);
                        User user = localSettings.getUser();
                        if (user_obj.has("notifications_enabled")) {
                            user.setNotifications_enabled(user_obj.getBoolean("notifications_enabled"));
                        }
                        if (user_obj.has("has_delegate_request")) {
                            user.setHasDelegateRequest(user_obj.getBoolean("has_delegate_request"));
                        }
                        if (user_obj.has("is_delegate")) {
                            user.setDelegate(user_obj.getBoolean("is_delegate"));

                        }
                        if (user_obj.has("unseen_notifications_count")) {
                            user.setUnseen_notifications_count(user_obj.getInt("unseen_notifications_count"));

                        }
                        if (user_obj.has("balance")) {
                            user.setWallet_value(user_obj.getDouble("balance"));

                        }

                        localSettings.setUser(user);

                        customKey = data.optString("customkey", null);
                        if (customKey != null)
                            Log.i("OneSignalExample", "customkey set with value: " + customKey);
                    }
                    LocalBroadcastManager.getInstance(MApplication.getInstance()).sendBroadcast(registrationComplete);

                }
            }
        } catch (JSONException e) {
            Log.e("Tag", "vvv " + e.getMessage());
        }

    }
}