package amaz.objects.TwentyfourSeven.ui.RequestFromStore;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.MediaStoreSignature;
import com.google.gson.Gson;
import com.rey.material.widget.ProgressView;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import amaz.objects.TwentyfourSeven.BaseActivity;
import amaz.objects.TwentyfourSeven.MApplication;
import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.adapters.RequestImageAdapter;
import amaz.objects.TwentyfourSeven.api.APIURLs;
import amaz.objects.TwentyfourSeven.data.models.Address;
import amaz.objects.TwentyfourSeven.data.models.DelegateImageData;
import amaz.objects.TwentyfourSeven.data.models.Order;
import amaz.objects.TwentyfourSeven.data.models.PrayerTimes;
import amaz.objects.TwentyfourSeven.data.models.Store;
import amaz.objects.TwentyfourSeven.data.models.VoiceNote;
import amaz.objects.TwentyfourSeven.data.models.responses.UploadDelegateImagesResponse;
import amaz.objects.TwentyfourSeven.injection.Injection;
import amaz.objects.TwentyfourSeven.listeners.OnDeleteImageClickListener;
import amaz.objects.TwentyfourSeven.listeners.OnRecordVoiceListener;
import amaz.objects.TwentyfourSeven.listeners.OnRefeshTokenResponse;
import amaz.objects.TwentyfourSeven.listeners.OnRequestImageIntentListener;
import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import amaz.objects.TwentyfourSeven.presenter.PresenterFactory;
import amaz.objects.TwentyfourSeven.ui.MapSelectDestination.MapSelectDestinationActivity;
import amaz.objects.TwentyfourSeven.ui.MyAccount.MainActivity;
import amaz.objects.TwentyfourSeven.ui.MyOrders.MyOrdersFragment;
import amaz.objects.TwentyfourSeven.ui.OrderDetails.CustomerOrderDetails.CustomerOrderDetailsActivity;
import amaz.objects.TwentyfourSeven.ui.RegisterOrLogin.RegisterOrLoginActivity;
import amaz.objects.TwentyfourSeven.utilities.AddImageUtilities;
import amaz.objects.TwentyfourSeven.utilities.Constants;
import amaz.objects.TwentyfourSeven.utilities.Fonts;
import amaz.objects.TwentyfourSeven.utilities.LanguageUtilities;
import amaz.objects.TwentyfourSeven.utilities.LocalSettings;
import amaz.objects.TwentyfourSeven.utilities.TokenUtilities;
import amaz.objects.TwentyfourSeven.utilities.VoiceRecorder;

