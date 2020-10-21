package amaz.objects.TwentyfourSeven.ui.OrderChat;

import android.Manifest;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonObject;
import com.google.maps.android.SphericalUtil;
import com.rey.material.widget.ProgressView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Properties;

import amaz.objects.TwentyfourSeven.BaseActivity;
import amaz.objects.TwentyfourSeven.MApplication;
import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.adapters.ChatAdapter;
import amaz.objects.TwentyfourSeven.adapters.OrderImagesAdapter;
import amaz.objects.TwentyfourSeven.api.APIURLs;
import amaz.objects.TwentyfourSeven.data.models.DirectionLeg;
import amaz.objects.TwentyfourSeven.data.models.ImageChat;
import amaz.objects.TwentyfourSeven.data.models.ImageSizes;
import amaz.objects.TwentyfourSeven.data.models.Message;
import amaz.objects.TwentyfourSeven.data.models.MessageChat;
import amaz.objects.TwentyfourSeven.data.models.Order;
import amaz.objects.TwentyfourSeven.data.models.Route;
import amaz.objects.TwentyfourSeven.data.models.responses.UploadDelegateImagesResponse;
import amaz.objects.TwentyfourSeven.injection.Injection;
import amaz.objects.TwentyfourSeven.listeners.OnImageClickListener;
import amaz.objects.TwentyfourSeven.listeners.OnLocationChangeListener;
import amaz.objects.TwentyfourSeven.listeners.OnRefeshTokenResponse;
import amaz.objects.TwentyfourSeven.listeners.OnRequestImageIntentListener;
import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import amaz.objects.TwentyfourSeven.presenter.PresenterFactory;
import amaz.objects.TwentyfourSeven.ui.CustOrDelProfile.CustOrDelProfileActivity;
import amaz.objects.TwentyfourSeven.ui.MyAccount.MainActivity;
import amaz.objects.TwentyfourSeven.ui.Notification.NotificationFragment;
import amaz.objects.TwentyfourSeven.ui.OrderDetails.CustomerOrderDetails.CustomerOrderDetailsActivity;
import amaz.objects.TwentyfourSeven.ui.OrderDetails.DelegateOrderDetails.DelegateOrderDetailsActivity;
import amaz.objects.TwentyfourSeven.ui.RegisterOrLogin.RegisterOrLoginActivity;
import amaz.objects.TwentyfourSeven.ui.SubmitComplaint.SubmitComplaintActivity;
import amaz.objects.TwentyfourSeven.utilities.AddImageUtilities;
import amaz.objects.TwentyfourSeven.utilities.Constants;
import amaz.objects.TwentyfourSeven.utilities.DelegateTracker;
import amaz.objects.TwentyfourSeven.utilities.Fonts;
import amaz.objects.TwentyfourSeven.utilities.LanguageUtilities;
import amaz.objects.TwentyfourSeven.utilities.LocalSettings;
import amaz.objects.TwentyfourSeven.utilities.TokenUtilities;

