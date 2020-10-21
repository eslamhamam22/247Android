package amaz.objects.TwentyfourSeven.ui.Categories;

import android.content.Context;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import amaz.objects.TwentyfourSeven.data.models.BlockedArea;
import amaz.objects.TwentyfourSeven.data.models.Category;
import amaz.objects.TwentyfourSeven.data.models.ImageSizes;
import amaz.objects.TwentyfourSeven.data.models.responses.BlockedAreasResponse;
import amaz.objects.TwentyfourSeven.data.models.responses.CategoriesResponse;
import amaz.objects.TwentyfourSeven.data.repositories.GeneralRepository;
import amaz.objects.TwentyfourSeven.listeners.OnResponseListener;
import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import amaz.objects.TwentyfourSeven.utilities.LocalSettings;
import retrofit2.Response;

public class CategoriesPresenter extends BasePresenter {

    private GeneralRepository repository;
    private Context context;
    private LocalSettings localSettings;
    private WeakReference<CategoriesView> view = new WeakReference<>(null);

    public CategoriesPresenter(GeneralRepository repository){
        this.repository = repository;
    }

    public void setView(CategoriesView view,Context context){
        this.view = new WeakReference<>(view);
        this.context = context;
        localSettings = new LocalSettings(context);

    }

    public void getAllCategories(final String locale){
        final CategoriesView categoriesView = this.view.get();
        categoriesView.showLoading();
        repository.getAllCategories(locale, new OnResponseListener() {
            @Override
            public void onSuccess(Response response) {
                CategoriesResponse categoriesResponse = (CategoriesResponse) response.body();
             //   if (localSettings.getToken() != null){
                    getBlockedAreas(locale,categoriesResponse);
//                }else {
//                    categoriesView.hideLoading();
//
//                    if (categoriesResponse.getData().getBanner() != null) {
//                        categoriesView.showBannerImage(categoriesResponse.getData().getBanner().getImage());
//                    }
//                    if (categoriesResponse.getData().getCategories().isEmpty()) {
//                        categoriesView.showNoData();
//                    } else {
//                        categoriesView.saveDefaultCategory(categoriesResponse.getData().getDefaultCategory());
//                        categoriesView.showCategoriesData(categoriesResponse.getData().getCategories());
//                    }
//                }
            }

            @Override
            public void onFailure() {
                categoriesView.hideLoading();
                categoriesView.showNetworkError();
            }

            @Override
            public void onAuthError() {

            }

            @Override
            public void onInvalidTokenError() {

            }

            @Override
            public void onServerError() {
                categoriesView.hideLoading();
                categoriesView.showServerError();
            }

            @Override
            public void onValidationError(String errorMessage) {

            }

            @Override
            public void onSuspendedUserError(String errorMessage) {

            }
        });
    }
    public void getBlockedAreas(final String locale, final CategoriesResponse categoriesResponse ){
        final CategoriesView categoriesView = this.view.get();
        repository.getAllBlockedArea(locale, new OnResponseListener() {
            @Override
            public void onSuccess(Response response) {
                categoriesView.hideLoading();
                BlockedAreasResponse blockedAreasResponse = (BlockedAreasResponse) response.body();
                localSettings.setBlockedAreas(blockedAreasResponse.getData().getBlocked_areas());
                localSettings.setMaxMinusAmount(blockedAreasResponse.getData().getMax_negative_delegate_wallet());
                localSettings.setAppShareLink(blockedAreasResponse.getData().getAppLink());
                //Log.e("applink", localSettings.getAppShareLink());
                //Log.d("min_amount",blockedAreasResponse.getData().getMax_negative_delegate_wallet()+"");
                if (categoriesResponse.getData().getBanner() != null && categoriesResponse.getData().getBanner().getImage() != null){
                    categoriesView.showBannerImage(categoriesResponse.getData().getBanner().getImage());
                }
                if (categoriesResponse.getData().getCategories().isEmpty()){
                    categoriesView.showNoData();
                }
                else {
                    categoriesView.saveDefaultCategory(categoriesResponse.getData().getDefaultCategory());
                    categoriesView.showCategoriesData(categoriesResponse.getData().getCategories());
                }
            }

            @Override
            public void onFailure() {
                categoriesView.hideLoading();
                categoriesView.showNetworkError();
            }

            @Override
            public void onAuthError() {
                categoriesView.hideLoading();
                categoriesView.showInvalideTokenError();
            }

            @Override
            public void onInvalidTokenError() {
                categoriesView.hideLoading();
            }



            @Override
            public void onServerError() {
                categoriesView.hideLoading();
                categoriesView.showServerError();
            }

            @Override
            public void onValidationError(String errorMessage) {
                categoriesView.hideLoading();

            }

            @Override
            public void onSuspendedUserError(String errorMessage) {
                categoriesView.hideLoading();
                categoriesView.showSuspededUserError(errorMessage);

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

    public interface CategoriesView {
        void showLoading();
        void hideLoading();
        void showNetworkError();
        void showServerError();
        void showNoData();
        void showBannerImage(ImageSizes banner);
        void showCategoriesData(ArrayList<Category> allCategories);
        void saveDefaultCategory(Category category);
        void showSuspededUserError(String errorMessage);
        void showInvalideTokenError();
    }
}
