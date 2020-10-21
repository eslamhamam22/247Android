package amaz.objects.TwentyfourSeven.ui.MyAddresses;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.dynamic.IFragmentWrapper;
import com.rey.material.widget.ProgressView;

import java.util.ArrayList;

import amaz.objects.TwentyfourSeven.BaseActivity;
import amaz.objects.TwentyfourSeven.MApplication;
import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.adapters.MyAddressesAdapter;
import amaz.objects.TwentyfourSeven.data.models.Address;
import amaz.objects.TwentyfourSeven.injection.Injection;
import amaz.objects.TwentyfourSeven.listeners.OnAddressClickListener;
import amaz.objects.TwentyfourSeven.listeners.OnRefeshTokenResponse;
import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import amaz.objects.TwentyfourSeven.presenter.PresenterFactory;
import amaz.objects.TwentyfourSeven.ui.AddAddress.AddAddressActivity;
import amaz.objects.TwentyfourSeven.ui.RegisterOrLogin.RegisterOrLoginActivity;
import amaz.objects.TwentyfourSeven.utilities.Constants;
import amaz.objects.TwentyfourSeven.utilities.Fonts;
import amaz.objects.TwentyfourSeven.utilities.LanguageUtilities;
import amaz.objects.TwentyfourSeven.utilities.LocalSettings;
import amaz.objects.TwentyfourSeven.utilities.TokenUtilities;

