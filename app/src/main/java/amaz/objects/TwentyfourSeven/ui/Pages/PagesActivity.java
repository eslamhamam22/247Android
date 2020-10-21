package amaz.objects.TwentyfourSeven.ui.Pages;

import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rey.material.widget.ProgressView;
import com.squareup.picasso.Picasso;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import amaz.objects.TwentyfourSeven.BaseActivity;
import amaz.objects.TwentyfourSeven.MApplication;
import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.data.models.PageData;
import amaz.objects.TwentyfourSeven.presenter.BasePresenter;
import amaz.objects.TwentyfourSeven.presenter.PresenterFactory;
import amaz.objects.TwentyfourSeven.utilities.Constants;
import amaz.objects.TwentyfourSeven.utilities.Fonts;
import amaz.objects.TwentyfourSeven.utilities.LanguageUtilities;
import amaz.objects.TwentyfourSeven.utilities.LocalSettings;

public class PagesActivity extends BaseActivity implements PagesPresenter.PagesView, View.OnClickListener {

    private RelativeLayout titleRl,titleRl2;
    private TextView pageTitleTv, pageTitleTv2, pageContentTv;
    private HtmlTextView pageContentHtv;
    private ImageView backIv, backIv2, contentIv;
    private ProgressView loaderPv;

    private TextView noConnectionTitleTv, noConnectionMessageTv;
    private LinearLayout noConnectionLl;
    private Button reloadBtn;

    private LocalSettings localSettings;
    private Fonts fonts;

    private PagesPresenter pagesPresenter;
    private String page;
    private PageData pageData;

    @Override
    public void onPresenterAvailable(BasePresenter presenter) {
        pagesPresenter = (PagesPresenter) presenter;
        pagesPresenter.setView(this);
        if (pageData == null){
            pagesPresenter.getPageData(localSettings.getLocale(),page);
        }

    }

    @Override
    public PresenterFactory.PresenterType getPresenterType() {
        return PresenterFactory.PresenterType.Page;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localSettings = new LocalSettings(this);
        setLanguage();
        setContentView(R.layout.activity_pages);
        getDataFromIntent();
        initialization();
        setFonts();
    }

    private void getDataFromIntent(){
        page = getIntent().getStringExtra(Constants.PAGE);
    }

    private void setLanguage() {
        if (localSettings.getLocale().equals(Constants.ARABIC)) {
            LanguageUtilities.switchToArabicLocale(this);
        }
    }

    private void initialization(){
        titleRl = findViewById(R.id.rl_title);
        titleRl2 = findViewById(R.id.rl_title_2);
        pageTitleTv = findViewById(R.id.tv_page_title);
        pageTitleTv2 = findViewById(R.id.tv_page_title_2);
        backIv = findViewById(R.id.iv_back);
        backIv2 = findViewById(R.id.iv_back_2);
        backIv.setOnClickListener(this);
        backIv2.setOnClickListener(this);

        pageContentTv = findViewById(R.id.tv_page_content);
        pageContentHtv = findViewById(R.id.wv_page_content);
        pageContentTv.setMovementMethod(new ScrollingMovementMethod());
        contentIv = findViewById(R.id.iv_content);
        loaderPv = findViewById(R.id.pv_load);

        if (localSettings.getLocale().equals(Constants.ARABIC)){
            backIv2.setImageResource(R.drawable.back_ar_red_ic);
            backIv.setImageResource(R.drawable.back_ar_ic);
        }
        else {
            backIv2.setImageResource(R.drawable.back_red_ic);
            backIv.setImageResource(R.drawable.back_ic);
        }
        if (page.equals(Constants.ABOUT_US)){
            titleRl2.setVisibility(View.GONE);
            titleRl.setVisibility(View.VISIBLE);
            contentIv.setVisibility(View.VISIBLE);
            /*try {
                PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                versionTv.setText(getString(R.string.version)+" "+pInfo.versionName);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            copyRightTv.setVisibility(View.VISIBLE);*/
        }
        else {
            titleRl2.setVisibility(View.VISIBLE);
            titleRl.setVisibility(View.GONE);
            contentIv.setVisibility(View.GONE);
        }

        noConnectionLl = findViewById(R.id.ll_no_connection);
        noConnectionTitleTv = findViewById(R.id.tv_no_connection_title);
        noConnectionMessageTv = findViewById(R.id.tv_no_connection_message);
        reloadBtn = findViewById(R.id.btn_reload_page);
        reloadBtn.setOnClickListener(this);
    }

    private void setFonts(){
        fonts = MApplication.getInstance().getFonts();
        pageTitleTv.setTypeface(fonts.customFontBD());
        pageTitleTv2.setTypeface(fonts.customFontBD());
        pageContentTv.setTypeface(fonts.customFont());
        pageContentHtv.setTypeface(fonts.customFont());

        noConnectionTitleTv.setTypeface(fonts.customFontBD());
        noConnectionMessageTv.setTypeface(fonts.customFont());
        reloadBtn.setTypeface(fonts.customFontBD());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_back_2:
                finish();
                break;

            case R.id.btn_reload_page:
                pagesPresenter.getPageData(localSettings.getLocale(),page);
                break;
        }
    }

    @Override
    public void showLoading() {
        loaderPv.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public void hideLoading() {
        loaderPv.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public void showPageNotFoundError() {
        Toast.makeText(this, R.string.no_data_error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showNetworkError() {
        noConnectionLl.setVisibility(View.VISIBLE);
    }

    @Override
    public void showServerError() {
        Toast.makeText(this, R.string.server_error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showPageData(PageData pageData) {
        if (noConnectionLl.getVisibility() == View.VISIBLE) {
            noConnectionLl.setVisibility(View.GONE);
        }
        this.pageData = pageData;
        pageTitleTv.setText(pageData.getName());
        pageTitleTv2.setText(pageData.getName());
        if (pageData.getContent() != null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                pageContentTv.setText(Html.fromHtml(pageData.getContent(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                pageContentTv.setText(Html.fromHtml(pageData.getContent()));
            }
            pageContentHtv.setHtml(pageData.getContent());
        }
        if (pageData.getImage() != null){
            Picasso.with(this).load(pageData.getImage().getBanner()).into(contentIv);
        }
        else {
            contentIv.setVisibility(View.GONE);
        }

    }


}
