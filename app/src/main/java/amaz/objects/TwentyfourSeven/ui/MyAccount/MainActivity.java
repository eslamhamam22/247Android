package amaz.objects.TwentyfourSeven.ui.MyAccount;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.rey.material.widget.ProgressView;

import amaz.objects.TwentyfourSeven.BaseActivity;
import amaz.objects.TwentyfourSeven.MApplication;
import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.adapters.ChatAdapter;
import amaz.objects.TwentyfourSeven.data.models.Category;
import amaz.objects.TwentyfourSeven.data.models.Order;
import amaz.objects.TwentyfourSeven.injection.Injection;
import amaz.objects.TwentyfourSeven.listeners.OnRefeshTokenResponse;
import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import amaz.objects.TwentyfourSeven.presenter.PresenterFactory;
import amaz.objects.TwentyfourSeven.ui.AboutUs.AboutUsActivity;
import amaz.objects.TwentyfourSeven.ui.AccountDetails.AccountDetailsActivity;
import amaz.objects.TwentyfourSeven.ui.AddressSearch.AddressSearchActivity;
import amaz.objects.TwentyfourSeven.ui.CarDetails.CarDetailsActivity;
import amaz.objects.TwentyfourSeven.ui.CategoryStores.CategoryStoresActivity;
import amaz.objects.TwentyfourSeven.ui.ContactUs.ContactUsActivity;
import amaz.objects.TwentyfourSeven.ui.HowToUse.HowToUseActivity;
import amaz.objects.TwentyfourSeven.ui.MapSelectDestination.MapSelectDestinationActivity;
import amaz.objects.TwentyfourSeven.ui.MyAddresses.MyAddressesActivity;
import amaz.objects.TwentyfourSeven.ui.MyComplaints.MyComplaintsActivity;
import amaz.objects.TwentyfourSeven.ui.MyOrders.MyOrdersFragment;
import amaz.objects.TwentyfourSeven.ui.MyReviews.MyReviewsActivity;
import amaz.objects.TwentyfourSeven.ui.Notification.NotificationFragment;
import amaz.objects.TwentyfourSeven.ui.OrderDetails.CustomerOrderDetails.CustomerOrderDetailsActivity;
import amaz.objects.TwentyfourSeven.ui.OrderDetails.DelegateOrderDetails.DelegateOrderDetailsActivity;
import amaz.objects.TwentyfourSeven.ui.OrderDetails.OrderDetailsActivity;
import amaz.objects.TwentyfourSeven.ui.Pages.PagesActivity;
import amaz.objects.TwentyfourSeven.ui.RegisterOrLogin.RegisterOrLoginActivity;
import amaz.objects.TwentyfourSeven.ui.Settings.SettingsFragment;
import amaz.objects.TwentyfourSeven.ui.Categories.CategoriesFragment;
import amaz.objects.TwentyfourSeven.utilities.Constants;
import amaz.objects.TwentyfourSeven.utilities.Fonts;
import amaz.objects.TwentyfourSeven.utilities.LanguageUtilities;
import amaz.objects.TwentyfourSeven.utilities.LocalSettings;
import amaz.objects.TwentyfourSeven.utilities.TokenUtilities;
import amaz.objects.TwentyfourSeven.utilities.tracking_module.TrackingReceiver;
import amaz.objects.TwentyfourSeven.utilities.tracking_module.TrackingService;
import amaz.objects.TwentyfourSeven.utilities.tracking_module.TrackingSettings;
import amaz.objects.TwentyfourSeven.viewholders.OrderChildViewHolder;

import static amaz.objects.TwentyfourSeven.utilities.tracking_module.TrackingService.LOCATION_BROADCAST_INTENT_ACTION;
import static amaz.objects.TwentyfourSeven.utilities.tracking_module.TrackingSettings.LOCATION_PERMISSION_REQUEST_CODE;
import static amaz.objects.TwentyfourSeven.utilities.tracking_module.TrackingSettings.LOCATION_SETTINGS_REQUEST_CODE;

public class MainActivity extends BaseActivity implements TabLayout.OnTabSelectedListener, MainPresenter.MainView, OnRefeshTokenResponse {

    private TabLayout tabsTl;
    private TextView notificationsTv, myOrdersTv, storesTv, myAccountTv, settingsTv;
    private CategoriesFragment categoriesFragment;
    private MyAccountFragment myAccountFragment;
    private MyOrdersFragment ordersFragment;
    private SettingsFragment settingsFragment;
    private ProgressView loaderPv;

