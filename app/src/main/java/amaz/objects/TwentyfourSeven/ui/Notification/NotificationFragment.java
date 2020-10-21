package amaz.objects.TwentyfourSeven.ui.Notification;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
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

import java.util.ArrayList;

import amaz.objects.TwentyfourSeven.BaseFragment;
import amaz.objects.TwentyfourSeven.MApplication;
import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.adapters.NotificationsAdapter;
import amaz.objects.TwentyfourSeven.data.models.Notification;
import amaz.objects.TwentyfourSeven.data.models.User;
import amaz.objects.TwentyfourSeven.injection.Injection;
import amaz.objects.TwentyfourSeven.listeners.OnNotificationClickListnner;
import amaz.objects.TwentyfourSeven.listeners.OnRefeshTokenResponse;
import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import amaz.objects.TwentyfourSeven.presenter.PresenterFactory;
import amaz.objects.TwentyfourSeven.ui.MyAccount.MainActivity;
import amaz.objects.TwentyfourSeven.ui.MyOrders.MyOrdersFragment;
import amaz.objects.TwentyfourSeven.ui.RegisterOrLogin.RegisterOrLoginActivity;
import amaz.objects.TwentyfourSeven.utilities.Constants;
import amaz.objects.TwentyfourSeven.utilities.Fonts;
import amaz.objects.TwentyfourSeven.utilities.LanguageUtilities;
import amaz.objects.TwentyfourSeven.utilities.LocalSettings;
import amaz.objects.TwentyfourSeven.utilities.TokenUtilities;

