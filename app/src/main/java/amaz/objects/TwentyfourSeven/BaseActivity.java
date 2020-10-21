package amaz.objects.TwentyfourSeven;

import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.appcompat.app.AppCompatActivity;

import amaz.objects.TwentyfourSeven.data.repositories.NetworkUserRepository;
import amaz.objects.TwentyfourSeven.data.repositories.UserRepository;
import amaz.objects.TwentyfourSeven.presenter.PresenterFactory;
import amaz.objects.TwentyfourSeven.utilities.LocalSettings;

import amaz.objects.TwentyfourSeven.data.models.responses.ProfileResponse;
import amaz.objects.TwentyfourSeven.listeners.OnResponseListener;
import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import amaz.objects.TwentyfourSeven.presenter.PresenterLoader;
import retrofit2.Response;

public abstract class BaseActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<BasePresenter> {

    private static final int LOADER_ID = 101;
    private BasePresenter presenter;
    private UserRepository userRepo = new NetworkUserRepository();

    public abstract void onPresenterAvailable(BasePresenter presenter);
    public abstract PresenterFactory.PresenterType getPresenterType();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoaderManager.getInstance(this).initLoader(LOADER_ID,null,this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (presenter != null){
            presenter.onResume();
            getUserProfile();
        }
        MApplication.activityResumed();


    }

    @Override
    protected void onPause() {
        super.onPause();
        if (presenter != null){
            presenter.onPause();
        }
        MApplication.activityPaused();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null){
            presenter.onDestroy();
        }
    }
    public void getUserProfile(){
        // accountDetailsView.showLoading();
        final LocalSettings localSettings = new LocalSettings(this);
        userRepo.getUserProfile(localSettings.getToken(), localSettings.getLocale(),  new OnResponseListener() {
            @Override
            public void onSuccess(Response response) {
                if ((((ProfileResponse) response.body()).getData().getUnseen_notifications_count() >0)){
                    localSettings.setIsHAsNotificationUnSeen(true);
                }
                localSettings.setUser(((ProfileResponse) response.body()).getData());

            }

            @Override
            public void onFailure() {
            }

            @Override
            public void onAuthError() {

            }

            @Override
            public void onInvalidTokenError() {

            }

            @Override
            public void onServerError() {

            }

            @Override
            public void onValidationError(String errorMessage) {

            }

            @Override
            public void onSuspendedUserError(String errorMessage) {

            }
        });
    }
    @NonNull
    @Override
    public Loader<BasePresenter> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new PresenterLoader(this,getPresenterType());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<BasePresenter> loader, final BasePresenter basePresenter) {
        this.presenter = basePresenter;
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                onPresenterAvailable(basePresenter);
            }
        });
    }

    @Override
    public void onLoaderReset(@NonNull Loader<BasePresenter> loader) {
        this.presenter = null;
    }

}
