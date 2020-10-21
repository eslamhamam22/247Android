package amaz.objects.TwentyfourSeven.ui.AccountDetails;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rey.material.widget.ProgressView;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import amaz.objects.TwentyfourSeven.BaseActivity;
import amaz.objects.TwentyfourSeven.MApplication;
import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.data.models.User;
import amaz.objects.TwentyfourSeven.injection.Injection;
import amaz.objects.TwentyfourSeven.listeners.OnRefeshTokenResponse;
import amaz.objects.TwentyfourSeven.listeners.OnRequestImageIntentListener;
import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import amaz.objects.TwentyfourSeven.presenter.PresenterFactory;
import amaz.objects.TwentyfourSeven.ui.MobileRegisteration.MobileRegisterationActivity;
import amaz.objects.TwentyfourSeven.ui.MyAccount.MainActivity;
import amaz.objects.TwentyfourSeven.ui.MyAccount.MyAccountFragment;
import amaz.objects.TwentyfourSeven.ui.RegisterOrLogin.RegisterOrLoginActivity;
import amaz.objects.TwentyfourSeven.utilities.AddImageUtilities;
import amaz.objects.TwentyfourSeven.utilities.Constants;
import amaz.objects.TwentyfourSeven.utilities.Fonts;
import amaz.objects.TwentyfourSeven.utilities.LanguageUtilities;
import amaz.objects.TwentyfourSeven.utilities.LocalSettings;
import amaz.objects.TwentyfourSeven.utilities.TokenUtilities;

