package amaz.objects.TwentyfourSeven.ui.AddMoney;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import amaz.objects.TwentyfourSeven.data.models.BankAccount;
import amaz.objects.TwentyfourSeven.data.models.responses.MyBankAccountsResponse;
import amaz.objects.TwentyfourSeven.data.repositories.UserRepository;
import amaz.objects.TwentyfourSeven.listeners.OnResponseListener;
import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import retrofit2.Response;


public class SubmitTransactionPresenter extends BasePresenter {

    private UserRepository userRepository;


    private WeakReference<SubmitTranscationView> view = new WeakReference<SubmitTranscationView>(null);

    public SubmitTransactionPresenter(UserRepository userRepository){

        this.userRepository = userRepository;
    }

    public void setView(SubmitTranscationView view){
        this.view = new WeakReference<SubmitTranscationView>(view);
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
    public void uploadBankTransfer(final String local, final String token,File imageFile,String amount,String accountNum){
        final SubmitTranscationView balanceView = view.get();
        balanceView.showLoading();
        userRepository.uploadBankTransfer(token,local,accountNum,amount,imageFile, new OnResponseListener() {
            @Override
            public void onSuccess(Response response) {
                balanceView.hideLoading();
                balanceView.showSucessTransferNote();
                balanceView.enableSubmit();


            }

            @Override
            public void onFailure() {
                balanceView.hideLoading();
                balanceView.showNetworkError();
                balanceView.enableSubmit();


            }

            @Override
            public void onAuthError() {
                balanceView.hideLoading();
                balanceView.showInvalideTokenError();
                balanceView.enableSubmit();

            }

            @Override
            public void onInvalidTokenError() {

                balanceView.hideLoading();
                balanceView.enableSubmit();

            }

            @Override
            public void onServerError() {
                balanceView.hideLoading();
                balanceView.showServerError();
                balanceView.enableSubmit();

            }

            @Override
            public void onValidationError(String errorMessage) {

                balanceView.hideLoading();
                balanceView.showValidationError(errorMessage);
                balanceView.enableSubmit();

            }

            @Override
            public void onSuspendedUserError(String errorMessage) {
                balanceView.hideLoading();
                balanceView.showSuspededUserError(errorMessage);
                balanceView.enableSubmit();

            }
        });

    }

    public interface SubmitTranscationView{

        void hideLoading();
        void showLoading();
        void showNetworkError();
        void showServerError();
        void showSuspededUserError(String errorMessage);
        void showInvalideTokenError();
        void showValidationError(String errorMessage);
        void showSucessTransferNote();
        void enableSubmit();

    }
}
