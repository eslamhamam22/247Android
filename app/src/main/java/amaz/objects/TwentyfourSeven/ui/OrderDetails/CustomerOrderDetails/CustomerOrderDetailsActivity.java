package amaz.objects.TwentyfourSeven.ui.OrderDetails.CustomerOrderDetails;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import com.airbnb.lottie.LottieAnimationView;
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
import com.google.gson.Gson;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.SphericalUtil;
import com.rey.material.widget.ProgressView;
import com.squareup.picasso.Picasso;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import amaz.objects.TwentyfourSeven.MApplication;
import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.adapters.CancelationReasonsAdapter;
import amaz.objects.TwentyfourSeven.adapters.OffersAdapter;
import amaz.objects.TwentyfourSeven.api.APIURLs;
import amaz.objects.TwentyfourSeven.data.models.CancelationReason;
import amaz.objects.TwentyfourSeven.data.models.Offer;
import amaz.objects.TwentyfourSeven.data.models.Order;
import amaz.objects.TwentyfourSeven.data.models.Route;
import amaz.objects.TwentyfourSeven.listeners.OnOfferClickListener;
import amaz.objects.TwentyfourSeven.listeners.OnRefeshTokenResponse;
import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import amaz.objects.TwentyfourSeven.presenter.PresenterFactory;
import amaz.objects.TwentyfourSeven.ui.OrderChat.OrderChatActivity;
import amaz.objects.TwentyfourSeven.ui.OrderDetails.OrderDetailsActivity;
import amaz.objects.TwentyfourSeven.utilities.AreasUtility;
import amaz.objects.TwentyfourSeven.utilities.Constants;
import amaz.objects.TwentyfourSeven.utilities.Fonts;

