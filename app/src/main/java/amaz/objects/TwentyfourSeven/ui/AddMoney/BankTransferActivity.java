package amaz.objects.TwentyfourSeven.ui.AddMoney;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rey.material.widget.ProgressView;

import java.util.ArrayList;

import amaz.objects.TwentyfourSeven.BaseActivity;
import amaz.objects.TwentyfourSeven.MApplication;
import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.adapters.BankAccountsAdapter;
import amaz.objects.TwentyfourSeven.data.models.BankAccount;
import amaz.objects.TwentyfourSeven.injection.Injection;
import amaz.objects.TwentyfourSeven.listeners.OnRefeshTokenResponse;
import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import amaz.objects.TwentyfourSeven.presenter.PresenterFactory;
import amaz.objects.TwentyfourSeven.ui.RegisterOrLogin.RegisterOrLoginActivity;
import amaz.objects.TwentyfourSeven.utilities.Constants;
import amaz.objects.TwentyfourSeven.utilities.Fonts;
import amaz.objects.TwentyfourSeven.utilities.LanguageUtilities;
import amaz.objects.TwentyfourSeven.utilities.LocalSettings;
import amaz.objects.TwentyfourSeven.utilities.TokenUtilities;

public class BankTransferActivity extends BaseActivity implements BankTransferPresenter.BankTransferView, View.OnClickListener,
        OnRefeshTokenResponse {

    private TextView tv_title_bank_transfer, tv_bank_account_title, tv_submit_transfer;
    private Fonts fonts;
    private BankTransferPresenter bankTransferPresenter;
    private LocalSettings localSettings;
    private ProgressView pvLoad;
    private ArrayList<BankAccount> bankAccounts = new ArrayList<>();
    private RecyclerView bankAccountsRV;
    private ImageView backIv, errorIv;
    private TextView errorTv;
    private BankAccountsAdapter bankAccountsAdapter;
    private RelativeLayout errorRl, mainContentRV;
    private TextView noConnectionTitleTv, noConnectionMessageTv;
    private LinearLayout noConnectionLl;
    private Button reloadBtn;

    @Override
    public PresenterFactory.PresenterType getPresenterType() {
        return PresenterFactory.PresenterType.MYBANKTRANSFER;
    }

    @Override
    public void onPresenterAvailable(BasePresenter presenter) {
        bankTransferPresenter = (BankTransferPresenter) presenter;
        bankTransferPresenter.setView(this);
        bankTransferPresenter.getBankAccounts(localSettings.getLocale(), localSettings.getToken());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localSettings = new LocalSettings(this);
        setLanguage();
        setContentView(R.layout.activity_bank_transfer);
        initialization();
        setFonts();
    }


    @Override
    public void onResume() {
        super.onResume();
        setLanguage();
    }

    private void initialization() {
        tv_title_bank_transfer = (TextView) findViewById(R.id.tv_title_bank_transfer);
        tv_bank_account_title = (TextView) findViewById(R.id.tv_bank_account_title);
        bankAccountsRV = (RecyclerView) findViewById(R.id.rv_bank_accounts);
        tv_submit_transfer = (TextView) findViewById(R.id.tv_submit_transfer);
        bankAccountsRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        bankAccountsAdapter = new BankAccountsAdapter(this, bankAccounts);
        bankAccountsRV.setAdapter(bankAccountsAdapter);

        backIv = findViewById(R.id.iv_back);
        pvLoad = findViewById(R.id.pv_load);
        if (localSettings.getLocale().equals(Constants.ARABIC)) {
            backIv.setImageResource(R.drawable.back_ar_ic);
        } else {
            backIv.setImageResource(R.drawable.back_ic);
        }
        backIv.setOnClickListener(this);

        errorRl = findViewById(R.id.rl_error);
        errorIv = findViewById(R.id.iv_error);
        errorTv = findViewById(R.id.tv_error);
        mainContentRV = findViewById(R.id.mainContentRV);

        noConnectionLl = findViewById(R.id.ll_no_connection);
        noConnectionTitleTv = findViewById(R.id.tv_no_connection_title);
        noConnectionMessageTv = findViewById(R.id.tv_no_connection_message);
        reloadBtn = findViewById(R.id.btn_reload_page);
        reloadBtn.setOnClickListener(this);
        tv_submit_transfer.setOnClickListener(this);
        fonts = MApplication.getInstance().getFonts();


    }

    private void setFonts() {
        tv_bank_account_title.setTypeface(fonts.customFont());
        tv_title_bank_transfer.setTypeface(fonts.customFontBD());
        tv_submit_transfer.setTypeface(fonts.customFontBD());
        errorTv.setTypeface(fonts.customFont());
        noConnectionTitleTv.setTypeface(fonts.customFont());
        noConnectionMessageTv.setTypeface(fonts.customFont());
        reloadBtn.setTypeface(fonts.customFontBD());
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
    public void setBankAccounts(ArrayList<BankAccount> bankAccounts) {
        if (errorRl.getVisibility() == View.VISIBLE) {
            errorRl.setVisibility(View.GONE);
        }
        mainContentRV.setVisibility(View.VISIBLE);
        noConnectionLl.setVisibility(View.GONE);
        this.bankAccounts.clear();
        this.bankAccounts.addAll(bankAccounts);
        bankAccountsAdapter.notifyDataSetChanged();

    }

    @Override
    public void showNoData() {
        errorRl.setVisibility(View.VISIBLE);
        mainContentRV.setVisibility(View.GONE);

        errorIv.setImageResource(R.drawable.grayscale);
        errorTv.setText(R.string.no_data_error);
    }

    @Override
    public void showNetworkError() {
        mainContentRV.setVisibility(View.GONE);
        noConnectionLl.setVisibility(View.VISIBLE);

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


    private void switchToRegisterationOrLogin() {

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
            case R.id.btn_reload_page:
                bankTransferPresenter.getBankAccounts(localSettings.getLocale(), localSettings.getToken());
                break;
            case R.id.tv_submit_transfer:
                Intent intent = new Intent(this, SubmitTransactionActivity.class);
                intent.putExtra(Constants.BANK_ACCOUNTS, bankAccounts);
                startActivity(intent);
                break;
        }

    }
}