public class RequestFromStoreActivity extends BaseActivity implements View.OnClickListener,
        OnRequestImageIntentListener, RequestFromStorePresenter.RequestFromStoreView, OnRefeshTokenResponse, TextWatcher,
        OnRecordVoiceListener, OnDeleteImageClickListener, AdapterView.OnItemSelectedListener {

    private RelativeLayout mainContentRl, recordVoiceRl, playVoiceRl;
    private LinearLayout mainContentLl;
    private CardView storeImageCv;
    private TextView requestTitleTv, fromTv, toTv, storeNameTv, storeAddressTv, destinationTv, openOrClosedTv, whatNeedTv,
            recordVoiceTv, recordTimerTv, saveRecordTv, cancelRecordTv, playTimerTv, recordTimeTv, uploadImageTv, imagesMaxNoTv,prayerTimeTv;
    private SeekBar voiceRecordSb;
    private LinearLayout fromSourceLl, toDestinationLl;
    private ImageView backIv, storeImageIv, addPhotoIv, clearSourceIv, clearDestinationIv, playIv, deleteRecordIv;
    private RecyclerView requestImagesRv;
    private EditText whatNeedEt;
    private Button submitRequestBtn;

    private TextView deliveryDurationTv;
    private Spinner deliveryDurationSp;
    private ArrayList<String> deliveryDurationList = new ArrayList<>();
    private ArrayAdapter<String> deliveryDurationAdapter;

    private TextView addCouponTv, couponCodeTitleTv, couponCodeContentTv;

    private PopupWindow addCouponPopupWindow;
    private LinearLayout popupAddCouponLl;
    private RelativeLayout popupAddCouponContentRl;
    private TextView popupAddCouponTitleTv, popupAddCouponErrorTv, popupAddCouponYesTv, popupAddCouponNoTv;
    private EditText popupAddCouponEt;
    private ProgressView popupAddCouponPv;

    private ArrayList<DelegateImageData> requestImagesList = new ArrayList<>();
    private RequestImageAdapter adapter;

    private ProgressView loaderPv;
    private AVLoadingIndicatorView voiceLoaderAvi;

    private LocalSettings localSettings;
    private Fonts fonts;

    private Store store;
    private boolean fromPickup, fromSource;
    private double destinationLatitude, destinationLongitude, sourceLatitude, sourceLongitude;
    private String destinationAddress, sourceAddress;
    private String categoryImage;
    private Address sourceSavedAddress;
    private int fromType;

    private File imageFile, recordFile;
    private VoiceRecorder voiceRecorder;
    private CountDownTimer countDownTimer;
    private int recordDuration;
    private boolean isPlayed;
    private long voiceNoteId;

    private int selectedDuration;
    private PrayerTimes prayerTimes;

    private RequestFromStorePresenter requestFromStorePresenter;

    @Override
    public void onPresenterAvailable(BasePresenter presenter) {
        requestFromStorePresenter = (RequestFromStorePresenter) presenter;
        requestFromStorePresenter.setView(this);
        if (prayerTimes == null){
            requestFromStorePresenter.getPrayerTimes(localSettings.getLocale());
        }

    }

    @Override
    public PresenterFactory.PresenterType getPresenterType() {
        return PresenterFactory.PresenterType.RequestFromStore;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localSettings = new LocalSettings(this);
        setLanguage();
        setContentView(R.layout.activity_request_from_store);
        getDataFromIntent();
        initialization();
        initializeAddCouponPopup();
        if (!fromPickup) {
            setStoreData();
        } else {
            setSourceData();
        }
        setFonts();
    }

    private void setLanguage() {
        if (localSettings.getLocale().equals(Constants.ARABIC)) {
            LanguageUtilities.switchToArabicLocale(this);
        }
    }

    private void getDataFromIntent() {
        this.fromPickup = getIntent().getBooleanExtra(Constants.FROM_PICKUP, false);
        this.sourceSavedAddress = (Address) getIntent().getSerializableExtra(Constants.ADDRESS);
        if (fromPickup) {
            fromType = 0;
            if (this.sourceSavedAddress == null) {
                this.sourceLatitude = getIntent().getDoubleExtra(Constants.LATITUDE, 0.0);
                this.sourceLongitude = getIntent().getDoubleExtra(Constants.LONGITUDE, 0.0);
                this.sourceAddress = getIntent().getStringExtra(Constants.SELECTED_PLACE_DESCRIPTION);
            }
        } else {
            fromType = 1;
            this.store = (Store) getIntent().getSerializableExtra(Constants.STORE);
            this.categoryImage = getIntent().getStringExtra(Constants.CATEGORY);
        }

    }

    private void initialization() {
        mainContentRl = findViewById(R.id.rl_main_content);
        mainContentRl.setOnClickListener(this);
        mainContentLl = findViewById(R.id.ll_main_content);
        mainContentLl.setOnClickListener(this);

        storeImageCv = findViewById(R.id.cv_store_image);
        requestTitleTv = findViewById(R.id.tv_request_title);
        if (fromPickup){
            requestTitleTv.setText(R.string.request_pick_location);
        }
        else {
            requestTitleTv.setText(R.string.request_from_store);
        }
        prayerTimeTv = findViewById(R.id.tv_prayer_time);

        backIv = findViewById(R.id.iv_back);
        backIv.setOnClickListener(this);
        if (localSettings.getLocale().equals(Constants.ARABIC)) {
            backIv.setImageResource(R.drawable.back_ar_ic);
        } else {
            backIv.setImageResource(R.drawable.back_ic);
        }

        fromSourceLl = findViewById(R.id.ll_source);
        if (fromPickup) {
            fromSourceLl.setOnClickListener(this);
        }
        fromTv = findViewById(R.id.tv_from);

        toTv = findViewById(R.id.tv_to);
        //toTv.addTextChangedListener(this);
        toTv.setVisibility(View.GONE);

        storeNameTv = findViewById(R.id.tv_store_name);
        //storeNameTv.addTextChangedListener(this);
        storeAddressTv = findViewById(R.id.tv_store_address);

        clearSourceIv = findViewById(R.id.iv_clear_source);
        clearSourceIv.setOnClickListener(this);
        clearSourceIv.setVisibility(View.GONE);

        toDestinationLl = findViewById(R.id.ll_to_destination);
        toDestinationLl.setOnClickListener(this);
        destinationTv = findViewById(R.id.tv_destination);
        destinationTv.addTextChangedListener(this);
        clearDestinationIv = findViewById(R.id.iv_clear_destination);
        clearDestinationIv.setOnClickListener(this);
        clearDestinationIv.setVisibility(View.GONE);

        openOrClosedTv = findViewById(R.id.tv_open_or_close);
        whatNeedTv = findViewById(R.id.tv_what_need);
        uploadImageTv = findViewById(R.id.tv_upload_image);
        imagesMaxNoTv = findViewById(R.id.tv_images_max_no);
        addPhotoIv = findViewById(R.id.iv_add_photo);
        addPhotoIv.setOnClickListener(this);
        storeImageIv = findViewById(R.id.iv_store_image);

        requestImagesRv = findViewById(R.id.rv_request_photos);
        requestImagesRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new RequestImageAdapter(this, requestImagesList, this);
        requestImagesRv.setAdapter(adapter);

        whatNeedEt = findViewById(R.id.et_what_need);
        whatNeedEt.addTextChangedListener(this);

        recordVoiceTv = findViewById(R.id.tv_record_voice);
        recordVoiceTv.setOnClickListener(this);

        recordVoiceRl = findViewById(R.id.rl_record_voice);
        recordTimerTv = findViewById(R.id.tv_record_timer);
        saveRecordTv = findViewById(R.id.tv_save_record);
        saveRecordTv.setOnClickListener(this);
        cancelRecordTv = findViewById(R.id.tv_cancel_record);
        cancelRecordTv.setOnClickListener(this);

        playVoiceRl = findViewById(R.id.rl_play_voice);
        playTimerTv = findViewById(R.id.tv_play_timer);
        recordTimeTv = findViewById(R.id.tv_record_time);
        voiceRecordSb = findViewById(R.id.sb_voice_record);

        playIv = findViewById(R.id.iv_play);
        playIv.setOnClickListener(this);
        deleteRecordIv = findViewById(R.id.iv_delete_record);
        deleteRecordIv.setOnClickListener(this);

        submitRequestBtn = findViewById(R.id.btn_request_from_stores);
        submitRequestBtn.setOnClickListener(this);
        submitRequestBtn.setEnabled(false);

        loaderPv = findViewById(R.id.pv_load);
        voiceLoaderAvi = findViewById(R.id.avi_voice_loader);

        voiceRecorder = new VoiceRecorder(this, this);

        deliveryDurationTv = findViewById(R.id.tv_delivery_duration);
        deliveryDurationSp = findViewById(R.id.sp_delivery_durations);
        deliveryDurationAdapter = new ArrayAdapter<String>(this,
                R.layout.item_drop_down, R.id.tv_drop_down, deliveryDurationList) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTypeface(fonts.customFont());
                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setTypeface(fonts.customFont());
                return v;
            }

        };
        deliveryDurationAdapter.setDropDownViewResource(R.layout.item_drop_down);

        deliveryDurationSp.setAdapter(deliveryDurationAdapter);
        deliveryDurationSp.setOnItemSelectedListener(this);

        addCouponTv = findViewById(R.id.tv_add_coupon);
        couponCodeTitleTv = findViewById(R.id.tv_coupon_code_title);
        couponCodeTitleTv.setOnClickListener(this);
        couponCodeContentTv = findViewById(R.id.tv_coupon_code_content);
        couponCodeContentTv.setOnClickListener(this);

    }

    private void initializeAddCouponPopup() {
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_add_coupon, null, false);
        addCouponPopupWindow = new PopupWindow(v, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        popupAddCouponLl = v.findViewById(R.id.ll_add_coupon);
        popupAddCouponLl.setOnClickListener(this);
        popupAddCouponContentRl = v.findViewById(R.id.rl_add_coupon_content);
        popupAddCouponContentRl.setOnClickListener(this);
        popupAddCouponTitleTv = v.findViewById(R.id.tv_add_coupon_title);
        popupAddCouponYesTv = v.findViewById(R.id.tv_add_coupon_yes);
        popupAddCouponYesTv.setOnClickListener(this);
        popupAddCouponNoTv = v.findViewById(R.id.tv_add_coupon_no);
        popupAddCouponNoTv.setOnClickListener(this);
        popupAddCouponEt = v.findViewById(R.id.et_coupon_value);
        popupAddCouponErrorTv = v.findViewById(R.id.tv_coupon_error);
        popupAddCouponPv = v.findViewById(R.id.pv_add_coupon_load);
    }

    private void openAddCouponPopup() {
        addCouponPopupWindow.setFocusable(true);
        addCouponPopupWindow.update();
        addCouponPopupWindow.showAtLocation(mainContentRl, Gravity.CENTER, 0, 0);
        popupAddCouponErrorTv.setVisibility(View.GONE);
        if (!couponCodeContentTv.getText().toString().trim().isEmpty()){
            popupAddCouponEt.setText(couponCodeContentTv.getText().toString());
        }
        else {
            popupAddCouponEt.setText(null);
        }
    }

    private void setStoreData() {
        storeNameTv.setText("\u200E" + store.getName() + "\u200E");
        enableOrDisableSubmit();
        storeAddressTv.setVisibility(View.VISIBLE);
        storeAddressTv.setText("\u200E" + store.getAddress() + "\u200E");
        storeImageCv.setVisibility(View.VISIBLE);
        //Picasso.with(this).load(categoryImage).placeholder(R.drawable.grayscale).into(storeImageIv);
        Glide.with(this)
                .load(APIURLs.BASE_URL+APIURLs.STORE_IMAGES+store.getPlaceId())
                .signature(new MediaStoreSignature("", System.currentTimeMillis(),0))
                .apply(new RequestOptions().placeholder(R.drawable.grayscale).dontAnimate())
                .error(Glide.with(this).load(categoryImage).apply(new RequestOptions().placeholder(R.drawable.grayscale).dontAnimate()))
                .into(storeImageIv);
        /*if (store.getOpeningHours() != null) {
            if (store.getOpeningHours().isOpenNow() != null) {
                if (store.getOpeningHours().isOpenNow()) {
                    openOrClosedTv.setTextColor(ContextCompat.getColor(this, R.color.light_green));
                    openOrClosedTv.setText(R.string.open_now);
                } else {
                    openOrClosedTv.setTextColor(ContextCompat.getColor(this, R.color.app_color));
                    openOrClosedTv.setText(R.string.closed);
                }
            }
        }*/
    }

    private void setSourceData() {
        if (sourceSavedAddress != null) {
            storeNameTv.setText("\u200E" + sourceSavedAddress.getAddressTitle() + "\u200E");
            storeAddressTv.setVisibility(View.VISIBLE);
            storeAddressTv.setText("\u200E" + sourceSavedAddress.getAddressDetails() + "\u200E");
        } else {
            storeNameTv.setText("\u200E" + sourceAddress + "\u200E");
            storeAddressTv.setVisibility(View.GONE);
        }
        enableOrDisableSubmit();
        clearSourceIv.setVisibility(View.VISIBLE);
        storeImageCv.setVisibility(View.GONE);
    }

    private void setFonts() {
        fonts = MApplication.getInstance().getFonts();
        requestTitleTv.setTypeface(fonts.customFontBD());
        fromTv.setTypeface(fonts.customFont());
        toTv.setTypeface(fonts.customFont());
        storeNameTv.setTypeface(fonts.customFont());
        storeAddressTv.setTypeface(fonts.customFont());
        destinationTv.setTypeface(fonts.customFont());
        openOrClosedTv.setTypeface(fonts.customFont());
        whatNeedTv.setTypeface(fonts.customFontBD());

        recordVoiceTv.setTypeface(fonts.customFont());
        saveRecordTv.setTypeface(fonts.customFont());
        cancelRecordTv.setTypeface(fonts.customFont());
        recordTimerTv.setTypeface(fonts.customFont());
        playTimerTv.setTypeface(fonts.customFont());
        recordTimeTv.setTypeface(fonts.customFont());

        uploadImageTv.setTypeface(fonts.customFontBD());
        imagesMaxNoTv.setTypeface(fonts.customFont());
        whatNeedEt.setTypeface(fonts.customFont());
        submitRequestBtn.setTypeface(fonts.customFontBD());
        prayerTimeTv.setTypeface(fonts.customFont());

        deliveryDurationTv.setTypeface(fonts.customFontBD());

        addCouponTv.setTypeface(fonts.customFontBD());
        couponCodeTitleTv.setTypeface(fonts.customFont());
        couponCodeContentTv.setTypeface(fonts.customFont());

        popupAddCouponTitleTv.setTypeface(fonts.customFont());
        popupAddCouponYesTv.setTypeface(fonts.customFont());
        popupAddCouponNoTv.setTypeface(fonts.customFont());
        popupAddCouponEt.setTypeface(fonts.customFont());
        popupAddCouponErrorTv.setTypeface(fonts.customFont());
    }

    private void showGalleryOrCameraDialog() {
        final AlertDialog galleryOrCameraDialog = new AlertDialog.Builder(this)
                .setMessage(R.string.upload_image_dialog_message)
                .setPositiveButton(R.string.upload_gallery_image, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        checkGalleryOrCameraPermissions(Constants.GALLERY);
                    }
                })
                .setNegativeButton(R.string.upload_camera_image, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        checkGalleryOrCameraPermissions(Constants.CAMERA);
                    }
                }).create();
        galleryOrCameraDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {

                TextView message = galleryOrCameraDialog.findViewById(android.R.id.message);
                Button btnGallery = galleryOrCameraDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                Button btnCamera = galleryOrCameraDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                message.setTypeface(fonts.customFont());
                btnGallery.setTypeface(fonts.customFont());
                btnCamera.setTypeface(fonts.customFont());
            }
        });

        galleryOrCameraDialog.show();
    }

    private void checkGalleryOrCameraPermissions(int galleryOrCamera) {
        if (galleryOrCamera == Constants.GALLERY) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.READ_EXTERNAL_STORAGE_REQUEST_PERMISSION);
            } else {
                AddImageUtilities.openGalleryOrCameraIntent(Constants.GALLERY, this, this);
            }
        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.CAMERA_REQUEST_PERMISSION);
            } else {
                AddImageUtilities.openGalleryOrCameraIntent(Constants.CAMERA, this, this);
            }
        }
    }

    private boolean validateImage(String imagePath) {
        boolean validate = true;
        if (imagePath != null) {
            String extension = imagePath.substring(imagePath.lastIndexOf(".") + 1);
            if (extension != null) {
                if (extension.toLowerCase().equals("jpg") || extension.toLowerCase().equals("png") || extension.toLowerCase().equals("jpeg")) {
                    File file = new File(imagePath);
                    if (file.length() / ((double) (1024 * 1024)) > 20) {
                        validate = false;
                        Toast.makeText(this, R.string.image_size_error, Toast.LENGTH_LONG).show();
                    }
                } else {
                    validate = false;
                    Toast.makeText(this, R.string.image_type_error, Toast.LENGTH_LONG).show();
                }
            } else {
                validate = false;
                Toast.makeText(this, R.string.image_type_error, Toast.LENGTH_LONG).show();
            }
        } else {
            validate = false;
        }
        return validate;
    }

    private void hideKeyboard() {
        View focusedView = this.getCurrentFocus();
        if (focusedView != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
        }
    }

    private void switchToRegisterationOrLogin() {
        Intent registerationOrLoginIntent = new Intent(this, RegisterOrLoginActivity.class);
        registerationOrLoginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(registerationOrLoginIntent);
    }

    private void switchToMapSelectDestination() {
        Intent mapSelectDestinationIntent = new Intent(this, MapSelectDestinationActivity.class);
        mapSelectDestinationIntent.putExtra(Constants.SOURCE, fromSource);
        startActivityForResult(mapSelectDestinationIntent, Constants.MAP_DESTINATION_REQUEST);
    }

    private void switchToOrders() {
        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.putExtra(Constants.OPEN_FRAGMENT, MyOrdersFragment.class.getSimpleName());
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainIntent);
    }

    private void clearDestination() {
        this.destinationLatitude = 0.0;
        this.destinationLongitude = 0.0;
        this.destinationAddress = "";
        destinationTv.setVisibility(View.VISIBLE);
        destinationTv.setText("");
        toTv.setText("");
        toTv.setVisibility(View.GONE);
        clearDestinationIv.setVisibility(View.GONE);
        enableOrDisableSubmit();
    }

    private void clearSource() {
        this.sourceLatitude = 0.0;
        this.sourceLongitude = 0.0;
        this.sourceAddress = "";
        storeAddressTv.setVisibility(View.GONE);
        storeAddressTv.setText("");
        storeNameTv.setText("");
        enableOrDisableSubmit();
        clearSourceIv.setVisibility(View.GONE);
    }

    private void createOrder() {
        ArrayList<Long> imagesIds = new ArrayList<>();
        for (int i=0 ; i<requestImagesList.size(); i++){
            imagesIds.add(requestImagesList.get(i).getId());
        }
        Gson gson = new Gson();
        String idsJson = gson.toJson(imagesIds);
        String idsString = idsJson.substring(1,idsJson.lastIndexOf("]"));

        if (fromPickup) {
            if (sourceSavedAddress != null) {
                if (!destinationTv.getText().toString().isEmpty()) {
                    requestFromStorePresenter.createOrder(localSettings.getToken(), localSettings.getLocale(), whatNeedEt.getText().toString(),
                            fromType, sourceSavedAddress.getLatitude(), sourceSavedAddress.getLongitude(),
                            sourceSavedAddress.getAddressDetails(), destinationLatitude, destinationLongitude, destinationTv.getText().toString(),
                            null, idsString, selectedDuration, couponCodeContentTv.getText().toString());
                } else {
                    requestFromStorePresenter.createOrder(localSettings.getToken(), localSettings.getLocale(), whatNeedEt.getText().toString(),
                            fromType, sourceSavedAddress.getLatitude(), sourceSavedAddress.getLongitude(),
                            sourceSavedAddress.getAddressDetails(), destinationLatitude, destinationLongitude, toTv.getText().toString(),
                            null, idsString, selectedDuration, couponCodeContentTv.getText().toString());
                }
            } else {
                if (!destinationTv.getText().toString().isEmpty()) {
                    requestFromStorePresenter.createOrder(localSettings.getToken(), localSettings.getLocale(), whatNeedEt.getText().toString(),
                            fromType, sourceLatitude, sourceLongitude,
                            sourceAddress, destinationLatitude, destinationLongitude, destinationTv.getText().toString(),
                            null, idsString, selectedDuration, couponCodeContentTv.getText().toString());
                } else {
                    requestFromStorePresenter.createOrder(localSettings.getToken(), localSettings.getLocale(), whatNeedEt.getText().toString(),
                            fromType, sourceLatitude, sourceLongitude,
                            sourceAddress, destinationLatitude, destinationLongitude, toTv.getText().toString(), null,
                            idsString, selectedDuration, couponCodeContentTv.getText().toString());
                }
            }
        } else {
            if (!destinationTv.getText().toString().isEmpty()) {
                requestFromStorePresenter.createOrder(localSettings.getToken(), localSettings.getLocale(), whatNeedEt.getText().toString(),
                        fromType, store.getGeometry().getLocation().getLatitude(), store.getGeometry().getLocation().getLongitude(),
                        store.getAddress(), destinationLatitude, destinationLongitude, destinationTv.getText().toString(), store.getName(),
                        idsString, selectedDuration, couponCodeContentTv.getText().toString());
            } else {
                requestFromStorePresenter.createOrder(localSettings.getToken(), localSettings.getLocale(), whatNeedEt.getText().toString(),
                        fromType, store.getGeometry().getLocation().getLatitude(), store.getGeometry().getLocation().getLongitude(),
                        store.getAddress(), destinationLatitude, destinationLongitude, toTv.getText().toString(), store.getName(),
                        idsString, selectedDuration, couponCodeContentTv.getText().toString());
            }
        }
    }

    private void updateDestinationData(Intent data) {
        Address address = (Address) data.getSerializableExtra(Constants.ADDRESS);
        if (address != null) {
            this.destinationLatitude = address.getLatitude();
            this.destinationLongitude = address.getLongitude();
            this.destinationAddress = address.getAddressDetails();
            toTv.setVisibility(View.VISIBLE);
            toTv.setText(address.getAddressTitle());
            enableOrDisableSubmit();
            destinationTv.setVisibility(View.VISIBLE);
            destinationTv.setText(address.getAddressDetails());
            if (!destinationTv.getText().toString().isEmpty()) {
                clearDestinationIv.setVisibility(View.VISIBLE);
                /*if (!whatNeedEt.getText().toString().trim().isEmpty() && !storeNameTv.getText().toString().isEmpty()) {
                    submitRequestBtn.setAlpha(1);
                    submitRequestBtn.setEnabled(true);
                } else {
                    submitRequestBtn.setAlpha((float) 0.5);
                    submitRequestBtn.setEnabled(false);
                }*/
            } else {
                /*submitRequestBtn.setAlpha((float) 0.5);
                submitRequestBtn.setEnabled(false);*/
                clearDestinationIv.setVisibility(View.GONE);
            }
        } else {
            this.destinationLatitude = data.getDoubleExtra(Constants.LATITUDE, 0);
            this.destinationLongitude = data.getDoubleExtra(Constants.LONGITUDE, 0);
            this.destinationAddress = data.getStringExtra(Constants.SELECTED_PLACE_DESCRIPTION);
            destinationTv.setVisibility(View.GONE);
            toTv.setVisibility(View.VISIBLE);
            toTv.setText(this.destinationAddress);
            if (!toTv.getText().toString().isEmpty()) {
                clearDestinationIv.setVisibility(View.VISIBLE);
                /*if (!whatNeedEt.getText().toString().trim().isEmpty() && !storeNameTv.getText().toString().isEmpty()) {
                    submitRequestBtn.setAlpha(1);
                    submitRequestBtn.setEnabled(true);
                } else {
                    submitRequestBtn.setAlpha((float) 0.5);
                    submitRequestBtn.setEnabled(false);
                }*/
            } else {
                //submitRequestBtn.setAlpha((float) 0.5);
                //submitRequestBtn.setEnabled(false);
                clearDestinationIv.setVisibility(View.GONE);
            }
        }
        enableOrDisableSubmit();
    }

    private void updateSourceData(Intent data) {
        Address address = (Address) data.getSerializableExtra(Constants.ADDRESS);
        if (address != null) {
            this.sourceLatitude = address.getLatitude();
            this.sourceLongitude = address.getLongitude();
            this.sourceAddress = address.getAddressDetails();
            storeNameTv.setText(address.getAddressTitle());
            storeAddressTv.setVisibility(View.VISIBLE);
            storeAddressTv.setText(address.getAddressDetails());
            if (!storeNameTv.getText().toString().isEmpty()) {
                clearSourceIv.setVisibility(View.VISIBLE);
                /*if ((!destinationTv.getText().toString().isEmpty() || !toTv.getText().toString().isEmpty())
                        && !whatNeedEt.getText().toString().trim().isEmpty()) {
                    submitRequestBtn.setAlpha(1);
                    submitRequestBtn.setEnabled(true);
                } else {
                    submitRequestBtn.setAlpha((float) 0.5);
                    submitRequestBtn.setEnabled(false);
                }*/
            } else {
                //submitRequestBtn.setAlpha((float) 0.5);
                //submitRequestBtn.setEnabled(false);
                clearDestinationIv.setVisibility(View.GONE);
            }

        } else {
            this.sourceLatitude = data.getDoubleExtra(Constants.LATITUDE, 0);
            this.sourceLongitude = data.getDoubleExtra(Constants.LONGITUDE, 0);
            this.sourceAddress = data.getStringExtra(Constants.SELECTED_PLACE_DESCRIPTION);
            storeAddressTv.setVisibility(View.GONE);
            storeNameTv.setText(this.sourceAddress);
            if (!storeNameTv.getText().toString().isEmpty()) {
                clearSourceIv.setVisibility(View.VISIBLE);
                /*if ((!destinationTv.getText().toString().isEmpty() || !toTv.getText().toString().isEmpty())
                        && !whatNeedEt.getText().toString().trim().isEmpty()) {
                    submitRequestBtn.setAlpha(1);
                    submitRequestBtn.setEnabled(true);
                } else {
                    submitRequestBtn.setAlpha((float) 0.5);
                    submitRequestBtn.setEnabled(false);
                }*/
            } else {
                //submitRequestBtn.setAlpha((float) 0.5);
                //submitRequestBtn.setEnabled(false);
                clearSourceIv.setVisibility(View.GONE);
            }
        }
        enableOrDisableSubmit();
    }

    private void stopAndSaveRecord() {
        countDownTimer.cancel();
        voiceRecorder.stopRecording(false);
        recordVoiceRl.setVisibility(View.GONE);
        playVoiceRl.setVisibility(View.VISIBLE);
    }

    private void cancelRecord() {
        countDownTimer.cancel();
        voiceRecorder.stopRecording(true);
        recordVoiceRl.setVisibility(View.GONE);
        recordVoiceTv.setVisibility(View.VISIBLE);
    }

    private void resetPlayLayout(){
        isPlayed = false;
        voiceRecordSb.setProgress(0);
        int fromMilliToSeconds = recordDuration / 1000;
        int minutes = fromMilliToSeconds / 60;
        int seconds = fromMilliToSeconds - (minutes * 60);
        playTimerTv.setText(String.format(Locale.ENGLISH,"%02d:%02d", minutes, seconds));
        voiceRecordSb.setThumb(getDrawable(R.drawable.grey));
        playIv.setImageResource(R.drawable.play);
    }

    private void enableOrDisableSubmit(){
        if ((!destinationTv.getText().toString().isEmpty() || !toTv.getText().toString().isEmpty()) && !storeNameTv.getText().toString().isEmpty()
                && !whatNeedEt.getText().toString().trim().isEmpty()) {
            submitRequestBtn.setAlpha(1);
            submitRequestBtn.setEnabled(true);
        } else {
            submitRequestBtn.setAlpha((float) 0.5);
            submitRequestBtn.setEnabled(false);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;

            case R.id.iv_add_photo:
                hideKeyboard();
                showGalleryOrCameraDialog();
                break;

            case R.id.ll_to_destination:
                hideKeyboard();
                fromSource = false;
                switchToMapSelectDestination();
                break;

            case R.id.ll_source:
                hideKeyboard();
                fromSource = true;
                switchToMapSelectDestination();
                break;

            case R.id.iv_clear_destination:
                clearDestination();
                break;

            case R.id.iv_clear_source:
                clearSource();
                break;

            case R.id.btn_request_from_stores:
                hideKeyboard();
                createOrder();
                break;

            case R.id.rl_main_content:
                hideKeyboard();
                break;

            case R.id.ll_main_content:
                hideKeyboard();
                break;

            case R.id.tv_record_voice:
                recordVoiceTv.setVisibility(View.GONE);
                recordVoiceRl.setVisibility(View.VISIBLE);
                voiceRecorder.startRecording();
                break;

            case R.id.tv_save_record:
                stopAndSaveRecord();
                break;

            case R.id.tv_cancel_record:
                cancelRecord();
                break;

            case R.id.iv_play:
                if (!isPlayed){
                    isPlayed = true;
                    playIv.setImageResource(R.drawable.pause);
                    voiceRecorder.startPlaying(recordFile,recordDuration);
                    voiceRecordSb.setThumb(getDrawable(R.drawable.red));
                }
                else {
                    isPlayed = false;
                    playIv.setImageResource(R.drawable.play);
                    voiceRecorder.pausePlaying();
                }
                break;

            case R.id.iv_delete_record:
                if (voiceNoteId != 0){
                    deleteRecordIv.setVisibility(View.GONE);
                    requestFromStorePresenter.deleteOrderVoice(localSettings.getToken(), localSettings.getLocale(), voiceNoteId);
                }
                else {
                    showSuccessDeleteVoice();
                }
                break;

            case R.id.tv_coupon_code_title:
                openAddCouponPopup();
                break;

            case R.id.tv_coupon_code_content:
                openAddCouponPopup();
                break;

            case R.id.ll_add_coupon:
                addCouponPopupWindow.dismiss();
                break;

            case R.id.rl_add_coupon_content:
                break;

            case R.id.tv_add_coupon_yes:
                if (!popupAddCouponEt.getText().toString().isEmpty()){
                    popupAddCouponErrorTv.setVisibility(View.GONE);
                    requestFromStorePresenter.validateCoupon(localSettings.getToken(), localSettings.getLocale(), popupAddCouponEt.getText().toString());
                }
                else {
                    popupAddCouponErrorTv.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.tv_add_coupon_no:
                addCouponPopupWindow.dismiss();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (deliveryDurationList.get(position).equals("1 hr")){
            selectedDuration = 1;
        }
        else if(deliveryDurationList.get(position).equals("2 hr")){
            selectedDuration = 2;
        }
        else {
            selectedDuration = 3;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onBackPressed() {
        voiceRecorder.stopRecording(true);
        voiceRecorder.stopPlaying();
        super.onBackPressed();
    }

    @Override
    public void startTimer() {
        recordTimerTv.setText(String.format(Locale.ENGLISH,"%02d:%02d", 0, 0));
        countDownTimer = new CountDownTimer(2 * 60 * 1000, 1000) {
            @Override
            public void onFinish() {
                // TODO Auto-generated method stub
                stopAndSaveRecord();
            }

            @Override
            public void onTick(long millisUntilFinished) {
                // TODO Auto-generated method stub

                long secondsFromStart = 120 - Math.round((double)millisUntilFinished / 1000);
                int minutes = (int)secondsFromStart/60;
                int seconds = (int)(secondsFromStart - (minutes*60));
                recordTimerTv.setText(String.format(Locale.ENGLISH,"%02d:%02d", minutes, seconds));
            }

        }.start();
    }

    @Override
    public void saveRecord(File recordFile, int recordDuration, String recordDate) {
        Log.e("record", recordDate + "     " + recordDuration);
        this.recordFile = recordFile;
        this.recordDuration = recordDuration;
        voiceRecordSb.setMax(recordDuration);
        SimpleDateFormat toDateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss.SSSZ",new Locale(localSettings.getLocale()));
        try {
            Date date = toDateFormat.parse(recordDate.replaceAll("Z$", "+0000"));
            SimpleDateFormat toHouresFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
            recordTimeTv.setText(toHouresFormat.format(date));

            int fromMilliToSeconds = recordDuration / 1000;
            int minutes = fromMilliToSeconds / 60;
            int seconds = fromMilliToSeconds - (minutes * 60);
            playTimerTv.setText(String.format(Locale.ENGLISH,"%02d:%02d", minutes, seconds));
            requestFromStorePresenter.uploadOrderVoice(localSettings.getToken(), localSettings.getLocale(), recordFile);

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public void updatePlayingUi(int currentPosition, boolean isRecordFinished) {
        if (!isRecordFinished){
            Log.e("run","run"+currentPosition+"   "+recordDuration);
            int fromMilliToSeconds = Math.round((float) currentPosition/1000);
            int minutes = fromMilliToSeconds/60;
            int seconds = fromMilliToSeconds-(minutes*60);
            voiceRecordSb.setProgress(currentPosition);
            playTimerTv.setText(String.format(Locale.ENGLISH,"%02d:%02d", minutes, seconds));
        }
        else {
            resetPlayLayout();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        enableOrDisableSubmit();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onRequestGallery(Intent gallery) {
        startActivityForResult(gallery, Constants.GALLERY);
    }

    @Override
    public void onRequestCamera(Intent camera, File imageFile) {
        this.imageFile = imageFile;
        startActivityForResult(camera, Constants.CAMERA);
    }

    @Override
    public void onDeleteImageClick(DelegateImageData imageData) {
        requestFromStorePresenter.deleteOrderImages(localSettings.getToken(), localSettings.getLocale(), imageData);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.READ_EXTERNAL_STORAGE_REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                AddImageUtilities.openGalleryOrCameraIntent(Constants.GALLERY, this, this);
            }
        } else if (requestCode == Constants.RECORD_VOICE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                voiceRecorder.startRecording();
            }
        } else {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                AddImageUtilities.openGalleryOrCameraIntent(Constants.CAMERA, this, this);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("result", "onactivityresult");
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.GALLERY) {
                if (data != null) {
                    Uri selectedImage = data.getData();
                    String imagePath = AddImageUtilities.getImagePath(selectedImage, this);
                    if (validateImage(imagePath)) {
                        File file = new File(imagePath);
                        this.imageFile = file;
                        AddImageUtilities.resizeImage(imageFile);
                        requestFromStorePresenter.uploadOrderImages(localSettings.getToken(), localSettings.getLocale(), imageFile);
                    }
                }
            } else if (requestCode == Constants.CAMERA) {
                String imagePath;
                if (this.imageFile != null) {
                    imagePath = this.imageFile.getAbsolutePath();
                    if (validateImage(imagePath)) {
                        AddImageUtilities.resizeImage(imageFile);
                        requestFromStorePresenter.uploadOrderImages(localSettings.getToken(), localSettings.getLocale(), this.imageFile);
                    }
                }


            } else {
                if (!fromSource) {
                    updateDestinationData(data);
                } else {
                    updateSourceData(data);
                    fromSource = false;
                }
            }
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
    public void showVoiceLoading() {
        voiceLoaderAvi.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideVoiceLoading() {
        voiceLoaderAvi.setVisibility(View.GONE);
    }

    @Override
    public void showCouponDialogLoading() {
        popupAddCouponPv.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public void hideCouponDialogLoading() {
        popupAddCouponPv.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public void showInvalideTokenError() {
        TokenUtilities.refreshToken(this,
                Injection.provideUserRepository(),
                localSettings.getToken(),
                localSettings.getLocale(),
                localSettings.getRefreshToken(),
                this);
    }

    @Override
    public void showNetworkError() {
        deleteRecordIv.setVisibility(View.VISIBLE);
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
        switchToRegisterationOrLogin();

    }

    @Override
    public void showValidationError(String errorMessage) {
        if (errorMessage == null){
            Toast.makeText(this, R.string.what_you_need_error, Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this, errorMessage.substring(errorMessage.indexOf(":")+1), Toast.LENGTH_LONG).show();
            if (errorMessage.contains(":") && errorMessage.substring(0,errorMessage.indexOf(":")).equals("couponCode")){
                couponCodeTitleTv.setVisibility(View.VISIBLE);
                couponCodeContentTv.setVisibility(View.GONE);
                couponCodeContentTv.setText("");
            }
        }
    }

    @Override
    public void showBlockedAreaError() {
        Toast.makeText(this, R.string.blocked_area_error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showSuccessUploadImage(UploadDelegateImagesResponse response) {
        requestImagesList.add(response.getData());
        adapter.notifyDataSetChanged();
        if (requestImagesList.size() == 10){
            addPhotoIv.setEnabled(false);
        }
        else {
            addPhotoIv.setEnabled(true);
        }
    }

    @Override
    public void showSuccessUploadVoice(VoiceNote voiceNote) {
        this.voiceNoteId = voiceNote.getId();
        deleteRecordIv.setVisibility(View.VISIBLE);
    }

    @Override
    public void showSuccessDelete(DelegateImageData imageData) {
        requestImagesList.remove(imageData);
        adapter.notifyDataSetChanged();
        if (requestImagesList.size()==10){
            addPhotoIv.setEnabled(false);
        }
        else {
            addPhotoIv.setEnabled(true);
        }

    }

    @Override
    public void showSuccessDeleteVoice() {
        recordFile = null;
        recordDuration = 0;
        voiceNoteId = 0;
        deleteRecordIv.setVisibility(View.GONE);
        playVoiceRl.setVisibility(View.GONE);
        recordVoiceTv.setVisibility(View.VISIBLE);
        resetPlayLayout();
    }

    @Override
    public void showPrayerTimes(PrayerTimes prayerTimes) {
        this.prayerTimes = prayerTimes;
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());

        int currentTotalMinutes = (cal.get(Calendar.HOUR_OF_DAY)*60) + cal.get(Calendar.MINUTE);
        Log.e("times",prayerTimes.getFajr()+"  "+prayerTimes.getDuhr()+"  "+prayerTimes.getAsr()+"  "+prayerTimes.getMaghrib()+"  "+prayerTimes.getIsha());

        int fajrTotalMinutes = (Integer.parseInt(prayerTimes.getFajr().substring(0,2))*60)+ Integer.parseInt(prayerTimes.getFajr().substring(3));
        int duhrTotalMinutes = (Integer.parseInt(prayerTimes.getDuhr().substring(0,2))*60) + Integer.parseInt(prayerTimes.getDuhr().substring(3));
        int asrTotalMinutes = (Integer.parseInt(prayerTimes.getAsr().substring(0,2))*60) + Integer.parseInt(prayerTimes.getAsr().substring(3));
        int maghribTotalMinutes = (Integer.parseInt(prayerTimes.getMaghrib().substring(0,2))*60) + Integer.parseInt(prayerTimes.getMaghrib().substring(3));
        int ishaTotalMinutes = (Integer.parseInt(prayerTimes.getIsha().substring(0,2))*60) + Integer.parseInt(prayerTimes.getIsha().substring(3));

        deliveryDurationList.clear();
        if (Math.abs(currentTotalMinutes-fajrTotalMinutes) <= 30){
            prayerTimeTv.setVisibility(View.VISIBLE);
            if(localSettings.getLocale().equals(Constants.ENGLISH)){
                deliveryDurationList.add("2 hr");
                deliveryDurationList.add("3 hr");
            }
            else {
                deliveryDurationList.add("ساعتان");
                deliveryDurationList.add("3 ساعات");
            }
            deliveryDurationAdapter.notifyDataSetChanged();
            selectedDuration = 2;
        }
        else if (Math.abs(currentTotalMinutes-duhrTotalMinutes) <= 30){
            prayerTimeTv.setVisibility(View.VISIBLE);
            if(localSettings.getLocale().equals(Constants.ENGLISH)){
                deliveryDurationList.add("2 hr");
                deliveryDurationList.add("3 hr");
            }
            else {
                deliveryDurationList.add("ساعتان");
                deliveryDurationList.add("3 ساعات");
            }
            deliveryDurationAdapter.notifyDataSetChanged();
            selectedDuration = 2;
        }
        else if (Math.abs(currentTotalMinutes-asrTotalMinutes) <= 30){
            prayerTimeTv.setVisibility(View.VISIBLE);
            if(localSettings.getLocale().equals(Constants.ENGLISH)){
                deliveryDurationList.add("2 hr");
                deliveryDurationList.add("3 hr");
            }
            else {
                deliveryDurationList.add("ساعتان");
                deliveryDurationList.add("3 ساعات");
            }
            deliveryDurationAdapter.notifyDataSetChanged();
            selectedDuration = 2;
        }
        else if (Math.abs(currentTotalMinutes-maghribTotalMinutes) <= 30){
            prayerTimeTv.setVisibility(View.VISIBLE);
            if(localSettings.getLocale().equals(Constants.ENGLISH)){
                deliveryDurationList.add("2 hr");
                deliveryDurationList.add("3 hr");
            }
            else {
                deliveryDurationList.add("ساعتان");
                deliveryDurationList.add("3 ساعات");
            }
            deliveryDurationAdapter.notifyDataSetChanged();
            selectedDuration = 2;
        }
        else if (Math.abs(currentTotalMinutes-ishaTotalMinutes) <= 30){
            prayerTimeTv.setVisibility(View.VISIBLE);
            if(localSettings.getLocale().equals(Constants.ENGLISH)){
                deliveryDurationList.add("2 hr");
                deliveryDurationList.add("3 hr");
            }
            else {
                deliveryDurationList.add("ساعتان");
                deliveryDurationList.add("3 ساعات");
            }
            deliveryDurationAdapter.notifyDataSetChanged();
            selectedDuration = 2;
        }
        else {
            prayerTimeTv.setVisibility(View.GONE);
            if(localSettings.getLocale().equals(Constants.ENGLISH)){
                deliveryDurationList.add("1 hr");
                deliveryDurationList.add("2 hr");
                deliveryDurationList.add("3 hr");
            }
            else {
                deliveryDurationList.add("ساعة");
                deliveryDurationList.add("ساعتان");
                deliveryDurationList.add("3 ساعات");
            }
            deliveryDurationAdapter.notifyDataSetChanged();
            selectedDuration = 1;
        }
    }

    @Override
    public void showDefaultPrayerTimes() {
        deliveryDurationList.clear();
        if(localSettings.getLocale().equals(Constants.ENGLISH)){
            deliveryDurationList.add("1 hr");
            deliveryDurationList.add("2 hr");
            deliveryDurationList.add("3 hr");
        }
        else {
            deliveryDurationList.add("ساعة");
            deliveryDurationList.add("ساعتان");
            deliveryDurationList.add("3 ساعات");
        }
        deliveryDurationAdapter.notifyDataSetChanged();
        selectedDuration = 1;
    }

    @Override
    public void showValidCoupon(String code) {
        addCouponPopupWindow.dismiss();
        couponCodeTitleTv.setVisibility(View.GONE);
        couponCodeContentTv.setVisibility(View.VISIBLE);
        couponCodeContentTv.setText(code);
    }

    @Override
    public void removePreviousCoupon() {
        couponCodeTitleTv.setVisibility(View.VISIBLE);
        couponCodeContentTv.setVisibility(View.GONE);
        couponCodeContentTv.setText("");
    }

    @Override
    public void showSuccessSubmit(Order order) {
        Toast.makeText(this, R.string.order_submitted_successfully, Toast.LENGTH_LONG).show();

        switchToOrderDetails(order);
    }

     private void switchToOrderDetails(Order order){
         Intent orderDetailsIntent = new Intent(this, CustomerOrderDetailsActivity.class);
         orderDetailsIntent.putExtra(Constants.FROM_CUSTOMER_ORDERS, true);
         orderDetailsIntent.putExtra(Constants.FROM_CREATE_ORDER, true);
         orderDetailsIntent.putExtra(Constants.ORDER, order);
         startActivity(orderDetailsIntent);
     }

    @Override
    public void hideTokenLoader() {
        hideLoading();
    }

}