public class OrderChatActivity extends BaseActivity implements View.OnClickListener, OnRequestImageIntentListener,
        OrderChatPresenter.OrderChatView, OnRefeshTokenResponse, OnImageClickListener {
    private LinearLayout mainContentLl;
    private ImageView backIv, user_imageIV, send_photo;
    private RecyclerView messages_list;
    private ChatAdapter chatAdapter;
    private ArrayList<Message> messages = new ArrayList<>();
    private TextView tv_track_order, tv_call, send_btn;
    private FrameLayout startRideFl, itemPickedFl, orderDeliveredFl;
    private RelativeLayout delegateDistanceRl;
    private ImageView delegateIv, fromIv;
    private Button startRideBtn, itemPickedBtn, orderDeliveredBtn;
    private ProgressView loaderPv;
    private PopupWindow previewPopupWindow;
    private ImageView popupCloseIv;
    private ImageView popupPreviewIv;
    private File imageFile;
    private PopupWindow pickItemPopupWindow;
    private LinearLayout pickItemLl, pickItemContentLl;
    private TextView popupPickItemUpdateCostTv, popupPickItemPriceTv, popupPickItemCurrencyTv, popupPickItemShippingTitleTv,
            popupPickItemShippingValueTv, popupPickItemVatTitleTv, popupPickItemVatValueTv, popupPickItemTotalTitleTv, popupPickItemTotalValueTv,
            popupPickItemCancelTv, popupPickItemSubmitTv, tv_userName, order_titleTV, delToFromDistanceTv, fromToToDistanceTv;
    private EditText popupPickItemPriceEt, message_et;
    private ProgressView popupPickItemLoaderPv;

    private PopupWindow deliverOrderPopupWindow;
    private LinearLayout deliverLl, deliverContentLl, popupDeliverItemPriceLl, popupDeliverDiscountLl;
    private TextView popupDeliverTitleTv, popupDeliverMessageTv, popupDeliverItemPriceTitleTv, popupDeliverItemPriceValueTv,
            popupDeliverShippingTitleTv, popupDeliverShippingValueTv, popupDeliverVatTitleTv, popupDeliverVatValueTv,
            popupDeliverDiscountTitleTv, popupDeliverDiscountValueTv,
            popupDeliverTotalTitleTv, popupDeliverTotalValueTv, popupDeliverNoTv, popupDeliverYesTv;
    private View popupDeliverItemPriceView, popupDeliverDiscountView;
    private ProgressView popupDeliverLoaderPv, pv_load_popup;

    private Order order = new Order();
    private boolean fromCustomerOrders;
    private boolean fromPushNotification;
    private LocalSettings localSettings;
    private Fonts fonts;
    private OrderChatPresenter orderChatPresenter;

    private double total, itemPrice;
    private BroadcastReceiver mBroadcastReceiver;
    private boolean firstConnect = false;
    private RatingBar ratingBar;
    private boolean orderSwitching = false;
    private ImageView gallaryImg, cameraIc;

    private double delLat, delLng;
    //private DelegateTracker tracker;

    private boolean isActivityVisible;
    private boolean isTrackOrderClicked = false;

    @Override
    public void onPresenterAvailable(BasePresenter presenter) {
        orderChatPresenter = (OrderChatPresenter) presenter;
        orderChatPresenter.setView(this, this);
        String order_id = order.getId() + "";
        orderChatPresenter.getMessages(order_id, fromCustomerOrders);

        if (!order.getStatus().equals("delivery_in_progress")) {
            orderChatPresenter.getDirections(order.getFromLat() + "," + order.getFromLng(), order.getToLat() + "," + order.getToLng(),
                    "driving", getString(R.string.google_server_key), false);
        }

        if (delLat == 0 && delLng == 0){
            if (order.getDelegate() != null){
                orderChatPresenter.getOrderDelegateLocation(order.getDelegate().getId());
            }
            else {
                orderChatPresenter.getOrderDelegateLocation(localSettings.getUser().getId());
            }
        }

    }

    @Override
    public PresenterFactory.PresenterType getPresenterType() {
        return PresenterFactory.PresenterType.OrderChat;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localSettings = new LocalSettings(this);
        setLanguage();
        setContentView(R.layout.activity_order_chat);
        getDataFromIntent();
        initViews();
        initializePickItemPopup();
        initializeDeliverPopup();
        setFonts();
        setBroadCast();
        initializePreviewPopup();

    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver,
                new IntentFilter(Constants.BROADCASTRECEVIERGENERATION));
        firstConnect = true;
        if (orderSwitching) {
            if (!localSettings.isOrderDetailsOpened()){
                switchToOrderDetails();
            }
            finish();
        }
        isActivityVisible = true;

    }

    @Override
    protected void onPause() {
        super.onPause();
        //  LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
        isActivityVisible = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isTrackOrderClicked = false;
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
                        Log.e("link_to_chat", link_to);
                        if (link_to.equals("order_details")) {
                            String orderNotificationData = intent.getStringExtra("order_data");
                            if (orderNotificationData != null) {
                                try {
                                    JSONObject orderNotificationJson = new JSONObject(orderNotificationData);
                                    if (orderNotificationJson.getInt("id") == order.getId()) {
                                        if (orderNotificationJson.get("status").equals("delivered") || orderNotificationJson.get("status").equals("cancelled")) {
                                            //if (MApplication.isActivityVisible()) {
                                            if (isActivityVisible) {
                                                switchToOrderDetails();
                                                finish();
                                            } else {
                                                orderSwitching = true;
                                            }
                                        }
                                        else if(orderNotificationJson.get("status").equals("delivery_in_progress")){
                                            hideDelegateDistance();
                                            order.setStatus(orderNotificationJson.get("status").toString());
                                            if (order.getDelegate() != null){
                                                orderChatPresenter.getOrderDelegateLocation(order.getDelegate().getId());
                                            }
                                            else {
                                                orderChatPresenter.getOrderDelegateLocation(localSettings.getUser().getId());
                                            }
                                        }
                                        else {
                                            order.setStatus(orderNotificationJson.get("status").toString());
                                            if (order.getDelegate() != null){
                                                orderChatPresenter.getOrderDelegateLocation(order.getDelegate().getId());
                                            }
                                            else {
                                                orderChatPresenter.getOrderDelegateLocation(localSettings.getUser().getId());
                                            }
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        else if (link_to.equals("delegate_order_details")){
                            String orderNotificationData = intent.getStringExtra("order_data");
                            if (orderNotificationData != null) {
                                try {
                                    JSONObject orderNotificationJson = new JSONObject(orderNotificationData);
                                    if (orderNotificationJson.getInt("id") == order.getId()) {
                                        if (orderNotificationJson.get("status").equals("delivered") || orderNotificationJson.get("status").equals("cancelled")) {
                                            //if (MApplication.isActivityVisible()) {
                                            if (isActivityVisible) {
                                                switchToOrderDetails();
                                                finish();
                                            } else {
                                                orderSwitching = true;
                                            }
                                        }
                                        else if(orderNotificationJson.get("status").equals("in_progress")){
                                            startRideFl.setVisibility(View.GONE);
                                            itemPickedFl.setVisibility(View.VISIBLE);
                                            orderDeliveredFl.setVisibility(View.GONE);
                                        }
                                        else if(orderNotificationJson.get("status").equals("delivery_in_progress")){
                                            startRideFl.setVisibility(View.GONE);
                                            itemPickedFl.setVisibility(View.GONE);
                                            orderDeliveredFl.setVisibility(View.VISIBLE);
                                            hideDelegateDistance();
                                            order.setStatus(orderNotificationJson.get("status").toString());
                                            if (order.getDelegate() != null){
                                                orderChatPresenter.getOrderDelegateLocation(order.getDelegate().getId());
                                            }
                                            else {
                                                orderChatPresenter.getOrderDelegateLocation(localSettings.getUser().getId());
                                            }
                                        }
                                        /*else {
                                            order.setStatus(orderNotificationJson.get("status").toString());
                                            if (order.getDelegate() != null){
                                                orderChatPresenter.getOrderDelegateLocation(order.getDelegate().getId());
                                            }
                                            else {
                                                orderChatPresenter.getOrderDelegateLocation(localSettings.getUser().getId());
                                            }
                                        }*/
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

    private void setLanguage() {
        if (localSettings.getLocale().equals(Constants.ARABIC)) {
            LanguageUtilities.switchToArabicLocale(this);
        }
    }

    private void getDataFromIntent() {
        order = (Order) getIntent().getSerializableExtra(Constants.ORDER);
        Log.e("order", order + "   omnia");
        fromCustomerOrders = getIntent().getBooleanExtra(Constants.FROM_CUSTOMER_ORDERS, fromCustomerOrders);
        fromPushNotification = getIntent().getBooleanExtra(Constants.FROM_PUSH_NOTIFICATION, fromPushNotification);
    }

    private void initViews() {
        mainContentLl = findViewById(R.id.ll_main_content);
        mainContentLl.setOnClickListener(this);
        messages_list = findViewById(R.id.conversation_list);
        messages_list.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                hideKeyboard();
                return false;
            }
        });
        backIv = findViewById(R.id.iv_back);
        backIv.setOnClickListener(this);
        message_et = findViewById(R.id.message_et);
        send_btn = findViewById(R.id.send_btn);
        tv_userName = findViewById(R.id.tv_userName);
        tv_userName.setOnClickListener(this);
        ratingBar = findViewById(R.id.ratingBar);
        user_imageIV = findViewById(R.id.iv_user_image);
        user_imageIV.setOnClickListener(this);
        order_titleTV = findViewById(R.id.tv_order_title);
        tv_track_order = findViewById(R.id.tv_track_order);
        tv_call = findViewById(R.id.tv_call);
        tv_call.setOnClickListener(this);
        if (order.getStatus().equals("assigned") || order.getStatus().equals("in_progress") || order.getStatus().equals("delivery_in_progress")) {
            tv_call.setVisibility(View.VISIBLE);
        }
        else {
            tv_call.setVisibility(View.GONE);
        }
        delegateDistanceRl = findViewById(R.id.rl_delegate_distance);
        delegateIv = findViewById(R.id.iv_delegate);
        delToFromDistanceTv = findViewById(R.id.tv_distance_del_from);
        fromIv = findViewById(R.id.iv_from_image);
        fromToToDistanceTv = findViewById(R.id.tv_distance_from_to);
        cameraIc = findViewById(R.id.camera_ic);
        gallaryImg = findViewById(R.id.gallary_img);
        if (localSettings.getLocale().equals(Constants.ARABIC)) {
            backIv.setImageResource(R.drawable.back_ar_ic);
        } else {
            backIv.setImageResource(R.drawable.back_ic);
        }
        tv_track_order.setOnClickListener(this);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        messages_list.setLayoutManager(linearLayoutManager);
        chatAdapter = new ChatAdapter(this, messages, order, fromCustomerOrders, this);
        messages_list.setAdapter(chatAdapter);
        startRideFl = findViewById(R.id.fl_start_ride);
        startRideBtn = findViewById(R.id.btn_start_ride);
        startRideBtn.setOnClickListener(this);
        /*if (!fromCustomerOrders) {
            if (order.getStatus().equals("assigned")) {
                startRideFl.setVisibility(View.VISIBLE);
            } else {
                startRideFl.setVisibility(View.GONE);
            }
        } else {
            startRideFl.setVisibility(View.GONE);
        }*/
        itemPickedFl = findViewById(R.id.fl_item_picked);
        itemPickedBtn = findViewById(R.id.btn_item_picked);
        itemPickedBtn.setOnClickListener(this);
        /*if (!fromCustomerOrders) {
            if (order.getStatus().equals("in_progress")) {
                itemPickedFl.setVisibility(View.VISIBLE);
            } else {
                itemPickedFl.setVisibility(View.GONE);
            }
        } else {
            itemPickedFl.setVisibility(View.GONE);
        }*/

        orderDeliveredFl = findViewById(R.id.fl_order_delivered);
        orderDeliveredBtn = findViewById(R.id.btn_order_delivered);
        /*if (!fromCustomerOrders) {
            if (order.getStatus().equals("delivery_in_progress")) {
                orderDeliveredFl.setVisibility(View.VISIBLE);
            } else {
                orderDeliveredFl.setVisibility(View.GONE);
            }
        } else {
            orderDeliveredFl.setVisibility(View.GONE);
        }*/
        if (!fromCustomerOrders) {
            //tracker = new DelegateTracker(this, this);
            //tracker.getLocation();
            if (order.getStatus().equals("assigned")) {
                startRideFl.setVisibility(View.VISIBLE);
                itemPickedFl.setVisibility(View.GONE);
                orderDeliveredFl.setVisibility(View.GONE);
            } else if (order.getStatus().equals("in_progress")) {
                startRideFl.setVisibility(View.GONE);
                itemPickedFl.setVisibility(View.VISIBLE);
                orderDeliveredFl.setVisibility(View.GONE);
            } else if (order.getStatus().equals("delivery_in_progress")) {
                startRideFl.setVisibility(View.GONE);
                itemPickedFl.setVisibility(View.GONE);
                orderDeliveredFl.setVisibility(View.VISIBLE);
            }
        } else {
            startRideFl.setVisibility(View.GONE);
            itemPickedFl.setVisibility(View.GONE);
            orderDeliveredFl.setVisibility(View.GONE);
        }
        if (order.getStatus().equals("delivery_in_progress")){
            hideDelegateDistance();
        }

        loaderPv = findViewById(R.id.pv_load);
        orderDeliveredBtn.setOnClickListener(this);

        cameraIc.setOnClickListener(this);
        gallaryImg.setOnClickListener(this);

        send_btn.setOnClickListener(this);
        send_btn.setEnabled(false);
        addSendTouchListenner();
        setOrderDetails();
    }

    private void addSendTouchListenner() {

        message_et.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().trim().length() == 0) {
                    send_btn.setEnabled(false);
                    send_btn.setTextColor(getResources().getColor(R.color.line_gray));
                } else {
                    send_btn.setEnabled(true);
                    send_btn.setTextColor(getResources().getColor(R.color.app_color));

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

    private void setOrderDetails() {
        order_titleTV.setText(getResources().getString(R.string.order_chat) + order.getId());
        if (!fromCustomerOrders) {
            if (order.getCreatedBy() != null) {
                tv_userName.setText(order.getCreatedBy().getName());
                if (order.getCreatedBy().getRating() == 0){
                    ratingBar.setRating(5);
                }
                else {
                    ratingBar.setRating(order.getCreatedBy().getRating());
                }

                if (order.getCreatedBy().getImage() != null) {
                    Picasso.with(this).load(order.getCreatedBy().getImage().getMedium()).placeholder(R.drawable.avatar)
                            .error(R.drawable.avatar).into(user_imageIV);
                }
            }

        } else {
            if (order.getDelegate() != null) {
                tv_userName.setText(order.getDelegate().getName());
                if (order.getDelegate().getDelegateRating() == 0){
                    ratingBar.setRating(5);
                }
                else {
                    ratingBar.setRating(order.getDelegate().getDelegateRating());
                }
                if (order.getDelegate().getImage() != null) {
                    Picasso.with(this).load(order.getDelegate().getImage().getMedium()).placeholder(R.drawable.avatar)
                            .error(R.drawable.avatar).into(user_imageIV);
                }

            }
        }
    }

    private void initializePickItemPopup() {
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_pick_item, null, false);
        pickItemPopupWindow = new PopupWindow(v, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        pickItemLl = v.findViewById(R.id.ll_pick_item);
        pickItemLl.setOnClickListener(this);
        pickItemContentLl = v.findViewById(R.id.ll_popup_content);
        pickItemContentLl.setOnClickListener(this);
        popupPickItemUpdateCostTv = v.findViewById(R.id.tv_pick_item_update);
        popupPickItemPriceTv = v.findViewById(R.id.tv_pick_item_price);
        popupPickItemPriceEt = v.findViewById(R.id.et_pick_item_price);
        popupPickItemCurrencyTv = v.findViewById(R.id.tv_pick_item_currency);
        popupPickItemShippingTitleTv = v.findViewById(R.id.tv_pick_item_shipping_title);
        popupPickItemShippingValueTv = v.findViewById(R.id.tv_pick_item_shipping_value);
        popupPickItemVatTitleTv = v.findViewById(R.id.tv_pick_item_vat_title);
        popupPickItemVatValueTv = v.findViewById(R.id.tv_pick_item_vat_value);
        popupPickItemTotalTitleTv = v.findViewById(R.id.tv_pick_item_total_title);
        popupPickItemTotalValueTv = v.findViewById(R.id.tv_pick_item_total_value);
        popupPickItemCancelTv = v.findViewById(R.id.tv_pick_item_cancel);
        popupPickItemCancelTv.setOnClickListener(this);
        popupPickItemSubmitTv = v.findViewById(R.id.tv_pick_item_submit);
        popupPickItemSubmitTv.setOnClickListener(this);
        popupPickItemSubmitTv.setEnabled(true);
        popupPickItemPriceEt.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!popupPickItemPriceEt.getText().toString().isEmpty() && !popupPickItemPriceEt.getText().toString().equals(".")) {
                    itemPrice = Double.parseDouble(popupPickItemPriceEt.getText().toString());
                    //total = itemPrice + order.getShippingCost() + order.getVat();
                    total = itemPrice + order.getShippingCost();
                    popupPickItemTotalValueTv.setText(String.format(Locale.ENGLISH,"%.2f", total) + " " + getString(R.string.sar));
                } else {
                    //popupPickItemTotalValueTv.setText(String.format("%.2f", order.getShippingCost() + order.getVat()) + " " + getString(R.string.sar));
                    popupPickItemTotalValueTv.setText(String.format(Locale.ENGLISH,"%.2f", order.getShippingCost()) + " " + getString(R.string.sar));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        popupPickItemLoaderPv = v.findViewById(R.id.pv_load);
    }

    private void initializeDeliverPopup() {
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_deliver_order, null, false);
        deliverOrderPopupWindow = new PopupWindow(v, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        deliverLl = v.findViewById(R.id.ll_deliver_order);
        deliverLl.setOnClickListener(this);
        deliverContentLl = v.findViewById(R.id.ll_deliver_order_content);
        deliverContentLl.setOnClickListener(this);
        popupDeliverTitleTv = v.findViewById(R.id.tv_deliver_order_title);
        popupDeliverMessageTv = v.findViewById(R.id.tv_deliver_order_message);
        popupDeliverItemPriceLl = v.findViewById(R.id.ll_deliver_item_price);
        popupDeliverItemPriceTitleTv = v.findViewById(R.id.tv_deliver_item_price_title);
        popupDeliverItemPriceValueTv = v.findViewById(R.id.tv_deliver_item_price_value);
        popupDeliverItemPriceView = v.findViewById(R.id.v_deliver_item_price);
        popupDeliverShippingTitleTv = v.findViewById(R.id.tv_deliver_shipping_title);
        popupDeliverShippingValueTv = v.findViewById(R.id.tv_deliver_shipping_value);
        popupDeliverVatTitleTv = v.findViewById(R.id.tv_deliver_vat_title);
        popupDeliverVatValueTv = v.findViewById(R.id.tv_deliver_vat_value);

        popupDeliverDiscountLl = v.findViewById(R.id.ll_deliver_discount);
        popupDeliverDiscountTitleTv = v.findViewById(R.id.tv_deliver_discount_title);
        popupDeliverDiscountValueTv = v.findViewById(R.id.tv_deliver_discount_value);
        popupDeliverDiscountView = v.findViewById(R.id.v_deliver_discount);

        popupDeliverTotalTitleTv = v.findViewById(R.id.tv_deliver_total_title);
        popupDeliverTotalValueTv = v.findViewById(R.id.tv_deliver_total_value);
        popupDeliverNoTv = v.findViewById(R.id.tv_deliver_no);
        popupDeliverNoTv.setOnClickListener(this);
        popupDeliverYesTv = v.findViewById(R.id.tv_deliver_yes);
        popupDeliverYesTv.setOnClickListener(this);
        popupDeliverLoaderPv = v.findViewById(R.id.pv_deliver_load);
    }

    private void setFonts() {
        fonts = MApplication.getInstance().getFonts();
        startRideBtn.setTypeface(fonts.customFontBD());
        itemPickedBtn.setTypeface(fonts.customFontBD());
        orderDeliveredBtn.setTypeface(fonts.customFontBD());

        popupPickItemUpdateCostTv.setTypeface(fonts.customFontBD());
        popupPickItemPriceTv.setTypeface(fonts.customFont());
        popupPickItemCurrencyTv.setTypeface(fonts.customFontBD());
        popupPickItemShippingTitleTv.setTypeface(fonts.customFont());
        popupPickItemShippingValueTv.setTypeface(fonts.customFontBD());
        popupPickItemVatTitleTv.setTypeface(fonts.customFont());
        popupPickItemVatValueTv.setTypeface(fonts.customFontBD());

        popupDeliverDiscountTitleTv.setTypeface(fonts.customFont());
        popupDeliverDiscountValueTv.setTypeface(fonts.customFontBD());

        popupPickItemTotalTitleTv.setTypeface(fonts.customFont());
        popupPickItemTotalValueTv.setTypeface(fonts.customFontBD());
        popupPickItemCancelTv.setTypeface(fonts.customFont());
        popupPickItemSubmitTv.setTypeface(fonts.customFont());
        popupPickItemPriceEt.setTypeface(fonts.customFontBD());
        send_btn.setTypeface(fonts.customFont());
        message_et.setTypeface(fonts.customFont());
        order_titleTV.setTypeface(fonts.customFont());
        tv_userName.setTypeface(fonts.customFont());
        tv_track_order.setTypeface(fonts.customFontBD());
        tv_call.setTypeface(fonts.customFontBD());
        delToFromDistanceTv.setTypeface(fonts.customFont());
        fromToToDistanceTv.setTypeface(fonts.customFont());
        // popupDeliver  font settings
        popupDeliverTitleTv.setTypeface(fonts.customFontBD());
        popupDeliverMessageTv.setTypeface(fonts.customFont());
        popupDeliverItemPriceTitleTv.setTypeface(fonts.customFont());
        popupDeliverItemPriceValueTv.setTypeface(fonts.customFontBD());
        popupDeliverShippingTitleTv.setTypeface(fonts.customFont());
        popupDeliverShippingValueTv.setTypeface(fonts.customFontBD());
        popupDeliverVatTitleTv.setTypeface(fonts.customFont());
        popupDeliverVatValueTv.setTypeface(fonts.customFontBD());
        popupDeliverTotalTitleTv.setTypeface(fonts.customFont());
        popupDeliverTotalValueTv.setTypeface(fonts.customFontBD());
        popupDeliverNoTv.setTypeface(fonts.customFont());
        popupDeliverYesTv.setTypeface(fonts.customFont());

    }

    private void openPickItemPopup() {
        pickItemPopupWindow.setFocusable(true);
        pickItemPopupWindow.update();
        pickItemPopupWindow.showAtLocation(mainContentLl, Gravity.CENTER, 0, 0);
    }

    private void setPickItemPopupData() {
        popupPickItemPriceEt.setText("");
        popupPickItemShippingValueTv.setText(String.format(Locale.ENGLISH,"%.2f", order.getShippingCost()) + " " + getString(R.string.sar));
        //popupPickItemVatValueTv.setText(String.format("%.2f", order.getVat()) + " " + getString(R.string.sar));
        //popupPickItemTotalValueTv.setText(String.format("%.2f", order.getShippingCost() + order.getVat()) + " " + getString(R.string.sar));
        popupPickItemTotalValueTv.setText(String.format(Locale.ENGLISH,"%.2f", order.getShippingCost()) + " " + getString(R.string.sar));
    }

    private void openDeliveryPopup() {
        deliverOrderPopupWindow.showAtLocation(mainContentLl, Gravity.CENTER, 0, 0);
    }

    private void setDeliveryPopupData() {
        popupDeliverShippingValueTv.setText(String.format(Locale.ENGLISH,"%.2f", order.getShippingCost()) + " " + getString(R.string.sar));
        //popupDeliverVatValueTv.setText(String.format("%.2f", order.getVat()) + " " + getString(R.string.sar));
        if (order.getDiscount() != null  && order.getDiscount() != 0) {
            popupDeliverDiscountLl.setVisibility(View.VISIBLE);
            popupDeliverDiscountView.setVisibility(View.VISIBLE);
            popupDeliverDiscountValueTv.setText(String.format(Locale.ENGLISH,"%.2f", order.getDiscount()) + " " + getString(R.string.sar));
        } else {
            popupDeliverDiscountLl.setVisibility(View.GONE);
            popupDeliverDiscountView.setVisibility(View.GONE);
        }

        if (order.getItemPrice() != null) {
            popupDeliverItemPriceLl.setVisibility(View.VISIBLE);
            popupDeliverItemPriceView.setVisibility(View.VISIBLE);
            popupDeliverItemPriceValueTv.setText(String.format(Locale.ENGLISH,"%.2f", order.getItemPrice()) + " " + getString(R.string.sar));
            //popupDeliverTotalValueTv.setText(String.format("%.2f", (order.getShippingCost() + order.getItemPrice() + order.getVat())) + " " + getString(R.string.sar));
            if (order.getDiscount() != null){
                popupDeliverTotalValueTv.setText(String.format(Locale.ENGLISH,"%.2f", (order.getShippingCost() + order.getItemPrice() - order.getDiscount())) + " " + getString(R.string.sar));
            }
            else {
                popupDeliverTotalValueTv.setText(String.format(Locale.ENGLISH,"%.2f", (order.getShippingCost() + order.getItemPrice())) + " " + getString(R.string.sar));
            }
        } else {
            popupDeliverItemPriceLl.setVisibility(View.GONE);
            popupDeliverItemPriceView.setVisibility(View.GONE);
            //popupDeliverTotalValueTv.setText(String.format("%.2f", (order.getShippingCost() + order.getVat())) + " " + getString(R.string.sar));
            if (order.getDiscount() != null){
                popupDeliverTotalValueTv.setText(String.format(Locale.ENGLISH,"%.2f", (order.getShippingCost() - order.getDiscount())) + " " + getString(R.string.sar));
            }
            else {
                popupDeliverTotalValueTv.setText(String.format(Locale.ENGLISH,"%.2f", (order.getShippingCost())) + " " + getString(R.string.sar));
            }
        }
    }

    private void switchToRegisterationOrLogin() {
        Intent registerationOrLoginIntent = new Intent(this, RegisterOrLoginActivity.class);
        registerationOrLoginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(registerationOrLoginIntent);
    }

    private void switchToOrderDetails() {
            Intent orderDetailsIntent;
            if (!fromCustomerOrders) {
                orderDetailsIntent = new Intent(this, DelegateOrderDetailsActivity.class);
            } else {
                orderDetailsIntent = new Intent(this, CustomerOrderDetailsActivity.class);
            }
            orderDetailsIntent.putExtra(Constants.ORDER, order);
            orderDetailsIntent.putExtra(Constants.FROM_CUSTOMER_ORDERS, fromCustomerOrders);
            startActivity(orderDetailsIntent);

    }

    private void switchToCustOrDelProfile(boolean isCustomerProfile, int custOrDelId) {
        Intent custOrDelProfileIntent = new Intent(this, CustOrDelProfileActivity.class);
        custOrDelProfileIntent.putExtra(Constants.IS_CUSTOMER_REVIEWS, isCustomerProfile);
        custOrDelProfileIntent.putExtra(Constants.USER, custOrDelId);
        startActivity(custOrDelProfileIntent);
    }

    private double calculateDistanceFromLegs(ArrayList<DirectionLeg> legs) {
        double distance = 0;
        if (legs != null && !legs.isEmpty()) {
            for (DirectionLeg leg : legs) {
                distance += leg.getDistance().getValue();
            }
        }
        return distance;
    }

    private void calculateAbsoluteDistance() {
        double fromToToDistance = SphericalUtil.computeDistanceBetween(new LatLng(order.getFromLat(), order.getFromLng()),
                new LatLng(order.getToLat(), order.getToLng()));
        if (fromToToDistance < 1000) {
            fromToToDistanceTv.setText(String.format(Locale.ENGLISH,"%.1f", fromToToDistance) + " " + getString(R.string.meter));
        } else {
            fromToToDistanceTv.setText(String.format(Locale.ENGLISH,"%.1f", (fromToToDistance / 1000)) + " " + getString(R.string.kilometer));
        }
    }

    private void calculateDelegateAbsoluteDistance() {
        if (delLat != 0 && delLng != 0) {
            double delegateToFromDistance = SphericalUtil.computeDistanceBetween(new LatLng(delLat, delLng),
                    new LatLng(order.getFromLat(), order.getFromLng()));
            if (delegateToFromDistance < 1000) {
                if (!order.getStatus().equals("delivery_in_progress")){
                    delToFromDistanceTv.setText(String.format(Locale.ENGLISH,"%.1f", delegateToFromDistance) + " " + getString(R.string.meter));
                }
                else {
                    fromToToDistanceTv.setText(String.format(Locale.ENGLISH,"%.1f", delegateToFromDistance) + " " + getString(R.string.meter));
                }
            } else {
                if (!order.getStatus().equals("delivery_in_progress")){
                    delToFromDistanceTv.setText(String.format(Locale.ENGLISH,"%.1f", (delegateToFromDistance / 1000)) + " " + getString(R.string.kilometer));
                }
                else {
                    fromToToDistanceTv.setText(String.format(Locale.ENGLISH,"%.1f", delegateToFromDistance) + " " + getString(R.string.meter));
                }
            }
        } else {
            delegateDistanceRl.setVisibility(View.GONE);
            delegateIv.setVisibility(View.GONE);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_START);
            fromIv.setLayoutParams(layoutParams);
        }
    }

    private void hideDelegateDistance(){
        delegateDistanceRl.setVisibility(View.GONE);
        delegateIv.setVisibility(View.GONE);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_START);
        fromIv.setLayoutParams(layoutParams);
        fromIv.setImageResource(R.drawable.car_ic);
    }

    private void hideKeyboard() {
        View focusedView = this.getCurrentFocus();
        if (focusedView != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
        }
    }

    protected void openDialer(String phone){
        Intent dialerIntent = new Intent(Intent.ACTION_DIAL);
        dialerIntent.setData(Uri.parse("tel:" + phone));
        startActivity(dialerIntent);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_back) {
            if (fromPushNotification) {
                Intent notificationIntent = new Intent(this, MainActivity.class);
                notificationIntent.putExtra(Constants.OPEN_FRAGMENT, NotificationFragment.class.getSimpleName());
                startActivity(notificationIntent);
                finish();
            } else {
                finish();
            }
        } else if (view.getId() == R.id.tv_track_order) {
            Intent orderDetailsIntent;
            if(!isTrackOrderClicked){
                isTrackOrderClicked = true;
                if (!fromCustomerOrders) {
                    orderDetailsIntent = new Intent(this, DelegateOrderDetailsActivity.class);
                } else {
                    orderDetailsIntent = new Intent(this, CustomerOrderDetailsActivity.class);
                }
                orderDetailsIntent.putExtra(Constants.ORDER, order);
                orderDetailsIntent.putExtra(Constants.FROM_CHAT, true);
                startActivity(orderDetailsIntent);
            }

        } else if (view.getId() == R.id.btn_start_ride) {
            orderChatPresenter.startRide(localSettings.getToken(), localSettings.getLocale(), order.getId());
        } else if (view.getId() == R.id.btn_item_picked) {
            setPickItemPopupData();
            openPickItemPopup();
        } else if (view.getId() == R.id.tv_pick_item_cancel) {
            itemPrice = 0;
            total = 0;
            pickItemPopupWindow.dismiss();
        } else if (view.getId() == R.id.tv_pick_item_submit) {
            popupPickItemSubmitTv.setEnabled(false);
            if (!popupPickItemPriceEt.getText().toString().isEmpty() && !popupPickItemPriceEt.getText().toString().equals(".")) {
                orderChatPresenter.pickItem(localSettings.getToken(), localSettings.getLocale(), order.getId(), itemPrice);
            } else {
                orderChatPresenter.pickItem(localSettings.getToken(), localSettings.getLocale(), order.getId());
            }
        } else if (view.getId() == R.id.ll_pick_item) {
            itemPrice = 0;
            total = 0;
            pickItemPopupWindow.dismiss();
        } else if (view.getId() == R.id.ll_popup_content) {

        } else if (view.getId() == R.id.btn_order_delivered) {
            setDeliveryPopupData();
            openDeliveryPopup();
        } else if (view.getId() == R.id.tv_deliver_no) {
            deliverOrderPopupWindow.dismiss();
        } else if (view.getId() == R.id.tv_deliver_yes) {
            popupDeliverYesTv.setEnabled(false);
            try {
                PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                String appVersion = pInfo.versionName;
                orderChatPresenter.deliverOrder(localSettings.getToken(),
                        localSettings.getLocale(), order.getId(), appVersion);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        } else if (view.getId() == R.id.ll_deliver_order) {
            deliverOrderPopupWindow.dismiss();
        } else if (view.getId() == R.id.ll_deliver_order_content) {

        } else if (view.getId() == R.id.send_btn) {
//            send.setEnabled(false);
//            View focusedView = this.getCurrentFocus();
//            if (focusedView != null) {
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
//            }
            Long tsLong = System.currentTimeMillis();
            Message message = new Message();
            MessageChat messageChat = new MessageChat();
            messageChat.no_locale = message_et.getText().toString();
            message.setMsg(messageChat);
            message.setCreated_by(localSettings.getUser().getId());
            message.setRecipient_type(2);
            message.setCreated_at(tsLong);

            orderChatPresenter.sendMessage(String.valueOf(order.getId()), message);
            // message_et.setText("");
        } else if (view.getId() == R.id.iv_close) {

            previewPopupWindow.dismiss();

        } else if (view.getId() == R.id.gallary_img) {

            checkGalleryOrCameraPermissions(Constants.GALLERY);
        } else if (view.getId() == R.id.camera_ic) {

            checkGalleryOrCameraPermissions(Constants.CAMERA);
        } else if (view.getId() == R.id.send_photo) {
            send_photo.setEnabled(false);

            String order_id = order.getId() + "";
            AddImageUtilities.resizeImage(imageFile);
            orderChatPresenter.uploadChatImage(localSettings.getToken(), localSettings.getLocale(), imageFile, order_id);
            //   orderChatPresenter.uploadChatImage(localSettings.getToken(), localSettings.getLocale(), this.imageFile,order_id);

        } else if (view.getId() == R.id.tv_userName){
            if (fromCustomerOrders){
                if (order.getDelegate() != null){
                    switchToCustOrDelProfile(false, order.getDelegate().getId());
                }
            }
            else {
                if (order.getCreatedBy() != null){
                    switchToCustOrDelProfile(true, order.getCreatedBy().getId());
                }
            }

        } else if (view.getId() == R.id.iv_user_image){
            if (fromCustomerOrders){
                if (order.getDelegate() != null){
                    switchToCustOrDelProfile(false, order.getDelegate().getId());
                }
            }
            else {
                if (order.getCreatedBy() != null){
                    switchToCustOrDelProfile(true, order.getCreatedBy().getId());
                }
            }
        } else if (view.getId() == R.id.ll_main_content){
            hideKeyboard();
        } else if (view.getId() == R.id.tv_call){
            if (fromCustomerOrders){
                if (order.getDelegate() != null){
                    openDialer(order.getDelegate().getMobile());
                }
            }
            else {
                if (order.getCreatedBy() != null){
                    openDialer(order.getCreatedBy().getMobile());
                }
            }
        }

    }

    @Override
    public void onBackPressed() {
        if (pickItemPopupWindow.isShowing()) {
            itemPrice = 0;
            total = 0;
            pickItemPopupWindow.dismiss();
        } else if (previewPopupWindow.isShowing()) {
            previewPopupWindow.dismiss();
        } else if (fromPushNotification) {
            Intent notificationIntent = new Intent(this, MainActivity.class);
            notificationIntent.putExtra(Constants.OPEN_FRAGMENT, NotificationFragment.class.getSimpleName());
            startActivity(notificationIntent);
            finish();
        } else {
            super.onBackPressed();
        }

    }

    private void initializePreviewPopup() {
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_delegate_images_preview, null, false);
        previewPopupWindow = new PopupWindow(v, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        popupPreviewIv = v.findViewById(R.id.iv_preview);
        popupCloseIv = v.findViewById(R.id.iv_close);
        send_photo = v.findViewById(R.id.send_photo);
        pv_load_popup = v.findViewById(R.id.pv_load_popup);

        send_photo.setVisibility(View.GONE);
        send_photo.setOnClickListener(this);
        popupCloseIv.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);

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
    public void showPickItemPopupLoading() {
        popupPickItemLoaderPv.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public void hidePickItemPopupLoading(boolean isFromSucess) {
        popupPickItemLoaderPv.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        if (!isFromSucess) {
            popupPickItemSubmitTv.setEnabled(true);
        }

    }

    @Override
    public void showDeliverPopupLoading() {
        popupDeliverLoaderPv.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public void hideDeliverPopupLoading() {
        popupDeliverLoaderPv.setVisibility(View.GONE);
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
    public void showToastNetworkError() {
        if (popupDeliverYesTv != null) {
            popupDeliverYesTv.setEnabled(true);

        }
        Toast.makeText(this, R.string.connection_error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showServerError() {
        Toast.makeText(this, R.string.server_error, Toast.LENGTH_LONG).show();
        if (popupDeliverYesTv != null) {
            popupDeliverYesTv.setEnabled(true);

        }
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
    public void showPerformActionError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        if (popupDeliverYesTv != null) {
            popupDeliverYesTv.setEnabled(true);

        }
    }

    @Override
    public void showSuccessStartRide() {
        startRideFl.setVisibility(View.GONE);
        itemPickedFl.setVisibility(View.VISIBLE);
        //tracker.getLocation();
        if (order.getDelegate() != null){
            orderChatPresenter.getOrderDelegateLocation(order.getDelegate().getId());
        }
        else {
            orderChatPresenter.getOrderDelegateLocation(localSettings.getUser().getId());
        }
    }

    @Override
    public void showSuccessPickItem(Order order) {
        this.order = order;
        pickItemPopupWindow.dismiss();
        itemPickedFl.setVisibility(View.GONE);
        orderDeliveredFl.setVisibility(View.VISIBLE);
        //tracker.getLocation();
        hideDelegateDistance();
        if (order.getDelegate() != null){
            orderChatPresenter.getOrderDelegateLocation(order.getDelegate().getId());
        }
        else {
            orderChatPresenter.getOrderDelegateLocation(localSettings.getUser().getId());
        }
    }

    @Override
    public void showSuccessDeliverOrder() {
        switchToOrderDetails();
        finish();
    }

    @Override
    public void showMessagesList(ArrayList<Message> messages) {
        this.messages.clear();
        this.messages.addAll(messages);
        chatAdapter.notifyDataSetChanged();
        messages_list.scrollToPosition(this.messages.size() - 1);
        messages_list.smoothScrollToPosition(this.messages.size() - 1);
    }

    @Override
    public void onImageClick(ImageSizes imageChat) {
        send_photo.setVisibility(View.GONE);
        Glide.with(this).load(imageChat.getBig())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.upload_default)
                        .dontAnimate())
                .into(popupPreviewIv);
                //.dontAnimate().placeholder(R.drawable.upload_default).into(popupPreviewIv);
        previewPopupWindow.showAtLocation(mainContentLl, Gravity.CENTER, 0, 0);
    }

    @Override
    public void showSuccessSend() {
        Log.d("sucessSend", "sucess send");
    }

    @Override
    public void showPopUpLoading() {
        pv_load_popup.setVisibility(View.VISIBLE);
    }

    @Override
    public void hidePopUpLoading(boolean isFromSucess) {
        pv_load_popup.setVisibility(View.GONE);
        if (!isFromSucess) {
            send_photo.setEnabled(true);
        }


    }

    @Override
    public void clearMessageText() {
        message_et.setText("");
    }

    @Override
    public void showSuccessUploadImage(UploadDelegateImagesResponse delegateImageData) {
        Long tsLong = System.currentTimeMillis();
        Message message = new Message();
        ImageChat imageChat = new ImageChat();

        imageChat.id = delegateImageData.getData().getId();
        imageChat.url = delegateImageData.getData().getImage().getMedium();
        ArrayList<ImageChat> messageChat_arr = new ArrayList<ImageChat>();
        messageChat_arr.add(imageChat);
        message.setCreated_by(localSettings.getUser().getId());
        message.setRecipient_type(2);
        message.setCreated_at(tsLong);
        message.setImages(messageChat_arr);
        previewPopupWindow.dismiss();
        orderChatPresenter.sendMessage(String.valueOf(order.getId()), message);
    }

    @Override
    public void showOrderDirections(Route route, boolean isDelegateDirection) {
        double distance = calculateDistanceFromLegs(route.getLegs());
        if (isDelegateDirection) {
            if (distance > 0) {
                if (distance < 1000) {
                    if (!order.getStatus().equals("delivery_in_progress")) {
                        delToFromDistanceTv.setText(String.format(Locale.ENGLISH,"%.1f", distance) + " " + getString(R.string.meter));
                    } else {
                        fromToToDistanceTv.setText(String.format(Locale.ENGLISH,"%.1f", distance) + " " + getString(R.string.meter));
                    }
                } else {
                    if (!order.getStatus().equals("delivery_in_progress")) {
                        delToFromDistanceTv.setText(String.format(Locale.ENGLISH,"%.1f", (distance / 1000)) + " " + getString(R.string.kilometer));
                    } else {
                        fromToToDistanceTv.setText(String.format(Locale.ENGLISH,"%.1f", (distance / 1000)) + " " + getString(R.string.kilometer));
                    }
                }
            } else {
                calculateDelegateAbsoluteDistance();
            }
        } else {
            if (distance > 0) {
                if (distance < 1000) {
                    fromToToDistanceTv.setText(String.format(Locale.ENGLISH,"%.1f", distance) + " " + getString(R.string.meter));
                } else {
                    fromToToDistanceTv.setText(String.format(Locale.ENGLISH,"%.1f", (distance / 1000)) + " " + getString(R.string.kilometer));
                }
            } else {
                calculateAbsoluteDistance();
            }
        }
    }

    @Override
    public void showDefaultDistance(boolean isDelegateDirection) {
        if (isDelegateDirection) {
            calculateDelegateAbsoluteDistance();
        } else {
            calculateAbsoluteDistance();
        }
    }

    @Override
    public void hideTokenLoader() {
        hideLoading();
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

                        Glide.with(this).load(imageFile)
                                .apply(new RequestOptions()
                                        .placeholder(R.drawable.upload_default)
                                        .dontAnimate())
                                .into(popupPreviewIv);
                                //.placeholder(R.drawable.upload_default).dontAnimate().into(popupPreviewIv);
                        send_photo.setVisibility(View.VISIBLE);
                        send_photo.setEnabled(true);

                        previewPopupWindow.showAtLocation(mainContentLl, Gravity.CENTER, 0, 0);
                    }
                }
            } else if (requestCode == Constants.CAMERA) {
                String imagePath;
                if (this.imageFile != null) {
                    imagePath = this.imageFile.getAbsolutePath();
                    if (validateImage(imagePath)) {
                        Glide.with(this).load(this.imageFile)
                                .apply(new RequestOptions()
                                        .placeholder(R.drawable.upload_default)
                                        .dontAnimate())
                                .into(popupPreviewIv);
                                //.placeholder(R.drawable.upload_default).dontAnimate().into(popupPreviewIv);
                        send_photo.setVisibility(View.VISIBLE);
                        send_photo.setEnabled(true);
                        previewPopupWindow.showAtLocation(mainContentLl, Gravity.CENTER, 0, 0);
                    }
                }


            }
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
    public void onRequestGallery(Intent gallery) {
        startActivityForResult(gallery, Constants.GALLERY);
    }

    @Override
    public void onRequestCamera(Intent camera, File imageFile) {
        this.imageFile = imageFile;
        startActivityForResult(camera, Constants.CAMERA);
    }

    @Override
    public void showDelegateCurrentLocation(double delLat, double delLng) {
        //tracker.stopUsingGPS();
        this.delLat = delLat;
        this.delLng = delLng;
        if (!order.getStatus().equals("delivery_in_progress")) {
            orderChatPresenter.getDirections(delLat + "," + delLng, order.getFromLat() + "," + order.getFromLng(),
                    "driving", getString(R.string.google_server_key), true);
        } else {
            orderChatPresenter.getDirections(delLat + "," + delLng, order.getToLat() + "," + order.getToLng(),
                    "driving", getString(R.string.google_server_key), true);
        }
    }

    /*@Override
    public void setAddressData(String countryNameCode, String city, double latitude, double longitude) {
        tracker.stopUsingGPS();
        this.currentLat = latitude;
        this.currentLng = longitude;
        if (!order.getStatus().equals("delivery_in_progress")) {
            orderChatPresenter.getDirections(currentLat + "," + currentLng, order.getFromLat() + "," + order.getFromLng(),
                    "driving", getString(R.string.google_server_key), true);
        } else {
            orderChatPresenter.getDirections(currentLat + "," + currentLng, order.getToLat() + "," + order.getToLng(),
                    "driving", getString(R.string.google_server_key), true);
        }
    }

    @Override
    public void openLocationSettings() {

    }

    @Override
    public void showActivateGPS() {

    }*/
}
