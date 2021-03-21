package amaz.objects.TwentyfourSeven.ui.OrderDetails;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.SphericalUtil;
import com.rey.material.widget.ProgressView;
import com.squareup.picasso.Picasso;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import amaz.objects.TwentyfourSeven.BaseActivity;
import amaz.objects.TwentyfourSeven.MApplication;
import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.adapters.OrderImagesAdapter;
import amaz.objects.TwentyfourSeven.api.APIURLs;
import amaz.objects.TwentyfourSeven.data.models.CancelationReason;
import amaz.objects.TwentyfourSeven.data.models.DelegateImageData;
import amaz.objects.TwentyfourSeven.data.models.DirectionLeg;
import amaz.objects.TwentyfourSeven.data.models.ImageSizes;
import amaz.objects.TwentyfourSeven.data.models.Offer;
import amaz.objects.TwentyfourSeven.data.models.Order;
import amaz.objects.TwentyfourSeven.data.models.Route;
import amaz.objects.TwentyfourSeven.injection.Injection;
import amaz.objects.TwentyfourSeven.listeners.OnImageClickListener;
import amaz.objects.TwentyfourSeven.listeners.OnRefeshTokenResponse;
import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import amaz.objects.TwentyfourSeven.presenter.PresenterFactory;
import amaz.objects.TwentyfourSeven.ui.Categories.CategoriesFragment;
import amaz.objects.TwentyfourSeven.ui.CustOrDelProfile.CustOrDelProfileActivity;
import amaz.objects.TwentyfourSeven.ui.MyAccount.MainActivity;
import amaz.objects.TwentyfourSeven.ui.Notification.NotificationFragment;
import amaz.objects.TwentyfourSeven.ui.OrderChat.OrderChatActivity;
import amaz.objects.TwentyfourSeven.ui.OrderDetails.CustomerOrderDetails.CustomerOrderDetailsActivity;
import amaz.objects.TwentyfourSeven.ui.RegisterOrLogin.RegisterOrLoginActivity;
import amaz.objects.TwentyfourSeven.ui.SubmitComplaint.SubmitComplaintActivity;
import amaz.objects.TwentyfourSeven.utilities.Constants;
import amaz.objects.TwentyfourSeven.utilities.Fonts;
import amaz.objects.TwentyfourSeven.utilities.LanguageUtilities;
import amaz.objects.TwentyfourSeven.utilities.LocalSettings;
import amaz.objects.TwentyfourSeven.utilities.TokenUtilities;