    private LocalSettings localSettings;
    private Fonts fonts;

    private MainPresenter mainPresenter;

    private boolean isFirstTime = true, firstConnect = false;
    private String openFragment;
    private BroadcastReceiver mBroadcastReceiver;
    private boolean isActivityVisible, isNotificationReceived;

    private TrackingSettings trackingSettings;
    private BroadcastReceiver trackingReceiver;

    @Override
    public void onPresenterAvailable(BasePresenter presenter) {
        mainPresenter = (MainPresenter) presenter;
        mainPresenter.setView(this);
    }

    @Override
    public PresenterFactory.PresenterType getPresenterType() {
        return PresenterFactory.PresenterType.Main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localSettings = new LocalSettings(this);
        setLanguage();
        setContentView(R.layout.activity_main);
        getDataFromIntent();
        initialization();
        initializeTabs();
        setFonts();
        setNotificationBroadCast();
        setTrackingBroadCast();
        if (openFragment != null) {
            if (openFragment.equals(MyAccountFragment.class.getSimpleName())) {
                tabsTl.getTabAt(3).select();
            } else if (openFragment.equals(MyOrdersFragment.class.getSimpleName())) {
                tabsTl.getTabAt(1).select();
            } else if (openFragment.equals(NotificationFragment.class.getSimpleName())) {
                tabsTl.getTabAt(0).select();
                switchToNotifications();
            } else {
                tabsTl.getTabAt(2).select();
            }
        } else {
            tabsTl.getTabAt(2).select();
        }
//        if (localSettings.getUser() != null) {
//            if (localSettings.getUser().isDelegate() && localSettings.getUser().getIsActiveDelegate().getActive()) {
////                if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
////                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
////                            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
////                        checkLocationSettings();
////                    }
////                } else {
////                    checkLocationSettings();
////                }
//                if(!isTrackingServiceRunning()){
//                    startTracking();
//                }
//
//            }
//        }

    }

    public void startTracking(){
        trackingSettings.startTracking();
    }

    public void stopTracking(){
        trackingSettings.stopTrackingService();
    }

//    public void checkLocationSettings() {
//        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
//                .addLocationRequest(LocationRequest.create().setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY));
//        Task<LocationSettingsResponse> result =
//                LocationServices.getSettingsClient(this).checkLocationSettings(builder.build());
//
//        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
//            @Override
//            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
//                try {
//                    LocationSettingsResponse response = task.getResult(ApiException.class);
//
//                    updateDelegateLocation();
//                } catch (ApiException e) {
//                    //e.printStackTrace();
//                    switch (e.getStatusCode()) {
//                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
//                            // Location settings are not satisfied. But could be fixed by showing the
//                            // user a dialog.
//
//                            try {
//                                // Cast to a resolvable exception.
//                                ResolvableApiException resolvable = (ResolvableApiException) e;
//
//                                resolvable.startResolutionForResult(
//                                        MainActivity.this,
//                                        Constants.CHECK_LOCATION_SETTINGS_REQUEST);
//
//                            } catch (IntentSender.SendIntentException sendingIntentException) {
//                                // Ignore the error.
//                            }
//
//                            break;
//                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
//                            // Location settings are not satisfied. However, we have no way to fix the
//                            // settings so we won't show the dialog.
//
//                            break;
//
//                        case LocationSettingsStatusCodes.SUCCESS:
//                            updateDelegateLocation();
//                            break;
//
//                    }
//                }
//
//            }
//        });
//    }

    public void logout() {
        mainPresenter.unRegisterOneSignalToken(localSettings.getToken(), localSettings.getLocale(), localSettings.getRegisteredToken());

    }

    private void getDataFromIntent() {
        openFragment = getIntent().getStringExtra(Constants.OPEN_FRAGMENT);
    }

    private void setLanguage() {
        if (localSettings.getLocale().equals(Constants.ARABIC)) {
            LanguageUtilities.switchToArabicLocale(this);
        }
    }

