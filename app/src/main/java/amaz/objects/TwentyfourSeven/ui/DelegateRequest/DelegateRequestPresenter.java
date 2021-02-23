package amaz.objects.TwentyfourSeven.ui.DelegateRequest;

import java.io.File;
import java.lang.ref.WeakReference;

import amaz.objects.TwentyfourSeven.data.models.responses.UploadDelegateImagesResponse;
import amaz.objects.TwentyfourSeven.data.repositories.UserRepository;
import amaz.objects.TwentyfourSeven.listeners.OnResponseListener;
import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import retrofit2.Response;

public class DelegateRequestPresenter extends BasePresenter {

    private WeakReference<DelegateRequestView> view = new WeakReference<>(null);
    private UserRepository repository;

    public DelegateRequestPresenter(UserRepository repository){
        this.repository = repository;
    }

    public void setView(DelegateRequestView view){
        this.view = new WeakReference<>(view);
    }

    public void uploadDelegateImages(String token, String locale, final String imageType, File imageFile){
        final DelegateRequestView delegateRequestView = view.get();
        delegateRequestView.showImageLoading(imageType);
        repository.uploadDelegateImages(token, locale, imageType, imageFile, new OnResponseListener() {
            @Override
            public void onSuccess(Response response) {
                delegateRequestView.hideImageLoading(imageType);
                delegateRequestView.showSuccessUpload((UploadDelegateImagesResponse)response.body());
            }

            @Override
            public void onFailure() {
                delegateRequestView.hideImageLoading(imageType);
                delegateRequestView.showNetworkError();
            }

            @Override
            public void onAuthError() {
                delegateRequestView.hideImageLoading(imageType);
                delegateRequestView.showInvalideTokenError();
            }

            @Override
            public void onInvalidTokenError() {
                delegateRequestView.hideImageLoading(imageType);
            }

            @Override
            public void onServerError() {
                delegateRequestView.hideImageLoading(imageType);
                delegateRequestView.showServerError();
            }

            @Override
            public void onValidationError(String errorMessage) {
                delegateRequestView.hideImageLoading(imageType);
            }

            @Override
            public void onSuspendedUserError(String errorMessage) {
                delegateRequestView.hideImageLoading(imageType);
                delegateRequestView.showSuspededUserError(errorMessage);
            }
        });
    }

    public void deleteDelegateImages(String token, String locale, long id, final String deletedType){
        final DelegateRequestView delegateRequestView = view.get();
        delegateRequestView.showImageLoading(deletedType);
        repository.deleteDelegateImages(token, locale, id, new OnResponseListener() {
            @Override
            public void onSuccess(Response response) {
                delegateRequestView.hideImageLoading(deletedType);
                delegateRequestView.showSuccessDelete(deletedType);
            }

            @Override
            public void onFailure() {
                delegateRequestView.hideImageLoading(deletedType);
                delegateRequestView.showNetworkError();
            }

            @Override
            public void onAuthError() {
                delegateRequestView.hideImageLoading(deletedType);
                delegateRequestView.showInvalideTokenError();
            }

            @Override
            public void onInvalidTokenError() {
                delegateRequestView.hideImageLoading(deletedType);
            }

            @Override
            public void onServerError() {
                delegateRequestView.hideImageLoading(deletedType);
                delegateRequestView.showServerError();
            }

            @Override
            public void onValidationError(String errorMessage) {
                delegateRequestView.hideImageLoading(deletedType);
            }

            @Override
            public void onSuspendedUserError(String errorMessage) {
                delegateRequestView.hideImageLoading(deletedType);
                delegateRequestView.showSuspededUserError(errorMessage);
            }
        });
    }

    public void submitDelegateRequest(String token, String locale, String carDetails, String idNumber, String imagesIds){
        final DelegateRequestView delegateRequestView = view.get();
        delegateRequestView.showLoading();
        repository.submitDelegateRequest(token, locale, carDetails, idNumber, imagesIds, new OnResponseListener() {
            @Override
            public void onSuccess(Response response) {
                delegateRequestView.hideLoading();
                delegateRequestView.showSuccessSubmit();
            }

            @Override
            public void onFailure() {
                delegateRequestView.hideLoading();
                delegateRequestView.showNetworkError();
            }

            @Override
            public void onAuthError() {
                delegateRequestView.hideLoading();
                delegateRequestView.showInvalideTokenError();
            }

            @Override
            public void onInvalidTokenError() {
                delegateRequestView.hideLoading();
            }

            @Override
            public void onServerError() {
                delegateRequestView.hideLoading();
                delegateRequestView.showServerError();
            }

            @Override
            public void onValidationError(String errorMessage) {
                delegateRequestView.hideLoading();
                delegateRequestView.showHasRequestError();
            }

            @Override
            public void onSuspendedUserError(String errorMessage) {
                delegateRequestView.hideLoading();
                delegateRequestView.showSuspededUserError(errorMessage);
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

    public interface DelegateRequestView {
        void showLoading();
        void hideLoading();
        void showImageLoading(String imageType);
        void hideImageLoading(String imageType);
        void showInvalideTokenError();
        void showNetworkError();
        void showServerError();
        void showHasRequestError();
        void showSuspededUserError(String errorMessage);
        void showSuccessUpload(UploadDelegateImagesResponse response);
        void showSuccessDelete(String deletedType);
        void showSuccessSubmit();
    }
}
