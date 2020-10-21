package amaz.objects.TwentyfourSeven.ui.AddMoney;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rey.material.widget.ProgressView;

import java.io.File;
import java.util.ArrayList;

import amaz.objects.TwentyfourSeven.BaseActivity;
import amaz.objects.TwentyfourSeven.MApplication;
import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.data.models.BankAccount;
import amaz.objects.TwentyfourSeven.injection.Injection;
import amaz.objects.TwentyfourSeven.listeners.OnRefeshTokenResponse;
import amaz.objects.TwentyfourSeven.listeners.OnRequestImageIntentListener;
import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import amaz.objects.TwentyfourSeven.presenter.PresenterFactory;
import amaz.objects.TwentyfourSeven.ui.MyBalance.MyBalanceActivity;
import amaz.objects.TwentyfourSeven.ui.RegisterOrLogin.RegisterOrLoginActivity;
import amaz.objects.TwentyfourSeven.utilities.AddImageUtilities;
import amaz.objects.TwentyfourSeven.utilities.Constants;
import amaz.objects.TwentyfourSeven.utilities.Fonts;
import amaz.objects.TwentyfourSeven.utilities.LanguageUtilities;
import amaz.objects.TwentyfourSeven.utilities.LocalSettings;
import amaz.objects.TwentyfourSeven.utilities.TokenUtilities;

