package amaz.objects.TwentyfourSeven.ui.MyBalance;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import amaz.objects.TwentyfourSeven.data.models.UserTransaction;
import amaz.objects.TwentyfourSeven.data.models.Wallet;
import amaz.objects.TwentyfourSeven.data.models.responses.MyBalanceResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.WalletResponse;
import amaz.objects.TwentyfourSeven.data.repositories.UserRepository;
import amaz.objects.TwentyfourSeven.listeners.OnResponseListener;
import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import retrofit2.Response;


public class MyBalancePresenter extends BasePresenter {

    private UserRepository userRepository;
    private int page = 1;
    private int limit = 10;
    private boolean isLoading = false;
    private boolean moreDataAvailable = true;
    private ArrayList<UserTransaction> userTransactions = new ArrayList<UserTransaction>();
    private  Wallet walletResponse = new Wallet();

    private WeakReference<MyBalanceView> view = new WeakReference<MyBalanceView>(null);
    public MyBalancePresenter(UserRepository userRepository){

        this.userRepository = userRepository;
    }

    public void setView(MyBalanceView view){
        this.view = new WeakReference<MyBalanceView>(view);
    }

    public void getWalletDetails(final String local, final String token){
        final MyBalanceView balanceView = view.get();
            balanceView.showLoading();

        userRepository.getWalletDetails(token,local, new OnResponseListener() {
            @Override
            public void onSuccess(Response response) {
               // balanceView.hideLoading();
                walletResponse = (((WalletResponse) response.body()).getData());
                getTransactionHistory(token,local);

            }

            @Override
            public void onFailure() {
                balanceView.hideLoading();
                    balanceView.showNetworkError(true);



            }

            @Override
            public void onAuthError() {
                balanceView.hideLoading();
                balanceView.showInvalideTokenError();
            }

            @Override
            public void onInvalidTokenError() {
                balanceView.hideLoading();
            }

            @Override
            public void onServerError() {
                balanceView.hideLoading();
                balanceView.showServerError();
            }

            @Override
            public void onValidationError(String errorMessage) {
                balanceView.hideLoading();
            }

            @Override
            public void onSuspendedUserError(String errorMessage) {
                balanceView.hideLoading();
                balanceView.showSuspededUserError(errorMessage);
            }
        });

    }

    public void getTransactionHistory(final String token, final String local){
        final MyBalanceView balanceView = view.get();
//          if (page == 1){
//              balanceView.showLoading();
//          }
            userRepository.getTransactionHistory(token,local, page, limit, new OnResponseListener() {
                @Override
                public void onSuccess(Response response) {
                    balanceView.hideLoading();
                    if (page == 1){
                        balanceView.setWalletData(walletResponse);

                    }
                      if (((MyBalanceResponse) response.body()).getUserTransactions().size() == 0 && page == 1){
                          balanceView.showNoData();
                      }else {
                          userTransactions.addAll(((MyBalanceResponse) response.body()).getUserTransactions());
                          balanceView.setUserTransactions(userTransactions);
                      }
                      page += 1;


                    if (((MyBalanceResponse) response.body()).getUserTransactions().size() < limit) {
                        moreDataAvailable = false;
                    }
                }

                @Override
                public void onFailure() {
                    balanceView.hideLoading();
                    if (page == 1) {
                        balanceView.showNetworkError(true);
                    }else{
                        balanceView.showNetworkError(false);

                    }


                }

                @Override
                public void onAuthError() {
                    balanceView.hideLoading();
                    balanceView.showInvalideTokenError();
                }

                @Override
                public void onInvalidTokenError() {
                    balanceView.hideLoading();
                }

                @Override
                public void onServerError() {
                    balanceView.hideLoading();
                    balanceView.showServerError();
                }

                @Override
                public void onValidationError(String errorMessage) {
                    balanceView.hideLoading();
                }

                @Override
                public void onSuspendedUserError(String errorMessage) {
                    balanceView.hideLoading();
                    balanceView.showSuspededUserError(errorMessage);
                }
            });

    }





    public void reset(){
        this.page = 1;
        moreDataAvailable = true;
        isLoading = false;
        userTransactions.clear();
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

    public interface MyBalanceView{

        void hideLoading();
        void showLoading();
        void showNetworkError(boolean isView);
        void setUserTransactions(ArrayList<UserTransaction> userTransactions);
        void showServerError();
        void showSuspededUserError(String errorMessage);
        void showInvalideTokenError();
        void showNoData();
        void setWalletData(Wallet walletData);

    }
}
