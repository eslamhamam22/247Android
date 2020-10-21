package amaz.objects.TwentyfourSeven.ui.Notification;

/**
 * Created by Asmaagaber on 7/5/18.
 */

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;


import amaz.objects.TwentyfourSeven.MApplication;
import amaz.objects.TwentyfourSeven.ui.MyAccount.MainActivity;
import amaz.objects.TwentyfourSeven.ui.MyAccount.MyAccountFragment;
import amaz.objects.TwentyfourSeven.ui.MyOrders.MyOrdersFragment;
import amaz.objects.TwentyfourSeven.ui.OrderDetails.CustomerOrderDetails.CustomerOrderDetailsActivity;
import amaz.objects.TwentyfourSeven.ui.OrderDetails.DelegateOrderDetails.DelegateOrderDetailsActivity;
import amaz.objects.TwentyfourSeven.ui.Splash.SplashActivity;
import amaz.objects.TwentyfourSeven.utilities.Constants;

import amaz.objects.TwentyfourSeven.utilities.LocalSettings;

public class MyNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
    // This fires when a notification is opened by tapping on it.
    private LocalBroadcastManager broadcaster;
    NotificationCompat.Builder mBuilder;
    SharedPreferences pref;
    LocalSettings localSettings;
    private FirebaseAnalytics mFirebaseAnalytics;
    private Boolean force_update_notification;


    @Override
    public void notificationOpened(OSNotificationOpenResult result) {
        Log.e("notificationOpened","notificationOpened");
        OSNotificationAction.ActionType actionType = result.action.type;
        JSONObject data = result.notification.payload.additionalData;
        String activityToBeOpened;
        if (data != null) {
            System.out.println("notification data in tapping "+data);

        }
        showNotificationMessage("");
        //If we send notification with action buttons we need to specidy the button id's and retrieve it to
        //do the necessary operation.
        if (actionType == OSNotificationAction.ActionType.ActionTaken) {
            Log.i("OneSignalExample", "Button pressed with id: " + result.action.actionID);
            if (result.action.actionID.equals("ActionOne")) {
             //   Toast.makeText(MyApplication.getContext(), "ActionOne Button was pressed", Toast.LENGTH_LONG).show();
            } else if (result.action.actionID.equals("ActionTwo")) {
             //   Toast.makeText(MyApplication.getContext(), "ActionTwo Button was pressed", Toast.LENGTH_LONG).show();
            }
        }

        try {
            if (data != null) {
                //While sending a Push notification from OneSignal dashboard
                System.out.println("notification data  " + data);
                broadcaster = LocalBroadcastManager.getInstance(MApplication.getInstance());
                localSettings = new LocalSettings(MApplication.getInstance());
                if (data.has("link_to")) {
                    String link_to = data.get("link_to").toString();
                    if (localSettings.getToken() != null) {
                        if (link_to.equals("account")) {

                            Intent mainIntent = new Intent(MApplication.getInstance().getApplicationContext(), MainActivity.class);
                            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mainIntent.putExtra(Constants.OPEN_FRAGMENT, MyAccountFragment.class.getSimpleName());
                            MApplication.getInstance().getApplicationContext().startActivity(mainIntent);
                        } else if (link_to.equals("orders")) {
                            Intent mainIntent = new Intent(MApplication.getInstance().getApplicationContext(), MainActivity.class);
                            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mainIntent.putExtra(Constants.OPEN_FRAGMENT, MyOrdersFragment.class.getSimpleName());
                            MApplication.getInstance().getApplicationContext().startActivity(mainIntent);
                        } else if (link_to.equals("delegate_order_details")) {
                            if (data.has("order")) {
                                String orderString = data.get("order").toString();
                                JSONObject orderObj = new JSONObject(orderString);
                                Log.e("omnia", orderObj.getInt("id") + "");
                                int orderId = orderObj.getInt("id");

                                Intent delegateOrderDetailsIntent = new Intent(MApplication.getInstance().getApplicationContext(), DelegateOrderDetailsActivity.class);
                                delegateOrderDetailsIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                delegateOrderDetailsIntent.putExtra("order_id", orderId);
                                delegateOrderDetailsIntent.putExtra(Constants.FROM_CUSTOMER_ORDERS, false);
                                delegateOrderDetailsIntent.putExtra(Constants.FROM_PUSH_NOTIFICATION, true);
                                MApplication.getInstance().getApplicationContext().startActivity(delegateOrderDetailsIntent);
                            }

                        } else if (link_to.equals("order_details")) {
                            if (data.has("order")) {
                                String orderString = data.get("order").toString();
                                JSONObject orderObj = new JSONObject(orderString);
                                Log.e("omnia", orderObj.getInt("id") + "");
                                int orderId = orderObj.getInt("id");
                                Intent customerOrderDetails = new Intent(MApplication.getInstance().getApplicationContext(), CustomerOrderDetailsActivity.class);
                                customerOrderDetails.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                customerOrderDetails.putExtra("order_id", orderId);
                                customerOrderDetails.putExtra(Constants.FROM_CUSTOMER_ORDERS, true);
                                customerOrderDetails.putExtra(Constants.FROM_PUSH_NOTIFICATION, true);
                                MApplication.getInstance().getApplicationContext().startActivity(customerOrderDetails);
                            }

                        }

                    }
                }
            }
            else {
                String launchUrl = result.notification.payload.launchURL;
                if(launchUrl != null){
                    Intent googlePlayIntent = new Intent(Intent.ACTION_VIEW);
                    googlePlayIntent.setData(Uri.parse(launchUrl));
                    googlePlayIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    MApplication.getInstance().getApplicationContext().startActivity(googlePlayIntent);
                }
                else {
                    Intent mainIntent = new Intent(MApplication.getInstance().getApplicationContext(), MainActivity.class);
                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    MApplication.getInstance().getApplicationContext().startActivity(mainIntent);
                }
            }
        }catch (JSONException e) {
            Log.e("Tag", "vvv " + e.getMessage());
        }
    }

    public void showNotificationMessage(String type) {
        // Check for empty push message
        initChannels(MApplication.getInstance());

    }

//
//


    public void initChannels(Context context) {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel("247",
                "247",
                NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("247");
        notificationManager.createNotificationChannel(channel);
    }

}