public class CustomerOrderDetailsActivity extends OrderDetailsActivity implements View.OnClickListener, OnRefeshTokenResponse,
        OnOfferClickListener, AdapterView.OnItemSelectedListener {

    private ImageView fromIv, delIv, iv_more;
    private TextView waitingDriversTv, orderDetailsTitleTv, waitingTimeTv, noDelegatesTv, noOffersTv, searchAgainTv, offersTv, cancelTV, submitComplainTV;
    private View menuSeparatorV, overlayV;
    private ExpandableLayout offersEl;
    private RelativeLayout delegateDistanceRl, waitingDriverRl, menuLayout, transparentView;
    private LottieAnimationView offersLoaderLv;
    private Button cancelBtn;
    private RelativeLayout offersRl;
    private RecyclerView offersRv;
    private OffersAdapter offersAdapter;
    private ArrayList<Offer> offersList = new ArrayList<>();
    private Button reorderBtn;

    private RelativeLayout orderInvoiceRl;
    private LinearLayout invoiceItemPriceLl, invoiceChangesLl, invoiceDiscountLl;
    private TextView invoiceDelegateNameTv, invoiceTotalTitleTv, invoiceTotalValueTv, callDelegateTv, invoiceItemPriceTitleTv, invoiceItemPriceValueTv,
            invoiceShippingTitleTv, invoiceShippingValueTv, invoiceCommissionTitleTv,
            invoiceCommissionValueTv, invoiceVatTitleTv, invoiceVatValueTv, invoiceDiscountTitleTv, invoiceDiscountValueTv, invoiceChangesTitleTv, invoiceChangesValueTv, addRateTv;
    private ImageView invoiceDelegateImageIv;
    private View invoiceItemPriceView, invoiceChangesView, invoiceDiscountView;
    private RatingBar delegateRateRb;
    private Button reorderBtnFromInvoice;

    private PopupWindow reorderPopupWindow;
    private LinearLayout popupReorderLl;
    private RelativeLayout popupReorderContentRl;
    private TextView popupReorderMessageTv, popupReorderYesTv, popupReorderNoTv;

    //private PopupWindow ratePopupWindow;
    private LinearLayout rateLl;
    private RelativeLayout rateDialogRl, rateContentRl;
    private TextView popupRateTitleTv, popupRateDelegateNameTv, popupRateMessage;
    private ImageView popupRateDelegateIv, popupRateCloseIv, popupRateHappyIv, popupRateSmileIv, popupRateMehIv, popupRateSubtractionIv,
            popupRateSadIv;
    private View popupRateSeparator;
    private EditText popupRateDelegateEt;
    private Button popupRateSubmitBtn;
    private ProgressView popupRateLoaderPv;
    private boolean isHappySelected, isSmileSelected, isMehSelected, isSubtractionSelected, isSadSelected;
    private int rateNum;

    private Fonts fonts;
    private Dialog cancel_dialoug;

    private int days;
    private int houres;
    private int minutes;
    private int seconds;
    private long totalSeconds;
    //private Timer timer;
    private CountDownTimer timer;
    private String imagesIdsString;

    private BroadcastReceiver mBroadcastReceiver;
    private boolean firstConnect = false;

    private Order order;

    private double delegateToFromDistance, delLat, delLng;
    private Polyline polyline;
    private TextView delDistanceToFromTv;
    //private boolean drawDelToPickRoute = true;
    private Marker carMarker;
    private boolean isMarkerRotating;
    private List<LatLng> routePoints;
    private List<LatLng> newRoutePoints = new ArrayList<>();
    private LatLng currentPoint;

    private int orderDelegateId;

    private TextView error_reason;
    private ArrayList<CancelationReason> cancelationReasons = new ArrayList<>();
    private int selectedReason = 0;

    private boolean isActivityVisible, isNotificationReceived;

    private ImageView hideCardsIv;
    private RelativeLayout orderBaseInfoRl;
    private boolean isCardsHided = false;


    @Override
    public void onPresenterAvailable(BasePresenter presenter) {
        super.onPresenterAvailable(presenter);
        getOrderDetailsPresenter().setView(this);
        if (order == null) {
            if (waitingDriverRl.getVisibility() == View.VISIBLE) {
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
                waitingDriverRl.setVisibility(View.GONE);
                overlayV.setVisibility(View.GONE);
            }
            getOrderDetailsPresenter().getCustomerOrderDetails(getLocalSettings().getToken(), getLocalSettings().getLocale(), getOrderId());
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
        initializeDelegateRatePopup();
        initializeReorderPopup();
        setFonts();
        setBroadCast();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver,
                new IntentFilter(Constants.BROADCASTRECEVIERGENERATION));
        firstConnect = true;
        if (isNotificationReceived) {
            if(isCardsHided){
                isCardsHided = false;
                hideCardsIv.setImageResource(R.drawable.open_map);
                orderBaseInfoRl.setVisibility(View.VISIBLE);
            }
            if (waitingDriverRl.getVisibility() == View.VISIBLE) {
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
                waitingDriverRl.setVisibility(View.GONE);
                overlayV.setVisibility(View.GONE);
            }
            getOrderDetailsPresenter().getCustomerOrderDetails(getLocalSettings().getToken(), getLocalSettings().getLocale(), getOrderId());
            //firstConnect = false;`
            isNotificationReceived = false;
        }
        isActivityVisible = true;
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

    public void setBroadCast() {

        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.e("received", "received customer");
                // checking for type intent filter
                if (intent.getAction().equals(Constants.BROADCASTRECEVIERGENERATION)) {
                    if (firstConnect) {
                        String link_to = intent.getStringExtra("link_to");
                        Log.e("link_to", link_to);
                        /*if (link_to.equals("order_details")) {
                            if (waitingDriverRl.getVisibility() == View.VISIBLE) {
                                waitingDriverRl.setVisibility(View.GONE);
                            }
                            getOrderDetailsPresenter().getCustomerOrderDetails(getLocalSettings().getToken(), getLocalSettings().getLocale(), getOrderId());
                            //firstConnect = false;`
                        }*/

                        if (link_to.equals("order_details")) {
                            setStatusChanged(true);
                            String orderNotificationData = intent.getStringExtra("order_data");
                            if (orderNotificationData != null) {
                                try {
                                    JSONObject orderNotificationJson = new JSONObject(orderNotificationData);
                                    if (orderNotificationJson.getInt("id") == getOrderId()) {
                                        if (isActivityVisible) {
                                            if(isCardsHided){
                                                isCardsHided = false;
                                                hideCardsIv.setImageResource(R.drawable.open_map);
                                                orderBaseInfoRl.setVisibility(View.VISIBLE);
                                            }
                                            if (waitingDriverRl.getVisibility() == View.VISIBLE) {
                                                if (timer != null) {
                                                    timer.cancel();
                                                    timer = null;
                                                }
                                                waitingDriverRl.setVisibility(View.GONE);
                                                overlayV.setVisibility(View.GONE);
                                            }
                                            getOrderDetailsPresenter().getCustomerOrderDetails(getLocalSettings().getToken(), getLocalSettings().getLocale(), getOrderId());
                                            //firstConnect = false;`
                                        }
                                        else {
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

    private void initialization() {
        orderDetailsTitleTv = findViewById(R.id.tv_order_details_title);
        orderDetailsTitleTv.setText(R.string.my_order);
        fromIv = findViewById(R.id.iv_from_image);
        delegateDistanceRl = findViewById(R.id.rl_delegate_distance);
        delIv = findViewById(R.id.iv_delegate);
        delDistanceToFromTv = findViewById(R.id.tv_distance_del_from);

        waitingDriverRl = findViewById(R.id.rl_waiting_driver);
        waitingDriversTv = findViewById(R.id.tv_waiting_drivers);
        waitingTimeTv = findViewById(R.id.tv_waiting_time);
        noDelegatesTv = findViewById(R.id.tv_no_drivers);
        noOffersTv = findViewById(R.id.tv_no_offers);
        searchAgainTv = findViewById(R.id.tv_reapeat_search);
        iv_more = findViewById(R.id.iv_more);
        iv_more.setOnClickListener(this);
        overlayV = findViewById(R.id.v_overlay);

        menuLayout = findViewById(R.id.menu_layout);
        transparentView = findViewById(R.id.transparent_view);
        transparentView.setOnClickListener(this);

        cancelTV = findViewById(R.id.cancelTxt);
        submitComplainTV = findViewById(R.id.submit_complain);
        menuSeparatorV = findViewById(R.id.v_menu_separator);

        submitComplainTV.setOnClickListener(this);
        cancelTV.setOnClickListener(this);

        searchAgainTv.setOnClickListener(this);
        cancelBtn = findViewById(R.id.btn_cancel);
        cancelBtn.setOnClickListener(this);

        offersLoaderLv = findViewById(R.id.lv_offers_loader);
        if(getLocalSettings().getLocale().equals(Constants.ARABIC)){
            offersLoaderLv.setAnimation(R.raw.offers_arabic_loader);
        }
        else {
            offersLoaderLv.setAnimation(R.raw.offers_loader);
        }
        offersRl = findViewById(R.id.rl_offers);
        offersEl = findViewById(R.id.el_offers);
        offersTv = findViewById(R.id.tv_offers);
        offersTv.setOnClickListener(this);
        offersRv = findViewById(R.id.rv_offers);
        offersRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        offersAdapter = new OffersAdapter(this, offersList, this);
        offersRv.setAdapter(offersAdapter);
        reorderBtn = findViewById(R.id.btn_reorder);
        reorderBtn.setOnClickListener(this);

        orderInvoiceRl = findViewById(R.id.rl_order_invoice);
        invoiceItemPriceLl = findViewById(R.id.ll_invoice_item_price);
        invoiceItemPriceView = findViewById(R.id.v_invoice_item_price_separator);
        invoiceDelegateNameTv = findViewById(R.id.tv_invoice_cust_del_name);
        invoiceDelegateNameTv.setOnClickListener(this);
        callDelegateTv = findViewById(R.id.tv_call_delegate);
        callDelegateTv.setOnClickListener(this);
        invoiceDelegateImageIv = findViewById(R.id.iv_invoice_cust_del_image);
        invoiceDelegateImageIv.setOnClickListener(this);
        invoiceTotalTitleTv = findViewById(R.id.tv_invoice_total_title);
        invoiceTotalValueTv = findViewById(R.id.tv_invoice_total_value);
        delegateRateRb = findViewById(R.id.rb_delegate_rate);
        invoiceItemPriceTitleTv = findViewById(R.id.tv_invoice_item_price_title);
        invoiceItemPriceValueTv = findViewById(R.id.tv_invoice_item_price_value);
        invoiceShippingTitleTv = findViewById(R.id.tv_invoice_shipping_title);
        invoiceShippingValueTv = findViewById(R.id.tv_invoice_shipping_value);
        invoiceCommissionTitleTv = findViewById(R.id.tv_invoice_commission_title);
        invoiceCommissionValueTv = findViewById(R.id.tv_invoice_commission_value);
        invoiceVatTitleTv = findViewById(R.id.tv_invoice_vat_title);
        invoiceVatValueTv = findViewById(R.id.tv_invoice_vat_value);
        invoiceChangesLl = findViewById(R.id.ll_invoice_changes);
        invoiceDiscountLl = findViewById(R.id.ll_invoice_discount);
        invoiceChangesTitleTv = findViewById(R.id.tv_invoice_changes_title);
        invoiceChangesValueTv = findViewById(R.id.tv_invoice_changes_value);
        invoiceChangesView = findViewById(R.id.v_invoice_changes_separator);
        invoiceDiscountTitleTv = findViewById(R.id.tv_invoice_discount_title);
        invoiceDiscountValueTv = findViewById(R.id.tv_invoice_discount_value);
        invoiceDiscountView = findViewById(R.id.v_invoice_discount_separator);
        addRateTv = findViewById(R.id.tv_customer_add_rate);
        addRateTv.setOnClickListener(this);
        reorderBtnFromInvoice = findViewById(R.id.btn_reorder_from_invoice);
        reorderBtnFromInvoice.setOnClickListener(this);

        hideCardsIv = findViewById(R.id.iv_hide_cards);
        hideCardsIv.setOnClickListener(this);
        orderBaseInfoRl = findViewById(R.id.rl_order_base_info);
    }

    private void initializeDelegateRatePopup() {
        /*View v = LayoutInflater.from(this).inflate(R.layout.dialog_rate, null, false);
        ratePopupWindow = new PopupWindow(v, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);*/

        rateDialogRl = findViewById(R.id.rl_rate_dialog);
        rateLl = findViewById(R.id.ll_rate);
        rateLl.setOnClickListener(this);
        rateContentRl = findViewById(R.id.rl_rate_content);
        rateContentRl.setOnClickListener(this);
        popupRateTitleTv = findViewById(R.id.tv_rate_review_cust_del);
        popupRateTitleTv.setText(R.string.review_driver);
        popupRateCloseIv = findViewById(R.id.iv_rate_close);
        popupRateCloseIv.setOnClickListener(this);
        popupRateDelegateIv = findViewById(R.id.iv_rate_cust_del_image);
        popupRateDelegateNameTv = findViewById(R.id.tv_rate_cust_del_name);
        popupRateDelegateEt = findViewById(R.id.et_rate_cust_del);

        popupRateMessage = findViewById(R.id.tv_rate_cust_del);
        popupRateMessage.setText(R.string.rate_driver);
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

    private void initializeReorderPopup() {
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_reorder, null, false);
        reorderPopupWindow = new PopupWindow(v, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        popupReorderLl = v.findViewById(R.id.ll_reorder);
        popupReorderLl.setOnClickListener(this);
        popupReorderContentRl = v.findViewById(R.id.rl_reorder_content);
        popupReorderContentRl.setOnClickListener(this);
        popupReorderMessageTv = v.findViewById(R.id.tv_reorder_message);
        popupReorderYesTv = v.findViewById(R.id.tv_reorder_yes);
        popupReorderYesTv.setOnClickListener(this);
        popupReorderNoTv = v.findViewById(R.id.tv_reorder_no);
        popupReorderNoTv.setOnClickListener(this);
    }

    private void setFonts() {
        fonts = MApplication.getInstance().getFonts();
        orderDetailsTitleTv.setTypeface(fonts.customFontBD());
        delDistanceToFromTv.setTypeface(fonts.customFont());
        waitingDriversTv.setTypeface(fonts.customFont());
        waitingTimeTv.setTypeface(fonts.customFont());
        noDelegatesTv.setTypeface(fonts.customFont());
        noOffersTv.setTypeface(fonts.customFont());
        searchAgainTv.setTypeface(fonts.customFont());
        cancelBtn.setTypeface(fonts.customFontBD());
        offersTv.setTypeface(fonts.customFontBD());
        submitComplainTV.setTypeface(fonts.customFont());
        cancelTV.setTypeface(fonts.customFont());
        reorderBtn.setTypeface(fonts.customFontBD());

        invoiceDelegateNameTv.setTypeface(fonts.customFont());
        invoiceTotalTitleTv.setTypeface(fonts.customFont());
        invoiceTotalValueTv.setTypeface(fonts.customFontBD());
        callDelegateTv.setTypeface(fonts.customFontBD());
        invoiceItemPriceTitleTv.setTypeface(fonts.customFont());
        invoiceItemPriceValueTv.setTypeface(fonts.customFontBD());
        invoiceShippingTitleTv.setTypeface(fonts.customFont());
        invoiceShippingValueTv.setTypeface(fonts.customFontBD());
        invoiceCommissionTitleTv.setTypeface(fonts.customFont());
        invoiceCommissionValueTv.setTypeface(fonts.customFontBD());
        invoiceVatTitleTv.setTypeface(fonts.customFont());
        invoiceVatValueTv.setTypeface(fonts.customFontBD());
        invoiceChangesTitleTv.setTypeface(fonts.customFont());
        invoiceChangesValueTv.setTypeface(fonts.customFontBD());
        invoiceDiscountTitleTv.setTypeface(fonts.customFont());
        invoiceDiscountValueTv.setTypeface(fonts.customFontBD());
        reorderBtnFromInvoice.setTypeface(fonts.customFontBD());

        popupRateTitleTv.setTypeface(fonts.customFontBD());
        popupRateDelegateNameTv.setTypeface(fonts.customFont());
        popupRateDelegateEt.setTypeface(fonts.customFont());
        popupRateMessage.setTypeface(fonts.customFont());
        popupRateSubmitBtn.setTypeface(fonts.customFontBD());

        popupReorderMessageTv.setTypeface(fonts.customFont());
        popupReorderYesTv.setTypeface(fonts.customFont());
        popupReorderNoTv.setTypeface(fonts.customFont());
    }

    private void openReorderPopup() {
        reorderPopupWindow.showAtLocation(getMainContentRl(), Gravity.CENTER, 0, 0);
    }

    private void openRatePopup() {
        /*ratePopupWindow.setFocusable(true);
        ratePopupWindow.update();
        ratePopupWindow.showAtLocation(getMainContentRl(), Gravity.CENTER, 0, 0);*/
        rateDialogRl.setVisibility(View.VISIBLE);
    }

    private void setRatePopupData() {
        popupRateDelegateEt.setText("");
        popupRateDelegateNameTv.setText(order.getDelegate().getName());
        if (order.getDelegate().getImage() != null) {
            Picasso.with(this).load(order.getDelegate().getImage().getMedium()).placeholder(R.drawable.avatar)
                    .error(R.drawable.avatar).into(popupRateDelegateIv);
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

    private void setInvoiceData() {
        this.orderDelegateId = order.getDelegate().getId();
        orderInvoiceRl.setVisibility(View.VISIBLE);
        invoiceDelegateNameTv.setText(order.getDelegate().getName());
        if (order.getDelegate().getImage() != null) {
            Picasso.with(this).load((order.getDelegate().getImage().getMedium())).placeholder(R.drawable.avatar)
                    .error(R.drawable.avatar).into(invoiceDelegateImageIv);
        }
        if (order.getDelegate().getDelegateRating() == 0) {
            delegateRateRb.setRating(5);
        } else {
            delegateRateRb.setRating(order.getDelegate().getDelegateRating());
        }

        invoiceShippingValueTv.setText(String.format(Locale.ENGLISH,"%.2f", order.getShippingCost()) + " " + getString(R.string.sar));
        //invoiceVatValueTv.setText(String.format("%.2f", order.getVat()) + " " + getString(R.string.sar));
        double total = 0;
        if (order.getItemPrice() != null) {
            invoiceItemPriceLl.setVisibility(View.VISIBLE);
            invoiceItemPriceView.setVisibility(View.VISIBLE);
            invoiceItemPriceValueTv.setText(String.format(Locale.ENGLISH,"%.2f", order.getItemPrice()) + " " + getString(R.string.sar));
            //invoiceTotalValueTv.setText(String.format("%.2f", (order.getShippingCost()+order.getItemPrice()+order.getVat()))+" "+getString(R.string.sar))
            if (order.getDiscount() != null){
                //total = order.getShippingCost() + order.getItemPrice() + order.getVat() - order.getDiscount();
                total = order.getShippingCost() + order.getItemPrice() - order.getDiscount();
            }
            else {
                //total = order.getShippingCost() + order.getItemPrice() + order.getVat();
                total = order.getShippingCost() + order.getItemPrice();
            }

        } else {
            invoiceItemPriceLl.setVisibility(View.GONE);
            invoiceItemPriceView.setVisibility(View.GONE);
            //invoiceTotalValueTv.setText(String.format("%.2f", (order.getShippingCost()+order.getVat()))+" "+getString(R.string.sar));
            if (order.getDiscount() != null){
                //total = order.getShippingCost() + order.getVat() - order.getDiscount();
                total = order.getShippingCost() - order.getDiscount();
            }
            else {
                //total = order.getShippingCost() + order.getVat();
                total = order.getShippingCost();
            }

        }

        if (order.getDiscount() != null && order.getDiscount() != 0){
            invoiceDiscountLl.setVisibility(View.VISIBLE);
            invoiceDiscountView.setVisibility(View.VISIBLE);
            invoiceDiscountValueTv.setText(String.format(Locale.ENGLISH,"%.2f", -1*order.getDiscount()) + " " + getString(R.string.sar));
        }
        else {
            invoiceDiscountLl.setVisibility(View.GONE);
            invoiceDiscountView.setVisibility(View.GONE);
        }

        if (order.getTotalPrice() != null && order.getActualPaid() != null && (order.getActualPaid() - order.getTotalPrice() != 0)) {
            invoiceChangesLl.setVisibility(View.VISIBLE);
            invoiceChangesView.setVisibility(View.VISIBLE);
            if (order.getActualPaid() > order.getTotalPrice()) {
                invoiceChangesValueTv.setText("+" + String.format(Locale.ENGLISH,"%.2f", (order.getActualPaid() - order.getTotalPrice())) + " " + getString(R.string.sar));
            } else {
                invoiceChangesValueTv.setText(String.format(Locale.ENGLISH,"%.2f", (order.getActualPaid() - order.getTotalPrice())) + " " + getString(R.string.sar));
            }
            total += order.getActualPaid() - order.getTotalPrice();
        } else {
            invoiceChangesLl.setVisibility(View.GONE);
            invoiceChangesView.setVisibility(View.GONE);
        }
        if (total < 0){
            invoiceTotalValueTv.setText("0 " + getString(R.string.sar));
        }
        else {
            invoiceTotalValueTv.setText(String.format(Locale.ENGLISH,"%.2f", total) + " " + getString(R.string.sar));
        }

    }

    public void toggleOffersDialog() {
        if (isOrderItemsExpanded()) {
            collapseOrderItemsDialog();
        }
        offersEl.toggle(true);
        //if (isOffersExpanded) {
        //isOffersExpanded = false;
        //offersTv.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(this,R.drawable.offers_ic), null, ContextCompat.getDrawable(this,R.drawable.anchor_upward),null);
        //} else {
        //isOffersExpanded = true;
        //offersTv.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(this,R.drawable.offers_ic), null, ContextCompat.getDrawable(this,R.drawable.anchor_downward),null);
        //}
    }

    public void collapseOffersDialog() {
        offersEl.collapse(true);
        //offersTv.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(this,R.drawable.order_items_ic), null, ContextCompat.getDrawable(this,R.drawable.anchor_upward),null);
        //isOffersExpanded = false;
    }

    /*private void startTimer() {

        timer = new Timer();
        TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("timer", "timer");
                        totalSeconds = ((new Date()).getTime() - getOrderDate().getTime()) / 1000;
                        days = (int) totalSeconds / (60 * 60 * 24);
                        houres = (int) ((totalSeconds / (60 * 60)) - (days * 24));
                        minutes = (int) ((totalSeconds / 60) - ((houres * 60) + (days * 24 * 60)));
                        seconds = (int) (totalSeconds - ((minutes * 60) + (houres * 60 * 60) + (days * 24 * 60 * 60)));
                        if (days > 0) {
                            if (days == 1) {
                                waitingTimeTv.setText(days + " " + getString(R.string.day));
                            } else {
                                waitingTimeTv.setText(days + " " + getString(R.string.days));
                            }
                            if (timer != null) {
                                timer.cancel();
                            }

                        } else if (houres > 0) {
                            waitingTimeTv.setText(String.format("%02d:%02d:%02d", houres, minutes, seconds));
                        } else {
                            waitingTimeTv.setText(String.format("%02d:%02d", minutes, seconds));
                        }
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 1000);
    }*/

    public void startTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        Log.e("order time", (5 * 60 * 1000) - ((new Date()).getTime() - getOrderDate().getTime()) + " ");
        timer = new CountDownTimer((5 * 60 * 1000) - ((new Date()).getTime() - getOrderDate().getTime()), 1000) {
            @Override
            public void onFinish() {
                // TODO Auto-generated method stub
                waitingDriversTv.setVisibility(View.GONE);
                waitingTimeTv.setVisibility(View.GONE);
                noOffersTv.setVisibility(View.INVISIBLE);
                noDelegatesTv.setVisibility(View.GONE);
                searchAgainTv.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTick(long millisUntilFinished) {
                // TODO Auto-generated method stub
                minutes = (int) millisUntilFinished / (60 * 1000);
                seconds = (int) ((millisUntilFinished / 1000) - (minutes * 60));
                waitingTimeTv.setText(String.format(Locale.ENGLISH,"%02d:%02d", minutes, seconds));
            }

        }.start();
    }

    public boolean isOffersExpanded() {
        return offersEl.isExpanded();
    }

    private void generateImagesIdsString() {
        if (order.getImages() != null && !order.getImages().isEmpty()) {
            ArrayList<Long> imagesIds = new ArrayList<>();
            for (int i = 0; i < order.getImages().size(); i++) {
                imagesIds.add(order.getImages().get(i).getId());
            }
            Gson gson = new Gson();
            String idsJson = gson.toJson(imagesIds);
            imagesIdsString = idsJson.substring(1, idsJson.lastIndexOf("]"));
            Log.e("imagesids", imagesIdsString + "  omnia");
        }
    }

//    private void setRoute(final double fromLat, final double fromLng, final double toLat, final double toLng, Route route) {
//        List<LatLng> polyz = PolyUtil.decode(route.getRoutePolyLine().getPoints());
//        Log.e("polys", polyz.size() + "");
//        this.routePoints = polyz;
//        //this.delegateToFromLatLngs = polyz;
//        if (polyz != null) {
//            PolylineOptions lineOptions = new PolylineOptions();
//            lineOptions.addAll(polyz);
//            lineOptions.width(5);
//            lineOptions.color(ContextCompat.getColor(this, R.color.app_color));
//
//            LatLng fromLocation = new LatLng(fromLat, fromLng);
//            LatLng toLocation = new LatLng(toLat, toLng);
//            final LatLngBounds.Builder builder = new LatLngBounds.Builder();
//            builder.include(fromLocation);
//            builder.include(toLocation);
//            for (int i = 0; i < polyz.size(); i++) {
//                builder.include(polyz.get(i));
//            }
//            final LatLngBounds bounds = builder.build();
//
//            if (getGoogleMap() != null) {
//                getGoogleMap().setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
//                    @Override
//                    public void onMapLoaded() {
//                        try{
//                            getGoogleMap().animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 270));
//                        } catch (IllegalStateException ex){
//                            getGoogleMap().animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 200));
//                        }
//                        //getGoogleMap().animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 300));
//
//                    }
//                });
//
//            }
//            if (polyline != null) {
//                polyline.remove();
//            }
//            polyline = getGoogleMap().addPolyline(lineOptions);
//            /*if ((order.getStatus().equals("assigned") || order.getStatus().equals("in_progress") || order.getStatus().equals("delivery_in_progress")) && isFromChat()){
//                moveOnRoute();
//            }*/
//
//
//        }
//    }
//
    private void calculateDelegateAbsoluteDistance() {
        Location fromDelegateLocation = new Location("fromDelegateLocation");
        fromDelegateLocation.setLatitude(delLat);
        fromDelegateLocation.setLongitude(delLng);

        Location fromLocation = new Location("fromLocation");
        fromLocation.setLatitude(order.getFromLat());
        fromLocation.setLongitude(order.getFromLng());

        delegateToFromDistance = fromDelegateLocation.distanceTo(fromLocation);
        if (delegateToFromDistance < 1000) {
            delDistanceToFromTv.setText(String.format(Locale.ENGLISH,"%.1f", delegateToFromDistance) + " " + getString(R.string.meter));
        } else {
            delDistanceToFromTv.setText(String.format(Locale.ENGLISH,"%.1f", (delegateToFromDistance / 1000)) + " " + getString(R.string.kilometer));
        }
    }

    private void updateDelegateLocation() {
        LatLng delegateLatLng = new LatLng(delLat, delLng);
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

    /*private void removePreviousPoints() {
        LatLng closestPoint = null;
        double smallestDistance = -1;
        for (LatLng point : routePoints) {
            double distance = SphericalUtil.computeDistanceBetween(currentPoint, point);
            if (smallestDistance == -1 || distance < smallestDistance) {
                closestPoint = point;
                smallestDistance = distance;
            }
        }
        getIntermmediatePoint(closestPoint);
    }

    private void addPoint(LatLng point, int closestIndex) {
        boolean pointExists = false;
        for (int i = 0; i < routePoints.size(); i++) {
            if (point.latitude == routePoints.get(i).latitude && point.longitude == routePoints.get(i).longitude) {
                pointExists = true;
            }
        }
        if (!pointExists) {
            Log.e("index", "index");
            if (closestIndex == -1) {
                Log.e("distance5", SphericalUtil.computeDistanceBetween(currentPoint, point) + "");
                routePoints.add(0, point);
            } else {
                Log.e("distance5", SphericalUtil.computeDistanceBetween(currentPoint, point) + "");
                routePoints.add(closestIndex, point);
            }

            for (int i = 0; i < routePoints.indexOf(point); i++) {
                Log.e("distance2", SphericalUtil.computeDistanceBetween(currentPoint, routePoints.get(i)) + "");
                routePoints.remove(routePoints.get(i));
            }
        }

    }

    private void getIntermmediatePoint(LatLng closestPoint) {
        LatLng intermmediate = SphericalUtil.interpolate(currentPoint, closestPoint, 0.5);
        double distance = SphericalUtil.computeDistanceBetween(currentPoint, intermmediate);
        Log.e("distance", distance + " ");
        if (distance > 20) {
            addPoint(intermmediate, routePoints.indexOf(closestPoint));
            getIntermmediatePoint(intermmediate);
        } else {
            addPoint(intermmediate, routePoints.indexOf(closestPoint));

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

    }*/

//    private void updateRoute() {
//        double firstToSecondDistance;
//        double delegateToFirstDistance;
//        double delegateToSecondDistance;
//        int removedIndex = -1;
//        for (int i = 1; i < routePoints.size(); i++) {
//            firstToSecondDistance = SphericalUtil.computeDistanceBetween(routePoints.get(i - 1), routePoints.get(i));
//            delegateToFirstDistance = SphericalUtil.computeDistanceBetween(routePoints.get(i - 1), currentPoint);
//            delegateToSecondDistance = SphericalUtil.computeDistanceBetween(currentPoint, routePoints.get(i));
//            if (delegateToFirstDistance < firstToSecondDistance && delegateToSecondDistance < firstToSecondDistance) {
//                removedIndex = routePoints.indexOf(routePoints.get(i - 1));
//                break;
//            }
//        }
//
//        if (removedIndex != -1) {
//            boolean isPointAdded = false;
//            newRoutePoints.clear();
//            newRoutePoints.add(0, currentPoint);
//            for (int i = removedIndex + 1; i < routePoints.size(); i++) {
//                newRoutePoints.add(routePoints.get(i));
//                isPointAdded = true;
//            }
//            if (isPointAdded) {
//                routePoints.clear();
//                routePoints.addAll(newRoutePoints);
//            }
//            Log.e("sizes", routePoints.size() + "   " + newRoutePoints.size());
//            if (polyline != null) {
//                polyline.getPoints().clear();
//                polyline.remove();
//
//            }
//            PolylineOptions lineOptions = new PolylineOptions();
//            lineOptions.addAll(routePoints);
//            lineOptions.width(5);
//            lineOptions.color(ContextCompat.getColor(this, R.color.app_color));
//            polyline = getGoogleMap().addPolyline(lineOptions);
//        } else {
//            if (polyline != null) {
//                polyline.getPoints().clear();
//                polyline.remove();
//
//            }
//            PolylineOptions lineOptions = new PolylineOptions();
//            lineOptions.addAll(routePoints);
//            lineOptions.width(5);
//            lineOptions.color(ContextCompat.getColor(this, R.color.hint_blue));
//            polyline = getGoogleMap().addPolyline(lineOptions);
//        }
//
//
//    }

    private void removeDelegateDistance() {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_START);
        fromIv.setLayoutParams(layoutParams);
        delIv.setVisibility(View.GONE);
        delegateDistanceRl.setVisibility(View.GONE);
    }

    private void changeCardsVisibility(){
        if(isCardsHided){
            hideCardsIv.setImageResource(R.drawable.open_map);
            orderBaseInfoRl.setVisibility(View.VISIBLE);
            showOrderDetails(order);
            if (rateDialogRl.getVisibility() == View.VISIBLE) {
                hideKeyboard();
                rateDialogRl.setVisibility(View.GONE);
            }
        }
        else {
            hideCardsIv.setImageResource(R.drawable.close_map);
            orderBaseInfoRl.setVisibility(View.GONE);
            waitingDriverRl.setVisibility(View.GONE);
            orderInvoiceRl.setVisibility(View.GONE);
            offersRl.setVisibility(View.GONE);
            reorderBtn.setVisibility(View.GONE);
            overlayV.setVisibility(View.GONE);
        }
        isCardsHided = !isCardsHided;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_offers:
                if (!offersEl.isExpanded()) {
                    toggleOffersDialog();
                }
                break;

            case R.id.tv_reapeat_search:
                getOrderDetailsPresenter().researchDelegates(getLocalSettings().getToken(), getLocalSettings().getLocale(), getOrderId());
                break;

            case R.id.btn_cancel:
                //cancelOrderPopup();
                getOrderDetailsPresenter().getCancelationReasons(getLocalSettings().getLocale(), 1);
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
                getOrderDetailsPresenter().getCancelationReasons(getLocalSettings().getLocale(), 1);
                break;

            case R.id.submit_complain:
                transparentView.setVisibility(View.GONE);
                menuLayout.setVisibility(View.GONE);
                switchToSubmitComplaint();
                break;

            case R.id.btn_reorder:
                openReorderPopup();
                break;

            case R.id.btn_reorder_from_invoice:
                openReorderPopup();
                break;

            case R.id.tv_reorder_yes:
                LatLng pointFromAddress = new LatLng(order.getFromLat(), order.getFromLng());
                LatLng pointToAddress = new LatLng(order.getToLat(), order.getToLng());

                if (!AreasUtility.checkLocationInBlockedArea(this, pointFromAddress) && !AreasUtility.checkLocationInBlockedArea(this, pointToAddress)) {
                    reorderPopupWindow.dismiss();
                    generateImagesIdsString();
                    getOrderDetailsPresenter().createOrder(getLocalSettings().getToken(), getLocalSettings().getLocale(), order.getDescription(),
                            order.getFromType(), order.getFromLat(), order.getFromLng(), order.getFrom_address(), order.getToLat(), order.getToLng(),
                            order.getTo_address(), order.getStore_name(), imagesIdsString, order.getDeliveryDuration());
                } else {
                    Toast.makeText(this, R.string.place_not_allow, Toast.LENGTH_LONG).show();

                }
                break;

            case R.id.tv_reorder_no:
                reorderPopupWindow.dismiss();
                break;

            case R.id.ll_reorder:
                reorderPopupWindow.dismiss();
                break;

            case R.id.rl_reorder_content:
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
                popupRateSubmitBtn.setAlpha((float) 0.5);
                popupRateSubmitBtn.setEnabled(false);
                getOrderDetailsPresenter().delegateRateOrder(getLocalSettings().getToken(), getLocalSettings().getLocale(), getOrderId(), rateNum,
                        popupRateDelegateEt.getText().toString(), false);
                break;

            case R.id.iv_rate_close:
                //ratePopupWindow.dismiss();
                hideKeyboard();
                rateDialogRl.setVisibility(View.GONE);
                break;

            case R.id.ll_rate:
                //ratePopupWindow.dismiss();
                hideKeyboard();
                rateDialogRl.setVisibility(View.GONE);
                break;

            case R.id.rl_rate_content:
                hideKeyboard();
                break;

            case R.id.tv_customer_add_rate:
                openRatePopup();
                setRatePopupData();
                break;

            case R.id.tv_invoice_cust_del_name:
                switchToCustOrDelProfile(false, orderDelegateId);
                break;

            case R.id.iv_invoice_cust_del_image:
                switchToCustOrDelProfile(false, orderDelegateId);
                break;

            case R.id.tv_call_delegate:
                openDialer(order.getDelegate().getMobile());
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
        if (timer != null) {
            //timer.purge();
            timer.cancel();
            timer = null;
        }
        if (transparentView.getVisibility() == View.VISIBLE) {
            transparentView.setVisibility(View.GONE);
            menuLayout.setVisibility(View.GONE);
        } else if (rateDialogRl.getVisibility() == View.VISIBLE) {
            hideKeyboard();
            rateDialogRl.setVisibility(View.GONE);
        } else if (reorderPopupWindow.isShowing()) {
            reorderPopupWindow.dismiss();
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public void onAcceptOfferClick(final Offer offer) {
        final Fonts fonts = MApplication.getInstance().getFonts();
        /*final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("")
                .setMessage(this.getResources().getString(R.string.accept_offer_message) + " " +
                        (String.format("%.2f", (offer.getShippingCost() + offer.getVat())) + " " + getString(R.string.sar) + getString(R.string.question_mark)))
                .setPositiveButton(this.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).setNegativeButton(this.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getOrderDetailsPresenter().acceptOffer(getLocalSettings().getToken(), getLocalSettings().getLocale(), offer.getId());
                        dialogInterface.cancel();
                    }
                }).create();*/

        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("")
                .setMessage(this.getResources().getString(R.string.accept_offer_message) + " " +
                        (String.format(Locale.ENGLISH,"%.2f", (offer.getShippingCost())) + " " + getString(R.string.sar) + getString(R.string.question_mark)))
                .setPositiveButton(this.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).setNegativeButton(this.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getOrderDetailsPresenter().acceptOffer(getLocalSettings().getToken(), getLocalSettings().getLocale(), offer.getId());
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

    @Override
    public void onRejectOfferClick(final Offer offer) {
        final Fonts fonts = MApplication.getInstance().getFonts();
        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("")
                .setMessage(this.getResources().getString(R.string.reject_offer_message))
                .setPositiveButton(this.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).setNegativeButton(this.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getOrderDetailsPresenter().rejectOffer(getLocalSettings().getToken(), getLocalSettings().getLocale(), offer.getId());
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

    @Override
    public void onDelegateClick(int delegateId) {
        switchToCustOrDelProfile(false, delegateId);
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
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        popupRateSubmitBtn.setAlpha(1);
        popupRateSubmitBtn.setEnabled(true);

    }

    @Override
    public void showOrderDetails(Order order) {
        this.order = order;
        super.showOrderDetails(order);

        fromIv.setImageResource(R.drawable.shop_ic);
        if (order.getStatus().equals("new")) {
            removeDelegateDistance();
            if (order.getDelegateSearchResult() != null && !order.getDelegateSearchResult()) {
                waitingDriverRl.setVisibility(View.VISIBLE);
                waitingTimeTv.setVisibility(View.GONE);
                overlayV.setVisibility(View.VISIBLE);
                waitingDriversTv.setVisibility(View.GONE);
                waitingTimeTv.setVisibility(View.GONE);
                noOffersTv.setVisibility(View.GONE);
                noDelegatesTv.setVisibility(View.VISIBLE);
                noDelegatesTv.setText(R.string.no_available_drivers);

                searchAgainTv.setVisibility(View.VISIBLE);
                //iv_more.setVisibility(View.VISIBLE);
            } else {
                waitingDriverRl.setVisibility(View.VISIBLE);
                overlayV.setVisibility(View.VISIBLE);
                waitingDriversTv.setVisibility(View.VISIBLE);
                waitingTimeTv.setVisibility(View.VISIBLE);
                noOffersTv.setVisibility(View.GONE);
                noDelegatesTv.setVisibility(View.GONE);
                searchAgainTv.setVisibility(View.GONE);
                //iv_more.setVisibility(View.VISIBLE);
                if ((5 * 60 * 1000) - ((new Date()).getTime() - getOrderDate().getTime()) >= 1000) {
                    startTimer();
                } else {
                    waitingDriversTv.setVisibility(View.GONE);
                    waitingTimeTv.setVisibility(View.GONE);
                    noOffersTv.setVisibility(View.INVISIBLE);
                    noDelegatesTv.setVisibility(View.GONE);
                    searchAgainTv.setVisibility(View.VISIBLE);
                }
            }
            iv_more.setVisibility(View.VISIBLE);

        }
        else if (order.getStatus().equals("pending")) {
            removeDelegateDistance();
            if (order.getOffers() != null && !order.getOffers().isEmpty()) {
                Log.e("offer id", order.getOffers().get(0).getId()+" ");
                Log.e("offers size", order.getOffers().size()+" ");
                if (timer != null) {
                    //timer.purge();
                    timer.cancel();
                    timer = null;
                }
                ViewGroup.LayoutParams params = offersEl.getLayoutParams();
                if (order.getOffers().size() == 1) {
                    params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 145, getResources().getDisplayMetrics());
                    offersEl.setLayoutParams(params);
                } else {
                    params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 220, getResources().getDisplayMetrics());
                    offersEl.setLayoutParams(params);
                }
                offersList.clear();
                offersList.addAll(order.getOffers());
                offersAdapter.setDeliveryDuration(order.getDeliveryDuration());
                offersAdapter.notifyDataSetChanged();
                offersRl.setVisibility(View.VISIBLE);
                offersTv.setVisibility(View.VISIBLE);
                offersLoaderLv.setVisibility(View.VISIBLE);
                offersEl.expand(true);
            } else {
                offersList.clear();
                offersAdapter.setDeliveryDuration(order.getDeliveryDuration());
                offersAdapter.notifyDataSetChanged();
                offersEl.collapse(false);
                offersTv.setVisibility(View.GONE);
                offersLoaderLv.setVisibility(View.GONE);
                /*waitingDriverRl.setVisibility(View.VISIBLE);
                waitingDriversTv.setVisibility(View.VISIBLE);
                waitingTimeTv.setVisibility(View.VISIBLE);
                noOffersTv.setVisibility(View.GONE);
                noDelegatesTv.setVisibility(View.GONE);
                searchAgainTv.setVisibility(View.GONE);
                //iv_more.setVisibility(View.VISIBLE);
                Log.e("time",((5 * 60 * 1000) - ((new Date()).getTime() - getOrderDate().getTime()))+"");
                if ((5 * 60 * 1000) - ((new Date()).getTime() - getOrderDate().getTime()) >= 1000) {
                    startTimer();
                } else {
                    waitingDriversTv.setVisibility(View.GONE);
                    waitingTimeTv.setVisibility(View.GONE);
                    noOffersTv.setVisibility(View.VISIBLE);
                    noDelegatesTv.setVisibility(View.GONE);
                    searchAgainTv.setVisibility(View.VISIBLE);
                }*/

                //
                if (order.getDelegateSearchResult() != null && !order.getDelegateSearchResult()) {
                    waitingDriverRl.setVisibility(View.VISIBLE);
                    waitingDriversTv.setVisibility(View.GONE);
                    overlayV.setVisibility(View.VISIBLE);
                    waitingTimeTv.setVisibility(View.GONE);
                    noOffersTv.setVisibility(View.GONE);
                    noDelegatesTv.setVisibility(View.VISIBLE);
                    noDelegatesTv.setText(R.string.no_more_delegates);
                    searchAgainTv.setVisibility(View.VISIBLE);
                    //iv_more.setVisibility(View.VISIBLE);
                } else {
                    waitingDriverRl.setVisibility(View.VISIBLE);
                    waitingDriversTv.setVisibility(View.VISIBLE);
                    overlayV.setVisibility(View.VISIBLE);
                    waitingTimeTv.setVisibility(View.VISIBLE);
                    noOffersTv.setVisibility(View.GONE);
                    noDelegatesTv.setVisibility(View.GONE);
                    searchAgainTv.setVisibility(View.GONE);
                    //iv_more.setVisibility(View.VISIBLE);
                    if ((5 * 60 * 1000) - ((new Date()).getTime() - getOrderDate().getTime()) >= 1000) {
                        startTimer();
                    } else {
                        waitingDriversTv.setVisibility(View.GONE);
                        waitingTimeTv.setVisibility(View.GONE);
                        noOffersTv.setVisibility(View.INVISIBLE);
                        noDelegatesTv.setVisibility(View.GONE);
                        searchAgainTv.setVisibility(View.VISIBLE);
                    }
                }
                //

            }

            iv_more.setVisibility(View.VISIBLE);
        }
        else if (order.getStatus().equals("assigned") || order.getStatus().equals("in_progress") || order.getStatus().equals("delivery_in_progress")
                || order.getStatus().equals("delivered")) {
            setInvoiceData();
            if (order.getStatus().equals("delivered")) {

                removeDelegateDistance();
                reorderBtnFromInvoice.setVisibility(View.VISIBLE);
                reorderBtn.setVisibility(View.GONE);
                callDelegateTv.setVisibility(View.GONE);
                if (carMarker != null) {
                    Log.e("carMarker","carMarker removed2");
                    carMarker.remove();
                }


            }
            else if (order.getStatus().equals("assigned") || order.getStatus().equals("in_progress")) {
                delIv.setVisibility(View.VISIBLE);
                delegateDistanceRl.setVisibility(View.VISIBLE);
                callDelegateTv.setVisibility(View.VISIBLE);
                //if (drawDelToPickRoute) {
                    getOrderDetailsPresenter().getOrderDelegateLocation(order.getDelegate().getId());
                //}
            } else {
                callDelegateTv.setVisibility(View.VISIBLE);
                removeDelegateDistance();
                fromIv.setImageResource(R.drawable.car_ic);
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
                    Log.e("carMarker","carMarker removed3");
                    carMarker.remove();
                }
                //getOrderDetailsPresenter().getOrderDelegateLocation(order.getDelegate().getId());
                /*if (getPickupToDestLine() != null){
                    getPickupToDestLine().getPoints().clear();
                    getPickupToDestLine().remove();
                }*/
            }
            //added by omnia
            iv_more.setVisibility(View.VISIBLE);
            cancelTV.setVisibility(View.GONE);
            submitComplainTV.setVisibility(View.VISIBLE);
            menuSeparatorV.setVisibility(View.GONE);

        }
        else if (order.getStatus().equals("cancelled")) {
            callDelegateTv.setVisibility(View.GONE);
            removeDelegateDistance();
            if (order.getShippingCost() != null) {
                setInvoiceData();
                reorderBtnFromInvoice.setVisibility(View.VISIBLE);
                reorderBtn.setVisibility(View.GONE);
            } else {
                orderInvoiceRl.setVisibility(View.GONE);
                reorderBtnFromInvoice.setVisibility(View.GONE);
                reorderBtn.setVisibility(View.VISIBLE);
            }
            //added by omnia
            iv_more.setVisibility(View.VISIBLE);
            cancelTV.setVisibility(View.GONE);
            submitComplainTV.setVisibility(View.VISIBLE);
            menuSeparatorV.setVisibility(View.GONE);

            if (carMarker != null) {
                Log.e("carMarker","carMarker removed3");
                carMarker.remove();
            }
        }

        if (order.getStatus().equals("delivered") && !order.isRated()) {
            addRateTv.setVisibility(View.VISIBLE);
            delegateRateRb.setVisibility(View.GONE);
            openRatePopup();
            setRatePopupData();
        } else {
            addRateTv.setVisibility(View.GONE);
            delegateRateRb.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void sucessCancel() {

    }

    @Override
    public void showSuccessAcceptOffer(Order order) {
        this.order = order;
        Intent orderChatIntent = new Intent(this, OrderChatActivity.class);
        orderChatIntent.putExtra(Constants.ORDER, order);
        orderChatIntent.putExtra(Constants.FROM_CUSTOMER_ORDERS, true);
        startActivity(orderChatIntent);
        finish();
    }

    @Override
    public void showSuccessRejectOffer(Order order) {
        offersList.clear();
        this.order = order;
        showOrderDetails(order);
    }

    @Override
    public void showAcceptOfferError(final String errorMessage) {
//        new CountDownTimer(3500, 1000) {
//            @Override
//            public void onTick(long l) {
//                Log.e("tick", "tick");
//                Toast acceptOfferErrorToast = Toast.makeText(CustomerOrderDetailsActivity.this, errorMessage, Toast.LENGTH_LONG);
//                acceptOfferErrorToast.show();
//            }
//
//            @Override
//            public void onFinish() {
//
//            }
//        }.start();
        Toast.makeText(CustomerOrderDetailsActivity.this, errorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showOrderCancelledSucessfully() {

        if (waitingDriverRl.getVisibility() == View.VISIBLE) {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
            waitingDriverRl.setVisibility(View.GONE);
            overlayV.setVisibility(View.GONE);
        }
        if (offersTv.getVisibility() == View.VISIBLE) {
            offersTv.setVisibility(View.GONE);
            offersLoaderLv.setVisibility(View.GONE);
            offersList.clear();
            offersAdapter.setDeliveryDuration(order.getDeliveryDuration());
            offersAdapter.notifyDataSetChanged();
            offersEl.collapse(false);
        }
        getOrderDetailsPresenter().getCustomerOrderDetails(getLocalSettings().getToken(), getLocalSettings().getLocale(), getOrderId());
    }

    @Override
    public void showSuccessOrderRate(Order order) {
        this.order = order;
        addRateTv.setVisibility(View.GONE);
        delegateRateRb.setVisibility(View.VISIBLE);
        popupRateSubmitBtn.setEnabled(true);

        //ratePopupWindow.dismiss();
        hideKeyboard();
        rateDialogRl.setVisibility(View.GONE);
    }

    private void cancelOrderPopup() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialog);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_cancel_order, null);
        builder.setView(dialogView);
        TextView cancel_message = (TextView) dialogView.findViewById(R.id.cancel_message);

        Spinner cancelationReasonsSp = dialogView.findViewById(R.id.sp_cancelation_reasons);
        //CancelationReasonsAdapter adpter = new CancelationReasonsAdapter(this, R.layout.item_drop_down, cancelationReasons);
        CancelationReasonsAdapter adpter = new CancelationReasonsAdapter(this, cancelationReasons);
        //adpter.setDropDownViewResource(R.layout.item_drop_down);
        cancelationReasonsSp.setAdapter(adpter);
        cancelationReasonsSp.setOnItemSelectedListener(this);

        cancel_message.setText(getResources().getString(R.string.cancel_order_msg));
        TextView submit = dialogView.findViewById(R.id.yes);
        TextView no = dialogView.findViewById(R.id.no);
        error_reason = dialogView.findViewById(R.id.error_reason);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id_order = getOrderId() + "";
                if (selectedReason == 0) {
                    error_reason.setVisibility(View.VISIBLE);
                } else {
                    error_reason.setVisibility(View.GONE);
                    getOrderDetailsPresenter().cancelOrder(getLocalSettings().getToken(), getLocalSettings().getLocale(), id_order, false, selectedReason);
                    cancel_dialoug.dismiss();
                }
            }
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
        error_reason.setTypeface(fonts.customFont());
        cancel_dialoug = builder.show();

    }

    @Override
    public void showSuccessReorder(Order order) {
        this.order = order;
        Log.e("orderreorder", order.getId() + "   ");
        setOrderId(order.getId());
        showOrderDetails(order);
        orderInvoiceRl.setVisibility(View.GONE);
        reorderBtnFromInvoice.setVisibility(View.GONE);
        reorderBtn.setVisibility(View.GONE);
        menuSeparatorV.setVisibility(View.VISIBLE);
        cancelTV.setVisibility(View.VISIBLE);

    }

    @Override
    public void showDelegateCurrentLocation(double latitude, double longitude) {
        Log.e("del1", latitude + "   " + longitude);
        if (order.getStatus().equals("assigned") || order.getStatus().equals("in_progress")) {
            this.delLat = latitude;
            this.delLng = longitude;
            if (carMarker == null) {
                Log.e("carMarker","carMarker added2");
                carMarker = getGoogleMap().addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).icon(BitmapDescriptorFactory.fromResource(R.drawable.car_purple)));
                currentPoint = new LatLng(latitude, longitude);
            } else {
                updateDelegateLocation();
                currentPoint = new LatLng(latitude, longitude);
            }
            //if (routePoints != null) {
//                if (!PolyUtil.isLocationOnPath(new LatLng(latitude, longitude), routePoints, false, 20)) {
//                    drawDelToPickRoute = true;
//                } else {

//                if (PolyUtil.isLocationOnPath(new LatLng(latitude, longitude), routePoints, false, 20)) {
//                    updateRoute();
//                }
//                }

            //}
//            if (drawDelToPickRoute) {
//                getOrderDetailsPresenter().getDirections(latitude + "," + longitude, order.getFromLat() + "," + order.getFromLng(),
//                        "driving", getString(R.string.google_server_key), true);
//                drawDelToPickRoute = false;
//            }
            if(delegateToFromDistance == 0 || isStatusChanged()){
                if(isStatusChanged()){
                    setStatusChanged(false);
                }
                getOrderDetailsPresenter().getDirections(latitude + "," + longitude, order.getFromLat() + "," + order.getFromLng(),
                        "driving", getString(R.string.google_server_key), true);
            }
        }
        else if (order.getStatus().equals("delivery_in_progress")) {
            super.showDelegateCurrentLocation(latitude, longitude);
        }
    }

    @Override
    public void showOrderDirections(Route route, boolean isDelegateDirection) {
        if (!isDelegateDirection) {
            super.showOrderDirections(route, isDelegateDirection);
        } else {
//            if (route.getRoutePolyLine() != null) {
//                setRoute(order.getFromLat(), order.getFromLng(), order.getToLat(), order.getToLng(), route);
//            }
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

    @Override
    public void showBlockedAreaError() {
        Toast.makeText(this, R.string.blocked_area_error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showCancelationReasons(ArrayList<CancelationReason> cancelationReasons) {
        this.cancelationReasons.clear();
        this.cancelationReasons.add(new CancelationReason(0, getResources().getString(R.string.choose_reason)));
        this.cancelationReasons.addAll(cancelationReasons);
        cancelOrderPopup();
    }

}
