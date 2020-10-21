package amaz.objects.TwentyfourSeven.ui.Categories;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import amaz.objects.TwentyfourSeven.data.models.ImageSizes;
import amaz.objects.TwentyfourSeven.injection.Injection;
import amaz.objects.TwentyfourSeven.listeners.OnRefeshTokenResponse;
import amaz.objects.TwentyfourSeven.ui.CategoryStores.CategoryStoresActivity;
import amaz.objects.TwentyfourSeven.ui.MyAccount.MainActivity;
import amaz.objects.TwentyfourSeven.utilities.Constants;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.rey.material.widget.ProgressView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

import amaz.objects.TwentyfourSeven.BaseFragment;
import amaz.objects.TwentyfourSeven.MApplication;
import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.adapters.CategoriesAdapter;
import amaz.objects.TwentyfourSeven.data.models.Category;
import amaz.objects.TwentyfourSeven.listeners.OnCategoryClickListener;
import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import amaz.objects.TwentyfourSeven.presenter.PresenterFactory;
import amaz.objects.TwentyfourSeven.utilities.Fonts;
import amaz.objects.TwentyfourSeven.utilities.LocalSettings;
import amaz.objects.TwentyfourSeven.utilities.TokenUtilities;

public class CategoriesFragment extends BaseFragment implements OnRefeshTokenResponse,CategoriesPresenter.CategoriesView, OnCategoryClickListener, View.OnClickListener {

    private TextView activeStoresTitleTv, searchTv, noConnectionTitleTv, noConnectionMessageTv;
    private ImageView bannerIv;
    private Button requestPickUpBtn, reloadBtn;
    private RecyclerView categoriesRv;
    private ProgressView loaderPv;
    private LinearLayout mainContentLl, noConnectionLl;

    private CategoriesAdapter adapter;
    private ArrayList<Category> allCategories = new ArrayList<>();

    private LocalSettings localSettings;
    private Fonts fonts;

    private CategoriesPresenter categoriesPresenter;

    @Override
    public void onPresenterAvailable(BasePresenter presenter) {
        categoriesPresenter = (CategoriesPresenter) presenter;
        if (!CategoriesFragment.this.isDetached() && getActivity() != null) {
            categoriesPresenter.setView(this,getActivity());
            if (allCategories.isEmpty()){
                categoriesPresenter.getAllCategories(localSettings.getLocale());
            }
        }
    }

