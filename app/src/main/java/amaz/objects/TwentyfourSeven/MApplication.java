package amaz.objects.TwentyfourSeven;

import android.app.Application;
import android.content.Context;
import androidx.multidex.MultiDex;
import android.util.Log;
import com.google.firebase.database.FirebaseDatabase;
import com.onesignal.OneSignal;

import amaz.objects.TwentyfourSeven.api.CustomTrust;
import io.branch.referral.Branch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import amaz.objects.TwentyfourSeven.api.APIURLs;
import amaz.objects.TwentyfourSeven.api.ApiEndPointInterface;
import amaz.objects.TwentyfourSeven.ui.Notification.MyNotificationOpenedHandler;
import amaz.objects.TwentyfourSeven.ui.Notification.MyNotificationReceivedHandler;
import amaz.objects.TwentyfourSeven.utilities.Constants;
import amaz.objects.TwentyfourSeven.utilities.Fonts;
import amaz.objects.TwentyfourSeven.utilities.LanguageUtilities;
import amaz.objects.TwentyfourSeven.utilities.LocalSettings;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MApplication extends Application {

    private static MApplication instance;
    private ApiEndPointInterface apiEndPointInterface, googleApiEndPointInterface, googlePlacesApiEndPointInterface, paymentStcApiEndPointInterface;
    private Fonts fonts;
    private static boolean activityVisible;

    public MApplication() {
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Fabric.with(this, new Crashlytics());
        setLanguage();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        Branch.getAutoInstance(this);
        initOneSignal();



    }
    public void initOneSignal(){
        OneSignal.startInit(this)
                .setNotificationOpenedHandler(new MyNotificationOpenedHandler())
                .setNotificationReceivedHandler( new MyNotificationReceivedHandler() )
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                //.unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                Log.d("debug", "PlayerId:" );

                if (userId != null){
                    LocalSettings localSettings = new LocalSettings(getApplicationContext());
                    //  localSettings.setPlayerId(userId);
                    Log.d("debug", "PlayerId:" + userId);
                    localSettings.setRegisteredToken(userId);
                }

            }
        });
    }
    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }

    public static MApplication getInstance(){
        return instance;
    }


    public ApiEndPointInterface getApi(){
        if (apiEndPointInterface == null){
            setupApiEndPoint();
        }
        return apiEndPointInterface;
    }

    public ApiEndPointInterface getGoogleApi(){
        if (googleApiEndPointInterface == null){
            setupGoogleApiEndPoint();
        }
        return googleApiEndPointInterface;
    }

    public ApiEndPointInterface getPlacesApi(){
        if (googlePlacesApiEndPointInterface == null){
            setupGooglePlacesApiEndPoint();
        }
        return googlePlacesApiEndPointInterface;
    }

    public ApiEndPointInterface getImagesApi(){
        if (apiEndPointInterface == null){
            setupImageApiEndPoint();
        }
        return apiEndPointInterface;
    }

    public ApiEndPointInterface getPaymentStcApi(){
        if (paymentStcApiEndPointInterface == null){
            setupPaymentStcApiEndPoint();
        }
        return paymentStcApiEndPointInterface;
    }

    private void setupApiEndPoint(){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(APIURLs.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
        apiEndPointInterface = builder.build().create(ApiEndPointInterface.class);
    }

    private void setupGoogleApiEndPoint(){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(APIURLs.GOOGLE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
        googleApiEndPointInterface = builder.build().create(ApiEndPointInterface.class);
    }

    private void setupGooglePlacesApiEndPoint(){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(APIURLs.GOOGLE_PLACES_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
        googlePlacesApiEndPointInterface = builder.build().create(ApiEndPointInterface.class);
    }

    private void setupImageApiEndPoint(){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(APIURLs.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        apiEndPointInterface = builder.client(client).build().create(ApiEndPointInterface.class);
    }

    private void setupPaymentStcApiEndPoint(){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(APIURLs.STC_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        CustomTrust customTrust = new CustomTrust(getApplicationContext());
        OkHttpClient client = customTrust.getClient();
        paymentStcApiEndPointInterface = builder.client(client).build().create(ApiEndPointInterface.class);
    }

    public Fonts getFonts(){
        if (fonts == null){
            fonts = new Fonts(this);
        }
        return fonts;
    }

    private void setLanguage(){
        LocalSettings localSettings = new LocalSettings(this);
        if (localSettings.getLocale() != null){
            if(localSettings.getLocale().equals(Constants.ARABIC)){
                LanguageUtilities.switchToArabicLocale(this);
                localSettings.setLocale(Constants.ARABIC);
            }
            else {
                LanguageUtilities.switchToEnglishLocale(this);
                localSettings.setLocale(Constants.ENGLISH);
            }
        }
        else {
            //String deviceLanguage = LanguageUtilities.getLocale();
            //if (deviceLanguage.equals(Constants.ARABIC)) {
                LanguageUtilities.switchToArabicLocale(this);
                localSettings.setLocale(Constants.ARABIC);
//            }
//            else {
//                LanguageUtilities.switchToEnglishLocale(this);
//                localSettings.setLocale(Constants.ENGLISH);
//            }
        }
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


}
