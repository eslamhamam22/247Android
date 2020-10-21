package amaz.objects.TwentyfourSeven.ui.AboutUs;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import amaz.objects.TwentyfourSeven.MApplication;
import amaz.objects.TwentyfourSeven.R;
import amaz.objects.TwentyfourSeven.utilities.Constants;
import amaz.objects.TwentyfourSeven.utilities.Fonts;
import amaz.objects.TwentyfourSeven.utilities.LanguageUtilities;
import amaz.objects.TwentyfourSeven.utilities.LocalSettings;

public class AboutUsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView aboutUsTitleTv;
    private ImageView backIv;

    private LocalSettings localSettings;
    private Fonts fonts;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localSettings = new LocalSettings(this);
        setLanguage();
        setContentView(R.layout.activity_about_us);
        initialization();
        setFonts();
    }

    private void setLanguage() {
        if (localSettings.getLocale().equals(Constants.ARABIC)) {
            LanguageUtilities.switchToArabicLocale(this);
        }
    }

    private void initialization(){
        aboutUsTitleTv = findViewById(R.id.tv_about_us_title);
        backIv = findViewById(R.id.iv_back);
        backIv.setOnClickListener(this);

        if (localSettings.getLocale().equals(Constants.ARABIC)){
            backIv.setImageResource(R.drawable.back_ar_ic);
        }
        else {
            backIv.setImageResource(R.drawable.back_ic);
        }
    }

    private void setFonts(){
        fonts = MApplication.getInstance().getFonts();
        aboutUsTitleTv.setTypeface(fonts.customFontBD());
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_back){
            finish();
        }
    }

}
