package amaz.objects.TwentyfourSeven.ui.Notification;
import android.content.Context;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import amaz.objects.TwentyfourSeven.data.models.Notification;
import amaz.objects.TwentyfourSeven.data.models.User;
import amaz.objects.TwentyfourSeven.data.models.responses.MyNotificationResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.MyNotificationStateResponse;
import amaz.objects.TwentyfourSeven.data.repositories.UserRepository;
import amaz.objects.TwentyfourSeven.listeners.OnResponseListener;
import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import amaz.objects.TwentyfourSeven.utilities.LocalSettings;
import retrofit2.Response;


public class NotificationPresenter  extends BasePresenter {

    private UserRepository userRepository;
    private int page = 1;
    private int limit = 10;
    private ArrayList<Notification> notifications = new ArrayList<>();
    private boolean isLoading = false;
    private boolean moreDataAvailable = true;
    private LocalSettings localSettings;

    private WeakReference<NotificationView> view = new WeakReference<NotificationView>(null);
    public NotificationPresenter(UserRepository repository){
        this.userRepository = repository;
    }

    public void setView(NotificationView view, Context context){
        this.view = new WeakReference<NotificationView>(view);

        localSettings = new LocalSettings(context);
    }

    public void getNotificationList(final String local, String token){
        final NotificationView notificationView = view.get();
        if (!isLoading && moreDataAvailable) {
            isLoading = true;
            if (page == 1) {
                notificationView.showFirstPageLoading();
            }
            userRepository.getNotification(token,local, page, limit, new OnResponseListener() {
                @Override
                public void onSuccess(Response response) {
                    notificationView.hideLoading();
                    if (localSettings.getUser() != null){
                        localSettings.getUser().setUnseen_notifications_count(((MyNotificationResponse) response.body()).getNotificationData().getUnseen_notifications_count());

                    }
                    if (((MyNotificationResponse) response.body()).getNotificationData().getNotification().isEmpty() && page == 1) {
                        notificationView.showNoData();
                    } else {
                        notifications.addAll(((MyNotificationResponse)response.body()).getNotificationData().getNotification());
                        notificationView.setNotifications(notifications);
                        page += 1;
                        if (((MyNotificationResponse) response.body()).getNotificationData().getNotification().size() < limit) {
                            moreDataAvailable = false;
                        }

                    }
                    isLoading = false;

                }

                @Override
                public void onFailure() {
                    notificationView.hideLoading();
                    if (page == 1){
                        notificationView.showScreenNetworkError();
                    }
                    else {
                        notificationView.showToastNetworkError();
                    }
                    isLoading = false;
                }

                @Override
                public void onAuthError() {
                    notificationView.hideLoading();
                    notificationView.showInvalideTokenError();
                    isLoading = false;
                }

                @Override
                public void onInvalidTokenError() {
                    notificationView.hideLoading();
                }

                @Override
                public void onServerError() {
                    notificationView.hideLoading();
                    notificationView.showServerError();
                    isLoading = false;
                }

                @Override
                public void onValidationError(String errorMessage) {
                    notificationView.hideLoading();
                    isLoading = false;
                }

                @Override
                public void onSuspendedUserError(String errorMessage) {
                    notificationView.hideLoading();
                    notificationView.showSuspededUserError(errorMessage);
                    isLoading = false;
                }
            });
        }
    }

    public void changeStateOfUserOfNotification(String token,String local,boolean state  ){
        final NotificationView registerationOrLoginView = view.get();
        registerationOrLoginView.showLoading();

        userRepository.setDeviceStat(token,local , state, new OnResponseListener() {

            @Override
            public void onSuccess(Response response) {
                registerationOrLoginView.hideLoading();
                registerationOrLoginView.setStateChanged(((MyNotificationStateResponse) response.body()).getUser());
            }

            @Override
            public void onFailure() {
                registerationOrLoginView.hideLoading();
                registerationOrLoginView.showToastNetworkError();
            }

            @Override
            public void onAuthError() {
                registerationOrLoginView.hideLoading();
            }

            @Override
            public void onInvalidTokenError() {
                registerationOrLoginView.hideLoading();
            }

            @Override
            public void onServerError() {
                registerationOrLoginView.hideLoading();
                registerationOrLoginView.showServerError();
            }

            @Override
            public void onValidationError(String errorMessage) {
                registerationOrLoginView.hideLoading();
                // registerationOrLoginView.showInvalidCodeError();
            }

            @Override
            public void onSuspendedUserError(String errorMessage) {
                registerationOrLoginView.hideLoading();
                registerationOrLoginView.showSuspededUserError(errorMessage);
            }
        });
    }

    public void markAsRead(String token, final String local ){
        final NotificationView registerationOrLoginView = view.get();
        userRepository.markNotificationAsRead(token,local, new OnResponseListener() {

            @Override
            public void onSuccess(Response response) {
                registerationOrLoginView.hideLoading();
                registerationOrLoginView.markAsRead();
            }

            @Override
            public void onFailure() {
                registerationOrLoginView.hideLoading();
                registerationOrLoginView.showToastNetworkError();
            }

            @Override
            public void onAuthError() {
                registerationOrLoginView.hideLoading();
            }

            @Override
            public void onInvalidTokenError() {
                registerationOrLoginView.hideLoading();
            }

            @Override
            public void onServerError() {
                registerationOrLoginView.hideLoading();
                registerationOrLoginView.showServerError();
            }

            @Override
            public void onValidationError(String errorMessage) {
                registerationOrLoginView.hideLoading();
                // registerationOrLoginView.showInvalidCodeError();
            }

            @Override
            public void onSuspendedUserError(String errorMessage) {
                registerationOrLoginView.hideLoading();
                registerationOrLoginView.showSuspededUserError(errorMessage);
            }
        });
    }

    public void reset(){
        this.page = 1;
        moreDataAvailable = true;
        isLoading = false;
      this.notifications.clear();
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {

    }
    public boolean getMoreDataAvailable(){
        return this.moreDataAvailable;
    }

    public int getPage() {
        return this.page;
    }

    public interface NotificationView{

        void showLoading();
        void hideLoading();
        void showToastNetworkError();
        void showScreenNetworkError();
        void showInvalideTokenError();
        void setNotifications(ArrayList<Notification> notifications);
        void showNoData();
        void showServerError();
        void showSuspededUserError(String errorMessage);
        void setStateChanged(User user);
        void showFirstPageLoading();
        void markAsRead();

    }
}
