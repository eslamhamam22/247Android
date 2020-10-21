package amaz.objects.TwentyfourSeven.ui.Settings;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rey.material.widget.ProgressView;

import amaz.objects.TwentyfourSeven.BaseFragment;
import amaz.objects.TwentyfourSeven.MApplication;
import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.data.models.User;
import amaz.objects.TwentyfourSeven.injection.Injection;
import amaz.objects.TwentyfourSeven.listeners.OnRefeshTokenResponse;
import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import amaz.objects.TwentyfourSeven.presenter.PresenterFactory;
import amaz.objects.TwentyfourSeven.ui.MyAccount.MainActivity;
import amaz.objects.TwentyfourSeven.ui.RegisterOrLogin.RegisterOrLoginActivity;
import amaz.objects.TwentyfourSeven.utilities.Constants;
import amaz.objects.TwentyfourSeven.utilities.Fonts;
import amaz.objects.TwentyfourSeven.utilities.LanguageUtilities;
import amaz.objects.TwentyfourSeven.utilities.LocalSettings;
import amaz.objects.TwentyfourSeven.utilities.TokenUtilities;


public class SettingsFragment extends BaseFragment implements View.OnClickListener, SettingsPresenter.SettingsView, OnRefeshTokenResponse {

    private TextView titleTv, languageTitleTv, languageContentTv, howUseTv, contactUsTv, aboutUsTv, termsTv, returnPolicyTv, rateAppTv, shareAppTv,
            popupEnglishTv, popupArabicTv;
    private ImageView popupcheckedEnglishIv, popupcheckedArabicIv;
    private RelativeLayout languageRl, mainRl;
    private PopupWindow languagePopupWindow;
    private LinearLayout popupLanguageLl;
    private ProgressView loaderPv;

    private LocalSettings localSettings;
    private Fonts fonts;

    private SettingsPresenter settingsPresenter;

    private String language;

    @Override
    public void onPresenterAvailable(BasePresenter presenter) {
        settingsPresenter = (SettingsPresenter) presenter;
        settingsPresenter.setView(this);
    }