    private void initialization() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        tabsTl = findViewById(R.id.tl_tabs);
        tabsTl.addOnTabSelectedListener(this);
        loaderPv = findViewById(R.id.pv_load);
        trackingSettings = new TrackingSettings(this);
    }

    private void initializeTabs() {
        if (localSettings.getToken() != null && localSettings.getIsHAsNotificationUnSeen()) {
            notificationsTv = (TextView) LayoutInflater.from(this).inflate(R.layout.item_tab, null, false);
            notificationsTv.setText(R.string.notifications);
            notificationsTv.setCompoundDrawablesRelativeWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.drawable.notif_dot_nr), null, null);

        } else {
            notificationsTv = (TextView) LayoutInflater.from(this).inflate(R.layout.item_tab, null, false);
            notificationsTv.setText(R.string.notifications);
            notificationsTv.setCompoundDrawablesRelativeWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.drawable.notifcation_nr_ic), null, null);

        }

        myOrdersTv = (TextView) LayoutInflater.from(this).inflate(R.layout.item_tab, null, false);
        myOrdersTv.setText(R.string.my_orders);
        myOrdersTv.setCompoundDrawablesRelativeWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.drawable.orders_nr_ic), null, null);

        storesTv = (TextView) LayoutInflater.from(this).inflate(R.layout.item_tab, null, false);
        storesTv.setText(R.string.stores);
        storesTv.setCompoundDrawablesRelativeWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.drawable.stores_nr_ic), null, null);

        myAccountTv = (TextView) LayoutInflater.from(this).inflate(R.layout.item_tab, null, false);
        myAccountTv.setText(R.string.my_account);
        myAccountTv.setCompoundDrawablesRelativeWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.drawable.myaccount_nr_ic), null, null);

        settingsTv = (TextView) LayoutInflater.from(this).inflate(R.layout.item_tab, null, false);
        settingsTv.setText(R.string.settings);
        settingsTv.setCompoundDrawablesRelativeWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.drawable.settings_nr_ic), null, null);

        tabsTl.addTab(tabsTl.newTab().setCustomView(notificationsTv));
        tabsTl.addTab(tabsTl.newTab().setCustomView(myOrdersTv));
        tabsTl.addTab(tabsTl.newTab().setCustomView(storesTv));
        tabsTl.addTab(tabsTl.newTab().setCustomView(myAccountTv));
        tabsTl.addTab(tabsTl.newTab().setCustomView(settingsTv));
    }

    private void setFonts() {
        fonts = MApplication.getInstance().getFonts();
        notificationsTv.setTypeface(fonts.customFontBD());
        myOrdersTv.setTypeface(fonts.customFontBD());
        storesTv.setTypeface(fonts.customFontBD());
        myAccountTv.setTypeface(fonts.customFontBD());
        settingsTv.setTypeface(fonts.customFontBD());
    }

