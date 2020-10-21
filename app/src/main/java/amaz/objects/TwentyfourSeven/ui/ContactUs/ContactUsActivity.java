package amaz.objects.TwentyfourSeven.ui.ContactUs;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rey.material.widget.ProgressView;

import java.util.List;

import amaz.objects.TwentyfourSeven.BaseActivity;
import amaz.objects.TwentyfourSeven.MApplication;
import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.data.models.ContactUsData;
import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import amaz.objects.TwentyfourSeven.presenter.PresenterFactory;
import amaz.objects.TwentyfourSeven.utilities.Constants;
import amaz.objects.TwentyfourSeven.utilities.Fonts;
import amaz.objects.TwentyfourSeven.utilities.LanguageUtilities;
import amaz.objects.TwentyfourSeven.utilities.LocalSettings;

public class ContactUsActivity extends BaseActivity implements ContactUsPresenter.ContactUsView, View.OnClickListener, View.OnTouchListener {

    private TextView contactUsTitleTv, contactInfoTv, mobileTv, emailTv, faxTv, addressTv, websiteTv, followTv;
    private RelativeLayout mainContentRl;
    private ScrollView mainSv;
    private FrameLayout transparentFl;
    private ImageView backIv, facebookIv, googleIv, twitterIv, instagramIv;
    private ProgressView loaderPv;

    private TextView noConnectionTitleTv, noConnectionMessageTv;
    private LinearLayout noConnectionLl;
    private Button reloadBtn;

    private LocalSettings localSettings;
    private Fonts fonts;

    private ContactUsPresenter contactUsPresenter;

    private ContactUsData contactUsData;

    @Override
    public void onPresenterAvailable(BasePresenter presenter) {
        contactUsPresenter = (ContactUsPresenter) presenter;
        contactUsPresenter.setView(this);
        if(contactUsData == null){
            contactUsPresenter.getContactsData(localSettings.getLocale());
        }
    }

    @Override
    public PresenterFactory.PresenterType getPresenterType() {
        return PresenterFactory.PresenterType.ContactUs;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localSettings = new LocalSettings(this);
        setLanguage();
        setContentView(R.layout.activicy_contact_us);
        initialization();
        setFonts();
    }

    private void setLanguage() {
        if (localSettings.getLocale().equals(Constants.ARABIC)) {
            LanguageUtilities.switchToArabicLocale(this);
        }
    }

    private void initialization() {
        contactUsTitleTv = findViewById(R.id.tv_contact_us_title);
        backIv = findViewById(R.id.iv_back);
        if (localSettings.getLocale().equals(Constants.ARABIC)) {
            backIv.setImageResource(R.drawable.back_ar_ic);
        } else {
            backIv.setImageResource(R.drawable.back_ic);
        }
        backIv.setOnClickListener(this);
        mainContentRl = findViewById(R.id.rl_main_content);
        mainContentRl.setVisibility(View.GONE);
        mainSv = findViewById(R.id.sv_main);
        transparentFl = findViewById(R.id.fl_transparent);
        transparentFl.setOnTouchListener(this);
        contactInfoTv = findViewById(R.id.tv_contact_info);
        mobileTv = findViewById(R.id.tv_mobile);
        mobileTv.setOnClickListener(this);
        emailTv = findViewById(R.id.tv_email);
        emailTv.setOnClickListener(this);
        faxTv = findViewById(R.id.tv_fax);
        addressTv = findViewById(R.id.tv_address);
        websiteTv = findViewById(R.id.tv_website);
        websiteTv.setOnClickListener(this);
        followTv = findViewById(R.id.tv_follow);
        facebookIv = findViewById(R.id.iv_facebook);
        facebookIv.setOnClickListener(this);
        googleIv = findViewById(R.id.iv_google);
        googleIv.setOnClickListener(this);
        twitterIv = findViewById(R.id.iv_twitter);
        twitterIv.setOnClickListener(this);
        instagramIv = findViewById(R.id.iv_instagram);
        instagramIv.setOnClickListener(this);
        loaderPv = findViewById(R.id.pv_load);

        noConnectionLl = findViewById(R.id.ll_no_connection);
        noConnectionTitleTv = findViewById(R.id.tv_no_connection_title);
        noConnectionMessageTv = findViewById(R.id.tv_no_connection_message);
        reloadBtn = findViewById(R.id.btn_reload_page);
        reloadBtn.setOnClickListener(this);
    }

