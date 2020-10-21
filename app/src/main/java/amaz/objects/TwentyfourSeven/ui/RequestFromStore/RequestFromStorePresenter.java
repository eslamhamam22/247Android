package amaz.objects.TwentyfourSeven.ui.RequestFromStore;

import java.io.File;
import java.lang.ref.WeakReference;

import amaz.objects.TwentyfourSeven.data.models.DelegateImageData;
import amaz.objects.TwentyfourSeven.data.models.Order;
import amaz.objects.TwentyfourSeven.data.models.PrayerTimes;
import amaz.objects.TwentyfourSeven.data.models.VoiceNote;
import amaz.objects.TwentyfourSeven.data.models.responses.CustomerOrderDetailsResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.PrayerTimesResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.UploadDelegateImagesResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.VoiceNoteResponse;
import amaz.objects.TwentyfourSeven.data.repositories.OrderRepository;
import amaz.objects.TwentyfourSeven.listeners.OnResponseListener;
import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import retrofit2.Response;

public class RequestFromStorePresenter extends BasePresenter {

    private WeakReference<RequestFromStoreView> view = new WeakReference<>(null);
    private OrderRepository repository;

    public RequestFromStorePresenter(OrderRepository repository){
        this.repository = repository;
    }

    public void setView(RequestFromStoreView view){
        this.view = new WeakReference<>(view);
    }

    public void uploadOrderImages(String token, String locale, File imageFile){
        final RequestFromStoreView requestFromStoreView = view.get();
        requestFromStoreView.showLoading();
        repository.uploadOrderImages(token, locale, imageFile, new OnResponseListener() {
            @Override
            public void onSuccess(Response response) {
                requestFromStoreView.hideLoading();
                requestFromStoreView.showSuccessUploadImage((UploadDelegateImagesResponse)response.body());
            }

            @Override
            public void onFailure() {
                requestFromStoreView.hideLoading();
                requestFromStoreView.showNetworkError();
            }

            @Override
            public void onAuthError() {
                requestFromStoreView.hideLoading();
                requestFromStoreView.showInvalideTokenError();
            }

            @Override
            public void onInvalidTokenError() {
                requestFromStoreView.hideLoading();
            }

            @Override
            public void onServerError() {
                requestFromStoreView.hideLoading();
                requestFromStoreView.showServerError();
            }

            @Override
            public void onValidationError(String errorMessage) {
                requestFromStoreView.hideLoading();
            }

            @Override
            public void onSuspendedUserError(String errorMessage) {
                requestFromStoreView.hideLoading();
                requestFromStoreView.showSuspededUserError(errorMessage);
            }
        });
    }

    public void deleteOrderImages(String token, String locale, final DelegateImageData imageData){
        final RequestFromStoreView requestFromStoreView = view.get();
        requestFromStoreView.showLoading();
        repository.deleteOrderImages(token, locale, imageData.getId(), new OnResponseListener() {
            @Override
            public void onSuccess(Response response) {
                requestFromStoreView.hideLoading();
                requestFromStoreView.showSuccessDelete(imageData);
            }

            @Override
            public void onFailure() {
                requestFromStoreView.hideLoading();
                requestFromStoreView.showNetworkError();
            }

            @Override
            public void onAuthError() {
                requestFromStoreView.hideLoading();
                requestFromStoreView.showInvalideTokenError();
            }

            @Override
            public void onInvalidTokenError() {
                requestFromStoreView.hideLoading();
            }

            @Override
            public void onServerError() {
                requestFromStoreView.hideLoading();
                requestFromStoreView.showServerError();
            }

            @Override
            public void onValidationError(String errorMessage) {
                requestFromStoreView.hideLoading();
            }

            @Override
            public void onSuspendedUserError(String errorMessage) {
                requestFromStoreView.hideLoading();
                requestFromStoreView.showSuspededUserError(errorMessage);
            }
        });
    }

