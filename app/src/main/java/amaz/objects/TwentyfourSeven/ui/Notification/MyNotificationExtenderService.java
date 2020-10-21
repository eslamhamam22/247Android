package amaz.objects.TwentyfourSeven.ui.Notification;

/**
 * Created by Asmaagaber on 7/5/18.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.onesignal.NotificationExtenderService;
import com.onesignal.OSNotificationDisplayedResult;
import com.onesignal.OSNotificationReceivedResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;

import amaz.objects.TwentyfourSeven.MApplication;
import amaz.objects.TwentyfourSeven.R;

public class MyNotificationExtenderService extends NotificationExtenderService {
    @Override
    protected boolean onNotificationProcessing(final OSNotificationReceivedResult receivedResult) {
//

        OverrideSettings overrideSettings = new OverrideSettings();

        JSONObject data = receivedResult.payload.additionalData;
        if(data != null){
            if (data.has("content_available")) {
                try {
                    if (data.getBoolean("content_available")) {
                        return true;

                    } else {
                        overrideSettings.extender = new NotificationCompat.Extender() {
                            @Override
                            public NotificationCompat.Builder extend(NotificationCompat.Builder builder) {
                                // Sets the background notification color to Red on Android 5.0+ devices.
                                Bitmap icon = BitmapFactory.decodeResource(MApplication.getInstance().getResources(),
                                        R.mipmap.notification_ic);
                                // builder.setLargeIcon(icon);
                                builder.setSmallIcon(R.mipmap.notification_ic);
                                // builder.setSound(  Uri.parse("android.resource://"+MApplication.getInstance().getPackageName()+"/"+R.raw.onesignal_default_sound));
                                //return builder;
                                return builder.setColor(new BigInteger("FFFF0000", 16).intValue());

                            }
                        };
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    overrideSettings.extender = new NotificationCompat.Extender() {
                        @Override
                        public NotificationCompat.Builder extend(NotificationCompat.Builder builder) {
                            // Sets the background notification color to Red on Android 5.0+ devices.
                            Bitmap icon = BitmapFactory.decodeResource(MApplication.getInstance().getResources(),
                                    R.mipmap.notification_ic);
                            // builder.setLargeIcon(icon);
                            builder.setSmallIcon(R.mipmap.notification_ic);
                            return builder.setColor(new BigInteger("FFFF0000", 16).intValue());
                        }

                    };
                }

                OSNotificationDisplayedResult displayedResult = displayNotification(overrideSettings);
                Log.d("OneSignalExample", "Notification displayed with id: " + displayedResult.androidNotificationId);


            } else {
                overrideSettings.extender = new NotificationCompat.Extender() {
                    @Override
                    public NotificationCompat.Builder extend(NotificationCompat.Builder builder) {
                        // Sets the background notification color to Red on Android 5.0+ devices.
                        Bitmap icon = BitmapFactory.decodeResource(MApplication.getInstance().getResources(),
                                R.mipmap.notification_ic);
                        // builder.setLargeIcon(icon);
                        builder.setSmallIcon(R.mipmap.notification_ic);
                        // builder.setSound(  Uri.parse("android.resource://"+MApplication.getInstance().getPackageName()+"/"+R.raw.onesignal_default_sound));
                        return builder.setColor(new BigInteger("FFFF0000", 16).intValue());                    //    return builder.setColor(new BigInteger("FF000000", 16).intValue());

                    }
                };
                OSNotificationDisplayedResult displayedResult = displayNotification(overrideSettings);
                Log.d("OneSignalExample", "Notification displayed with id: " + displayedResult.androidNotificationId);

                return true;
            }
        }
        OSNotificationDisplayedResult displayedResult = displayNotification(overrideSettings);
        Log.d("OneSignalExample", "Notification displayed with id: " + displayedResult.androidNotificationId);

        return true;

    }
}
