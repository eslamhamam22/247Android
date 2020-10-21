package amaz.objects.TwentyfourSeven.ui.DelegateRequest;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.cardview.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rey.material.widget.ProgressView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import amaz.objects.TwentyfourSeven.BaseActivity;
import amaz.objects.TwentyfourSeven.MApplication;
import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.data.models.User;
import amaz.objects.TwentyfourSeven.data.models.responses.UploadDelegateImagesResponse;
import amaz.objects.TwentyfourSeven.injection.Injection;
import amaz.objects.TwentyfourSeven.listeners.OnRefeshTokenResponse;
import amaz.objects.TwentyfourSeven.listeners.OnRequestImageIntentListener;
import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import amaz.objects.TwentyfourSeven.presenter.PresenterFactory;
import amaz.objects.TwentyfourSeven.ui.MyAccount.MainActivity;
import amaz.objects.TwentyfourSeven.ui.MyAccount.MyAccountFragment;
import amaz.objects.TwentyfourSeven.ui.RegisterOrLogin.RegisterOrLoginActivity;
import amaz.objects.TwentyfourSeven.utilities.AddImageUtilities;
import amaz.objects.TwentyfourSeven.utilities.Constants;
import amaz.objects.TwentyfourSeven.utilities.Fonts;
import amaz.objects.TwentyfourSeven.utilities.LanguageUtilities;
import amaz.objects.TwentyfourSeven.utilities.LocalSettings;
import amaz.objects.TwentyfourSeven.utilities.TokenUtilities;

