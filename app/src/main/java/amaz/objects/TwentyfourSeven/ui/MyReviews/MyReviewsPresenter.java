package amaz.objects.TwentyfourSeven.ui.MyReviews;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import amaz.objects.TwentyfourSeven.data.models.Review;
import amaz.objects.TwentyfourSeven.data.models.User;
import amaz.objects.TwentyfourSeven.data.models.responses.ReviewsResponse;
import amaz.objects.TwentyfourSeven.data.repositories.UserRepository;
import amaz.objects.TwentyfourSeven.listeners.OnResponseListener;
import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import retrofit2.Response;

public class MyReviewsPresenter extends BasePresenter {

    private WeakReference<MyReviewsView> view = new WeakReference<>(null);
    private UserRepository repository;

    private int page = 1;
    private int limit = 10;
    private ArrayList<Review> data = new ArrayList<>();

    private boolean isLoading = false;
    private boolean moreDataAvailable = true;

    public MyReviewsPresenter(UserRepository repository){
        this.repository = repository;
    }

    public void setView(MyReviewsView view){
        this.view = new WeakReference<>(view);
    }

    public void getCustomerOrDelegateReviews(String token, String locale, int userId, boolean isCustomerReviews){

        final MyReviewsView myReviewsView = view.get();
        if (page == 1){
            myReviewsView.showLoading();
        }
        else {
            myReviewsView.showLoadMore();
        }

        if (!isLoading && moreDataAvailable) {
            isLoading = true;
            repository.getCustomerOrDelegateReviews(token, locale, userId, page, limit, isCustomerReviews, new OnResponseListener() {
                @Override
                public void onSuccess(Response response) {
                    if (page == 1) {
                        myReviewsView.hideLoading();
                    }
                    else {
                        myReviewsView.hideLoadMore();
                    }
                    ArrayList<Review> myReviews = ((ReviewsResponse)response.body()).getData().getReviews();
                    User user = ((ReviewsResponse)response.body()).getData().getUser();
                    if (myReviews.size() == 0 && data.size() == 0) {
                        myReviewsView.showNoData(user);
                    } else {
                        if (myReviews.size() < limit){
                            moreDataAvailable = false;
                        }
                        data.addAll(myReviews);
                        myReviewsView.showMyReviews(myReviews, user);
                        page++;
                    }
                    isLoading = false;
                }

                @Override
                public void onFailure() {
                    if (page == 1) {
                        myReviewsView.hideLoading();
                        myReviewsView.showScreenNetworkError();
                    }
                    else {
                        myReviewsView.hideLoadMore();
                        myReviewsView.showToastNetworkError();
                    }
                    isLoading = false;
                }

                @Override
                public void onAuthError() {
                    if (page == 1) {
                        myReviewsView.hideLoading();
                    }
                    else {
                        myReviewsView.hideLoadMore();
                    }
                    myReviewsView.showInvalideTokenError();
                    isLoading = false;
                }

                @Override
                public void onInvalidTokenError() {
                    if (page == 1) {
                        myReviewsView.hideLoading();
                    }
                    else {
                        myReviewsView.hideLoadMore();
                    }
                    isLoading = false;
                }

                @Override
                public void onServerError() {
                    if (page == 1) {
                        myReviewsView.hideLoading();
                    }
                    else {
                        myReviewsView.hideLoadMore();
                    }
                    myReviewsView.showServerError();
                    isLoading = false;
                }

                @Override
                public void onValidationError(String errorMessage) {
                    if (page == 1) {
                        myReviewsView.hideLoading();
                    }
                    else {
                        myReviewsView.hideLoadMore();
                    }
                    isLoading = false;
                }

                @Override
                public void onSuspendedUserError(String errorMessage) {
                    if (page == 1) {
                        myReviewsView.hideLoading();
                    }
                    else {
                        myReviewsView.hideLoadMore();
                    }
                    myReviewsView.showSuspededUserError(errorMessage);
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

    public interface MyReviewsView {
        void showLoading();
        void hideLoading();
        void showLoadMore();
        void hideLoadMore();
        void showInvalideTokenError();
        void showToastNetworkError();
        void showScreenNetworkError();
        void showNoData(User user);
        void showServerError();
        void showSuspededUserError(String errorMessage);
        void showMyReviews(ArrayList<Review> myReviews, User user);

    }
}
