package amaz.objects.TwentyfourSeven.ui.ContactUs;

import java.lang.ref.WeakReference;

import amaz.objects.TwentyfourSeven.data.models.ContactUsData;
import amaz.objects.TwentyfourSeven.data.models.responses.ContactUsResponse;
import amaz.objects.TwentyfourSeven.data.repositories.UserRepository;
import amaz.objects.TwentyfourSeven.listeners.OnResponseListener;
import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import retrofit2.Response;

public class ContactUsPresenter extends BasePresenter {

    private UserRepository repository;
    private WeakReference<ContactUsView> view = new WeakReference<>(null);

    public ContactUsPresenter(UserRepository repository){
        this.repository = repository;
    }

    public void setView(ContactUsView view){
        this.view = new WeakReference<>(view);
    }

    public void getContactsData(String locale){
        final ContactUsView contactUsView = view.get();
        contactUsView.showLoading();
        repository.getContactUsData(locale, new OnResponseListener() {
            @Override
            public void onSuccess(Response response) {
                contactUsView.hideLoading();
                contactUsView.showContactUsData(((ContactUsResponse) response.body()).getData());
            }

            @Override
            public void onFailure() {
                contactUsView.hideLoading();
                contactUsView.showNetworkError();
            }

            @Override
            public void onAuthError() {
                contactUsView.hideLoading();
                contactUsView.showPageNotFoundError();
            }

            @Override
            public void onInvalidTokenError() {
                contactUsView.hideLoading();
            }

            @Override
            public void onServerError() {
                contactUsView.hideLoading();
                contactUsView.showServerError();
            }

            @Override
            public void onValidationError(String errorMessage) {
                contactUsView.hideLoading();
            }

            @Override
            public void onSuspendedUserError(String errorMessage) {
                contactUsView.hideLoading();
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

    public interface ContactUsView {
        void showLoading();
        void hideLoading();
        void showPageNotFoundError();
        void showNetworkError();
        void showServerError();
        void showContactUsData(ContactUsData data);
    }
}
