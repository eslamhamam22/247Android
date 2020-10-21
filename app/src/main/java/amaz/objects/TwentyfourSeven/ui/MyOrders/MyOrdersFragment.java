package amaz.objects.TwentyfourSeven.ui.MyOrders;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.rey.material.widget.ProgressView;

import org.zakariya.stickyheaders.StickyHeaderLayoutManager;

import java.util.ArrayList;

import amaz.objects.TwentyfourSeven.BaseFragment;
import amaz.objects.TwentyfourSeven.MApplication;
import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.adapters.NotificationsAdapter;
import amaz.objects.TwentyfourSeven.adapters.OrdersAdapter;
import amaz.objects.TwentyfourSeven.data.models.DelegateActivationRequest;
import amaz.objects.TwentyfourSeven.data.models.Notification;
import amaz.objects.TwentyfourSeven.data.models.Order;
import amaz.objects.TwentyfourSeven.data.models.OrdersSection;
import amaz.objects.TwentyfourSeven.data.models.User;
import amaz.objects.TwentyfourSeven.injection.Injection;
import amaz.objects.TwentyfourSeven.listeners.OnLocationChangeListener;
import amaz.objects.TwentyfourSeven.listeners.OnNotificationClickListnner;
import amaz.objects.TwentyfourSeven.listeners.OnOrderClickListener;
import amaz.objects.TwentyfourSeven.listeners.OnRefeshTokenResponse;
import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import amaz.objects.TwentyfourSeven.presenter.PresenterFactory;
import amaz.objects.TwentyfourSeven.ui.AddMoney.AddMoneyActivity;
import amaz.objects.TwentyfourSeven.ui.MyAccount.MainActivity;
import amaz.objects.TwentyfourSeven.ui.RegisterOrLogin.RegisterOrLoginActivity;
import amaz.objects.TwentyfourSeven.utilities.Constants;
import amaz.objects.TwentyfourSeven.utilities.Fonts;
import amaz.objects.TwentyfourSeven.utilities.GPSTracker;
import amaz.objects.TwentyfourSeven.utilities.LanguageUtilities;
import amaz.objects.TwentyfourSeven.utilities.LocalSettings;
import amaz.objects.TwentyfourSeven.utilities.TokenUtilities;
import amaz.objects.TwentyfourSeven.utilities.tracking_module.TrackingService;

