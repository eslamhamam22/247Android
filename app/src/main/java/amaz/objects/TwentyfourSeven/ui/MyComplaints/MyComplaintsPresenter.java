package amaz.objects.TwentyfourSeven.ui.MyComplaints;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import amaz.objects.TwentyfourSeven.data.models.Complaint;
import amaz.objects.TwentyfourSeven.data.models.responses.ComplaintsResponse;
import amaz.objects.TwentyfourSeven.data.repositories.UserRepository;
import amaz.objects.TwentyfourSeven.listeners.OnResponseListener;
import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import retrofit2.Response;

public class MyComplaintsPresenter extends BasePresenter {
    private WeakReference<MyComplaintsView> view = new WeakReference<>(null);
    private UserRepository repository;

    private int page = 1;
    private int limit = 10;
    private ArrayList<Complaint> data = new ArrayList<>();

    private boolean isLoading = false;
    private boolean moreDataAvailable = true;

    public MyComplaintsPresenter(UserRepository repository){
        this.repository = repository;
    }

    public void setView(MyComplaintsView view){
        this.view = new WeakReference<>(view);
    }

    public void getMyComplaints(String token, String locale){

        final MyComplaintsView myComplaintsView = view.get();
        if (page == 1){
            myComplaintsView.showLoading();
        }
        else {
            myComplaintsView.showLoadMore();
        }

        if (!isLoading && moreDataAvailable) {
            isLoading = true;
            repository.getComplaints(token, locale, page, limit, new OnResponseListener() {
                @Override
                public void onSuccess(Response response) {
                    if (page == 1) {
                        myComplaintsView.hideLoading();
                    }
                    else {
                        myComplaintsView.hideLoadMore();
                    }
                    ArrayList<Complaint> complaints = ((ComplaintsResponse)response.body()).getComplaints();
                    if (complaints.size() == 0 && data.size() == 0) {
                        myComplaintsView.showNoData();
                    } else {
                        if (complaints.size() < limit){
                            moreDataAvailable = false;
                        }
                        data.addAll(complaints);
                        myComplaintsView.showMyComplaints(complaints);
                        page++;
                    }
                    isLoading = false;
                }

                @Override
                public void onFailure() {
                    if (page == 1) {
                        myComplaintsView.hideLoading();
                        myComplaintsView.showScreenNetworkError();
                    }
                    else {
                        myComplaintsView.hideLoadMore();
                        myComplaintsView.showToastNetworkError();
                    }
                    isLoading = false;
                }

                @Override
                public void onAuthError() {
                    if (page == 1) {
                        myComplaintsView.hideLoading();
                    }
                    else {
                        myComplaintsView.hideLoadMore();
                    }
                    myComplaintsView.showInvalideTokenError();
                    isLoading = false;
                }

                @Override
                public void onInvalidTokenError() {
                    if (page == 1) {
                        myComplaintsView.hideLoading();
                    }
                    else {
                        myComplaintsView.hideLoadMore();
                    }
                    isLoading = false;
                }

                @Override
                public void onServerError() {
                    if (page == 1) {
                        myComplaintsView.hideLoading();
                    }
                    else {
                        myComplaintsView.hideLoadMore();
                    }
                    myComplaintsView.showServerError();
                    isLoading = false;
                }

                @Override
                public void onValidationError(String errorMessage) {
                    if (page == 1) {
                        myComplaintsView.hideLoading();
                    }
                    else {
                        myComplaintsView.hideLoadMore();
                    }
                    isLoading = false;
                }

                @Override
                public void onSuspendedUserError(String errorMessage) {
                    if (page == 1) {
                        myComplaintsView.hideLoading();
                    }
                    else {
                        myComplaintsView.hideLoadMore();
                    }
                    myComplaintsView.showSuspededUserError(errorMessage);
                    isLoading = false;
                }
            });
        }
    }

    public boolean isMoreDataAvailable() {
        return moreDataAvailable;
    }

    public int getPage() {
        return page;
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

    public interface MyComplaintsView {
        void showLoading();
        void hideLoading();
        void showLoadMore();
        void hideLoadMore();
        void showInvalideTokenError();
        void showToastNetworkError();
        void showScreenNetworkError();
        void showNoData();
        void showServerError();
        void showSuspededUserError(String errorMessage);
        void showMyComplaints(ArrayList<Complaint> myComplaints);

    }
}