public class OrderDetailsActivity extends BaseActivity implements View.OnClickListener, OrderDetailsPresenter.OrderDetailsView,
        OnRefeshTokenResponse, OnImageClickListener {

    private SupportMapFragment mapFragment;
    private TextView orderNoTv, deliveryTimeTv, orderDateTv, orderStatusTv, fromTv, toTv, totaldistanceTv, waitingDriversTv, waitingTimeTv, offersTv,
            orderItemsTv, orderDescriptionTv, cancelReasonContentTv;

    private TextView noConnectionTitleTv, noConnectionMessageTv;
    private LinearLayout noConnectionLl;
    private Button reloadBtn;

    private ExpandableLayout orderItemsEl;
    private RelativeLayout mainContentRl;
    private Button cancelBtn;
    private RecyclerView offersRv, orderImagesRv;
    private OrderImagesAdapter orderImagesAdapter;
    private ArrayList<DelegateImageData> orderImages = new ArrayList<>();
    private ImageView backIv;
    private ProgressView loaderPv;

    private PopupWindow previewPopupWindow;
    private ImageView popupCloseIv;
    private ImageView popupPreviewIv;

    private LocalSettings localSettings;
    private Fonts fonts;

    //private boolean isOffersExpanded = false;
    //private boolean isOrderItemsExpanded = false;

    private OrderDetailsPresenter orderDetailsPresenter;
    private int orderId;

    private GoogleMap googleMap;
    private Order order;
    private double fromToToDistance;
    private boolean fromChat;
    private boolean fromCustomerOrders;
    private boolean fromPushNotification,fromCreateOrder;
    private Polyline pickupToDestLine;

    private boolean drawDelToDestination = true;
    private Marker carMarker;
    private boolean isMarkerRotating;
    private List<LatLng> routePoints;
    private List<LatLng> newRoutePoints = new ArrayList<>();
    private LatLng currentPoint;
    private double delLat, delLng;

    private boolean isStatusChanged = false;

    @Override
    public void onPresenterAvailable(BasePresenter presenter) {
        orderDetailsPresenter = (OrderDetailsPresenter) presenter;
    }

    @Override
    public PresenterFactory.PresenterType getPresenterType() {
        return PresenterFactory.PresenterType.OrderDetails;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        localSettings = new LocalSettings(this);
        setLanguage();
        setContentView(R.layout.activity_order_details);
        getDataFromIntent();
        initialization();
        initializePreviewPopup();
        setFonts();
        setUpMap();
    }

    @Override
    protected void onResume() {
        super.onResume();
        localSettings.setIsOrderDetailsOpened(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        localSettings.setIsOrderDetailsOpened(false);
    }

    private void setLanguage() {
        if (localSettings.getLocale().equals(Constants.ARABIC)) {
            LanguageUtilities.switchToArabicLocale(this);
        }
    }

    private void getDataFromIntent() {
        Order order = (Order) getIntent().getSerializableExtra(Constants.ORDER);
        if (order != null){
            this.orderId = order.getId();
            Log.e("orderID",order.getId()+"");
            Log.e("token",localSettings.getToken());
        }
        else {

            this.orderId = getIntent().getIntExtra("order_id",0);
            Log.e("orderID",orderId+"");
            Log.e("token",localSettings.getToken());
        }
        fromChat = getIntent().getBooleanExtra(Constants.FROM_CHAT, false);
        fromCustomerOrders = getIntent().getBooleanExtra(Constants.FROM_CUSTOMER_ORDERS, false);
        fromPushNotification = getIntent().getBooleanExtra(Constants.FROM_PUSH_NOTIFICATION, false);
        fromCreateOrder = getIntent().getBooleanExtra(Constants.FROM_CREATE_ORDER, false);

    }

    private void initialization() {
        Log.e("userid",localSettings.getUser().getId()+"");
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fr_map);
        backIv = findViewById(R.id.iv_back);
        backIv.setOnClickListener(this);
        if (localSettings.getLocale().equals(Constants.ARABIC)) {
            backIv.setImageResource(R.drawable.back_ar_ic);
        } else {
            backIv.setImageResource(R.drawable.back_ic);
        }

        mainContentRl = findViewById(R.id.rl_main_content);
        mainContentRl.setVisibility(View.GONE);
        orderNoTv = findViewById(R.id.tv_order_number);
        deliveryTimeTv = findViewById(R.id.tv_delivery_duration);
        deliveryTimeTv.setVisibility(View.GONE);
        orderDateTv = findViewById(R.id.tv_order_date);
        orderStatusTv = findViewById(R.id.tv_order_status);
        fromTv = findViewById(R.id.tv_from);
        toTv = findViewById(R.id.tv_to);
        totaldistanceTv = findViewById(R.id.tv_distance_from_to);

        waitingDriversTv = findViewById(R.id.tv_waiting_drivers);
        waitingTimeTv = findViewById(R.id.tv_waiting_time);
        cancelBtn = findViewById(R.id.btn_cancel);
        cancelBtn.setOnClickListener(this);
        cancelBtn = findViewById(R.id.btn_cancel);

        offersTv = findViewById(R.id.tv_offers);
        offersTv.setOnClickListener(this);
        offersRv = findViewById(R.id.rv_offers);
        offersRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        orderItemsEl = findViewById(R.id.el_order_items);
        orderItemsTv = findViewById(R.id.tv_order_items);
        orderItemsTv.setOnClickListener(this);
        orderDescriptionTv = findViewById(R.id.tv_order_description);
        cancelReasonContentTv = findViewById(R.id.tv_cancel_reason_content);
        orderImagesRv = findViewById(R.id.rv_order_images);
        orderImagesRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        orderImagesAdapter = new OrderImagesAdapter(this, orderImages, this);
        orderImagesRv.setAdapter(orderImagesAdapter);

        loaderPv = findViewById(R.id.pv_load);

        noConnectionLl = findViewById(R.id.ll_no_connection);
        noConnectionTitleTv = findViewById(R.id.tv_no_connection_title);
        noConnectionMessageTv = findViewById(R.id.tv_no_connection_message);
        reloadBtn = findViewById(R.id.btn_reload_page);
        reloadBtn.setOnClickListener(this);
    }

    private void initializePreviewPopup(){
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_delegate_images_preview,null,false);
        previewPopupWindow = new PopupWindow(v, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        popupPreviewIv = v.findViewById(R.id.iv_preview);
        popupCloseIv = v.findViewById(R.id.iv_close);
        popupCloseIv.setOnClickListener(this);
    }

    private void setFonts() {
        fonts = MApplication.getInstance().getFonts();
        orderNoTv.setTypeface(fonts.customFont());
        deliveryTimeTv.setTypeface(fonts.customFont());
        orderDateTv.setTypeface(fonts.customFont());
        orderStatusTv.setTypeface(fonts.customFontBD());
        fromTv.setTypeface(fonts.customFont());
        toTv.setTypeface(fonts.customFont());
        totaldistanceTv.setTypeface(fonts.customFont());
        waitingDriversTv.setTypeface(fonts.customFont());
        waitingTimeTv.setTypeface(fonts.customFont());
        cancelBtn.setTypeface(fonts.customFontBD());
        offersTv.setTypeface(fonts.customFontBD());
        orderItemsTv.setTypeface(fonts.customFontBD());
        orderDescriptionTv.setTypeface(fonts.customFont());
        cancelReasonContentTv.setTypeface(fonts.customFont());

        noConnectionTitleTv.setTypeface(fonts.customFontBD());
        noConnectionMessageTv.setTypeface(fonts.customFont());
        reloadBtn.setTypeface(fonts.customFontBD());
    }


    private void setUpMap() {
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap googleMap) {
                OrderDetailsActivity.this.googleMap = googleMap;
                try {
                    // Customise the styling of the base map using a JSON object defined
                    // in a raw resource file.
                    boolean success = OrderDetailsActivity.this.googleMap.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                    OrderDetailsActivity.this, R.raw.map_style));

                    if (!success) {
                        Log.e("MapsActivityRaw", "Style parsing failed.");
                    }
                } catch (Resources.NotFoundException e) {
                    Log.e("MapsActivityRaw", "Can't find style.", e);
                }

                googleMap.getUiSettings().setScrollGesturesEnabled(true);
                googleMap.clear();
                //googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                googleMap.getUiSettings().setRotateGesturesEnabled(false);

