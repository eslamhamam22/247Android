package amaz.objects.TwentyfourSeven.ui.AddMoney;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import amaz.objects.TwentyfourSeven.data.models.BankAccount;
import amaz.objects.TwentyfourSeven.data.models.responses.MyBankAccountsResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.MyOrdersResponse;
import amaz.objects.TwentyfourSeven.data.repositories.UserRepository;
import amaz.objects.TwentyfourSeven.listeners.OnResponseListener;
import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import amaz.objects.TwentyfourSeven.ui.MyBalance.MyBalancePresenter;
import retrofit2.Response;


public class BankTransferPresenter extends BasePresenter {

    private UserRepository userRepository;


    private WeakReference<BankTransferView> view = new WeakReference<BankTransferView>(null);
    public BankTransferPresenter(UserRepository userRepository){

        this.userRepository = userRepository;
    }

    public void setView(BankTransferView view){
        this.view = new WeakReference<BankTransferView>(view);
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
    public void getBankAccounts(final String local, final String token){
        final BankTransferView balanceView = view.get();
        balanceView.showLoading();
        userRepository.getBankAccounts(token,local, new OnResponseListener() {
            @Override
            public void onSuccess(Response response) {
                balanceView.hideLoading();

            // historyUserOrders.addAll(((MyOrdersResponse) response.body()).getOrders());
            if (((MyBankAccountsResponse) response.body()).getData().size() == 0){
                balanceView.showNoData();
            }else{
                balanceView.setBankAccounts(((MyBankAccountsResponse) response.body()).getData());

            }


            }

            @Override
            public void onFailure() {
                balanceView.hideLoading();
                balanceView.showNetworkError();


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

    public interface BankTransferView{

        void hideLoading();
        void showLoading();
        void showNetworkError();
        void showServerError();
        void showSuspededUserError(String errorMessage);
        void showInvalideTokenError();
        void setBankAccounts(ArrayList<BankAccount> bankAccounts);
        void showNoData();

    }
}
