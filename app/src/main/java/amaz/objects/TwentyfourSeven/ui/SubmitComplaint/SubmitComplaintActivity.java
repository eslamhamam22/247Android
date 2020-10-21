package amaz.objects.TwentyfourSeven.ui.SubmitComplaint;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rey.material.widget.ProgressView;

import amaz.objects.TwentyfourSeven.BaseActivity;
import amaz.objects.TwentyfourSeven.MApplication;
import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.injection.Injection;
import amaz.objects.TwentyfourSeven.listeners.OnRefeshTokenResponse;
import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import amaz.objects.TwentyfourSeven.presenter.PresenterFactory;
import amaz.objects.TwentyfourSeven.ui.MyComplaints.MyComplaintsActivity;
import amaz.objects.TwentyfourSeven.ui.RegisterOrLogin.RegisterOrLoginActivity;
import amaz.objects.TwentyfourSeven.utilities.Constants;
import amaz.objects.TwentyfourSeven.utilities.Fonts;
import amaz.objects.TwentyfourSeven.utilities.LanguageUtilities;
import amaz.objects.TwentyfourSeven.utilities.LocalSettings;
import amaz.objects.TwentyfourSeven.utilities.TokenUtilities;

public class SubmitComplaintActivity extends BaseActivity implements View.OnClickListener, SubmitComplaintPresenter.SubmitComplaintView,
        OnRefeshTokenResponse, TextWatcher {

    private RelativeLayout mainContentRl;
    private LinearLayout mainContentLl;
    private TextView submitComplaintTitleTv, orderNoTitleTv, orderNoContentTv, subjectTv, descriptionTv;
    private EditText subjectEt, descriptionEt;
    private Button submitComplaintBtn;
    private ImageView backIv;
    private ProgressView loaderPv;

    private LocalSettings localSettings;
    private Fonts fonts;

    private SubmitComplaintPresenter submitComplaintPresenter;
    private int orderId;

    @Override
    public void onPresenterAvailable(BasePresenter presenter) {
        submitComplaintPresenter = (SubmitComplaintPresenter) presenter;
        submitComplaintPresenter.setView(this);
    }

    @Override
    public PresenterFactory.PresenterType getPresenterType() {
        return PresenterFactory.PresenterType.SubmitComplaint;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localSettings = new LocalSettings(this);
        setLanguage();
        setContentView(R.layout.activity_submit_complaint);
        getDataFromIntent();
        initialization();
        setFonts();
    }

    private void getDataFromIntent() {
        orderId = getIntent().getIntExtra(Constants.ORDER, 0);
    }

    private void setLanguage() {
        if (localSettings.getLocale().equals(Constants.ARABIC)) {
            LanguageUtilities.switchToArabicLocale(this);
        }
    }

    private void initialization() {
        submitComplaintTitleTv = findViewById(R.id.tv_submit_complaint_title);
        backIv = findViewById(R.id.iv_back);
        backIv.setOnClickListener(this);
        if (localSettings.getLocale().equals(Constants.ARABIC)) {
            backIv.setImageResource(R.drawable.back_ar_ic);
        } else {
            backIv.setImageResource(R.drawable.back_ic);
        }

        mainContentRl = findViewById(R.id.rl_main_content);
        mainContentRl.setOnClickListener(this);
        mainContentLl = findViewById(R.id.ll_main_content);
        mainContentLl.setOnClickListener(this);
        orderNoTitleTv = findViewById(R.id.tv_order_no_title);
        orderNoContentTv = findViewById(R.id.tv_order_no_content);
        orderNoContentTv.setText(String.valueOf(orderId));
        subjectTv = findViewById(R.id.tv_complaint_title);
        subjectEt = findViewById(R.id.et_subject_content);
        subjectEt.addTextChangedListener(this);
        descriptionTv = findViewById(R.id.tv_description_title);
        descriptionEt = findViewById(R.id.et_description_content);
        descriptionEt.addTextChangedListener(this);
        submitComplaintBtn = findViewById(R.id.btn_submit_complaint);
        submitComplaintBtn.setOnClickListener(this);
        submitComplaintBtn.setEnabled(false);

        loaderPv = findViewById(R.id.pv_load);
    }


    private void setFonts() {
        fonts = MApplication.getInstance().getFonts();

        submitComplaintTitleTv.setTypeface(fonts.customFontBD());
        orderNoTitleTv.setTypeface(fonts.customFont());
        orderNoContentTv.setTypeface(fonts.customFont());
        subjectTv.setTypeface(fonts.customFont());
        subjectEt.setTypeface(fonts.customFont());
        descriptionTv.setTypeface(fonts.customFont());
        descriptionEt.setTypeface(fonts.customFont());
        submitComplaintBtn.setTypeface(fonts.customFontBD());
    }

    private void switchToRegisterationOrLogin() {
        Intent registerationOrLoginIntent = new Intent(this, RegisterOrLoginActivity.class);
        registerationOrLoginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(registerationOrLoginIntent);
    }

    public void switchToMyComplaints() {
        Intent myComplaintsIntent = new Intent(this, MyComplaintsActivity.class);
        startActivity(myComplaintsIntent);
        finish();
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
                finish();
                break;

            case R.id.btn_submit_complaint:
                hideKeyboard();
                submitComplaintPresenter.submitComplaint(localSettings.getToken(), localSettings.getLocale(), orderId,
                        subjectEt.getText().toString(), descriptionEt.getText().toString());
                break;

            case R.id.ll_main_content:
                hideKeyboard();
                break;

            case R.id.rl_main_content:
                hideKeyboard();
                break;
        }

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if ((!subjectEt.getText().toString().isEmpty() && !descriptionEt.getText().toString().isEmpty())) {
            submitComplaintBtn.setAlpha(1);
            submitComplaintBtn.setEnabled(true);
        } else {
            submitComplaintBtn.setAlpha((float) 0.5);
            submitComplaintBtn.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

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
    public void showSubjectError() {
        Toast.makeText(this, R.string.complaint_title_error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showDescriptionError() {
        Toast.makeText(this, R.string.complaint_description_error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showSuccessSubmit() {
        Toast.makeText(this, R.string.submit_complaint_message, Toast.LENGTH_LONG).show();
        switchToMyComplaints();
    }

    @Override
    public void hideTokenLoader() {
        hideLoading();
    }

}
