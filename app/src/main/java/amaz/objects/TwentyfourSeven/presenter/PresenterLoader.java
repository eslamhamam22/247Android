package amaz.objects.TwentyfourSeven.presenter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.loader.content.Loader;

public class PresenterLoader extends Loader<BasePresenter> {

    private PresenterFactory.PresenterType presenterType;
    private BasePresenter presenter;

    public PresenterLoader(@NonNull Context context, PresenterFactory.PresenterType type) {
        super(context);
        presenterType = type;
    }

    @Override
    protected void onStartLoading() {
        if (presenter != null){
            deliverResult(presenter);
        }
        forceLoad();
    }

    @Override
    protected void onForceLoad() {
        presenter = PresenterFactory.create(presenterType);
        deliverResult(presenter);
    }

    @Override
    protected void onReset() {
        presenter = null;
    }

}
