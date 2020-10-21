package amaz.objects.TwentyfourSeven.utilities;

import android.content.Context;
import android.graphics.Typeface;

public class Fonts {

    private Typeface customFontArabic, customFontArabicBD, customFontEnglish, customFontEnglishBD;
    private Context context;
    private LocalSettings localSettings;

    public Fonts(Context context) {
        this.context = context;
        initializeFonts();
    }

    private void initializeFonts(){
        localSettings = new LocalSettings(context);
        customFontArabic = Typeface.createFromAsset(context.getAssets(),"fonts/Bahij_TheSansArabic-Plain.ttf");
        customFontArabicBD = Typeface.createFromAsset(context.getAssets(),"fonts/Bahij_TheSansArabic-Bold.ttf");
        customFontEnglish = Typeface.createFromAsset(context.getAssets(),"fonts/Montserrat-Regular.ttf");
        customFontEnglishBD = Typeface.createFromAsset(context.getAssets(),"fonts/Montserrat-Bold.ttf");
    }

    public Typeface customFont(){
        if (localSettings.getLocale().equals(Constants.ARABIC)){
            return customFontArabic;
        }
        else {
            return customFontEnglish;
        }
    }

    public Typeface customFontBD(){
        if (localSettings.getLocale().equals(Constants.ARABIC)){
            return customFontArabicBD;
        }
        else {
            return customFontEnglishBD;
        }
    }
}
