package amaz.objects.TwentyfourSeven.ui.Pages;

import java.lang.ref.WeakReference;

import amaz.objects.TwentyfourSeven.data.models.PageData;
import amaz.objects.TwentyfourSeven.data.models.responses.PageResponse;
import amaz.objects.TwentyfourSeven.data.repositories.GeneralRepository;
import amaz.objects.TwentyfourSeven.listeners.OnResponseListener;
import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import retrofit2.Response;

public class PagesPresenter extends BasePresenter {

    private GeneralRepository repository;
    private WeakReference<PagesView> view = new WeakReference<>(null);

    public PagesPresenter(GeneralRepository repository){
        this.repository = repository;
    }

    public void setView(PagesView view){
        this.view = new WeakReference<>(view);
    }

    public void getPageData(String locale,String page){

        final PagesView pagesView = view.get();
        pagesView.showLoading();
        repository.getPageData(locale, page, new OnResponseListener() {
            @Override
            public void onSuccess(Response response) {
                pagesView.hideLoading();
                pagesView.showPageData(((PageResponse)response.body()).getData());
            }

            @Override
            public void onFailure() {
                pagesView.hideLoading();
                pagesView.showNetworkError();
            }

            @Override
            public void onAuthError() {
                pagesView.hideLoading();
                pagesView.showPageNotFoundError();
            }

            @Override
            public void onInvalidTokenError() {
                pagesView.hideLoading();
            }

            @Override
            public void onServerError() {
                pagesView.hideLoading();
                pagesView.showServerError();
            }

            @Override
            public void onValidationError(String errorMessage) {
                pagesView.hideLoading();
            }

            @Override
            public void onSuspendedUserError(String errorMessage) {
                pagesView.hideLoading();
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

    public interface PagesView {
        void showLoading();
        void hideLoading();
        void showPageNotFoundError();
        void showNetworkError();
        void showServerError();
        void showPageData(PageData pageData);
    }
}