//    private void updateDelegateLocation() {
//
//        Intent intent = new Intent(this, DelegateLocationService.class);
//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//            ContextCompat.startForegroundService(this, intent);
//        }
//        else {
//            startService(intent);
//        }
//
//    }

    private void switchToNotifications() {
        setUnSelectAllTab();
        notificationsTv.setCompoundDrawablesRelativeWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.drawable.notifcation_nr_ic_1), null, null);
        notificationsTv.setTextColor(ContextCompat.getColor(this, R.color.background_blue));
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fl_main_content, new NotificationFragment()).commit();
    }

    public void switchToMyOrders() {
        setUnSelectAllTab();
        tabsTl.getTabAt(1).select();
        myOrdersTv.setCompoundDrawablesRelativeWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.drawable.orders_ac_ic), null, null);
        myOrdersTv.setTextColor(ContextCompat.getColor(this, R.color.background_blue));
        ordersFragment = new MyOrdersFragment();
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fl_main_content, ordersFragment).commit();
    }

    private void switchToStores() {

        setUnSelectAllTab();
        storesTv.setCompoundDrawablesRelativeWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.drawable.stores_ac_ic), null, null);
        storesTv.setTextColor(ContextCompat.getColor(this, R.color.background_blue));
        categoriesFragment = new CategoriesFragment();
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fl_main_content, categoriesFragment).commit();
    }

    public void switchToMyAccount() {
        //getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setUnSelectAllTab();
        tabsTl.getTabAt(3).select();
        myAccountTv.setCompoundDrawablesRelativeWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.drawable.myaccount_ac_ic), null, null);
        myAccountTv.setTextColor(ContextCompat.getColor(this, R.color.background_blue));
        myAccountFragment = new MyAccountFragment();
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fl_main_content, myAccountFragment).commit();
    }

    private void switchToSettings() {
        setUnSelectAllTab();
        settingsTv.setCompoundDrawablesRelativeWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.drawable.settings_ac_ic), null, null);
        settingsTv.setTextColor(ContextCompat.getColor(this, R.color.background_blue));
        settingsFragment = new SettingsFragment();
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fl_main_content, settingsFragment).commit();
    }

    public void switchToHowToBecomeADelegate() {
        Intent howToBecomeDriverIntent = new Intent(this, HowToUseActivity.class);
        howToBecomeDriverIntent.putExtra(Constants.PAGE, Constants.HOW_TO_BECOME_A_DELEGATE);
        startActivity(howToBecomeDriverIntent);
    }

    public void switchToAccountDetails() {
        Intent accountDetailsIntent = new Intent(this, AccountDetailsActivity.class);
        startActivity(accountDetailsIntent);
    }

    public void switchToPages(String page) {
        Intent termsAndConditionsIntent = new Intent(this, PagesActivity.class);
        termsAndConditionsIntent.putExtra(Constants.PAGE, page);
        startActivity(termsAndConditionsIntent);
    }

    public void switchToAboutUs() {
        Intent aboutUsIntent = new Intent(this, AboutUsActivity.class);
        startActivity(aboutUsIntent);
    }

    public void switchToMyAddresses() {
        Intent myAddressesIntent = new Intent(this, MyAddressesActivity.class);
        startActivity(myAddressesIntent);
    }

    public void switchToMyComplaints() {
        Intent myComplaintsIntent = new Intent(this, MyComplaintsActivity.class);
        startActivity(myComplaintsIntent);
    }

    public void switchToGooglePlay() {
        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "belshifa.objects.ws.belshifa")));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "belshifa.objects.ws.belshifa")));
        }
    }

    public void openSharePopup() {
        if (localSettings.getAppShareLink() != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, localSettings.getAppShareLink());
            startActivity(Intent.createChooser(shareIntent, "Share link!"));
        }
    }

    public void switchToContactUs() {
        Intent contactUsIntent = new Intent(this, ContactUsActivity.class);
        startActivity(contactUsIntent);
    }

    public void switchToHowToUse() {
        Intent howToUseIntent = new Intent(this, HowToUseActivity.class);
        howToUseIntent.putExtra(Constants.PAGE, Constants.HOW_TO_USE);
        howToUseIntent.putExtra(Constants.FROM_SETTINGS, true);
        startActivity(howToUseIntent);
    }

    public void switchToCarDetails() {
        Intent carDetailsIntent = new Intent(this, CarDetailsActivity.class);
        startActivity(carDetailsIntent);
    }

    public void switchToRegisterationOrLogin() {
        Intent registerationOrLoginIntent = new Intent(this, RegisterOrLoginActivity.class);
        registerationOrLoginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(registerationOrLoginIntent);
    }

    public void switchToCategoryStores(Category category) {
        Intent categoryStoresIntent = new Intent(this, CategoryStoresActivity.class);
        categoryStoresIntent.putExtra(Constants.CATEGORY, category);
        startActivity(categoryStoresIntent);
    }

    public void switchToSearch() {
        Intent searchIntent = new Intent(this, AddressSearchActivity.class);
        searchIntent.putExtra(Constants.COUNTRY_NAME_CODE, "");
        searchIntent.putExtra(Constants.FROM_ADDRESS, false);
        searchIntent.putExtra(Constants.SOURCE, this.getClass().getSimpleName());
        startActivity(searchIntent);
    }

    public void switchToMapSelectDestination() {
        Intent mapSelectDestinationIntent = new Intent(this, MapSelectDestinationActivity.class);
        mapSelectDestinationIntent.putExtra(Constants.FROM_PICKUP, true);
        startActivity(mapSelectDestinationIntent);
    }

    public void switchToOrderDetails(Order order, boolean isCustomerOrders) {
        Intent orderDetailsIntent;
        Log.e("status", order.getStatus());
        if (!isCustomerOrders) {
            /*if (order.getStatus().equals("assigned") || order.getStatus().equals("in_progress") ||
                    order.getStatus().equals("delivery_in_progress") || order.getStatus().equals("item_picked")){
                orderDetailsIntent = new Intent(this, OrderChatActivity.class);
            }
            else {*/
            orderDetailsIntent = new Intent(this, DelegateOrderDetailsActivity.class);
            //}
        } else {
            /*if (order.getStatus().equals("assigned") || order.getStatus().equals("in_progress") ||
                    order.getStatus().equals("delivery_in_progress") || order.getStatus().equals("item_picked")){
                orderDetailsIntent = new Intent(this, OrderChatActivity.class);
            }
            else {*/
            orderDetailsIntent = new Intent(this, CustomerOrderDetailsActivity.class);
            //}

        }
        orderDetailsIntent.putExtra(Constants.ORDER, order);
        orderDetailsIntent.putExtra(Constants.FROM_CUSTOMER_ORDERS, isCustomerOrders);
        startActivity(orderDetailsIntent);
    }

    public void switchToDelegateOrderDetails(int orderId) {
        Intent delegateOrderDetailsIntent = new Intent(this, DelegateOrderDetailsActivity.class);
        delegateOrderDetailsIntent.putExtra("order_id", orderId);
        startActivity(delegateOrderDetailsIntent);
    }

    public void switchToCustomerOrderDetails(int orderId) {
        Intent customerOrderDetailsIntent = new Intent(this, CustomerOrderDetailsActivity.class);
        customerOrderDetailsIntent.putExtra("order_id", orderId);
        startActivity(customerOrderDetailsIntent);
    }

    public void switchToMyReviews(boolean isCustomerReviews) {
        Intent myReviewsIntent = new Intent(this, MyReviewsActivity.class);
        myReviewsIntent.putExtra(Constants.IS_CUSTOMER_REVIEWS, isCustomerReviews);
        startActivity(myReviewsIntent);
    }

    public void refreshActivity() {
        Intent mainIntent = new Intent(this, MainActivity.class);
        finish();
        startActivity(mainIntent);
    }

    public void setUnSelectAllTab() {
        if (localSettings.getToken() != null && localSettings.getIsHAsNotificationUnSeen()) {
            notificationsTv.setCompoundDrawablesRelativeWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.drawable.notif_dot_nr), null, null);

        } else {
            notificationsTv.setCompoundDrawablesRelativeWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.drawable.notifcation_nr_ic), null, null);

        }
        notificationsTv.setTextColor(ContextCompat.getColor(this, R.color.gray));
        myOrdersTv.setCompoundDrawablesRelativeWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.drawable.orders_nr_ic), null, null);
        myOrdersTv.setTextColor(ContextCompat.getColor(this, R.color.gray));
        storesTv.setCompoundDrawablesRelativeWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.drawable.stores_nr_ic), null, null);
        storesTv.setTextColor(ContextCompat.getColor(this, R.color.gray));
        myAccountTv.setCompoundDrawablesRelativeWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.drawable.myaccount_nr_ic), null, null);
        myAccountTv.setTextColor(ContextCompat.getColor(this, R.color.gray));
        settingsTv.setCompoundDrawablesRelativeWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.drawable.settings_nr_ic), null, null);
        settingsTv.setTextColor(ContextCompat.getColor(this, R.color.gray));

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        switch (tab.getPosition()) {
            case 0:
                if (!isFirstTime) {
                    if (localSettings.getToken() != null) {
                        switchToNotifications();
                    } else {
                        switchToRegisterationOrLogin();
                    }
                } else {
                    isFirstTime = false;
                }


                break;

            case 1:
                if (localSettings.getToken() != null) {
                    switchToMyOrders();
                } else {
                    switchToRegisterationOrLogin();
                }

                break;

            case 2:
                switchToStores();
                break;

            case 3:
                if (localSettings.getToken() != null) {
                    switchToMyAccount();
                } else {
                    switchToRegisterationOrLogin();
                }

                break;

            case 4:
                switchToSettings();
                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

        switch (tab.getPosition()) {
            case 0:
                notificationsTv.setCompoundDrawablesRelativeWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.drawable.notifcation_nr_ic), null, null);
                notificationsTv.setTextColor(ContextCompat.getColor(this, R.color.gray));
                break;

            case 1:
                myOrdersTv.setCompoundDrawablesRelativeWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.drawable.orders_nr_ic), null, null);
                myOrdersTv.setTextColor(ContextCompat.getColor(this, R.color.gray));
                break;

            case 2:
                storesTv.setCompoundDrawablesRelativeWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.drawable.stores_nr_ic), null, null);
                storesTv.setTextColor(ContextCompat.getColor(this, R.color.gray));
                break;

            case 3:
                myAccountTv.setCompoundDrawablesRelativeWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.drawable.myaccount_nr_ic), null, null);
                myAccountTv.setTextColor(ContextCompat.getColor(this, R.color.gray));
                break;

            case 4:
                settingsTv.setCompoundDrawablesRelativeWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.drawable.settings_nr_ic), null, null);
                settingsTv.setTextColor(ContextCompat.getColor(this, R.color.gray));
                break;
        }
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment currentFragment = fm.findFragmentById(R.id.fl_main_content);
        if (currentFragment != null) {
            if (currentFragment.getClass().getSimpleName().equals(SettingsFragment.class.getSimpleName())) {
                if (settingsFragment.isLanguagePopupShown()) {
                    settingsFragment.dismissLanguagePopup();
                } else {
                    super.onBackPressed();
                }
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void showLoading() {
        loaderPv.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public void hideLoading() {
        loaderPv.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public void showInvalideTokenError() {
        Log.e("authError", "authentication error");
        TokenUtilities.refreshToken(this, Injection.provideUserRepository(), localSettings.getToken(), localSettings.getLocale(), localSettings.getRefreshToken(), this);
    }

    @Override
    public void showNetworkError() {
        Toast.makeText(this, R.string.connection_error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showServerError() {
        Toast.makeText(this, R.string.server_error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showSuspededUserError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        localSettings.removeToken();
        localSettings.removeRefreshToken();
        localSettings.removeUser();
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(nm != null){
            nm.cancelAll();
        }
        switchToRegisterationOrLogin();

    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver,
                new IntentFilter(Constants.BROADCASTRECEVIERGENERATION));

        LocalBroadcastManager.getInstance(this).registerReceiver(
                trackingReceiver,
                new IntentFilter(LOCATION_BROADCAST_INTENT_ACTION));

        firstConnect = true;

        if (isNotificationReceived) {
            FragmentManager fm = getSupportFragmentManager();
            Fragment currentFragment = fm.findFragmentById(R.id.fl_main_content);
            if (currentFragment != null) {
                if (currentFragment.getClass().getSimpleName().equals(MyAccountFragment.class.getSimpleName())) {
                    ((MyAccountFragment)currentFragment).refreshData();
                }else  if (currentFragment.getClass().getSimpleName().equals(NotificationFragment.class.getSimpleName())) {
                    ((NotificationFragment)currentFragment).resetLists();
                }
                else  if (currentFragment.getClass().getSimpleName().equals(MyOrdersFragment.class.getSimpleName())) {
                    ((MyOrdersFragment)currentFragment).resetAfterNotification();
                }

            }

            if (localSettings.getUser() != null) {
                if (localSettings.getIsHAsNotificationUnSeen()) {
                    notificationsTv.setText(R.string.notifications);
                    notificationsTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, (float) 7.5);
                    if (currentFragment.getClass().getSimpleName().equals(NotificationFragment.class.getSimpleName())) {
                        notificationsTv.setCompoundDrawablesRelativeWithIntrinsicBounds(null, ContextCompat.getDrawable(MainActivity.this, R.drawable.notifcation_nr_ic_1), null, null);
                    }
                    else {
                        notificationsTv.setCompoundDrawablesRelativeWithIntrinsicBounds(null, ContextCompat.getDrawable(MainActivity.this, R.drawable.notif_dot_nr), null, null);
                    }

                } else {
                    notificationsTv.setText(R.string.notifications);
                    notificationsTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, (float) 7.5);
                    notificationsTv.setCompoundDrawablesRelativeWithIntrinsicBounds(null, ContextCompat.getDrawable(MainActivity.this, R.drawable.notifcation_nr_ic), null, null);

                }
            } else {
                notificationsTv.setText(R.string.notifications);
                notificationsTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, (float) 7.5);
                notificationsTv.setCompoundDrawablesRelativeWithIntrinsicBounds(null, ContextCompat.getDrawable(MainActivity.this, R.drawable.notifcation_nr_ic), null, null);
            }

            isNotificationReceived = false;
        }
        isActivityVisible = true;

        if (localSettings.getUser() != null) {
            if (localSettings.getUser().isDelegate() && localSettings.getUser().getIsActiveDelegate().getActive()) {
                if(!isTrackingServiceRunning()){
                    Log.e("starttracking","start tracking");
                    startTracking();
                }
                else {
                    Log.e("starttracking","started tracking");
                }

            }
        }

    }

    @Override
    protected void onPause() {
        //LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
        super.onPause();
        isActivityVisible = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public void navigateToRegisterationOrLogin() {
        localSettings.removeToken();
        localSettings.removeRefreshToken();
        localSettings.removeFirebaseToken();
        localSettings.removeUser();
        if(isTrackingServiceRunning()){
            stopTracking();
        }
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(nm != null){
            nm.cancelAll();
        }
        switchToRegisterationOrLogin();
    }

    public void setNotificationBroadCast() {

        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Constants.BROADCASTRECEVIERGENERATION)) {
                    if (firstConnect) {
                        String link_to = intent.getStringExtra("link_to");

                        if (isActivityVisible) {
                            FragmentManager fm = getSupportFragmentManager();
                            Fragment currentFragment = fm.findFragmentById(R.id.fl_main_content);
                            if (currentFragment != null) {
                                if (currentFragment.getClass().getSimpleName().equals(MyAccountFragment.class.getSimpleName())) {
                                    ((MyAccountFragment) currentFragment).refreshData();
                                } else if (currentFragment.getClass().getSimpleName().equals(NotificationFragment.class.getSimpleName())) {
                                    ((NotificationFragment) currentFragment).resetLists();
                                } else if (currentFragment.getClass().getSimpleName().equals(MyOrdersFragment.class.getSimpleName())) {
                                    ((MyOrdersFragment) currentFragment).resetAfterNotification();
                                }

                            }
                        } else {
                            isNotificationReceived = true;
                        }

                        if (localSettings.getUser() != null) {
                            if (localSettings.getIsHAsNotificationUnSeen()) {
                                notificationsTv.setText(R.string.notifications);
                                notificationsTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, (float) 7.5);
                                FragmentManager fm = getSupportFragmentManager();
                                Fragment currentFragment = fm.findFragmentById(R.id.fl_main_content);
                                if (currentFragment.getClass().getSimpleName().equals(NotificationFragment.class.getSimpleName())) {
                                    notificationsTv.setCompoundDrawablesRelativeWithIntrinsicBounds(null, ContextCompat.getDrawable(MainActivity.this, R.drawable.notifcation_nr_ic_1), null, null);
                                }
                                else {
                                    notificationsTv.setCompoundDrawablesRelativeWithIntrinsicBounds(null, ContextCompat.getDrawable(MainActivity.this, R.drawable.notif_dot_nr), null, null);
                                }

                            } else {
                                notificationsTv.setText(R.string.notifications);
                                notificationsTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, (float) 7.5);
                                notificationsTv.setCompoundDrawablesRelativeWithIntrinsicBounds(null, ContextCompat.getDrawable(MainActivity.this, R.drawable.notifcation_nr_ic), null, null);

                            }
                        } else {
                            notificationsTv.setText(R.string.notifications);
                            notificationsTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, (float) 7.5);
                            notificationsTv.setCompoundDrawablesRelativeWithIntrinsicBounds(null, ContextCompat.getDrawable(MainActivity.this, R.drawable.notifcation_nr_ic), null, null);
                        }

                    }
                }
            }

        };
    }

    public void setTrackingBroadCast(){
        trackingReceiver = new TrackingReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                super.onReceive(context, intent);
            }
        };
    }

    @Override
    public void hideTokenLoader() {
        hideLoading();
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == Constants.CHECK_LOCATION_SETTINGS_REQUEST) {
//            if (resultCode == Activity.RESULT_OK) {
//                updateDelegateLocation();
//            }
//        }
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if(!isTrackingServiceRunning()){
                    startTracking();
                }
            }
            else {
                if(isTrackingServiceRunning()){
                    stopTracking();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOCATION_SETTINGS_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if(!isTrackingServiceRunning()){
                    startTracking();
                }
            }
            else {
                if(isTrackingServiceRunning()){
                    stopTracking();
                }
            }
        }
    }

    public boolean isTrackingServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if(manager != null){
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (TrackingService.class.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
        }
        return false;
    }

}