    public void createOrder(String token, String locale, String orderDescription, int fromType, double fromLat, double fromLng, String fromAddress,
                            double toLat, double toLng, String toAddress, String storeName, String imagesIds, int deliveryDuration, String couponCode){
        final RequestFromStoreView requestFromStoreView = view.get();
        if (validateOrderDescription(orderDescription)) {
            requestFromStoreView.showLoading();
            repository.createOrder(token, locale, orderDescription, fromType, fromLat, fromLng, fromAddress, toLat, toLng, toAddress,
                    storeName, imagesIds, deliveryDuration, couponCode, false,
                    new OnResponseListener() {
                        @Override
                        public void onSuccess(Response response) {
                            requestFromStoreView.hideLoading();
                            Order order = ((CustomerOrderDetailsResponse) response.body()).getData().getOrder();

                            requestFromStoreView.showSuccessSubmit(order);
                        }

                        @Override
                        public void onFailure() {
                            requestFromStoreView.hideLoading();
                            requestFromStoreView.showNetworkError();
                        }

                        @Override
                        public void onAuthError() {
                            requestFromStoreView.hideLoading();
                            requestFromStoreView.showInvalideTokenError();
                        }

                        @Override
                        public void onInvalidTokenError() {
                            requestFromStoreView.hideLoading();
                        }

                        @Override
                        public void onServerError() {
                            requestFromStoreView.hideLoading();
                            requestFromStoreView.showServerError();
                        }

                        @Override
                        public void onValidationError(String errorMessage) {
                            requestFromStoreView.hideLoading();
                            requestFromStoreView.showValidationError(errorMessage);
                        }

                        @Override
                        public void onSuspendedUserError(String errorMessage) {
                            requestFromStoreView.hideLoading();
                            requestFromStoreView.showSuspededUserError(errorMessage);
                        }
                    });
        }
        else {
            requestFromStoreView.showValidationError(null);
        }
    }

    public void uploadOrderVoice(String token, String locale, File voiceFile){
        final RequestFromStoreView requestFromStoreView = view.get();
        requestFromStoreView.showVoiceLoading();
        repository.uploadeOrdervoice(token, locale, voiceFile, new OnResponseListener() {
            @Override
            public void onSuccess(Response response) {
                requestFromStoreView.hideVoiceLoading();
                requestFromStoreView.showSuccessUploadVoice(((VoiceNoteResponse)response.body()).getVoiceNote());
            }

            @Override
            public void onFailure() {
                requestFromStoreView.hideVoiceLoading();
                requestFromStoreView.showNetworkError();
            }

            @Override
            public void onAuthError() {
                requestFromStoreView.hideVoiceLoading();
                requestFromStoreView.showInvalideTokenError();
            }

            @Override
            public void onInvalidTokenError() {
                requestFromStoreView.hideVoiceLoading();
            }

            @Override
            public void onServerError() {
                requestFromStoreView.hideVoiceLoading();
                requestFromStoreView.showServerError();
            }

            @Override
            public void onValidationError(String errorMessage) {
                requestFromStoreView.hideVoiceLoading();
            }

            @Override
            public void onSuspendedUserError(String errorMessage) {
                requestFromStoreView.hideVoiceLoading();
                requestFromStoreView.showSuspededUserError(errorMessage);
            }
        });
    }

    public void deleteOrderVoice(String token, String locale, long id){
        final RequestFromStoreView requestFromStoreView = view.get();
        requestFromStoreView.showVoiceLoading();
        repository.deleteOrderVoice(token, locale, id, new OnResponseListener() {
            @Override
            public void onSuccess(Response response) {
                requestFromStoreView.hideLoading();
                requestFromStoreView.showSuccessDeleteVoice();
            }

            @Override
            public void onFailure() {
                requestFromStoreView.hideVoiceLoading();
                requestFromStoreView.showNetworkError();
            }

            @Override
            public void onAuthError() {
                requestFromStoreView.hideVoiceLoading();
                requestFromStoreView.showInvalideTokenError();
            }

            @Override
            public void onInvalidTokenError() {
                requestFromStoreView.hideVoiceLoading();
            }

            @Override
            public void onServerError() {
                requestFromStoreView.hideVoiceLoading();
                requestFromStoreView.showServerError();
            }

            @Override
            public void onValidationError(String errorMessage) {
                requestFromStoreView.hideVoiceLoading();
            }

            @Override
            public void onSuspendedUserError(String errorMessage) {
                requestFromStoreView.hideVoiceLoading();
                requestFromStoreView.showSuspededUserError(errorMessage);
            }
        });
    }