    private void setFonts() {
        fonts = MApplication.getInstance().getFonts();
        contactUsTitleTv.setTypeface(fonts.customFontBD());
        contactInfoTv.setTypeface(fonts.customFontBD());
        mobileTv.setTypeface(fonts.customFont());
        emailTv.setTypeface(fonts.customFont());
        faxTv.setTypeface(fonts.customFont());
        addressTv.setTypeface(fonts.customFont());
        websiteTv.setTypeface(fonts.customFont());
        followTv.setTypeface(fonts.customFontBD());

        noConnectionTitleTv.setTypeface(fonts.customFontBD());
        noConnectionMessageTv.setTypeface(fonts.customFont());
        reloadBtn.setTypeface(fonts.customFontBD());
    }

    private void setMap() {
        if (contactUsData.getLatitude() != 0.0 && contactUsData.getLongitude() != 0.0) {
            final LatLng currentLocation = new LatLng(contactUsData.getLatitude(), contactUsData.getLongitude());
            final MarkerOptions markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable._247_pin_map)).position(currentLocation).title("");
            if (!this.isDestroyed()) {
                ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fr_map)).getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(final GoogleMap googleMap) {
                        googleMap.clear();

                        googleMap.addMarker(markerOptions);
                        //googleMap.getUiSettings().setScrollGesturesEnabled(true);
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 13));
                    }
                });
            }
        }
    }

    private void openDialer() {
        Intent dialerIntent = new Intent(Intent.ACTION_DIAL);
        dialerIntent.setData(Uri.parse("tel:" + contactUsData.getMobile()));
        startActivity(dialerIntent);
    }

    private void openEmail() {
        final Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{contactUsData.getEmail()});
        intent.setType("text/plain");
        final PackageManager pm = getPackageManager();
        final List<ResolveInfo> matches = pm.queryIntentActivities(intent, 0);
        ResolveInfo best = null;
        for (final ResolveInfo info : matches) {
            if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail")) {
                best = info;
            }
        }

        if (best != null) {
            intent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
            startActivity(intent);
        }
    }

    private void openWebSite() {
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        facebookIntent.setData(Uri.parse(contactUsData.getSiteUrl()));
        startActivity(facebookIntent);
    }

    private void openFacebook() {
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        facebookIntent.setData(Uri.parse(getFacebookPageURL()));
        startActivity(facebookIntent);
    }

    private void openGooglePlus() {
        Intent googleIntent = new Intent(Intent.ACTION_VIEW);
        googleIntent.setData(Uri.parse("https://plus.google.com/"+contactUsData.getGoogleUrl()));
        startActivity(googleIntent);
    }

    private void openTwitter() {
        Intent intent;
        try {
            // get the Twitter app if possible
            getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id="+contactUsData.getTwitterUrl()));
            //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            // no Twitter app, revert to browser
            Log.e("twitter","https://twitter.com/intent/user?user_id="+contactUsData.getTwitterUrl());
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/intent/user?user_id="+contactUsData.getTwitterUrl()));
        }
        startActivity(intent);
    }

    private void openInstagram() {
        Uri uri = Uri.parse("http://instagram.com/"+contactUsData.getInstagramUrl());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        intent.setPackage("com.instagram.android");

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.instagram.com/"+contactUsData.getInstagramUrl())));
        }
    }

    public String getFacebookPageURL() {
        PackageManager packageManager = getPackageManager();
        try {
            packageManager.getPackageInfo("com.facebook.katana", 0);
            return "fb://page/" + contactUsData.getFacebookUrl();

        } catch (PackageManager.NameNotFoundException e) {
            return "https://www.facebook.com/"+contactUsData.getFacebookUrl(); //normal web url
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;

            case R.id.tv_mobile:
                openDialer();
                break;

            case R.id.tv_email:
                openEmail();
                break;

            case R.id.tv_website:
                openWebSite();
                break;

            case R.id.iv_facebook:
                openFacebook();
                break;

            case R.id.iv_google:
                openGooglePlus();
                break;

            case R.id.iv_twitter:
                openTwitter();
                break;

            case R.id.iv_instagram:
                openInstagram();
                break;

            case R.id.btn_reload_page:
                contactUsPresenter.getContactsData(localSettings.getLocale());
                break;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (view.getId() == R.id.fl_transparent) {
            int action = motionEvent.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    // Disallow ScrollView to intercept touch events.
                    mainSv.requestDisallowInterceptTouchEvent(true);
                    // Disable touch on transparent view
                    return false;

                case MotionEvent.ACTION_UP:
                    // Allow ScrollView to intercept touch events.
                    mainSv.requestDisallowInterceptTouchEvent(false);
                    return true;

                case MotionEvent.ACTION_MOVE:
                    mainSv.requestDisallowInterceptTouchEvent(true);
                    return false;

                default:
                    return true;
            }
        } else {
            return true;
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
    public void showPageNotFoundError() {
        Toast.makeText(this, R.string.no_data_error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showNetworkError() {
        noConnectionLl.setVisibility(View.VISIBLE);
    }

    @Override
    public void showServerError() {
        Toast.makeText(this, R.string.server_error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showContactUsData(ContactUsData data) {
        if (noConnectionLl.getVisibility() == View.VISIBLE) {
            noConnectionLl.setVisibility(View.GONE);
        }
        mainContentRl.setVisibility(View.VISIBLE);
        this.contactUsData = data;
        setMap();
        mobileTv.setText(data.getMobile());
        emailTv.setText(data.getEmail());
        faxTv.setText(data.getFax());
        if (localSettings.getLocale().equals(Constants.ARABIC)) {
            addressTv.setText(data.getArabicAddress());
        } else {
            addressTv.setText(data.getEnglishAddress());
        }
        websiteTv.setText(data.getSiteUrl());
        if(contactUsData.getFacebookUrl() == null || contactUsData.getFacebookUrl().isEmpty()){
            facebookIv.setVisibility(View.GONE);
        }
        else {
            facebookIv.setVisibility(View.VISIBLE);
        }

        if(contactUsData.getTwitterUrl() == null || contactUsData.getTwitterUrl().isEmpty()){
            twitterIv.setVisibility(View.GONE);
        }
        else {
            twitterIv.setVisibility(View.VISIBLE);
        }

        if(contactUsData.getGoogleUrl() == null || contactUsData.getGoogleUrl().isEmpty()){
            googleIv.setVisibility(View.GONE);
        }
        else {
            googleIv.setVisibility(View.VISIBLE);
        }

        if(contactUsData.getInstagramUrl() == null || contactUsData.getInstagramUrl().isEmpty()){
            instagramIv.setVisibility(View.GONE);
        }
        else {
            instagramIv.setVisibility(View.VISIBLE);
        }

        if((contactUsData.getFacebookUrl() == null || contactUsData.getFacebookUrl().isEmpty()) &&
           (contactUsData.getInstagramUrl() == null || contactUsData.getInstagramUrl().isEmpty()) &&
           (contactUsData.getTwitterUrl() == null || contactUsData.getTwitterUrl().isEmpty()) &&
           (contactUsData.getGoogleUrl() == null || contactUsData.getGoogleUrl().isEmpty())){
            followTv.setVisibility(View.GONE);
        }
        else {
            followTv.setVisibility(View.VISIBLE);
        }
    }

}
