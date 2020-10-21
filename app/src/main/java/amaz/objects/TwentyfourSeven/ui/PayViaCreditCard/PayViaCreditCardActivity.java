package amaz.objects.TwentyfourSeven.ui.PayViaCreditCard;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import amaz.objects.TwentyfourSeven.MApplication;
import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.data.models.CardPayRegisterationData;
import amaz.objects.TwentyfourSeven.utilities.Constants;
import amaz.objects.TwentyfourSeven.utilities.Fonts;
import amaz.objects.TwentyfourSeven.utilities.LanguageUtilities;
import amaz.objects.TwentyfourSeven.utilities.LocalSettings;

public class PayViaCreditCardActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView backIv;
    private WebView htmlHyperPayWv;
    private TextView creditCardTitleTv;
    private Fonts fonts;
    private LocalSettings localSettings;
    private CardPayRegisterationData cardPayData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localSettings = new LocalSettings(this);
        setLanguage();
        setContentView(R.layout.activity_pay_via_creditcard);
        getDataFromIntent();
        initialization();
        setFonts();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setLanguage();
    }

    private void initialization() {
        htmlHyperPayWv = findViewById(R.id.wv_html_hyperpay);
        creditCardTitleTv = findViewById(R.id.tv_credit_card_title);
        htmlHyperPayWv.getSettings().setJavaScriptEnabled(true);
        htmlHyperPayWv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        htmlHyperPayWv.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                // do your handling codes here, which url is the requested url
                // probably you need to open that url rather than redirect:
                Log.e("redirect", url);
                view.loadUrl(url);
                return false; // then it is not handled by default action
            }
        });
        htmlHyperPayWv.loadDataWithBaseURL(null, cardPayData.getHtml(),"text/html", null, null);

        backIv = findViewById(R.id.iv_back);
        backIv.setOnClickListener(this);
        if (localSettings.getLocale().equals(Constants.ARABIC)) {
            backIv.setImageResource(R.drawable.back_ar_ic);
        } else {
            backIv.setImageResource(R.drawable.back_ic);
        }

        fonts = MApplication.getInstance().getFonts();

    }

    private void setFonts() {
        creditCardTitleTv.setTypeface(fonts.customFont());
    }

    private void setLanguage() {

        if (localSettings.getLocale().equals(Constants.ARABIC)) {
            LanguageUtilities.switchToArabicLocale(this);
        }
    }

    private void getDataFromIntent() {
        cardPayData = (CardPayRegisterationData) getIntent().getSerializableExtra("card_pay_data");
    }

//    private String getWebViewData(){
//        return "<html>\n" +
//                "    \n" +
//                "    <head>\n" +
//                "            <script src=\"https://test.oppwa.com/v1/paymentWidgets.js?checkoutId="+checkoutId+"\"></script>\n" +
//                "            <style>\n" +
//                "                body {\n" +
//                "                    background-color:#f6f6f5;\n" +
//                "                }\n" +
//                "            </style>\n" +
//                "    </head>\n" +
//                "    <body>\n" +
//                "\n" +
//                "            <form action=\"https://247dev.objectsdev.com/payment-card/payment/callback/\" class=\"paymentWidgets\" data-brands=\"VISA MASTER AMEX\"></form>\n" +
//                "\n" +
//                "            <script>\n" +
//                "                var wpwlOptions = {style:\"card\"}            \n" +
//                "            </script>\n" +
//                "\n" +
//                "\n" +
//                "\n" +
//                "    </body>\n" +
//                "</html>";
//    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.iv_back){
            finish();
        }
    }
}
