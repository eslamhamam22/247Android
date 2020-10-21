package amaz.objects.TwentyfourSeven;


import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import amaz.objects.TwentyfourSeven.presenter.PresenterFactory;
import amaz.objects.TwentyfourSeven.presenter.PresenterLoader;

public abstract class BaseFragment extends Fragment implements LoaderManager.LoaderCallbacks<BasePresenter> {

    private static final int LOADER_ID = 101;
    private BasePresenter presenter;

    public abstract void onPresenterAvailable(BasePresenter presenter);
    public abstract PresenterFactory.PresenterType getPresenterType();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LoaderManager.getInstance(this).initLoader(LOADER_ID,null,this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter != null){
            presenter.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (presenter != null){
            presenter.onPause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null){
            presenter.onDestroy();
        }
    }

    @NonNull
    @Override
    public Loader<BasePresenter> onCreateLoader(int i, @Nullable Bundle bundle) {
        if (getActivity() != null){
            return new PresenterLoader(getActivity(),getPresenterType());
        }
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<BasePresenter> loader, BasePresenter basePresenter) {
        presenter = basePresenter;
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                onPresenterAvailable(presenter);
            }
        });
    }

    @Override
    public void onLoaderReset(@NonNull Loader<BasePresenter> loader) {
        this.presenter = null;
    }
}