public class SubmitTransactionActivity extends BaseActivity implements SubmitTransactionPresenter.SubmitTranscationView, View.OnClickListener,
         OnRefeshTokenResponse, Spinner.OnItemSelectedListener, OnRequestImageIntentListener {

    private TextView tv_submit_trans_title,tv_choose_bank,tv_transfer_title,tv_attatch_doc,tv_amount_bank,tv_transfer_document,tv_submit;
    private Fonts fonts;
    private SubmitTransactionPresenter submitTransactionPresenter;
    private LocalSettings localSettings;
    private ProgressView pvLoad;
    private ImageView backIv,errorIv;
    private TextView errorTv;
    private RelativeLayout upload_doc;
    private Spinner payment_spinner;
    private ArrayList<BankAccount> bankAccounts = new ArrayList<>();
    private ArrayList<String> bank_accounts_str = new ArrayList<>();

    private ArrayAdapter adpter;
    private File imageFile;
    private ImageView iv_uploaded_image,iv_delete;
    private RelativeLayout card_image_rv;
      private String amount_value = "";
      private String selected_bank_acount = "";
      private EditText amountET;
    @Override
    public PresenterFactory.PresenterType getPresenterType() {
        return PresenterFactory.PresenterType.SUBMITTRANSACTION;
    }

    @Override
    public void onPresenterAvailable(BasePresenter presenter) {
        submitTransactionPresenter = (SubmitTransactionPresenter) presenter;
        submitTransactionPresenter.setView(this);

    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localSettings = new LocalSettings(this);
        setLanguage();
        setContentView(R.layout.activity_submit_transaction);
        getDataFromIntent();
        initialization();
        addEditTextTouchListenner();
        setFonts();
    }

   
    @Override
    public void onResume() {
        super.onResume();
        setLanguage();
    }

    private void getDataFromIntent(){

        bankAccounts = (ArrayList<BankAccount>) getIntent().getSerializableExtra(Constants.BANK_ACCOUNTS);
        if (bankAccounts != null){
            bank_accounts_str.add(getString(R.string.choose_bank));
            for (int i= 0;i<bankAccounts.size();i++){
                bank_accounts_str.add(bankAccounts.get(i).getBank_name());
            }
        }else{
            bankAccounts = new ArrayList<BankAccount>();
        }


    }

    private void initialization(){
        tv_submit_trans_title = (TextView) findViewById(R.id.tv_submit_trans_title);
        tv_choose_bank = (TextView) findViewById(R.id.tv_choose_bank);
        tv_transfer_title = (TextView) findViewById(R.id.tv_transfer_title);
        payment_spinner = (Spinner) findViewById(R.id.payment_spinner);
        tv_transfer_document = (TextView) findViewById(R.id.tv_transfer_document);
        tv_attatch_doc = (TextView) findViewById(R.id.tv_attatch_doc);
        upload_doc = (RelativeLayout) findViewById(R.id.upload_doc);
        tv_submit =  (TextView) findViewById(R.id.tv_submit);
        tv_amount_bank =  (TextView) findViewById(R.id.tv_amount_bank);
        amountET =  (EditText) findViewById(R.id.et_amount);

        iv_uploaded_image =  (ImageView) findViewById(R.id.iv_uploaded_image);
        card_image_rv = findViewById(R.id.card_image_rv);
        iv_delete = findViewById(R.id.iv_delete);
        upload_doc.setOnClickListener(this);
        iv_delete.setOnClickListener(this);
        backIv = findViewById(R.id.iv_back);
        pvLoad = findViewById(R.id.pv_load);
        if (localSettings.getLocale().equals(Constants.ARABIC)) {
            backIv.setImageResource(R.drawable.back_ar_ic);
        } else {
            backIv.setImageResource(R.drawable.back_ic);
        }
        backIv.setOnClickListener(this);

        fonts = MApplication.getInstance().getFonts();

        card_image_rv.setVisibility(View.GONE);
        adpter = new ArrayAdapter<String>(this,
                R.layout.spinneritem, bank_accounts_str) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
//                ((TextView) v).setTextSize(16);
                ((TextView) v).setGravity(Gravity.CENTER);
                ((TextView) v).setTypeface(fonts.customFont());
                ((TextView) v).setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                if (position == 0){
                    ((TextView) v).setTextColor(getResources().getColor(R.color.line_gray));

                }else{
                    ((TextView) v).setTextColor(getResources().getColor(R.color.gray));

                }
                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {

                View v = super.getDropDownView(position, convertView, parent);

                ((TextView) v).setGravity(Gravity.CENTER);
                ((TextView) v).setTypeface(fonts.customFont());

                ((TextView) v).setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                if (position == 0){
                    ((TextView) v).setTextColor(getResources().getColor(R.color.line_gray));

                }else{
                    ((TextView) v).setTextColor(getResources().getColor(R.color.gray));

                }
                return v;

            }

        };
        adpter.setDropDownViewResource(  R.layout.spinner_dropdown_item);

        payment_spinner.setAdapter(adpter);
        //upload_doc.setEnabled(false);
       // upload_doc.setAlpha(0.5f);

        payment_spinner.setOnItemSelectedListener(this);
        tv_submit.setOnClickListener(this);
    }
    private void addEditTextTouchListenner() {

        amountET.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                   amount_value = s.toString();
                if (s.toString().trim().length() == 0) {
                    tv_submit.setEnabled(false);
                    tv_submit.setAlpha(0.5f);


                } else {
                    if ((!(selected_bank_acount.equals("")) )&& upload_doc.getVisibility() == View.GONE ) {
                        tv_submit.setEnabled(true);
                        tv_submit.setAlpha(1);
                    }

                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void setFonts(){
         tv_attatch_doc.setTypeface(fonts.customFontBD());
         tv_choose_bank.setTypeface(fonts.customFont());
         tv_submit.setTypeface(fonts.customFont());
         tv_submit_trans_title.setTypeface(fonts.customFont());
         tv_transfer_document.setTypeface(fonts.customFontBD());
         tv_transfer_title.setTypeface(fonts.customFont());
        tv_amount_bank.setTypeface(fonts.customFont());
    }

    private void setLanguage() {

            if (localSettings.getLocale().equals(Constants.ARABIC)) {
                LanguageUtilities.switchToArabicLocale(this);
            }

    }

    @Override
    public void hideLoading() {
        pvLoad.setVisibility(View.GONE);

    }

    @Override
    public void showLoading() {
        pvLoad.setVisibility(View.VISIBLE);

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
    public void showValidationError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showSucessTransferNote() {
        Toast.makeText(this, R.string.sucess_transfer, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MyBalanceActivity.class);
        intent.putExtra(Constants.OPEN_BALANCE,true);
        finish();
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.putExtra(Constants.OPEN_FRAGMENT,MyAccountFragment.class.getSimpleName());
        startActivity(intent);
        // log
    }

    @Override
    public void enableSubmit() {
        tv_submit.setEnabled(true);
        tv_submit.setAlpha(1);
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

    private void switchToRegisterationOrLogin(){
            Intent registerationOrLoginIntent = new Intent(this, RegisterOrLoginActivity.class);
            registerationOrLoginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(registerationOrLoginIntent);

    }

    @Override
    public void hideTokenLoader() {
        hideLoading();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.iv_back:
                finish();
                break;
            case R.id.bankTransferRV:
                Intent intent = new Intent(this,BankTransferActivity.class);
                startActivity(intent);
                break;
            case R.id.upload_doc:
                showGalleryOrCameraDialog();
                break;
            case R.id.iv_delete:
                card_image_rv.setVisibility(View.GONE);
                upload_doc.setVisibility(View.VISIBLE);
                tv_submit.setEnabled(false);
                tv_submit.setAlpha(0.5f);

                break;
            case R.id.tv_submit:
                tv_submit.setAlpha(0.5f);
                tv_submit.setEnabled(false);
                AddImageUtilities.resizeImage(imageFile);
                float amount = 0;
                amount = Float.parseFloat(amount_value);
                submitTransactionPresenter.uploadBankTransfer(localSettings.getLocale(),localSettings.getToken(),imageFile,amount_value,selected_bank_acount);

                break;
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (i == 0){
//            upload_doc.setEnabled(false);
//            upload_doc.setAlpha(0.5f);
            tv_submit.setEnabled(false);
            tv_submit.setAlpha(0.5f);
            selected_bank_acount= "";

        }else {
//            upload_doc.setEnabled(true);
//            upload_doc.setAlpha(1);
            selected_bank_acount= bankAccounts.get(i-1).getId();

            if (upload_doc.getVisibility() == View.GONE && (!amount_value.equals(""))) {
                tv_submit.setEnabled(true);
                tv_submit.setAlpha(1);
            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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
                        Glide.with(this).load(imageFile)
                                .apply(new RequestOptions()
                                        .placeholder(R.drawable.default_image)
                                        .dontAnimate())
                                .into(iv_uploaded_image);
                                //.placeholder(R.drawable.default_image).dontAnimate().into(iv_uploaded_image);
                        card_image_rv.setVisibility(View.VISIBLE);
                        upload_doc.setVisibility(View.GONE);
                        if ((!selected_bank_acount.equals("") )&& (!amount_value.equals(""))) {
                            tv_submit.setEnabled(true);
                            tv_submit.setAlpha(1);
                        }
                    }

                }

            } else if (requestCode == Constants.CAMERA) {
                String imagePath;
                if (this.imageFile != null) {
                    imagePath = this.imageFile.getAbsolutePath();
                    if (validateImage(imagePath)) {
                        AddImageUtilities.resizeImage(imageFile);
                        Glide.with(this).load(imageFile)
                                .apply(new RequestOptions()
                                        .placeholder(R.drawable.default_image))
                                .into(iv_uploaded_image);
                                //.placeholder(R.drawable.default_image).into(iv_uploaded_image);
                        card_image_rv.setVisibility(View.VISIBLE);
                        upload_doc.setVisibility(View.GONE);
                        if (!selected_bank_acount.equals("") && !amount_value.equals("")) {
                            tv_submit.setEnabled(true);
                            tv_submit.setAlpha(1);
                        }

                    }
                }


            }
        }


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

}
