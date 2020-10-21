package amaz.objects.TwentyfourSeven.ui.OrderDetails.DelegateOrderDetails;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.SphericalUtil;
import com.rey.material.widget.ProgressView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import amaz.objects.TwentyfourSeven.MApplication;
import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.adapters.CancelationReasonsAdapter;
import amaz.objects.TwentyfourSeven.api.APIURLs;
import amaz.objects.TwentyfourSeven.data.models.CancelationReason;
import amaz.objects.TwentyfourSeven.data.models.Offer;
import amaz.objects.TwentyfourSeven.data.models.Order;
import amaz.objects.TwentyfourSeven.data.models.OrderCustomer;
import amaz.objects.TwentyfourSeven.data.models.Route;
import amaz.objects.TwentyfourSeven.listeners.OnLocationChangeListener;
import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import amaz.objects.TwentyfourSeven.presenter.PresenterFactory;
import amaz.objects.TwentyfourSeven.ui.OrderDetails.OrderDetailsActivity;
import amaz.objects.TwentyfourSeven.utilities.Constants;
import amaz.objects.TwentyfourSeven.utilities.DelegateTracker;
import amaz.objects.TwentyfourSeven.utilities.Fonts;
import amaz.objects.TwentyfourSeven.utilities.tracking_module.TrackingReceiver;
import amaz.objects.TwentyfourSeven.utilities.tracking_module.TrackingService;
import amaz.objects.TwentyfourSeven.utilities.tracking_module.TrackingSettings;

import static amaz.objects.TwentyfourSeven.utilities.tracking_module.TrackingService.LOCATION_BROADCAST_INTENT_ACTION;
import static amaz.objects.TwentyfourSeven.utilities.tracking_module.TrackingSettings.LOCATION_SETTINGS_REQUEST_CODE;