    public void getPrayerTimes(String locale){
        final RequestFromStoreView requestFromStoreView = view.get();
        requestFromStoreView.showLoading();
        repository.getPrayerTimes(locale, new OnResponseListener() {
            @Override
            public void onSuccess(Response response) {
                requestFromStoreView.hideLoading();
                requestFromStoreView.showPrayerTimes(((PrayerTimesResponse)response.body()).getPrayerTimes());
            }

            @Override
            public void onFailure() {
                requestFromStoreView.hideLoading();
                requestFromStoreView.showNetworkError();
                requestFromStoreView.showDefaultPrayerTimes();
            }

            @Override
            public void onAuthError() {
                requestFromStoreView.hideLoading();
            }

            @Override
            public void onInvalidTokenError() {
                requestFromStoreView.hideLoading();
            }

            @Override
            public void onServerError() {
                requestFromStoreView.hideLoading();
                requestFromStoreView.showServerError();
                requestFromStoreView.showDefaultPrayerTimes();
            }

            @Override
            public void onValidationError(String errorMessage) {
                requestFromStoreView.hideLoading();
            }

            @Override
            public void onSuspendedUserError(String errorMessage) {
                requestFromStoreView.hideLoading();
            }
        });
    }

    public void validateCoupon(String token, String locale, final String code){
        final RequestFromStoreView requestFromStoreView = view.get();
        requestFromStoreView.showCouponDialogLoading();
        repository.validateCoupon(token, locale, code, new OnResponseListener(){
            @Override
            public void onSuccess(Response response) {
                requestFromStoreView.hideCouponDialogLoading();
                requestFromStoreView.showValidCoupon(code);
            }

            @Override
            public void onFailure() {
                requestFromStoreView.hideCouponDialogLoading();
                requestFromStoreView.showNetworkError();
            }

            @Override
            public void onAuthError() {
                requestFromStoreView.hideCouponDialogLoading();
                requestFromStoreView.showInvalideTokenError();
            }

            @Override
            public void onInvalidTokenError() {
                requestFromStoreView.hideCouponDialogLoading();
            }

            @Override
            public void onServerError() {
                requestFromStoreView.hideCouponDialogLoading();
                requestFromStoreView.showServerError();
            }

            @Override
            public void onValidationError(String errorMessage) {
                requestFromStoreView.hideCouponDialogLoading();
                requestFromStoreView.showValidationError(errorMessage);
                requestFromStoreView.removePreviousCoupon();
            }

            @Override
            public void onSuspendedUserError(String errorMessage) {
                requestFromStoreView.hideCouponDialogLoading();
                requestFromStoreView.showSuspededUserError(errorMessage);
            }
        });
    }

    private boolean validateOrderDescription(String orderDescription){
        boolean isValid = true;
        if (orderDescription.length()>255){
            isValid = false;
        }
        return isValid;
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

    public interface RequestFromStoreView {
        void showLoading();
        void hideLoading();
        void showVoiceLoading();
        void hideVoiceLoading();
        void showCouponDialogLoading();
        void hideCouponDialogLoading();
        void showInvalideTokenError();
        void showNetworkError();
        void showServerError();
        void showSuspededUserError(String errorMessage);
        void showValidationError(String errorMessage);
        void showBlockedAreaError();
        void showSuccessUploadImage(UploadDelegateImagesResponse response);
        void showSuccessUploadVoice(VoiceNote voiceNote);
        void showSuccessDelete(DelegateImageData imageData);
        void showSuccessDeleteVoice();
        void showSuccessSubmit(Order order);
        void showPrayerTimes(PrayerTimes prayerTimes);
        void showDefaultPrayerTimes();
        void showValidCoupon(String code);
        void removePreviousCoupon();
    }
}
