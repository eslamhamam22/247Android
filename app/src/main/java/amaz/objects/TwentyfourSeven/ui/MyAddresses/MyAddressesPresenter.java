package amaz.objects.TwentyfourSeven.ui.MyAddresses;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import amaz.objects.TwentyfourSeven.data.models.Address;
import amaz.objects.TwentyfourSeven.data.models.responses.MyAddressesResponse;
import amaz.objects.TwentyfourSeven.data.repositories.UserRepository;
import amaz.objects.TwentyfourSeven.listeners.OnResponseListener;
import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import retrofit2.Response;

public class MyAddressesPresenter extends BasePresenter {

    private UserRepository repository;
    private WeakReference<MyAddressesView> view = new WeakReference<>(null);

    public MyAddressesPresenter(UserRepository repository){
        this.repository = repository;
    }

    public void setView(MyAddressesView view){
        this.view = new WeakReference<>(view);
    }

    public void getMyAddresses(String token,String locale){
        final MyAddressesView myAddressesView = view.get();
        myAddressesView.showLoading();
        repository.getMyAddresses(token, locale, new OnResponseListener() {
            @Override
            public void onSuccess(Response response) {
                myAddressesView.hideLoading();
                if (((MyAddressesResponse)response.body()).getAddresses().isEmpty()){
                    myAddressesView.showNoData();
                }
                else {
                    myAddressesView.showAddresses(((MyAddressesResponse)response.body()).getAddresses());
                }

            }

            @Override
            public void onFailure() {
                myAddressesView.hideLoading();
                myAddressesView.showNetworkError();
            }

            @Override
            public void onAuthError() {
                myAddressesView.hideLoading();
                myAddressesView.showInvalideTokenError();
            }

            @Override
            public void onInvalidTokenError() {

                myAddressesView.hideLoading();
            }

            @Override
            public void onServerError() {
                myAddressesView.hideLoading();
                myAddressesView.showServerError();
            }

            @Override
            public void onValidationError(String errorMessage) {

                myAddressesView.hideLoading();
            }

            @Override
            public void onSuspendedUserError(String errorMessage) {
                myAddressesView.hideLoading();
                myAddressesView.showSuspededUserError(errorMessage);
            }
        });
    }

    public void deleteAddress(String token, String locale, int addressId){
        final MyAddressesView myAddressesView = view.get();
        myAddressesView.showLoading();
        repository.deleteAddress(token, locale, addressId, new OnResponseListener() {
            @Override
            public void onSuccess(Response response) {
                myAddressesView.hideLoading();
                myAddressesView.showSuccessDelete();
            }

            @Override
            public void onFailure() {
                myAddressesView.hideLoading();
                myAddressesView.showNetworkError();
            }

            @Override
            public void onAuthError() {
                myAddressesView.hideLoading();
                myAddressesView.showInvalideTokenError();
            }

            @Override
            public void onInvalidTokenError() {
                myAddressesView.hideLoading();
            }

            @Override
            public void onServerError() {
                myAddressesView.hideLoading();
                myAddressesView.showServerError();
            }

            @Override
            public void onValidationError(String errorMessage) {
                myAddressesView.hideLoading();
            }

            @Override
            public void onSuspendedUserError(String errorMessage) {
                myAddressesView.hideLoading();
                myAddressesView.showSuspededUserError(errorMessage);
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

    public interface MyAddressesView{
        void showLoading();
        void hideLoading();
        void showInvalideTokenError();
        void showNetworkError();
        void showNoData();
        void showServerError();
        void showSuspededUserError(String errorMessage);
        void showAddresses(ArrayList<Address> addresses);
        void showSuccessDelete();
    }
}
