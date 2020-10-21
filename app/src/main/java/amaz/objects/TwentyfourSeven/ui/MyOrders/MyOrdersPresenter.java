package amaz.objects.TwentyfourSeven.ui.MyOrders;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import amaz.objects.TwentyfourSeven.data.models.Notification;
import amaz.objects.TwentyfourSeven.data.models.Order;
import amaz.objects.TwentyfourSeven.data.models.User;
import amaz.objects.TwentyfourSeven.data.models.responses.MyDelegateStateResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.MyNotificationResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.MyNotificationStateResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.MyOrdersResponse;
import amaz.objects.TwentyfourSeven.data.repositories.OrderRepository;
import amaz.objects.TwentyfourSeven.data.repositories.UserRepository;
import amaz.objects.TwentyfourSeven.listeners.OnResponseListener;
import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import retrofit2.Response;


public class MyOrdersPresenter extends BasePresenter {

    private UserRepository userRepository;
    private int page = 1, pageRequest=1;
    private int limit = 10;
    private ArrayList<Order> userCurrentOrders = new ArrayList<>();
    private ArrayList<Order> currentRequest = new ArrayList<>();
    ArrayList<Order> historyUserOrders = new ArrayList<>();
    ArrayList<Order> historyDelegateRequests = new ArrayList<>();
    private boolean isLoading = false;
    private boolean moreDataAvailable = true,moreRequestDataAvailable = true;

    private WeakReference<MyOrderView> view = new WeakReference<MyOrderView>(null);
    public MyOrdersPresenter(UserRepository userRepository){

        this.userRepository = userRepository;
    }

    public void setView(MyOrderView view){
        this.view = new WeakReference<MyOrderView>(view);
    }

    public void getCurrentOrders(final String local, final String token){
        final MyOrderView notificationView = view.get();
           notificationView.showFirstPageLoading();

            userRepository.getCurrentUserOrder(token,local, 1, limit, new OnResponseListener() {
                @Override
                public void onSuccess(Response response) {
                    if (((MyOrdersResponse) response.body()).getOrders().isEmpty()) {
                        notificationView.showNoCurrentData();
                        getHistoryUserOrders(local,token);

                    } else {
                        userCurrentOrders.clear();
                        userCurrentOrders.addAll(((MyOrdersResponse) response.body()).getOrders());
                     getHistoryUserOrders(local,token);


                    }

                }

                @Override
                public void onFailure() {
                    notificationView.hideLoading();
                    notificationView.showNetworkViewError();


                }

                @Override
                public void onAuthError() {
                    notificationView.hideLoading();
                    notificationView.showInvalideTokenError();
                }

                @Override
                public void onInvalidTokenError() {
                    notificationView.hideLoading();
                }

                @Override
                public void onServerError() {
                    notificationView.hideLoading();
                    notificationView.showServerError();
                }

                @Override
                public void onValidationError(String errorMessage) {
                    notificationView.hideLoading();
                }

                @Override
                public void onSuspendedUserError(String errorMessage) {
                    notificationView.hideLoading();
                    notificationView.showSuspededUserError(errorMessage);
                }
            });

    }
    public void getHistoryUserOrders(String local,String token){
        final MyOrderView notificationView = view.get();
        if (!isLoading && moreDataAvailable) {
            isLoading = true;

            userRepository.getHistoryUserOrder(token,local, page, limit, new OnResponseListener() {
                @Override
                public void onSuccess(Response response) {
                    notificationView.hideLoading();
                    if (((MyOrdersResponse) response.body()).getOrders().isEmpty() && page == 1) {
                        notificationView.setOrders(userCurrentOrders,((MyOrdersResponse) response.body()).getOrders());

//                        if(userCurrentOrders.size()  == 0) {
//                            notificationView.showNoData();
//
//                        }else {
                            notificationView.showNoHistoryData();
                //        }

                    } else {
                        historyUserOrders.addAll(((MyOrdersResponse) response.body()).getOrders());

                        notificationView.setOrders(userCurrentOrders,historyUserOrders);
                       page += 1;
                        if (((MyOrdersResponse) response.body()).getOrders().size() < limit) {
                            moreDataAvailable = false;
                        }

                    }
                    isLoading = false;

                }

                @Override
                public void onFailure() {
                    notificationView.hideLoading();
                    if (page == 1) {
                        notificationView.showNetworkViewError();
                    }else{
                        notificationView.showNetworkError();

                    }


                }

                @Override
                public void onAuthError() {
                    notificationView.hideLoading();
                    notificationView.showInvalideTokenError();
                }

                @Override
                public void onInvalidTokenError() {
                    notificationView.hideLoading();
                }

                @Override
                public void onServerError() {
                    notificationView.hideLoading();
                    notificationView.showServerError();
                }

                @Override
                public void onValidationError(String errorMessage) {
                    notificationView.hideLoading();
                }

                @Override
                public void onSuspendedUserError(String errorMessage) {
                    notificationView.hideLoading();
                    notificationView.showSuspededUserError(errorMessage);
                }
            });
        }else{
            notificationView.hideLoading();

        }
    }