//                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//                    @Override
//                    public boolean onMarkerClick(Marker marker) {
//                        String uriBegin = "geo:" + marker.getPosition().latitude + "," + marker.getPosition().longitude;
//                        String query = marker.getPosition().latitude + "," + marker.getPosition().longitude;
//                        String encodedQuery = Uri.encode(query);
//
//                        String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
//                        Uri uri = Uri.parse(uriString);
//                        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
//                        startActivity(intent);
//                        return true;
//                    }
//                });
            }


        });

    }

    private void setMapOrderData(final double fromLat, final double fromLng, final double toLat, final double toLng) {
        LatLng fromLocation = new LatLng(fromLat, fromLng);
        LatLng toLocation = new LatLng(toLat, toLng);

        final LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(fromLocation);
        builder.include(toLocation);

        final LatLngBounds bounds = builder.build();
//        if(localSettings.getLocale().equals(Constants.ARABIC)){
//            googleMap.addMarker(new MarkerOptions().position(toLocation).icon(BitmapDescriptorFactory.fromResource(R.drawable.btn_blue_ar)));
//            googleMap.addMarker(new MarkerOptions().position(fromLocation).icon(BitmapDescriptorFactory.fromResource(R.drawable.btn_red_ar)));
//        }
//        else {
//            googleMap.addMarker(new MarkerOptions().position(toLocation).icon(BitmapDescriptorFactory.fromResource(R.drawable.btn_blue_en)));
//            googleMap.addMarker(new MarkerOptions().position(fromLocation).icon(BitmapDescriptorFactory.fromResource(R.drawable.btn_red_en)));
//        }

        googleMap.addMarker(new MarkerOptions().position(toLocation).icon(BitmapDescriptorFactory.fromResource(R.drawable.destination_pin_map)));
        googleMap.addMarker(new MarkerOptions().position(fromLocation).icon(BitmapDescriptorFactory.fromResource(R.drawable.store_pin_map)));

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(fromLocation, 17));

        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                try{
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 270));
                } catch (IllegalStateException ex){
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 200));
                }
                //googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 270));
            }
        });

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return true;
            }
        });
    }

    private void setRoute(final double fromLat, final double fromLng, final double toLat, final double toLng, Route route){
        List<LatLng> polyz = PolyUtil.decode(route.getRoutePolyLine().getPoints());
        this.routePoints = polyz;
        if (polyz != null) {
            PolylineOptions lineOptions = new PolylineOptions();
            lineOptions.addAll(polyz);
            lineOptions.width(5);
            lineOptions.color(ContextCompat.getColor(this, R.color.hint_blue));

            LatLng fromLocation = new LatLng(fromLat, fromLng);
            LatLng toLocation = new LatLng(toLat, toLng);
            final LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(fromLocation);
            builder.include(toLocation);
            for (int i = 0; i<polyz.size();i++){
                builder.include(polyz.get(i));
            }
            final LatLngBounds bounds = builder.build();

            if (googleMap != null){
                googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {
                        try{
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 270));
                        } catch (IllegalStateException ex){
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 200));
                        }
                        //googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 300));
                    }
                });
                pickupToDestLine = googleMap.addPolyline(lineOptions);
            }

            if (pickupToDestLine != null) {
                pickupToDestLine.remove();
            }
            pickupToDestLine = getGoogleMap().addPolyline(lineOptions);

        }
    }

    private void toggleOrderItemsDialog() {
        if (this instanceof CustomerOrderDetailsActivity){
            if (((CustomerOrderDetailsActivity)this).isOffersExpanded() && !orderItemsEl.isExpanded() && offersTv.getVisibility() == View.VISIBLE){
                ((CustomerOrderDetailsActivity)this).collapseOffersDialog();
                orderItemsEl.expand(true);
            }
            else if(!((CustomerOrderDetailsActivity)this).isOffersExpanded()&& orderItemsEl.isExpanded() && offersTv.getVisibility() == View.VISIBLE){
                ((CustomerOrderDetailsActivity)this).toggleOffersDialog();
                orderItemsEl.collapse(true);
            }
            else {
                orderItemsEl.toggle(true);
            }
        }
        else {
            orderItemsEl.toggle(true);
            if (orderItemsEl.isExpanded()) {
                //isOrderItemsExpanded = false;
                orderItemsTv.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(this,R.drawable.order_items_ic),null,ContextCompat.getDrawable(this,R.drawable.anchor_upward),null);
            } else {
                //isOrderItemsExpanded = true;
                orderItemsTv.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(this,R.drawable.order_items_ic), null,ContextCompat.getDrawable(this,R.drawable.anchor_downward),null);
            }
        }

    }

    protected void collapseOrderItemsDialog(){
        orderItemsEl.collapse(true);
        orderItemsTv.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(this,R.drawable.order_items_ic), null, ContextCompat.getDrawable(this,R.drawable.anchor_upward),null);
        //isOrderItemsExpanded = false;
    }

    private void switchToRegisterationOrLogin() {
        Intent registerationOrLoginIntent = new Intent(this, RegisterOrLoginActivity.class);
        registerationOrLoginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(registerationOrLoginIntent);
    }

    protected void switchToSubmitComplaint() {
        Intent submitComplaintIntent = new Intent(this, SubmitComplaintActivity.class);
        submitComplaintIntent.putExtra(Constants.ORDER, orderId);
        startActivity(submitComplaintIntent);
    }

    protected void switchToCustOrDelProfile(boolean isCustomerProfile, int custOrDelId) {
        Intent custOrDelProfileIntent = new Intent(this, CustOrDelProfileActivity.class);
        custOrDelProfileIntent.putExtra(Constants.IS_CUSTOMER_REVIEWS, isCustomerProfile);
        custOrDelProfileIntent.putExtra(Constants.USER, custOrDelId);
        startActivity(custOrDelProfileIntent);
    }

    protected void openDialer(String phone){
        Intent dialerIntent = new Intent(Intent.ACTION_DIAL);
        dialerIntent.setData(Uri.parse("tel:" + phone));
        startActivity(dialerIntent);
    }

    private void setStatusData(String status){
        if (status.equals("new")){
            orderStatusTv.setText(getString(R.string.newstaus));
            orderStatusTv.setTextColor(ContextCompat.getColor(this,R.color.hint_blue));

        }
        else if(status.equals("pending")){
            orderStatusTv.setText(getString(R.string.pending));
            orderStatusTv.setTextColor(ContextCompat.getColor(this,R.color.pending_color));
        }
        else if(status.equals("assigned")){
            orderStatusTv.setText(getString(R.string.assigned));
            orderStatusTv.setTextColor(ContextCompat.getColor(this,R.color.assigned_color));
        }
        else if(status.equals("in_progress")){
            orderStatusTv.setText(getString(R.string.in_progress));
            orderStatusTv.setTextColor(ContextCompat.getColor(this,R.color.in_progres_color));
        }
        else if(status.equals("delivery_in_progress")){
            orderStatusTv.setText(getString(R.string.being_delivered));
            orderStatusTv.setTextColor(ContextCompat.getColor(this,R.color.progress_color));

        }
        else if(status.equals("delivered")){
            orderStatusTv.setText(getString(R.string.delivered));
            orderStatusTv.setTextColor(ContextCompat.getColor(this,R.color.light_green));
        }
        else if(status.equals("cancelled")){
            orderStatusTv.setText(getString(R.string.cancelled));
            orderStatusTv.setTextColor(ContextCompat.getColor(this,R.color.app_color));
        }

    }

    /*private String convertStringToDate(String dateString){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        String newFormattedDate = "";
        try {
            this.orderDate = format.parse(dateString);
            newFormattedDate = convertDateFormat();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newFormattedDate;
    }

    private String convertDateFormat(){
        SimpleDateFormat format = new SimpleDateFormat("dd MMM yyy");
        return format.format(this.orderDate);
    }*/

    private Date convertStringToDate(String dateString){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ",new Locale(localSettings.getLocale()));
        try {
            return format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String convertDateFormat(Date date){
        SimpleDateFormat format = new SimpleDateFormat("dd MMM yyy",new Locale(localSettings.getLocale()));
        return format.format(date);
    }

    private void calculateAbsoluteDistance(){
        /*Location fromLocation = new Location("fromLocation");
        fromLocation.setLatitude(order.getFromLat());
        fromLocation.setLongitude(order.getFromLng());

        Location toLocation = new Location("toLocation");
        toLocation.setLatitude(order.getToLat());
        toLocation.setLongitude(order.getToLng());

        fromToToDistance = fromLocation.distanceTo(toLocation);*/
        fromToToDistance = SphericalUtil.computeDistanceBetween(new LatLng(order.getFromLat(), order.getFromLng()),
                new LatLng(order.getToLat(), order.getToLng()));
        if (fromToToDistance<1000){
            totaldistanceTv.setText(String.format(Locale.ENGLISH,"%.1f",fromToToDistance)+" "+getString(R.string.meter));
        }
        else {
            totaldistanceTv.setText(String.format(Locale.ENGLISH,"%.1f",(fromToToDistance/1000))+" "+getString(R.string.kilometer));
        }
    }

    protected double calculateDistanceFromLegs(ArrayList<DirectionLeg> legs){
        double distance = 0;
        if (legs != null && !legs.isEmpty()){
            for (DirectionLeg leg : legs){
                distance += leg.getDistance().getValue();
            }
        }
        return distance;
    }

    /*public List<LatLng> decodeOverviewPolyLinePonts(String encoded) {
        List<LatLng> poly = new ArrayList<LatLng>();
        if (encoded != null && !encoded.isEmpty() && encoded.trim().length() > 0) {
            int index = 0, len = encoded.length();
            int lat = 0, lng = 0;

            while (index < len) {
                int b, shift = 0, result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lat += dlat;

                shift = 0;
                result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lng += dlng;

                LatLng p = new LatLng((((double) lat / 1E5)),
                        (((double) lng / 1E5)));
                poly.add(p);
            }
        }
        return poly;
    }*/

    private void updateOrderView(Order order){
        this.order = order;
        mainContentRl.setVisibility(View.VISIBLE);
        noConnectionLl.setVisibility(View.GONE);
        orderNoTv.setText(String.valueOf(order.getId()));
        deliveryTimeTv.setText(String.format(Locale.ENGLISH,"%02d", order.getDeliveryDuration())+" "+getString(R.string.houre));
        if (order.getStore_name() == null) {
            fromTv.setText("\u200E" + order.getFrom_address() + "\u200E");
        } else {
            fromTv.setText("\u200E" + order.getStore_name() + "\u200E");
        }
        toTv.setText("\u200E" + order.getTo_address() + "\u200E");
        setStatusData(order.getStatus());
        orderDateTv.setText(convertDateFormat(convertStringToDate(order.getCreated_at())));
        /*if (order.getDelegateSearchStartedAt() != null && !order.getDelegateSearchStartedAt().isEmpty()){
            convertStringToDate(order.getDelegateSearchStartedAt());
        }*/
        Log.e("latlng", order.getFromLat() + "  " + order.getFromLng() + "  " + order.getToLat() + "  " + order.getToLng());
        orderDescriptionTv.setText("\u200E" + order.getDescription() + "\u200E");
        orderImages.clear();
        orderImages.addAll(order.getImages());
        orderImagesAdapter.notifyDataSetChanged();
        if (orderImages.isEmpty()){
            orderImagesRv.setVisibility(View.GONE);
        }
        else {
            orderImagesRv.setVisibility(View.VISIBLE);
        }
        /*if (order.getStatus().equals("delivery_in_progress")){
            if (drawDelToDestination) {
                if (order.getDelegate() != null){
                    getOrderDetailsPresenter().getOrderDelegateLocation(order.getDelegate().getId());
                }
                else {
                    //getOrderDetailsPresenter().getOrderDelegateLocation(order.getCreatedBy().getId());
                }
            }
        }
        else {
            orderDetailsPresenter.getDirections(order.getFromLat() + "," + order.getFromLng(), order.getToLat() + "," + order.getToLng(),
                    "driving", getString(R.string.google_server_key), false);
        }*/

        if (order.getStatus().equals("delivery_in_progress")){
            if (drawDelToDestination) {
                if (order.getDelegate() != null){
                    getOrderDetailsPresenter().getOrderDelegateLocation(order.getDelegate().getId());
                }
            }
        }
        else {
            if(order.getStatus().equals("cancelled") || order.getStatus().equals("delivered")){
                if (carMarker != null){
                    carMarker.remove();
                }
            }

            orderDetailsPresenter.getDirections(order.getFromLat() + "," + order.getFromLng(), order.getToLat() + "," + order.getToLng(),
                    "driving", getString(R.string.google_server_key), false);

        }

        if (order.getStatus().equals("cancelled")){
            if (order.getCancelationReason() != null){
                cancelReasonContentTv.setVisibility(View.VISIBLE);
                cancelReasonContentTv.setText(getString(R.string.cancelation_reasons)+order.getCancelationReason().getTitle());
            }
            else {
                cancelReasonContentTv.setVisibility(View.GONE);
            }
        }
        else {
            cancelReasonContentTv.setVisibility(View.GONE);
        }
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Date getOrderDate() {
        return convertStringToDate(order.getDelegateSearchStartedAt());
    }

    public boolean isStatusChanged() {
        return isStatusChanged;
    }

    public void setStatusChanged(boolean statusChanged) {
        isStatusChanged = statusChanged;
    }

    public OrderDetailsPresenter getOrderDetailsPresenter() {
        return orderDetailsPresenter;
    }

    public LocalSettings getLocalSettings() {
        return localSettings;
    }

    public boolean isOrderItemsExpanded() {
        return orderItemsEl.isExpanded();
    }

    public GoogleMap getGoogleMap() {
        return googleMap;
    }

    public RelativeLayout getMainContentRl() {
        return mainContentRl;
    }

    public double getFromToToDistance() {
        return fromToToDistance;
    }

    public boolean isFromChat() {
        return fromChat;
    }

    public Polyline getPickupToDestLine() {
        return pickupToDestLine;
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

//    private void updateRoute(){
//        double firstToSecondDistance;
//        double delegateToFirstDistance;
//        double delegateToSecondDistance;
//        int removedIndex = -1;
//        for (int i = 1 ; i<routePoints.size() ; i++){
//            firstToSecondDistance = SphericalUtil.computeDistanceBetween(routePoints.get(i-1), routePoints.get(i));
//            delegateToFirstDistance = SphericalUtil.computeDistanceBetween(routePoints.get(i-1), currentPoint);
//            delegateToSecondDistance = SphericalUtil.computeDistanceBetween(currentPoint, routePoints.get(i));
//            if (delegateToFirstDistance<firstToSecondDistance && delegateToSecondDistance<firstToSecondDistance){
//                removedIndex = routePoints.indexOf(routePoints.get(i-1));
//                break;
//            }
//        }
//
//        if (removedIndex != -1){
//            boolean isPointsAdded = false;
//            newRoutePoints.clear();
//            newRoutePoints.add(0, currentPoint);
//            for (int i = removedIndex+1 ; i<routePoints.size() ; i++){
//                newRoutePoints.add(routePoints.get(i));
//                isPointsAdded = true;
//            }
//            if (isPointsAdded){
//                routePoints.clear();
//                routePoints.addAll(newRoutePoints);
//            }
//
//            Log.e("sizes", routePoints.size()+"   "+newRoutePoints.size());
//            if (pickupToDestLine != null) {
//                pickupToDestLine.getPoints().clear();
//                pickupToDestLine.remove();
//
//            }
//            PolylineOptions lineOptions = new PolylineOptions();
//            lineOptions.addAll(routePoints);
//            lineOptions.width(5);
//            lineOptions.color(ContextCompat.getColor(this, R.color.hint_blue));
//            pickupToDestLine = getGoogleMap().addPolyline(lineOptions);
//        }
//        else {
//            if (pickupToDestLine != null) {
//                pickupToDestLine.getPoints().clear();
//                pickupToDestLine.remove();
//
//            }
//            PolylineOptions lineOptions = new PolylineOptions();
//            lineOptions.addAll(routePoints);
//            lineOptions.width(5);
//            lineOptions.color(ContextCompat.getColor(this, R.color.hint_blue));
//            pickupToDestLine = getGoogleMap().addPolyline(lineOptions);
//        }
//
//    }

    protected void hideKeyboard() {
        View focusedView = this.getCurrentFocus();
        if (focusedView != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.tv_order_items:
                toggleOrderItemsDialog();
                break;

            case R.id.iv_back:
                if (fromPushNotification){
                    Intent notificationIntent = new Intent(this, MainActivity.class);
                    notificationIntent.putExtra(Constants.OPEN_FRAGMENT, NotificationFragment.class.getSimpleName());
                    startActivity(notificationIntent);
                    finish();
                }else if(fromCreateOrder){
                    Intent categoriesIntent = new Intent(this, MainActivity.class);
                    categoriesIntent.putExtra(Constants.OPEN_FRAGMENT, CategoriesFragment.class.getSimpleName());
                    categoriesIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(categoriesIntent);
                    finish();
                }
                else {
                    finish();
                }
                break;

            case R.id.iv_close:
                previewPopupWindow.dismiss();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (previewPopupWindow.isShowing()){
            previewPopupWindow.dismiss();
        }
        else if (fromPushNotification){
                Intent notificationIntent = new Intent(this, MainActivity.class);
                notificationIntent.putExtra(Constants.OPEN_FRAGMENT, NotificationFragment.class.getSimpleName());
                startActivity(notificationIntent);
                finish();
        }else if(fromCreateOrder){
            Intent categoriesIntent = new Intent(this, MainActivity.class);
            categoriesIntent.putExtra(Constants.OPEN_FRAGMENT, CategoriesFragment.class.getSimpleName());
            categoriesIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(categoriesIntent);
            finish();
        }
        else {
            super.onBackPressed();
        }
    }


    @Override
    public void onImageClick(ImageSizes orderImage) {
        Glide.with(this).load(orderImage.getBig())
                .apply(new RequestOptions().placeholder(R.drawable.upload_default).dontAnimate()).into(popupPreviewIv);
                //.placeholder(R.drawable.upload_default).dontAnimate().into(popupPreviewIv);
        previewPopupWindow.showAtLocation(mainContentRl, Gravity.CENTER, 0, 0);
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
    public void showPopupLoading() {

    }

    @Override
    public void hidePopupLoading() {

    }

    @Override
    public void showRatePopupLoading() {

    }

    @Override
    public void hideRatePopupLoading() {

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
    public void showScreenNetworkError() {
        mainContentRl.setVisibility(View.GONE);
        noConnectionLl.setVisibility(View.VISIBLE);
    }

    @Override
    public void showToastNetworkError() {

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
    public void showNotAvailableOrderError(String errorMessage) {

    }

    @Override
    public void showAcceptOfferError(String errorMessage) {

    }

    @Override
    public void showOrderDetails(Order order) {
        this.order = order;
        if (!fromChat){
            if (order.getStatus().equals("assigned") || order.getStatus().equals("in_progress") ||
                    order.getStatus().equals("delivery_in_progress")) {
                Intent orderChatIntent = new Intent(this, OrderChatActivity.class);
                orderChatIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                orderChatIntent.putExtra(Constants.ORDER, order);
                orderChatIntent.putExtra(Constants.FROM_CUSTOMER_ORDERS, fromCustomerOrders);
                orderChatIntent.putExtra(Constants.FROM_PUSH_NOTIFICATION, fromPushNotification);
                startActivity(orderChatIntent);
                finish();
            }
            else {
                updateOrderView(order);
            }
        }
        else {
            updateOrderView(order);
        }

    }

    @Override
    public void showFreeCommission(boolean freeCommission) {

    }

    @Override
    public void showMapData(double fromLat, double fromLng, double toLat, double toLng) {
        setMapOrderData(fromLat, fromLng, toLat, toLng);
    }

    @Override
    public void showOrderDirections(Route route, boolean isDelegateDirection) {
        if(order.getStatus().equals("pending") && order.getCreatedBy() != null){
            if (pickupToDestLine != null){
                pickupToDestLine.remove();
            }
            if (route.getRoutePolyLine() != null) {
                setRoute(order.getFromLat(), order.getFromLng(), order.getToLat(), order.getToLng(), route);
            }
        }

        fromToToDistance = calculateDistanceFromLegs(route.getLegs());
        if (fromToToDistance > 0){
            if (fromToToDistance < 1000) {
                totaldistanceTv.setText(String.format(Locale.ENGLISH,"%.1f", fromToToDistance) + " " + getString(R.string.meter));
            } else {
                totaldistanceTv.setText(String.format(Locale.ENGLISH,"%.1f", (fromToToDistance / 1000)) + " " + getString(R.string.kilometer));
            }
        }
        else {
            calculateAbsoluteDistance();
        }

    }

    @Override
    public void showDefaultDistance(boolean isDelegateDirection) {
        calculateAbsoluteDistance();
    }

    @Override
    public void showSuccessAddOffer(Offer offer) {

    }

    @Override
    public void hideTokenLoader() {
        hideLoading();
    }

    @Override
    public void sucessCancel() {

    }

    @Override
    public void showOrderCancelledSucessfully() {

    }

    @Override
    public void showSuccessAcceptOffer(Order order) {

    }

    @Override
    public void showSuccessRejectOffer(Order order) {

    }

    @Override
    public void showSuccessOrderRate(Order order) {

    }

    @Override
    public void showSuccessReorder(Order order) {

    }

    @Override
    public void showDelegateCurrentLocation(double latitude, double longitude) {

        this.delLat = latitude;
        this.delLng = longitude;
//        if (fromToToDistance != 0 && currentPoint != null){
//            fromToToDistance-=SphericalUtil.computeDistanceBetween(currentPoint, new LatLng(latitude, longitude));
//            if (fromToToDistance > 0){
//                if (fromToToDistance < 1000) {
//                    totaldistanceTv.setText(String.format(Locale.ENGLISH,"%.1f", fromToToDistance) + " " + getString(R.string.meter));
//                } else {
//                    totaldistanceTv.setText(String.format(Locale.ENGLISH,"%.1f", (fromToToDistance / 1000)) + " " + getString(R.string.kilometer));
//                }
//            }
//        }

        if (carMarker == null) {
            Log.e("carMarker","carMarker added1");
            carMarker = getGoogleMap().addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).icon(BitmapDescriptorFactory.fromResource(R.drawable.car_purple)));
            currentPoint = new LatLng(latitude, longitude);
        } else {
            updateDelegateLocation();
            currentPoint = new LatLng(latitude, longitude);
        }
//        if (routePoints != null) {
//            if (!PolyUtil.isLocationOnPath(new LatLng(latitude, longitude), routePoints, false, 20)) {
//                drawDelToDestination = true;
//            } else {
//                if (pickupToDestLine != null){
//                    pickupToDestLine.remove();
//                }
//
//                updateRoute();
//            }
//
//        }
//        if (drawDelToDestination) {
//            getOrderDetailsPresenter().getDirections(latitude + "," + longitude, order.getToLat() + "," + order.getToLng(),
//                    "driving", getString(R.string.google_server_key), false);
//            drawDelToDestination = false;
//        }

        if(order.getStatus().equals("delivery_in_progress") && (fromToToDistance == 0 || isStatusChanged)){
            if(isStatusChanged()){
                setStatusChanged(false);
            }
            getOrderDetailsPresenter().getDirections(latitude + "," + longitude, order.getToLat() + "," + order.getToLng(),
                    "driving", getString(R.string.google_server_key), false);
        }

    }
    @Override
    public void showBlockedAreaError() {

    }

    @Override
    public void showCancelationReasons(ArrayList<CancelationReason> cancelationReasons) {

    }

    @Override
    public void showSuccessIgnoreOrder() {

    }

    @Override
    public void showServerErrorMessage(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void saveCustomerTokens(ArrayList<String> tokens) {
        localSettings.setCustomerTokens(tokens);
    }

    @Override
    public void saveDelegateTokens(ArrayList<String> tokens) {
        localSettings.setDelegateTokens(tokens);
    }
}
