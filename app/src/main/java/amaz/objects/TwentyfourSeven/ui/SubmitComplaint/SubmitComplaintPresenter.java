package amaz.objects.TwentyfourSeven.ui.SubmitComplaint;

import java.lang.ref.WeakReference;

import amaz.objects.TwentyfourSeven.data.repositories.OrderRepository;
import amaz.objects.TwentyfourSeven.listeners.OnResponseListener;
import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import retrofit2.Response;

public class SubmitComplaintPresenter extends BasePresenter {

    private WeakReference<SubmitComplaintView> view = new WeakReference<>(null);
    private OrderRepository repository;

    public SubmitComplaintPresenter(OrderRepository repository){
        this.repository = repository;
    }

    public void setView(SubmitComplaintView view){
        this.view = new WeakReference<>(view);
    }

    public void submitComplaint(String token, String locale, int orderId, String title, String description){
        final SubmitComplaintView submitComplaintView = view.get();
        if (!isValidSubject(title)){
            submitComplaintView.showSubjectError();
        }
        else if (!isValidDescription(description)){
            submitComplaintView.showDescriptionError();
        }
        else {
            submitComplaintView.showLoading();
            repository.submitComplaint(token, locale, orderId, title, description,
                    new OnResponseListener() {
                        @Override
                        public void onSuccess(Response response) {
                            submitComplaintView.hideLoading();
                            submitComplaintView.showSuccessSubmit();

                        }

                        @Override
                        public void onFailure() {
                            submitComplaintView.hideLoading();
                            submitComplaintView.showNetworkError();
                        }

                        @Override
                        public void onAuthError() {
                            submitComplaintView.hideLoading();
                            submitComplaintView.showInvalideTokenError();
                        }

                        @Override
                        public void onInvalidTokenError() {
                            submitComplaintView.hideLoading();
                        }

                        @Override
                        public void onServerError() {
                            submitComplaintView.hideLoading();
                            submitComplaintView.showServerError();
                        }

                        @Override
                        public void onValidationError(String errorMessage) {
                            submitComplaintView.hideLoading();
                        }

                        @Override
                        public void onSuspendedUserError(String errorMessage) {
                            submitComplaintView.hideLoading();
                            submitComplaintView.showSuspededUserError(errorMessage);
                        }
                    });
        }

    }

    private boolean isValidSubject(String subject){
        boolean isValid = true;
        if (subject.length() > 30){
            isValid = false;
        }
        return isValid;
    }

    private boolean isValidDescription(String description){
        boolean isValid = true;
        if (description.length() > 512){
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

    public interface SubmitComplaintView {
        void showLoading();
        void hideLoading();
        void showInvalideTokenError();
        void showNetworkError();
        void showServerError();
        void showSuspededUserError(String errorMessage);
        void showSubjectError();
        void showDescriptionError();
        void showSuccessSubmit();

    }
}