    @Override
    public PresenterFactory.PresenterType getPresenterType() {
        return PresenterFactory.PresenterType.Categories;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        initializeView(view);
        setFonts();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    private void initializeView(View view) {
        localSettings = new LocalSettings(getActivity());
        mainContentLl = view.findViewById(R.id.ll_main_content);
        activeStoresTitleTv = view.findViewById(R.id.tv_active_stores_title);
        bannerIv = view.findViewById(R.id.iv_banner);
        searchTv = view.findViewById(R.id.tv_stores_search);
        searchTv.setOnClickListener(this);
        requestPickUpBtn = view.findViewById(R.id.btn_request_pickup);
        requestPickUpBtn.setOnClickListener(this);
        categoriesRv = view.findViewById(R.id.rv_categories);
        categoriesRv.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        adapter = new CategoriesAdapter(getActivity(), allCategories, this);
        categoriesRv.setAdapter(adapter);
        loaderPv = view.findViewById(R.id.pv_load);

        noConnectionLl = view.findViewById(R.id.ll_no_connection);
        noConnectionTitleTv = view.findViewById(R.id.tv_no_connection_title);
        noConnectionMessageTv = view.findViewById(R.id.tv_no_connection_message);
        reloadBtn = view.findViewById(R.id.btn_reload_page);
        reloadBtn.setOnClickListener(this);
    }

    private void setFonts() {
        fonts = MApplication.getInstance().getFonts();
        activeStoresTitleTv.setTypeface(fonts.customFontBD());
        searchTv.setTypeface(fonts.customFont());
        requestPickUpBtn.setTypeface(fonts.customFontBD());
        noConnectionTitleTv.setTypeface(fonts.customFontBD());
        noConnectionMessageTv.setTypeface(fonts.customFont());
        reloadBtn.setTypeface(fonts.customFontBD());
    }


    @Override
    public void showLoading() {
        if (!CategoriesFragment.this.isDetached() && getActivity() != null) {
            loaderPv.setVisibility(View.VISIBLE);
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    @Override
    public void hideLoading() {
        if (!CategoriesFragment.this.isDetached() && getActivity() != null) {
            loaderPv.setVisibility(View.GONE);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    @Override
    public void showNetworkError() {
        if (!CategoriesFragment.this.isDetached() && getActivity() != null) {
            mainContentLl.setVisibility(View.GONE);
            noConnectionLl.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showServerError() {
        if (!CategoriesFragment.this.isDetached() && getActivity() != null) {
            Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void showNoData() {
        if (!CategoriesFragment.this.isDetached() && getActivity() != null) {
            Toast.makeText(getActivity(), R.string.no_data_error, Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void showBannerImage(final ImageSizes banner) {
        if (!CategoriesFragment.this.isDetached() && getActivity() != null) {
            mainContentLl.setVisibility(View.VISIBLE);
            noConnectionLl.setVisibility(View.GONE);
            //Picasso.with(getActivity()).load(banner.getBanner()).placeholder(R.drawable.louding_img).into(bannerIv);
            Glide.with(this).load(banner.getBanner())
                    .apply(new RequestOptions()
                            .dontAnimate().placeholder(R.drawable.louding_img))
                    .into(bannerIv);
        }
    }

    @Override
    public void showCategoriesData(ArrayList<Category> categories) {
        if (!CategoriesFragment.this.isDetached() && getActivity() != null) {
            localSettings.setCategories(categories);
            noConnectionLl.setVisibility(View.GONE);
            allCategories.clear();
            allCategories.addAll(categories);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void saveDefaultCategory(Category category) {
        if (!CategoriesFragment.this.isDetached() && getActivity() != null) {
            localSettings.setDefaultCategory(category);
        }
    }

    @Override
    public void showSuspededUserError(String errorMessage) {
        if (!CategoriesFragment.this.isDetached() && getActivity() != null) {

            Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
            localSettings.removeToken();
            localSettings.removeRefreshToken();
            localSettings.removeUser();
            ((MainActivity) getActivity()).switchToRegisterationOrLogin();
        }
    }

    @Override
    public void onCategoryClick(Category category) {
        if (!CategoriesFragment.this.isDetached() && getActivity() != null) {
            ((MainActivity) getActivity()).switchToCategoryStores(category);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_reload_page:
                categoriesPresenter.getAllCategories(localSettings.getLocale());
                break;

            case R.id.tv_stores_search:
                if (!CategoriesFragment.this.isDetached() && getActivity() != null) {
                    ((MainActivity) getActivity()).switchToSearch();
                }
                break;

            case R.id.btn_request_pickup:
                if (!CategoriesFragment.this.isDetached() && getActivity() != null) {
                    if (localSettings.getToken() != null){
                        ((MainActivity) getActivity()).switchToMapSelectDestination();
                    }
                    else {
                        ((MainActivity) getActivity()).switchToRegisterationOrLogin();
                    }
                }
                break;
        }

    }
    @Override
    public void showInvalideTokenError() {
        Log.e("authError", "authentication error");
        TokenUtilities.refreshToken(getActivity(),
                Injection.provideUserRepository(),
                localSettings.getToken(),
                localSettings.getLocale(),
                localSettings.getRefreshToken(),
                this);

    }

    @Override
    public void hideTokenLoader() {
        if (!CategoriesFragment.this.isDetached() && getActivity() != null) {

            hideLoading();
        }
    }

}