    public void getCurrentRequest(final String local, final String token){
        final MyOrderView notificationView = view.get();
        notificationView.showFirstPageLoading();

        userRepository.getCurrentDelegateOrder(token,local, 1, 10, new OnResponseListener() {
            @Override
            public void onSuccess(Response response) {
                if (((MyOrdersResponse) response.body()).getOrders().isEmpty()) {

                    notificationView.showNoCurrentData();
                    getHistoryDelegateOrders(local,token);

                } else {
                    currentRequest.clear();
                    currentRequest.addAll(((MyOrdersResponse) response.body()).getOrders());
                    getHistoryDelegateOrders(local,token);


                }

            }

            @Override
            public void onFailure() {
                notificationView.hideLoading();
                notificationView.showNetworkViewError();


            }

            @Override
            public void onAuthError() {
                notificationView.hideLoading();
                notificationView.showInvalideTokenError();
            }

            @Override
            public void onInvalidTokenError() {
                notificationView.hideLoading();
            }

            @Override
            public void onServerError() {
                notificationView.hideLoading();
                notificationView.showServerError();
            }

            @Override
            public void onValidationError(String errorMessage) {
                notificationView.hideLoading();
            }

            @Override
            public void onSuspendedUserError(String errorMessage) {
                notificationView.hideLoading();
                notificationView.showSuspededUserError(errorMessage);
            }
        });

    }
    public void getHistoryDelegateOrders(String local,String token){
        final MyOrderView notificationView = view.get();
        if (!isLoading && moreRequestDataAvailable) {
            isLoading = true;

            userRepository.getHistoryDelegateOrder(token,local, pageRequest, limit, new OnResponseListener() {
                @Override
                public void onSuccess(Response response) {
                    notificationView.hideLoading();
                    if (((MyOrdersResponse) response.body()).getOrders().isEmpty() && pageRequest == 1) {
//                        if (currentRequest.size() == 0) {
//                            notificationView.showNoData();
//
//                        }else {
                            notificationView.setRequests(currentRequest,((MyOrdersResponse) response.body()).getOrders());

                            notificationView.showNoHistoryData();
                      //  }

                    } else {
                        historyDelegateRequests.addAll(((MyOrdersResponse) response.body()).getOrders());
                        notificationView.setRequests(currentRequest,historyDelegateRequests);

                        pageRequest += 1;
                        if (((MyOrdersResponse) response.body()).getOrders().size() < limit) {
                            moreRequestDataAvailable = false;
                        }

                    }
                    isLoading = false;

                }

                @Override
                public void onFailure() {
                    notificationView.hideLoading();
                    if (page == 1) {
                        notificationView.showNetworkViewError();
                    }else{
                        notificationView.showNetworkError();

                    }


                }

                @Override
                public void onAuthError() {
                    notificationView.hideLoading();
                    notificationView.showInvalideTokenError();
                }

                @Override
                public void onInvalidTokenError() {

                    notificationView.hideLoading();
                }

                @Override
                public void onServerError() {
                    notificationView.hideLoading();
                    notificationView.showServerError();
                }

                @Override
                public void onValidationError(String errorMessage) {
                    notificationView.hideLoading();
                }

                @Override
                public void onSuspendedUserError(String errorMessage) {
                    notificationView.hideLoading();
                    notificationView.showSuspededUserError(errorMessage);
                }
            });
        }else{
            notificationView.hideLoading();

        }
    }
    public void changeStateOfUserRequest(String token,String local,Boolean state  ){
        final MyOrderView registerationOrLoginView = view.get();
        registerationOrLoginView.showLoading();
        String status = "0";
        if (state){
            status  = "1";
        }
        userRepository.updateDelegateStatus(token,local , status, new OnResponseListener() {

            @Override
            public void onSuccess(Response response) {
                registerationOrLoginView.hideLoading();
                registerationOrLoginView.setStateChanged(((MyDelegateStateResponse) response.body()).delegateActivationRequest.getActive());
            }

            @Override
            public void onFailure() {
                registerationOrLoginView.hideLoading();
                registerationOrLoginView.showNetworkError();
                registerationOrLoginView.setUnSucessChangesStatus();
            }

            @Override
            public void onAuthError() {
                registerationOrLoginView.hideLoading();
                registerationOrLoginView.setUnSucessChangesStatus();

            }

            @Override
            public void onInvalidTokenError() {

                registerationOrLoginView.hideLoading();
                registerationOrLoginView.setUnSucessChangesStatus();

            }

            @Override
            public void onServerError() {
                registerationOrLoginView.hideLoading();
                registerationOrLoginView.showServerError();
                registerationOrLoginView.setUnSucessChangesStatus();

            }

            @Override
            public void onValidationError(String errorMessage) {
                registerationOrLoginView.hideLoading();
                registerationOrLoginView.setUnSucessChangesStatus();

                // registerationOrLoginView.showInvalidCodeError();
            }

            @Override
            public void onSuspendedUserError(String errorMessage) {
                registerationOrLoginView.hideLoading();
                registerationOrLoginView.showSuspededUserError(errorMessage);
                registerationOrLoginView.setUnSucessChangesStatus();

            }
        });
    }



    public void reset(){
        this.page = 1;
        this.pageRequest = 1;

        moreDataAvailable = true;
        moreRequestDataAvailable = true;

        isLoading = false;
        this.userCurrentOrders.clear();
        this.historyUserOrders.clear();
        this.historyDelegateRequests.clear();
        this.currentRequest.clear();

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
    public boolean getMoreRequestDataAvailable(){
        return this.moreRequestDataAvailable;
    }
    public int getPage() {
        return this.page;
    }
    public int getRequestPage() {
        return this.pageRequest;
    }
    public interface MyOrderView{

        void showLoading();
        void hideLoading();
        void showNetworkError();
        void showInvalideTokenError();
        void setOrders(ArrayList<Order> currentOrders, ArrayList<Order> historyOrders);
        void showNoData();
        void showServerError();
        void showSuspededUserError(String errorMessage);
        void setStateChanged(Boolean active);
        void showFirstPageLoading();
        void showNoCurrentData();
        void showNoHistoryData();
        void setRequests(ArrayList<Order> currentOrders, ArrayList<Order> historyOrders);
        void showNetworkViewError();
        void setUnSucessChangesStatus();

    }
}
