package amaz.objects.TwentyfourSeven.ui.HowToUse;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import amaz.objects.TwentyfourSeven.data.models.Slider;
import amaz.objects.TwentyfourSeven.data.models.responses.HowToUseResponse;
import amaz.objects.TwentyfourSeven.data.repositories.GeneralRepository;
import amaz.objects.TwentyfourSeven.listeners.OnResponseListener;
import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import retrofit2.Response;

public class HowToUsePresenter extends BasePresenter {

    private WeakReference<HowToUseView> view = new WeakReference<>(null);
    private GeneralRepository repository;

    public HowToUsePresenter(GeneralRepository repository) {
        this.repository = repository;
    }

    public void setView(HowToUseView view) {
        this.view = new WeakReference<>(view);
    }

    public void getHowToUseData(String locale) {
        final HowToUseView howToUseView = view.get();
        howToUseView.showLoading();

        repository.getHowToUseData(locale, new OnResponseListener() {
            @Override
            public void onSuccess(Response response) {
                howToUseView.hideLoading();
                if (((HowToUseResponse) response.body()).getData().isEmpty()){
                    howToUseView.showNoData();
                }
                else {
                    howToUseView.showHowToUseData(((HowToUseResponse) response.body()).getData());
                }

            }

            @Override
            public void onFailure() {
                howToUseView.hideLoading();
                howToUseView.showNetworkError();
            }

            @Override
            public void onAuthError() {
                howToUseView.hideLoading();
                howToUseView.showNoData();
            }

            @Override
            public void onInvalidTokenError() {
                howToUseView.hideLoading();
            }

            @Override
            public void onServerError() {
                howToUseView.hideLoading();
                howToUseView.showServerError();
            }

            @Override
            public void onValidationError(String errorMessage) {
                howToUseView.hideLoading();
            }

            @Override
            public void onSuspendedUserError(String errorMessage) {
                howToUseView.hideLoading();
            }
        });
    }

    public void getHowToBecomeADelegateData(String locale) {
        final HowToUseView howToUseView = view.get();
        howToUseView.showLoading();

        repository.getHowToBecomeADelegateData(locale, new OnResponseListener() {
            @Override
            public void onSuccess(Response response) {
                howToUseView.hideLoading();
                if (((HowToUseResponse) response.body()).getData().isEmpty()){
                    howToUseView.showNoData();
                }
                else {
                    howToUseView.showHowToUseData(((HowToUseResponse) response.body()).getData());
                }
            }

            @Override
            public void onFailure() {
                howToUseView.hideLoading();
                howToUseView.showNetworkError();
            }

            @Override
            public void onAuthError() {
                howToUseView.hideLoading();
                howToUseView.showNoData();
            }

            @Override
            public void onInvalidTokenError() {
                howToUseView.hideLoading();
            }

            @Override
            public void onServerError() {
                howToUseView.hideLoading();
                howToUseView.showServerError();
            }

            @Override
            public void onValidationError(String errorMessage) {
                howToUseView.hideLoading();
            }

            @Override
            public void onSuspendedUserError(String errorMessage) {
                howToUseView.hideLoading();
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

    public interface HowToUseView {
        void showLoading();

        void hideLoading();

        void showNoData();

        void showNetworkError();

        void showServerError();

        void showHowToUseData(ArrayList<Slider> howToUseData);
    }
}