public class MyOrdersFragment extends BaseFragment implements MyOrdersPresenter.MyOrderView, View.OnClickListener,
        OnRefeshTokenResponse, OnOrderClickListener, OnLocationChangeListener {

    private RecyclerView orderaRecycleView, requestRecycleView;
    private OrdersAdapter adapter, requestAdapter;
    private TextView title, tv_switch_notification, tv_my_orders_tab, tv_my_delivery_tab, noConnectionTitleTv, noConnectionMessageTv,
            tv_title_max_reach, tv_max_reach, tv_add_money;
    private Fonts fonts;
    private MyOrdersPresenter myOrdersPresenter;
    private LocalSettings localSettings;
    private ProgressBar loadMore;
    private ArrayList<OrdersSection> ordersSections = new ArrayList<>();
    private ArrayList<OrdersSection> requestSections = new ArrayList<>();

    private ProgressView loaderPv;
    private boolean fromSwitch = true;
    private RelativeLayout errorRl, my_orders_tab, my_request_tab, switchLayout;
    private ImageView errorIv;
    private TextView errorTv;
    private boolean firstTime = false;
    private Switch turn_request_switch;
    private StickyHeaderLayoutManager manager, manager2;
    private LinearLayout li_tabs, addMoney_linear;
    private View selected_delivery_view, selected_order_view;
    private LinearLayout noConnectionLl;
    private Button reloadBtn;
    private ArrayList<Order> currentOrder = new ArrayList<>();
    private GPSTracker tracker;

    private boolean isCustomerOrders = true;
    private boolean isselectSecondTab = false;


    @Override
    public PresenterFactory.PresenterType getPresenterType() {
        return PresenterFactory.PresenterType.MyOrder;
    }

    @Override
    public void onPresenterAvailable(BasePresenter presenter) {
        myOrdersPresenter = (MyOrdersPresenter) presenter;
        myOrdersPresenter.setView(this);
        if (isCustomerOrders) {
            myOrdersPresenter.getCurrentOrders(localSettings.getLocale(), localSettings.getToken());
        } else {
            myOrdersPresenter.getCurrentRequest(localSettings.getLocale(), localSettings.getToken());
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_orders, container, false);
        localSettings = new LocalSettings(getActivity());
        setLanguage();
        initialization(view);
        setFonts();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setLanguage();
    }

    private void initialization(View view) {
        title = (TextView) view.findViewById(R.id.tv_my_order_title);
        tv_switch_notification = (TextView) view.findViewById(R.id.tv_switch_notification);
        orderaRecycleView = (RecyclerView) view.findViewById(R.id.rv_current_order);
        requestRecycleView = (RecyclerView) view.findViewById(R.id.rv_request_order);
        li_tabs = view.findViewById(R.id.li_tabs);
        addMoney_linear = view.findViewById(R.id.addMoney_linear);
        tv_title_max_reach = view.findViewById(R.id.tv_title_max_reach);
        tv_max_reach = view.findViewById(R.id.tv_max_reach);
        tv_add_money = view.findViewById(R.id.tv_add_money);
        my_orders_tab = view.findViewById(R.id.my_orders_tab);
        my_request_tab = view.findViewById(R.id.my_request_tab);
        tv_my_orders_tab = view.findViewById(R.id.tv_my_orders_tab);
        selected_order_view = view.findViewById(R.id.selected_order_view);
        tv_my_delivery_tab = view.findViewById(R.id.tv_my_delivery_tab);
        selected_delivery_view = view.findViewById(R.id.selected_delivery_view);
        noConnectionLl = view.findViewById(R.id.ll_no_connection);

        switchLayout = view.findViewById(R.id.rl_title);
        noConnectionTitleTv = view.findViewById(R.id.tv_no_connection_title);
        noConnectionMessageTv = view.findViewById(R.id.tv_no_connection_message);
        reloadBtn = view.findViewById(R.id.btn_reload_page);
        reloadBtn.setOnClickListener(this);
        my_request_tab.setOnClickListener(this);
        my_orders_tab.setOnClickListener(this);
        tv_add_money.setOnClickListener(this);

        fonts = MApplication.getInstance().getFonts();
        turn_request_switch = view.findViewById(R.id.turn_request_switch);
        loadMore = (ProgressBar) view.findViewById(R.id.load_more);
        tracker = new GPSTracker(getActivity(), this, false);

        manager = new StickyHeaderLayoutManager();
        manager2 = new StickyHeaderLayoutManager();

        orderaRecycleView.setLayoutManager(manager);
        requestRecycleView.setLayoutManager(manager2);

        adapter = new OrdersAdapter(getActivity(), ordersSections, this);
        orderaRecycleView.setAdapter(adapter);

        requestAdapter = new OrdersAdapter(getActivity(), requestSections, this);
        requestRecycleView.setAdapter(requestAdapter);
        orderaRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(0) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (myOrdersPresenter.getMoreDataAvailable()) {
                        if (manager.getFirstVisibleHeaderViewHolder(true) != null && myOrdersPresenter.getPage() != 1) {
                            int currentVisibleHeader = manager.getFirstVisibleHeaderViewHolder(true).getSection();
                            if (currentVisibleHeader == 1) {
                                loadMore.setVisibility(View.VISIBLE);
                                myOrdersPresenter.getHistoryUserOrders(localSettings.getLocale(), localSettings.getToken());

                            }
                        }
                    }
                }
            }
        });
        requestRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(0) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (myOrdersPresenter.getMoreRequestDataAvailable() && myOrdersPresenter.getRequestPage() != 1) {
                        if (manager.getFirstVisibleHeaderViewHolder(true) != null) {
                            int currentVisibleHeader = manager2.getFirstVisibleHeaderViewHolder(true).getSection();
                            if (currentVisibleHeader == 1) {
                                loadMore.setVisibility(View.VISIBLE);
                                myOrdersPresenter.getHistoryDelegateOrders(localSettings.getLocale(), localSettings.getToken());
                            }
                        }
                    }
                }
            }
        });

        loaderPv = view.findViewById(R.id.pv_load);
        errorRl = view.findViewById(R.id.rl_error);
        errorIv = view.findViewById(R.id.iv_error);
        errorTv = view.findViewById(R.id.tv_error);

        if (localSettings.getUser().isDelegate()) {
            li_tabs.setVisibility(View.VISIBLE);

            switchLayout.setVisibility(View.GONE);
            turn_request_switch.setChecked(localSettings.getUser().getIsActiveDelegate().getActive());
        } else {

            li_tabs.setVisibility(View.GONE);
            switchLayout.setVisibility(View.GONE);
        }

        turn_request_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (turn_request_switch.isChecked()) {
                    if (tracker.isRequestPermission()) {
                        if (tracker.isLocationAvailable()) {
                            myOrdersPresenter.changeStateOfUserRequest(localSettings.getToken(), localSettings.getLocale(), turn_request_switch.isChecked());
                        } else {
                            turn_request_switch.setChecked(!turn_request_switch.isChecked());
                            Toast.makeText(getActivity(), R.string.activate_gps_message, Toast.LENGTH_LONG).show();

                        }
                    } else {
                        turn_request_switch.setChecked(!turn_request_switch.isChecked());
                    }
                } else {
                    myOrdersPresenter.changeStateOfUserRequest(localSettings.getToken(), localSettings.getLocale(), turn_request_switch.isChecked());

                }
            }

        });


    }

    private void setFonts() {
        tv_switch_notification.setTypeface(fonts.customFont());
        errorTv.setTypeface(fonts.customFont());
        title.setTypeface(fonts.customFontBD());
        tv_my_orders_tab.setTypeface(fonts.customFont());
        tv_my_delivery_tab.setTypeface(fonts.customFont());
        noConnectionTitleTv.setTypeface(fonts.customFontBD());
        noConnectionMessageTv.setTypeface(fonts.customFont());
        reloadBtn.setTypeface(fonts.customFontBD());
        tv_title_max_reach.setTypeface(fonts.customFontBD());
        tv_max_reach.setTypeface(fonts.customFont());
        tv_add_money.setTypeface(fonts.customFont());

        errorTv.setTypeface(fonts.customFont());
    }

    private void setLanguage() {
        if (!MyOrdersFragment.this.isDetached() && getActivity() != null) {

            if (localSettings.getLocale().equals(Constants.ARABIC)) {
                LanguageUtilities.switchToArabicLocale(getActivity());
            }
        }

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.my_orders_tab:
                isCustomerOrders = true;
                requestRecycleView.setVisibility(View.GONE);
                orderaRecycleView.setVisibility(View.VISIBLE);
                selected_delivery_view.setVisibility(View.GONE);
                selected_order_view.setVisibility(View.VISIBLE);
//                switchLayout.animate()
//                        .translationY(0)
//                        .alpha(0.0f)
//                        .setListener(new AnimatorListenerAdapter() {
//                            @Override
//                            public void onAnimationEnd(Animator animation) {
//                                super.onAnimationEnd(animation);
//                                switchLayout.setVisibility(View.GONE);
//                            }
//                        });
                switchLayout.setVisibility(View.GONE);
                addMoney_linear.setVisibility(View.GONE);

                tv_my_orders_tab.setTextColor(getResources().getColor(R.color.black));
                tv_my_delivery_tab.setTextColor(getResources().getColor(R.color.line_gray));
                if (ordersSections.size() == 0) {
                    myOrdersPresenter.getCurrentOrders(localSettings.getLocale(), localSettings.getToken());
                }

                break;
            case R.id.my_request_tab:
                selectDeliveryRequestTab();
            case R.id.btn_reload_page:
                if (li_tabs.getVisibility() == View.VISIBLE) {
                    if (selected_order_view.getVisibility() == View.VISIBLE) {
                        myOrdersPresenter.getCurrentOrders(localSettings.getLocale(), localSettings.getToken());

                    } else {
                        myOrdersPresenter.getCurrentRequest(localSettings.getLocale(), localSettings.getToken());

                    }
                } else {
                    myOrdersPresenter.getCurrentOrders(localSettings.getLocale(), localSettings.getToken());

                }
                break;
            case R.id.tv_add_money:
                if (!MyOrdersFragment.this.isDetached() && getActivity() != null) {

                    Intent intent = new Intent(getActivity(), AddMoneyActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

    private void selectDeliveryRequestTab() {
        if (!MyOrdersFragment.this.isDetached() && getActivity() != null) {

            isCustomerOrders = false;
            requestRecycleView.setVisibility(View.VISIBLE);
            orderaRecycleView.setVisibility(View.GONE);
            selected_delivery_view.setVisibility(View.VISIBLE);
            selected_order_view.setVisibility(View.GONE);
            switchLayout.setVisibility(View.VISIBLE);
            double value_pos = -1 * localSettings.getUser().getWallet_value();
            if (value_pos >= localSettings.getMaxMinusAmount()) {
                addMoney_linear.setVisibility(View.VISIBLE);
            } else {
                addMoney_linear.setVisibility(View.GONE);

            }

            tv_my_orders_tab.setTextColor(getResources().getColor(R.color.line_gray));
            tv_my_delivery_tab.setTextColor(getResources().getColor(R.color.black));

            if (requestSections.size() == 0) {
                myOrdersPresenter.getCurrentRequest(localSettings.getLocale(), localSettings.getToken());
            }
        }
    }

    @Override
    public void showFirstPageLoading() {

        if (!MyOrdersFragment.this.isDetached() && getActivity() != null) {
            //  this.ordersSections.clear();
            loaderPv.setVisibility(View.VISIBLE);
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }

    }

    @Override
    public void showNoCurrentData() {
        if (!MyOrdersFragment.this.isDetached() && getActivity() != null) {

        }
    }

    @Override
    public void showNoHistoryData() {

    }

    @Override
    public void showLoading() {
        if (!MyOrdersFragment.this.isDetached() && getActivity() != null) {
            loaderPv.setVisibility(View.VISIBLE);
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }

    }

    @Override
    public void hideLoading() {
        if (!MyOrdersFragment.this.isDetached() && getActivity() != null) {
            loaderPv.setVisibility(View.GONE);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    @Override
    public void showInvalideTokenError() {
        if (!MyOrdersFragment.this.isDetached() && getActivity() != null) {
            Log.e("authError", "authentication error");
            TokenUtilities.refreshToken(getActivity(),
                    Injection.provideUserRepository(),
                    localSettings.getToken(),
                    localSettings.getLocale(),
                    localSettings.getRefreshToken(),
                    this);
        }
    }

    @Override
    public void setOrders(ArrayList<Order> currentOrders, ArrayList<Order> historyOrders) {
        if (!MyOrdersFragment.this.isDetached() && getActivity() != null) {

            if (loadMore.getVisibility() == View.VISIBLE) {
                loadMore.setVisibility(View.GONE);
            }
            if (errorRl.getVisibility() == View.VISIBLE) {
                errorRl.setVisibility(View.GONE);
            }
            noConnectionLl.setVisibility(View.GONE);

            this.currentOrder.clear();
            this.currentOrder.addAll(currentOrders);
            //    Order order = new Order();

            //    ArrayList<Order> orders = new ArrayList<>();
            // orders.add(order);
            OrdersSection currentSection = new OrdersSection();
            currentSection.setSectionTitle(getResources().getString(R.string.current_request));
            currentSection.setSectionProducts(currentOrders);

            OrdersSection historySection = new OrdersSection();
            historySection.setSectionTitle(getResources().getString(R.string.history));
            historySection.setSectionProducts(historyOrders);

            this.ordersSections.clear();
            this.ordersSections.add(currentSection);
            this.ordersSections.add(historySection);
            //  orderaRecycleView.getRecycledViewPool().clear();
            adapter.notifyAllSectionsDataSetChanged();
            // adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setRequests(ArrayList<Order> currentOrders, ArrayList<Order> historyOrders) {
        if (!MyOrdersFragment.this.isDetached() && getActivity() != null) {

            if (loadMore.getVisibility() == View.VISIBLE) {
                loadMore.setVisibility(View.GONE);
            }
            if (errorRl.getVisibility() == View.VISIBLE) {
                errorRl.setVisibility(View.GONE);
            }
            noConnectionLl.setVisibility(View.GONE);

            OrdersSection currentSection = new OrdersSection();
            currentSection.setSectionTitle(getResources().getString(R.string.current_request));
            currentSection.setSectionProducts(currentOrders);

            OrdersSection historySection = new OrdersSection();
            historySection.setSectionTitle(getResources().getString(R.string.history));
            historySection.setSectionProducts(historyOrders);
            this.currentOrder.clear();
            this.currentOrder.addAll(currentOrders);
            this.requestSections.clear();
            this.requestSections.add(currentSection);
            this.requestSections.add(historySection);
            //  requestRecycleView.getRecycledViewPool().clear();

            requestAdapter.notifyAllSectionsDataSetChanged();
            // adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showNetworkViewError() {
        if (!MyOrdersFragment.this.isDetached() && getActivity() != null) {

            noConnectionLl.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showNetworkError() {
        if (!MyOrdersFragment.this.isDetached() && getActivity() != null) {

            Toast.makeText(getActivity(), R.string.connection_error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void showNoData() {
        errorRl.setVisibility(View.VISIBLE);
        errorIv.setImageResource(R.drawable.grayscale);
        errorTv.setText(R.string.no_data_error);
    }

    @Override
    public void showServerError() {
        if (!MyOrdersFragment.this.isDetached() && getActivity() != null) {

            Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void showSuspededUserError(String errorMessage) {
        if (!MyOrdersFragment.this.isDetached() && getActivity() != null) {

            Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
            localSettings.removeToken();
            localSettings.removeRefreshToken();
            localSettings.removeUser();
            switchToRegisterationOrLogin();
        }
    }


    private void switchToRegisterationOrLogin() {
        if (!MyOrdersFragment.this.isDetached() && getActivity() != null) {

            Intent registerationOrLoginIntent = new Intent(getActivity(), RegisterOrLoginActivity.class);
            registerationOrLoginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(registerationOrLoginIntent);
        }
    }

    @Override
    public void setStateChanged(Boolean active) {
        User user = localSettings.getUser();
        DelegateActivationRequest delegateActivationRequest = new DelegateActivationRequest();
        delegateActivationRequest.setActive(active);
        user.setIsActiveDelegate(delegateActivationRequest);
        localSettings.setUser(user);
        if (localSettings.getUser().isDelegate() && localSettings.getUser().getIsActiveDelegate().getActive()) {
//            if (!MyOrdersFragment.this.isDetached() && getActivity() != null) {
//                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
//                        && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                    ((MainActivity)getActivity()).checkLocationSettings();
//                }
//            }
            if (getActivity() != null) {
                if (!((MainActivity)getActivity()).isTrackingServiceRunning()) {
                    ((MainActivity) getActivity()).startTracking();
                }
            }
        } else if (!localSettings.getUser().getIsActiveDelegate().getActive()) {
            if (getActivity() != null) {
                if (((MainActivity)getActivity()).isTrackingServiceRunning()) {
                    ((MainActivity) getActivity()).stopTracking();
                }

            }
        }
    }

    @Override
    public void setUnSucessChangesStatus() {
        if (!MyOrdersFragment.this.isDetached() && getActivity() != null) {
            turn_request_switch.setChecked(localSettings.getUser().getIsActiveDelegate().getActive());
        }


    }

    @Override
    public void hideTokenLoader() {
        if (!MyOrdersFragment.this.isDetached() && getActivity() != null) {
            hideLoading();
        }

    }

    public void resetAfterNotification() {
        if (localSettings.getUser().isDelegate()) {

            li_tabs.setVisibility(View.VISIBLE);
            // switchLayout.setVisibility(View.GONE);
            turn_request_switch.setChecked(localSettings.getUser().getIsActiveDelegate().getActive());

        } else {
            li_tabs.setVisibility(View.GONE);
            switchLayout.setVisibility(View.GONE);


        }

        requestSections.clear();
        ordersSections.clear();
        myOrdersPresenter.reset();
        if (li_tabs.getVisibility() == View.VISIBLE) {
            if (selected_order_view.getVisibility() == View.VISIBLE) {
                myOrdersPresenter.getCurrentOrders(localSettings.getLocale(), localSettings.getToken());

            } else {
                myOrdersPresenter.getCurrentRequest(localSettings.getLocale(), localSettings.getToken());

            }
        } else {
            myOrdersPresenter.getCurrentOrders(localSettings.getLocale(), localSettings.getToken());

        }
    }

    @Override
    public void onOrderClick(Order order) {
        if (!MyOrdersFragment.this.isDetached() && getActivity() != null) {
            ((MainActivity) getActivity()).switchToOrderDetails(order, isCustomerOrders);
        }
    }

    @Override
    public void onShowMore() {
        if (!MyOrdersFragment.this.isDetached() && getActivity() != null) {
            Intent intent = new Intent(getActivity(), MyCurrentOrdersActivity.class);
            intent.putExtra(Constants.ORDERS, currentOrder);
            startActivity(intent);
        }
    }

    @Override
    public void setAddressData(String countryNameCode, String city, double latitude, double longitude) {

    }

    @Override
    public void openLocationSettings() {

    }

    @Override
    public void showActivateGPS() {

    }
}
