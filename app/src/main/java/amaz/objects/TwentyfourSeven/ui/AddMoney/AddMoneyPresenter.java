package amaz.objects.TwentyfourSeven.ui.AddMoney;

import java.lang.ref.WeakReference;

import amaz.objects.TwentyfourSeven.data.models.CardPayRegisterationData;
import amaz.objects.TwentyfourSeven.data.models.DirectPaymentAuthorizeData;
import amaz.objects.TwentyfourSeven.data.models.DirectPaymentConfirmData;
import amaz.objects.TwentyfourSeven.data.models.responses.CardPayRegisterationResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.DirectPaymentAuthorizeV4ResponseMessage;
import amaz.objects.TwentyfourSeven.data.models.responses.DirectPaymentConfirmV4ResponseMessage;
import amaz.objects.TwentyfourSeven.data.repositories.UserRepository;
import amaz.objects.TwentyfourSeven.listeners.OnResponseListener;
import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import retrofit2.Response;


public class AddMoneyPresenter extends BasePresenter {

    private UserRepository userRepository;


    private WeakReference<AddMoneyView> view = new WeakReference<AddMoneyView>(null);

    public AddMoneyPresenter(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    public void setView(AddMoneyView view) {
        this.view = new WeakReference<AddMoneyView>(view);
    }

    public void registerCardPayment(final String local, final String token, double amount, int orderId) {
        final AddMoneyView addMoneyView = view.get();
        addMoneyView.showLoading();
        userRepository.registerCardPayment(token, local, amount, orderId, new OnResponseListener() {
            @Override
            public void onSuccess(Response response) {
                addMoneyView.hideLoading();
                addMoneyView.showSuccessRegisterCardPay(((CardPayRegisterationResponse) response.body()).getData());
            }

            @Override
            public void onFailure() {
                addMoneyView.hideLoading();
                addMoneyView.showNetworkError();
            }

            @Override
            public void onAuthError() {
                addMoneyView.hideLoading();
                addMoneyView.showInvalideTokenError();
            }

            @Override
            public void onInvalidTokenError() {
                addMoneyView.hideLoading();
            }

            @Override
            public void onServerError() {
                addMoneyView.hideLoading();
                addMoneyView.showServerError();
            }

            @Override
            public void onValidationError(String errorMessage) {
                addMoneyView.hideLoading();
            }

            @Override
            public void onSuspendedUserError(String errorMessage) {
                addMoneyView.hideLoading();
                addMoneyView.showSuspededUserError(errorMessage);
            }
        });

    }

    public void getCheckoutId(final String local, final String token, double amount, int orderId) {
        final AddMoneyView addMoneyView = view.get();
        addMoneyView.showLoading();
        userRepository.getCheckoutId(token, local, amount, orderId, new OnResponseListener() {
            @Override
            public void onSuccess(Response response) {
                addMoneyView.hideLoading();
                addMoneyView.showSuccessRegisterCardPay(((CardPayRegisterationResponse) response.body()).getData());
            }

            @Override
            public void onFailure() {
                addMoneyView.hideLoading();
                addMoneyView.showNetworkError();
            }

            @Override
            public void onAuthError() {
                addMoneyView.hideLoading();
                addMoneyView.showInvalideTokenError();
            }

            @Override
            public void onInvalidTokenError() {
                addMoneyView.hideLoading();
            }

            @Override
            public void onServerError() {
                addMoneyView.hideLoading();
                addMoneyView.showServerError();
            }

            @Override
            public void onValidationError(String errorMessage) {
                addMoneyView.hideLoading();
            }

            @Override
            public void onSuspendedUserError(String errorMessage) {
                addMoneyView.hideLoading();
                addMoneyView.showSuspededUserError(errorMessage);
            }
        });

    }

    public void postStcDirectPaymentAuthorize(final String local, final String token, String mobile, Double amount) {
        final AddMoneyView addMoneyView = view.get();
        addMoneyView.showLoading();
        userRepository.postStcDirectPaymentAuthorize(token, local, mobile, amount, new OnResponseListener() {
            @Override
            public void onSuccess(Response response) {
                addMoneyView.hideLoading();
                addMoneyView.showSuccessStcAuthorize(((DirectPaymentAuthorizeV4ResponseMessage) response.body()).getData());
            }

            @Override
            public void onFailure() {
                addMoneyView.hideLoading();
                addMoneyView.showNetworkError();
            }

            @Override
            public void onAuthError() {
                addMoneyView.hideLoading();
                addMoneyView.showInvalideTokenError();
            }

            @Override
            public void onInvalidTokenError() {
                addMoneyView.hideLoading();
            }

            @Override
            public void onServerError() {
                addMoneyView.hideLoading();
                addMoneyView.showServerError();
            }

            @Override
            public void onValidationError(String errorMessage) {
                addMoneyView.hideLoading();
            }

            @Override
            public void onSuspendedUserError(String errorMessage) {
                addMoneyView.hideLoading();
                addMoneyView.showSuspededUserError(errorMessage);
            }
        });

    }

    public void postStcDirectPaymentConfirm(final String local, final String token, String otpReference, String otpValue, String sTCPayPmtReference, String tokenReference) {
        final AddMoneyView addMoneyView = view.get();
        addMoneyView.showLoading();
        userRepository.postStcDirectPaymentConfirm(token, local, otpReference, otpValue, sTCPayPmtReference, tokenReference, new OnResponseListener() {
            @Override
            public void onSuccess(Response response) {
                addMoneyView.hideLoading();
                addMoneyView.showSuccessStcConfirm(((DirectPaymentConfirmV4ResponseMessage) response.body()).getData());
            }

            @Override
            public void onFailure() {
                addMoneyView.hideLoading();
                addMoneyView.showNetworkError();
            }

            @Override
            public void onAuthError() {
                addMoneyView.hideLoading();
                addMoneyView.showInvalideTokenError();
            }

            @Override
            public void onInvalidTokenError() {
                addMoneyView.hideLoading();
            }

            @Override
            public void onServerError() {
                addMoneyView.hideLoading();
                addMoneyView.showServerError();
            }

            @Override
            public void onValidationError(String errorMessage) {
                addMoneyView.hideLoading();
            }

            @Override
            public void onSuspendedUserError(String errorMessage) {
                addMoneyView.hideLoading();
                addMoneyView.showSuspededUserError(errorMessage);
            }
        });

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

    public interface AddMoneyView {

        void hideLoading();

        void showLoading();

        void showNetworkError();

        void showServerError();

        void showSuspededUserError(String errorMessage);

        void showInvalideTokenError();

        void showSuccessRegisterCardPay(CardPayRegisterationData data);

        void showSuccessStcAuthorize(DirectPaymentAuthorizeData data);

        void showSuccessStcConfirm(DirectPaymentConfirmData data);
    }
}