public class MyAddressesActivity extends BaseActivity implements MyAddressesPresenter.MyAddressesView, View.OnClickListener,
        OnAddressClickListener, OnRefeshTokenResponse {

    private TextView titleTv;
    private ImageView backIv;
    private RecyclerView addressesRv;
    private Button addAddressBtn;
    private ProgressView loaderPv;

    private RelativeLayout errorRl;
    private ImageView errorIv;
    private TextView errorTv;

    private TextView noConnectionTitleTv, noConnectionMessageTv;
    private LinearLayout noConnectionLl;
    private Button reloadBtn;


    private MyAddressesAdapter adapter;
    private ArrayList<Address> addresses = new ArrayList<>();

    private LocalSettings localSettings;
    private Fonts fonts;

    private MyAddressesPresenter myAddressesPresenter;

    private Address deletedAddress;
    private boolean fromDestination;

    @Override
    public void onPresenterAvailable(BasePresenter presenter) {
        myAddressesPresenter = (MyAddressesPresenter) presenter;
        myAddressesPresenter.setView(this);
        if (errorRl.getVisibility() == View.VISIBLE){
            errorRl.setVisibility(View.GONE);
        }
        if(!addresses.isEmpty()){
            addresses.clear();
            adapter.notifyDataSetChanged();
        }
        myAddressesPresenter.getMyAddresses(localSettings.getToken(),localSettings.getLocale());
    }

    @Override
    public PresenterFactory.PresenterType getPresenterType() {
        return PresenterFactory.PresenterType.MyAddresses;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localSettings = new LocalSettings(this);
        setLanguage();
        setContentView(R.layout.activity_my_addresses);
        getDataFromIntent();
        initialization();
        setFonts();
    }

    private void setLanguage() {
        if (localSettings.getLocale().equals(Constants.ARABIC)) {
            LanguageUtilities.switchToArabicLocale(this);
        }
    }

    private void getDataFromIntent(){
        fromDestination = getIntent().getBooleanExtra(Constants.DESTINATION,false);
    }

    private void initialization(){
        titleTv = findViewById(R.id.tv_my_addresses_title);
        if (fromDestination){
            titleTv.setText(R.string.select_from_addresses);
        }
        else {
            titleTv.setText(R.string.my_addresses_title);
        }
        backIv = findViewById(R.id.iv_back);
        if (localSettings.getLocale().equals(Constants.ARABIC)){
            backIv.setImageResource(R.drawable.back_ar_ic);
        }
        else {
            backIv.setImageResource(R.drawable.back_ic);
        }
        backIv.setOnClickListener(this);

        addressesRv = findViewById(R.id.rv_addressses);
        addressesRv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        adapter = new MyAddressesAdapter(this,addresses,this, fromDestination);
        addressesRv.setAdapter(adapter);

        addAddressBtn = findViewById(R.id.btn_add_address);
        addAddressBtn.setOnClickListener(this);
        if (fromDestination){
            addAddressBtn.setVisibility(View.GONE);
        }
        else {
            addAddressBtn.setVisibility(View.VISIBLE);
        }
        loaderPv = findViewById(R.id.pv_load);

        errorRl = findViewById(R.id.rl_error);
        errorIv = findViewById(R.id.iv_error);
        errorTv = findViewById(R.id.tv_error);

        noConnectionLl = findViewById(R.id.ll_no_connection);
        noConnectionTitleTv = findViewById(R.id.tv_no_connection_title);
        noConnectionMessageTv = findViewById(R.id.tv_no_connection_message);
        reloadBtn = findViewById(R.id.btn_reload_page);
        reloadBtn.setOnClickListener(this);

    }

    private void setFonts(){
        fonts = MApplication.getInstance().getFonts();
        titleTv.setTypeface(fonts.customFontBD());
        addAddressBtn.setTypeface(fonts.customFontBD());
        errorTv.setTypeface(fonts.customFont());

        noConnectionTitleTv.setTypeface(fonts.customFontBD());
        noConnectionMessageTv.setTypeface(fonts.customFont());
        reloadBtn.setTypeface(fonts.customFontBD());
    }

    private void switchToRegisterationOrLogin(){
        Intent registerationOrLoginIntent = new Intent(this,RegisterOrLoginActivity.class);
        registerationOrLoginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(registerationOrLoginIntent);
    }

    private void switchToAddAddress(){
        Intent addAddressIntent = new Intent(this,AddAddressActivity.class);
        startActivity(addAddressIntent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;

            case R.id.btn_add_address:
                switchToAddAddress();
                break;

            case R.id.btn_reload_page:
                myAddressesPresenter.getMyAddresses(localSettings.getToken(),localSettings.getLocale());
                break;
        }
    }

    @Override
    public void onAddressClick(Address address) {
        if (fromDestination){
            Intent result = new Intent();
            result.putExtra(Constants.ADDRESS, address);
            setResult(AppCompatActivity.RESULT_OK, result);
            finish();

        }
    }

    @Override
    public void onDeleteAddressClick(Address deletedAddress) {
        this.deletedAddress = deletedAddress;
        final AlertDialog alert = new AlertDialog.Builder(this)
                .setTitle("")
                .setMessage(R.string.delete_address_message)
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        myAddressesPresenter.deleteAddress(localSettings.getToken(),localSettings.getLocale(),MyAddressesActivity.this.deletedAddress.getId());

                    }
                })
                .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })

                .create();

        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                TextView textView = alert.findViewById(android.R.id.message);
                Button btnYes = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                Button btnNo = alert.getButton(DialogInterface.BUTTON_NEGATIVE);

                Fonts fonts = MApplication.getInstance().getFonts();
                textView.setTypeface(fonts.customFont());
                btnYes.setTypeface(fonts.customFont());
                btnNo.setTypeface(fonts.customFont());

            }
        });
        alert.show();
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
        TokenUtilities.refreshToken(this,
                Injection.provideUserRepository(),
                localSettings.getToken(),
                localSettings.getLocale(),
                localSettings.getRefreshToken(),
                this);
    }

    @Override
    public void showNetworkError() {
        addAddressBtn.setVisibility(View.GONE);
        noConnectionLl.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNoData() {
        if (noConnectionLl.getVisibility() == View.VISIBLE) {
            noConnectionLl.setVisibility(View.GONE);
        }
        if (addAddressBtn.getVisibility() == View.GONE) {
            addAddressBtn.setVisibility(View.VISIBLE);
        }

        errorRl.setVisibility(View.VISIBLE);
        errorIv.setImageResource(R.drawable.grayscale);
        errorTv.setText(R.string.no_data_error);

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
    public void showAddresses(ArrayList<Address> addresses) {
        if (errorRl.getVisibility() == View.VISIBLE){
            errorRl.setVisibility(View.GONE);
        }
        if (noConnectionLl.getVisibility() == View.VISIBLE) {
            noConnectionLl.setVisibility(View.GONE);
        }
        if (addAddressBtn.getVisibility() == View.GONE) {
            addAddressBtn.setVisibility(View.VISIBLE);
        }
        this.addresses.clear();
        this.addresses.addAll(addresses);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showSuccessDelete() {
        this.addresses.remove(deletedAddress);
        adapter.notifyDataSetChanged();
        if (this.addresses.isEmpty()){
            showNoData();
        }
    }

    @Override
    public void hideTokenLoader() {
        hideLoading();
    }
}
