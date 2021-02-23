package amaz.objects.TwentyfourSeven.ui.AddMoney;

import org.json.JSONObject;

import java.lang.ref.WeakReference;

import amaz.objects.TwentyfourSeven.data.models.responses.DirectPaymentAuthorizeResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.DirectPaymentConfirmResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.DirectPaymentResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.PaymentInquiryResponse;
import amaz.objects.TwentyfourSeven.data.repositories.PaymentRepository;
import amaz.objects.TwentyfourSeven.listeners.OnResponseListener;
import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import retrofit2.Response;


public class StcPayPresenter extends BasePresenter {

    private PaymentRepository paymentRepository;


    private WeakReference<StcPayView> view = new WeakReference<StcPayView>(null);

    public StcPayPresenter(PaymentRepository paymentRepository) {

        this.paymentRepository = paymentRepository;
    }

    public void setView(StcPayView view) {
        this.view = new WeakReference<StcPayView>(view);
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

    public void stcDirectPaymentAuthorizeRequest(String merchantId, JSONObject payment) {
        final StcPayView stcPayView = view.get();
        stcPayView.showLoading();
        paymentRepository.stcDirectPaymentAuthorize(merchantId, payment, new OnResponseListener() {
            @Override
            public void onSuccess(Response response) {
                stcPayView.hideLoading();
                if (((DirectPaymentAuthorizeResponse) response.body()) == null) {
                    stcPayView.showNoData();
                } else {
                    stcPayView.stcDirectPaymentAuthorizeResult(((DirectPaymentAuthorizeResponse) response.body()));
                }
            }

            @Override
            public void onFailure() {
                stcPayView.hideLoading();
                stcPayView.showNetworkError();
            }

            @Override
            public void onAuthError() {
                stcPayView.hideLoading();
                stcPayView.showInvalideTokenError();
            }

            @Override
            public void onInvalidTokenError() {
                stcPayView.hideLoading();
            }

            @Override
            public void onServerError() {
                stcPayView.hideLoading();
                stcPayView.showServerError();
            }

            @Override
            public void onValidationError(String errorMessage) {
                stcPayView.hideLoading();
            }

            @Override
            public void onSuspendedUserError(String errorMessage) {
                stcPayView.hideLoading();
                stcPayView.showSuspededUserError(errorMessage);
            }
        });

    }

    public void stcDirectPaymentConfirmRequest(String merchantId, JSONObject payment) {
        final StcPayView stcPayView = view.get();
        stcPayView.showLoading();
        paymentRepository.stcDirectPaymentConfirm(merchantId, payment, new OnResponseListener() {
            @Override
            public void onSuccess(Response response) {
                stcPayView.hideLoading();
                if (((DirectPaymentConfirmResponse) response.body()) == null) {
                    stcPayView.showNoData();
                } else {
                    stcPayView.stcDirectPaymentConfirmResult(((DirectPaymentConfirmResponse) response.body()));
                }
            }

            @Override
            public void onFailure() {
                stcPayView.hideLoading();
                stcPayView.showNetworkError();
            }

            @Override
            public void onAuthError() {
                stcPayView.hideLoading();
                stcPayView.showInvalideTokenError();
            }

            @Override
            public void onInvalidTokenError() {
                stcPayView.hideLoading();
            }

            @Override
            public void onServerError() {
                stcPayView.hideLoading();
                stcPayView.showServerError();
            }

            @Override
            public void onValidationError(String errorMessage) {
                stcPayView.hideLoading();
            }

            @Override
            public void onSuspendedUserError(String errorMessage) {
                stcPayView.hideLoading();
                stcPayView.showSuspededUserError(errorMessage);
            }
        });

    }

    public void stcDirectPaymentRequest(String merchantId, JSONObject payment) {
        final StcPayView stcPayView = view.get();
        stcPayView.showLoading();
        paymentRepository.stcDirectPayment(merchantId, payment, new OnResponseListener() {
            @Override
            public void onSuccess(Response response) {
                stcPayView.hideLoading();
                if (((DirectPaymentResponse) response.body()) == null) {
                    stcPayView.showNoData();
                } else {
                    stcPayView.stcDirectPaymentResult(((DirectPaymentResponse) response.body()));
                }
            }

            @Override
            public void onFailure() {
                stcPayView.hideLoading();
                stcPayView.showNetworkError();
            }

            @Override
            public void onAuthError() {
                stcPayView.hideLoading();
                stcPayView.showInvalideTokenError();
            }

            @Override
            public void onInvalidTokenError() {
                stcPayView.hideLoading();
            }

            @Override
            public void onServerError() {
                stcPayView.hideLoading();
                stcPayView.showServerError();
            }

            @Override
            public void onValidationError(String errorMessage) {
                stcPayView.hideLoading();
            }

            @Override
            public void onSuspendedUserError(String errorMessage) {
                stcPayView.hideLoading();
                stcPayView.showSuspededUserError(errorMessage);
            }
        });

    }

    public void stcPaymentInquiryRequest(String merchantId, JSONObject payment) {
        final StcPayView stcPayView = view.get();
        stcPayView.showLoading();
        paymentRepository.stcPaymentInquiry(merchantId, payment, new OnResponseListener() {
            @Override
            public void onSuccess(Response response) {
                stcPayView.hideLoading();
                if (((PaymentInquiryResponse) response.body()) == null) {
                    stcPayView.showNoData();
                } else {
                    stcPayView.stcPaymentInquiryResult(((PaymentInquiryResponse) response.body()));
                }
            }

            @Override
            public void onFailure() {
                stcPayView.hideLoading();
                stcPayView.showNetworkError();
            }

            @Override
            public void onAuthError() {
                stcPayView.hideLoading();
                stcPayView.showInvalideTokenError();
            }

            @Override
            public void onInvalidTokenError() {
                stcPayView.hideLoading();
            }

            @Override
            public void onServerError() {
                stcPayView.hideLoading();
                stcPayView.showServerError();
            }

            @Override
            public void onValidationError(String errorMessage) {
                stcPayView.hideLoading();
            }

            @Override
            public void onSuspendedUserError(String errorMessage) {
                stcPayView.hideLoading();
                stcPayView.showSuspededUserError(errorMessage);
            }
        });

    }

    public interface StcPayView {

        void hideLoading();

        void showLoading();

        void showNetworkError();

        void showServerError();

        void showSuspededUserError(String errorMessage);

        void showInvalideTokenError();

        void showNoData();

        void stcDirectPaymentAuthorizeResult(DirectPaymentAuthorizeResponse result);
        void stcDirectPaymentConfirmResult(DirectPaymentConfirmResponse result);
        void stcDirectPaymentResult(DirectPaymentResponse result);
        void stcPaymentInquiryResult(PaymentInquiryResponse result);
    }
}
