package amaz.objects.TwentyfourSeven.ui.MyOrders;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import amaz.objects.TwentyfourSeven.data.models.Order;
import amaz.objects.TwentyfourSeven.data.models.responses.MyDelegateStateResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.MyOrdersResponse;
import amaz.objects.TwentyfourSeven.data.repositories.UserRepository;
import amaz.objects.TwentyfourSeven.listeners.OnResponseListener;
import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import retrofit2.Response;


public class MyCurrentOrdersPresenter extends BasePresenter {

    private UserRepository userRepository;
    private int page = 2;
    private int limit = 10;
    private ArrayList<Order> historyUserOrders = new ArrayList<>();
    private boolean isLoading = false;
    private boolean moreDataAvailable = true;

    private WeakReference<MyCurrentOrderView> view = new WeakReference<MyCurrentOrderView>(null);
    public MyCurrentOrdersPresenter(UserRepository userRepository){

        this.userRepository = userRepository;
    }

    public void setView(MyCurrentOrderView view){
        this.view = new WeakReference<MyCurrentOrderView>(view);
    }

    public void getCurrentOrders(final String local, final String token){
        final MyCurrentOrderView notificationView = view.get();

            userRepository.getCurrentUserOrder(token,local, page, limit, new OnResponseListener() {
                @Override
                public void onSuccess(Response response) {
                    notificationView.hideLoading();

                  //  historyUserOrders.addAll(((MyOrdersResponse) response.body()).getOrders());

                    notificationView.setOrders(((MyOrdersResponse) response.body()).getOrders());
                    page += 1;


                    if (((MyOrdersResponse) response.body()).getOrders().size() < limit) {
                        moreDataAvailable = false;
                    }
                }

                @Override
                public void onFailure() {
                    notificationView.hideLoading();
                    notificationView.showNetworkError();


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





    public void reset(){
        this.page = 1;
        moreDataAvailable = true;
        isLoading = false;
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
    public interface MyCurrentOrderView{

        void hideLoading();
        void showNetworkError();
        void setOrders(ArrayList<Order> currentOrders);
        void showServerError();
        void showSuspededUserError(String errorMessage);
        void showInvalideTokenError();

    }
}
