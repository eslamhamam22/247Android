package amaz.objects.TwentyfourSeven.ui.MyAccount;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.Locale;

import amaz.objects.TwentyfourSeven.MApplication;
import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.ui.MyBalance.MyBalanceActivity;
import amaz.objects.TwentyfourSeven.utilities.Constants;
import amaz.objects.TwentyfourSeven.utilities.Fonts;
import amaz.objects.TwentyfourSeven.utilities.LocalSettings;

public class MyAccountFragment extends Fragment implements View.OnClickListener {

    private TextView myAccountTitleTv, userNameTv, walletTitleTv, walletAmountTv, orderNoTitle, orderNoContent, carDetailsTv, myAddressesTv,
            myComplaintsTv, myReviewsTv, logoutTv, becomeTv, delegateTv, customerRateTitleTv, delegateRateTitleTv,
            customerRateContentTv, delegateRateContentTv;
    private LinearLayout becomeDelegateLl, customerRateLl, delegateRateLl, customerRateContentLl, delegateRateContentLl;
    private ImageView editAccountIv, profileIv;
    private RelativeLayout walletRV;
    private LocalSettings localSettings;
    private Fonts fonts;
    private View viewcarDetails;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_account,container,false);
        initializeView(view);
        setFonts();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        super.onResume();

        setUserData();
        setWalletValue();
    }

    private void initializeView(View view){
        localSettings = new LocalSettings(getActivity());
        myAccountTitleTv = view.findViewById(R.id.tv_title_my_account);
        editAccountIv = view.findViewById(R.id.iv_edit_account);
        editAccountIv.setOnClickListener(this);
        profileIv = view.findViewById(R.id.iv_profile);

        customerRateLl = view.findViewById(R.id.ll_customer_rating);
        customerRateContentLl = view.findViewById(R.id.ll_customer_rate_content);
        customerRateContentLl.setOnClickListener(this);
        customerRateTitleTv = view.findViewById(R.id.tv_customer_rate_title);
        customerRateContentTv = view.findViewById(R.id.tv_customer_rate_content);
        if (localSettings.getUser().getRating() != null){
            customerRateContentTv.setText(String.format(Locale.ENGLISH,"%.2f", localSettings.getUser().getRating()));
        }
        else {
            customerRateContentTv.setText(String.format(Locale.ENGLISH,"%.2f", 5.0));
        }

        delegateRateLl = view.findViewById(R.id.ll_delegate_rating);
        delegateRateContentLl = view.findViewById(R.id.ll_delegate_rate_content);
        delegateRateContentLl.setOnClickListener(this);
        delegateRateTitleTv = view.findViewById(R.id.tv_delegate_rate_title);
        delegateRateContentTv = view.findViewById(R.id.tv_delegate_rate_content);

        if (!localSettings.getUser().isDelegate()){
            delegateRateLl.setVisibility(View.GONE);
        }
        else {
            if (localSettings.getUser().getDelegateRating() != null){
                delegateRateContentTv.setText(String.format(Locale.ENGLISH,"%.2f", localSettings.getUser().getDelegateRating()));
            }
            else {
                delegateRateContentTv.setText(String.format(Locale.ENGLISH,"%.2f", 5.0));
            }
            delegateRateLl.setVisibility(View.VISIBLE);
        }

        userNameTv = view.findViewById(R.id.tv_user_name);
        walletTitleTv = view.findViewById(R.id.tv_wallet_title);
        walletAmountTv = view.findViewById(R.id.tv_wallet_amount);
        walletRV = view.findViewById(R.id.wallet_rv);
        orderNoTitle = view.findViewById(R.id.tv_order_no_title);
        orderNoContent = view.findViewById(R.id.tv_order_no_content);
        carDetailsTv = view.findViewById(R.id.tv_car_details);
        viewcarDetails=  view.findViewById(R.id.viewcar_details);
        carDetailsTv.setOnClickListener(this);
        myAddressesTv = view.findViewById(R.id.tv_my_addresses);
        myAddressesTv.setOnClickListener(this);
        myComplaintsTv = view.findViewById(R.id.tv_my_complaints);
        myComplaintsTv.setOnClickListener(this);
        myReviewsTv = view.findViewById(R.id.tv_my_reviews);
        logoutTv = view.findViewById(R.id.tv_logout);
        if (!MyAccountFragment.this.isDetached() && getActivity() != null) {
            if (localSettings.getLocale().equals(Constants.ENGLISH)){
                logoutTv.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(getActivity(),R.drawable.logout_ic),null,null,null);
            }
            else {
                logoutTv.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(getActivity(),R.drawable.logout_ar_ic),null,null,null);
            }
        }
        logoutTv.setOnClickListener(this);
        becomeDelegateLl = view.findViewById(R.id.ll_become_a_delegate);
        becomeDelegateLl.setOnClickListener(this);
        becomeTv = view.findViewById(R.id.tv_become);
        delegateTv = view.findViewById(R.id.tv_delegate);
        if (localSettings.getLocale().equals(Constants.ARABIC)){
            delegateTv.setVisibility(View.GONE);
        }
        else {
            delegateTv.setVisibility(View.VISIBLE);
        }
        if (localSettings.getUser().isDelegate()){
            becomeDelegateLl.setVisibility(View.GONE);
            carDetailsTv.setVisibility(View.VISIBLE);
            viewcarDetails.setVisibility(View.VISIBLE);
            walletRV.setEnabled(true);
        }
        else {
            becomeDelegateLl.setVisibility(View.VISIBLE);
            carDetailsTv.setVisibility(View.GONE);
            viewcarDetails.setVisibility(View.GONE);

            walletRV.setEnabled(false);

        }
        walletRV.setOnClickListener(this);
    }

    private void setWalletValue(){
        if (localSettings.getUser().getWallet_value() >= 0){
            walletAmountTv.setTextColor(getResources().getColor(R.color.kelly_green));
        }else{
            walletAmountTv.setTextColor(getResources().getColor(R.color.app_color));

        }
        walletAmountTv.setText(String.format(Locale.ENGLISH,"%.2f",localSettings.getUser().getWallet_value()) + " "+ getResources().getString(R.string.sar));
    }

    private void setFonts(){
        fonts = MApplication.getInstance().getFonts();
        myAccountTitleTv.setTypeface(fonts.customFontBD());
        userNameTv.setTypeface(fonts.customFont());
        walletTitleTv.setTypeface(fonts.customFont());
        walletAmountTv.setTypeface(fonts.customFontBD());
        orderNoTitle.setTypeface(fonts.customFont());
        orderNoContent.setTypeface(fonts.customFont());
        carDetailsTv.setTypeface(fonts.customFont());
        myAddressesTv.setTypeface(fonts.customFont());
        myComplaintsTv.setTypeface(fonts.customFont());
        myReviewsTv.setTypeface(fonts.customFont());
        logoutTv.setTypeface(fonts.customFont());
        becomeTv.setTypeface(fonts.customFontBD());
        delegateTv.setTypeface(fonts.customFontBD());
        customerRateTitleTv.setTypeface(fonts.customFont());
        customerRateContentTv.setTypeface(fonts.customFont());
        delegateRateTitleTv.setTypeface(fonts.customFont());
        delegateRateContentTv.setTypeface(fonts.customFont());
    }

    private void setUserData(){

        if(localSettings.getUser().getImage() != null){
            Glide.with(getActivity()).load(localSettings.getUser().getImage().getMedium())
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
        userNameTv.setText(localSettings.getUser().getName());
    }

    private void showHasRequestError(){
        Toast.makeText(getActivity(),R.string.has_request_error,Toast.LENGTH_SHORT).show();
    }

    public void refreshData(){
        Log.d("here",localSettings.getUser().isDelegate()+ "");
        if (localSettings.getUser()!=null) {
            if (localSettings.getUser().isDelegate()) {
                becomeDelegateLl.setVisibility(View.GONE);
                carDetailsTv.setVisibility(View.VISIBLE);
                delegateRateLl.setVisibility(View.VISIBLE);
                if (localSettings.getUser().getDelegateRating() != null){
                    delegateRateContentTv.setText(String.format(Locale.ENGLISH,"%.2f", localSettings.getUser().getDelegateRating()));
                }
                else {
                    delegateRateContentTv.setText(String.format(Locale.ENGLISH,"%.2f", 5.0));
                }
            } else {
                becomeDelegateLl.setVisibility(View.VISIBLE);
                carDetailsTv.setVisibility(View.GONE);
                delegateRateLl.setVisibility(View.GONE);
            }
            setWalletValue();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_become_a_delegate:
                if (!MyAccountFragment.this.isDetached() && getActivity() != null){
                    if (localSettings.getUser().isHasDelegateRequest()){
                        showHasRequestError();
                    }
                    else {
                        ((MainActivity)getActivity()).switchToHowToBecomeADelegate();
                    }

                }
                break;

            case R.id.iv_edit_account:
                if (!MyAccountFragment.this.isDetached() && getActivity() != null){
                    ((MainActivity)getActivity()).switchToAccountDetails();
                }
                break;

            case R.id.tv_logout:
                if (!MyAccountFragment.this.isDetached() && getActivity() != null){
                    ((MainActivity)getActivity()).logout();
                }
                break;

            case R.id.tv_my_addresses:
                if (!MyAccountFragment.this.isDetached() && getActivity() != null) {
                    ((MainActivity)getActivity()).switchToMyAddresses();
                }
                break;

            case R.id.tv_my_complaints:
                if (!MyAccountFragment.this.isDetached() && getActivity() != null) {
                    ((MainActivity)getActivity()).switchToMyComplaints();
                }
                break;

            case R.id.tv_car_details:
                if (!MyAccountFragment.this.isDetached() && getActivity() != null) {
                    ((MainActivity)getActivity()).switchToCarDetails();
                }
                break;
            case R.id.wallet_rv:
                if (!MyAccountFragment.this.isDetached() && getActivity() != null) {
                    Intent myBAlanceIntent = new Intent(getActivity(),MyBalanceActivity.class);
                    startActivity(myBAlanceIntent);
                }
                break;

            case R.id.ll_customer_rate_content:
                if (!MyAccountFragment.this.isDetached() && getActivity() != null) {
                    ((MainActivity)getActivity()).switchToMyReviews(true);
                }
                break;

            case R.id.ll_delegate_rate_content:
                if (!MyAccountFragment.this.isDetached() && getActivity() != null) {
                    ((MainActivity)getActivity()).switchToMyReviews(false);
                }
                break;
        }
    }
}