public class NotificationFragment extends BaseFragment implements NotificationPresenter.NotificationView, View.OnClickListener,
        OnNotificationClickListnner, OnRefeshTokenResponse {

    //private RelativeLayout mainContentRl;
    private RecyclerView notificationsRecycleView;
    private NotificationsAdapter adapter;
    private TextView title, tv_switch_notification;
    private Fonts fonts;
    private NotificationPresenter notificationPresenter;
    private LocalSettings localSettings;
    private ProgressBar loadMore;
    private ArrayList<Notification> notifications = new ArrayList<>();
    private Notification selectedNotification;
    private ProgressView loaderPv;

    private RelativeLayout errorRl;
    private ImageView errorIv;
    private TextView errorTv;

    private TextView noConnectionTitleTv, noConnectionMessageTv;
    private LinearLayout noConnectionLl;
    private Button reloadBtn;

    private boolean firstTime = false;
    private ImageView receive_notification_switch;
    private boolean isChecked = true;

    @Override
    public PresenterFactory.PresenterType getPresenterType() {
        return PresenterFactory.PresenterType.Notification;
    }

    @Override
    public void onPresenterAvailable(BasePresenter presenter) {
        if (!NotificationFragment.this.isDetached() && getActivity() != null) {
            notificationPresenter = (NotificationPresenter) presenter;
            notificationPresenter.setView(this, getActivity());
            if (errorRl.getVisibility() == View.VISIBLE) {
                errorRl.setVisibility(View.GONE);
            }
            if (notifications.isEmpty()){
                notificationPresenter.getNotificationList(localSettings.getLocale(), localSettings.getToken());
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_notification, container, false);
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
        title = (TextView) view.findViewById(R.id.tv_my_notification_title);
        //mainContentRl = view.findViewById(R.id.rl_main_content);
        //mainContentRl.setVisibility(View.GONE);
        tv_switch_notification = (TextView) view.findViewById(R.id.tv_switch_notification);
        notificationsRecycleView = (RecyclerView) view.findViewById(R.id.notifications_list);
        fonts = MApplication.getInstance().getFonts();
        receive_notification_switch = view.findViewById(R.id.turn_notification_switch);
        loadMore = (ProgressBar) view.findViewById(R.id.load_more);
        notificationsRecycleView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        adapter = new NotificationsAdapter(getActivity(), notifications, this);
        notificationsRecycleView.setAdapter(adapter);
        // notifications.clear();
        notificationsRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(0) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (notificationPresenter.getMoreDataAvailable() && notificationPresenter.getPage() != 1) {
                        // if (firstTime) {
                        loadMore.setVisibility(View.VISIBLE);
                        notificationPresenter.getNotificationList(localSettings.getLocale(), localSettings.getToken());
                        // }
                        // firstTime = true;
                    }
                }
            }
        });

        loaderPv = view.findViewById(R.id.pv_load);

        errorRl = view.findViewById(R.id.rl_error);
        errorIv = view.findViewById(R.id.iv_error);
        errorTv = view.findViewById(R.id.tv_error);

        noConnectionLl = view.findViewById(R.id.ll_no_connection);
        noConnectionTitleTv = view.findViewById(R.id.tv_no_connection_title);
        noConnectionMessageTv = view.findViewById(R.id.tv_no_connection_message);
        reloadBtn = view.findViewById(R.id.btn_reload_page);
        reloadBtn.setOnClickListener(this);

        if (localSettings.getUser().isNotifications_enabled()) {
            receive_notification_switch.setImageResource(R.drawable.switch_on);
            isChecked = true;
        } else {
            receive_notification_switch.setImageResource(R.drawable.switch_off);
            isChecked = false;


        }
        receive_notification_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                notificationPresenter.changeStateOfUserOfNotification(localSettings.getToken(), localSettings.getLocale(), !isChecked);

            }
        });
    }

    private void setFonts() {
        tv_switch_notification.setTypeface(fonts.customFont());
        errorTv.setTypeface(fonts.customFont());
        title.setTypeface(fonts.customFont());

        noConnectionTitleTv.setTypeface(fonts.customFontBD());
        noConnectionMessageTv.setTypeface(fonts.customFont());
        reloadBtn.setTypeface(fonts.customFontBD());
    }

    private void setLanguage() {
        if (!NotificationFragment.this.isDetached() && getActivity() != null) {

            if (localSettings.getLocale().equals(Constants.ARABIC)) {
                LanguageUtilities.switchToArabicLocale(getActivity());
            }
        }

    }

    public void resetLists() {

        if (!NotificationFragment.this.isDetached() && getActivity() != null) {
            if(notificationPresenter != null){
                notificationPresenter.reset();
                this.notifications.clear();
                adapter.notifyDataSetChanged();
                notificationPresenter.getNotificationList(localSettings.getLocale(), localSettings.getToken());
            }

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_reload_page:
                notificationPresenter.getNotificationList(localSettings.getLocale(), localSettings.getToken());
                break;
        }
    }


    @Override
    public void showFirstPageLoading() {
        if (!NotificationFragment.this.isDetached() && getActivity() != null) {
            this.notifications.clear();
            loaderPv.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void showLoading() {
        if (!NotificationFragment.this.isDetached() && getActivity() != null) {
            loaderPv.setVisibility(View.VISIBLE);
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }

    }

    @Override
    public void hideLoading() {
        if (!NotificationFragment.this.isDetached() && getActivity() != null) {
            loaderPv.setVisibility(View.GONE);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    @Override
    public void showInvalideTokenError() {
        if (!NotificationFragment.this.isDetached() && getActivity() != null) {
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
    public void setNotifications(ArrayList<Notification> notifications) {
        if (!NotificationFragment.this.isDetached() && getActivity() != null) {
            //mainContentRl.setVisibility(View.VISIBLE);

            if (loadMore.getVisibility() == View.VISIBLE) {
                loadMore.setVisibility(View.GONE);
            }
            if (errorRl.getVisibility() == View.VISIBLE) {
                errorRl.setVisibility(View.GONE);
            }

            if (noConnectionLl.getVisibility() == View.VISIBLE) {
                noConnectionLl.setVisibility(View.GONE);
            }
//            ArrayList<Notification> tempElements = new ArrayList<Notification>();
//            tempElements.addAll(notifications);
//            Collections.reverse(tempElements);
            this.notifications.clear();
            adapter.notifyDataSetChanged();

            this.notifications.addAll(notifications);
            adapter.notifyDataSetChanged();

            notificationPresenter.markAsRead(localSettings.getToken(), localSettings.getLocale());
        }
    }


    @Override
    public void showToastNetworkError() {
        if (!NotificationFragment.this.isDetached() && getActivity() != null) {
            if (loadMore.getVisibility() == View.VISIBLE) {
                loadMore.setVisibility(View.GONE);
            }
            Toast.makeText(getActivity(), R.string.connection_error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void showScreenNetworkError() {
        if (!NotificationFragment.this.isDetached() && getActivity() != null) {
            noConnectionLl.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showNoData() {
        if (!NotificationFragment.this.isDetached() && getActivity() != null) {
            if (noConnectionLl.getVisibility() == View.VISIBLE) {
                noConnectionLl.setVisibility(View.GONE);
            }

            errorRl.setVisibility(View.VISIBLE);
            errorIv.setImageResource(R.drawable.grayscale);
            errorTv.setText(R.string.no_data_error);
        }
    }

    @Override
    public void showServerError() {
        if (!NotificationFragment.this.isDetached() && getActivity() != null) {
            if (loadMore.getVisibility() == View.VISIBLE) {
                loadMore.setVisibility(View.GONE);
            }
            Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void showSuspededUserError(String errorMessage) {
        if (!NotificationFragment.this.isDetached() && getActivity() != null) {

            Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
            localSettings.removeToken();
            localSettings.removeRefreshToken();
            localSettings.removeUser();
            switchToRegisterationOrLogin();
        }
    }


    @Override
    public void onNotificationClicked(Notification notification) {
        if (!NotificationFragment.this.isDetached() && getActivity() != null) {
            if (notification.link_to.equals("account")) {
                ((MainActivity) getActivity()).switchToMyAccount();
            } else if (notification.link_to.equals("orders")) {
                ((MainActivity) getActivity()).switchToMyOrders();
            } else if (notification.link_to.equals("delegate_order_details")) {
                ((MainActivity) getActivity()).switchToDelegateOrderDetails(notification.order.getId());
            } else if (notification.link_to.equals("order_details")) {
                ((MainActivity) getActivity()).switchToCustomerOrderDetails(notification.order.getId());
            }
        }
    }

    private void switchToRegisterationOrLogin() {
        if (!NotificationFragment.this.isDetached() && getActivity() != null) {

            Intent registerationOrLoginIntent = new Intent(getActivity(), RegisterOrLoginActivity.class);
            registerationOrLoginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(registerationOrLoginIntent);
        }
    }

    @Override
    public void setStateChanged(User user) {

        localSettings.setUser(user);
        if (localSettings.getUser().isNotifications_enabled()) {
            receive_notification_switch.setImageResource(R.drawable.switch_on);
            isChecked = true;
        } else {
            receive_notification_switch.setImageResource(R.drawable.switch_off);
            isChecked = false;


        }
    }

    @Override
    public void markAsRead() {
        User user = localSettings.getUser();
        user.setUnseen_notifications_count(0);
        localSettings.setIsHAsNotificationUnSeen(false);
        localSettings.setUser(user);
    }

    @Override
    public void hideTokenLoader() {
        hideLoading();
    }
}