public class DelegateRequestActivity extends BaseActivity implements DelegateRequestPresenter.DelegateRequestView, View.OnClickListener,
        OnRequestImageIntentListener, OnRefeshTokenResponse {

    private TextView titleTv, personalInfoTv, fullNameTv, phoneNoTv, cityTv, carDetailsTv, uploadLicenseTv, uploadIdTv, uploadCarFront, uploadCarBack;
    private ImageView backIv, licenseIv, licenseCloseIv, idIv, idCloseIv, carFrontIv, frontCloseIv, carBackIv, backCloseIv;
    private EditText carBrandEt;
    private Button submitRequestBtn;
    private CardView licenseCv, idCv, carFrontCv, carBackCv;
    private LinearLayout mainContentLl, licenseLl, idLl, carFrontLl, carBackLl;
    private ProgressView loaderPv, loaderLicensePv, loaderNationalIdPv, loaderCarFrontPv, loaderCarBackPv;

    private LocalSettings localSettings;
    private Fonts fonts;

    private File licenseImageFile, idImageFile, carFrontImageFile, carBackImageFile;
    private long licenseImageId, idImageId, frontImageId, backImageId;
    private String currentImageType;
    private static final String LICENSE = "delegate_license";
    private static final String PERSONAL_ID = "delegate_national_id";
    private static final String CAR_FRONT = "car_front";
    private static final String CAR_BACK = "car_back";

    private DelegateRequestPresenter delegateRequestPresenter;

    @Override
    public void onPresenterAvailable(BasePresenter presenter) {

        delegateRequestPresenter = (DelegateRequestPresenter) presenter;
        delegateRequestPresenter.setView(this);
    }

    @Override
    public PresenterFactory.PresenterType getPresenterType() {
        return PresenterFactory.PresenterType.UploadDelegateImages;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localSettings = new LocalSettings(this);
        setLanguage();
        setContentView(R.layout.activity_delegate_request);
        initialization();
        setFonts();
        setUserData();
    }

    private void initialization() {
        titleTv = findViewById(R.id.tv_delegate_request_title);
        backIv = findViewById(R.id.iv_back);
        backIv.setOnClickListener(this);
        if (localSettings.getLocale().equals(Constants.ARABIC)) {
            backIv.setImageResource(R.drawable.back_ar_ic);
        } else {
            backIv.setImageResource(R.drawable.back_ic);
        }

        mainContentLl = findViewById(R.id.ll_main_content);
        mainContentLl.setOnClickListener(this);

        personalInfoTv = findViewById(R.id.tv_personal_info);
        fullNameTv = findViewById(R.id.tv_full_name);
        phoneNoTv = findViewById(R.id.tv_phone_number);
        cityTv = findViewById(R.id.tv_city);
        carDetailsTv = findViewById(R.id.tv_car_details);
        carBrandEt = findViewById(R.id.et_car_brand);
        carBrandEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validateRequestFields();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        licenseCv = findViewById(R.id.cv_license);
        licenseCv.setOnClickListener(this);
        licenseLl = findViewById(R.id.ll_license);
        uploadLicenseTv = findViewById(R.id.tv_upload_license);
        licenseIv = findViewById(R.id.iv_license);
        licenseCloseIv = findViewById(R.id.iv_license_close);
        licenseCloseIv.setOnClickListener(this);
        licenseCloseIv.setVisibility(View.GONE);
        loaderLicensePv = findViewById(R.id.pv_load_license);

        idCv = findViewById(R.id.cv_id);
        idCv.setOnClickListener(this);
        idLl = findViewById(R.id.ll_id);
        uploadIdTv = findViewById(R.id.tv_upload_id);
        idIv = findViewById(R.id.iv_id);
        idCloseIv = findViewById(R.id.iv_id_close);
        idCloseIv.setOnClickListener(this);
        idCloseIv.setVisibility(View.GONE);
        loaderNationalIdPv = findViewById(R.id.pv_load_national_id);

        carFrontCv = findViewById(R.id.cv_car_front);
        carFrontCv.setOnClickListener(this);
        carFrontLl = findViewById(R.id.ll_car_front);
        uploadCarFront = findViewById(R.id.tv_upload_car_front);
        carFrontIv = findViewById(R.id.iv_car_front);
        frontCloseIv = findViewById(R.id.iv_front_close);
        frontCloseIv.setOnClickListener(this);
        frontCloseIv.setVisibility(View.GONE);
        loaderCarFrontPv = findViewById(R.id.pv_load_car_front);

        carBackCv = findViewById(R.id.cv_car_back);
        carBackCv.setOnClickListener(this);
        carBackLl = findViewById(R.id.ll_car_back);
        uploadCarBack = findViewById(R.id.tv_upload_car_back);
        carBackIv = findViewById(R.id.iv_car_back);
        backCloseIv = findViewById(R.id.iv_back_close);
        backCloseIv.setOnClickListener(this);
        backCloseIv.setVisibility(View.GONE);
        loaderCarBackPv = findViewById(R.id.pv_load_car_back);

        submitRequestBtn = findViewById(R.id.btn_submit_request);
        submitRequestBtn.setOnClickListener(this);
        loaderPv = findViewById(R.id.pv_load);

        disableSubmitBtn();
    }

    private void setFonts() {
        fonts = MApplication.getInstance().getFonts();
        titleTv.setTypeface(fonts.customFontBD());
        personalInfoTv.setTypeface(fonts.customFontBD());
        fullNameTv.setTypeface(fonts.customFont());
        phoneNoTv.setTypeface(fonts.customFont());
        cityTv.setTypeface(fonts.customFont());
        carDetailsTv.setTypeface(fonts.customFontBD());
        carBrandEt.setTypeface(fonts.customFont());
        uploadLicenseTv.setTypeface(fonts.customFont());
        uploadIdTv.setTypeface(fonts.customFont());
        uploadCarFront.setTypeface(fonts.customFont());
        uploadCarBack.setTypeface(fonts.customFont());
        submitRequestBtn.setTypeface(fonts.customFontBD());
    }

    private void setLanguage() {
        if (localSettings.getLocale().equals(Constants.ARABIC)) {
            LanguageUtilities.switchToArabicLocale(this);
        }
    }

    private void setUserData() {
        fullNameTv.setText(localSettings.getUser().getName());
        phoneNoTv.setText(localSettings.getUser().getMobile());
        cityTv.setText(localSettings.getUser().getCity());
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

    private void switchToMyAccount(){
        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        mainIntent.putExtra(Constants.OPEN_FRAGMENT,MyAccountFragment.class.getSimpleName());
        startActivity(mainIntent);
    }

    private void submitRequest(){
        ArrayList<Long> imagesIds = new ArrayList<>();
        if (licenseImageId != 0){
            imagesIds.add(licenseImageId);
        }
        if (idImageId != 0){
            imagesIds.add(idImageId);
        }
        if (frontImageId != 0){
            imagesIds.add(frontImageId);
        }
        if (backImageId != 0){
            imagesIds.add(backImageId);
        }
        Gson gson = new Gson();
        String idsJson = gson.toJson(imagesIds);
        delegateRequestPresenter.submitDelegateRequest(localSettings.getToken(), localSettings.getLocale(), carBrandEt.getText().toString(), idsJson);

    }

    private void enableSubmitBtn(){
        submitRequestBtn.setAlpha(1);
        submitRequestBtn.setEnabled(true);
    }

    private void disableSubmitBtn(){
        submitRequestBtn.setAlpha((float)0.5);
        submitRequestBtn.setEnabled(false);
    }

    private void validateRequestFields(){
        if (!carBrandEt.getText().toString().isEmpty() && licenseImageId != 0 && idImageId != 0 && frontImageId != 0 && backImageId != 0){
            enableSubmitBtn();
        }
        else {
            disableSubmitBtn();
        }
    }

    @Override
    public void onClick(View view) {
        hideKeyboard();
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;

            case R.id.ll_main_content:
                hideKeyboard();
                break;

            case R.id.cv_license:
                currentImageType = LICENSE;
                showGalleryOrCameraDialog();
                break;

            case R.id.cv_id:
                currentImageType = PERSONAL_ID;
                showGalleryOrCameraDialog();
                break;

            case R.id.cv_car_front:
                currentImageType = CAR_FRONT;
                showGalleryOrCameraDialog();
                break;

            case R.id.cv_car_back:
                currentImageType = CAR_BACK;
                showGalleryOrCameraDialog();
                break;

            case R.id.iv_license_close:
                delegateRequestPresenter.deleteDelegateImages(localSettings.getToken(),localSettings.getLocale(),licenseImageId,LICENSE);
                break;

            case R.id.iv_id_close:
                delegateRequestPresenter.deleteDelegateImages(localSettings.getToken(),localSettings.getLocale(),idImageId,PERSONAL_ID);
                break;

            case R.id.iv_front_close:
                delegateRequestPresenter.deleteDelegateImages(localSettings.getToken(),localSettings.getLocale(),frontImageId,CAR_FRONT);
                break;

            case R.id.iv_back_close:
                delegateRequestPresenter.deleteDelegateImages(localSettings.getToken(),localSettings.getLocale(),backImageId,CAR_BACK);
                break;

            case R.id.btn_submit_request:
                submitRequest();
                break;
        }
    }

    @Override
    public void onRequestGallery(Intent gallery) {
        startActivityForResult(gallery, Constants.GALLERY);
    }

    @Override
    public void onRequestCamera(Intent camera, File imageFile) {
        if(currentImageType != null){
            switch (currentImageType) {
                case LICENSE:
                    this.licenseImageFile = imageFile;
                    break;

                case PERSONAL_ID:
                    this.idImageFile = imageFile;
                    break;

                case CAR_FRONT:
                    this.carFrontImageFile = imageFile;
                    break;

                case CAR_BACK:
                    this.carBackImageFile = imageFile;
                    break;
            }
            startActivityForResult(camera, Constants.CAMERA);
        }
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
        Log.e("result", "onactivityresult");
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.GALLERY) {
                if (data != null) {
                    Uri selectedImage = data.getData();
                    String imagePath = AddImageUtilities.getImagePath(selectedImage, this);
                    if (validateImage(imagePath)) {
                        File file = new File(imagePath);
                        if(currentImageType != null){
                            switch (currentImageType) {
                                case LICENSE:
                                    this.licenseImageFile = file;
                                    AddImageUtilities.resizeImage(licenseImageFile);
                                    delegateRequestPresenter.uploadDelegateImages(localSettings.getToken(), localSettings.getLocale(), currentImageType, licenseImageFile);
                                    break;

                                case PERSONAL_ID:
                                    this.idImageFile = file;
                                    AddImageUtilities.resizeImage(idImageFile);
                                    delegateRequestPresenter.uploadDelegateImages(localSettings.getToken(), localSettings.getLocale(), currentImageType, idImageFile);
                                    break;

                                case CAR_FRONT:
                                    this.carFrontImageFile = file;
                                    AddImageUtilities.resizeImage(carFrontImageFile);
                                    delegateRequestPresenter.uploadDelegateImages(localSettings.getToken(), localSettings.getLocale(), currentImageType, carFrontImageFile);
                                    break;

                                case CAR_BACK:
                                    this.carBackImageFile = file;
                                    AddImageUtilities.resizeImage(carBackImageFile);
                                    delegateRequestPresenter.uploadDelegateImages(localSettings.getToken(), localSettings.getLocale(), currentImageType, carBackImageFile);
                                    break;
                            }
                        }
                    }
                }
            } else {
                String imagePath;
                if(currentImageType != null){
                    switch (currentImageType) {
                        case LICENSE:
                            if (this.licenseImageFile != null) {
                                imagePath = this.licenseImageFile.getAbsolutePath();
                                if (validateImage(imagePath)) {
                                    AddImageUtilities.resizeImage(licenseImageFile);

                                    delegateRequestPresenter.uploadDelegateImages(localSettings.getToken(), localSettings.getLocale(), currentImageType, licenseImageFile);
                                }
                            }
                            break;

                        case PERSONAL_ID:
                            if (this.idImageFile != null) {
                                imagePath = this.idImageFile.getAbsolutePath();
                                if (validateImage(imagePath)) {
                                    AddImageUtilities.resizeImage(idImageFile);

                                    delegateRequestPresenter.uploadDelegateImages(localSettings.getToken(), localSettings.getLocale(), currentImageType, idImageFile);
                                }
                            }
                            break;

                        case CAR_FRONT:
                            if (this.carFrontImageFile != null) {
                                imagePath = this.carFrontImageFile.getAbsolutePath();
                                if (validateImage(imagePath)) {
                                    AddImageUtilities.resizeImage(carFrontImageFile);
                                    delegateRequestPresenter.uploadDelegateImages(localSettings.getToken(), localSettings.getLocale(), currentImageType, carFrontImageFile);
                                }
                            }
                            break;

                        case CAR_BACK:
                            if (this.carBackImageFile != null) {
                                imagePath = this.carBackImageFile.getAbsolutePath();
                                if (validateImage(imagePath)) {
                                    AddImageUtilities.resizeImage(carBackImageFile);
                                    delegateRequestPresenter.uploadDelegateImages(localSettings.getToken(), localSettings.getLocale(), currentImageType, carBackImageFile);
                                }
                            }
                            break;
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
    public void showImageLoading(String imageType) {
        switch (imageType) {
            case LICENSE:
                loaderLicensePv.setVisibility(View.VISIBLE);
                licenseCv.setEnabled(false);
                licenseIv.setEnabled(false);
                break;

            case PERSONAL_ID:
                loaderNationalIdPv.setVisibility(View.VISIBLE);
                idCv.setEnabled(false);
                idIv.setEnabled(false);
                break;

            case CAR_FRONT:
                loaderCarFrontPv.setVisibility(View.VISIBLE);
                carFrontCv.setEnabled(false);
                carFrontIv.setEnabled(false);
                break;

            case CAR_BACK:
                loaderCarBackPv.setVisibility(View.VISIBLE);
                carBackCv.setEnabled(false);
                carBackIv.setEnabled(false);
                break;
        }
    }

    @Override
    public void hideImageLoading(String imageType) {
        switch (imageType) {
            case LICENSE:
                loaderLicensePv.setVisibility(View.GONE);
                licenseCv.setEnabled(true);
                licenseCloseIv.setEnabled(true);
                break;

            case PERSONAL_ID:
                loaderNationalIdPv.setVisibility(View.GONE);
                idCv.setEnabled(true);
                idIv.setEnabled(true);
                break;

            case CAR_FRONT:
                loaderCarFrontPv.setVisibility(View.GONE);
                carFrontCv.setEnabled(true);
                carFrontIv.setEnabled(true);
                break;

            case CAR_BACK:
                loaderCarBackPv.setVisibility(View.GONE);
                carBackCv.setEnabled(true);
                carBackIv.setEnabled(true);
                break;
        }
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
    public void showHasRequestError() {
        Toast.makeText(this, R.string.has_request_error, Toast.LENGTH_LONG).show();
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
    public void showSuccessUpload(UploadDelegateImagesResponse response) {

        switch (response.getData().getType()) {
            case LICENSE:
                licenseLl.setVisibility(View.GONE);
                licenseCloseIv.setVisibility(View.VISIBLE);
                Picasso.with(this).load(response.getData().getImage().getMedium()).placeholder(R.drawable.delegate_louding_img).into(licenseIv);
                licenseImageId = response.getData().getId();
                break;

            case PERSONAL_ID:
                idLl.setVisibility(View.GONE);
                idCloseIv.setVisibility(View.VISIBLE);
                Picasso.with(this).load(response.getData().getImage().getMedium()).placeholder(R.drawable.delegate_louding_img).into(idIv);
                idImageId = response.getData().getId();
                break;

            case CAR_FRONT:
                carFrontLl.setVisibility(View.GONE);
                frontCloseIv.setVisibility(View.VISIBLE);
                Picasso.with(this).load(response.getData().getImage().getMedium()).placeholder(R.drawable.delegate_louding_img).into(carFrontIv);
                frontImageId = response.getData().getId();
                break;

            case CAR_BACK:
                carBackLl.setVisibility(View.GONE);
                backCloseIv.setVisibility(View.VISIBLE);
                Picasso.with(this).load(response.getData().getImage().getMedium()).placeholder(R.drawable.delegate_louding_img).into(carBackIv);
                backImageId = response.getData().getId();
                break;
        }

        validateRequestFields();
    }

    @Override
    public void showSuccessDelete(String deletedType) {
        switch (deletedType){
            case LICENSE:
                licenseImageId = 0;
                licenseIv.setImageDrawable(null);
                licenseLl.setVisibility(View.VISIBLE);
                licenseCloseIv.setVisibility(View.GONE);
                break;

            case PERSONAL_ID:
                idImageId = 0;
                idIv.setImageDrawable(null);
                idLl.setVisibility(View.VISIBLE);
                idCloseIv.setVisibility(View.GONE);
                break;

            case CAR_FRONT:
                frontImageId = 0;
                carFrontIv.setImageDrawable(null);
                carFrontLl.setVisibility(View.VISIBLE);
                frontCloseIv.setVisibility(View.GONE);
                break;

            case CAR_BACK:
                backImageId = 0;
                carBackIv.setImageDrawable(null);
                carBackLl.setVisibility(View.VISIBLE);
                backCloseIv.setVisibility(View.GONE);
                break;
        }

        disableSubmitBtn();
    }

    @Override
    public void showSuccessSubmit() {
        User user = localSettings.getUser();
        user.setHasDelegateRequest(true);
        localSettings.setUser(user);
        Toast.makeText(this, R.string.request_submitted_successfully, Toast.LENGTH_LONG).show();
        switchToMyAccount();
    }

    @Override
    public void hideTokenLoader() {
        hideLoading();
    }
}