    @Override
    public PresenterFactory.PresenterType getPresenterType() {
        return PresenterFactory.PresenterType.Settings;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings,container,false);
        initializeView(view);
        initializeLanguagePopup();
        setFonts();
        return view;
    }

    private void initializeView(View view){
        localSettings = new LocalSettings(getActivity());
        mainRl = view.findViewById(R.id.rl_main);
        titleTv = view.findViewById(R.id.tv_title_settings);
        languageRl = view.findViewById(R.id.rl_language);
        languageRl.setOnClickListener(this);
        languageTitleTv = view.findViewById(R.id.tv_language_title);
        languageContentTv = view.findViewById(R.id.tv_language_content);
        howUseTv = view.findViewById(R.id.tv_how_use);
        howUseTv.setOnClickListener(this);
        contactUsTv = view.findViewById(R.id.tv_contact_us);
        contactUsTv.setOnClickListener(this);
        aboutUsTv = view.findViewById(R.id.tv_about_us);
        aboutUsTv.setOnClickListener(this);
        termsTv = view.findViewById(R.id.tv_terms_conditions);
        termsTv.setOnClickListener(this);
        returnPolicyTv = view.findViewById(R.id.tv_return_policy);
        returnPolicyTv.setOnClickListener(this);
        rateAppTv = view.findViewById(R.id.tv_rate_app);
        rateAppTv.setOnClickListener(this);
        shareAppTv = view.findViewById(R.id.tv_share_app);
        shareAppTv.setOnClickListener(this);
        loaderPv = view.findViewById(R.id.pv_load);

        if (localSettings.getLocale().equals(Constants.ENGLISH)){
            languageContentTv.setText(R.string.english);
        }
        else {
            languageContentTv.setText(R.string.arabic);
        }
    }

    private void initializeLanguagePopup(){
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_language,null,false);
        languagePopupWindow = new PopupWindow(v, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        popupLanguageLl = v.findViewById(R.id.ll_language);
        popupLanguageLl.setOnClickListener(this);
        popupEnglishTv = v.findViewById(R.id.tv_english);
        popupEnglishTv.setOnClickListener(this);
        popupArabicTv = v.findViewById(R.id.tv_arabic);
        popupArabicTv.setOnClickListener(this);
        popupcheckedEnglishIv = v.findViewById(R.id.iv_checked_english);
        popupcheckedArabicIv = v.findViewById(R.id.iv_checked_arabic);

        if (localSettings.getLocale() != null){
            if (localSettings.getLocale().equals(Constants.ENGLISH)){
                selectEnglishLanguage();
            }
            else {
                selectArabicLanguage();
            }
        }
        else {
            selectEnglishLanguage();
        }
    }

    private void setFonts(){
        fonts = MApplication.getInstance().getFonts();
        titleTv.setTypeface(fonts.customFontBD());
        languageTitleTv.setTypeface(fonts.customFont());
        languageContentTv.setTypeface(fonts.customFont());
        howUseTv.setTypeface(fonts.customFont());
        contactUsTv.setTypeface(fonts.customFont());
        aboutUsTv.setTypeface(fonts.customFont());
        termsTv.setTypeface(fonts.customFont());
        returnPolicyTv.setTypeface(fonts.customFont());
        rateAppTv.setTypeface(fonts.customFont());
        shareAppTv.setTypeface(fonts.customFont());
        popupEnglishTv.setTypeface(fonts.customFont());
        popupArabicTv.setTypeface(fonts.customFont());
    }

    private void openLanguagePopup(){
        languagePopupWindow.showAtLocation(mainRl, Gravity.CENTER, 0, 0);
    }

    private void selectArabicLanguage(){
        popupArabicTv.setTextColor(ContextCompat.getColor(getActivity(), R.color.hint_blue));
        popupcheckedArabicIv.setVisibility(View.VISIBLE);

        popupEnglishTv.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
        popupcheckedEnglishIv.setVisibility(View.GONE);

        language = Constants.ARABIC;
    }

    private void selectEnglishLanguage(){
        popupEnglishTv.setTextColor(ContextCompat.getColor(getActivity(), R.color.hint_blue));
        popupcheckedEnglishIv.setVisibility(View.VISIBLE);

        popupArabicTv.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
        popupcheckedArabicIv.setVisibility(View.GONE);

        language = Constants.ENGLISH;
    }

    private void resetLanguagePopup(){
        if (localSettings.getLocale().equals(Constants.ARABIC)){
            selectArabicLanguage();
        }
        else {
            selectEnglishLanguage();
        }
    }

    public boolean isLanguagePopupShown() {
        return languagePopupWindow.isShowing();
    }

    public void dismissLanguagePopup(){
        languagePopupWindow.dismiss();
    }

    private void switchToRegisterationOrLogin(){
        Intent registerationOrLoginIntent = new Intent(getActivity(),RegisterOrLoginActivity.class);
        registerationOrLoginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(registerationOrLoginIntent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_language:
                openLanguagePopup();
                break;

            case R.id.ll_language:
                languagePopupWindow.dismiss();
                break;

            case R.id.tv_english:
                if (!SettingsFragment.this.isDetached() && getActivity() != null) {
                    if (language.equals(Constants.ARABIC)){
                        selectEnglishLanguage();
                        languagePopupWindow.dismiss();
                        if (localSettings.getToken() != null){
                            settingsPresenter.changeUserLanguage(localSettings.getToken(),localSettings.getLocale(),language);
                        }
                        else {
                            showSuccess();
                        }
                    }
                }
                break;

            case R.id.tv_arabic:
                if (!SettingsFragment.this.isDetached() && getActivity() != null) {
                    if (language.equals(Constants.ENGLISH)){
                        selectArabicLanguage();
                        languagePopupWindow.dismiss();
                        if (localSettings.getToken() != null){
                            settingsPresenter.changeUserLanguage(localSettings.getToken(),localSettings.getLocale(),language);
                        }
                        else {
                            showSuccess();
                        }

                    }
                }
                break;

            case R.id.tv_terms_conditions:
                if (!SettingsFragment.this.isDetached() && getActivity() != null) {
                    ((MainActivity)getActivity()).switchToPages(Constants.TERMS_AND_CONDITIONS);
                }

                break;

            case R.id.tv_return_policy:
                if (!SettingsFragment.this.isDetached() && getActivity() != null) {
                    ((MainActivity)getActivity()).switchToPages(Constants.PRIVACY_POLICY);
                }
                break;

            case R.id.tv_about_us:
                if (!SettingsFragment.this.isDetached() && getActivity() != null) {
                    ((MainActivity)getActivity()).switchToAboutUs();
                }
                break;

            case R.id.tv_contact_us:
                if (!SettingsFragment.this.isDetached() && getActivity() != null) {
                    ((MainActivity)getActivity()).switchToContactUs();
                }
                break;

            case R.id.tv_how_use:
                if (!SettingsFragment.this.isDetached() && getActivity() != null) {
                    ((MainActivity)getActivity()).switchToHowToUse();
                }
                break;

            case R.id.tv_rate_app:
                if (!SettingsFragment.this.isDetached() && getActivity() != null) {
                    ((MainActivity)getActivity()).switchToGooglePlay();
                }
                break;

            case R.id.tv_share_app:
                if (!SettingsFragment.this.isDetached() && getActivity() != null) {
                    ((MainActivity)getActivity()).openSharePopup();
                }
                break;

        }
    }

    @Override
    public void showLoading() {
        if (!SettingsFragment.this.isDetached() && getActivity() != null) {
            loaderPv.setVisibility(View.VISIBLE);
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    @Override
    public void hideLoading() {
        if (!SettingsFragment.this.isDetached() && getActivity() != null) {
            loaderPv.setVisibility(View.GONE);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    @Override
    public void showInvalideTokenError() {
        if (!SettingsFragment.this.isDetached() && getActivity() != null) {
            Log.e("authError", "authentication error");
            resetLanguagePopup();
            TokenUtilities.refreshToken(getActivity(),
                    Injection.provideUserRepository(),
                    localSettings.getToken(),
                    localSettings.getLocale(),
                    localSettings.getRefreshToken(),
                    this);
        }
    }

    @Override
    public void showNetworkError() {
        if (!SettingsFragment.this.isDetached() && getActivity() != null) {
            resetLanguagePopup();
            Toast.makeText(getActivity(), R.string.connection_error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void showServerError() {
        if (!SettingsFragment.this.isDetached() && getActivity() != null) {
            resetLanguagePopup();
            Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void showSuspededUserError(String errorMessage) {

        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
        localSettings.removeToken();
        localSettings.removeRefreshToken();
        localSettings.removeUser();
        switchToRegisterationOrLogin();
    }

    @Override
    public void setUserData(User user) {
        if (!SettingsFragment.this.isDetached() && getActivity() != null) {
            localSettings.setUser(user);
        }
    }

    @Override
    public void showSuccess() {
        if (!SettingsFragment.this.isDetached() && getActivity() != null) {
            if (language.equals(Constants.ENGLISH)){
                localSettings.setLocale(Constants.ENGLISH);
                LanguageUtilities.switchToEnglishLocale(getActivity());
            }
            if (language.equals(Constants.ARABIC)){
                localSettings.setLocale(Constants.ARABIC);
                LanguageUtilities.switchToArabicLocale(getActivity());
            }
            if (localSettings.getLocale().equals(Constants.ENGLISH)){
                languageContentTv.setText(R.string.english);
            }
            else {
                languageContentTv.setText(R.string.arabic);
            }
            ((MainActivity)getActivity()).refreshActivity();
        }
    }

    @Override
    public void hideTokenLoader() {
        hideLoading();
    }
}