public class AccountDetailsActivity extends BaseActivity implements AccountDetailsPresenter.AccountDetailsView, View.OnClickListener,
        DatePickerDialog.OnDateSetListener, OnRequestImageIntentListener, OnRefeshTokenResponse {

    private LinearLayout mainContentLl, uploadImageLl;
    private ImageView backIv, profileIv;
    private TextView accountDetailsTitleTv, uploadImageTv, phoneNumberTv, birthDateTv, genderTv;
    private EditText fullNameEt, cityEt;
    private ImageView maleIb, femaleIb;
    private Button updateBtn;
    private ProgressView loaderPv;
    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;

    private LocalSettings localSettings;
    private Fonts fonts;

    private User user;
    private boolean fromVerification;
    private boolean fromUpdate;

    private boolean isMale = true;
    private File imageFile;

    private AccountDetailsPresenter accountDetailsPresenter;

    @Override
    public void onPresenterAvailable(BasePresenter presenter) {
        accountDetailsPresenter = (AccountDetailsPresenter) presenter;
        accountDetailsPresenter.setView(this);
    }

    @Override
    public PresenterFactory.PresenterType getPresenterType() {
        return PresenterFactory.PresenterType.AccountDetails;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localSettings = new LocalSettings(this);
        setLanguage();
        setContentView(R.layout.activity_account_details);
        getDataFromIntent();
        initialization();
        setFonts();
        //if (fromVerification) {
            setUserData();
        //}

    }

    private void setLanguage() {
        if (localSettings.getLocale().equals(Constants.ARABIC)) {
            LanguageUtilities.switchToArabicLocale(this);
        }
    }

    private void getDataFromIntent() {
        fromVerification = getIntent().getBooleanExtra(Constants.FROM_VERIFICATION, false);
        fromUpdate = getIntent().getBooleanExtra(Constants.FROM_UPDATE, false);
    }

    private void initialization() {
        accountDetailsTitleTv = findViewById(R.id.tv_title_account_details);
        mainContentLl = findViewById(R.id.ll_main_content);
        mainContentLl.setOnClickListener(this);
        uploadImageLl = findViewById(R.id.ll_upload_image);
        uploadImageLl.setOnClickListener(this);
        backIv = findViewById(R.id.iv_back);
        backIv.setOnClickListener(this);
        if (localSettings.getLocale().equals(Constants.ARABIC)) {
            backIv.setImageResource(R.drawable.back_ar_ic);
        } else {
            backIv.setImageResource(R.drawable.back_ic);
        }
        profileIv = findViewById(R.id.iv_profile);
        uploadImageTv = findViewById(R.id.tv_upload_image);
        genderTv = findViewById(R.id.tv_gender);
        fullNameEt = findViewById(R.id.et_full_name);
        phoneNumberTv = findViewById(R.id.tv_phone_number);
        phoneNumberTv.setOnClickListener(this);
        cityEt = findViewById(R.id.et_city);
        birthDateTv = findViewById(R.id.tv_birth_date);
        birthDateTv.setOnClickListener(this);
        updateBtn = findViewById(R.id.btn_update);
        updateBtn.setOnClickListener(this);
        maleIb = findViewById(R.id.ib_male);
        maleIb.setOnClickListener(this);
        femaleIb = findViewById(R.id.ib_female);
        femaleIb.setOnClickListener(this);
        loaderPv = findViewById(R.id.pv_load);

        dateFormat = new SimpleDateFormat("yyyy-MM-dd",new Locale(localSettings.getLocale()));
        calendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this,
                this,calendar.get(Calendar.YEAR),
                             calendar.get(Calendar.MONTH),
                             calendar.get(Calendar.DAY_OF_MONTH));

        if (fromVerification && !fromUpdate) {
            backIv.setVisibility(View.GONE);
            phoneNumberTv.setVisibility(View.GONE);
            updateBtn.setText(R.string.submit_profile);
        } else {
            backIv.setVisibility(View.VISIBLE);
            phoneNumberTv.setVisibility(View.VISIBLE);
            updateBtn.setText(R.string.update);
        }
    }

    private void setFonts() {
        fonts = MApplication.getInstance().getFonts();
        accountDetailsTitleTv.setTypeface(fonts.customFontBD());
        uploadImageTv.setTypeface(fonts.customFont());
        genderTv.setTypeface(fonts.customFont());
        fullNameEt.setTypeface(fonts.customFont());
        phoneNumberTv.setTypeface(fonts.customFont());
        cityEt.setTypeface(fonts.customFont());
        birthDateTv.setTypeface(fonts.customFont());
        updateBtn.setTypeface(fonts.customFontBD());
    }

    private void setUserData() {
        Log.e("token", localSettings.getToken());
        user = localSettings.getUser();
        if (user.getImage() != null){
            Glide.with(this).load(user.getImage().getSmall())
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.avatar)
                            .error(R.drawable.avatar)
                            .dontAnimate())
                    .into(profileIv);
                    //.placeholder(R.drawable.avatar).error(R.drawable.avatar).dontAnimate().into(profileIv);
        }
        else {
            profileIv.setImageResource(R.drawable.avatar);
        }
        fullNameEt.setText(user.getName());
        phoneNumberTv.setText(user.getMobile());
        if (user.getBirthDate() != null){
            birthDateTv.setText(user.getBirthDate().substring(0,user.getBirthDate().indexOf("T")));
            try {
                calendar.setTime(dateFormat.parse(user.getBirthDate().substring(0,user.getBirthDate().indexOf("T"))));
                datePickerDialog = new DatePickerDialog(this,
                        this,calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        cityEt.setText(user.getCity());
        if (user.getGender() != null) {
            if (user.getGender().equals("male")) {
                if (localSettings.getLocale().equals(Constants.ENGLISH)){
                    maleIb.setImageResource(R.drawable.men_ac);
                    maleIb.setBackgroundResource(R.drawable.male_blue_corners_english);
                    femaleIb.setImageResource(R.drawable.women_nr);
                    femaleIb.setBackgroundResource(R.drawable.female_white_corners_english);
                    isMale = true;
                }
                else {
                    maleIb.setImageResource(R.drawable.men_ac);
                    maleIb.setBackgroundResource(R.drawable.male_blue_corners_arabic);
                    femaleIb.setImageResource(R.drawable.women_nr);
                    femaleIb.setBackgroundResource(R.drawable.female_white_corners_arabic);
                    isMale = true;
                }

            } else if (user.getGender().equals("female")) {
                if (localSettings.getLocale().equals(Constants.ENGLISH)){
                    maleIb.setImageResource(R.drawable.men_nr);
                    maleIb.setBackgroundResource(R.drawable.male_white_corners_english);
                    femaleIb.setImageResource(R.drawable.women_ac);
                    femaleIb.setBackgroundResource(R.drawable.female_blue_corners_english);
                    isMale = false;
                }
                else {
                    maleIb.setImageResource(R.drawable.men_nr);
                    maleIb.setBackgroundResource(R.drawable.male_white_corners_arabic);
                    femaleIb.setImageResource(R.drawable.women_ac);
                    femaleIb.setBackgroundResource(R.drawable.female_blue_corners_arabic);
                    isMale = false;
                }

            } else {
                if (localSettings.getLocale().equals(Constants.ENGLISH)){
                    maleIb.setImageResource(R.drawable.men_ac);
                    maleIb.setBackgroundResource(R.drawable.male_blue_corners_english);
                    femaleIb.setImageResource(R.drawable.women_nr);
                    femaleIb.setBackgroundResource(R.drawable.female_white_corners_english);
                    isMale = true;
                }
                else {
                    maleIb.setImageResource(R.drawable.men_ac);
                    maleIb.setBackgroundResource(R.drawable.male_blue_corners_arabic);
                    femaleIb.setImageResource(R.drawable.women_nr);
                    femaleIb.setBackgroundResource(R.drawable.female_white_corners_arabic);
                    isMale = true;
                }

            }
        }

    }

    private void switchToStores() {
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
    }

    private void switchToMobileRegisteration(){
        Intent mobileRegisterationIntent = new Intent(this,MobileRegisterationActivity.class);
        startActivity(mobileRegisterationIntent);
    }

    private void switchToRegisterationOrLogin(){
        Intent registerationOrLoginIntent = new Intent(this,RegisterOrLoginActivity.class);
        registerationOrLoginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(registerationOrLoginIntent);
    }

    private void switchToMyAccount(){
        Intent mainIntent = new Intent(this,MainActivity.class);
        mainIntent.putExtra(Constants.OPEN_FRAGMENT,MyAccountFragment.class.getSimpleName());
        startActivity(mainIntent);
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


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                if (fromUpdate){
                    switchToMyAccount();
                }
                finish();
                break;
            case R.id.ib_male:
                if (localSettings.getLocale().equals(Constants.ENGLISH)){
                    maleIb.setImageResource(R.drawable.men_ac);
                    maleIb.setBackgroundResource(R.drawable.male_blue_corners_english);
                    femaleIb.setImageResource(R.drawable.women_nr);
                    femaleIb.setBackgroundResource(R.drawable.female_white_corners_english);
                    isMale = true;
                }
                else {
                    maleIb.setImageResource(R.drawable.men_ac);
                    maleIb.setBackgroundResource(R.drawable.male_blue_corners_arabic);
                    femaleIb.setImageResource(R.drawable.women_nr);
                    femaleIb.setBackgroundResource(R.drawable.female_white_corners_arabic);
                    isMale = true;
                }
                break;

            case R.id.ib_female:
                if (localSettings.getLocale().equals(Constants.ENGLISH)){
                    maleIb.setImageResource(R.drawable.men_nr);
                    maleIb.setBackgroundResource(R.drawable.male_white_corners_english);
                    femaleIb.setImageResource(R.drawable.women_ac);
                    femaleIb.setBackgroundResource(R.drawable.female_blue_corners_english);
                    isMale = false;
                }
                else {
                    maleIb.setImageResource(R.drawable.men_nr);
                    maleIb.setBackgroundResource(R.drawable.male_white_corners_arabic);
                    femaleIb.setImageResource(R.drawable.women_ac);
                    femaleIb.setBackgroundResource(R.drawable.female_blue_corners_arabic);
                    isMale = false;
                }
                break;

            case R.id.btn_update:
                hideKeyboard();
                //if (fromVerification) {
                    if (isMale) {
                        accountDetailsPresenter.updateUserProfile(localSettings.getToken(), localSettings.getLocale(), fullNameEt.getText().toString(),
                                birthDateTv.getText().toString(), "male", cityEt.getText().toString(), this.imageFile);
                    } else {
                        accountDetailsPresenter.updateUserProfile(localSettings.getToken(), localSettings.getLocale(), fullNameEt.getText().toString(),
                                birthDateTv.getText().toString(), "female", cityEt.getText().toString(), this.imageFile);
                    }
                //}
                break;

            case R.id.tv_birth_date:
                hideKeyboard();
                datePickerDialog.show();
                break;

            case R.id.ll_upload_image:
                showGalleryOrCameraDialog();
                break;

            case R.id.tv_phone_number:
                switchToMobileRegisteration();
                break;

            case R.id.ll_main_content:
                hideKeyboard();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (fromUpdate){
            switchToMyAccount();
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        calendar.set(Calendar.YEAR, i);
        calendar.set(Calendar.MONTH, i1);
        calendar.set(Calendar.DAY_OF_MONTH, i2);
        birthDateTv.setText(dateFormat.format(calendar.getTime()));
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.READ_EXTERNAL_STORAGE_REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                AddImageUtilities.openGalleryOrCameraIntent(Constants.GALLERY, this, this);
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
        Log.e("result","onactivityresult");
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.GALLERY) {
                if (data != null) {
                    Uri selectedImage = data.getData();
                    String imagePath = AddImageUtilities.getImagePath(selectedImage, this);
                    if (validateImage(imagePath)) {
                        File file = new File(imagePath);
                        Glide.with(this).load(file)
                                .apply(new RequestOptions()
                                        .dontAnimate())
                                .into(profileIv);
                                //.dontAnimate().into(profileIv);
                        this.imageFile = file;
                        Log.e("file",this.imageFile.toString());
                    }
                }
            } else {
                String imagePath;
                if (this.imageFile != null) {
                    imagePath = this.imageFile.getAbsolutePath();
                    if (validateImage(imagePath)) {
                        Glide.with(this).load(this.imageFile)
                                .apply(new RequestOptions()
                                        .dontAnimate())
                                .into(profileIv);
                                //.dontAnimate().into(profileIv);
                        Log.e("file",this.imageFile.toString());
                    }
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
    public void showEmptyFullNameError() {
        Toast.makeText(this, R.string.empty_name_error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showInvalidFullNameError() {
        Toast.makeText(this, R.string.invalid_name_error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showInvalideTokenError() {
        Log.e("authError", "authentication error");
        TokenUtilities.refreshToken(this,
                Injection.provideUserRepository(),
                localSettings.getToken(),
                localSettings.getLocale(),
                localSettings.getRefreshToken(),
                this);
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
        switchToRegisterationOrLogin();

    }

    @Override
    public void showSuccessUpdate() {

    }

    @Override
    public void setUserData(User user) {
        localSettings.setUser(user);
    }

    @Override
    public void navigateToStores() {
        if (fromVerification){
            switchToStores();
        }
        else {
            finish();
        }

    }

    @Override
    public void hideTokenLoader() {
        hideLoading();
    }
}