public class DelegateOrderDetailsActivity extends OrderDetailsActivity implements View.OnClickListener, OnLocationChangeListener,
        AdapterView.OnItemSelectedListener {

    private RelativeLayout mainContentRl, delegateReadyRl, delegateDistanceRl;
    private TextView orderDetailsTitleTv, newOrderTv, areYouReadyTv, delDistanceToFromTv;
    private ImageView acceptIv, cancelIv, delegateIv, fromIv, delIv;

    private PopupWindow offerPopupWindow;
    private LinearLayout addOfferLl, addOfferContentLl, popupProfitLl;
    private TextView popupAddOrderTv, popupShippingCostTv, popupInvalidShippingTv, popupCurrencyTv, popupCommisionTitleTv, popupCommisionValueTv,
            popupVatTitleTv, popupVatValueTv, popupProfitTitleTv, popupProfitValueTv, popupRejectTv, popupReadyTv, popupFreeCommissionTv;
    private EditText popupShippingCostEt;
    private ProgressView popupLoaderPv;

    private RelativeLayout delegateOfferRl, menuLayout, transparentView;
    private TextView customerNameTv, totalTitleTv, totalValueTv, callCustomerTv, shippingTitleTv, shippingValueTv,
            commissionTitleTv, commissionValueTv, vatTitleTv, vatValueTv, profitTitleTv, profitValueTv, cancelOfferTv, addRateTv;
    private ImageView customerImageIv;
    private RatingBar customerRateRb;

    private LinearLayout itemPriceLl;
    private TextView itemPriceTitleTv, itemPriceValueTv;
    private View itemPriceSeparatorView;

    private LinearLayout changesLl;
    private TextView changesTitleTv, changesValueTv;
    private View changesSeparatorView;

    private LinearLayout discountLl;
    private TextView discountTitleTv, discountValueTv;
    private View discountSeparatorView;

    private TextView cancelTV, submitComplainTV;
    private ImageView iv_more;
    private View menuSeparatorV;

    //private PopupWindow ratePopupWindow;
    private LinearLayout rateLl;
    private RelativeLayout rateDialogRl, rateContentRl;
    private TextView popupRateTitleTv, popupRateCustomerNameTv, popupRateMessage;
    private ImageView popupRateCustomerIv, popupRateCloseIv, popupRateHappyIv, popupRateSmileIv, popupRateMehIv, popupRateSubtractionIv,
            popupRateSadIv;
    private View popupRateSeparator;
    private EditText popupRateCustomerEt;
    private Button popupRateSubmitBtn;
    private ProgressView popupRateLoaderPv;
    private boolean isHappySelected, isSmileSelected, isMehSelected, isSubtractionSelected, isSadSelected;
    private int rateNum;

    private PopupWindow ignorePopupWindow;
    private LinearLayout popupIgnoreLl;
    private RelativeLayout popupIgnoreContentRl;
    private TextView popupIgnoreMessageTv, popupIgnoreYesTv, popupIgnoreNoTv;

    //private DelegateTracker tracker;
    private Fonts fonts;
    private Offer myOffer;

    private double currentLat, currentLng;
    private Order order;

    private double delegateToFromDistance, commission;
    private Polyline polyline;

    private BroadcastReceiver mBroadcastReceiver;
    private boolean firstConnect = false;
    private Dialog cancel_dialoug;

    private Marker carMarker;
    private boolean isMarkerRotating;
    private boolean drawRoute = true;
    private List<LatLng> routePoints;
    private List<LatLng> newRoutePoints = new ArrayList<>();
    private LatLng currentPoint;
    private boolean isAfterOrderData = false;

    private boolean freeCommission = false;

    private int orderCustomerId;

    private TextView error_reason;
    private ArrayList<CancelationReason> cancelationReasons = new ArrayList<>();
    private int selectedReason = 0;

    private RelativeLayout notAvailableOrderRl;
    private TextView notAvailableOrderTv;

    private boolean isActivityVisible, isNotificationReceived;

    private ImageView hideCardsIv;
    private RelativeLayout orderBaseInfoRl;
    private boolean isCardsHided = false;

    private TrackingSettings trackingSettings;
    private BroadcastReceiver trackingReceiver;

    @Override
    public void onPresenterAvailable(BasePresenter presenter) {
        super.onPresenterAvailable(presenter);
        getOrderDetailsPresenter().setView(this);
        if (order == null) {
            getOrderDetailsPresenter().getDelegateOrderDetails(getLocalSettings().getToken(), getLocalSettings().getLocale(), getOrderId());
            getOrderDetailsPresenter().getOrderDelegateLocationOnce(getLocalSettings().getUser().getId());
        }
    }

    @Override
    public PresenterFactory.PresenterType getPresenterType() {
        return PresenterFactory.PresenterType.OrderDetails;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialization();
        initializeOfferPopup();
        initializeCustomerRatePopup();
        initializeIgnorePopup();
        setFonts();
        setNotificationBroadCast();
        setTrackingBroadCast();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver,
                new IntentFilter(Constants.BROADCASTRECEVIERGENERATION));

        LocalBroadcastManager.getInstance(this).registerReceiver(
                trackingReceiver,
                new IntentFilter(LOCATION_BROADCAST_INTENT_ACTION));

        firstConnect = true;
        if (isNotificationReceived) {
            if (isCardsHided) {
                isCardsHided = false;
                hideCardsIv.setImageResource(R.drawable.open_map);
                orderBaseInfoRl.setVisibility(View.VISIBLE);
            }
            if (delegateOfferRl.getVisibility() == View.VISIBLE) {
                delegateOfferRl.setVisibility(View.GONE);
            }
            getOrderDetailsPresenter().getDelegateOrderDetails(getLocalSettings().getToken(), getLocalSettings().getLocale(), getOrderId());
            //firstConnect = false;
            isNotificationReceived = false;
        }
        isActivityVisible = true;

        if(!isTrackingServiceRunning()){
            Log.e("starttracking","start tracking");
            startTracking();
        }
        else {
            Log.e("starttracking","started tracking");
        }
    }

    @Override
    protected void onPause() {
        //LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
        super.onPause();
        isActivityVisible = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
    }

    public void setNotificationBroadCast() {

        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Constants.BROADCASTRECEVIERGENERATION)) {
                    if (firstConnect) {
                        String link_to = intent.getStringExtra("link_to");
                        Log.e("link_to", link_to);
                        /*if (link_to.equals("delegate_order_details")) {
                            if (delegateOfferRl.getVisibility() == View.VISIBLE) {
                                delegateOfferRl.setVisibility(View.GONE);
                            }
                            getOrderDetailsPresenter().getDelegateOrderDetails(getLocalSettings().getToken(), getLocalSettings().getLocale(), getOrderId());
                            //firstConnect = false;`
                        }*/

                        if (link_to.equals("delegate_order_details")) {
                            setStatusChanged(true);
                            String orderNotificationData = intent.getStringExtra("order_data");
                            if (orderNotificationData != null) {
                                try {
                                    JSONObject orderNotificationJson = new JSONObject(orderNotificationData);
                                    if (orderNotificationJson.getInt("id") == getOrderId()) {
                                        if (isActivityVisible) {
                                            if (isCardsHided) {
                                                isCardsHided = false;
                                                hideCardsIv.setImageResource(R.drawable.open_map);
                                                orderBaseInfoRl.setVisibility(View.VISIBLE);
                                            }
                                            if (delegateOfferRl.getVisibility() == View.VISIBLE) {
                                                delegateOfferRl.setVisibility(View.GONE);
                                            }
                                            getOrderDetailsPresenter().getDelegateOrderDetails(getLocalSettings().getToken(), getLocalSettings().getLocale(), getOrderId());
                                            //firstConnect = false;
                                        } else {
                                            isNotificationReceived = true;
                                        }

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }

        };
    }

    public void setTrackingBroadCast(){
        trackingReceiver = new TrackingReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                super.onReceive(context, intent);
                if(intent.getAction() != null &&
                        intent.getAction().equals(TrackingService.LOCATION_BROADCAST_INTENT_ACTION)){
                    Location location = intent.getParcelableExtra(Location.class.getSimpleName());
                    if(location != null){
                        Log.e("location", location.getLatitude()+ " "+location.getLongitude());
                        setAddressData(null, null, location.getLatitude(), location.getLongitude());
                    }
                }
            }
        };
    }

    private void initialization() {
        orderDetailsTitleTv = findViewById(R.id.tv_order_details_title);
        orderDetailsTitleTv.setText(R.string.order);
        mainContentRl = getMainContentRl();
        delegateDistanceRl = findViewById(R.id.rl_delegate_distance);
        delegateDistanceRl.setVisibility(View.VISIBLE);
        delegateIv = findViewById(R.id.iv_delegate);
        delegateIv.setVisibility(View.VISIBLE);
        delDistanceToFromTv = findViewById(R.id.tv_distance_del_from);
        fromIv = findViewById(R.id.iv_from_image);
        delIv = findViewById(R.id.iv_delegate);
        delegateReadyRl = findViewById(R.id.rl_delegate_ready);
        newOrderTv = findViewById(R.id.tv_new_order);
        areYouReadyTv = findViewById(R.id.tv_are_you_ready);
        acceptIv = findViewById(R.id.iv_accept);
        acceptIv.setOnClickListener(this);
        cancelIv = findViewById(R.id.iv_cancel);
        cancelIv.setOnClickListener(this);
        iv_more = findViewById(R.id.iv_more);
        iv_more.setOnClickListener(this);
        menuLayout = findViewById(R.id.menu_layout);
        transparentView = findViewById(R.id.transparent_view);
        transparentView.setOnClickListener(this);

        cancelTV = findViewById(R.id.cancelTxt);
        submitComplainTV = findViewById(R.id.submit_complain);
        menuSeparatorV = findViewById(R.id.v_menu_separator);

        submitComplainTV.setOnClickListener(this);
        cancelTV.setOnClickListener(this);

        delegateOfferRl = findViewById(R.id.rl_delegate_offer);
        customerNameTv = findViewById(R.id.tv_customer_name);
        customerNameTv.setOnClickListener(this);
        callCustomerTv = findViewById(R.id.tv_call_customer);
        callCustomerTv.setOnClickListener(this);
        totalTitleTv = findViewById(R.id.tv_total_title);
        totalValueTv = findViewById(R.id.tv_total_value);
        customerRateRb = findViewById(R.id.rb_customer_rate);
        shippingTitleTv = findViewById(R.id.tv_shipping_title);
        shippingValueTv = findViewById(R.id.tv_shipping_value);
        commissionTitleTv = findViewById(R.id.tv_commission_title);
        commissionValueTv = findViewById(R.id.tv_commission_value);
        vatTitleTv = findViewById(R.id.tv_vat_title);
        vatValueTv = findViewById(R.id.tv_vat_value);
        profitTitleTv = findViewById(R.id.tv_profit_title);
        profitValueTv = findViewById(R.id.tv_profit_value);
        cancelOfferTv = findViewById(R.id.tv_cancel_offer);
        cancelOfferTv.setOnClickListener(this);
        customerImageIv = findViewById(R.id.iv_customer_image);
        customerImageIv.setOnClickListener(this);
        addRateTv = findViewById(R.id.tv_delegate_add_rate);
        addRateTv.setOnClickListener(this);

        itemPriceLl = findViewById(R.id.ll_item_price);
        itemPriceTitleTv = findViewById(R.id.tv_item_price_title);
        itemPriceValueTv = findViewById(R.id.tv_item_price_value);
        itemPriceSeparatorView = findViewById(R.id.v_item_price_separator);

        changesLl = findViewById(R.id.ll_changes);
        changesTitleTv = findViewById(R.id.tv_changes_title);
        changesValueTv = findViewById(R.id.tv_changes_value);
        changesSeparatorView = findViewById(R.id.v_changes_separator);

        discountLl = findViewById(R.id.ll_discount);
        discountTitleTv = findViewById(R.id.tv_discount_title);
        discountValueTv = findViewById(R.id.tv_discount_value);
        discountSeparatorView = findViewById(R.id.v_discount_separator);

        notAvailableOrderRl = findViewById(R.id.rl_order_not_available);
        notAvailableOrderTv = findViewById(R.id.tv_order_not_available);

//        tracker = new DelegateTracker(this, this);
//        tracker.getLocation();
        trackingSettings = new TrackingSettings(this);
//        if(!isTrackingServiceRunning()){
//            startTracking();
//        }

        hideCardsIv = findViewById(R.id.iv_hide_cards);
        hideCardsIv.setOnClickListener(this);
        orderBaseInfoRl = findViewById(R.id.rl_order_base_info);
    }

    private void initializeOfferPopup() {
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_add_offer, null, false);
        offerPopupWindow = new PopupWindow(v, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        addOfferLl = v.findViewById(R.id.ll_add_offer);
        addOfferLl.setOnClickListener(this);
        addOfferContentLl = v.findViewById(R.id.ll_popup_content);
        addOfferContentLl.setOnClickListener(this);
        popupAddOrderTv = v.findViewById(R.id.tv_add_offer);
        popupShippingCostTv = v.findViewById(R.id.tv_shipping_cost);
        popupInvalidShippingTv = v.findViewById(R.id.tv_invalid_shipping_tv);
        popupCurrencyTv = v.findViewById(R.id.tv_currency);
        popupCommisionTitleTv = v.findViewById(R.id.tv_commission_title);
        popupCommisionValueTv = v.findViewById(R.id.tv_commission_value);
        popupFreeCommissionTv = v.findViewById(R.id.tv_free_commission);
        popupVatTitleTv = v.findViewById(R.id.tv_vat_title);
        popupVatValueTv = v.findViewById(R.id.tv_vat_value);
        popupProfitLl = v.findViewById(R.id.ll_profit);
        popupProfitTitleTv = v.findViewById(R.id.tv_profit_title);
        popupProfitValueTv = v.findViewById(R.id.tv_profit_value);
        popupShippingCostEt = v.findViewById(R.id.et_shipping_cost);
        popupShippingCostEt.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!freeCommission) {
                    if (order.getFinanceSettings() != null) {
                        double shippingCost = 0;
                        if (!popupShippingCostEt.getText().toString().isEmpty()) {
                            if (popupShippingCostEt.getText().toString().equals(".")) {
                                shippingCost = 0.0;
                            } else {
                                shippingCost = Double.parseDouble(popupShippingCostEt.getText().toString());
                            }

                        }
                        if (order.getFinanceSettings().getCommissionType().equals("0")) {
                            if (!order.getFinanceSettings().getCommissionValue().isEmpty()) {
                                double commissionPercentage = Double.parseDouble(order.getFinanceSettings().getCommissionValue());
                                commission = (commissionPercentage / 100) * shippingCost;
                                popupCommisionValueTv.setText(String.format(Locale.ENGLISH,"%.2f", commission) + " " + getString(R.string.sar));
                            } else {
                                if (!order.getFinanceSettings().getCommissionValue().isEmpty()) {
                                    commission = Double.parseDouble(order.getFinanceSettings().getCommissionValue());
                                }
                            }
                        } else {
                            if (!order.getFinanceSettings().getCommissionValue().isEmpty()) {
                                commission = Double.parseDouble(order.getFinanceSettings().getCommissionValue());
                            }
                        }

                        /*if (order.getFinanceSettings().getVatType().equals("0") && !order.getFinanceSettings().getVatValue().isEmpty()) {
                            double vatPercentage = Double.parseDouble(order.getFinanceSettings().getVatValue());
                            double vatValue = (vatPercentage / 100) * commission;
                            popupVatValueTv.setText(String.format("%.2f", vatValue) + " " + getString(R.string.sar));
                        }*/
                        if (!popupShippingCostEt.getText().toString().isEmpty()) {
                            checkFinanceSettingsData(false);
                        }
                    }
                } else {
                    popupFreeCommissionTv.setVisibility(View.VISIBLE);
                    commission = 0;
                    popupCommisionValueTv.setText(String.format(Locale.ENGLISH,"%.2f", commission) + " " + getString(R.string.sar));
                    if (order.getFinanceSettings() != null) {
                        /*if (order.getFinanceSettings().getVatType().equals("1")) { // fixed
                            popupVatValueTv.setText(order.getFinanceSettings().getVatValue() + " " + getString(R.string.sar));
                        } else {
                            popupVatValueTv.setText(String.format("%.2f", 0.0) + " " + getString(R.string.sar));
                        }*/
                        if (!popupShippingCostEt.getText().toString().isEmpty()) {
                            checkFinanceSettingsData(false);
                        }
                    }

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (popupShippingCostEt.getText().toString().isEmpty()) {
                    if (order.getFinanceSettings() != null) {
                        if (order.getFinanceSettings().getCommissionType().equals("0")) { // percentage
                            popupCommisionValueTv.setText(getString(R.string.dashes));
                        }

                        /*if (order.getFinanceSettings().getVatType().equals("0")) { // percentage
                            popupVatValueTv.setText(getString(R.string.dashes));
                        }*/
                    }

                    popupProfitLl.setVisibility(View.GONE);
                }

            }
        });
        popupRejectTv = v.findViewById(R.id.tv_reject);
        popupRejectTv.setOnClickListener(this);
        popupReadyTv = v.findViewById(R.id.tv_ready);
        popupReadyTv.setOnClickListener(this);
        popupLoaderPv = v.findViewById(R.id.pv_load);
    }

    private void initializeCustomerRatePopup() {
        /*View v = LayoutInflater.from(this).inflate(R.layout.dialog_rate,null,false);
        ratePopupWindow = new PopupWindow(v, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);*/

        rateDialogRl = findViewById(R.id.rl_rate_dialog);
        rateLl = findViewById(R.id.ll_rate);
        rateLl.setOnClickListener(this);
        rateContentRl = findViewById(R.id.rl_rate_content);
        rateContentRl.setOnClickListener(this);
        popupRateTitleTv = findViewById(R.id.tv_rate_review_cust_del);
        popupRateTitleTv.setText(R.string.review_customer);
        popupRateCloseIv = findViewById(R.id.iv_rate_close);
        popupRateCloseIv.setOnClickListener(this);
        popupRateCustomerIv = findViewById(R.id.iv_rate_cust_del_image);
        popupRateCustomerNameTv = findViewById(R.id.tv_rate_cust_del_name);
        popupRateCustomerEt = findViewById(R.id.et_rate_cust_del);
        popupRateMessage = findViewById(R.id.tv_rate_cust_del);
        popupRateMessage.setText(R.string.rate_customer);
        popupRateSubmitBtn = findViewById(R.id.btn_submit_rating);
        popupRateSubmitBtn.setOnClickListener(this);

        popupRateHappyIv = findViewById(R.id.iv_happy);
        popupRateHappyIv.setOnClickListener(this);
        popupRateSmileIv = findViewById(R.id.iv_smile);
        popupRateSmileIv.setOnClickListener(this);
        popupRateMehIv = findViewById(R.id.iv_meh);
        popupRateMehIv.setOnClickListener(this);
        popupRateSubtractionIv = findViewById(R.id.iv_subtraction);
        popupRateSubtractionIv.setOnClickListener(this);
        popupRateSadIv = findViewById(R.id.iv_sad);
        popupRateSadIv.setOnClickListener(this);

        popupRateSeparator = findViewById(R.id.v_rate_separator);
        if (getLocalSettings().getLocale().equals(Constants.ARABIC)) {
            popupRateSeparator.setRotationY(180);
        }

        popupRateLoaderPv = findViewById(R.id.pv_rate_load);
    }

    private void initializeIgnorePopup() {
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_reorder, null, false);
        ignorePopupWindow = new PopupWindow(v, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        popupIgnoreLl = v.findViewById(R.id.ll_reorder);
        popupIgnoreLl.setOnClickListener(this);
        popupIgnoreContentRl = v.findViewById(R.id.rl_reorder_content);
        popupIgnoreContentRl.setOnClickListener(this);
        popupIgnoreMessageTv = v.findViewById(R.id.tv_reorder_message);
        popupIgnoreMessageTv.setTextColor(R.color.black);
        popupIgnoreMessageTv.setText(R.string.ignore_message);
        popupIgnoreYesTv = v.findViewById(R.id.tv_reorder_yes);
        popupIgnoreYesTv.setOnClickListener(this);
        popupIgnoreNoTv = v.findViewById(R.id.tv_reorder_no);
        popupIgnoreNoTv.setOnClickListener(this);
    }

    private void setFonts() {
        fonts = MApplication.getInstance().getFonts();
        orderDetailsTitleTv.setTypeface(fonts.customFontBD());
        newOrderTv.setTypeface(fonts.customFont());
        delDistanceToFromTv.setTypeface(fonts.customFont());
        areYouReadyTv.setTypeface(fonts.customFont());

        popupAddOrderTv.setTypeface(fonts.customFontBD());
        popupShippingCostTv.setTypeface(fonts.customFont());
        popupCurrencyTv.setTypeface(fonts.customFontBD());
        popupCommisionTitleTv.setTypeface(fonts.customFont());
        popupCommisionValueTv.setTypeface(fonts.customFontBD());
        popupFreeCommissionTv.setTypeface(fonts.customFont());
        popupVatTitleTv.setTypeface(fonts.customFont());
        popupVatValueTv.setTypeface(fonts.customFontBD());
        popupProfitTitleTv.setTypeface(fonts.customFont());
        popupProfitValueTv.setTypeface(fonts.customFontBD());
        popupShippingCostEt.setTypeface(fonts.customFontBD());
        popupInvalidShippingTv.setTypeface(fonts.customFont());
        popupRejectTv.setTypeface(fonts.customFontBD());
        popupReadyTv.setTypeface(fonts.customFontBD());

        customerNameTv.setTypeface(fonts.customFont());
        totalTitleTv.setTypeface(fonts.customFont());
        totalValueTv.setTypeface(fonts.customFontBD());
        callCustomerTv.setTypeface(fonts.customFontBD());
        shippingTitleTv.setTypeface(fonts.customFont());
        shippingValueTv.setTypeface(fonts.customFontBD());
        commissionTitleTv.setTypeface(fonts.customFont());
        commissionValueTv.setTypeface(fonts.customFontBD());
        vatTitleTv.setTypeface(fonts.customFont());
        vatValueTv.setTypeface(fonts.customFontBD());
        profitTitleTv.setTypeface(fonts.customFont());
        profitValueTv.setTypeface(fonts.customFontBD());
        cancelOfferTv.setTypeface(fonts.customFontBD());
        submitComplainTV.setTypeface(fonts.customFont());
        cancelTV.setTypeface(fonts.customFont());
        addRateTv.setTypeface(fonts.customFont());

        itemPriceTitleTv.setTypeface(fonts.customFont());
        itemPriceValueTv.setTypeface(fonts.customFontBD());

        changesTitleTv.setTypeface(fonts.customFont());
        changesValueTv.setTypeface(fonts.customFontBD());

        discountTitleTv.setTypeface(fonts.customFont());
        discountValueTv.setTypeface(fonts.customFontBD());

        notAvailableOrderTv.setTypeface(fonts.customFontBD());

        popupRateTitleTv.setTypeface(fonts.customFontBD());
        popupRateCustomerNameTv.setTypeface(fonts.customFont());
        popupRateCustomerEt.setTypeface(fonts.customFont());
        popupRateMessage.setTypeface(fonts.customFont());
        popupRateSubmitBtn.setTypeface(fonts.customFontBD());

        popupIgnoreMessageTv.setTypeface(fonts.customFont());
        popupIgnoreYesTv.setTypeface(fonts.customFont());
        popupIgnoreNoTv.setTypeface(fonts.customFont());
    }

    private void openOfferPopup() {
        if (getLocalSettings().getUser() != null) {
            if (getLocalSettings().getUser().getWallet_value() >= 0) {
                offerPopupWindow.setFocusable(true);
                offerPopupWindow.update();
                offerPopupWindow.showAtLocation(mainContentRl, Gravity.CENTER, 0, 0);
            } else {
                double value_pos = -1 * getLocalSettings().getUser().getWallet_value();
                if (value_pos >= getLocalSettings().getMaxMinusAmount()) {
                    Toast.makeText(this, getResources().getString(R.string.max_debit_offer), Toast.LENGTH_LONG).show();

                } else {
                    offerPopupWindow.setFocusable(true);
                    offerPopupWindow.update();
                    offerPopupWindow.showAtLocation(mainContentRl, Gravity.CENTER, 0, 0);
                }
            }
        }
    }

    private void setOfferPopup() {
        popupShippingCostEt.setText("");
        popupInvalidShippingTv.setVisibility(View.GONE);
        //double vatFixedValue;
        if (!freeCommission) {
            if (order.getFinanceSettings() != null) {
                if (order.getFinanceSettings().getCommissionType().equals("1")) { // fixed
                    popupCommisionValueTv.setText(order.getFinanceSettings().getCommissionValue() + " " + getString(R.string.sar));
                    if (!order.getFinanceSettings().getCommissionValue().isEmpty()) {
                        commission = Double.parseDouble(order.getFinanceSettings().getCommissionValue());
                    }
                } else {
                    popupCommisionValueTv.setText(getString(R.string.dashes));
                }

                /*if (order.getFinanceSettings().getVatType().equals("1")) { // fixed
                    popupVatValueTv.setText(order.getFinanceSettings().getVatValue() + " " + getString(R.string.sar));
                } else {
                    if (commission != 0) {
                        if (!order.getFinanceSettings().getVatValue().isEmpty()) {
                            vatFixedValue = (Double.parseDouble(order.getFinanceSettings().getVatValue()) / 100) * commission;
                            popupVatValueTv.setText(String.format("%.2f", vatFixedValue) + " " + getString(R.string.sar));
                        } else {
                            popupVatValueTv.setText(getString(R.string.dashes));
                        }

                    } else {
                        popupVatValueTv.setText(getString(R.string.dashes));
                    }

                }*/
            }
        } else {
            popupFreeCommissionTv.setVisibility(View.VISIBLE);
            commission = 0;
            popupCommisionValueTv.setText(String.format(Locale.ENGLISH,"%.2f", commission) + " " + getString(R.string.sar));
            /*if (order.getFinanceSettings() != null) {
                if (order.getFinanceSettings().getVatType().equals("1")) { // fixed
                    popupVatValueTv.setText(order.getFinanceSettings().getVatValue() + " " + getString(R.string.sar));
                } else {
                    vatFixedValue = 0;
                    popupVatValueTv.setText(String.format("%.2f", vatFixedValue) + " " + getString(R.string.sar));
                }
            }*/
        }
    }

    private void openRatePopup() {
        /*ratePopupWindow.setFocusable(true);
        ratePopupWindow.update();
        ratePopupWindow.showAtLocation(mainContentRl, Gravity.CENTER, 0, 0);*/
        rateDialogRl.setVisibility(View.VISIBLE);
    }

    private void openIgnorePopup() {
        ignorePopupWindow.showAtLocation(getMainContentRl(), Gravity.CENTER, 0, 0);
    }

    private void setRatePopupData() {
        popupRateCustomerEt.setText("");
        popupRateCustomerNameTv.setText(order.getCreatedBy().getName());
        if (order.getCreatedBy().getImage() != null) {
            Picasso.with(this).load(order.getCreatedBy().getImage().getMedium()).placeholder(R.drawable.avatar)
                    .error(R.drawable.avatar).into(popupRateCustomerIv);
        }
        isHappySelected = false;
        isSmileSelected = false;
        isMehSelected = false;
        isSubtractionSelected = false;
        isSadSelected = false;
        popupRateHappyIv.setImageResource(R.drawable.smile_1__2_1);
        popupRateSmileIv.setImageResource(R.drawable.smile_1__1);
        popupRateMehIv.setImageResource(R.drawable.meh_1);
        popupRateSubtractionIv.setImageResource(R.drawable.subtraction_22);
        popupRateSadIv.setImageResource(R.drawable.sad_1);
        rateNum = 0;
        popupRateSubmitBtn.setAlpha((float) 0.5);
        popupRateSubmitBtn.setEnabled(false);
    }

    private void setMapOrderData(final double fromLat, final double fromLng, final double toLat, final double toLng) {
        LatLng fromLocation = new LatLng(fromLat, fromLng);
        LatLng toLocation = new LatLng(toLat, toLng);
        //LatLng delegateLocation = null;
        /*if (currentLat != 0 && currentLng != 0){
            delegateLocation = new LatLng(currentLat, currentLng);
        }*/
        final LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(fromLocation);
        builder.include(toLocation);
        /*if (delegateLocation != null){
            builder.include(delegateLocation);
        }*/

        final LatLngBounds bounds = builder.build();
        if (getLocalSettings().getLocale().equals(Constants.ARABIC)) {
            getGoogleMap().addMarker(new MarkerOptions().position(toLocation).icon(BitmapDescriptorFactory.fromResource(R.drawable.btn_blue_ar)));
            getGoogleMap().addMarker(new MarkerOptions().position(fromLocation).icon(BitmapDescriptorFactory.fromResource(R.drawable.btn_red_ar)));
        } else {
            getGoogleMap().addMarker(new MarkerOptions().position(toLocation).icon(BitmapDescriptorFactory.fromResource(R.drawable.btn_blue_en)));
            getGoogleMap().addMarker(new MarkerOptions().position(fromLocation).icon(BitmapDescriptorFactory.fromResource(R.drawable.btn_red_en)));
        }
        /*if (delegateLocation != null){
            carMarker = getGoogleMap().addMarker(new MarkerOptions().position(delegateLocation).icon(BitmapDescriptorFactory.fromResource(R.drawable.car_purple)));

        }*/

        getGoogleMap().moveCamera(CameraUpdateFactory.newLatLngZoom(fromLocation, 17));

        getGoogleMap().setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                try{
                    getGoogleMap().animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 270));
                } catch (IllegalStateException ex){
                    getGoogleMap().animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 200));
                }
                //getGoogleMap().animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 270));

            }
        });

        getGoogleMap().setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (!marker.getId().equals(carMarker.getId())) {
                    String uriBegin = "geo:" + marker.getPosition().latitude + "," + marker.getPosition().longitude;
                    String query = marker.getPosition().latitude + "," + marker.getPosition().longitude;
                    String encodedQuery = Uri.encode(query);

                    String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                    Uri uri = Uri.parse(uriString);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
                return true;
            }
        });

    }

    private void setRoute(final double fromLat, final double fromLng, final double toLat, final double toLng, Route route) {
        List<LatLng> polyz = PolyUtil.decode(route.getRoutePolyLine().getPoints());
        this.routePoints = polyz;
        if (polyz != null) {
            PolylineOptions lineOptions = new PolylineOptions();
            lineOptions.addAll(polyz);
            lineOptions.width(5);
            lineOptions.color(ContextCompat.getColor(this, R.color.app_color));

            LatLng fromLocation = new LatLng(fromLat, fromLng);
            LatLng toLocation = new LatLng(toLat, toLng);
            final LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(fromLocation);
            builder.include(toLocation);
            for (int i = 0; i < polyz.size(); i++) {
                builder.include(polyz.get(i));
            }
            final LatLngBounds bounds = builder.build();

            if (getGoogleMap() != null) {
                getGoogleMap().setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {
                        try{
                            getGoogleMap().animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 270));
                        } catch (IllegalStateException ex){
                            getGoogleMap().animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 200));
                        }
                        //getGoogleMap().animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 300));
                    }
                });
                if (polyline != null) {
                    polyline.remove();
                }
                if (carMarker == null) {
                    carMarker = getGoogleMap().addMarker(new MarkerOptions().position(new LatLng(currentLat, currentLng)).icon(BitmapDescriptorFactory.fromResource(R.drawable.car_purple)));
                } else {
                    carMarker.remove();
                    carMarker = getGoogleMap().addMarker(new MarkerOptions().position(new LatLng(currentLat, currentLng)).icon(BitmapDescriptorFactory.fromResource(R.drawable.car_purple)));

                }
                polyline = getGoogleMap().addPolyline(lineOptions);

            }
            /*if ((order.getStatus().equals("assigned") || order.getStatus().equals("in_progress") || order.getStatus().equals("delivery_in_progress")) && isFromChat()){
                moveOnRoute();
            }*/


        }
    }

    private void updateDelegateLocation() {
        Log.e("onlocation2", "onlocation2");
        LatLng delegateLatLng = new LatLng(currentLat, currentLng);
        animateMarker(carMarker, delegateLatLng, false);
        rotateMarker(carMarker, (float) bearingBetweenLocations(carMarker.getPosition(), delegateLatLng));
    }

    public void animateMarker(final Marker marker, final LatLng toPosition,
                              final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = getGoogleMap().getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 500;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.e("onlocation3", "onlocation3");
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed / duration);
                double lng = t * toPosition.longitude + (1 - t) * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t) * startLatLng.latitude;

                marker.setPosition(new LatLng(lat, lng));

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }

    private void rotateMarker(final Marker marker, final float toRotation) {
        if (!isMarkerRotating) {
            final Handler handler = new Handler();
            final long start = SystemClock.uptimeMillis();
            final float startRotation = marker.getRotation();
            final long duration = 1000;

            final Interpolator interpolator = new LinearInterpolator();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    isMarkerRotating = true;
                    Log.e("onlocation4", "onlocation4");
                    long elapsed = SystemClock.uptimeMillis() - start;
                    float t = interpolator.getInterpolation((float) elapsed / duration);

                    float rot = t * toRotation + (1 - t) * startRotation;

                    marker.setRotation(-rot > 180 ? rot / 2 : rot);
                    if (t < 1.0) {
                        // Post again 16ms later.
                        handler.postDelayed(this, 16);
                    } else {
                        isMarkerRotating = false;
                    }
                }
            });
        }
    }

    private double bearingBetweenLocations(LatLng latLng1, LatLng latLng2) {

        /*double PI = 3.14159;
        double lat1 = latLng1.latitude * PI / 180;
        double long1 = latLng1.longitude * PI / 180;
        double lat2 = latLng2.latitude * PI / 180;
        double long2 = latLng2.longitude * PI / 180;

        double dLon = (long2 - long1);

        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon);

        double brng = Math.atan2(y, x);

        brng = Math.toDegrees(brng);
        brng = (brng + 360) % 360;
        SphericalUtil.computeHeading(latLng1, latLng2);*/
        return SphericalUtil.computeHeading(latLng1, latLng2);
    }

    private void updateRoute() {
        double firstToSecondDistance;
        double delegateToFirstDistance;
        double delegateToSecondDistance;
        int removedIndex = -1;
        for (int i = 1; i < routePoints.size(); i++) {
            firstToSecondDistance = SphericalUtil.computeDistanceBetween(routePoints.get(i - 1), routePoints.get(i));
            delegateToFirstDistance = SphericalUtil.computeDistanceBetween(routePoints.get(i - 1), currentPoint);
            delegateToSecondDistance = SphericalUtil.computeDistanceBetween(currentPoint, routePoints.get(i));
            if (delegateToFirstDistance < firstToSecondDistance && delegateToSecondDistance < firstToSecondDistance) {
                removedIndex = routePoints.indexOf(routePoints.get(i - 1));
                break;
            }
        }

        if (removedIndex != -1) {
            boolean isPointAdded = false;
            newRoutePoints.clear();
            newRoutePoints.add(0, currentPoint);
            for (int i = removedIndex + 1; i < routePoints.size(); i++) {
                newRoutePoints.add(routePoints.get(i));
                isPointAdded = true;
            }
            if (isPointAdded) {
                routePoints.clear();
                routePoints.addAll(newRoutePoints);
            }

            Log.e("sizes", routePoints.size() + "   " + newRoutePoints.size());
            if (polyline != null) {
                polyline.getPoints().clear();
                polyline.remove();

            }
            PolylineOptions lineOptions = new PolylineOptions();
            lineOptions.addAll(routePoints);
            lineOptions.width(5);
            lineOptions.color(ContextCompat.getColor(this, R.color.app_color));
            polyline = getGoogleMap().addPolyline(lineOptions);
        } else {
            if (polyline != null) {
                polyline.getPoints().clear();
                polyline.remove();

            }
            PolylineOptions lineOptions = new PolylineOptions();
            lineOptions.addAll(routePoints);
            lineOptions.width(5);
            lineOptions.color(ContextCompat.getColor(this, R.color.app_color));
            polyline = getGoogleMap().addPolyline(lineOptions);
        }

    }

    private void removeDelegateDistance() {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_START);
        fromIv.setLayoutParams(layoutParams);
        delIv.setVisibility(View.GONE);
        delegateDistanceRl.setVisibility(View.GONE);
    }

    private void calculateDelegateAbsoluteDistance() {
        if (currentLat != 0 && currentLng != 0) {
            /*Location fromDelegateLocation = new Location("fromDelegateLocation");
            fromDelegateLocation.setLatitude(currentLat);
            fromDelegateLocation.setLongitude(currentLng);

            Location fromLocation = new Location("fromLocation");
            fromLocation.setLatitude(order.getFromLat());
            fromLocation.setLongitude(order.getFromLng());

            delegateToFromDistance = fromDelegateLocation.distanceTo(fromLocation);*/
            delegateToFromDistance = SphericalUtil.computeDistanceBetween(new LatLng(currentLat, currentLng),
                    new LatLng(order.getFromLat(), order.getFromLng()));
            if (delegateToFromDistance < 1000) {
                delDistanceToFromTv.setText(String.format(Locale.ENGLISH,"%.1f", delegateToFromDistance) + " " + getString(R.string.meter));
            } else {
                delDistanceToFromTv.setText(String.format(Locale.ENGLISH,"%.1f", (delegateToFromDistance / 1000)) + " " + getString(R.string.kilometer));
            }
        } else {
            delegateDistanceRl.setVisibility(View.GONE);
            delegateIv.setVisibility(View.GONE);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_START);
            fromIv.setLayoutParams(layoutParams);
        }
    }

    private void checkFinanceSettingsData(boolean fromAdd) {
        popupInvalidShippingTv.setVisibility(View.GONE);
        double shippingCost = 0;
        double minMileageCost = 0;
        double maxMileageCost = 0;
        double minFixedCost = 0;
        if (!popupShippingCostEt.getText().toString().isEmpty()) {
            if (popupShippingCostEt.getText().toString().equals(".")) {
                shippingCost = 0.0;
            } else {
                shippingCost = Double.parseDouble(popupShippingCostEt.getText().toString());
            }

        }
        if (order.getFinanceSettings() != null) {
            if (!order.getFinanceSettings().getMinMileageCost().isEmpty()) {
                minMileageCost = Double.parseDouble(order.getFinanceSettings().getMinMileageCost());
            }
            if (!order.getFinanceSettings().getMaxMileageCost().isEmpty()) {
                maxMileageCost = Double.parseDouble(order.getFinanceSettings().getMaxMileageCost());
            }
            if (!order.getFinanceSettings().getMinFixedCost().isEmpty()) {
                minFixedCost = Double.parseDouble(order.getFinanceSettings().getMinFixedCost());
            }
            if (fromAdd) {
                if (validateShippingCost(shippingCost, delegateToFromDistance, getFromToToDistance(), minMileageCost, maxMileageCost, minFixedCost)) {
                    addOffer(shippingCost);
                }
            } else {
                validateShippingCost(shippingCost, delegateToFromDistance, getFromToToDistance(), minMileageCost, maxMileageCost, minFixedCost);
            }

        }

    }

    private boolean validateShippingCost(double shippingCost, double distToPickUp, double distToDelivery, double minMileageCost,
                                         double maxMileageCost, double fixedPrice) {

        boolean isValid = true;
        double minShippingCost = minMileageCost * ((distToPickUp + distToDelivery) / 1000);
        double maxShippingCost = maxMileageCost * ((distToPickUp + distToDelivery) / 1000);
        if (minShippingCost < fixedPrice) {
            minShippingCost = fixedPrice;
        }
        if (maxShippingCost < fixedPrice) {
            maxShippingCost = fixedPrice;
        }
        if (shippingCost < minShippingCost) {
            isValid = false;
            showMinShippingCostError(minShippingCost);
            popupProfitLl.setVisibility(View.GONE);
        } else if (shippingCost > maxShippingCost) {
            isValid = false;
            showMaxShippingCostError(maxShippingCost);
            popupProfitLl.setVisibility(View.GONE);
        } else {
            popupProfitLl.setVisibility(View.VISIBLE);
            popupProfitValueTv.setText(String.format(Locale.ENGLISH,"%.2f", shippingCost - commission) + " " + getString(R.string.sar));
        }
        return isValid;
    }

    private void addOffer(double shippingCost) {
        getOrderDetailsPresenter().addOffer(getLocalSettings().getToken(), getLocalSettings().getLocale(), getOrderId(), shippingCost,
                delegateToFromDistance / 1000, getFromToToDistance() / 1000, currentLat, currentLng);
    }

    private void showMinShippingCostError(double minShippingCost) {
        popupInvalidShippingTv.setVisibility(View.VISIBLE);
        popupInvalidShippingTv.setText(getString(R.string.min_shipping_cost) + " " + String.format(Locale.ENGLISH,"%.2f", minShippingCost));
        if (order.getFinanceSettings() != null) {
            if (order.getFinanceSettings().getCommissionType().equals("0")) { // percentage
                popupCommisionValueTv.setText(getString(R.string.dashes));
            }

            /*if (order.getFinanceSettings().getVatType().equals("0")) { // percentage
                popupVatValueTv.setText(getString(R.string.dashes));
            }*/
        }
    }

    private void showMaxShippingCostError(double maxShippingCost) {
        popupInvalidShippingTv.setVisibility(View.VISIBLE);
        popupInvalidShippingTv.setText(getString(R.string.max_shipping_cost) + " " + String.format(Locale.ENGLISH,"%.2f", maxShippingCost));
        if (order.getFinanceSettings() != null) {
            if (order.getFinanceSettings().getCommissionType().equals("0")) { // percentage
                popupCommisionValueTv.setText(getString(R.string.dashes));
            }

            /*if (order.getFinanceSettings().getVatType().equals("0")) { // percentage
                popupVatValueTv.setText(getString(R.string.dashes));
            }*/
        }
    }

    private void showOfferData(Offer offer) {
        this.myOffer = offer;
        shippingValueTv.setText(String.format(Locale.ENGLISH,"%.2f", offer.getShippingCost()) + " " + getString(R.string.sar));
        if (offer.getCommission() == 0) {
            commissionValueTv.setText(R.string.free);
        } else {
            commissionValueTv.setText(String.format(Locale.ENGLISH,"%.2f", offer.getCommission()) + " " + getString(R.string.sar));
        }
        //vatValueTv.setText(String.format("%.2f", offer.getVat()) + " " + getString(R.string.sar));
        //totalValueTv.setText(String.format("%.2f", (offer.getShippingCost() + offer.getVat())) + " " + getString(R.string.sar));
        totalValueTv.setText(String.format(Locale.ENGLISH,"%.2f", (offer.getShippingCost())) + " " + getString(R.string.sar));
        profitValueTv.setText(String.format(Locale.ENGLISH,"%.2f", (offer.getShippingCost() - offer.getCommission())) + " " + getString(R.string.sar));
    }

    private void setCustomerData(OrderCustomer orderCustomer) {
        this.orderCustomerId = orderCustomer.getId();
        customerNameTv.setText(orderCustomer.getName());
        if (orderCustomer.getImage() != null) {
            Picasso.with(this).load(orderCustomer.getImage().getMedium()).placeholder(R.drawable.avatar)
                    .error(R.drawable.avatar).into(customerImageIv);
        }
        if (orderCustomer.getRating() == 0) {
            customerRateRb.setRating(5);
        } else {
            customerRateRb.setRating(orderCustomer.getRating());
        }

    }

    private void setInvoiceData() {
        delegateOfferRl.setVisibility(View.VISIBLE);
        cancelOfferTv.setVisibility(View.GONE);
        customerNameTv.setText(order.getCreatedBy().getName());
        if (order.getCreatedBy().getImage() != null) {
            Picasso.with(this).load((order.getCreatedBy().getImage().getMedium())).placeholder(R.drawable.avatar)
                    .error(R.drawable.avatar).into(customerImageIv);
        }
        shippingValueTv.setText(String.format(Locale.ENGLISH,"%.2f", order.getShippingCost()) + " " + getString(R.string.sar));
        if (order.getCommission() == 0) {
            commissionValueTv.setText(getString(R.string.free));
        } else {
            commissionValueTv.setText(String.format(Locale.ENGLISH,"%.2f", order.getCommission()) + " " + getString(R.string.sar));
        }
        //vatValueTv.setText(String.format("%.2f", order.getVat()) + " " + getString(R.string.sar));
        profitValueTv.setText(String.format(Locale.ENGLISH,"%.2f", (order.getShippingCost() - order.getCommission())) + " " + getString(R.string.sar));
        double total = 0;
        if (order.getItemPrice() != null) {
            itemPriceLl.setVisibility(View.VISIBLE);
            itemPriceSeparatorView.setVisibility(View.VISIBLE);
            itemPriceValueTv.setText(String.format(Locale.ENGLISH,"%.2f", order.getItemPrice()) + " " + getString(R.string.sar));
            //totalValueTv.setText(String.format("%.2f", (order.getShippingCost()+order.getItemPrice()+order.getVat()))+" "+getString(R.string.sar));
            if (order.getDiscount() != null) {
                //total = order.getShippingCost() + order.getItemPrice() + order.getVat() - order.getDiscount();
                total = order.getShippingCost() + order.getItemPrice() - order.getDiscount();
            } else {
                //total = order.getShippingCost() + order.getItemPrice() + order.getVat();
                total = order.getShippingCost() + order.getItemPrice();
            }

        } else {
            itemPriceLl.setVisibility(View.GONE);
            itemPriceSeparatorView.setVisibility(View.GONE);
            //totalValueTv.setText(String.format("%.2f", (order.getShippingCost()+order.getVat()))+" "+getString(R.string.sar));
            if (order.getDiscount() != null) {
                //total = order.getShippingCost() + order.getVat() - order.getDiscount();
                total = order.getShippingCost() - order.getDiscount();
            } else {
                //total = order.getShippingCost() + order.getVat();
                total = order.getShippingCost();
            }

        }

        if (order.getDiscount() != null && order.getDiscount() != 0) {
            discountLl.setVisibility(View.VISIBLE);
            discountSeparatorView.setVisibility(View.VISIBLE);
            discountValueTv.setText(String.format(Locale.ENGLISH,"%.2f", -1 * order.getDiscount()) + " " + getString(R.string.sar));
        } else {
            discountLl.setVisibility(View.GONE);
            discountSeparatorView.setVisibility(View.GONE);
        }

        if (order.getTotalPrice() != null && order.getActualPaid() != null && (order.getActualPaid() - order.getTotalPrice() != 0)) {
            changesLl.setVisibility(View.VISIBLE);
            changesSeparatorView.setVisibility(View.VISIBLE);
            if (order.getActualPaid() > order.getTotalPrice()) {
                changesValueTv.setText("+" + String.format(Locale.ENGLISH,"%.2f", (order.getActualPaid() - order.getTotalPrice())) + " " + getString(R.string.sar));
            } else {
                changesValueTv.setText(String.format(Locale.ENGLISH,"%.2f", (order.getActualPaid() - order.getTotalPrice())) + " " + getString(R.string.sar));
            }

            total += order.getActualPaid() - order.getTotalPrice();
        } else {
            changesLl.setVisibility(View.GONE);
            changesSeparatorView.setVisibility(View.GONE);
        }
        if (total < 0) {
            totalValueTv.setText("0 " + getString(R.string.sar));
        } else {
            totalValueTv.setText(String.format(Locale.ENGLISH,"%.2f", total) + " " + getString(R.string.sar));
        }

    }

    private void changeCardsVisibility() {
        if (isCardsHided) {
            hideCardsIv.setImageResource(R.drawable.open_map);
            orderBaseInfoRl.setVisibility(View.VISIBLE);
            showOrderDetails(order);
            if (rateDialogRl.getVisibility() == View.VISIBLE) {
                hideKeyboard();
                rateDialogRl.setVisibility(View.GONE);
            }
        } else {
            hideCardsIv.setImageResource(R.drawable.close_map);
            orderBaseInfoRl.setVisibility(View.GONE);
            delegateReadyRl.setVisibility(View.GONE);
            delegateOfferRl.setVisibility(View.GONE);
        }
        isCardsHided = !isCardsHided;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {

            case R.id.iv_accept:
                openOfferPopup();
                break;

            case R.id.iv_cancel:
                //finish();
                openIgnorePopup();
                break;

            case R.id.tv_cancel_offer:
                cancelOfferPopup();
                break;

            case R.id.ll_popup_content:
                break;

            case R.id.ll_add_offer:
                offerPopupWindow.dismiss();
                setOfferPopup();
                break;

            case R.id.tv_ready:
                checkFinanceSettingsData(true);
                break;
            case R.id.iv_more:
                if (transparentView.getVisibility() == View.VISIBLE) {
                    transparentView.setVisibility(View.GONE);
                    menuLayout.setVisibility(View.GONE);
                } else {
                    transparentView.setVisibility(View.VISIBLE);
                    menuLayout.setVisibility(View.VISIBLE);

                }
                break;

            case R.id.transparent_view:
                transparentView.setVisibility(View.GONE);
                menuLayout.setVisibility(View.GONE);
                break;

            case R.id.cancelTxt:
                transparentView.setVisibility(View.GONE);
                menuLayout.setVisibility(View.GONE);
                //cancelOrderPopup();
                getOrderDetailsPresenter().getCancelationReasons(getLocalSettings().getLocale(), 2);
                break;

            case R.id.submit_complain:
                transparentView.setVisibility(View.GONE);
                menuLayout.setVisibility(View.GONE);
                switchToSubmitComplaint();
                break;

            case R.id.tv_reject:
                offerPopupWindow.dismiss();
                setOfferPopup();
                break;

            case R.id.iv_happy:
                if (!isHappySelected) {
                    isHappySelected = true;
                    isSmileSelected = false;
                    isMehSelected = false;
                    isSubtractionSelected = false;
                    isSadSelected = false;
                    popupRateHappyIv.setImageResource(R.drawable.smile_1__2);
                    popupRateSmileIv.setImageResource(R.drawable.smile_1__1);
                    popupRateMehIv.setImageResource(R.drawable.meh_1);
                    popupRateSubtractionIv.setImageResource(R.drawable.subtraction_22);
                    popupRateSadIv.setImageResource(R.drawable.sad_1);
                    rateNum = 5;
                    popupRateSubmitBtn.setAlpha(1);
                    popupRateSubmitBtn.setEnabled(true);
                }
                break;

            case R.id.iv_smile:
                if (!isSmileSelected) {
                    isHappySelected = false;
                    isSmileSelected = true;
                    isMehSelected = false;
                    isSubtractionSelected = false;
                    isSadSelected = false;
                    popupRateHappyIv.setImageResource(R.drawable.smile_1__2_1);
                    popupRateSmileIv.setImageResource(R.drawable.smile_1_);
                    popupRateMehIv.setImageResource(R.drawable.meh_1);
                    popupRateSubtractionIv.setImageResource(R.drawable.subtraction_22);
                    popupRateSadIv.setImageResource(R.drawable.sad_1);
                    rateNum = 4;
                    popupRateSubmitBtn.setAlpha(1);
                    popupRateSubmitBtn.setEnabled(true);
                }
                break;

            case R.id.iv_meh:
                if (!isMehSelected) {
                    isHappySelected = false;
                    isSmileSelected = false;
                    isMehSelected = true;
                    isSubtractionSelected = false;
                    isSadSelected = false;
                    popupRateHappyIv.setImageResource(R.drawable.smile_1__2_1);
                    popupRateSmileIv.setImageResource(R.drawable.smile_1__1);
                    popupRateMehIv.setImageResource(R.drawable.meh);
                    popupRateSubtractionIv.setImageResource(R.drawable.subtraction_22);
                    popupRateSadIv.setImageResource(R.drawable.sad_1);
                    rateNum = 3;
                    popupRateSubmitBtn.setAlpha(1);
                    popupRateSubmitBtn.setEnabled(true);
                }
                break;

            case R.id.iv_subtraction:
                if (!isSubtractionSelected) {
                    isHappySelected = false;
                    isSmileSelected = false;
                    isMehSelected = false;
                    isSubtractionSelected = true;
                    isSadSelected = false;
                    popupRateHappyIv.setImageResource(R.drawable.smile_1__2_1);
                    popupRateSmileIv.setImageResource(R.drawable.smile_1__1);
                    popupRateMehIv.setImageResource(R.drawable.meh_1);
                    popupRateSubtractionIv.setImageResource(R.drawable.subtraction_18);
                    popupRateSadIv.setImageResource(R.drawable.sad_1);
                    rateNum = 2;
                    popupRateSubmitBtn.setAlpha(1);
                    popupRateSubmitBtn.setEnabled(true);
                }
                break;

            case R.id.iv_sad:
                if (!isSadSelected) {
                    isHappySelected = false;
                    isSmileSelected = false;
                    isMehSelected = false;
                    isSubtractionSelected = false;
                    isSadSelected = true;
                    popupRateHappyIv.setImageResource(R.drawable.smile_1__2_1);
                    popupRateSmileIv.setImageResource(R.drawable.smile_1__1);
                    popupRateMehIv.setImageResource(R.drawable.meh_1);
                    popupRateSubtractionIv.setImageResource(R.drawable.subtraction_22);
                    popupRateSadIv.setImageResource(R.drawable.sad);
                    rateNum = 1;
                    popupRateSubmitBtn.setAlpha(1);
                    popupRateSubmitBtn.setEnabled(true);
                }
                break;

            case R.id.btn_submit_rating:
                popupRateSubmitBtn.setEnabled(false);
                getOrderDetailsPresenter().delegateRateOrder(getLocalSettings().getToken(), getLocalSettings().getLocale(), getOrderId(), rateNum,
                        popupRateCustomerEt.getText().toString(), true);
                break;

            case R.id.iv_rate_close:
                hideKeyboard();
                rateDialogRl.setVisibility(View.GONE);
                break;

            case R.id.ll_rate:
                hideKeyboard();
                rateDialogRl.setVisibility(View.GONE);
                break;

            case R.id.rl_rate_content:
                hideKeyboard();
                break;

            case R.id.tv_delegate_add_rate:
                openRatePopup();
                setRatePopupData();
                break;

            case R.id.tv_customer_name:
                switchToCustOrDelProfile(true, orderCustomerId);
                break;

            case R.id.iv_customer_image:
                switchToCustOrDelProfile(true, orderCustomerId);
                break;

            case R.id.tv_call_customer:
                openDialer(order.getCreatedBy().getMobile());
                break;

            case R.id.tv_reorder_yes:
                ignorePopupWindow.dismiss();
                getOrderDetailsPresenter().ignoreOrder(getLocalSettings().getToken(), getLocalSettings().getLocale(), getOrderId());
                break;

            case R.id.tv_reorder_no:
                ignorePopupWindow.dismiss();
                break;

            case R.id.ll_reorder:
                ignorePopupWindow.dismiss();
                break;

            case R.id.rl_reorder_content:
                break;

            case R.id.iv_hide_cards:
                changeCardsVisibility();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        selectedReason = cancelationReasons.get(i).getId();
        error_reason.setVisibility(View.GONE);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onBackPressed() {
        if (offerPopupWindow.isShowing()) {
            offerPopupWindow.dismiss();
            setOfferPopup();
        } else if (rateDialogRl.getVisibility() == View.VISIBLE) {
            hideKeyboard();
            rateDialogRl.setVisibility(View.GONE);
        } else if (transparentView.getVisibility() == View.VISIBLE) {
            transparentView.setVisibility(View.GONE);
            menuLayout.setVisibility(View.GONE);
        } else {
            /*ActivityManager mngr = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            int num;
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                num = mngr.getRunningTasks(1).get(0).numActivities;
            }else{
                num = mngr.getAppTasks().get(0).getTaskInfo().numActivities;
            }
            if(num == 1){
                Intent notificationIntent = new Intent(this, MainActivity.class);
                notificationIntent.putExtra(Constants.OPEN_FRAGMENT, NotificationFragment.class.getSimpleName());
                startActivity(notificationIntent);
                finish();
            }
            else {*/
            super.onBackPressed();
            //}
        }

    }

    @Override
    public void showPopupLoading() {
        popupLoaderPv.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        popupRejectTv.setEnabled(false);
        popupReadyTv.setEnabled(false);
        addOfferLl.setEnabled(false);
    }

    @Override
    public void hidePopupLoading() {
        popupLoaderPv.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        popupRejectTv.setEnabled(true);
        popupReadyTv.setEnabled(true);
        addOfferLl.setEnabled(true);
    }

    @Override
    public void showRatePopupLoading() {
        popupRateLoaderPv.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public void hideRatePopupLoading() {
        popupRateLoaderPv.setVisibility(View.GONE);
        popupRateSubmitBtn.setEnabled(true);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }


    @Override
    public void showNotAvailableOrderError(String errorMessage) {
        if (offerPopupWindow.isShowing()) {
            offerPopupWindow.dismiss();
        }
        mainContentRl.setVisibility(View.GONE);
        notAvailableOrderRl.setVisibility(View.VISIBLE);
        notAvailableOrderTv.setText(errorMessage);
        //Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showOrderDetails(Order order) {
        if (order != null) {
            this.order = order;
            if (!isFromChat()) {
                if (order.getStatus().equals("assigned") || order.getStatus().equals("in_progress") ||
                        order.getStatus().equals("delivery_in_progress")) {
                    currentLat = 0;
                    currentLng = 0;
                    //tracker.stopUsingGPS();
                }
            }
            super.showOrderDetails(order);
            fromIv.setImageResource(R.drawable.shop_ic);
            if (order.getOffers() != null && !order.getOffers().isEmpty()) {
                delegateOfferRl.setVisibility(View.VISIBLE);
                showOfferData(order.getOffers().get(0));
            }
            setCustomerData(order.getCreatedBy());
//            if (currentLat != 0 && currentLng != 0 && drawRoute && !order.getStatus().equals("delivery_in_progress") &&
//                    !order.getStatus().equals("delivered") && !order.getStatus().equals("cancelled")) {
//                getOrderDetailsPresenter().getDirections(currentLat + "," + currentLng, order.getFromLat() + "," + order.getFromLng(),
//                        "driving", getString(R.string.google_server_key), true);
//                drawRoute = false;
//            }

            if (order.getStatus().equals("assigned") || order.getStatus().equals("in_progress") || order.getStatus().equals("delivery_in_progress")
                    || order.getStatus().equals("delivered")) {
                setInvoiceData();
                if (!order.getStatus().equals("delivered")) {
                    callCustomerTv.setVisibility(View.VISIBLE);
                } else {
                    callCustomerTv.setVisibility(View.GONE);
                }
            } else if (order.getStatus().equals("cancelled")) {
                if (order.getShippingCost() != null) {
                    setInvoiceData();
                } else {
                    delegateOfferRl.setVisibility(View.GONE);
                }
                callCustomerTv.setVisibility(View.GONE);
            }
            if (order.getStatus().equals("delivered") ||
                    order.getStatus().equals("cancelled") || order.getStatus().equals("delivery_in_progress")) {
                removeDelegateDistance();
                if (order.getStatus().equals("delivery_in_progress")) {
                    fromIv.setImageResource(R.drawable.car_ic);
                } else {
                    fromIv.setImageResource(R.drawable.shop_ic);
                }
                if (routePoints != null) {
                    routePoints.clear();
                }
                if (newRoutePoints != null) {
                    newRoutePoints.clear();
                }

                if (polyline != null) {
                    polyline.getPoints().clear();
                    polyline.remove();
                }
                if (carMarker != null) {
                    carMarker.remove();
                }
            }
        /*else if(order.getStatus().equals("delivery_in_progress")){
            removeDelegateDistance();
            if (routePoints != null){
                routePoints.clear();
            }
            if (newRoutePoints != null){
                newRoutePoints.clear();
            }

            if (polyline != null) {
                polyline.getPoints().clear();
                polyline.remove();
            }
            if (carMarker != null){
                carMarker.remove();
            }
        }*/
            if ((order.getStatus().equals("assigned") || order.getStatus().equals("in_progress") || order.getStatus().equals("delivery_in_progress"))
                    && isFromChat()) {
                //commented by omnia
                //iv_more.setVisibility(View.VISIBLE);
                iv_more.setVisibility(View.VISIBLE);
                cancelTV.setVisibility(View.VISIBLE);
                submitComplainTV.setVisibility(View.VISIBLE);
                menuSeparatorV.setVisibility(View.VISIBLE);
            } else {
                //commented by omnia
                //iv_more.setVisibility(View.GONE);
                iv_more.setVisibility(View.VISIBLE);
                cancelTV.setVisibility(View.GONE);
                submitComplainTV.setVisibility(View.VISIBLE);
                menuSeparatorV.setVisibility(View.GONE);
            }
            if (order.getStatus().equals("delivered") && !order.isRated()) {
                addRateTv.setVisibility(View.VISIBLE);
                customerRateRb.setVisibility(View.GONE);
                openRatePopup();
                setRatePopupData();
            } else {
                addRateTv.setVisibility(View.GONE);
                customerRateRb.setVisibility(View.VISIBLE);
            }

            if (currentLat != 0 && currentLng != 0 && !isAfterOrderData) {
                //getPickupToDestLine() == null && polyline == null){
                setAddressData(null, null, currentLat, currentLng);
            }
        }
    }

    @Override
    public void showFreeCommission(boolean freeCommission) {
        Log.e("free", freeCommission + "");
        this.freeCommission = freeCommission;
    }

    @Override
    public void showMapData(double fromLat, double fromLng, double toLat, double toLng) {
        setMapOrderData(fromLat, fromLng, toLat, toLng);
    }

    @Override
    public void showOrderDirections(Route route, boolean isDelegateDirection) {
        Log.e("direction", isDelegateDirection + "");
        if (!isDelegateDirection) {
            super.showOrderDirections(route, isDelegateDirection);
        } else {
            if (!order.getStatus().equals("delivery_in_progress")) {

                if(order.getStatus().equals("pending")){
                    if (route.getRoutePolyLine() != null) {
                        setRoute(order.getFromLat(), order.getFromLng(), order.getToLat(), order.getToLng(), route);
                    }
                }
                else {
                    if (carMarker == null) {
                        carMarker = getGoogleMap().addMarker(new MarkerOptions().position(new LatLng(currentLat, currentLng)).icon(BitmapDescriptorFactory.fromResource(R.drawable.car_purple)));
                    } else {
                        carMarker.remove();
                        carMarker = getGoogleMap().addMarker(new MarkerOptions().position(new LatLng(currentLat, currentLng)).icon(BitmapDescriptorFactory.fromResource(R.drawable.car_purple)));

                    }
                }
                delegateToFromDistance = calculateDistanceFromLegs(route.getLegs());
                if (delegateToFromDistance > 0) {
                    if (delegateToFromDistance < 1000) {
                        delDistanceToFromTv.setText(String.format(Locale.ENGLISH,"%.1f", delegateToFromDistance) + " " + getString(R.string.meter));
                    } else {
                        delDistanceToFromTv.setText(String.format(Locale.ENGLISH,"%.1f", (delegateToFromDistance / 1000)) + " " + getString(R.string.kilometer));
                    }
                } else {
                    calculateDelegateAbsoluteDistance();
                }
            }


        }

        if (order.getOffers() != null && order.getOffers().isEmpty()) {
            if ((order.getStatus().equals("new") || order.getStatus().equals("pending")) &&
                    (getFromToToDistance() != 0 && delegateToFromDistance != 0)) {
                delegateReadyRl.setVisibility(View.VISIBLE);
                setOfferPopup();
            } else {
                delegateReadyRl.setVisibility(View.GONE);
            }
        } else {
            delegateReadyRl.setVisibility(View.GONE);
        }

    }

    @Override
    public void showDefaultDistance(boolean isDelegateDirection) {
        if (!isDelegateDirection) {
            super.showDefaultDistance(isDelegateDirection);
        } else {
            calculateDelegateAbsoluteDistance();
        }
        if (getFromToToDistance() != 0 && delegateToFromDistance != 0) {
            delegateReadyRl.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showSuccessAddOffer(Offer offer) {
        Log.e("offerid", offer.getId() + "  omnia");
        offerPopupWindow.dismiss();
        delegateReadyRl.setVisibility(View.GONE);
        delegateOfferRl.setVisibility(View.VISIBLE);
        showOfferData(offer);
    }

    @Override
    public void sucessCancel() {
        /*finish();

        Intent intent = new Intent (this,MainActivity.class);
        intent.putExtra(Constants.OPEN_FRAGMENT,MyOrdersFragment.class.getSimpleName());
        intent.putExtra(Constants.SelectDeliveryTabs,true);
        startActivity(intent);*/
        this.myOffer = null;
        delegateOfferRl.setVisibility(View.GONE);
        delegateReadyRl.setVisibility(View.VISIBLE);
        setOfferPopup();
    }

    @Override
    public void setAddressData(String countryNameCode, String city, double latitude, double longitude) {
        Log.e("setAddressData", "setAddressData");
        this.currentLat = latitude;
        this.currentLng = longitude;
//        if (delegateToFromDistance != 0) {
//            delegateToFromDistance -= SphericalUtil.computeDistanceBetween(currentPoint, new LatLng(latitude, longitude));
//            if (delegateToFromDistance > 0) {
//                if (delegateToFromDistance < 1000) {
//                    delDistanceToFromTv.setText(String.format(Locale.ENGLISH,"%.1f", delegateToFromDistance) + " " + getString(R.string.meter));
//                } else {
//                    delDistanceToFromTv.setText(String.format(Locale.ENGLISH,"%.1f", (delegateToFromDistance / 1000)) + " " + getString(R.string.kilometer));
//                }
//            }
//        }
        if (order != null) {
            isAfterOrderData = true;
            if (order.getStatus().equals("assigned") || order.getStatus().equals("in_progress")) {
                if (carMarker == null) {
                    carMarker = getGoogleMap().addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).icon(BitmapDescriptorFactory.fromResource(R.drawable.car_purple)));
                    currentPoint = new LatLng(latitude, longitude);
                } else {
                    updateDelegateLocation();
                    currentPoint = new LatLng(latitude, longitude);
                }
//                if (routePoints != null) {
//                    if (!PolyUtil.isLocationOnPath(new LatLng(latitude, longitude), routePoints, false, 20)) {
//                        drawRoute = true;
//                    } else {

//                        updateRoute();
//                    }
//
//                }
//                if (drawRoute) {
//                    getOrderDetailsPresenter().getDirections(latitude + "," + longitude, order.getFromLat() + "," + order.getFromLng(),
//                            "driving", getString(R.string.google_server_key), true);
//                    drawRoute = false;
//                }

                if(delegateToFromDistance == 0){
                    getOrderDetailsPresenter().getDirections(latitude + "," + longitude, order.getFromLat() + "," + order.getFromLng(),
                            "driving", getString(R.string.google_server_key), true);
                }
            } else if (order.getStatus().equals("delivery_in_progress")) {
                super.showDelegateCurrentLocation(latitude, longitude);
            } else if (order.getStatus().equals("new") || order.getStatus().equals("pending")) {
                this.currentLat = latitude;
                this.currentLng = longitude;
                if (carMarker == null) {
                    carMarker = getGoogleMap().addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).icon(BitmapDescriptorFactory.fromResource(R.drawable.car_purple)));
                    currentPoint = new LatLng(latitude, longitude);
                }
                getOrderDetailsPresenter().getDirections(latitude + "," + longitude, order.getFromLat() + "," + order.getFromLng(),
                        "driving", getString(R.string.google_server_key), true);
            }
        } else {
            isAfterOrderData = false;
            if (carMarker == null) {
                carMarker = getGoogleMap().addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).icon(BitmapDescriptorFactory.fromResource(R.drawable.car_purple)));
                currentPoint = new LatLng(latitude, longitude);
            } else {
                updateDelegateLocation();
                currentPoint = new LatLng(latitude, longitude);
            }
        }
    }

    @Override
    public void showDelegateCurrentLocation(double latitude, double longitude) {
        Log.e("delegateCurrentLocation", "setAddressData");
        setAddressData(null, null, latitude, longitude);
    }

    @Override
    public void openLocationSettings() {
        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), Constants.LOCATION_SETTINGS_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.LOCATION_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                tracker.getLocation();
                if(!isTrackingServiceRunning()){
                    startTracking();
                }
            } else {
                if(isTrackingServiceRunning()){
                    stopTracking();
                }
                finish();
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == Constants.LOCATION_SETTINGS_REQUEST) {
//            tracker.getLocation();
//        } else if (requestCode == Constants.CHECK_LOCATION_SETTINGS_REQUEST) {
//            if (resultCode == Activity.RESULT_OK) {
//                tracker.getLocation();
//            }
//        }
        if (requestCode == LOCATION_SETTINGS_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if(!isTrackingServiceRunning()){
                    startTracking();
                }
            }
            else {
                if(isTrackingServiceRunning()){
                    stopTracking();
                }
            }
        }
    }

    @Override
    public void showActivateGPS() {

    }

    @Override
    public void showOrderCancelledSucessfully() {
        getOrderDetailsPresenter().getDelegateOrderDetails(getLocalSettings().getToken(), getLocalSettings().getLocale(), getOrderId());

    }

    @Override
    public void showSuccessOrderRate(Order order) {
        this.order = order;
        addRateTv.setVisibility(View.GONE);
        customerRateRb.setVisibility(View.VISIBLE);
        hideKeyboard();
        rateDialogRl.setVisibility(View.GONE);
    }

    @Override
    public void showCancelationReasons(ArrayList<CancelationReason> cancelationReasons) {
        this.cancelationReasons.clear();
        this.cancelationReasons.add(new CancelationReason(0, getResources().getString(R.string.choose_reason)));
        this.cancelationReasons.addAll(cancelationReasons);
        cancelOrderPopup();
    }

    @Override
    public void showSuccessIgnoreOrder() {
        finish();
    }

    private void cancelOfferPopup() {
        final Fonts fonts = MApplication.getInstance().getFonts();
        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("")
                .setMessage(this.getResources().getString(R.string.cancel_msg))
                .setPositiveButton(this.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).setNegativeButton(this.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String id_offer = myOffer.getId() + "";
                        getOrderDetailsPresenter().cancelOffer(getLocalSettings().getToken(), getLocalSettings().getLocale(), id_offer);

                        dialogInterface.cancel();
                    }
                }).create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                TextView title = alertDialog.findViewById(R.id.alertTitle);
                TextView message = alertDialog.findViewById(android.R.id.message);
                Button btnYes = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                Button btnNo = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                title.setTypeface(fonts.customFont());
                message.setTypeface(fonts.customFont());
                btnYes.setTypeface(fonts.customFont());
                btnNo.setTypeface(fonts.customFont());
            }
        });
        alertDialog.show();
    }

    private void cancelOrderPopup() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialog);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_cancel_order, null);
        builder.setView(dialogView);
        TextView cancel_message = dialogView.findViewById(R.id.cancel_message);

        Spinner cancelationReasonsSp = dialogView.findViewById(R.id.sp_cancelation_reasons);
        //CancelationReasonsAdapter adpter = new CancelationReasonsAdapter(this, R.layout.spinneritem, cancelationReasons);
        CancelationReasonsAdapter adpter = new CancelationReasonsAdapter(this, cancelationReasons);
        //adpter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        cancelationReasonsSp.setAdapter(adpter);
        cancelationReasonsSp.setOnItemSelectedListener(this);

        cancel_message.setText(getResources().getString(R.string.cancel_delegate_order));
        TextView submit = dialogView.findViewById(R.id.yes);
        TextView no = dialogView.findViewById(R.id.no);
        //final EditText cancel_et= dialogView.findViewById(R.id.cancel_et);
        //cancel_et.setVisibility(View.VISIBLE);
        error_reason = dialogView.findViewById(R.id.error_reason);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if (cancel_et.getText().toString().equals("")) {
                    error_reason.setVisibility(View.VISIBLE);
                }else {
                    error_reason.setVisibility(View.GONE);*/
                String id_order = getOrderId() + "";
                if (selectedReason == 0) {
                    error_reason.setVisibility(View.VISIBLE);
                } else {
                    error_reason.setVisibility(View.GONE);
                    getOrderDetailsPresenter().cancelOrder(getLocalSettings().getToken(), getLocalSettings().getLocale(), id_order, true, selectedReason);
                    cancel_dialoug.dismiss();
                }
            }
            //}
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel_dialoug.dismiss();
            }
        });

        cancel_message.setTypeface(fonts.customFont());
        submit.setTypeface(fonts.customFont());
        no.setTypeface(fonts.customFont());
        //cancel_et.setTypeface(fonts.customFont());
        error_reason.setTypeface(fonts.customFont());

        cancel_dialoug = builder.show();

    }

    public void startTracking(){
        trackingSettings.startTracking();
    }

    public void stopTracking(){
        trackingSettings.stopTrackingService();
    }

    public boolean isTrackingServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if(manager != null){
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (TrackingService.class.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
        }
        return false;
    }

}
