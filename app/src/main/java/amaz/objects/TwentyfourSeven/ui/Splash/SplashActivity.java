package amaz.objects.TwentyfourSeven.ui.Splash;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

//import android.util.Log;
import android.view.WindowManager;

//import com.google.android.play.core.appupdate.AppUpdateInfo;
//import com.google.android.play.core.appupdate.AppUpdateManager;
//import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
//import com.google.android.play.core.install.model.AppUpdateType;
//import com.google.android.play.core.install.model.UpdateAvailability;
//import com.google.android.play.core.tasks.OnSuccessListener;

import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.ui.AccountDetails.AccountDetailsActivity;
import amaz.objects.TwentyfourSeven.ui.HowToUse.HowToUseActivity;
import amaz.objects.TwentyfourSeven.ui.MyAccount.MainActivity;
import amaz.objects.TwentyfourSeven.ui.RegisterOrLogin.RegisterOrLoginActivity;
import amaz.objects.TwentyfourSeven.utilities.Constants;
import amaz.objects.TwentyfourSeven.utilities.LocalSettings;

public class SplashActivity extends AppCompatActivity {

    private LocalSettings localSettings;
    private Handler handler;

    //private AppUpdateManager appUpdateManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        // Creates instance of the manager.
        //appUpdateManager = AppUpdateManagerFactory.create(this);
        initialization();

    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        checkForUpdate();
//    }

//    private void checkForUpdate(){
//        // Checks that the platform will allow the specified type of update.
//        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
//            @Override
//            public void onSuccess(AppUpdateInfo appUpdateInfo) {
//                if ((appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE ||
//                        appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS)
//                        && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)){
//
//                    try {
//                        appUpdateManager.startUpdateFlowForResult(
//                                appUpdateInfo, AppUpdateType.IMMEDIATE, SplashActivity.this, 20);
//                    }
//                    catch (IntentSender.SendIntentException e) {
//                        e.printStackTrace();
//                    }
//
//                } else {
//                    Log.e("AppUpdateAvailability", "checkForAppUpdateAvailability:"+appUpdateInfo.installStatus());
//                }
//            }
//        });
//    }

    private void initialization() {
        localSettings = new LocalSettings(this);
        handler = new Handler();
        setHandler();

    }

    private void setHandler() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (localSettings.getIsHowToUseShown()) {
                    if (localSettings.getToken() != null) {
                        if (localSettings.getUser().getName() != null) {
                            switchToStores();
                        } else {
                            switchToAccountDetails();
                        }
                    } else {
                        switchToRegisterationOrLogin();
                    }
                } else {
                    switchToHowToUse();
                    localSettings.setIsHowToUseShown(true);
                }

            }
        }, 3000);
    }

    private void switchToStores() {
        Intent storesIntent = new Intent(this, MainActivity.class);
        startActivity(storesIntent);
        finish();
    }

    private void switchToRegisterationOrLogin() {
        Intent registerationOrLoginIntent = new Intent(this, RegisterOrLoginActivity.class);
        startActivity(registerationOrLoginIntent);
        finish();
    }

    private void switchToAccountDetails() {
        Intent accountDetailsIntent = new Intent(this, AccountDetailsActivity.class);
        accountDetailsIntent.putExtra(Constants.FROM_VERIFICATION, true);
        startActivity(accountDetailsIntent);
        finish();
    }

    private void switchToHowToUse() {
        Intent howToUseIntent = new Intent(this, HowToUseActivity.class);
        howToUseIntent.putExtra(Constants.PAGE, Constants.HOW_TO_USE);
        howToUseIntent.putExtra(Constants.FROM_SETTINGS, false);
        startActivity(howToUseIntent);
        finish();
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 20) {
//            if (resultCode != RESULT_OK) {
//                Log.e("result","Update flow failed! Result code: " + resultCode);
//                // If the update is cancelled or fails,
//                // you can request to start the update again.
//                checkForUpdate();
//            }
//        }
//    }


}
